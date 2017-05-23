package com.carrie.lldiary.helper;

import android.content.Context;

import org.json.JSONArray;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindCallback;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by new on 2017/4/14.
 * 关于Bmob的一些操作
 */

public class BmobHelper {
    private final String TAG="BmobHelper";
    public static final String COLUMN_USER_NAME="username";
    public static final String COLUMN_PHONE_NUMBER="mobilePhoneNumber";
    public static final String COLUMN_EMAIL="email";

    /**
     * 查找用户是否存在
     * @param context
     * @param column 列名
     * @param value 列值
     * @param findListener 回调
     */
    public static void findUser(Context context,String column, String value,FindListener<BmobUser> findListener){
        BmobQuery<BmobUser> query = new BmobQuery<>();
        query.addWhereEqualTo(column, value);
        query.findObjects(context, findListener);
    }

    /**
     * 更新用户
     * @param context
     * @param newUser <p>BmobUser newUser = new BmobUser();
                        newUser.setEmail("xxx@163.com");
     * @param updateListener
     */
    public static void updateUser(Context context,BmobUser newUser,UpdateListener updateListener){
        BmobUser bmobUser = BmobUser.getCurrentUser(context);
        newUser.update(context,bmobUser.getObjectId(),updateListener);
    }

}
