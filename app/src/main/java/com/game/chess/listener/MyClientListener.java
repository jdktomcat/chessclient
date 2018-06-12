package com.game.chess.listener;

import com.game.chess.entity.ChatMessage;
import com.game.chess.entity.Client;
import com.game.chess.entity.Mp3;
import com.game.chess.entity.User;
import com.game.chess.activity.MainActivity;

/**
 * 客户端事件监听
 *
 * @author 汤旗
 */
public class MyClientListener implements Client.ClientListener {

    private MainActivity mainActivity;

    public MyClientListener(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void onAskPeace() {
        mainActivity.askPeaceDialog();
    }

    @Override
    public void onAskRollback() {
        mainActivity.askRollbackDialog();
    }

    @Override
    public void onConnect() {
        mainActivity.setLocalUser(User.getLocalUser());
        if (mainActivity.getLocalUser() != null) {
            mainActivity.getClient().setUser(mainActivity.getLocalUser());
            mainActivity.getClient().login();
        } else {
            mainActivity.loginView();
        }
    }

    @Override
    public void onConnectFailed() {
        mainActivity.toastMsg("网络连接失败！请检查网络设置后重新启动。");
    }

    @Override
    public void onDeLine() {
        // 显示窗口
        mainActivity.getClient().connect();
    }

    @Override
    public void onFailed(String msg) {
        // 显示窗口
        mainActivity.toastMsg(msg);
    }

    @Override
    public void onGameOver(int win, String string) {
        mainActivity.setLocalUser(User.getLocalUser());
        if (win == 0) {
            mainActivity.toastMsg("和棋" + string);
            mainActivity.getLocalUser().draw();
        } else if (win == 1) {
            if (mainActivity.getClient().getGame().getUser1().equals(mainActivity.getLocalUser())) {
                mainActivity.toastMsg("恭喜你赢了！" + string);
                mainActivity.getLocalUser().win();
                Mp3.gamewin.start();
            } else {
                mainActivity.toastMsg("很遗憾你输了！" + string);
                mainActivity.getLocalUser().defeat();
                Mp3.gamelose.start();
            }
        } else if (win == 2) {
            if (mainActivity.getClient().getGame().getUser2().equals(mainActivity.getLocalUser())) {
                mainActivity.toastMsg("恭喜你赢了！" + string);
                mainActivity.getLocalUser().win();
                Mp3.gamewin.start();
            } else {
                mainActivity.toastMsg("很遗憾你输了！" + string);
                mainActivity.getLocalUser().defeat();
                Mp3.gamelose.start();
            }
        }
        User.saveLocalUser(mainActivity.getLocalUser());
        mainActivity.menuView();
    }

    @Override
    public void onLoginFailed() {
        mainActivity.loginView();
        mainActivity.toastMsg("登陆失败.");
    }

    @Override
    public void onLoginSuccess() {
        User user = mainActivity.getClient().getUser();
        User localUser = mainActivity.getLocalUser();
        user.setPassword(localUser.getPassword());
        mainActivity.setLocalUser(user);
        User.saveLocalUser(user);
        mainActivity.menuView();
    }

    @Override
    public void onReceiveMsg(String content) {
        ChatMessage cm = ChatMessage.fromString(content);
        mainActivity.getChatMessages().add(cm);
        if (mainActivity.getChatDialog() != null) {
            if (mainActivity.getChatDialog().isShowing()) {
                Mp3.dingdong.start();
                mainActivity.showChat();
            } else {
                if (!cm.getFrom().equals(mainActivity.getLocalUser().getName())) {
                    mainActivity.toastMsg(cm.getFrom() + "说:" + cm.getContent());
                    Mp3.receivemsg.start();
                }
            }
        } else {
            if (!cm.getFrom().equals(mainActivity.getLocalUser().getName())) {
                mainActivity.toastMsg(cm.getFrom() + "说:" + cm.getContent());
                Mp3.receivemsg.start();
            }
        }
    }

    @Override
    public void onSignFailed() {
        mainActivity.signView();
        mainActivity.toastMsg("注册失败.");
    }

    @Override
    public void onStart() {
        mainActivity.gameView();
    }

    @Override
    public void onUpdate() {
        // 设置是否可以控制棋盘
        if (mainActivity.getGameView() != null) {
            int step = mainActivity.getClient().getGame().getStep();
            if (mainActivity.getClient().isBlack()) {
                if (step % 2 == 0) {
                    mainActivity.getGameView().setCanSelect(false);
                } else {
                    mainActivity.getGameView().setCanSelect(true);
                }
            } else {
                if (step % 2 == 0) {
                    mainActivity.getGameView().setCanSelect(true);
                } else {
                    mainActivity.getGameView().setCanSelect(false);
                }
            }
        }
    }

    @Override
    public void onEat() {
        if (mainActivity.getClient().getGame().getStep() > 0) {
            Mp3.eat.start();
        }
    }

    @Override
    public void onMove() {
        if (mainActivity.getClient().getGame().getStep() > 0) {
            Mp3.go.start();
        }
    }
}
