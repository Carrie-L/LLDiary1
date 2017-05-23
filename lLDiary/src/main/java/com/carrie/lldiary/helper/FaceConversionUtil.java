package com.carrie.lldiary.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;

import com.carrie.lldiary.entity.Emoji;
import com.carrie.lldiary.utils.AppUtils;
import com.carrie.lldiary.utils.FileUtils;
import com.carrie.lldiary.utils.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 
 ****************************************** 
 * @author 廖乃波
 * @文件名称 : FaceConversionUtil.java
 * @创建时间 : 2013-1-27 下午02:34:09
 * @文件描述 : 表情轉換工具
 * 
 *       1，首先判断输入的字符，是否包含表情的文字，比如 这个表情对应的文件名为 emoji_1.png，它对应的文字描述 ：
 *       [可爱]，如果我们在输出的是输出这么一句话：老婆，我想你了。 那么我们对应的根本文字就是：老婆，我想你了[可爱]。
 * 
 *       2，具体的转换过程就是用正则表达式比配文字中是否含有[xxx]这类的文字，如果有，那么我们就根据拿到的[xxx]找到它对应的资源文件id,
 *       当然这其中有一个关系表
 *       ，看你怎么处理这个关系了。最后将其用SpannableString替换成文字，表面上显示有图片，其实TextView里的text依然是
 *       :老婆，我想你了[可爱]。这个过程明白么？
 * 
 ****************************************** 
 */
public class FaceConversionUtil {

	/** 显示在聊天框的表情图片大小 **/
	private static final int SPANNABLE_BITMAP_SIZE = 58;

	/** 显示在页面上的表情大小 */
	private static final int BITMAP_SHOW_SIZE = 200;

	private static final String TAG = "FaceConversionUtil";

	/** 每一页表情的个数 */
//	private int pageSize = 31;// 更改页面高度在custom_facerel..xml中,id=ll_facechoose

			private int mPageSize;

	private static FaceConversionUtil mFaceConversionUtil;

	/** 保存于内存中的表情HashMap */
	private HashMap<String, String> emojiMap;

	/** 保存于内存中的表情集合 */
	private ArrayList<Emoji> emojis = new ArrayList<Emoji>();

	/** 表情分页的结果集合 */
	public ArrayList<ArrayList<Emoji>> emojiLists = new ArrayList<ArrayList<Emoji>>();

	private FaceConversionUtil() {

	}
	
	private void initList(){
		if(emojiMap==null){
			emojiMap = new HashMap<String, String>();
		}else{
			emojiMap.clear();
		}
//		
//		if(emojis==null){
//			emojis= new ArrayList<Emoji>();
//		}else{
//			emojis.clear();
//		}
		
		emojiLists=AppUtils.initList(emojiLists);
		emojis=AppUtils.initList(emojis);
	}

	public static FaceConversionUtil getInstace() {
		if (mFaceConversionUtil == null) {
			mFaceConversionUtil = new FaceConversionUtil();
		}
		return mFaceConversionUtil;
	}



