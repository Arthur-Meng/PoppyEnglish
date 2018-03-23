package com.poppyenglish;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import android.os.Handler;

public class ClientThread implements Runnable {
	private Handler handler;
	// 该线程所处理的Socket所对应的输入流
	private BufferedReader br = null;

	public ClientThread(Socket socket, Handler handler) throws IOException {
		this.handler = handler;
		br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}

	@Override
	public void run() {
		try {
			String content = null;
			// 不断读取Socket输入流的内容
			while (true) {
				while ((content = br.readLine()) != null) {
					MyContent myContent = MyContent.getInstance();
					myContent.setIfReady(true);
					myContent.setContentAll(content);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}