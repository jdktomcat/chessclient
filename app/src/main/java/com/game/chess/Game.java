package com.game.chess;

import java.util.ArrayList;

public class Game {
	// 走棋步数
	private int step;
	// 步时
	private long walkTime;
	// 局时1
	private long time1;

	// 局时2
	private long time2;

	// 玩家1
	private User user1;

	// 玩家2
	private User user2;

	// 地图
	private int map[][];

	// 走棋步
	private ArrayList<Walk> walks;
	private Walk walk;

	public int getStep() {
		return step;
	}

	public long getWalkTime() {
		return walkTime;
	}

	public long getTime1() {
		return time1;
	}

	public long getTime2() {
		return time2;
	}

	public User getUser1() {
		return user1;
	}

	public User getUser2() {
		return user2;
	}

	public int[][] getMap() {
		return map;
	}

	public ArrayList<Walk> getWalks() {
		return walks;
	}

	/**
	 * 预留，设置所有走棋步骤，游戏复盘时候需要用到
	 * 
	 * @param data
	 */
	public void setWalks(String data) throws Exception {
		String tag[] = data.split(";");
		this.walks = new ArrayList<Walk>();
		for (int i = 0; i < tag.length; i++) {
			Walk w = Walk.fromString(tag[i]);
			this.walks.add(w);
		}
	}

	/**
	 * 设置游戏数据
	 * 
	 * @param data
	 */
	public void setData(String data) throws Exception {
		// step;walk;map;
		String tag[] = data.split(";");
		step = Integer.parseInt(tag[0]);
		walk = Walk.fromString(tag[1]);
		map = GameUtil.mapFromString(tag[2]);
		System.out.println(data);
	}
	public void setUser(String user) throws Exception{
		String tag[] = user.split(";");
		user1 = User.fromString(tag[0]);
		user2 = User.fromString(tag[1]);
	}
	public Walk getWalk() {
		return walk;
	}

	public void setTime(String time) throws Exception {
		// walktime,time1,time2
		String tag[] = time.split(",");
		walkTime = Long.parseLong(tag[0]);
		time1 = Long.parseLong(tag[1]);
		time2 = Long.parseLong(tag[2]);
	}
	// // 返回所有步骤字符串
	// public String getAllWalks() {
	// String s = "";
	// for (int i = 0; i < walks.size(); i++) {
	// s += walks.get(i).toString() + ";";
	// }
	// return s;
	// }

	// @Override
	// public String toString() {
	// // 步数，walk，map
	// return step + ";" + walks.get(walks.size() - 1) + ";" + getMapString();
	// }

}
