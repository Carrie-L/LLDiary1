package com.carrie.lldiary;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.inputmethod.InputMethodManager;

import com.baidu.mobstat.StatService;
import com.carrie.lldiary.dao.DaoMaster;
import com.carrie.lldiary.dao.DaoSession;
import com.carrie.lldiary.dao.Diary;
import com.carrie.lldiary.dao.DiaryDao;
import com.carrie.lldiary.helper.DBHelper;
import com.carrie.lldiary.helper.OldDBHelper;
import com.carrie.lldiary.utils.AppUtils;
import com.carrie.lldiary.utils.Constant;
import com.carrie.lldiary.utils.CrashHandler;
import com.carrie.lldiary.utils.LogUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.bmob.v3.AsyncCustomEndpoints;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.CloudCodeListener;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class App extends Application {
    public static final String ApplicationID = "462f212d06c70915ebe3077252a9e9af";
    private static final String TAG = "App";

    public static SharedPreferences mSP_style;
    public static SharedPreferences mSP_account;
    public static SharedPreferences mSP_config;
    public static BmobUser mCurrUser;

    public static String mRootDir;
    public static String mPackageName;

    public static InputMethodManager mInputManager;

    private static final String DATABASE_NAME = "LLDiary.db";

    private static DaoMaster daoMaster;
    private static DaoSession daoSession;
    public static SQLiteDatabase db;
    public static DBHelper dbHelper;
    public static String mAppVersion;
    public static int mVersionCode;
    private long mDiaryId;

    @Override
    public void onCreate() {
        super.onCreate();

        LogUtil.i(TAG, "初始化APP");

        mSP_style = getSharedPreferences(Constant.SP_STYLE, Context.MODE_PRIVATE);
        mSP_account = getSharedPreferences(Constant.SP_ACCOUUNT, Context.MODE_PRIVATE);
        mSP_config = getSharedPreferences(Constant.SP_CONFIG, Context.MODE_PRIVATE);

        //--------初始化数据库----------------------
        dbHelper = DBHelper.getInstance(getApplicationContext());
        //------------------------------------------

        //-------------------初始化Fresco------------------------------------
//		ImagePipelineConfig config = ImagePipelineConfig.newBuilder(getApplicationContext())
//				.setBitmapMemoryCacheParamsSupplier(bitmapCacheParamsSupplier)
//				.setCacheKeyFactory(cacheKeyFactory)
//				.setEncodedMemoryCacheParamsSupplier(encodedCacheParamsSupplier)
//				.setExecutorSupplier(executorSupplier)
//				.setImageCacheStatsTracker(imageCacheStatsTracker)
//				.setMainDiskCacheConfig(mainDiskCacheConfig)
//				.setMemoryTrimmableRegistry(memoryTrimmableRegistry)
//				.setNetworkFetchProducer(networkFetchProducer)
//				.setPoolFactory(poolFactory)
//				.setProgressiveJpegConfig(progressiveJpegConfig)
//				.setRequestListeners(requestListeners)
//				.setSmallImageDiskCacheConfig(smallImageDiskCacheConfig)
//				.build();
        //-------------------------------------------------------
        Fresco.initialize(getApplicationContext());

        mRootDir = AppUtils.getFileRootDir(getApplicationContext());
        mPackageName = getPackageName();
        LogUtil.i(TAG, "mRootDir=" + mRootDir);
        LogUtil.i(TAG, "mPackageName=" + mPackageName);

        mInputManager = (InputMethodManager) (getApplicationContext()).getSystemService(Context.INPUT_METHOD_SERVICE);

        // 初始化 Bmob SDK
        // 使用时请将第二个参数Application ID替换成你在Bmob服务器端创建的Application ID
        Bmob.initialize(this, ApplicationID);

        // 初始化界面颜色
        if (App.mSP_style.getInt(Constant.STYLE_ALPHA, 0) == 0 && App.mSP_style.getInt(Constant.STYLE_RED, 0) == 0
                && App.mSP_style.getInt(Constant.STYLE_GREEN, 0) == 0 && App.mSP_style.getInt(Constant.STYLE_BLUE, 0) == 0) {
            AppUtils.setHomeColor(0, 199, 140, 255);
        }

        mCurrUser = BmobUser.getCurrentUser(getApplicationContext());

        MobclickAgent.updateOnlineConfig(getApplicationContext());// 友盟统计发送策略
        AnalyticsConfig.enableEncrypt(true);// 加密发送日志

        StatService.setDebugOn(true);

        // Capturing the error information
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());

        initMob();
        try {
            sendSMS();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        getPackageVersion();
        transOldDataToNewDB();


        boolean hasUpdate = App.mSP_config.getBoolean("DB_UPDATE", false);
        LogUtil.e(TAG, "hasUpdate=" + hasUpdate);
        if (hasUpdate) {
            LogUtil.e(TAG, "updateDB---");
            updateDB();
            if (mDiaryId > 0) {//因为id从1开始自增，所以如果插入成功，返回的id一定是大于0的
                App.mSP_config.edit().putBoolean("DB_UPDATE", false).apply();
            }
        }
    }

    private void updateDB() {
        //第一步，先将原有数据全部储存到TempDb中
        //第二步，删除现有表，新建新表

        //第一步，获取是哪个表发生了改变
        //第二步，将这个表中的所有数据存储到临时表中
        //第三部，新建新表
        //第四步，将数据从临时表中转移到新表中
        //第五步，删除临时表

        String sqlDiary = "ALTER TABLE DIARY RENAME TO DIARY_TEMP";
        db.execSQL(sqlDiary);
        //		String sqlAnn = "ALTER TABLE ANN RENAME TO ANN_TEMP";
//		db.execSQL(sqlAnn);

        DiaryDao.createTable(db, true);
        Cursor cursor = db.rawQuery("SELECT * FROM DIARY_TEMP", null);
        Diary diary;
        if (cursor != null) {
            while (cursor.moveToNext()) {
                diary = new Diary();
                diary.setData(cursor, diary);
                LogUtil.i("DaoMaster", "diary=" + diary.toString());
                mDiaryId = App.dbHelper.insertDiary(diary);
                LogUtil.i("DaoMaster", "mDiaryId=" + mDiaryId);
            }
            cursor.close();
        }
    }

    private void getPackageVersion() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            mAppVersion = packageInfo.versionName;
            mVersionCode = packageInfo.versionCode;
            LogUtil.i(TAG, "mAppVersion=" + mAppVersion + ",mVersionCode=" + mVersionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void transOldDataToNewDB() {
        OldDBHelper.getOldAnnData();
    }

    private void initMob() {
        SMSSDK.initSDK(this, "1cfabca5f4276", "233769319dc20ff36cda8c703d3c4463");
    }

    private void sendSMS() throws JSONException {
        //test对应你刚刚创建的云端逻辑名称
        String cloudCodeName = "sendSMS";
        JSONObject params = new JSONObject();
//name是上传到云端的参数名称，值是bmob，云端逻辑可以通过调用request.body.name获取这个值
        params.put("name", "SMS");
//创建云端逻辑对象
        AsyncCustomEndpoints cloudCode = new AsyncCustomEndpoints();
//异步调用云端逻辑
        cloudCode.callEndpoint(this, cloudCodeName, params, new CloudCodeListener() {

            //执行成功时调用，返回result对象
            @Override
            public void onSuccess(Object result) {
                LogUtil.e(TAG, "result = " + result.toString());
                AppUtils.sendSms(getApplicationContext());
            }

            @Override
            public void onFailure(int i, String s) {
                LogUtil.e(TAG, "BmobException = " + s);
            }
        });
    }


    /**
     * <font color="#f97798">关闭软键盘</font>
     *
     * @param context <font color="#f97798"></font>
     * @return void
     * @version 创建时间：2016年4月28日 下午6:00:41
     */
    public void hideSoftKeyboard(Context context) {

    }

    public static DaoMaster getDaoMaster(Context context) {
        if (daoMaster == null) {
            DaoMaster.OpenHelper openHelper = new DaoMaster.DevOpenHelper(context, DATABASE_NAME, null);
            db = openHelper.getWritableDatabase();
            daoMaster = new DaoMaster(db);
        }
        return daoMaster;
    }

    public static DaoSession getDaoSession(Context context) {
        if (daoSession == null) {
            if (daoMaster == null) {
                daoMaster = getDaoMaster(context);
            }
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }

    public static SQLiteDatabase getDb(Context context) {
        if (daoSession == null) {
            if (daoMaster == null) {
                daoMaster = getDaoMaster(context);
            }
            db = daoMaster.getDatabase();
        }
        return db;
    }


}
