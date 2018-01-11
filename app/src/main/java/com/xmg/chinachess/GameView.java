package com.xmg.chinachess;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnTouchListener;

public class GameView extends MyView implements OnTouchListener {
	public interface GameViewListener {
		public void walk(Walk walk);

		public void onSelect();

		public void onClickBtnChat();

		public void onClickBtnMenu();
	}

	// 黑士
	public static final int BA = 101;
	// 黑象
	public static final int BB = 102;
	// 黑炮
	public static final int BC = 103;
	// 黑将
	public static final int BK = 104;
	// 黑马
	public static final int BN = 105;
	// 黑卒
	public static final int BP = 106;

	// 黑车
	public static final int BR = 107;
	// 红士
	public static final int RA = 201;
	// 红象
	public static final int RB = 202;
	// 红炮
	public static final int RC = 203;
	// 红将
	public static final int RK = 204;
	// 红马
	public static final int RN = 205;

	// 红卒
	public static final int RP = 206;
	// 红车
	public static final int RR = 207;
	private Client client;
	private Bitmap bg;
	private Bitmap board;
	private Bitmap ba, bb, bc, bk, bn, bp, br, oos, ra, rb, rc, rk, rn, rp, rr;
	private Bitmap head1, head2, head3, head4, head5, head6, head7, head8,
			head9, head10, head11, head12, head13, head14;
	private float chessWidth;
	private float mapHeight;

	private float oy;
	private float topHeight;
	private PointF downPoint;

	private Point select;

	private GameViewListener gameViewListener;

	public GameView(Context context, Client client,
			GameViewListener gameViewListener) {
		super(context);
		this.client = client;
		this.setOnTouchListener(this);
		this.setLongClickable(true);
		this.gameViewListener = gameViewListener;
	}

	private boolean canSelect;

	public void setCanSelect(boolean canSelect) {
		this.canSelect = canSelect;
		select = null;
	}

	@Override
	public void doDraw(Canvas canvas) {
		super.doDraw(canvas);
		Paint paint = new Paint();
		// 画背景
		drawBg(canvas, paint);
		// 画棋盘
		drawBoard(canvas, paint);
		// 画棋子
		drawChess(canvas, paint);
		// 画选择点
		drawSelect(canvas, paint);
		// 画头像
		drawHead(canvas, paint);
		// 画时间
		drawTime(canvas, paint);
		// 画聊天按钮
		drawBtnChat(canvas, paint);
		// 画菜单按钮
		drawBtnMenu(canvas, paint);
	}

	/**
	 * 画背景
	 * 
	 * @param canvas
	 * @param paint
	 */
	private void drawBg(Canvas canvas, Paint paint) {
		canvas.drawBitmap(bg, 0, 0, paint);
	}

	/**
	 * 画棋盘
	 * 
	 * @param canvas
	 * @param paint
	 */
	private void drawBoard(Canvas canvas, Paint paint) {
		canvas.drawBitmap(board, 0, (getHeight() - board.getHeight()) / 2,
				paint);
	}

	// public Bitmap getChessBoard() {
	// Bitmap b = Bitmap.createBitmap(900, 1000, Config.ARGB_8888);
	// Canvas canvas = new Canvas(b);
	//
	// return b;
	// }
	private RectF chatBtnRect;
	private boolean chatBtnDown;

	public void drawBtnChat(Canvas canvas, Paint paint) {
		if (chatBtnRect == null) {
			float w = (getWidth() - rectTop.right);
			chatBtnRect = new RectF(getWidth() - 0.9f * w, 0.25f * oy,
					getWidth() - 0.1f * w, 0.75f * oy);

		}
		if (chatBtnDown) {
			paint.setColor(0xaa6699ff);
		} else {
			paint.setColor(0xff6699ff);
		}
		canvas.drawRoundRect(chatBtnRect, 10, 10, paint);
		paint.setColor(0xffffffff);
		paint.setTextSize(chatBtnRect.height() / 2);
		drawCenter(canvas, "聊天", chatBtnRect.centerX(), chatBtnRect.centerY(),
				paint);
	}

	private RectF menuBtnRect;
	private boolean menuBtnDown;

	public void drawBtnMenu(Canvas canvas, Paint paint) {
		if (menuBtnRect == null) {
			float w = (getWidth() - rectTop.right);
			menuBtnRect = new RectF(getWidth() - 0.9f * w, getHeight() - 0.75f
					* oy, getWidth() - 0.1f * w, getHeight() - 0.25f * oy);

		}
		if (menuBtnDown) {
			paint.setColor(0xaa6699ff);
		} else {
			paint.setColor(0xff6699ff);
		}
		canvas.drawRoundRect(menuBtnRect, 10, 10, paint);
		paint.setColor(0xffffffff);
		paint.setTextSize(chatBtnRect.height() / 2);
		drawCenter(canvas, "菜单", menuBtnRect.centerX(), menuBtnRect.centerY(),
				paint);
	}

