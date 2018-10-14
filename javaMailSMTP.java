package javaMailSMTP;

import java.io.*;
import java.net.*;
import java.util.StringTokenizer;

import sun.misc.BASE64Encoder;

public class sendMail {
	String server = "";
	int port = 25;
	String from = "";
	String username = "";
	String password = "";
	String to = "";
	String subject = "";
	String content = "";
	Socket socket = null;
	BufferedReader in;
	BufferedWriter out;
	BASE64Encoder encode;
	
	public void init() throws Exception {
		socket = new Socket(server, port);
		encode = new BASE64Encoder();
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		if (getResult() == 220) {
			System.out.println("服务器连接。");
		} else {
			System.out.println("服务器连接失败。");
		}
		sendCmd("HELO " + server);
		if (getResult() == 250) {
			System.out.println("服务器注册。");
		} else {
			System.out.println("服务器注册失败");
		}
	}
	
	public int getResult() throws Exception {
		StringTokenizer st = new StringTokenizer(in.readLine(), " ");
		return Integer.parseInt(st.nextToken());
	}
	
	public void auth() throws Exception {
		sendCmd("AUTH LOGIN");
		if (getResult() == 334) {
			System.out.println("用户验证，请发送用户名和密码: ");
		} else {
			System.out.println("用户验证失败。");
		}
		sendCmd(encode.encode(username.getBytes()));
		if (getResult() == 334) {
			System.out.println("验证用户名");
		} else {
			System.out.println("用户名错误。");
		}
		sendCmd(encode.encode(password.getBytes()));
		if (getResult() == 235) {
			System.out.println("验证密码。");
		} else {
			System.out.println("密码错误。");
		}
	}
	
	public void sendData() throws Exception {
		sendCmd("MAIL FROM:<" + from + ">");
		if (getResult() == 250) {
			
		} else {
			System.out.println("指定源地址错误");
		}
		sendCmd("RCPT TO:<" + to + ">");
		if (getResult() == 250) {
			
		} else {
			System.out.println("指定目标地址错误");
		}
		sendCmd("DATA");
		if (getResult() == 354) {
			
		} else {
			System.out.println("不能发送数据");
		}
		sendCmd("From: " + from);
		sendCmd("To: " + to);
		sendCmd("Subject: " + subject);
		sendCmd("Content: " + content);
		sendCmd(".");
		if (getResult() == 250) {
			System.out.println("发送数据完毕");
		} else {
			System.out.println("发送数据错误");
		}
		sendCmd("QUIT");
		if (getResult() == 221) {
			System.out.println("已经退出");
		} else {
			System.out.println("退出失败");
		}
	}
	
	public void sendCmd(String cmd) throws Exception {
		out.write(cmd);
		out.newLine();
		out.flush();
	}

	public static void main(String[] args) throws Exception {
		sendMail smtp = new sendMail();
		smtp.server = "smtp.163.com";
		smtp.port = 25;
		smtp.from = "15626819107@163.com";
		smtp.username = "15626819107";
		smtp.password = "@a123456";
		smtp.to = "459281182@qq.com";
		smtp.subject = "mail test";
		smtp.content = "hi jack";
		smtp.init();
		smtp.auth();
		smtp.sendData();
	}

}
