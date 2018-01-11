package com.xmg.chinachess;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class User {
	public static final String FILE_NAME = "sdcard/chinesechess/user.ini";
	private static User localUser;

	public static void removeLocalUser(){
		File file = new File(FILE_NAME);
		file.delete();
	}
	public void win(){
		viCount++;
		score+=2;
	}
	public void defeat(){
		deCount++;
		score-=2;
	}
	public void draw(){
		drCount++;
	}
	public static void saveLocalUser(User user) {
		File file = new File(FILE_NAME);
		File parentFile = file.getParentFile();
		if (!parentFile.exists()) {
			parentFile.mkdir();
		}
		OutputStream os = null;
		try {
			os = new FileOutputStream(file);
			os.write(user.toString().getBytes("utf8"));
		} catch (Exception e) {
		} finally {
			try {
				os.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static User getLocalUser() {
		File file = new File(FILE_NAME);
		File parentFile = file.getParentFile();
		if (!parentFile.exists()) {
			parentFile.mkdir();
			return null;
		}
		InputStream is = null;
		try {
			is = new FileInputStream(file);
			String data = new BufferedReader(new InputStreamReader(is, "utf8"))
					.readLine();
			if (data != null) {
				localUser = User.fromString(data);
			}
		} catch (Exception e) {
			return null;
		} finally {
			try {
				is.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return localUser;
	}

	// 用户名
	private String name;
	// 密码
	private String password;
	// 分数
	private int score;
	// 头像
	private String head;
	// 胜利次数
	private int viCount;
	// 失败次数
	private int deCount;
	// 和棋次数
	private int drCount;

	public User(String name, String password, String head) {
		super();
		this.name = name;
		this.password = password;
		this.head = head;
	}

	public static String decode(String str) {
		return str.replace("<maohao>", ":").replace("<br>", "\n")
				.replace("<space>", " ").replace("fenhao", ";")
				.replace("douhao", ",");
	}

	// 不包含" "、":"、","、","以及"\n"
	public static String encode(String str) {
		return str.replace("\n", "<br>").replace(":", "<maohao>")
				.replace(" ", "<space>").replace(";", "<fenhao>")
				.replace(",", "<douhao>");
	}

	public int getDeCount() {
		return deCount;
	}

	public int getDrCount() {
		return drCount;
	}

	public String getHead() {
		return head;
	}

	public String getName() {
		return name;
	}

	public String getPassword() {
		return password;
	}

	public int getScore() {
		return score;
	}

	public int getViCount() {
		return viCount;
	}

	public void setDeCount(int deCount) {
		this.deCount = deCount;
	}

	public void setDrCount(int drCount) {
		this.drCount = drCount;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public void setViCount(int viCount) {
		this.viCount = viCount;
	}

	public User() {
	}

	@Override
	public String toString() {
		return encode(name) + "," + encode(password) + "," + score + "," + head
				+ "," + viCount + "," + deCount + "," + drCount;
	}

	public static User fromString(String user) throws Exception {
		User u = new User();
		String data[] = user.split(",");
		String name = decode(data[0]);
		String password = decode(data[1]);
		int score = Integer.parseInt(data[2]);
		String head = data[3];
		int viCount = Integer.parseInt(data[4]);
		int deCount = Integer.parseInt(data[5]);
		int drCount = Integer.parseInt(data[6]);
		u.setName(name);
		u.setPassword(password);
		u.setScore(score);
		u.setHead(head);
		u.setViCount(viCount);
		u.setDeCount(deCount);
		u.setDrCount(drCount);
		return u;
	}

	@Override
	public boolean equals(Object obj) {
		try {
			return name.equals(((User) obj).name);
		} catch (Exception e) {
			return false;
		}
	}
}
