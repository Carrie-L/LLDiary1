package com.carrie.lldiary.base;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * Created by new on 2017/4/14.
 */

public abstract class BaseHandler<T> extends Handler {
    private WeakReference<T> reference;

    public BaseHandler(T activity) {
        reference = new WeakReference<>(activity);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        handleMsg(msg,reference.get());
    }

    public abstract void handleMsg(Message msg, T t);
}
