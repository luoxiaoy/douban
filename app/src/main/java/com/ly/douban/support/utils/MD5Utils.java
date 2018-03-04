//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ly.douban.support.utils;

import android.text.TextUtils;

import com.ly.douban.support.logger.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {
    private static final String TAG = "MD5UTILS";
    private static final String STRING_ENCODE = "UTF-8";

    private MD5Utils() {
    }

    public static boolean checkZipFileMD5(File file, String md5) {
        if(!TextUtils.isEmpty(md5) && file != null && file.exists()) {
            String calculatedDigest = calculateMD5(file);
            if(calculatedDigest == null) {
                Logger.e("MD5UTILS", "calculatedDigest null", new Object[0]);
                return false;
            } else {
                Logger.v("MD5UTILS", "Calculated digest: " + calculatedDigest, new Object[0]);
                Logger.v("MD5UTILS", "Provided digest: " + md5, new Object[0]);
                return TextUtils.equals(calculatedDigest, md5);
            }
        } else {
            Logger.e("MD5UTILS", "MD5 string empty or updateFile null", new Object[0]);
            return false;
        }
    }

    public static String calculateMD5(File updateFile) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException var15) {
            Logger.e("MD5UTILS", "Exception while getting digest", new Object[]{var15});
            return null;
        }

        FileInputStream is;
        try {
            is = new FileInputStream(updateFile);
        } catch (FileNotFoundException var14) {
            Logger.e("MD5UTILS", "Exception while getting FileInputStream", new Object[]{var14});
            return null;
        }

        byte[] buffer = new byte[8192];

        String var8;
        try {
            int read;
            while((read = is.read(buffer)) > 0) {
                digest.update(buffer, 0, read);
            }

            byte[] md5sum = digest.digest();
            BigInteger bigInt = new BigInteger(1, md5sum);
            String output = bigInt.toString(16);
            output = String.format("%32s", new Object[]{output}).replace(' ', '0');
            var8 = output;
        } catch (IOException var16) {
            throw new RuntimeException("Unable to process file for MD5", var16);
        } finally {
            if (is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            IOUtils.closeSilently(is);
        }

        return var8;
    }

    public static String calScriptFileMd5(File file) throws IOException {
        return getStringMd5(FileUtil.getFileContent(file.getPath()));
    }

    public static String getStringMd5(String str) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] array = messageDigest.digest(str.getBytes("UTF-8"));
            StringBuffer sb = new StringBuffer();

            for(int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString(array[i] & 255 | 256).substring(1, 3));
            }

            return sb.toString();
        } catch (Exception var5) {
            Logger.e("error", var5);
            return "";
        }
    }
}
