package com.carrie.lldiary.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;

public class MyActivityManager {
	public static List<Activity> activities=new ArrayList<Activity>();
	private Context context;
	
	public static void add(Activity activity){
		activities.add(activity);
	}
	
	public static void finish(){
		for (Activity activity:activities) {
			activity.finish();
		}
	}
	
	
	

}
