package com.carrie.lldiary.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.carrie.lldiary.R;
import com.carrie.lldiary.utils.LogUtil;


public class EmoticonFragment extends Fragment {
	private static final String TAG = "EmoticonFragment";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_emoji, container, false);

//		EmojiView mEmojiView=(EmojiView) view.findViewById(R.id.emojiView);
//		mEmojiView.setData(null);//emoticon.txt

		LogUtil.i(TAG, "onCreateView");
		return view;
	}

}
