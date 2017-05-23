package com.carrie.lldiary.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.carrie.lldiary.R;
import com.carrie.lldiary.activity.DiaryEditActivity;
import com.carrie.lldiary.activity.EmojiDownActivity;
import com.carrie.lldiary.adapter.EmojiMenuAdapter;
import com.carrie.lldiary.adapter.EmojiRecyclerAdapter;
import com.carrie.lldiary.adapter.FaceAdapter;
import com.carrie.lldiary.adapter.ViewPagerAdapter;
import com.carrie.lldiary.entity.Emoji;
import com.carrie.lldiary.helper.DividerItemDecoration;
import com.carrie.lldiary.helper.FaceConversionUtil;
import com.carrie.lldiary.helper.RecyclerItemClickListener;
import com.carrie.lldiary.helper.RecyclerItemClickListener.OnItemClickListener;
import com.carrie.lldiary.utils.AppUtils;
import com.carrie.lldiary.utils.FileUtils;
import com.carrie.lldiary.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class EmojiView extends RelativeLayout {

    private static final String TAG = "EmojiView";
    private Context mContext;
    private ViewPager mViewPager;

    /**
     * 表情页的监听事件
     */
    public OnCorpusSelectedListener mListener;
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

    /**
     * 表情图标文件集合
     */
//    public ArrayList<Emoji> emojiFiles;
//    public ArrayList<File> emojiFiles;
    public ArrayList<Emoji> emojiFiles;
    /**
     * 表情区域
     */
    private View view;
    /**
     * 输入框
     */
    private EditText et_text;
    /**
     * 表情数据填充器
     */
    private List<FaceAdapter> faceAdapters;
    /**
     * 当前表情页
     */
    private int mCurrItem = 0;

    private ArrayList<Emoji> menuEmojis;

    private SpannableString spannableString;
    private EmojiMenuAdapter adapter;

    public EmojiView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setupView();
    }

    public EmojiView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupView();
    }

    public EmojiView(Context context) {
        super(context);
        setupView();
    }

    private void setupView() {
        mContext = getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_emoji, this);
        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
        ll_point = (LinearLayout) view.findViewById(R.id.ll_point);

        iv_add = (ImageView) view.findViewById(R.id.iv_add_emoji);
        mRecView = (RecyclerView) view.findViewById(R.id.recy_view_emoji);

        iv_add.setImageResource(R.drawable.add_normal);
        iv_add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, EmojiDownActivity.class);
                mContext.startActivity(intent);
            }
        });

        LinearLayoutManager llmanager = new LinearLayoutManager(mContext);
        llmanager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecView.setLayoutManager(llmanager);
        mRecView.setHasFixedSize(true);
        mRecView.setLongClickable(true);
        mRecView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.HORIZONTAL_LIST));
    }

    public void setRecyViewData(final ArrayList<Emoji> list) {

        if (adapter == null) {
            adapter = new EmojiMenuAdapter(list);
            mRecView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }

        mRecView.addOnItemTouchListener(new RecyclerItemClickListener(mContext, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                setEnabled(false);

                if (position == 0) {
                    setData("emoji", 0, null);
                } else if (position == 1) {
                    setData("emoji2", 1, null);
                } else if (position == 2) {
                    setData("hzw.txt", 2, null);
                } else {
                    String emojiPath=AppUtils.getSubFrontStr(list.get(position).getFaceName(),'/');
                    LogUtil.i(TAG,"emojiPath="+emojiPath);
                    setData(emojiPath, position, list.get(position));
                    AppUtils.toast0(mContext,position+"");
                }

            }
        }));

    }

    private final int PAGE_SIZE=30;

    /**
     * 获取表情数据，填充viewpager
     *
     * @param emojiPath
     */
    public void setData(String emojiPath, int pos, Emoji emoji) {
        emojis = AppUtils.initListList(emojis);
        if (pos < 3) {
            LogUtil.i(TAG, "-----------------------emoji-----------------------");
            FaceConversionUtil.getInstace().getFileText(mContext, emojiPath,PAGE_SIZE);
            emojis = FaceConversionUtil.getInstace().emojiLists;
        } else {
            LogUtil.i(TAG, "-----------------------downed emoji-----------------------");
            emojiFiles=AppUtils.initList(emojiFiles);

            FileUtils.findFiles(emojiFiles,emojiPath,".png");
            LogUtil.i(TAG,"emojiFiles="+emojiFiles.size()+"........"+emojiFiles.toString());

            emojis= FaceConversionUtil.getInstace().getEmojiList(emojiFiles,PAGE_SIZE);
        }

        LogUtil.i(TAG, "emojis=" + emojis.size());
        LogUtil.i(TAG, "---------------------setData----------------------------------");

        initViewPager();
        Init_Point();
        Init_Data();


    }

    int pageSize = 30;
    private ArrayList<String> datas;
    private ImageView iv_add;
    private RecyclerView mRecView;

    /**
     * 获取分页数据
     *
     * @param page
     * @return
     */
    private ArrayList<String> getData(int page) {
        LogUtil.i(TAG, "page=" + page);

        int startIndex = page * pageSize;
        int endIndex = startIndex + pageSize;

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
            GridLayoutManager mGlManager = new GridLayoutManager(mContext, 8);
            // 如果布局大小一致有利于优化
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(mGlManager);
            mRecyclerView.setPadding(5, 0, 5, 0);
            mRecyclerView.setBackgroundColor(Color.TRANSPARENT);
            mRecyclerView.setAdapter(adapter);

            //点击表情，将之显示在日记内容里
            mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(mContext, new OnItemClickListener() {

                @Override
                public void onItemClick(View view, int position) {
                    Emoji emoji = (Emoji) emojis.get(mCurrItem).get(position);
                    LogUtil.i(TAG,"emoji.getId()@@@="+emoji.getId());
                    if(emoji.getId()==0){
                        LogUtil.i(TAG,"emoji.getId()==0");
                        if (mListener != null)
                            mListener.onCorpusSelected(emoji);
                        spannableString = FaceConversionUtil.getInstace().addFace(mContext, emoji.getId(),emoji.getFaceName(), emoji.getCharacter());
                        LogUtil.i(TAG,"spannableString="+spannableString.toString());
                        DiaryEditActivity.et_content.append(spannableString);

                    }else{
                        //删除按钮
                        LogUtil.i(TAG,"emoji.getId()!=0，前3个表情");
                        if (emoji.getId() == R.drawable.face_del_icon) {
                            int selection = et_text.getSelectionStart();
                            String text = et_text.getText().toString();
                            if (selection > 0) {
                                String text2 = text.substring(selection - 1);
                                if ("]".equals(text2)) {
                                    int start = text.lastIndexOf("[");
                                    int end = selection;
                                    et_text.getText().delete(start, end);
                                    return;
                                }
                                et_text.getText().delete(selection - 1, selection);
                            }
                        }

                        if (mListener != null)
                            mListener.onCorpusSelected(emoji);
                        spannableString = FaceConversionUtil.getInstace().addFace(mContext, emoji.getId(),"", emoji.getCharacter());
                        DiaryEditActivity.et_content.append(spannableString);
                    }


//                    if (!TextUtils.isEmpty(emoji.getCharacter())) {
//                        if (mListener != null)
//                            mListener.onCorpusSelected(emoji);
//                        spannableString = FaceConversionUtil.getInstace().addFace(mContext, emoji.getId(), emoji.getCharacter());
//                        DiaryEditActivity.et_content.append(spannableString);
//                    }

                }
            }));
            pageViews.add(mRecyclerView);

            LogUtil.i(TAG,"******************** initViewPager_END ********************");
        }
    }


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
        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                mCurrItem = arg0;
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

