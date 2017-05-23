package com.carrie.lldiary.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.os.Message;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.carrie.lldiary.App;
import com.carrie.lldiary.R;
import com.carrie.lldiary.entity.DiaryBg;
import com.carrie.lldiary.helper.Sorter;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.RequestSMSCodeListener;

/**
 * 封装方法
 * 
 * @Description TODO
 * @author Carrie
 * @version 创建时间：2016年4月26日 10:24:25
 */
public class AppUtils {

	private static final String TAG = "AppUtils";
	public String str;
	private static int mHomeColor;

	/**
	 * <font color="#f97798">检测输入值是否为空</font>
	 *
	 * @param context
	 * @param text
	 *            <font color="#f97798">EditText输入值</font>
	 * @param toastStr
	 *            <font color="#f97798">提示语</font>
	 * @return void
	 * @version 创建时间：2016年4月26日 11:18:53
	 */
	public static void checkTextEmpty(Context context, String text, String toastStr) {
		if (TextUtils.isEmpty(text)) {
			toast1(context, toastStr);
			return;
		}

	}

	/**
	 *
	 * <font color="#f97798">提示LENGTH_LONG</font>
	 *
	 * @param context
	 * @param text
	 *            <font color="#f97798">提示语</font>
	 * @return void
	 * @version 创建时间：2016年4月26日 下午12:34:49
	 */
	public static void toast1(Context context, String text) {
		Toast.makeText(context, text, Toast.LENGTH_LONG).show();
	}

	public static void toast11(Context context, int resid) {
		Toast.makeText(context, context.getString(resid), Toast.LENGTH_LONG).show();
	}

	/**
	 *
	 * <font color="#f97798">提示LENGTH_SHORT</font>
	 *
	 * @param context
	 * @param text
	 *            <font color="#f97798">提示语</font>
	 * @return void
	 * @version 创建时间：2016年4月26日 下午12:34:49
	 */
	public static void toast0(Context context, String text) {
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}

	/**
	 * <font color="#f97798">提示LENGTH_SHORT</font>
	 *
	 * @param context
	 * @param resid
	 *            <font color="#f97798">R.string.**</font>
	 * @return void
	 * @version 创建时间：2016年4月27日 下午2:57:55
	 */
	public static void toast00(Context context, int resid) {
		Toast.makeText(context, context.getString(resid), Toast.LENGTH_SHORT).show();
	}

	/**
	 *
	 * <font color="#f97798">邮箱验证</font>
	 *
	 * @return void
	 * @version 创建时间：2016年4月26日 下午1:35:02
	 */
	private void emailVerify() {
		// BmobUser.requestEmailVerify(getApplicationContext(), userEmail, new
		// EmailVerifyListener() {
		// @Override
		// public void onSuccess() {
		// AppUtils.toast1(getApplicationContext(), "邮箱验证成功");
		//
		//
		// });
		// }
		// @Override
		// public void onFailure(int arg0, String arg1) {
		// LogUtil.e(TAG, "邮箱验证失败: "+arg1);
		// AppUtils.toast1(getApplicationContext(), "邮箱验证失败: "+arg1);
		// }
		// });
	}

	public static int setHomeColor(int red, int green, int blue, int alpha) {
		mHomeColor = Color.argb(alpha, red, green, blue);
		App.mSP_style.edit().putInt(Constant.STYLE_ALPHA, alpha).putInt(Constant.STYLE_RED, red).putInt(Constant.STYLE_GREEN, green)
				.putInt(Constant.STYLE_BLUE, blue).commit();
		return Color.argb(alpha, red, green, blue);
	}

	/**
	 * <font color="#f97798">得到当前主题颜色</font>
	 *
	 * @return <font color="#f97798">Color.argb</font>
	 * @version 创建时间：2016年4月26日 下午5:17:31
	 */
	public static int getHomeColor() {
		return Color.argb(App.mSP_style.getInt(Constant.STYLE_ALPHA, 0), App.mSP_style.getInt(Constant.STYLE_RED, 0),
				App.mSP_style.getInt(Constant.STYLE_GREEN, 0), App.mSP_style.getInt(Constant.STYLE_BLUE, 0));
	}

	/**
	 * <font color="#f97798">比主颜色稍浅，Alpha /1.3</font>
	 *
	 * @return <font color="#f97798">Color.argb</font>
	 * @return int
	 * @version 创建时间：2016年4月26日 下午4:06:25
	 */
	public static int getLightHomeColor() {
		return Color.argb((int) (App.mSP_style.getInt(Constant.STYLE_ALPHA, 0) / 1.3), App.mSP_style.getInt(Constant.STYLE_RED, 0),
				App.mSP_style.getInt(Constant.STYLE_GREEN, 0), App.mSP_style.getInt(Constant.STYLE_BLUE, 0));
	}

