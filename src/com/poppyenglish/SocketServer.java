package com.poppyenglish;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import android.os.Handler;
import android.os.Message;

public class SocketServer {
	private static OutputStream os;
	private static SocketServer instance = new SocketServer();
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// 如果消息来自子线程
			if (msg.what == 0x234) {
			}
		}

	};

	private SocketServer() {
		Thread thread = new Thread() {
			public void run() {
				// socket = new Socket("10.154.149.188", 50000);
				try {
					Socket socket;
					socket = new Socket("192.168.191.1", 50000);
					System.out.print(socket);
					// 客户端启动ClientThread线程不断读取来自服务器的数据
					new Thread(new ClientThread(socket, handler)).start();
					os = socket.getOutputStream();
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		};
		thread.start();

	}

	public static SocketServer getInstance() {
		return instance;
	}

	public static void write(String msg) {
		try {
			os.write((msg + '\n').getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
