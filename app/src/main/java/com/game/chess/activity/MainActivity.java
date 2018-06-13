package com.game.chess.activity;

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

import com.game.chess.R;
import com.game.chess.entity.ChatMessage;
import com.game.chess.entity.Client;
import com.game.chess.entity.GameView;
import com.game.chess.entity.Img;
import com.game.chess.entity.Mp3;
import com.game.chess.entity.User;
import com.game.chess.listener.MyClickListener;
import com.game.chess.listener.MyClientListener;
import com.game.chess.listener.MyGameViewListener;

import java.util.ArrayList;

/**
 * 主活动
 *
 * @author 汤旗
 */
public class MainActivity extends Activity {
    /**
     * 游戏界面
     */
    public static final int GAME_VIEW = 1;

    /**
     * 登录界面
     */
    public static final int LOGIN_VIEW = 2;

    /**
     * 注册界面
     */
    public static final int SIGN_VIEW = 3;

    /**
     * 菜单界面
     */
    public static final int MENU_VIEW = 4;

    /**
     * 开始界面
     */
    public static final int START_VIEW = 5;

    /**
     * 消息列表
     */
    private ArrayList<ChatMessage> chatMessages = new ArrayList<>();

    /**
     * 客户端
     */
    private Client client;

    /**
     * 游戏界面
     */
    private GameView gameView;

    /**
     * 本地用户
     */
    private User localUser;