	/**
	 *
	 * <font color="#f97798">Activity退出动画</font> <font color="#f97798"></font>
	 *
	 * @return void
	 * @version 创建时间：2016年4月26日 下午9:26:54
	 */
	public static void translateExit() {

	}

	/**
	 * <font color="#f97798">改变Hint字体大小</font>
	 *
	 * @param et
	 * @param hintContent
	 * @param hintSize
	 *            <font color="#f97798">要设置的大小</font>
	 * @return void
	 * @version 创建时间：2016年4月27日 下午4:17:26
	 */
	public static void setHintSize(EditText et, String hintContent, int hintSize) {
		// 新建一个可以添加属性的文本对象
		SpannableString ss = new SpannableString(hintContent);
		// 新建一个属性对象,设置文字的大小
		AbsoluteSizeSpan ass = new AbsoluteSizeSpan(hintSize, true);
		// 附加属性到文本
		ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		// 设置hint
		et.setHint(new SpannedString(ss)); // 一定要进行转换,否则属性会消失
	}

	/**
	 * <font color="#f97798">Bmob操作失败监听通用提示</font> <font color="#f97798"></font>
	 *
	 * @return void
	 * @version 创建时间：2016年4月27日 下午4:32:49
	 */
	public static void bmobFailToast(Context context, int arg0) {
		if (arg0 == 9010) {// 网络超时
			AppUtils.toast0(context, context.getString(R.string.toast_network_outtime));
		} else if (arg0 == 9016) {// 无网络连接，请检查您的手机网络。
			AppUtils.toast1(context, context.getString(R.string.toast_network_error));
		} else if (arg0 == 205) {
			AppUtils.toast1(context, context.getString(R.string.toast_email_not_exsits));
		} else if (arg0 == 101) {
			AppUtils.toast1(context, context.getString(R.string.toast_login_failed_incorrect));
		} else if (arg0 == 202) {// 用户名已存在
			AppUtils.toast1(context, context.getString(R.string.toast_modify_username_failed));
		} else if (arg0 == 203) {// 邮箱已存在
			AppUtils.toast1(context, context.getString(R.string.toast_modify_useremail_failed));
		}

	}

	public static String SD_ROOT_DIR = "";
	private static String mFileRootDir = "";
	private static String FILE_PATH;
	private static final int FILE_SIZE = 1024;

