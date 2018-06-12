package com.game.chess.listener;

import android.view.View;
import android.widget.Button;

import com.game.chess.Img;
import com.game.chess.Mp3;
import com.game.chess.User;
import com.game.chess.activity.MainActivity;

public class MyClickListener implements View.OnClickListener {

    private MainActivity mainActivity;

    public MyClickListener(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void onClick(View v) {
        Mp3.select.start();
        if (v == mainActivity.getLoginBtnLogin()) {
            String name = mainActivity.getLoginBtnSign().getText().toString();
            String pwd = mainActivity.getLoginEditPassword().getText().toString();
            mainActivity.setLocalUser(new User());
            mainActivity.getLocalUser().setName(name);
            mainActivity.getLocalUser().setPassword(pwd);
            mainActivity.getClient().setUser(mainActivity.getLocalUser());
            mainActivity.getClient().login();
            mainActivity.getLoginBtnLogin().setEnabled(false);
            mainActivity.getLoginBtnSign().setEnabled(false);
        } else if (v == mainActivity.getLoginBtnSign()) {
            mainActivity.signView();
        } else if (v == mainActivity.getBtnFindGame()) {
            if (mainActivity.isFindding()) {
                mainActivity.getClient().cancleFind();
                mainActivity.getBtnFindGame().setText("匹配游戏");
                mainActivity.setFindding(false);
            } else {
                mainActivity.getClient().findGame();
                mainActivity.getBtnFindGame().setText("取消匹配");
                mainActivity.setFindding(true);
            }
        } else if (v == mainActivity.getSignBtnSign()) {
            String name = mainActivity.getSignEditName().getText().toString();
            String password = mainActivity.getSignEditPassword().getText().toString();
            String repassword = mainActivity.getSignEditRepassword().getText().toString();
            if ("".equals(name)) {
                mainActivity.toastMsg("名字不能为空！");
                return;
            }
            if ("".equals(password)) {
                mainActivity.toastMsg("密码不能为空！");
                return;
            }
            if (!password.equals(repassword)) {
                mainActivity.toastMsg("密码和重复密码不一致！");
                return;
            }
            User localUser = new User(name, password, mainActivity.getHeadPic() + "");
            mainActivity.setLocalUser(localUser);
            mainActivity.getClient().setUser(localUser);
            mainActivity.getSignBtnSign().setEnabled(false);
            mainActivity.getSignBtnBack().setEnabled(false);
            mainActivity.getClient().sign();
        } else if (v == mainActivity.getSignBtnBack()) {
            mainActivity.loginView();
        }
        int headPic = mainActivity.getHeadPic();
        if (((Button) v).getText().toString().equals("上一张")) {
            headPic--;
            mainActivity.setHeadPic(headPic);
            if (headPic < 1) {
                headPic = 14;
            }
            mainActivity.getSignHead().setImageBitmap(Img.getHead(headPic));
        } else if (((Button) v).getText().toString().equals("下一张")) {
            headPic++;
            if (headPic > 14) {
                headPic = 1;
            }
            mainActivity.getSignHead().setImageBitmap(Img.getHead(headPic));

        } else if (((Button) v).getText().toString().equals("注销")) {
            User.removeLocalUser();
            mainActivity.getClient().close();
            mainActivity.loginView();
        }
    }
}
