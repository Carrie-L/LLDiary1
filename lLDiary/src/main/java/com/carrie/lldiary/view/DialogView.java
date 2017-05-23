package com.carrie.lldiary.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.carrie.lldiary.R;
import com.carrie.lldiary.utils.AppUtils;

public class DialogView extends RelativeLayout {

	private TextView tv_title;
	private View view;
	private Button btn_ok;
	private Button btn_cancel;
	private EditText et1;
	private EditText et2;
	private EditText et3;
	
	public DialogView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		view = LayoutInflater.from(context).inflate(R.layout.view_dialog, this);
		setupView();
	}

	public DialogView(Context context, AttributeSet attrs) {
		super(context, attrs);
		view = LayoutInflater.from(context).inflate(R.layout.view_dialog, this);
		setupView();
	}

	public DialogView(Context context) {
		super(context);
		view = LayoutInflater.from(context).inflate(R.layout.view_dialog, this);
		setupView();
	}

	private void setupView() {
		
		tv_title = (TextView) view.findViewById(R.id.tv_title);
		btn_ok = (Button) view.findViewById(R.id.btn_ok);
		btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
		
		et1 = (EditText) view.findViewById(R.id.et_modify1);
		
		
		setBackgroundColor();

	}

	public void setTitle(int resid) {
		tv_title.setText(resid);
	}

	public void setTitle(String text) {
		tv_title.setText(text);
	}

	public void setHint(int resid) {
		et1.setHint(resid);
	}

	public void setHint(String text) {
		et1.setHint(text);
	}
	
	/**
	 * <font color="#f97798">获取输入的值</font>
	 * @return <font color="#f97798">String</font>
	 * @version 创建时间：2016年4月27日 下午1:19:31
	 */
	public String getText1(){
		return et1.getText().toString().trim();
	}
	public String getText2(){
		return et2.getText().toString().trim();
	}
	public String getText3(){
		return et3.getText().toString().trim();
	}
	
	private void setBackgroundColor(){
	//	tv_title.setBackgroundColor(AppUtils.getLightHomeColor());
//		btn_ok.setBackgroundColor(AppUtils.getLightHomeColor());
//		btn_cancel.setBackgroundColor(AppUtils.getLightHomeColor());
	}

	/**
	 * <font color="#f97798">是否显示第二行EditText</font>
	 * @param bool  true：显示 ； false ：gone
	 * @param resid <font color="#f97798">hint</font>
	 * @return void
	 * @version 创建时间：2016年4月27日 下午3:09:25
	 */
	public void showEdit2nd(boolean bool,int resid) {
		if (bool) {
			et2 = (EditText) view.findViewById(R.id.et_modify2);
			et2.setVisibility(View.VISIBLE);
			et2.setHint(resid);
		}
	}

	/**
	 * <font color="#f97798">是否显示第3行EditText</font>
	 * 
	 * @param bool
	 *            <font color="#f97798"></font>
	 * @return void
	 * @version 创建时间：2016年4月27日 下午12:51:57
	 */
	public void showEdit3rd(boolean bool,int resid) {
		if (bool) {
			et3 = (EditText) view.findViewById(R.id.et_modify3);
			et3.setVisibility(View.VISIBLE);
			et3.setHint(resid);
		}
	}

	public void setOnOKClickListener(OnClickListener listener) {
		btn_ok.setOnClickListener(listener);
	}
	
	public void setOnCancelClickListener(OnClickListener listener) {
		btn_cancel.setOnClickListener(listener);
	}
	
	public void setRequestFocus1(){
		et1.requestFocus();
	}
	
	public void setRequestFocus2(){
		et2.requestFocus();
	}
	
	public void setRequestFocus3(){
		et3.requestFocus();
	}
	
	public void setHintSize1(String hintContent,int hintSize ){
		AppUtils.setHintSize(et1, hintContent, hintSize);
	}
	
	public void setHintSize2(String hintContent,int hintSize ){
		AppUtils.setHintSize(et2, hintContent, hintSize);
	}
	
	public void setHintSize3(String hintContent,int hintSize ){
		AppUtils.setHintSize(et3, hintContent, hintSize);
	}

}
