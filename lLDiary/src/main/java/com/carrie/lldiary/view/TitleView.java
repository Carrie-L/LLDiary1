package com.carrie.lldiary.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.carrie.lldiary.R;

import static com.carrie.lldiary.R.id.iv;
import static com.carrie.lldiary.activity.MyActivityManager.finish;

public class TitleView extends LinearLayout{

	private TextView tv_title;
	private ImageView ib_right;
    /**在最右按钮的左边一个*/
	private ImageView ivRight1;
	private Context mContext;
	private View view;
    private ImageView ib_back;

    public TitleView(Context context) {
		super(context);
		mContext=context;
		initView();
	}

	public TitleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext=context;
		initView();
	}

	public TitleView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		mContext=context;
		initView();
	}

	private void initView(){
		view =LayoutInflater.from(mContext).inflate(R.layout.view_title_view, this);
		tv_title = (TextView) view.findViewById(R.id.tv_title);
        ib_back = (ImageView) view.findViewById(R.id.ib_back);
		ib_right=(ImageView) view.findViewById(R.id.ib_right);
        ivRight1=(ImageView) view.findViewById(R.id.ib_right_1);
//		ib_back.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				finish();
//			}
//		});
	}

	public void setTitle(String title){
		tv_title.setText(title);
	}

	public void setTitle(int resId){
		tv_title.setText(mContext.getString(resId));
	}

	public String getTitle(){
		return tv_title.getText().toString().trim();
	}

	public void setOnLeftClickListener(OnClickListener listener){
		ib_back.setOnClickListener(listener);
	}
	/**
	 * 点击右边的按钮
	 * @param listener
     */
	public void setOnRightClickListener(OnClickListener listener){
		ib_right.setOnClickListener(listener);
	}

	/**
	 * 设置右边按钮图片
	 * @param resId
     */
	public void setRightIcon(int resId){
		if(resId!=0){
			ib_right.setImageResource(resId);
			ib_right.setVisibility(VISIBLE);
		}
	}

    /**
     * 点击右边第二个按钮
     * @param listener
     */
    public void setOnRightClickListener1(OnClickListener listener){
        ivRight1.setOnClickListener(listener);
    }

    /**
     * 设置右边第二个按钮图标
     * @param resId
     */
    public void setRightIcon1(int resId){
        if(resId!=0){
            ivRight1.setImageResource(resId);
            ivRight1.setVisibility(VISIBLE);
        }
    }

}
