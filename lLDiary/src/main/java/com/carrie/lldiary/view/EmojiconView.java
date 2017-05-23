package com.carrie.lldiary.view;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.carrie.lldiary.R;
import com.carrie.lldiary.adapter.EmojiRecyclerAdapter;
import com.carrie.lldiary.adapter.FaceAdapter;
import com.carrie.lldiary.adapter.ViewPagerAdapter;
import com.carrie.lldiary.entity.Emoji;
import com.carrie.lldiary.helper.FaceConversionUtil;
import com.carrie.lldiary.helper.RecyclerItemClickListener;
import com.carrie.lldiary.utils.AppUtils;
import com.carrie.lldiary.utils.FileUtils;
import com.carrie.lldiary.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/11 0011.
 * 自定义表情页面（只有表情）
 */
public class EmojiconView extends RelativeLayout{
    /**
     * 表情页的监听事件
     */
    public EmojiView.OnCorpusSelectedListener mListener;
    /**
     * 表情页界面集合
     */
    private ArrayList<RecyclerView> pageViews;
    /**
     * 游标显示布局
     */
    private LinearLayout ll_point;
    /**
     * 游标点集合
     */
    private ArrayList<ImageView> pointViews;
    /**
     * 表情集合 ArrayList<ArrayList<Emoji>>
     */
    public ArrayList<ArrayList<Emoji>> emojis;
    public ArrayList<Emoji> emojiDraws;
    private static final String TAG="EmojiconView";
    /**
     * 表情数据填充器
     */
    private List<FaceAdapter> faceAdapters;
    /**
     * 当前表情页
     */
    private int mCurrItem = 0;

//    int pageSize = 30;
    private int mPageSize;
    private ArrayList<String> datas;

    private Context mContext;

    private ViewPager mViewPager;
    private RelativeLayout rl;




    public EmojiconView(Context context) {
        super(context);
        mContext=context;
        init();
    }

