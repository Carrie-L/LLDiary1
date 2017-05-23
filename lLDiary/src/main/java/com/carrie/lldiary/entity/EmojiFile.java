package com.carrie.lldiary.entity;

import android.content.Context;

import java.util.Date;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * 带有url地址的Emoji文件列表
 * Created by Administrator on 2016/5/2 0002.
 */
public class EmojiFile extends BmobObject{
    public BmobFile urlFile;
    public String   EmojiName;
    public BmobFile Emoji;


    @Override
    public String toString() {
        return "EmojiFile{" +
                "urlFile=" + urlFile +
                ", EmojiName='" + EmojiName + '\'' +
                ", Emoji=" + Emoji +
                '}';
    }

    public String toString(Context context) {
        return"啦啦啦EmojiFile{" +

                ", EmojiName='" + EmojiName + '\'' +
                ", Emoji.getFileUrl=" + Emoji.getFileUrl(context) +
                ", Emoji.getFilename=" + Emoji.getFilename() +
                '}';
    }
}
