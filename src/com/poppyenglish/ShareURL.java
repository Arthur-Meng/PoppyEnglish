package com.poppyenglish;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ShareURL {
	static String[] shareinfo=new String[100];
	static String addinfo = null;

	public String[] getinfo(final String user) {

		Thread thread = new Thread() {
			String strUrl = "http://192.168.191.1/PoppyEnglish/share?tel=" + user;
			URL url = null;

			public void run() {
				try {
					url = new URL(strUrl);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
				HttpURLConnection httpURLConnection = null;
				try {
					httpURLConnection = (HttpURLConnection) url.openConnection();
				} catch (IOException e) {
					e.printStackTrace();
				}

				InputStreamReader inputStreamReader = null;
				try {
					inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
				} catch (IOException e) {
					e.printStackTrace();
				}
				BufferedReader buff = null;
				try {
					buff = new BufferedReader(inputStreamReader);
				} catch (Exception e) {
					e.printStackTrace();
				}
				String readLine = null;
				int k = 0;
				try {
					if (buff == null) {
						shareinfo = null;
					} else {
						while ((readLine = buff.readLine()) != null) {
							shareinfo[k] = readLine;
							k++;
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					if (inputStreamReader != null) {
						inputStreamReader.close();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				httpURLConnection.disconnect();
			}
		};
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return shareinfo;
	}

	private String addinfo(final String user, final String number) {
		Thread thread = new Thread() {
			String strUrl = "http://192.168.191.1/PoppyEnglish/addshare?tel=" + user + "&" + "number=" + number;
			URL url = null;

			public void run() {
				try {
					url = new URL(strUrl);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
				HttpURLConnection httpURLConnection = null;
				try {
					httpURLConnection = (HttpURLConnection) url.openConnection();
				} catch (IOException e) {
					e.printStackTrace();
				}

				InputStreamReader inputStreamReader = null;
				try {
					inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
				} catch (IOException e) {
					e.printStackTrace();
				}
				BufferedReader buff = null;
				try {
					buff = new BufferedReader(inputStreamReader);
				} catch (Exception e) {
					e.printStackTrace();
				}
				String readLine = null;
				int k = 0;
				try {
					if (buff == null) {
						addinfo = null;
					} else {
						while ((readLine = buff.readLine()) != null) {
							addinfo += readLine;
							k++;
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					if (inputStreamReader != null) {
						inputStreamReader.close();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				httpURLConnection.disconnect();
			}
		};
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return addinfo;
	}
}