//    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//        Emoji emoji = (Emoji) faceAdapters.get(mCurrItem).getItem(arg2);
//        if (emoji.getId() == R.drawable.face_del_icon) {
//            int selection = et_text.getSelectionStart();
//            String text = et_text.getText().toString();
//            if (selection > 0) {
//                String text2 = text.substring(selection - 1);
//                if ("]".equals(text2)) {
//                    int start = text.lastIndexOf("[");
//                    int end = selection;
//                    et_text.getText().delete(start, end);
//                    return;
//                }
//                et_text.getText().delete(selection - 1, selection);
//            }
//        }
//        if (!TextUtils.isEmpty(emoji.getCharacter())) {
//            if (mListener != null)
//                mListener.onCorpusSelected(emoji);
//            spannableString = FaceConversionUtil.getInstace().addFace(mContext, emoji.getId(), emoji.getCharacter());
//            DiaryEditActivity.et_content.append(spannableString);
//        }
//    }

    public void setOnCorpusSelectedListener(OnCorpusSelectedListener listener) {
        mListener = listener;
    }

    /**
     * 表情选择监听
     *
     * @author naibo-liao
     * @时间： 2013-1-15下午04:32:54
     */
    public interface OnCorpusSelectedListener {

        void onCorpusSelected(Emoji emoji);

        void onCorpusDeleted();
    }

}
