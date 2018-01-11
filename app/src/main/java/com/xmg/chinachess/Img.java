package com.xmg.chinachess;

import com.example.chinachess.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Img {
	// 背景
	public static Bitmap BG;
	// 棋盘
	public static Bitmap BOARD;
	// 选择框
	public static Bitmap OOS;
	// 黑士
	public static Bitmap BA;
	// 黑象
	public static Bitmap BB;
	// 黑炮
	public static Bitmap BC;
	// 黑将
	public static Bitmap BK;
	// 黑马
	public static Bitmap BN;
	// 黑卒
	public static Bitmap BP;
	// 黑车
	public static Bitmap BR;

	// 红士
	public static Bitmap RA;
	// 红象
	public static Bitmap RB;
	// 红炮
	public static Bitmap RC;
	// 红将
	public static Bitmap RK;
	// 红马
	public static Bitmap RN;
	// 红卒
	public static Bitmap RP;
	// 红车
	public static Bitmap RR;

	public static Bitmap HEAD1;
	public static Bitmap HEAD2;
	public static Bitmap HEAD3;
	public static Bitmap HEAD4;
	public static Bitmap HEAD5;
	public static Bitmap HEAD6;
	public static Bitmap HEAD7;
	public static Bitmap HEAD8;
	public static Bitmap HEAD9;
	public static Bitmap HEAD10;
	public static Bitmap HEAD11;
	public static Bitmap HEAD12;
	public static Bitmap HEAD13;
	public static Bitmap HEAD14;

	public static void init(Context context) {
		BG = BitmapFactory
				.decodeResource(context.getResources(), R.drawable.bg);

		BOARD = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.board);
		OOS = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.oos);

		BA = BitmapFactory
				.decodeResource(context.getResources(), R.drawable.ba);
		BB = BitmapFactory
				.decodeResource(context.getResources(), R.drawable.bb);
		BC = BitmapFactory
				.decodeResource(context.getResources(), R.drawable.bc);
		BK = BitmapFactory
				.decodeResource(context.getResources(), R.drawable.bk);
		BN = BitmapFactory
				.decodeResource(context.getResources(), R.drawable.bn);
		BP = BitmapFactory
				.decodeResource(context.getResources(), R.drawable.bp);
		BR = BitmapFactory
				.decodeResource(context.getResources(), R.drawable.br);

		RA = BitmapFactory
				.decodeResource(context.getResources(), R.drawable.ra);
		RB = BitmapFactory
				.decodeResource(context.getResources(), R.drawable.rb);
		RC = BitmapFactory
				.decodeResource(context.getResources(), R.drawable.rc);
		RK = BitmapFactory
				.decodeResource(context.getResources(), R.drawable.rk);
		RN = BitmapFactory
				.decodeResource(context.getResources(), R.drawable.rn);
		RP = BitmapFactory
				.decodeResource(context.getResources(), R.drawable.rp);
		RR = BitmapFactory
				.decodeResource(context.getResources(), R.drawable.rr);

		HEAD1 = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.head1);
		HEAD2 = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.head2);
		HEAD3 = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.head3);
		HEAD4 = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.head4);
		HEAD5 = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.head5);
		HEAD6 = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.head6);
		HEAD7 = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.head7);
		HEAD8 = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.head8);
		HEAD9 = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.head9);
		HEAD10 = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.head10);
		HEAD11 = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.head11);
		HEAD12 = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.head12);
		HEAD13 = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.head13);
		HEAD14 = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.head14);
	}

	public static Bitmap getHead(int h) {
		switch (h) {
		case 1:
			return HEAD1;
		case 2:

			return HEAD2;
		case 3:
			return HEAD3;
		case 4:
			return HEAD4;
		case 5:
			return HEAD5;
		case 6:
			return HEAD6;
		case 7:
			return HEAD7;
		case 8:
			return HEAD8;
		case 9:
			return HEAD9;
		case 10:
			return HEAD10;
		case 11:
			return HEAD11;
		case 12:

			return HEAD12;
		case 13:
			return HEAD13;
		case 14:
			return HEAD14;
		default:
			return null;
		}
	}
}