	static {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			FILE_PATH = Environment.getExternalStorageDirectory() + "/";
		} else {
			FILE_PATH = Environment.getDataDirectory() + "/";
			// FILE_PATH = "/data" +
			// Environment.getDataDirectory().getAbsolutePath() +
			// "/"+SD_ROOT_DIR+"file/";
		}
	}

	/**
	 * <font color="#f97798">获取文件根目录</font>
	 *
	 * @param context
	 * @return <font color="#f97798">/com.carrie.lldiary/</font>
	 * @version 创建时间：2016年4月27日 下午7:41:11
	 */
	public static String getFileRootDir(Context context) {
		if (TextUtils.isEmpty(SD_ROOT_DIR)) {
			SD_ROOT_DIR = context.getApplicationInfo().processName + "/";
		}

		if (TextUtils.isEmpty(mFileRootDir))
			mFileRootDir = FILE_PATH + SD_ROOT_DIR;

		File dirFile = new File(mFileRootDir);
		if (!dirFile.exists() || !dirFile.isDirectory())
			dirFile.mkdirs();

		File nomediafFile = new File(mFileRootDir + ".nomedia");
		try {
			if (!nomediafFile.exists())
				nomediafFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Log.d(TAG, "getFileRootDir:" + mFileRootDir);
		return mFileRootDir;
	}

	/**
	 * 获取资源文件URI
	 *
	 * @param resId
	 * @return
	 */
	public static Uri getResourceUri(int resId) {
		return Uri.parse("android.resource://" + App.mPackageName + "/" + resId);
	}

	/**
	 * 判断email格式是否正确
	 *
	 * @param email
	 * @return
	 */
	public static boolean isEmailValid(String email) {
		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);

		return m.matches();
	}

	/**
	 * <font color="#f97798">打印Bmob失败Log</font>
	 * @param text
	 * @param arg0
	 * @param arg1 <font color="#f97798"></font>
	 * @return void
	 * @version 创建时间：2016年4月28日 上午9:55:14
	 */
	public static  void failedLog(String text,int arg0,String arg1 ){
		LogUtil.e(TAG, text+"，arg0="+arg0+"，arg1="+arg1);
	}

	/**
	 * <font color="#f97798">avatar保存路径</font>
	 * @param userName
	 * @return <font color="#f97798"></font>
	 * @return String
	 * @version 创建时间：2016年4月28日 上午10:50:01
	 */
	public static String avatarPath(String userName){
		StringBuffer path=new StringBuffer();
		path.append(App.mRootDir).append("avatar_").append(userName).append(".png");
		return path.toString();
	}

	/**
	 *
	 * <font color="#f97798">隐藏软键盘</font> <font color="#f97798"></font>
	 * @return void
	 * @version 创建时间：2016年4月28日 下午6:26:02
	 */
	public static void closeSoftKb(Context context){
		View view =((Activity) context).getWindow().peekDecorView();
        if (view != null) {
            App.mInputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
	}

	public static <T> ArrayList<T> initList(ArrayList<T> lists){
		if(lists==null){
			lists=new ArrayList<T>();
		}else{
			lists.clear();
		}

		return lists;

	}

	public static <T> ArrayList<ArrayList<T>> initListList(ArrayList<ArrayList<T>> lists){
		if(lists==null){
			lists=new ArrayList<ArrayList<T>>();
		}else{
			lists.clear();
		}

		return lists;

	}

	/**
	 * 打印lists.size()
	 * @param lists
	 * @param <T>
     */
	public static <T> void logListSize(ArrayList<T> lists){
		LogUtil.i(TAG,lists+"="+lists.size());
	}

	/**
	 * 打印lists.size() + lists.toString()
	 * @param lists
	 * @param <T>
     */
	public static <T> void logArrayListToString(ArrayList<T> lists){
		LogUtil.i(TAG,">>> size()="+lists.size()+"……"+lists.toString());
	}

	public static <T> void logListToString(String text,List<T> lists){
		LogUtil.i(TAG,text+">>> size()="+lists.size()+"……"+lists.toString());
	}

	/**
	 * 得到百分比
	 * @param a  除数
	 * @param b  被除数
     * @return  df.format((a*100)/b+"%")
     */
	public static String get2Decimal(int a,int b){
		DecimalFormat df=new DecimalFormat();
		df.setMaximumIntegerDigits(3);
		df.setMinimumIntegerDigits(1);
		return df.format((a*100)/b)+"%";
	}

	/**
	 * 获取字符串 截断符号 的后面一串
	 * @param str abc.jpg
	 * @param c '.'
     * @return jpg
     */
	public static String getSubLastStr(String str,char c){
		return str.substring(str.lastIndexOf(c)+1);
	}

	/**
	 * 获取字符串 截断符号 的前面一串
	 * @param str abc.jpg
	 * @param c '.'
	 * @return abc
	 */
	public static String getSubFrontStr(String str,char c){
		return str.substring(0,str.length()-str.substring(str.lastIndexOf(c)-1).length()+1);
	}

	/**
	 * 获取字符串 截断符号 的前面一串 + 符号本身
	 * @param str adc/adb.jpg
	 * @param c '/'
     * @return adc/
     */
	public static String getSubFrontStr2(String str,char c){
		return str.substring(0,str.length()-str.substring(str.lastIndexOf(c)-1).length()+2);
	}

	/**
	 * 在Config.xml中存入信息
	 * @param context
	 * @param key
	 * @param value
     */
	public static void setSP_Config_Str(Context context,String key,String value){
		SharedPreferences sp=context.getSharedPreferences(Constant.SP_CONFIG,Context.MODE_PRIVATE);
		SharedPreferences.Editor editor=sp.edit();
		editor.putString(key,value).apply();
	}

	/**
	 * 获取Config.xml中的数据
	 * @param context
	 * @param key
     * @return
     */
	public static String getSP_Config_Str(Context context,String key){
		SharedPreferences sp=context.getSharedPreferences(Constant.SP_CONFIG,Context.MODE_PRIVATE);
		return sp.getString(key,"");
	}

//	public static void sortByInsertTime(ArrayList<Emoji> list) {
//		Comparator<Emoji> comparator = new Comparator<Emoji>() {
//			@Override
//			public int compare(Emoji lhs, Emoji rhs) {
//				if (lhs.toString().compareTo(rhs.toString()) > 0) {
//					return -1;
//				} else if (lhs.compareTo(rhs) < 0) {
//					return 1;
//				} else {
//					return 0;
//				}
//			}
//		};
//		Collections.sort(list, comparator);
//	}

	/**
	 * 插入排序法
	 * @author Andy.Chen
	 * @param <T>
	 *
	 */
	public class InsertionSort<T extends Comparable<T>> extends Sorter<T> {

		@Override
		public void sort(T[] array, int from, int len) {
			T tmp = null;
			for(int i=from+1;i<from+len;i++){
				tmp = array[i];
				int j = i;
				for( ; j>from; j--){
					if(tmp.compareTo(array[j-1]) < 0){
						array[j] = array[j-1];
					}else{
						break;
					}
				}
				array[j] = tmp;
			}
		}

	}

	public static Message sendMsgToHandler(int what,Object obj){
		Message msg=new Message();
		msg.what=what;
		msg.obj=obj;
		return msg;
	}

	/**
	 * 自定义事件统计
	 */
	public static void StatEvent(Context context,String id,String label){
		// 自定义事件次数统计
		StatService.onEvent(context, id,label, 1);
		// 自定义事件时长统计
		StatService.onEventEnd(context, id, label);
	}

	public static boolean backToast(Activity activity,boolean isChanged){
		if(isChanged){
			DialogUtils.alertMDesignDialog(activity,"",activity.getString(R.string.dialog_back));
			return true;
		}
		return false;
	}

	public static void logText(String TAG,String key,String value){
		LogUtil.i(TAG,key+"="+value);
	}

	public static void logInt(String TAG,String key,int value){
		LogUtil.i(TAG,key+"="+value);
	}

	public static boolean isEdited=false;
	public static boolean  isEditTextEdited(EditText editText){
		editText.addTextChangedListener(new TextWatcher() {
			private String editor;
			private int initStart=-1;

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				LogUtil.i(TAG,"beforeTextChanged:s="+s.toString()+",start="+start+",count="+count+",after="+after);
				if(after<10){
					if(after>0&&!TextUtils.isEmpty(s.toString())){
						editor= s.toString();
						initStart=1;
					}else if(after==0&&TextUtils.isEmpty(s.toString())){
						initStart=0;
					}

					LogUtil.i(TAG,"initStart="+initStart);
				}
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				LogUtil.i(TAG,"afterTextChanged:s="+s.toString());
				if(initStart==0&&!TextUtils.isEmpty(s.toString())){
					isEdited=true;
				}else if(initStart==1&&!s.toString().equals(editor)){
					isEdited=true;
				}

			}
		});
		return isEdited;
	}

	public static String getPhoneNumber(){
		return App.mSP_account.getString(Constant.account_phone,"");
	}

	public static boolean isFirstStartApp(){
		return App.mSP_config.getBoolean("isFirstStartApp",true);
	}

	public static int getScreenWidth(){
		return App.mSP_config.getInt("ScreenWidth",0);
	}

	public static int getScreenHeight(){
		return App.mSP_config.getInt("ScreenHeight",0);
	}

	public static void setSPDBUpdate(boolean update){
		App.mSP_config.edit().putBoolean("DB_UPDATE",update).apply();
	}

	/**
	 * 获取资源ID
	 * @param resources getResource
	 * @param name e.g：R.color.pink，则name 为 pink ；R.drawable.bg，则 name 为 bg
	 * @param type e.g：R.color.pink，则type 为 color ；R.drawable.bg，则 type 为 drawable
	 */
	public static int getResourceIdentifier(Resources resources,String name,String type) {
		return resources.getIdentifier(name, type, "com.carrie.lldiary");
	}

//	public static ArrayList<DiaryBg> getSelectClassifyIcons(){
//		String[] icons=new String[]{"drawable|emoji_e_228","drawable|emoji_e_229","drawable|emoji_e_230","drawable|emoji_e_230","drawable|emoji_e_230","drawable|emoji_e_230","drawable|emoji_e_230","drawable|emoji_e_230"};
//
//
//	}

	public static void sendSms(Context context){
//		SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
//		String sendTime = format.format(new Date());
		String smsContent="纪念日提醒："+DateUtil.getCurrentDate();
		BmobSMS.requestSMS(context, App.mCurrUser.getMobilePhoneNumber(), smsContent,null,new RequestSMSCodeListener() {

			@Override
			public void done(Integer smsId,BmobException ex) {
				// TODO Auto-generated method stub
				if(ex==null){//
					LogUtil.i("bmob","短信发送成功，短信id："+smsId);//用于查询本次短信发送详情
				}else{
					LogUtil.i("bmob","errorCode = "+ex.getErrorCode()+",errorMsg = "+ex.getLocalizedMessage());
				}
			}
		});
	}

}
