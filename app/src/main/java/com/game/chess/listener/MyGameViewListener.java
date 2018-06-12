package com.game.chess.listener;

import com.game.chess.entity.GameView;
import com.game.chess.entity.Mp3;
import com.game.chess.entity.Walk;
import com.game.chess.activity.MainActivity;

/**
 * 游戏界面监听
 *
 * @author 汤旗
 */
public class MyGameViewListener implements GameView.GameViewListener {

    private MainActivity mainActivity;

    public MyGameViewListener(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void walk(Walk walk) {
        mainActivity.getClient().walk(walk);
    }

    @Override
    public void onSelect() {
        Mp3.select.start();
    }

    @Override
    public void onClickBtnChat() {
        mainActivity.chatDialog();
    }

    @Override
    public void onClickBtnMenu() {
        mainActivity.menuDialog();
    }
}
