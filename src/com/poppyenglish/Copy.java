package com.poppyenglish;


import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class Copy {
	/**
     * 
     *
     * @param context
     * @param fileName
     * @return
     */

    public static boolean copyFileFromAssets(Context context,String fileName) {
        boolean result = false;
        try {
            String filepath = "data/data/" +"com.poppyenglish"+ "/databases/";
            if ((new File(filepath + fileName)).exists() == false) {
                File f = new File(filepath);
                if (!f.exists()) {
                    f.mkdir();
                }
                try {
                    // 输入流
                    InputStream is = context.getAssets().open(fileName);
                    // 输出流
                    OutputStream os = new FileOutputStream(filepath + fileName);
                    // 缓存
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = is.read(buffer)) > 0) {
                        os.write(buffer, 0, length);
                    }
                    // 结束了
                    os.flush();
                    os.close();
                    is.close();
                    result = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return result;
    }
}
