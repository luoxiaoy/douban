//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ly.douban.support.utils;

import android.text.TextUtils;

import com.ly.douban.support.logger.Logger;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileUtil {
    public FileUtil() {
    }

    public static boolean checkFileExists(String path) {
        if(!TextUtils.isEmpty(path)) {
            throw new IllegalArgumentException("Path cannot be empty");
        } else {
            File newFile = new File(path);
            return newFile.exists() && newFile.isFile();
        }
    }

    public static void renameToFile(String oldName, String newName) {
        File oldFile = new File(oldName);
        oldFile.renameTo(new File(newName));
    }

    public static String getFileContent(String filePath) throws IOException {
        BufferedInputStream in = null;

        try {
            File file = new File(filePath);
            if(!file.exists()) {
                String var10 = "";
                return var10;
            } else {
                in = new BufferedInputStream(new FileInputStream(file));
                int totalLen = (int)file.length();
                byte[] buffer = new byte[totalLen];

                for(int readCount = 0; readCount < totalLen; readCount += in.read(buffer, readCount, Math.min(totalLen - readCount, 4096))) {
                    ;
                }

                String var6 = new String(buffer, "UTF-8");
                return var6;
            }
        } finally {
            IOUtils.closeSilently(in);
        }
    }

    public static boolean writeContentToFile(String filePath, String content) throws IOException {
        PrintWriter writer = null;

        boolean var4;
        try {
            File file = new File(filePath);
            if(!file.exists() && !file.createNewFile()) {
                var4 = false;
                return var4;
            }

            writer = new PrintWriter(new FileOutputStream(file));
            writer.println(content);
            writer.close();
            var4 = true;
        } finally {
            if(writer != null) {
                writer.close();
            }

        }

        return var4;
    }

    public static void createParentDirIfNeeded(File file) throws IOException {
        File parentDir = file.getParentFile();
        if(!parentDir.exists()) {
            createParentDirIfNeeded(parentDir.getParentFile());
            createDir(parentDir);
        }

    }

    public static File createFile(String filePath) throws IOException {
        File file = new File(filePath);
        if(!file.exists()) {
            createParentDirIfNeeded(file);
            file.createNewFile();
        }

        return file;
    }

    public static File createDir(String dirPath) throws IOException {
        File file = new File(dirPath);
        if(!file.exists()) {
            createParentDirIfNeeded(file);
            file.mkdirs();
        }

        return file;
    }

    public static long dirSize(File directory) {
        long length = 0L;
        if(directory.listFiles() != null) {
            File[] var3 = directory.listFiles();
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                File file = var3[var5];
                if(file.isFile()) {
                    length += file.length();
                } else {
                    length += dirSize(file);
                }
            }
        }

        return length;
    }

    public static void createDir(File dir) throws IOException {
        if(dir == null) {
            throw new IllegalArgumentException("dir cannot be null");
        } else if(!dir.exists() || !dir.isDirectory()) {
            if(!dir.mkdirs()) {
                throw new IOException(String.format("Failed to create dir path %s", new Object[]{dir.getAbsolutePath()}));
            }
        }
    }

    public static void deleteDir(File file) {
        if(file != null && file.exists()) {
            String deleteCmd = "rm -r " + file.getAbsolutePath();
            Logger.d("spider_del", "deleteCmd %s", new Object[]{deleteCmd});
            Runtime runtime = Runtime.getRuntime();

            try {
                runtime.exec(deleteCmd);
            } catch (IOException var4) {
                ;
            }
        }

    }

    public static void cleanDir(File dir) {
        if(dir != null && dir.isDirectory()) {
            String[] children = dir.list();

            for(int i = 0; i < children.length; ++i) {
                (new File(dir, children[i])).delete();
            }
        }

    }

    public static void deleteFile(String filePath) {
        if(null != filePath) {
            File file = new File(filePath);
            if(file.exists()) {
                file.delete();
            }
        }

    }

    public static void unPackZip(File desFile, File zipFile) {
        try {
            InputStream is = new FileInputStream(zipFile);
            ZipInputStream zis = new ZipInputStream(new BufferedInputStream(is));
            byte[] buffer = new byte[1024];

            while(true) {
                ZipEntry ze;
                while((ze = zis.getNextEntry()) != null) {
                    String filename = ze.getName();
                    if(ze.isDirectory()) {
                        File fmd = new File(desFile.getPath() + File.separator + filename);
                        fmd.mkdirs();
                    } else {
                        FileOutputStream fout = new FileOutputStream(new File(desFile.getPath() + File.separator + filename));

                        int count;
                        while((count = zis.read(buffer)) != -1) {
                            fout.write(buffer, 0, count);
                        }

                        fout.close();
                        zis.closeEntry();
                    }
                }

                zis.close();
                break;
            }
        } catch (IOException var9) {
            Logger.e("error", var9);
        }

    }

    public static boolean unpackToOverrideInDir(InputStream inputStream, File destDir) {
        ZipInputStream zis = null;

        boolean var4;
        try {
            zis = new ZipInputStream(new BufferedInputStream(inputStream));
            byte[] buffer = new byte[1024];

            ZipEntry ze;
            while((ze = zis.getNextEntry()) != null) {
                String filename = ze.getName();
                File destFile = new File(destDir, filename);
                if(!destFile.exists()) {
                    if(ze.isDirectory()) {
                        File fmd = new File(destDir, filename);
                        fmd.mkdirs();
                    } else {
                        FileOutputStream fout = new FileOutputStream(destFile);

                        int count;
                        while((count = zis.read(buffer)) != -1) {
                            fout.write(buffer, 0, count);
                        }

                        fout.close();
                        zis.closeEntry();
                    }
                }
            }

            zis.close();
            zis = null;
            boolean var20 = true;
            return var20;
        } catch (IOException var17) {
            Logger.e("error", var17);
            var4 = false;
        } finally {
            if(zis != null) {
                try {
                    zis.close();
                } catch (IOException var16) {
                    var16.printStackTrace();
                }
            }

        }

        return var4;
    }
}
