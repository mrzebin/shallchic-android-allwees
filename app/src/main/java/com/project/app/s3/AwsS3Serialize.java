package com.project.app.s3;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class AwsS3Serialize {
    /**
     * 序列化对象
     *
     * @param object 类对象
     * @return 序列化后的字符串
     */
    public static String serializeObject(Object object) {
        ByteArrayOutputStream byteArrayOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        String serStr;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            objectOutputStream = new ObjectOutputStream(
                    byteArrayOutputStream);
            objectOutputStream.writeObject(object);
            serStr = byteArrayOutputStream.toString(StandardCharsets.ISO_8859_1.toString());
            serStr = java.net.URLEncoder.encode(serStr, StandardCharsets.UTF_8.toString());
        } catch (IOException ex) {
            ex.printStackTrace();
            serStr = null;
        } finally {
            if (objectOutputStream != null)
                try {
                    objectOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (byteArrayOutputStream != null)
                try {
                    byteArrayOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

        return serStr;
    }

    /**
     * 反序列化对象
     *
     * @param str 对象
     * @return 反序列化后的对象
     */
    public static Object deSerializeObject(String str) {
        String redStr = null;
        try {
            redStr = URLDecoder.decode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ByteArrayInputStream byteArrayInputStream = null;
        ObjectInputStream objectInputStream = null;
        Object object;
        try {
            byteArrayInputStream = new ByteArrayInputStream(
                    redStr.getBytes(StandardCharsets.ISO_8859_1));
            objectInputStream = new ObjectInputStream(
                    byteArrayInputStream);
            object = objectInputStream.readObject();
        } catch (Exception ex) {
            ex.printStackTrace();
            object = null;
        } finally {
            if (objectInputStream != null)
                try {
                    objectInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (byteArrayInputStream != null)
                try {
                    byteArrayInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return object;
    }
}
