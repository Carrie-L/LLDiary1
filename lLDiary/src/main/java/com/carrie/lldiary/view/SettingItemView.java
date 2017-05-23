package com.carrie.lldiary.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.carrie.lldiary.R;
import com.carrie.lldiary.utils.AppUtils;

/**
 * 自定义控件
 	* @Description: setting_item_view
 	* @author Carrie
 	* @version 创建时间：2016年4月27日 上午10:16:40
 */
public class SettingItemView  extends RelativeLayout{

	private View view;
	private TextView tv;

	public SettingItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setupView();
	}

	public SettingItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setupView();
	}

	public SettingItemView(Context context) {
		super(context);
		setupView();
	}
	
	private void setupView(){
		view = LayoutInflater.from(getContext()).inflate(R.layout.view_setting_item, this);
		tv = (TextView) view.findViewById(R.id.tv_text);
	}
	
	/**
	 * <font color="#f97798"> 设置icon图标</font>
	 * @param resid <font color="#f97798">R.drawble.icon</font>
	 * @return void
	 * @version 创建时间：2016年4月27日 上午10:12:26
	 */
	public void setBackground(int resid){
		view.findViewById(R.id.iv_icon).setBackgroundResource(resid);
	}
	
	/**
	 * <font color="#f97798">设置文字内容</font>
	 * @param text <font color="#f97798"></font>
	 * @return void
	 * @version 创建时间：2016年4月27日 上午10:13:09
	 */
	public void setText(String text){
		tv.setText(text);
	}
	
	public void setText(int resid){
		tv.setText(resid);
	}
	
	/**
	 * <font color="#f97798">设置水平分割线的颜色：为主题颜色</font> <font color="#f97798"></font>
	 * @return void
	 * @version 创建时间：2016年4月27日 上午10:15:00
	 */
	public void setLineColor(){
		view.findViewById(R.id.iv_line1).setBackgroundColor(Color.WHITE);
		view.findViewById(R.id.iv_line2).setBackgroundColor(Color.WHITE);
		view.findViewById(R.id.iv_line3).setBackgroundColor(AppUtils.getHomeColor());
	}
	
	/**
	 * <font color="#f97798">设置左侧短横线颜色为主题颜色，即小段末尾线包裹全部</font> <font color="#f97798"></font>
	 * @return void
	 * @version 创建时间：2016年4月27日 上午11:43:48
	 */
	public void setBottomLineColor(){
		view.findViewById(R.id.iv_line2).setBackgroundColor(AppUtils.getHomeColor());
	}
	
	/**
	 * <font color="#f97798">设置顶部横线颜色</font> <font color="#f97798"></font>
	 * @return void
	 * @version 创建时间：2016年4月27日 上午11:54:12
	 */
	public void setTopLineColor(){
		view.findViewById(R.id.iv_line1).setBackgroundColor(AppUtils.getHomeColor());
	}
	
	/**
	 * <font color="#f97798">设置最右边小图标状态（包含 > 和 √）</font>
	 * @param resid <font color="#f97798"></font>
	 * @return void
	 * @version 创建时间：2016年4月27日 上午10:25:33
	 */
	public void setStatusBackground(int resid){
		view.findViewById(R.id.iv_status).setBackgroundResource(resid);
	}

}
