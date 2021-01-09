package com.project.app.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.project.app.MyApp;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class FileManager {
    private final Context mContext;

    public FileManager(Context context){
        this.mContext = context;
    }

    public void save(String fileName,String fileContent) throws IOException {
        FileOutputStream output = mContext.openFileOutput(fileName,Context.MODE_PRIVATE);
        output.write(fileContent.getBytes());
        output.close();
    }


    public String read(String fileName) throws IOException {
        StringBuilder sb = new StringBuilder();
        FileInputStream inputStream = mContext.openFileInput(fileName);
        byte[] temp = new byte[1024];
        int len;
        while ((len = inputStream.read(temp)) >0){
            String cell = new String(temp,0,len);
            sb.append(cell);
        }
        inputStream.close();
        return sb.toString();
    }

    /**
     * 读取assert文件资源
     * @param fileName
     * @return
     */
    public static String readAssetFile(String fileName){
        String data = "";
        try {
            AssetManager manager = MyApp.mContext.getAssets();
            InputStreamReader inputStreamReader; //使用IO流读取json文件内容
            inputStreamReader = new InputStreamReader(manager.open(fileName), StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(inputStreamReader);//使用字符高效流
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = br.readLine())!=null){
                builder.append(line);
            }
            br.close();
            inputStreamReader.close();
            return builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * 获取本地文件真实 uri
     *
     * @param contentUri
     * @return
     */
    public String getRealPathFromUri(Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = mContext.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }





}
