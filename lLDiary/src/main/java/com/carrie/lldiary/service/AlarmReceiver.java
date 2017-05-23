package com.carrie.lldiary.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.carrie.lldiary.R;
import com.carrie.lldiary.activity.PlanActivity;
import com.carrie.lldiary.utils.AlarmUtil;
import com.carrie.lldiary.utils.LogUtil;

public class AlarmReceiver extends BroadcastReceiver{

	private static final String TAG = "AlarmReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		Toast.makeText(context, "闹铃响了, 可以做点事情了~~", Toast.LENGTH_LONG).show();
		System.out.println("闹铃响了, 可以做点事情了~~");

		String title=intent.getStringExtra("Title");
		long timeMills=intent.getLongExtra("Time",0);
		LogUtil.i(TAG,"title="+title);
		LogUtil.i(TAG,"timeMills="+timeMills);

		NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification.Builder builder=new Notification.Builder(context);
		builder.setContentTitle(title).setContentText("Just do it~").setSmallIcon(R.drawable.logo).setTicker(title).setWhen(timeMills)
				.setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE)
				.setContentIntent(getDefaultIntent(Notification.FLAG_AUTO_CANCEL,context));
		intent = new Intent(context, PlanActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent pi = PendingIntent.getActivity(context, AlarmUtil.PLAN_ALARM,
				intent, PendingIntent.FLAG_CANCEL_CURRENT);
		manager.notify(1, builder.build());
	}

	public PendingIntent getDefaultIntent(int flags,Context context){
		PendingIntent pendingIntent= PendingIntent.getActivity(context, 1, new Intent(), flags);
		return pendingIntent;
	}

}
