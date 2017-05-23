package com.carrie.lldiary.view;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.carrie.lldiary.R;
import com.carrie.lldiary.adapter.TabFragmentAdapter;
import com.carrie.lldiary.fragment.BackgroundFragment;
import com.carrie.lldiary.fragment.EmojiFragment;
import com.carrie.lldiary.fragment.EmoticonFragment;
import com.carrie.lldiary.fragment.PictureFragment;
import com.carrie.lldiary.fragment.TextFragment;
import com.carrie.lldiary.utils.AppUtils;

import java.util.ArrayList;

/**
 * 编辑日记： 最下方的TabBar和ViewPager部分的自定义控件
 * 
 * @author Administrator
 * 
 */
public class TabViewPager extends RelativeLayout implements OnClickListener {

	private static final String TAG = null;
	private RadioButton rb_1;
	private RadioButton rb_2;
	private RadioButton rb_3;
	private RadioButton rb_4;
	private RadioButton rb_0;

	private Context mContext;
	private RelativeLayout rl_contentView;
	private ViewPager mViewPager;
	private LinearLayout ll_point;
	private ArrayList<Fragment> mFragmentLists;

	private int mCurrItem;

	public TabViewPager(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setupView();
	}

	public TabViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		setupView();
	}

	public TabViewPager(Context context) {
		super(context);
		setupView();
	}

	private void setupView() {
		mContext = getContext();
		View view = LayoutInflater.from(mContext).inflate(R.layout.view_bottom_tab, this);
		rb_0 = (RadioButton) view.findViewById(R.id.rb_0);
		rb_1 = (RadioButton) view.findViewById(R.id.rb_1);
		rb_2 = (RadioButton) view.findViewById(R.id.rb_2);
		rb_3 = (RadioButton) view.findViewById(R.id.rb_3);
		rb_4 = (RadioButton) view.findViewById(R.id.rb_4);

		rb_0.setOnClickListener(this);
		rb_1.setOnClickListener(this);
		rb_2.setOnClickListener(this);
		rb_3.setOnClickListener(this);
		rb_4.setOnClickListener(this);

		rl_contentView = (RelativeLayout) view.findViewById(R.id.rl_container);
		mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
		ll_point = (LinearLayout) view.findViewById(R.id.ll_point);

		rl_contentView.setVisibility(View.GONE);

		initViewPager();

	}

	private void initViewPager() {
		mFragmentLists = new ArrayList<Fragment>();
		Fragment emojiFrag = new EmojiFragment();
		Fragment emoticonFrag = new EmoticonFragment();
		Fragment picFrag = new PictureFragment();
		Fragment textFrag = new TextFragment();
		Fragment bgFrag = new BackgroundFragment();

		mFragmentLists.add(bgFrag);
		mFragmentLists.add(emojiFrag);
		mFragmentLists.add(emoticonFrag);
		mFragmentLists.add(picFrag);
		mFragmentLists.add(textFrag);

		mViewPager.setAdapter(new TabFragmentAdapter(((FragmentActivity) mContext).getSupportFragmentManager(), mFragmentLists));
		mViewPager.setCurrentItem(0);

	}

	public void setOnClickListener0() {
//		rb_0.setOnClickListener(listener);
		mCurrItem = 0;
		mViewPager.setCurrentItem(mCurrItem);

		showContent();
	}

	public void setOnClickListener1() {
		mCurrItem = 1;
		mViewPager.setCurrentItem(mCurrItem);

		showContent();

	}

	public void setOnClickListener2() {
		mCurrItem = 2;
		mViewPager.setCurrentItem(mCurrItem);

		showContent();
	}

	public void setOnClickListener3() {
//		rb_3.setOnClickListener(listener);
		mCurrItem = 3;
		mViewPager.setCurrentItem(mCurrItem);

		showContent();
	}

	public void setOnClickListener4() {
//		rb_4.setOnClickListener(listener);
		mCurrItem = 4;
		mViewPager.setCurrentItem(mCurrItem);

		showContent();
	}

	private void showContent() {
		// 隐藏表情选择框
		if (rl_contentView.getVisibility() == View.VISIBLE) {
		//	rl_contentView.setVisibility(View.GONE);
			// App.mInputManager.toggleSoftInput(0,
			// InputMethodManager.HIDE_NOT_ALWAYS); // 如果当前键盘隐藏，则显示
		} else {
			AppUtils.closeSoftKb(mContext);
			rl_contentView.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rb_0:
			mCurrItem = 0;
//			mViewPager.setCurrentItem(mCurrItem);
			setOnClickListener0();
			break;
		case R.id.rb_1:
			mCurrItem = 1;
			setOnClickListener1();
			break;
		case R.id.rb_2:
			setOnClickListener2();
			break;
		case R.id.rb_3:
			mCurrItem = 3;
			setOnClickListener3();
			break;
		case R.id.rb_4:
			mCurrItem = 4;
			setOnClickListener4();
			break;
		}

	}

	/**
	 * 
	 * <font color="#f97798">是否隐藏表情选择框</font>
	 * 
	 * @param bool
	 *            <font color="#f97798">true: 隐藏，false : 显示</font>
	 * @return void
	 * @version 创建时间：2016年4月28日 下午5:46:11
	 */
	public void hideFaceView(boolean bool) {
		if (bool) {// 隐藏
			rl_contentView.setVisibility(View.GONE);
		} else {
			rl_contentView.setVisibility(View.VISIBLE);
		}
	}

}