	/**
	 * 添加表情
	 * 
	 * @param context
	 * @param imgId
	 * @param spannableString
	 * @return
	 */
	public SpannableString addFace(Context context, int imgId,String faceName, String spannableString) {
		if (TextUtils.isEmpty(spannableString)) {
			return null;
		}
		Bitmap bitmap =null;

		if(imgId==0){
			LogUtil.i(TAG,"faceName="+faceName);
			bitmap = BitmapFactory.decodeFile(faceName);
		}else{
			bitmap = BitmapFactory.decodeResource(context.getResources(), imgId);
		}
		bitmap = Bitmap.createScaledBitmap(bitmap, SPANNABLE_BITMAP_SIZE, SPANNABLE_BITMAP_SIZE, true);
		ImageSpan imageSpan = new ImageSpan(context, bitmap);
		SpannableString spannable = new SpannableString(spannableString);
		spannable.setSpan(imageSpan, 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return spannable;
	}

//	/**
//	 * 得到一个SpanableString对象，通过传入的字符串,并进行正则判断
//	 *
//	 * @param context
//	 * @param
//	 * @return
//	 */
//	public SpannableString getExpressionString2(Context context, String str) {
//		SpannableString spannableString = new SpannableString(str);
//		// 正则表达式比配字符串里是否含有表情，如： 我好[开心]啊
//		String zhengze = "\\[[^\\]]+\\]";
//		// 通过传入的正则表达式来生成一个pattern
//		Pattern sinaPatten = Pattern.compile(zhengze, Pattern.CASE_INSENSITIVE);
//		try {
//			dealExpression(context, spannableString, sinaPatten, 0);
//		} catch (Exception e) {
//			Log.e("dealExpression", e.getMessage());
//		}
//		return spannableString;
//	}
//
//	/**
//	 * 对spanableString进行正则判断，如果符合要求，则以表情图片代替
//	 *
//	 * @param context
//	 * @param spannableString
//	 * @param patten
//	 * @param start
//	 * @throws Exception
//	 */
//	private void dealExpression(Context context, SpannableString spannableString, Pattern patten, int start) throws Exception {
//		Matcher matcher = patten.matcher(spannableString);
//		while (matcher.find()) {
//			String key = matcher.group();
//			// 返回第一个字符的索引的文本匹配整个正则表达式,ture 则继续递归
//			if (matcher.start() < start) {
//				continue;
//			}
//			String value = emojiMap.get(key);
//			if (TextUtils.isEmpty(value)) {
//				continue;
//			}
//			int resId = context.getResources().getIdentifier(value, "drawable", context.getPackageName());
//			// 通过上面匹配得到的字符串来生成图片资源id
//			// Field field=R.drawable.class.getDeclaredField(value);
//			// int resId=Integer.parseInt(field.get(null).toString());
//			if (resId != 0) {
//				Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
//				bitmap = Bitmap.createScaledBitmap(bitmap, BITMAP_SHOW_SIZE, BITMAP_SHOW_SIZE, true);
//				// 通过图片资源id来得到bitmap，用一个ImageSpan来包装
//				ImageSpan imageSpan = new ImageSpan(bitmap);
//				// 计算该图片名字的长度，也就是要替换的字符串的长度
//				int end = matcher.start() + key.length();
//				// 将该图片替换字符串中规定的位置中
//				spannableString.setSpan(imageSpan, matcher.start(), end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//				if (end < spannableString.length()) {
//					// 如果整个字符串还未验证完，则继续。。
//					dealExpression(context, spannableString, patten, end);
//				}
//				break;
//			}
//		}
//	}

	public void getFileText(Context context,String emojiPath,int pageSize) {
		mPageSize=pageSize;
		initList();
		ParseData(FileUtils.getEmojiFile(context,emojiPath), context,pageSize);
	}

	/**
	 * 解析字符
	 * 
	 * @param data
	 */
	private void ParseData(List<String> data, Context context,int pageSize) {
		if (data == null) {
			return;
		}
		Emoji emojEentry;
		try {
			for (String str : data) {
				String[] text = str.split(",");
				String fileName = text[0].substring(0, text[0].lastIndexOf("."));
				emojiMap.put(text[1], fileName);
				int resID = context.getResources().getIdentifier(fileName, "drawable", context.getPackageName());

				if (resID != 0) {
					emojEentry = new Emoji();
					emojEentry.setId(resID);
					emojEentry.setCharacter(text[1]);
					emojEentry.setFaceName(fileName);
					emojis.add(emojEentry);
				}
			}
			LogUtil.i(TAG, "emojis="+emojis.size());
			AppUtils.logListToString("emojis",emojis);

//			//Math.ceil 向上取整计算，返回大于或等于参数、并且与之最接近的整数，向上舍入值
//			int pageCount = (int) Math.ceil(emojis.size() /pageSize  + 0.1);
//			LogUtil.i(TAG, "pageCount="+pageCount);
//
//			for (int i = 0; i < pageCount; i++) {
//				emojiLists.add(getData(i));
//			}
			emojiLists=getEmojiList(emojis,pageSize);
			AppUtils.logListToString("emojiLists",emojiLists);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ArrayList<ArrayList<Emoji>> getEmojiList(ArrayList<Emoji> list,int pageSize){
		LogUtil.i(TAG,"getEmojiList:__emojis.size()="+emojis.size());
		LogUtil.i(TAG,"getEmojiList:__list.size()="+list.size());

		this.mPageSize=pageSize;

		if(emojis.size()==0){
			emojis=list;
		}

		emojiLists=AppUtils.initListList(emojiLists);

		int pageCount=(int) Math.ceil(list.size() /pageSize  + 0.1);
		for (int i = 0; i < pageCount; i++) {
			emojiLists.add(getData(list,i));
		}
		return emojiLists;
	}
	
	/**
	 * 获取分页数据
	 * 
	 * @param page
	 * @return
	 */
	private ArrayList<Emoji> getData(ArrayList<Emoji> lists,int page) {
		LogUtil.i(TAG, "page="+page+",mPageSize="+mPageSize);
		int startIndex = page * mPageSize;
		int endIndex = startIndex + mPageSize;
		
		LogUtil.i(TAG, "startIndex1="+startIndex+",endIndex1="+endIndex);
		LogUtil.i(TAG,"getData:lists.size()="+lists.size());
		if (endIndex > lists.size()) {
			endIndex = lists.size();
		}
		
		LogUtil.i(TAG, "startIndex2="+startIndex+",endIndex2="+endIndex);
		// 不这么写，会在viewpager加载中报集合操作异常，我也不知道为什么
		ArrayList<Emoji> list = new ArrayList<Emoji>();
		list=AppUtils.initList(list);
		//list:每页的数据
		list.addAll(lists.subList(startIndex, endIndex));
		AppUtils.logListToString("1",list);

		for (int i = list.size(); i < mPageSize; i++) {
			Emoji object = new Emoji();
			list.add(object);
		}

		AppUtils.logListToString("2",list);
		return list;
	}
}