package com.carrie.lldiary.service;

import org.android.agoo.client.BaseConstants;
import org.json.JSONObject;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.carrie.lldiary.activity.ConversationDetailActivity;
import com.umeng.fb.ConversationActivity;
import com.umeng.fb.push.FeedbackPush;
import com.umeng.message.UmengBaseIntentService;
import com.umeng.message.entity.UMessage;




public class MyPushIntentService extends UmengBaseIntentService{
	private static final String TAG = MyPushIntentService.class.getName();

	@Override
	protected void onMessage(Context context, Intent intent) {
		super.onMessage(context, intent);
        FeedbackPush.getInstance(context).init(ConversationActivity.class,true);
        Log.d(TAG, "onMessage");
        if(FeedbackPush.getInstance(context).onFBMessage(intent)) {
            //The push message is reply from developer.
        	Intent intent2=new Intent(context,ConversationDetailActivity.class);
            PendingIntent piIntent=PendingIntent.getActivity(context, 0,
    				intent2, PendingIntent.FLAG_CANCEL_CURRENT);
            
            return;
        }

        //The push message is not reply from developer.
        /*************** other code ***************/
        try {
            //鍙互閫氳繃MESSAGE_BODY鍙栧緱娑堟伅浣�
            String message = intent.getStringExtra(BaseConstants.MESSAGE_BODY);
            UMessage msg = new UMessage(new JSONObject(message));
            Log.d(TAG, "message="+message);    //娑堟伅浣�
            Log.d(TAG, "custom="+msg.custom);    //鑷畾涔夋秷鎭殑鍐呭
            Log.d(TAG, "title="+msg.title);    //閫氱煡鏍囬
            Log.d(TAG, "text="+msg.text);    //閫氱煡鍐呭
            // code  to handle message here
            // ...
            
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }
}