    public EmojiconView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext=context;
        init();
    }

    public EmojiconView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext=context;
        init();
    }

    private void init(){
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_emojicon, this);
        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
        ll_point = (LinearLayout) view.findViewById(R.id.ll_point);
        rl = (RelativeLayout) view.findViewById(R.id.rl_container);
    }

    public void setLayoutHeight(int height){
        ViewGroup.LayoutParams params=rl.getLayoutParams();
        params.height=height;
        rl.setLayoutParams(params);
    }

    /**
     * 获取表情数据，填充viewpager
     * @param startIndex  icon图标名称数字，如emoji_e_12.png，startIndex即为12
     * @param lastIndex   icon图标名称数字，如emoji_e_277.png，lastIndex即为277，将12-277之间的图标加进去
     * @param pageSize    每页显示多少个图标
     */
    public void setData(String prefix,int startIndex,int lastIndex,int pageSize) {
        this.mPageSize=pageSize;
        LogUtil.i(TAG,"444:startIndex="+startIndex+",lastIndex="+lastIndex+",pageSize="+pageSize);

        emojis = AppUtils.initListList(emojis);
        LogUtil.i(TAG, "-----------------------emoji-----------------------");
        emojiDraws=AppUtils.initList(emojiDraws);
        emojiDraws= FileUtils.getResDrawables(prefix,startIndex,lastIndex,emojiDraws,mContext);
        LogUtil.i(TAG, "emojiDraws=" + emojiDraws.size());

        emojis = FaceConversionUtil.getInstace().getEmojiList(emojiDraws,mPageSize);

        LogUtil.i(TAG, "emojis=" + emojis.size());
        LogUtil.i(TAG, "---------------------setData----------------------------------");

        initViewPager();
        Init_Point();
        Init_Data();


    }

    /**
     * 获取分页数据
     *
     * @param page
     * @return
     */
    private ArrayList<String> getData(int page) {
        LogUtil.i(TAG, "page=" + page);

        int startIndex = page * mPageSize;
        int endIndex = startIndex + mPageSize;

        LogUtil.i(TAG, "startIndex1=" + startIndex + ",endIndex1=" + endIndex);

        if (endIndex > datas.size()) {
            endIndex = datas.size();
        }

        LogUtil.i(TAG, "startIndex2=" + startIndex + ",endIndex2=" + endIndex);
        // 不这么写，会在viewpager加载中报集合操作异常，我也不知道为什么
        ArrayList<String> list = new ArrayList<String>();
        list.addAll(datas.subList(startIndex, endIndex));
        return list;
    }

    private void initViewPager() {
        pageViews = new ArrayList<RecyclerView>();

        LogUtil.i(TAG,"********************initViewPager********************");
        // 中间添加表情页
        LogUtil.i(TAG+"_initViewPager", "emojis=" + emojis.size());

        faceAdapters = new ArrayList<FaceAdapter>();

        for (int i = 0; i < emojis.size(); i++) {
            EmojiRecyclerAdapter adapter = new EmojiRecyclerAdapter(emojis.get(i));
            RecyclerView mRecyclerView = new RecyclerView(mContext);
            GridLayoutManager mGlManager = new GridLayoutManager(mContext,6);
            // 如果布局大小一致有利于优化
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(mGlManager);
            mRecyclerView.setPadding(8, 1,8,1);
            mRecyclerView.setBackgroundColor(Color.TRANSPARENT);
            mRecyclerView.setAdapter(adapter);

            mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(mContext, new RecyclerItemClickListener.OnItemClickListener(){

                @Override
                public void onItemClick(View view, int position) {
                    Emoji emoji = (Emoji) emojis.get(mCurrItem).get(position);
                    LogUtil.i(TAG,"emoji.getId()@@@="+emoji.getId()+",name="+emoji.getFaceName());

                    mDialog.dismiss();

                    if(mDialogClickListener!=null){
                        mDialogClickListener.selectIcon(emoji.getId());
                        LogUtil.e(TAG,".selectIcon(emoji.getId()="+emoji.getId());
                    }
                }
            }));

            pageViews.add(mRecyclerView);

            LogUtil.i(TAG,"******************** initViewPager_END ********************");
        }
    }


    public AlertDialog mDialog;
    private View mView;



    /**
     * 初始化游标
     */
    private void Init_Point() {
        LogUtil.i(TAG+"_Init_Point", "pageViews=" + pageViews.size());
        pointViews = AppUtils.initList(pointViews);

        ll_point.removeAllViews();

        ImageView imageView;
        for (int i = 0; i < pageViews.size(); i++) {
            imageView = new ImageView(mContext);
            imageView.setBackgroundResource(R.drawable.d1);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            layoutParams.leftMargin = 10;
            layoutParams.rightMargin = 10;
            layoutParams.width = 8;
            layoutParams.height = 8;
            ll_point.addView(imageView, layoutParams);
            /*
             * if (i == 0 || i == pageViews.size() - 1) {
			 * imageView.setVisibility(View.GONE); }
			 */
            if (i == 0) {
                imageView.setBackgroundResource(R.drawable.d2);
            }
            pointViews.add(imageView);
        }
    }

    /**
     * 填充数据
     */
    private void Init_Data() {
        LogUtil.i(TAG,"-----------------Init_Data------------------------------");

        mViewPager.setAdapter(new ViewPagerAdapter(pageViews));

        mViewPager.setCurrentItem(0);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                mCurrItem = arg0;
                LogUtil.i(TAG,"mCurrItem="+mCurrItem);
                // 描绘分页点
                draw_Point(arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });

    }

    /**
     * 绘制游标背景
     */
    public void draw_Point(int index) {
        for (int i = 0; i < pointViews.size(); i++) {
            if (index == i) {
                pointViews.get(i).setBackgroundResource(R.drawable.d2);
            } else {
                pointViews.get(i).setBackgroundResource(R.drawable.d1);
            }
        }
    }

    public int getCurrItem(){
        return mCurrItem;
    }

    private OnDialogItemClickListener mDialogClickListener;
    public void setOnDialogItemClickListener(OnDialogItemClickListener listener){
        mDialogClickListener=listener;
    }

    public interface OnDialogItemClickListener{
        void selectIcon(int resId);
    }



}
