package com.m520it.mobilephone.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamUtil {
    public static String readFromStream(InputStream is){
        //利用字节数组流
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        int len = 0;
        byte[] buffer = new byte[1024];
        try {
            while((len=is.read(buffer))!=-1){
                os.write(buffer,0,len);
            }
            String result = os.toString();
            is.close();
            os.close();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
