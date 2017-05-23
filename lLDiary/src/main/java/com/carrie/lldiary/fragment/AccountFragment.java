package com.carrie.lldiary.fragment;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

import com.carrie.lldiary.App;
import com.carrie.lldiary.R;
import com.carrie.lldiary.activity.HomeActivity;
import com.carrie.lldiary.base.BaseFragment;
import com.carrie.lldiary.db.AccountDB;
import com.carrie.lldiary.entity.MyUser;
import com.carrie.lldiary.utils.AppUtils;
import com.carrie.lldiary.utils.CircleImage;
import com.carrie.lldiary.utils.CircularImage;
import com.carrie.lldiary.utils.Constant;
import com.carrie.lldiary.utils.DateUtil;
import com.carrie.lldiary.utils.LogUtil;
import com.carrie.lldiary.view.AccountSettingView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 账户管理
 * 
 * @author Carrie
 * 
 */
public class AccountFragment extends BaseFragment implements OnClickListener {
	private static final String TAG = "AccountFragment";
	
	@ViewInject(R.id.tv_title)
	private TextView tv_title;
	@ViewInject(R.id.ib_back)
	private ImageButton ib_back;
	@ViewInject(R.id.ib_add)
	private ImageButton ib_add;
	@ViewInject(R.id.acc_head_photo)
	private CircularImage image;
	@ViewInject(R.id.btn_confirm)
	Button btn_confirm;
	@ViewInject(R.id.btn_cancel)
	Button btn_cancel;
	@ViewInject(R.id.et_username)
	private EditText et_username;
	@ViewInject(R.id.et_useremail)
	private EditText et_useremail;
	@ViewInject(R.id.tv_acc_username)
	private TextView tv_showName;

	private String[] items = new String[] { "选择本地图片", "拍照" };
	/* 头像名称 */
	private static final String IMAGE_FILE_NAME = "avatarTemp.png";

	/* 请求码 */
	private static final int IMAGE_REQUEST_CODE = 0;
	private static final int CAMERA_REQUEST_CODE = 1;
	private static final int RESULT_REQUEST_CODE = 2;

	private static final int OUT_PUT_WIDTH = 150;

	SharedPreferences sp;
	private AccountSettingView mAccView;

	private String name;

	private String mAvatarPath;

