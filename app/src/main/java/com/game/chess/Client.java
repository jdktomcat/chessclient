package com.game.chess;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.Handler;
import android.os.Message;

public class Client implements Runnable {
    public Handler clientHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ON_ASK_PEACE:
                    clientListener.onAskPeace();
                    break;
                case ON_START:
                    clientListener.onStart();
                    break;
                case ON_CONECT_FAILED:
                    clientListener.onConnectFailed();
                    break;
                case ON_RECEIVE_MSG:
                    clientListener.onReceiveMsg(msg.obj.toString());
                    break;
                case ON_LOGIN_SUCCESS:
                    clientListener.onLoginSuccess();
                    break;
                case ON_LOGIN_FAILED:
                    clientListener.onLoginFailed();
                    break;
                case ON_GAMEOVER:
                    String tag[] = msg.obj.toString().split(";");
                    clientListener.onGameOver(Integer.parseInt(tag[0]), tag[1]);
                    break;
                case ON_FAILED:
                    clientListener.onFailed(msg.obj.toString());
                    break;
                case ON_DELINE:
                    clientListener.onDeLine();
                    break;
                case ON_CONNECT:
                    clientListener.onConnect();
                    break;
                case ON_ASK_ROLLBACK:
                    clientListener.onAskRollback();
                    break;
                case ON_UPDATE:
                    clientListener.onUpdate();
                    break;
                case ON_SIGN_FAILED:
                    clientListener.onSignFailed();
                    break;
                case ON_EAT:
                    clientListener.onEat();
                    break;
                case ON_MOVE:
                    clientListener.onMove();
                    break;
                default:
                    break;
            }
        }
    };
    public static final int ON_ASK_PEACE = 1;
    public static final int ON_ASK_ROLLBACK = 2;
    public static final int ON_CONNECT = 3;
    public static final int ON_DELINE = 4;
    public static final int ON_FAILED = 5;
    public static final int ON_GAMEOVER = 6;
    public static final int ON_LOGIN_FAILED = 7;
    public static final int ON_LOGIN_SUCCESS = 8;
    public static final int ON_RECEIVE_MSG = 9;
    public static final int ON_CONECT_FAILED = 10;
    public static final int ON_START = 11;
    public static final int ON_UPDATE = 12;
    public static final int ON_SIGN_FAILED = 13;
    public static final int ON_EAT = 14;
    public static final int ON_MOVE = 15;

    public interface ClientListener {
        public void onAskPeace();

        public void onSignFailed();

        public void onAskRollback();

        public void onConnect();

        public void onDeLine();

        public void onFailed(String msg);

        public void onGameOver(int parseInt, String string);

        public void onLoginFailed();

        public void onLoginSuccess();

        public void onReceiveMsg(String content);

        public void onUpdate();

        public void onConnectFailed();

        public void onStart();

        public void onEat();

        public void onMove();
    }

    private ExecutorService es;
    // 游戏实例
    private Game game;
    private User user;
    private Socket socket;
    private InputStream is;
    private OutputStream os;
    private String ip;
    private int port;
    private ClientListener clientListener;
    private boolean isConnected;

    public Client(String ip, int port, ClientListener clientListener) {
        this.ip = ip;
        this.port = port;
        this.clientListener = clientListener;
        es = Executors.newFixedThreadPool(5);
        game = new Game();
    }

    public void connect() {
        if (!isConnected) {
            isConnected = true;
            es.execute(this);
        }

    }

    public void setUser(User user) {
        this.user = user;
    }

    /**
     * 同意求和
     */
    public void agreePeace() {
        sendLine("agreepeace:1");
    }

    /**
     * 同意悔棋
     */
    public void agreeRollBack() {
        sendLine("agreerollback:1");
    }

    /**
     * 求和
     */
    public void askPeace() {
        sendLine("askpeace:1");
    }

    /**
     * 悔棋
     */
    public void askRollBack() {
        sendLine("askrollback");
    }

    /**
     * 心跳包
     */
    public void askTime() {
        sendLine("asktime");
    }

    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用户掉线
     */
    private void deLine() {
        clientHandler.sendEmptyMessage(ON_DELINE);
        isConnected = false;
    }

    /**
     * 比较两个Client是否相同
     */
    @Override
    public boolean equals(Object obj) {
        try {
            return ((Client) obj).user.equals(user);
        } catch (Exception e) {
            return false;
        }
    }

    public Game getGame() {
        return game;
    }

    public User getUser() {
        return user;
    }

    /**
     * 登陆
     */
    public void login() {
        sendLine("login:" + user.toString());
    }

    /**
     * 登陆
     *
     * @param content
     */
    private void loginBack(String content) {
        try {
            user = User.fromString(content);
            clientHandler.sendEmptyMessage(ON_LOGIN_SUCCESS);
        } catch (Exception e) {
            clientHandler.sendEmptyMessage(ON_LOGIN_FAILED);
        }
    }

    /**
     * 请求和棋
     */
    private void receiveAskPeace() {
        clientHandler.sendEmptyMessage(ON_ASK_PEACE);
    }

    /**
     * 请求悔棋
     */
    private void receiveAskRollback() {
        clientHandler.sendEmptyMessage(ON_ASK_ROLLBACK);
    }

    /**
     * content：服务器传出的错误（服务器超载，服务器更新，异地登陆等等）
     *
     * @param content
     */
    private void receiveFailed(String content) {
        Message msg = new Message();
        msg.what = ON_FAILED;
        msg.obj = content;
        clientHandler.sendMessage(msg);
    }

    private void receiveGameData(String content) {
        int[][] map1 = game.getMap();
        try {
            game.setData(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int[][] map2 = game.getMap();
        switch (GameUtil.getWalkMode(map1, map2)) {
            case GameUtil.EAT:
                clientHandler.sendEmptyMessage(ON_EAT);
                break;
            case GameUtil.MOVE:
                clientHandler.sendEmptyMessage(ON_MOVE);
                break;

        }
        clientHandler.sendEmptyMessage(ON_UPDATE);
    }

    /**
     * 游戏结束
     *
     * @param content
     */
    private void receiveGameOver(String content) {
        // Content:user(0/1/2);reason;allwalks;
        String tag[] = content.split(";");
        try {
            game.setWalks(tag[2]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Message msg = new Message();
        msg.what = ON_GAMEOVER;
        msg.obj = content;
        clientHandler.sendMessage(msg);

    }

    /**
     * content:收到对方的消息
     *
     * @param content
     */
    private void receiveMsg(String content) {
        Message msg = new Message();
        msg.what = ON_RECEIVE_MSG;
        msg.obj = content;
        clientHandler.sendMessage(msg);
    }

    /**
     * 收到服务器传出的时间
     *
     * @param content
     */
    private void receiveTime(String content) {
        try {
            game.setTime(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void receiveUser(String content) {
        clientHandler.sendEmptyMessage(ON_START);
        try {
            game.setUser(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            try {
                socket = new Socket();
                socket.connect(new InetSocketAddress(ip, port), 2000);
                startAskTime();
                clientHandler.sendEmptyMessage(ON_CONNECT);
            } catch (Exception e) {
                clientHandler.sendEmptyMessage(ON_CONECT_FAILED);
                return;
            }

            is = socket.getInputStream();
            BufferedReader bd = new BufferedReader(new InputStreamReader(is,
                    "utf8"));
            String data = null;
            while ((data = bd.readLine()) != null) {
                String tag[] = data.split(":");
                String cmd = tag[0];
                String content = null;
                if (tag.length == 2) {
                    content = tag[1];
                }
                if (cmd.endsWith("msg")) {
                    receiveMsg(content);
                } else if (cmd.endsWith("askpeace")) {
                    receiveAskPeace();
                } else if (cmd.endsWith("askrollback")) {
                    receiveAskRollback();
                } else if (cmd.endsWith("failed")) {
                    receiveFailed(content);
                } else if (cmd.endsWith("time")) {
                    receiveTime(content);
                } else if (cmd.endsWith("gameover")) {
                    receiveGameOver(content);
                } else if (cmd.endsWith("user")) {
                    receiveUser(content);
                } else if (cmd.endsWith("loginback")) {
                    loginBack(content);
                } else if (cmd.endsWith("signback")) {
                    signBack(content);
                } else if (cmd.endsWith("game")) {
                    receiveGameData(content);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        deLine();

    }

    /**
     * 传入数据字符串data，通过用户连接向用户发送字符串data+"\r\n"。
     *
     * @param data
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    public synchronized void sendLine(final String data) {
        es.execute(new Runnable() {
            public void run() {
                try {
                    os = socket.getOutputStream();
                    os.write((data + "\r\n").getBytes("utf8"));
                } catch (Exception e) {
                    // 发送失败，说明用户掉线
                    deLine();
                }
            }
        });
    }

    /**
     * 注册
     */
    public void sign() {
        sendLine("sign:" + user.toString());
    }

    public void sendChatMsg(ChatMessage cm) {
        sendLine("msg:" + cm.toString());
    }

    /**
     * 注册也是登陆，只不过多添加了一些数据
     *
     * @param content
     */
    private void signBack(String content) {
        try {
            user = User.fromString(content);
            clientHandler.sendEmptyMessage(ON_LOGIN_SUCCESS);

        } catch (Exception e) {
            clientHandler.sendEmptyMessage(ON_SIGN_FAILED);

        }
    }

    /**
     * 走棋
     *
     * @param walk
     * @throws Exception
     */
    public void walk(Walk walk) {
        sendLine("walk:" + walk.toString());
    }

    public boolean isBlack() {
        if (user.equals(game.getUser1())) {
            return true;
        }
        return false;
    }

    public void findGame() {
        sendLine("findgame");
    }

    public void cancleFind() {
        sendLine("canclefind");
    }

    public void startAskTime() {
        es.execute(new Runnable() {
            @Override
            public void run() {
                while (socket.isConnected()) {
                    sendLine("asktime");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void giveUp() {
        sendLine("giveup:j");
    }
}
