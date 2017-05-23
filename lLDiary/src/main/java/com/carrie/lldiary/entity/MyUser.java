package com.carrie.lldiary.entity;

import java.io.File;

import android.R.integer;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * 拓展用户信息类
 * 
 * @author Administrator
 *
 */
public class MyUser extends BmobUser {

	private static final long serialVersionUID = 1L;
	/**
	 * 用户头像
	 */
	public BmobFile avatar;
	@Override
	public String toString() {
		return "MyUser [avatar=" + avatar + "]";
	}
	
	

}
