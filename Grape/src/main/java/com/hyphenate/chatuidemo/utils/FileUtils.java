package com.hyphenate.chatuidemo.utils;

import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by cheng on 16-12-2.
 */
public class FileUtils {
    public static final String FOLDER = "Grape";

    /**
     * 获取文件夹路径
     * @param tem
     * @return
     */
    public static String getFolder(boolean tem){
        if (tem)
            return Environment.getExternalStorageDirectory() + "/" + FOLDER + "/";
        else
            return Environment.getExternalStorageDirectory() + "/" + FOLDER;
    }

    /**
     * 检测sd卡是否挂载
     * @return
     */
    public static boolean isSdMounted(){
        String status = Environment.getExternalStorageState();
        return status.equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 创建grape文件夹
     * @return
     */
    public static boolean createFolder(){
        if (isSdMounted()) {
            String base_dir = Environment.getExternalStorageDirectory() + "/" + FOLDER;
            File dir = new File(base_dir);
            if (!dir.exists())
                return dir.mkdir();
            else
                return true;
        }
        return false;
    }

    /**
     * 检测文件是否存在
     * @param file
     * @return
     */
    public static boolean hasFile(File file){
        if (file.exists())
            return true;
        return false;
    }

    /**
     * 删除文件
     * @param file
     * @return
     */
    public static boolean deleteFile(File file){
        if(hasFile(file)){
            file.delete();
            return true;
        }
        return false;
    }

    /**
     * 文件重命名
     * @param from
     * @param to
     * @return
     */
    public static boolean renameFile(File from, File to){
        if(CopyFile(from, to) && deleteFile(from)){
            return true;
        }
        return false;
    }

    /**
     * 获取文件的路径
     * @return
     */
    public static Uri getTempUri(String path){
        String status = Environment.getExternalStorageState();
        // 已挂载而且有读写权限
        if (status.equals(Environment.MEDIA_MOUNTED)){
            File f = new File(getFolder(true)+ path);
            try {
                f.createNewFile();
                return Uri.fromFile(f);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    /**
     * 复制文件from为to
     * @param from
     * @param to
     * @return
     */
    public static boolean CopyFile(File from, File to){
        try {
            InputStream in = new FileInputStream(from);
            OutputStream out = new FileOutputStream(to);
            byte[] buf = new byte[1024];
            int numread;
            while ((numread = in.read(buf)) > 0){
                out.write(buf, 0, numread);
            }
            in.close();
            out.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e("TAG", "CopyFile: "+e );
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("TAG", "CopyFile: "+e );
        }
        return false;
    }

    /**
     * 删除目录
     * @param file
     */
    public static void deleteCatagory(File file){
        if(!hasFile(file)) return;
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                file.delete();
                return;
            }
            for (File f : childFile) {
                deleteCatagory(f);
            }
            file.delete();
        }
    }
}
