package com.poppyenglish;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.http.callback.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class PhotoUtils {
	File tempFile;
	Handler mHandler;
	SweetAlertDialog progressDialog;
	AlertDialog.Builder dialog;
	Activity activity;
	int PHOTO_CAMERA = 0;
	int PHOTO_WALL = 1;
	int PHOTO_STORE = 2;
	int PHOTO_NOT_STORE = 3;
	HttpUtils httpUtils;
	String usertel;
	Bitmap bitmap;

	public void sendPhoto(Activity activity, String tel) {
		// 显示选择的对话框
		usertel = tel;
		String[] options = { "相机", "相册" };
		showDialog("选择上传方式", options, activity);
		getProgressDialog(activity);
	}

	public void getProgressDialog(Activity activity) {
		progressDialog = new SweetAlertDialog(activity, SweetAlertDialog.PROGRESS_TYPE);
		progressDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
		progressDialog.setTitleText("Loading");
		progressDialog.setCancelable(false);
	}

	/**
	 * 显示选择图片来源的dialog(来自拍照还是本地图库)
	 * 
	 * @param title
	 * @param items
	 */
	public void showDialog(String title, String[] items, Activity sendactivity) {
		activity = sendactivity;
		OnClickListener dialogListener = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0:
					tempFile = new File(Environment.getExternalStorageDirectory(), usertel);
					// 调用系统拍照
					startCamera(dialog);
					break;
				case 1:
					tempFile = new File(Environment.getExternalStorageDirectory(), usertel);
					// 打开系统图库
					startWall(dialog);
					break;
				default:
					break;
				}
			}
		};
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.arg1 > 0){
					//不取消dialog
				}

			}
		};
		dialog = new AlertDialog.Builder(activity).setTitle(title).setItems(items, dialogListener);
		// 显示dialog
		dialog.show();
	}

	/**
	 * 调用相机来照相
	 * 
	 * @param dialog
	 */
	public void startCamera(DialogInterface dialog) {
		dialog.dismiss();// 首先隐藏选择照片来源的dialog
		// 调用系统的拍照功能
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra("camerasensortype", 1);// 调用前置摄像头
		intent.putExtra("autofocus", true);// 进行自动对焦操作
		intent.putExtra("fullScreen", false);// 设置全屏
		intent.putExtra("showActionIcons", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));// 指定调用相机之后所拍照存储到的位置
		activity.startActivityForResult(intent, PHOTO_CAMERA);
	}

	/**
	 * 打开系统图库
	 * 
	 * @param dialog
	 */
	public void startWall(DialogInterface dialog) {
		dialog.dismiss();// 设置隐藏dialog
		Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
		activity.startActivityForResult(intent, PHOTO_WALL);
	}

	/**
	 * 将图片裁剪到指定大小
	 * 
	 * @param uri
	 * @param size
	 * @param flag
	 */
	public void startPhotoCut(Uri uri, int size, boolean flag) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", true);// 设置Intent中的view是可以裁剪的
		// 设置宽高比
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// 设置裁剪图片的宽高
		intent.putExtra("outputX", size);
		intent.putExtra("outputY", size);
		// 设置是否返回数据
		intent.putExtra("return-data", true);
		if (flag == true) {
			activity.startActivityForResult(intent, PHOTO_STORE);
		} else {
			activity.startActivityForResult(intent, PHOTO_NOT_STORE);
		}
	}

	/**
	 * 将图片上传
	 * 
	 * @param data
	 * @param flag
	 *            表示如果是拍照获得的照片的话则是true，如果是从系统选择的照片的话就是false
	 */
	public void setPictureToImageView(Intent data, boolean flag) {
		Bundle bundle = data.getExtras();
		// 压缩
		bitmap = bundle.getParcelable("data");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		FileOutputStream fos = null;
		bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 第2个参数表示压缩率，100表示不压缩
		try {
			fos = new FileOutputStream(tempFile);
			fos.write(baos.toByteArray());
			fos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (null != bundle) {
			// 上传图片到服务器
			httpUtils = new HttpUtils(100000);
			httpUtils.configCurrentHttpCacheExpiry(5000);
			if (flag == false) {
				// 上传图片
				uploadThread.start();
			} else {
				// 上传图片
				uploadThread.start();
			}

		}
	}

	Thread uploadThread = new Thread() {
		@Override
		public void run() {
			uploadPicture();
		}
	};

	/**
	 * 上传图片到数据库
	 */
	public void uploadPicture() {
		RequestParams params = new RequestParams();
		params.addBodyParameter("msg", usertel);
		params.addBodyParameter("file", tempFile);
		httpUtils.send(HttpMethod.POST, "http://192.168.191.1/PoppyEnglish/img", params, new RequestCallBack<String>() {

			@Override
			public void onStart() {
				progressDialog.show();// 显示进度条
			}

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				System.out.println("上传失败");
				System.out.println(arg0.toString());
				// 上传失败之后隐藏进度条
				progressDialog.dismiss();
			}

			@Override
			public void onLoading(long total, long current, boolean isUploading) {
				System.out.println("current/total:  " + current + "/" + total);
				int process = 0;
				if (total != 0) {
					process = (int) (current / (total / 100));
				}
				Message message = new Message();
				message.arg1 = process;
				mHandler.sendMessage(message);
				super.onLoading(total, current, isUploading);
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				System.out.println("上传成功");
				// 上传成功之后隐藏进度条
				progressDialog.dismiss();
			}
		});
	}

}
