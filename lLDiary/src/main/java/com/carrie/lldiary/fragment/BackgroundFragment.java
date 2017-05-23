package com.carrie.lldiary.fragment;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.carrie.lldiary.R;
import com.carrie.lldiary.activity.DiaryEditActivity;
import com.carrie.lldiary.adapter.BgAdapter;
import com.carrie.lldiary.entity.Image;
import com.carrie.lldiary.helper.RecyclerItemClickListener;

import java.util.ArrayList;

/**
 * 日记更换背景
 */
public class BackgroundFragment extends Fragment{

	private RecyclerView recyclerView;
	private ArrayList<Image> lists;
	private Image entity;
	private Context mContext;
	private Resources resources;
	private static final String TAG="BackgroundFragment";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.fragment_bg, container,false);
		recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
		mContext=container.getContext();
		return view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		lists=new ArrayList<>();
		lists.add(new Image(0,"纯色"));

		lists.add(new Image(R.color.bg_pure_corn_silk,""));
		lists.add(new Image(R.color.bg_pure_honeydew,""));
		lists.add(new Image(R.color.bg_pure_lavender_blush,""));
		lists.add(new Image(R.color.bg_pure_primary_color,""));
		lists.add(new Image(R.color.bg_pure_seashell,""));

		lists.add(new Image(0,"图案"));

		lists.add(new Image(R.drawable.bg_pic_1,null));
		lists.add(new Image(R.drawable.bg_pic_2,""));
		lists.add(new Image(R.drawable.bg_pic_3,""));
		lists.add(new Image(R.drawable.bg_pic_4,""));
		lists.add(new Image(R.drawable.bg_pic_5,""));


		recyclerView.setHasFixedSize(true);
		GridLayoutManager glManager=new GridLayoutManager(mContext,5);
		glManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
			@Override
			public int getSpanSize(int position) {
				if(lists.get(position).icon==0){
					return 5;//each item occupy 5 span size
				}else{
					return 1;//each item occupy 1 span size
				}
			}
		});
		recyclerView.setLayoutManager(glManager);

		BgAdapter adapter=new BgAdapter(lists);
		recyclerView.setAdapter(adapter);

		resources = getResources();

		recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(mContext, new RecyclerItemClickListener.OnItemClickListener() {
			private String iconName;
			private Message message;

			@Override
			public void onItemClick(View view, int position) {
				entity=lists.get(position);

				if(entity.icon!=0){
					message = new Message();
					message.what=DiaryEditActivity.CHANGE_DIARY_BG;
					iconName = resources.getText(entity.icon).toString();
					message.obj=entity.icon;
					DiaryEditActivity.mHandler.sendMessage(message);
				}
			}
		}));

	}
}