	private Bitmap getHead(int h) {
		switch (h) {
		case 1:
			return head1;
		case 2:

			return head2;
		case 3:
			return head3;
		case 4:
			return head4;
		case 5:
			return head5;
		case 6:
			return head6;
		case 7:
			return head7;
		case 8:
			return head8;
		case 9:
			return head9;
		case 10:
			return head10;
		case 11:
			return head11;
		case 12:

			return head13;
		case 13:
			return head13;
		case 14:
			return head14;
		default:
			return null;
		}

	}

	private RectF rectTop, rectBottom;
	private float scoreX, timeX, walkTimeX;

	private void drawTime(Canvas canvas, Paint paint) {
		User user1 = client.getGame().getUser1();
		User user2 = client.getGame().getUser2();
		long time1 = client.getGame().getTime1();
		long time2 = client.getGame().getTime2();
		long walkTime1 = client.getGame().getWalkTime();
		long walkTime2 = client.getGame().getWalkTime();
		if (client.getGame().getStep() % 2 == 0) {
			walkTime1 = 0;
		} else {
			walkTime2 = 0;
		}
		if (rectTop == null) {
			float left = 0.25f * getWidth();
			float top = 0.2f * topHeight;
			float top2 = getHeight() - 0.8f * topHeight;
			float right = 0.75f * getWidth();
			float bottom = 0.8f * topHeight;
			float bottom2 = getHeight() - 0.2f * topHeight;

			rectTop = new RectF(left, top, right, bottom);
			rectBottom = new RectF(left, top2, right, bottom2);

			scoreX = rectTop.left + rectTop.width() / 6.0f;
			timeX = rectTop.left + rectTop.width() / 2.0f;
			walkTimeX = rectTop.left + rectTop.width() / 6.0f * 5;
		}
		paint.setColor(Color.argb(155, 0, 0, 0));
		canvas.drawRoundRect(rectTop, 10, 10, paint);
		canvas.drawRoundRect(rectBottom, 10, 10, paint);

		// top积分，局时，步时
		paint.setColor(Color.CYAN);
		drawCenter(canvas, "积分", scoreX, rectTop.top + rectTop.height() / 4,
				paint);
		drawCenter(canvas, "局时", timeX, rectTop.top + rectTop.height() / 4,
				paint);
		drawCenter(canvas, "步时", walkTimeX, rectTop.top + rectTop.height() / 4,
				paint);
		// top积分，局时，步时
		drawCenter(canvas, "积分", scoreX, rectBottom.top + rectBottom.height()
				/ 4, paint);
		drawCenter(canvas, "局时", timeX, rectBottom.top + rectBottom.height()
				/ 4, paint);
		drawCenter(canvas, "步时", walkTimeX,
				rectBottom.top + rectBottom.height() / 4, paint);

		paint.setColor(Color.RED);
		if (client.isBlack()) {
			// top积分，局时，步时
			drawCenter(canvas, user2.getScore() + "", scoreX, rectTop.top
					+ rectTop.height() / 4 * 3, paint);
			drawCenter(canvas, GameUtil.longToTime(time2) + "", timeX,
					rectTop.top + rectTop.height() / 4 * 3, paint);
			drawCenter(canvas, GameUtil.longToTime(walkTime2) + "", walkTimeX,
					rectTop.top + rectTop.height() / 4 * 3, paint);
			// bottom积分，局时，步时
			drawCenter(canvas, user1.getScore() + "", scoreX, rectBottom.top
					+ rectBottom.height() / 4 * 3, paint);
			drawCenter(canvas, GameUtil.longToTime(time1) + "", timeX,
					rectBottom.top + rectBottom.height() / 4 * 3, paint);
			drawCenter(canvas, GameUtil.longToTime(walkTime1) + "", walkTimeX,
					rectBottom.top + rectBottom.height() / 4 * 3, paint);

		} else {
			// top积分，局时，步时
			drawCenter(canvas, user1.getScore() + "", scoreX, rectTop.top
					+ rectTop.height() / 4 * 3, paint);
			drawCenter(canvas, GameUtil.longToTime(time1) + "", timeX,
					rectTop.top + rectTop.height() / 4 * 3, paint);
			drawCenter(canvas, GameUtil.longToTime(walkTime1) + "", walkTimeX,
					rectTop.top + rectTop.height() / 4 * 3, paint);
			// bottom积分，局时，步时
			drawCenter(canvas, user2.getScore() + "", scoreX, rectBottom.top
					+ rectBottom.height() / 4 * 3, paint);
			drawCenter(canvas, GameUtil.longToTime(time2) + "", timeX,
					rectBottom.top + rectBottom.height() / 4 * 3, paint);
			drawCenter(canvas, GameUtil.longToTime(walkTime2) + "", walkTimeX,
					rectBottom.top + rectBottom.height() / 4 * 3, paint);

		}
	}

