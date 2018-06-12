package com.xmg.chinachess.activity;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chinachess.R;

import com.xmg.chinachess.ChatMessage;
import com.xmg.chinachess.Client;
import com.xmg.chinachess.Client.ClientListener;
import com.xmg.chinachess.GameView;
import com.xmg.chinachess.GameView.GameViewListener;
import com.xmg.chinachess.Img;
import com.xmg.chinachess.Mp3;
import com.xmg.chinachess.User;
import com.xmg.chinachess.Walk;

import java.util.ArrayList;

public class MainActivity extends Activity {
    private ArrayList<ChatMessage> chatMessages = new ArrayList<ChatMessage>();

    class MyClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            Mp3.select.start();
            if (v == loginBtnLogin) {
                String name = loginEditName.getText().toString();
                String pwd = loginEditPassword.getText().toString();
                localUser = new User();
                localUser.setName(name);
                localUser.setPassword(pwd);
                client.setUser(localUser);
                client.login();
                loginBtnLogin.setEnabled(false);
                loginBtnSign.setEnabled(false);
            } else if (v == loginBtnSign) {
                signView();
            } else if (v == btnFindGame) {
                if (isFindding) {
                    client.cancleFind();
                    btnFindGame.setText("匹配游戏");
                    isFindding = false;
                } else {
                    client.findGame();
                    btnFindGame.setText("取消匹配");
                    isFindding = true;
                }
            } else if (v == signBtnSign) {
                String name = signEditName.getText().toString();
                String password = signEditPassword.getText().toString();
                String repassword = signEditRepassword.getText().toString();
                if ("".equals(name)) {
                    toastMsg("名字不能为空！");
                    return;
                }
                if ("".equals(password)) {
                    toastMsg("密码不能为空！");
                    return;
                }
                if (!password.equals(repassword)) {
                    toastMsg("密码和重复密码不一致！");
                    return;
                }
                localUser = new User(name, password, headPic + "");
                client.setUser(localUser);
                signBtnSign.setEnabled(false);
                signBtnBack.setEnabled(false);
                client.sign();
            } else if (v == signBtnBack) {
                loginView();
            }
            if (((Button) v).getText().toString().equals("上一张")) {
                headPic--;
                if (headPic < 1) {
                    headPic = 14;
                }
                signHead.setImageBitmap(Img.getHead(headPic));
            } else if (((Button) v).getText().toString().equals("下一张")) {
                headPic++;
                if (headPic > 14) {
                    headPic = 1;
                }
                signHead.setImageBitmap(Img.getHead(headPic));

            } else if (((Button) v).getText().toString().equals("注销")) {
                User.removeLocalUser();
                client.close();
                loginView();
            }
        }
    }

    /**
     * 客户端事件监听
     *
     * @author LiYuanMing
     */
    class MyClientListener implements ClientListener {

        @Override
        public void onAskPeace() {
            askPeaceDialog();
        }

        @Override
        public void onAskRollback() {
            askRollbackDialog();
        }

        @Override
        public void onConnect() {
            localUser = User.getLocalUser();
            if (localUser != null) {
                client.setUser(localUser);
                client.login();
            } else {
                loginView();
            }
        }

        @Override
        public void onConnectFailed() {
            toastMsg("网络连接失败！请检查网络设置后重新启动。");
        }

        @Override
        public void onDeLine() {
            // 显示窗口
            client.connect();
        }

        @Override
        public void onFailed(String msg) {
            // 显示窗口
            toastMsg(msg);
        }

        @Override
        public void onGameOver(int win, String string) {
            localUser = User.getLocalUser();
            if (win == 0) {
                toastMsg("和棋" + string);
                localUser.draw();
            } else if (win == 1) {
                if (client.getGame().getUser1().equals(localUser)) {
                    toastMsg("恭喜你赢了！" + string);
                    localUser.win();
                    Mp3.gamewin.start();
                } else {
                    toastMsg("很遗憾你输了！" + string);
                    localUser.defeat();
                    Mp3.gamelose.start();
                }
            } else if (win == 2) {
                if (client.getGame().getUser2().equals(localUser)) {
                    toastMsg("恭喜你赢了！" + string);
                    localUser.win();
                    Mp3.gamewin.start();
                } else {
                    toastMsg("很遗憾你输了！" + string);
                    localUser.defeat();
                    Mp3.gamelose.start();
                }
            }
            User.saveLocalUser(localUser);
            menuView();
        }

        @Override
        public void onLoginFailed() {
            loginView();
            toastMsg("登陆失败.");
        }

        @Override
        public void onLoginSuccess() {
            client.getUser().setPassword(localUser.getPassword());
            localUser = client.getUser();
            User.saveLocalUser(localUser);
            menuView();
        }

        @Override
        public void onReceiveMsg(String content) {
            ChatMessage cm = ChatMessage.fromString(content);
            chatMessages.add(cm);
            if (chatDialog != null) {
                if (chatDialog.isShowing()) {
                    Mp3.dingdong.start();
                    showChat();
                } else {
                    if (!cm.getFrom().equals(localUser.getName())) {
                        toastMsg(cm.getFrom() + "说:" + cm.getContent());
                        Mp3.receivemsg.start();
                    }
                }
            } else {
                if (!cm.getFrom().equals(localUser.getName())) {
                    toastMsg(cm.getFrom() + "说:" + cm.getContent());
                    Mp3.receivemsg.start();
                }
            }
        }

        @Override
        public void onSignFailed() {
            signView();
            toastMsg("注册失败.");
        }

        @Override
        public void onStart() {
            gameView();

        }

        @Override
        public void onUpdate() {
            // 设置是否可以控制棋盘
            if (gameView != null) {
                int step = client.getGame().getStep();
                if (client.isBlack()) {
                    if (step % 2 == 0) {
                        gameView.setCanSelect(false);
                    } else {
                        gameView.setCanSelect(true);
                    }
                } else {
                    if (step % 2 == 0) {
                        gameView.setCanSelect(true);
                    } else {
                        gameView.setCanSelect(false);
                    }
                }
            }
        }

        @Override
        public void onEat() {
            if (client.getGame().getStep() > 0) {
                Mp3.eat.start();
            }
        }

        @Override
        public void onMove() {
            if (client.getGame().getStep() > 0) {
                Mp3.go.start();
            }
        }

    }

    /**
     * 游戏界面监听
     *
     * @author 汤旗
     */
    class MyGameViewListener implements GameViewListener {

        @Override
        public void walk(Walk walk) {
            client.walk(walk);
        }

        @Override
        public void onSelect() {
            Mp3.select.start();
        }

        @Override
        public void onClickBtnChat() {
            chatDialog();
        }

        @Override
        public void onClickBtnMenu() {
            menuDialog();
        }

    }

    private Client client;
    private GameView gameView;
    private User localUser;
    private Handler toastHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Toast.makeText(MainActivity.this, msg.obj.toString(), Toast.LENGTH_LONG).show();
        }
    };
    public static final int GAME_VIEW = 1;
    public static final int LOGIN_VIEW = 2;
    public static final int SIGN_VIEW = 3;
    public static final int MENU_VIEW = 4;
    public static final int START_VIEW = 5;

    private int thisView;

    private EditText loginEditName, loginEditPassword;
    private Button loginBtnLogin, loginBtnSign;
    private MyClickListener myClickListener;
    private MyGameViewListener myGameViewListener;
    private MyClientListener myClientListener;

    private boolean isFindding;

    private int headPic = 1;

    private ImageView signHead;

    private Button signBtnLastPic, signBtnNextPic, signBtnSign, signBtnBack;

    private EditText signEditName, signEditPassword, signEditRepassword;

    private Button btnFindGame, btnLogout;

    private TextView textName, textScore, textVicount, textDecount,
            textDrcount;
    private ImageView menuHead;

    public void gameView() {
        if (thisView != GAME_VIEW) {
            thisView = GAME_VIEW;
            gameView = new GameView(this, client, myGameViewListener);
            setContentView(gameView);
        }
    }

    public void helpView() {

    }

    public void loginView() {
        if (thisView != LOGIN_VIEW) {
            thisView = LOGIN_VIEW;
            setContentView(R.layout.login_view);
            loginBtnLogin = (Button) findViewById(R.id.loginBtnLogin);
            loginBtnSign = (Button) findViewById(R.id.loginBtnSign);
            loginEditName = (EditText) findViewById(R.id.loginEditName);
            loginEditPassword = (EditText) findViewById(R.id.loginEditPassword);
            loginBtnLogin.setOnClickListener(myClickListener);
            loginBtnSign.setOnClickListener(myClickListener);
        }
        loginBtnLogin.setEnabled(true);
        loginBtnSign.setEnabled(true);
    }

    public void menuView() {
        // localUser = client.getUser();
        isFindding = false;
        if (thisView != MENU_VIEW) {
            thisView = MENU_VIEW;
            setContentView(R.layout.menu_view);
            btnFindGame = (Button) findViewById(R.id.btnFindGame);
            btnFindGame.setOnClickListener(myClickListener);

            btnLogout = (Button) findViewById(R.id.btnLogout);
            btnLogout.setOnClickListener(myClickListener);

            textName = (TextView) findViewById(R.id.textName);
            textScore = (TextView) findViewById(R.id.textScore);
            textVicount = (TextView) findViewById(R.id.textVicount);
            textDecount = (TextView) findViewById(R.id.textDecount);
            textDrcount = (TextView) findViewById(R.id.textDrcount);
            menuHead = (ImageView) findViewById(R.id.menuHead);
        }
        int head = Integer.parseInt(localUser.getHead());
        menuHead.setImageBitmap(Img.getHead(head));
        textName.setText(localUser.getName());
        textScore.setText(localUser.getScore() + "");
        textVicount.setText(localUser.getViCount() + "");
        textDecount.setText(localUser.getDeCount() + "");
        textDrcount.setText(localUser.getDrCount() + "");
    }

    private Handler startHandler = new Handler() {
        public void handleMessage(Message msg) {
            client = new Client("10.101.17.51", 9898, myClientListener);
            client.connect();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startView();
        Mp3.init(this);
        Img.init(this);
        Mp3.bgm.start();
        myClickListener = new MyClickListener();
        myGameViewListener = new MyGameViewListener();
        myClientListener = new MyClientListener();
        new Thread(new Runnable() {
            public void run() {
                // 延时3秒展现启动画面
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                startHandler.sendEmptyMessage(0);
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.exit(0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.exit(0);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            exitDialog();
            return true;
        }

        if (keyCode == KeyEvent.KEYCODE_HOME && event.getRepeatCount() == 0) {
            exitDialog();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void signView() {
        if (thisView != SIGN_VIEW) {
            headPic = 1;
            thisView = SIGN_VIEW;
            setContentView(R.layout.sign_view);

            signEditName = (EditText) findViewById(R.id.signEditName);
            signEditPassword = (EditText) findViewById(R.id.signEditPassword);
            signEditRepassword = (EditText) findViewById(R.id.signEditRepassword);

            signHead = (ImageView) findViewById(R.id.signHead);
            signHead.setImageBitmap(Img.getHead(headPic));

            signBtnLastPic = (Button) findViewById(R.id.signBtnLastPic);
            signBtnNextPic = (Button) findViewById(R.id.signBtnNextPic);
            signBtnSign = (Button) findViewById(R.id.signBtnSign);
            signBtnBack = (Button) findViewById(R.id.signBtnBack);

            signBtnLastPic.setOnClickListener(myClickListener);
            signBtnNextPic.setOnClickListener(myClickListener);
            signBtnSign.setOnClickListener(myClickListener);
            signBtnBack.setOnClickListener(myClickListener);
        }
        signBtnSign.setEnabled(true);
        signBtnBack.setEnabled(true);

    }

    public void startView() {
        thisView = START_VIEW;
        setContentView(R.layout.start_view);
    }

    private void toastMsg(String content) {
        Message msg = new Message();
        msg.obj = content;
        toastHandler.sendMessage(msg);
    }

    private Dialog chatDialog;
    private Button btnSend;
    private ScrollView scrollMsg;
    private EditText editMsg;
    private TextView textMsg;

    public String getChatMsgs() {
        String msgs = "";
        if (chatMessages != null) {
            for (ChatMessage cm : chatMessages) {
                String msg = cm.getFrom() + " " + cm.getSendTime() + "\n  "
                        + cm.getContent() + "\n";
                msgs += msg;
            }
        }
        return msgs;
    }

    private void showChat() {
        textMsg.setText(getChatMsgs());
        scrollMsg.post(new Runnable() {

            @Override
            public void run() {
                scrollMsg.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    public void chatDialog() {
        if (chatDialog == null) {
            LayoutInflater inflater = LayoutInflater.from(this);
            View view = inflater.inflate(R.layout.chat_window, null);
            chatDialog = new Dialog(this, R.style.dialog);
            chatDialog.setContentView(view);

            btnSend = (Button) view.findViewById(R.id.btnSendMsg);
            scrollMsg = (ScrollView) view.findViewById(R.id.scrollMsg);
            editMsg = (EditText) view.findViewById(R.id.editMsg);
            textMsg = (TextView) view.findViewById(R.id.textMsg);

            if (btnSend != null) {
                btnSend.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        ChatMessage chatMessage = new ChatMessage(localUser
                                .getName(), editMsg.getText().toString());
                        client.sendChatMsg(chatMessage);
                        editMsg.setText("");
                    }
                });
            }
        }
        chatDialog.show();
        showChat();
    }

    private Dialog menuDialog;
    private Button btnRollback, btnPeace, btnGiveup;

    public void menuDialog() {
        if (menuDialog == null) {
            LayoutInflater inflater = LayoutInflater.from(this);
            View view = inflater.inflate(R.layout.menu_window, null);
            menuDialog = new Dialog(this, R.style.dialog);
            menuDialog.setContentView(view);
            btnRollback = (Button) view.findViewById(R.id.btnRollback);
            btnPeace = (Button) view.findViewById(R.id.btnAskpeace);
            btnGiveup = (Button) view.findViewById(R.id.btnGiveUp);
            btnGiveup.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    client.giveUp();
                    menuDialog.dismiss();
                }
            });
            btnPeace.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    client.askPeace();
                    menuDialog.dismiss();
                }
            });
            btnRollback.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    client.askRollBack();
                    menuDialog.dismiss();
                }
            });
        }
        menuDialog.show();
    }

    private Dialog exitDialog;

    public void exitDialog() {
        if (exitDialog == null) {
            Builder builder = new Builder(this);
            exitDialog = builder
                    .setMessage("确认退出游戏吗？")
                    .setTitle("提示")
                    .setPositiveButton(
                            "确认",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0,
                                                    int arg1) {
                                    exitDialog.dismiss();
                                    MainActivity.this.finish();
                                }
                            })
                    .setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface arg0,
                                                    int arg1) {
                                    exitDialog.dismiss();
                                }
                            }).create();
        }
        exitDialog.show();
    }

    private Dialog askPeaceDialog;

    public void askPeaceDialog() {
        if (askPeaceDialog == null) {
            Builder builder = new Builder(this);
            askPeaceDialog = builder
                    .setMessage("对方请求和棋，是否同意？")
                    .setTitle("提示")
                    .setPositiveButton(
                            "同意",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0,
                                                    int arg1) {
                                    askPeaceDialog.dismiss();
                                    client.agreePeace();
                                }
                            })
                    .setNegativeButton("不同意",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface arg0,
                                                    int arg1) {
                                    askPeaceDialog.dismiss();
                                }
                            }).create();
        }
        askPeaceDialog.show();
    }

    private Dialog askRollbackDialog;

    public void askRollbackDialog() {
        if (askRollbackDialog == null) {
            Builder builder = new Builder(this);
            askRollbackDialog = builder
                    .setMessage("对方请求悔棋，是否同意？")
                    .setTitle("提示")
                    .setPositiveButton(
                            "同意",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0,
                                                    int arg1) {
                                    askRollbackDialog.dismiss();
                                    client.agreeRollBack();
                                }
                            })
                    .setNegativeButton("不同意",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface arg0,
                                                    int arg1) {
                                    askRollbackDialog.dismiss();
                                }
                            }).create();
        }
        askRollbackDialog.show();
    }
}
