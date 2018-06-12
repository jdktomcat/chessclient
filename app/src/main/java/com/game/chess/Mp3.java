package com.game.chess;

import com.example.chinachess.R;

import android.content.Context;
import android.media.MediaPlayer;

public class Mp3 {
    public static MediaPlayer bgm;
    public static MediaPlayer eat;
    public static MediaPlayer gamelose;
    public static MediaPlayer gamewin;
    public static MediaPlayer go;
    public static MediaPlayer select;
    public static MediaPlayer dingdong;
    public static MediaPlayer receivemsg;

    public static void init(Context context) {
        bgm = MediaPlayer.create(context, R.raw.bgm);
        bgm.setLooping(true);
        eat = MediaPlayer.create(context, R.raw.eat);
        gamelose = MediaPlayer.create(context, R.raw.gamelose);
        gamewin = MediaPlayer.create(context, R.raw.gamewin);
        go = MediaPlayer.create(context, R.raw.go);
        select = MediaPlayer.create(context, R.raw.select);
        dingdong = MediaPlayer.create(context, R.raw.dingdong);
        receivemsg = MediaPlayer.create(context, R.raw.receivemsg);
    }
}
