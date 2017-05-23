package com.carrie.lldiary.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.carrie.lldiary.App;
import com.carrie.lldiary.R;
import com.carrie.lldiary.dao.EmojiUpdateTime;
import com.carrie.lldiary.entity.Emoji;
import com.carrie.lldiary.utils.AppUtils;
import com.carrie.lldiary.utils.Constant;
import com.carrie.lldiary.utils.LogUtil;
import com.carrie.lldiary.view.EmojiView;

import java.util.ArrayList;

public class EmojiFragment extends Fragment {
	private static final String TAG = "EmojiFragment";
	private EmojiView mEmojiView;
	private Context mContext;
	private ArrayList<EmojiUpdateTime> menuEmojis;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_emoji, container, false);

		mEmojiView = (EmojiView) view.findViewById(R.id.emojiView);
		mContext=getContext();

		LogUtil.i(TAG, "onCreateView");
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		LogUtil.i(TAG, "onActivityCreated");
		AppUtils.setSP_Config_Str(mContext, Constant.LATEST_INSERT_TIME_EMOJI,"");//清0

		setData();

	}

	private void setData(){
//		menuEmojis = new ArrayList<Emoji>();
		menuEmojis=AppUtils.initList(menuEmojis);

		for (int i = 0; i < 3; i++) {
			Emoji emoji = new Emoji();

			if(i==0){
				emoji.setCharacter("emoji");
				emoji.setId(R.drawable.emoji_1);
			}else{
				emoji.setCharacter("twelvesx_" + i);
				int resID = mContext.getResources().getIdentifier("twelvesx_" + i, "drawable", mContext.getPackageName());
				emoji.setId(resID);
			}
//			menuEmojis.add(emoji);
		}
		LogUtil.i(TAG, "menuEmojis=" + menuEmojis.size());


		LogUtil.i(TAG,"setData");
	}

	@Override
	public void onResume() {
		super.onResume();

		LogUtil.i(TAG,"onResume");
		String latestInsertTime=AppUtils.getSP_Config_Str(mContext, Constant.LATEST_INSERT_TIME_EMOJI);
		LogUtil.i(TAG,"latestInsertTime="+latestInsertTime);
		menuEmojis= App.dbHelper.queryLastestDownedEmoji(latestInsertTime);
//		mEmojiView.setRecyViewData(menuEmojis);

	}
}
