package com.wu.large.util;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.text.TextUtils;

import androidx.annotation.RequiresApi;


import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * 作者: 吴奎庆
 * <p>
 * 时间: 2019/11/1
 * <p>
 * 简介:
 */
public class LargeUtil {

    public static final Map<String, String> FILE_TYPE_MAP = new HashMap();
    private static final String gif_types = ".gif.GIF";
    private static final String no_support_types = ".bmp.tiff";



    /**
     * 判断是不是Android Q  版本
     *
     * @return
     */
    public static boolean isAndroidQ() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;
    }


    /**
     * Gets the content:// URI from the given corresponding path to a file
     *
     * @param context
     * @param imageFile
     * @return content Uri
     */
    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID}, MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }


    /**
     * 不支持的图片预览格式
     * @param path
     * @return
     */
    public static boolean isNoSupport(String path) {
        if (TextUtils.isEmpty(path)) return false;
        int end = path.lastIndexOf(".");
        if (end <= 0) return false;

        String type = path.substring(end);
        if (TextUtils.isEmpty(type)) return false;
        return no_support_types.contains(type);
    }


    public static boolean isGif(String path) {
        if (TextUtils.isEmpty(path)) return false;
        int end = path.lastIndexOf(".");
        if (end <= 0) return false;

        String type = path.substring(end);
        if (TextUtils.isEmpty(type)) return false;
        return gif_types.contains(type);
    }



    public static String getFileType(String filePath) {
        FileInputStream is = null;
        String value = null;

        try {
            is = new FileInputStream(filePath);
            byte[] b = new byte[3];
            is.read(b, 0, b.length);
            value = bytesToHexString(b);
        } catch (Exception var12) {
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException var11) {
                }
            }

        }

        String type = (String)FILE_TYPE_MAP.get(value);
        return type == null ? "" : type;
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src != null && src.length > 0) {
            for(int i = 0; i < src.length; ++i) {
                int v = src[i] & 255;
                String hv = Integer.toHexString(v);
                if (hv.length() < 2) {
                    stringBuilder.append(0);
                }

                stringBuilder.append(hv);
            }

            return stringBuilder.toString();
        } else {
            return null;
        }
    }


}