	@Override
	public void initData(Bundle savedInstanceState) {
		ib_add.setVisibility(View.INVISIBLE);
		ib_back.setOnClickListener(this);
		image.setOnClickListener(this);

		mAccView.setOnAvatarListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showImgDialog();
			}
		});
	}

	@Override
	public View initView(LayoutInflater inflater, ViewGroup container) {
		BaseFragment.sClsName = "Account";

		View view = inflater.inflate(R.layout.fragment_account, null);
		ViewUtils.inject(this, view);

		mAccView = (AccountSettingView) view.findViewById(R.id.account_setting_view);

		tv_title.setText("账户管理");
		sp = mContext.getSharedPreferences("config", 0);

		// 获取当前用户
		MyUser myUser = BmobUser.getCurrentUser(mContext, MyUser.class);
		name = myUser.getUsername();
		mAvatarPath = AppUtils.avatarPath(name);
		char c = ' ';
		if (!TextUtils.isEmpty(name)) {
			c = '，';
		}
		tv_showName.setText(DateUtil.timePeriod() + c + name);
		tv_showName.setTextColor(AppUtils.getHomeColor());
		
		if(new File(mAvatarPath).exists()){
	//		image.setBackground(new BitmapDrawable(getResources(),mAvatarPath ));
			image.setImageDrawable(new BitmapDrawable(getResources(),mAvatarPath ));
		}else{
			image.setImageResource(R.drawable.avatar_default);
		}

		
		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_back:
			Intent intent = new Intent(mContext, HomeActivity.class);
			startActivity(intent);
			((Activity) mContext).finish();
			break;
		case R.id.acc_head_photo:
			AppUtils.toast0(mContext, DateUtil.timePeriod()+" *^_^*");
			break;
		default:
			break;
		}
	}

	/**
	 * 显示图片选择对话框
	 */
	private void showImgDialog() {
		new AlertDialog.Builder(mContext).setTitle("设置头像").setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0:
					Intent intentFromGallery = new Intent();
					intentFromGallery.setType("image/*"); // 设置文件类型
					intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
					startActivityForResult(intentFromGallery, IMAGE_REQUEST_CODE);
					break;
				case 1:
					Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					// 判断存储卡是否可以用，可用进行存储
					if (SdCard.hasSdcard()) {
						intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT,
								Uri.fromFile(new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME)));
					}
					startActivityForResult(intentFromCapture, CAMERA_REQUEST_CODE);
					break;
				}
			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		}).show();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 结果码不等于取消时候
		if (resultCode != 0) {

			switch (requestCode) {
			case IMAGE_REQUEST_CODE:
				startPhotoZoom(data.getData());
				break;
			case CAMERA_REQUEST_CODE:
				LogUtil.i(TAG, "CAMERA_REQUEST_CODE");
				if (SdCard.hasSdcard()) {
					File tempFile = new File(Environment.getExternalStorageDirectory() + "/" + IMAGE_FILE_NAME);
					startPhotoZoom(Uri.fromFile(tempFile));
				} else {
					Toast.makeText(mContext, "未找到存储卡，无法存储照片！", Toast.LENGTH_LONG).show();
				}
				break;
			case RESULT_REQUEST_CODE:
				if (data != null) {
					getImageToView(data);
				}
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {
		LogUtil.i(TAG + ",startPhotoZoom", "uri=" + uri);

		Intent intent = new Intent("com.android.camera.action.CROP");

		if (uri == null) {
			Log.i("tag", "The uri is not exist.");
			return;
		}
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
			Log.i(TAG, "KITKAT");
			String url = CircleImage.getPath(getActivity(), uri);
			intent.setDataAndType(Uri.fromFile(new File(url)), "image/*");
		} else {
			intent.setDataAndType(uri, "image/*");
		}

		// 设置裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX",OUT_PUT_WIDTH);
		intent.putExtra("outputY", OUT_PUT_WIDTH);
		intent.putExtra("return-data", true);

		startActivityForResult(intent, RESULT_REQUEST_CODE);
	}

	public void saveBitmap(String fileName, Bitmap mBitmap) {
		File f = new File(fileName);
		FileOutputStream fOut = null;
		try {
			f.createNewFile();
			fOut = new FileOutputStream(f);
			mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
			fOut.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fOut.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param picdata
	 */
	private void getImageToView(Intent data) {
		Bitmap bitmap = data.getParcelableExtra("data");
		image.setImageBitmap(bitmap);

		bitmap = CircleImage.toRoundBitmap(bitmap);// 将图片转换成圆形
		saveBitmap(AppUtils.avatarPath(name), bitmap);
		
		postImg();
	}
	
	/**
	 * <font color="#f97798">将头像上传到服务器，并添加到_User数据库</font> <font color="#f97798"></font>
	 * @return void
	 * @version 创建时间：2016年4月28日 上午9:32:28
	 */
	private void postImg(){
		final MyUser myUser=new MyUser();
		final BmobFile bmobFile=new BmobFile(new File(mAvatarPath));
		bmobFile.upload(mContext, new UploadFileListener() {
			
			@Override
			public void onSuccess() {
				AppUtils.toast0(mContext, "上传头像成功");
				myUser.avatar=bmobFile;
				myUser.update(mContext, App.mCurrUser.getObjectId(),new UpdateListener() {
					
					@Override
					public void onSuccess() {
						AppUtils.toast0(mContext, "修改头像成功");
					}
					
					@Override
					public void onFailure(int arg0, String arg1) {
						AppUtils.toast0(mContext, "修改头像失败+arg0="+arg0+"，arg1="+arg1);
						LogUtil.e(TAG, "修改头像失败+arg0="+arg0+"，arg1="+arg1);
					}
				});
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				AppUtils.toast0(mContext, "上传头像失败+arg0="+arg0+"，arg1="+arg1);
				LogUtil.e(TAG, "上传头像失败+arg0="+arg0+"，arg1="+arg1);
			}
		});
		
	}

	public static class SdCard {
		/**
		 * 检查是否存在SDCard
		 * 
		 * @return
		 */
		public static boolean hasSdcard() {
			String state = Environment.getExternalStorageState();
			if (state.equals(Environment.MEDIA_MOUNTED)) {
				LogUtil.i(TAG, "存在SDK");
				return true;
			} else {
				LogUtil.i(TAG, "不存在SDK");
				return false;
			}
		}
	}

}