	private void drawHead(Canvas canvas, Paint paint) {
		User user1 = client.getGame().getUser1();
		User user2 = client.getGame().getUser2();
		int head1 = 0;
		int head2 = 0;
		String u1 = null;
		String u2 = null;
		if (client.isBlack()) {
			head1 = Integer.parseInt(user2.getHead());
			head2 = Integer.parseInt(user1.getHead());
			u1 = user2.getName();
			u2 = user1.getName();
		} else {
			head1 = Integer.parseInt(user1.getHead());
			head2 = Integer.parseInt(user2.getHead());
			u1 = user1.getName();
			u2 = user2.getName();
		}
		paint.setTextAlign(Align.CENTER);
		paint.setTextSize(0.15f * topHeight);
		paint.setColor(Color.WHITE);
		canvas.drawBitmap(getHead(head1), 10, 0.1f * topHeight, paint);
		drawCenter(canvas, u1, 10 + headWidth / 2, 0.85f * topHeight, paint);
		canvas.drawBitmap(getHead(head2), 10, getHeight() - 0.3f * topHeight
				- headWidth, paint);
		drawCenter(canvas, u2, 10 + headWidth / 2, getHeight() - 0.15f
				* topHeight, paint);
	}

	public void drawCenter(Canvas canvas, String tag, float x, float y,
			Paint paint) {
		FontMetrics fm = paint.getFontMetrics();
		canvas.drawText(tag, x, y - (fm.ascent + fm.descent) / 2, paint);
	}

	public void drawCenter(Canvas canvas, Bitmap bmp, float x, float y) {
		canvas.drawBitmap(bmp, x - bmp.getWidth() / 2, y - bmp.getHeight() / 2,
				new Paint());
	}

	/**
	 * 画棋子
	 * 
	 * @param canvas
	 * @param paint
	 */
	private void drawChess(Canvas canvas, Paint paint) {
		int map[][] = client.getGame().getMap();
		if (map != null) {
			if (client.isBlack()) {
				map = GameUtil.rotateMap(map);
			}
			for (int i = 0; i < 10; i++) {
				for (int j = 0; j < 9; j++) {
					Bitmap chessImg = null;
					switch (map[i][j]) {
					case BA:
						chessImg = ba;
						break;
					case BB:
						chessImg = bb;
						break;
					case BC:
						chessImg = bc;
						break;
					case BK:
						chessImg = bk;
						break;
					case BN:
						chessImg = bn;
						break;
					case BP:
						chessImg = bp;
						break;
					case BR:
						chessImg = br;
						break;
					case RA:
						chessImg = ra;
						break;
					case RB:
						chessImg = rb;
						break;
					case RC:
						chessImg = rc;
						break;
					case RK:
						chessImg = rk;
						break;
					case RN:
						chessImg = rn;
						break;
					case RP:
						chessImg = rp;
						break;
					case RR:
						chessImg = rr;
						break;
					}
					if (chessImg != null) {
						canvas.drawBitmap(chessImg, chessWidth * j, oy
								+ chessWidth * i, paint);
					}
				}
			}
		}
	}

	/**
	 * 画选择点
	 * 
	 * @param canvas
	 * @param paint
	 */
	private void drawSelect(Canvas canvas, Paint paint) {
		Walk w = client.getGame().getWalk();
		if (client.isBlack()) {
			w = GameUtil.rotateWalk(w);
		}
		if (w != null) {
			canvas.drawBitmap(oos, w.x1 * chessWidth, oy + w.y1 * chessWidth,
					paint);
			canvas.drawBitmap(oos, w.x2 * chessWidth, oy + w.y2 * chessWidth,
					paint);
		}
		if (canSelect) {
			if (select != null) {
				canvas.drawBitmap(oos, select.x * chessWidth, oy + select.y
						* chessWidth, paint);

			}
		}
	}

	private float headWidth;

