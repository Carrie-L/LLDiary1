package com.carrie.lldiary.helper;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;

import com.carrie.lldiary.utils.LogUtil;

/**
 * Created by new on 2017/4/17.
 */

public class TestGestureListener extends GestureDetector.SimpleOnGestureListener {
    private static final String TAG = "TestGestureListener";
    private final int verticalMinistance = 20;            //水平最小识别距离
    private final int minVelocity = 10;           //最小识别速度

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        //大于设定的最小滑动距离并且在水平/竖直方向速度绝对值大于设定的最小速度，则执行相应方法
        if(e1.getX()-e2.getX() > verticalMinistance && Math.abs(velocityX) > minVelocity){
            LogUtil.e(TAG,"turn left");
        }else if(e2.getX() - e1.getX() > verticalMinistance && Math.abs(velocityX) > minVelocity){
            LogUtil.e(TAG,"turn right");
        }else if(e1.getY()-e2.getY() > 20 && Math.abs(velocityY) > 10){
            LogUtil.e(TAG,"turn up");
        }else if(e2.getY()-e1.getY() > 20 && Math.abs(velocityY) > 10){
            LogUtil.e(TAG,"turn down");
        }
        return false;
    }


}
