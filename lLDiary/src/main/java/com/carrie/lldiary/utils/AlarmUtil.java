package com.carrie.lldiary.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.carrie.lldiary.service.AlarmReceiver;

/**
 * Created by Administrator on 2016/7/9 0009.
 */
public class AlarmUtil {
    public static final int PLAN_ALARM=0;

    public static void setAlarm(Context context,long timeMills,String title){
        Intent intent = new Intent(context,
                AlarmReceiver.class);
        intent.putExtra("Title",title);
        intent.putExtra("Time",timeMills);
        PendingIntent pi = PendingIntent.getBroadcast(
                context, PLAN_ALARM, intent, 0);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.set(AlarmManager.RTC_WAKEUP,
                timeMills, pi);
    }
}
