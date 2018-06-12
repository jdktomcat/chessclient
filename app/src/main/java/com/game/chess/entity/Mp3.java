package com.game.chess.entity;

import android.content.Context;
import android.media.MediaPlayer;

import com.game.chess.R;

public class Mp3 {
    public static MediaPlayer bgm;
    public static MediaPlayer eat;
    public static MediaPlayer gameLose;
    public static MediaPlayer gameWin;
    public static MediaPlayer go;
    public static MediaPlayer select;
    public static MediaPlayer dingDong;
    public static MediaPlayer receiveMsg;

    public static void init(Context context) {
        bgm = MediaPlayer.create(context, R.raw.bgm);
        bgm.setLooping(true);
        eat = MediaPlayer.create(context, R.raw.eat);
        gameLose = MediaPlayer.create(context, R.raw.gamelose);
        gameWin = MediaPlayer.create(context, R.raw.gamewin);
        go = MediaPlayer.create(context, R.raw.go);
        select = MediaPlayer.create(context, R.raw.select);
        dingDong = MediaPlayer.create(context, R.raw.dingdong);
        receiveMsg = MediaPlayer.create(context, R.raw.receivemsg);
    }
}