    /**
     * 提示处理器
     */
    private Handler toastHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Toast.makeText(MainActivity.this, msg.obj.toString(), Toast.LENGTH_LONG).show();
        }
    };


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

    private TextView textName, textScore, textVicount, textDecount, textDrcount;

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
        myClickListener = new MyClickListener(this);
        myGameViewListener = new MyGameViewListener(this);
        myClientListener = new MyClientListener(this);
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

    public void toastMsg(String content) {
        Message msg = new Message();
        msg.obj = content;
        toastHandler.sendMessage(msg);
    }

    private Dialog chatDialog;
    private Button btnSend;
    private ScrollView scrollMsg;
    private EditText editMsg;
    private TextView textMsg;

    public String getChatMsg() {
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

    public void showChat() {
        textMsg.setText(getChatMsg());
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
                                public void onClick(DialogInterface arg0, int arg1) {
                                    askRollbackDialog.dismiss();
                                    client.agreeRollBack();
                                }
                            })
                    .setNegativeButton("不同意",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    askRollbackDialog.dismiss();
                                }
                            }).create();
        }
        askRollbackDialog.show();
    }

    public int getThisView() {
        return thisView;
    }

    public EditText getLoginEditName() {
        return loginEditName;
    }

    public EditText getLoginEditPassword() {
        return loginEditPassword;
    }

    public Button getLoginBtnLogin() {
        return loginBtnLogin;
    }

    public Button getLoginBtnSign() {
        return loginBtnSign;
    }


    public MyClickListener getMyClickListener() {
        return myClickListener;
    }

    public MyGameViewListener getMyGameViewListener() {
        return myGameViewListener;
    }

    public boolean isFindding() {
        return isFindding;
    }

    public int getHeadPic() {
        return headPic;
    }

    public ImageView getSignHead() {
        return signHead;
    }

    public Button getSignBtnLastPic() {
        return signBtnLastPic;
    }

    public Button getSignBtnNextPic() {
        return signBtnNextPic;
    }

    public Button getSignBtnSign() {
        return signBtnSign;
    }

    public Button getSignBtnBack() {
        return signBtnBack;
    }

    public EditText getSignEditName() {
        return signEditName;
    }

    public EditText getSignEditPassword() {
        return signEditPassword;
    }

    public EditText getSignEditRepassword() {
        return signEditRepassword;
    }

    public Button getBtnFindGame() {
        return btnFindGame;
    }

    public Button getBtnLogout() {
        return btnLogout;
    }

    public TextView getTextName() {
        return textName;
    }

    public TextView getTextScore() {
        return textScore;
    }

    public TextView getTextVicount() {
        return textVicount;
    }

    public TextView getTextDecount() {
        return textDecount;
    }

    public TextView getTextDrcount() {
        return textDrcount;
    }

    public ImageView getMenuHead() {
        return menuHead;
    }

    public Handler getStartHandler() {
        return startHandler;
    }

    public Dialog getChatDialog() {
        return chatDialog;
    }

    public Button getBtnSend() {
        return btnSend;
    }

    public ScrollView getScrollMsg() {
        return scrollMsg;
    }

    public EditText getEditMsg() {
        return editMsg;
    }

    public TextView getTextMsg() {
        return textMsg;
    }

    public Dialog getMenuDialog() {
        return menuDialog;
    }

    public Button getBtnRollback() {
        return btnRollback;
    }

    public Button getBtnPeace() {
        return btnPeace;
    }

    public Button getBtnGiveup() {
        return btnGiveup;
    }

    public Dialog getExitDialog() {
        return exitDialog;
    }

    public Dialog getAskPeaceDialog() {
        return askPeaceDialog;
    }

    public Dialog getAskRollbackDialog() {
        return askRollbackDialog;
    }

    public ArrayList<ChatMessage> getChatMessages() {
        return chatMessages;
    }

    public Client getClient() {
        return client;
    }

    public GameView getGameView() {
        return gameView;
    }

    public User getLocalUser() {
        return localUser;
    }

    public Handler getToastHandler() {
        return toastHandler;
    }

    public void setChatMessages(ArrayList<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setGameView(GameView gameView) {
        this.gameView = gameView;
    }

    public void setLocalUser(User localUser) {
        this.localUser = localUser;
    }

    public void setToastHandler(Handler toastHandler) {
        this.toastHandler = toastHandler;
    }

    public void setThisView(int thisView) {
        this.thisView = thisView;
    }

    public void setLoginEditName(EditText loginEditName) {
        this.loginEditName = loginEditName;
    }

    public void setLoginEditPassword(EditText loginEditPassword) {
        this.loginEditPassword = loginEditPassword;
    }

    public void setLoginBtnLogin(Button loginBtnLogin) {
        this.loginBtnLogin = loginBtnLogin;
    }

    public void setLoginBtnSign(Button loginBtnSign) {
        this.loginBtnSign = loginBtnSign;
    }

    public void setMyClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public void setMyGameViewListener(MyGameViewListener myGameViewListener) {
        this.myGameViewListener = myGameViewListener;
    }

    public void setMyClientListener(MyClientListener myClientListener) {
        this.myClientListener = myClientListener;
    }

    public void setFindding(boolean findding) {
        isFindding = findding;
    }

    public void setHeadPic(int headPic) {
        this.headPic = headPic;
    }

    public void setSignHead(ImageView signHead) {
        this.signHead = signHead;
    }

    public void setSignBtnLastPic(Button signBtnLastPic) {
        this.signBtnLastPic = signBtnLastPic;
    }

    public void setSignBtnNextPic(Button signBtnNextPic) {
        this.signBtnNextPic = signBtnNextPic;
    }

    public void setSignBtnSign(Button signBtnSign) {
        this.signBtnSign = signBtnSign;
    }

    public void setSignBtnBack(Button signBtnBack) {
        this.signBtnBack = signBtnBack;
    }

    public void setSignEditName(EditText signEditName) {
        this.signEditName = signEditName;
    }

    public void setSignEditPassword(EditText signEditPassword) {
        this.signEditPassword = signEditPassword;
    }

    public void setSignEditRepassword(EditText signEditRepassword) {
        this.signEditRepassword = signEditRepassword;
    }

    public void setBtnFindGame(Button btnFindGame) {
        this.btnFindGame = btnFindGame;
    }

    public void setBtnLogout(Button btnLogout) {
        this.btnLogout = btnLogout;
    }

    public void setTextName(TextView textName) {
        this.textName = textName;
    }

    public void setTextScore(TextView textScore) {
        this.textScore = textScore;
    }

    public void setTextVicount(TextView textVicount) {
        this.textVicount = textVicount;
    }

    public void setTextDecount(TextView textDecount) {
        this.textDecount = textDecount;
    }

    public void setTextDrcount(TextView textDrcount) {
        this.textDrcount = textDrcount;
    }

    public void setMenuHead(ImageView menuHead) {
        this.menuHead = menuHead;
    }

    public void setStartHandler(Handler startHandler) {
        this.startHandler = startHandler;
    }

    public void setChatDialog(Dialog chatDialog) {
        this.chatDialog = chatDialog;
    }

    public void setBtnSend(Button btnSend) {
        this.btnSend = btnSend;
    }

    public void setScrollMsg(ScrollView scrollMsg) {
        this.scrollMsg = scrollMsg;
    }

    public void setEditMsg(EditText editMsg) {
        this.editMsg = editMsg;
    }

    public void setTextMsg(TextView textMsg) {
        this.textMsg = textMsg;
    }

    public void setMenuDialog(Dialog menuDialog) {
        this.menuDialog = menuDialog;
    }

    public void setBtnRollback(Button btnRollback) {
        this.btnRollback = btnRollback;
    }

    public void setBtnPeace(Button btnPeace) {
        this.btnPeace = btnPeace;
    }

    public void setBtnGiveup(Button btnGiveup) {
        this.btnGiveup = btnGiveup;
    }

    public void setExitDialog(Dialog exitDialog) {
        this.exitDialog = exitDialog;
    }

    public void setAskPeaceDialog(Dialog askPeaceDialog) {
        this.askPeaceDialog = askPeaceDialog;
    }

    public void setAskRollbackDialog(Dialog askRollbackDialog) {
        this.askRollbackDialog = askRollbackDialog;
    }
}