	private void initBmp() {
		chessWidth = getWidth() / 9.0f;
		mapHeight = chessWidth * 10.0f;
		headWidth = (getHeight() / 2 - getWidth() / 0.9f / 2) * 0.6f;
		oy = getHeight() / 2.0f - (getWidth() * 10.0f / 9.0f) / 2.0f;

		float sw = (getWidth() * 1.0f) / Img.BG.getWidth();
		float sh = (getHeight() * 1.0f) / Img.BG.getHeight();
		bg = scaleBitmap(Img.BG, sw, sh);

		sw = (getWidth() * 1.0f) / Img.BOARD.getWidth();
		board = scaleBitmap(Img.BOARD, sw, sw);
		topHeight = (getHeight() - board.getHeight()) / 2;
		sw = chessWidth / Img.BA.getWidth();
		oos = scaleBitmap(Img.OOS, sw, sw);

		ba = scaleBitmap(Img.BA, sw, sw);
		bb = scaleBitmap(Img.BB, sw, sw);
		bc = scaleBitmap(Img.BC, sw, sw);
		bk = scaleBitmap(Img.BK, sw, sw);
		bn = scaleBitmap(Img.BN, sw, sw);
		bp = scaleBitmap(Img.BP, sw, sw);
		br = scaleBitmap(Img.BR, sw, sw);

		ra = scaleBitmap(Img.RA, sw, sw);
		rb = scaleBitmap(Img.RB, sw, sw);
		rc = scaleBitmap(Img.RC, sw, sw);
		rk = scaleBitmap(Img.RK, sw, sw);
		rn = scaleBitmap(Img.RN, sw, sw);
		rp = scaleBitmap(Img.RP, sw, sw);
		rr = scaleBitmap(Img.RR, sw, sw);

		sw = headWidth / Img.HEAD1.getWidth();
		head1 = scaleBitmap(Img.HEAD1, sw, sw);
		head2 = scaleBitmap(Img.HEAD2, sw, sw);
		head3 = scaleBitmap(Img.HEAD3, sw, sw);
		head4 = scaleBitmap(Img.HEAD4, sw, sw);
		head5 = scaleBitmap(Img.HEAD5, sw, sw);
		head6 = scaleBitmap(Img.HEAD6, sw, sw);
		head7 = scaleBitmap(Img.HEAD7, sw, sw);
		head8 = scaleBitmap(Img.HEAD8, sw, sw);
		head9 = scaleBitmap(Img.HEAD9, sw, sw);
		head10 = scaleBitmap(Img.HEAD10, sw, sw);
		head11 = scaleBitmap(Img.HEAD11, sw, sw);
		head12 = scaleBitmap(Img.HEAD12, sw, sw);
		head13 = scaleBitmap(Img.HEAD13, sw, sw);
		head14 = scaleBitmap(Img.HEAD14, sw, sw);
	}

	private boolean canSelect(int x, int y) {
		int map[][] = client.getGame().getMap();
		if (client.isBlack()) {
			x = 8 - x;
			y = 9 - y;
		}
		if (map[y][x] > 200) {
			return !client.isBlack();
		} else if (map[y][x] > 100) {
			return client.isBlack();
		} else {
			return false;
		}
	}

	private void onClick(float x, float y) {
		if (canSelect) {
			int chessWidth = (int) this.chessWidth;
			int kx = (int) x;
			int ky = (int) y;
			if (y > oy && y < (oy + mapHeight)) {
				int sx = (kx / chessWidth);
				int sy = (int) ((ky - oy) / chessWidth);
				if (select == null) {
					if (canSelect(sx, sy)) {
						select = new Point(sx, sy);
						gameViewListener.onSelect();
					}
				} else {
					Walk w = new Walk(select.x, select.y, sx, sy);
					if (client.isBlack()) {
						w = GameUtil.rotateWalk(w);
					}
					gameViewListener.walk(w);
					select = null;
					canSelect = false;
				}
			}
		}
	}

	@Override
	public boolean onTouch(View view, MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			downPoint = new PointF(x, y);
			if (chatBtnRect.contains(x, y)) {
				chatBtnDown = true;
			} else if (menuBtnRect.contains(x, y)) {
				menuBtnDown = true;
			}
			break;
		case MotionEvent.ACTION_UP:
			if (chatBtnRect.contains(x, y)) {
				gameViewListener.onClickBtnChat();
			} else if (menuBtnRect.contains(x, y)) {
				gameViewListener.onClickBtnMenu();
			}
			chatBtnDown=false;
			menuBtnDown=false;
			if (Math.abs(x - downPoint.x) < 5 && Math.abs(y - downPoint.y) < 5) {
				onClick(x, y);
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (chatBtnRect.contains(x, y)) {
				chatBtnDown = true;
			} else{
				chatBtnDown = false;
			}
			if (menuBtnRect.contains(x, y)) {
				menuBtnDown = true;
			}else{
				menuBtnDown = false;
			}
			break;
		}
		return super.onTouchEvent(event);
	}

	private Bitmap scaleBitmap(Bitmap bmp, float sw, float sh) {
		Matrix m = new Matrix();
		m.postScale(sw, sh);
		return Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(),
				m, true);
	}

	public void setSelect(Point select) {
		this.select = select;
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		super.surfaceCreated(arg0);
		initBmp();
	}

}
