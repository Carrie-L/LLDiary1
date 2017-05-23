package com.carrie.lldiary.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.carrie.lldiary.App;
import com.carrie.lldiary.R;
import com.carrie.lldiary.adapter.EmojiDownAdapter;
import com.carrie.lldiary.dao.EmojiUpdateTime;
import com.carrie.lldiary.entity.EmojiFile;
import com.carrie.lldiary.entity.EmojiUrl;
import com.carrie.lldiary.helper.DBHelper;
import com.carrie.lldiary.helper.DividerItemDecoration;
import com.carrie.lldiary.utils.AppUtils;
import com.carrie.lldiary.utils.Constant;
import com.carrie.lldiary.utils.DateUtil;
import com.carrie.lldiary.utils.FileUtils;
import com.carrie.lldiary.utils.LogUtil;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.MaterialHeader;
import in.srain.cube.views.ptr.util.PtrLocalDisplay;

//import com.sina.org.apache.http.util.EncodingUtils;

/**
 * 下载服务器上的Emoji
 * Created by Administrator on 2016/5/2 0002.
 */
public class EmojiDownActivity extends Activity implements EmojiDownAdapter.OnDownClickListener {
    private static final String TAG = "EmojiDownActivity";
    private static final int DOWNLOADING = 0;
    private static final int DOWN_FINISH = 1;
    private static final int DOWN_FAILED = -1;
    RecyclerView mRecyView;
    private Context mContext;

    protected PtrFrameLayout mPtrFrameLayout;
    private int curr = 0;
    boolean isFinished = false;
    private int curr2;
    private BmobQuery<EmojiFile> query;
    private int mCurr;
    private EmojiDownAdapter adapter;
    private String str;
    private ArrayList<EmojiUpdateTime> newEmojiUpdates;
    private int mDownProgress;
    private String mDownDecimal;//下载百分比

    private Button mButton;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emoji_down);

        mContext = this;

        mRecyView = (RecyclerView) findViewById(R.id.recyview_emoji_down);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyView.setLayoutManager(linearLayoutManager);
        mRecyView.setHasFixedSize(true);
        mRecyView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));

        newEmojiUpdates = new ArrayList<EmojiUpdateTime>();

        dbHelper = App.dbHelper;

        //查询数据库中已存在的Emoji列表信息
        newEmojiUpdates = dbHelper.getEmojiUTList();

        //查询最新插入时间
        String latestInsertTime = dbHelper.getEmojiLastInsertTime();
        LogUtil.i(TAG, "latestInsertTime=" + latestInsertTime);
        AppUtils.setSP_Config_Str(mContext, Constant.LATEST_INSERT_TIME_EMOJI, latestInsertTime);


        adapter = new EmojiDownAdapter(mContext, newEmojiUpdates);
        mRecyView.setAdapter(adapter);

        adapter.setOnDownClickListener(this);

        mPtrFrameLayout = (PtrFrameLayout) findViewById(R.id.material_style_ptr_frame);

        final MaterialHeader header = new MaterialHeader(this);
        int colors[] = getResources().getIntArray(R.array.google_colors);
        header.setColorSchemeColors(colors);
        header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
        header.setPadding(0, PtrLocalDisplay.dp2px(15), 0, PtrLocalDisplay.dp2px(10));
        header.setPtrFrameLayout(mPtrFrameLayout);

        mPtrFrameLayout.setLoadingMinTime(1000);
        mPtrFrameLayout.setDurationToCloseHeader(1500);
        mPtrFrameLayout.setHeaderView(header);
        mPtrFrameLayout.addPtrUIHandler(header);
        mPtrFrameLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPtrFrameLayout.autoRefresh(false);
            }
        }, 100);

        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            //检测是否可以下拉刷新，如果可以，自动刷新
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
//                return true;
            }

            //
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                downEmojiUrlFile();
            }
        });


    }

    private void downEmojiUrlFile() {
        query = new BmobQuery<EmojiFile>();
        String localUpdateAt = dbHelper.getEmojiLastUpdateAt();
        if (!TextUtils.isEmpty(localUpdateAt)) {
            Date date = DateUtil.dateAdd1Second(localUpdateAt);
            query.addWhereGreaterThan("updatedAt", new BmobDate(date));
        }
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.findObjects(mContext, new FindListener<EmojiFile>() {
            @Override
            public void onSuccess(final List<EmojiFile> list) {
                File file = new File(App.mRootDir + "Emoji");
                if (!file.exists()) {
                    file.mkdir();
                }
                //有数据，下载
                if (list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        mCurr = i;

                        final EmojiFile emojiFile = list.get(i);

                        String fileName = emojiFile.Emoji.getFilename();
                        String objectId = list.get(i).getObjectId();
                        String updateAt = list.get(i).getUpdatedAt();

                        str = App.mRootDir + "Emoji/" + FileUtils.getSubName(emojiFile.Emoji.getFilename(), '.') + "/";

                        LogUtil.i(TAG, "str=" + str);
                        if (!new File(str).exists()) {
                            new File(str).mkdir();
                        }

                        //下载urlFile
                        emojiFile.urlFile.download(mContext, new File(str + emojiFile.urlFile.getFilename()), new DownloadFileListener() {
                            @Override
                            public void onSuccess(String s) {
                                LogUtil.i(TAG, "下载url文件成功," + s);
                            }

                            @Override
                            public void onFailure(int i, String s) {
                                AppUtils.failedLog("下载url文件失败", i, s);
                            }
                        });

                        //下载emoji
                        emojiFile.Emoji.download(mContext, new File(str + emojiFile.Emoji.getFilename()), new DownloadFileListener() {
                            @Override
                            public void onSuccess(String s) {
                                AppUtils.toast0(mContext, "下载emoji成功");
                                LogUtil.i(TAG, "下载emoji成功,str +emojiFile.urlFile.getFilename()=" + App.mRootDir + "Emoji/" + FileUtils.getSubName(emojiFile.Emoji.getFilename(), '.') + "/" + emojiFile.urlFile.getFilename());

                                EmojiUpdateTime emojiUpdateAt = new EmojiUpdateTime();
                                emojiUpdateAt.setObjectId(emojiFile.getObjectId());
                                emojiUpdateAt.setPath( App.mRootDir + "Emoji/" + FileUtils.getSubName(emojiFile.Emoji.getFilename(), '.') + "/" + emojiFile.Emoji.getFilename());
                                emojiUpdateAt.setUrl(App.mRootDir + "Emoji/" + FileUtils.getSubName(emojiFile.Emoji.getFilename(), '.') + "/" + emojiFile.urlFile.getFilename());
                                emojiUpdateAt.setName(emojiFile.EmojiName);
                                emojiUpdateAt.setStatus(0);
                                emojiUpdateAt.setUpdateAt(emojiFile.getUpdatedAt());
                                emojiUpdateAt.setInsertTime(DateUtil.getCurrentTime2());

                                if (dbHelper.isEmojiExisted(emojiFile.getObjectId())) {
                                    LogUtil.i(TAG, "已经存在此数据，更新");

                                    dbHelper.updateEmojiUT(emojiUpdateAt);

                                    for (int i = 0; i < newEmojiUpdates.size(); i++) {
                                        if (newEmojiUpdates.get(i).getObjectId().equals(emojiFile.getObjectId())) {
                                            newEmojiUpdates.remove(i);
                                            newEmojiUpdates.add(emojiUpdateAt);
                                            break;
                                        }
                                    }
                                } else {
                                    LogUtil.i(TAG, "没有存在此数据，直接插入");
                                    dbHelper.insertEmojiUTItem(emojiUpdateAt);
                                    newEmojiUpdates.add(emojiUpdateAt);
                                }
                                if (mCurr == (list.size() - 1)) {
                                    sortByUpdateAt(newEmojiUpdates);

                                    mPtrFrameLayout.refreshComplete();
                                    if (adapter == null) {
                                        adapter = new EmojiDownAdapter(mContext, newEmojiUpdates);
                                        mRecyView.setAdapter(adapter);
                                    } else {
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(int i, String s) {
                                AppUtils.failedLog("下载emoji失败", i, s);
                                AppUtils.toast0(mContext, "下载失败");
                            }
                        });
                    }
                } else {//不下载，直接从SD卡里面拿数据
                    mPtrFrameLayout.refreshComplete();
                }


            }

            @Override
            public void onError(int i, String s) {
                AppUtils.failedLog("查询失败", i, s);
            }
        });
    }


    @Override
    public void onClick(Button button, final int pos) {
        String filePath = newEmojiUpdates.get(pos).getPath();
        LogUtil.i(TAG, "GETdATE=" + filePath);//  /storage/emulated/0/com.carrie.lldiary/Emoji/emojiUrl/emojiUrl.txt
        String rootPath = FileUtils.getSubName(filePath, '/') + "/";
        LogUtil.i(TAG, "rootPath=" + rootPath);
        getData(filePath);
        downEmoji(rootPath, button, newEmojiUpdates.get(pos).getObjectId());
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWNLOADING:
                    mButton.setText(mDownDecimal);
                    break;
                case DOWN_FINISH:
                    Button button = (Button) msg.obj;
                    button.setText("已下载");
                    button.setClickable(false);
                    button.setBackgroundColor(getResources().getColor(R.color.main_light_gray));
                    break;
                case DOWN_FAILED:

                    break;
            }


        }
    };

    private EmojiUrl mEmojiUrl;
    private String[] mUrls;

    /**
     * 解析json
     */
    private void getData(String fullPath) {
        String content = readFile(fullPath);
        mEmojiUrl = new Gson().fromJson(content, EmojiUrl.class);
        mUrls = mEmojiUrl.url;
        LogUtil.i(TAG, "mUrls.length=" + mUrls.length);
    }

    /**
     * 下载单个图片
     *
     * @param dir
     */
    private void downEmoji(String dir, final Button button, final String objectId) {
        mDownProgress = 0;
        mButton = button;

        for (int i = 0; i < mUrls.length; i++) {

            BmobFile bmobFile = new BmobFile(i + ".png", "", mUrls[i].toString());
            bmobFile.download(mContext, new File(dir + bmobFile.getFilename()), new DownloadFileListener() {
                @Override
                public void onSuccess(String s) {
                    mDownProgress++;
                    mDownDecimal = AppUtils.get2Decimal(mDownProgress, mUrls.length - 1);
                    LogUtil.i(TAG, "下载进度=" + mDownDecimal);

                    if (mDownDecimal.contains("100%")) {
                        Message msg = new Message();
                        msg.what = DOWN_FINISH;
                        msg.obj = mButton;
                        mHandler.sendMessage(msg);

                        dbHelper.updateEmojiUTStatus(objectId);
                    } else {
                        Message msg = Message.obtain();
                        msg.what = DOWNLOADING;
                        mHandler.sendMessage(msg);
                    }
                }

                @Override
                public void onFailure(int arg0, String s) {
                    AppUtils.toast0(mContext, "下载失败:" + arg0 + "," + s);
                    AppUtils.failedLog("下载失败", arg0, s);

                    Message msg = new Message();
                    msg.what = DOWN_FAILED;
                    mHandler.sendMessage(msg);
                }
            });
        }
    }

    /**
     * 全路径
     *
     * @param path
     * @return
     */
    private String readFile(String path) {
        LogUtil.i(TAG, "path=" + path);
        String content = "";
        File file = new File(path);
        int length = 0;
        try {
            FileInputStream fis = new FileInputStream(new File(path));
            length = fis.available();
            byte[] buffer = new byte[length];
            fis.read(buffer);
//            content = EncodingUtils.getString(buffer, "UTF-8");
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    public void sortByUpdateAt(ArrayList<EmojiUpdateTime> list) {
        Comparator<EmojiUpdateTime> comparator = new Comparator<EmojiUpdateTime>() {
            @Override
            public int compare(EmojiUpdateTime lhs, EmojiUpdateTime rhs) {
                if (lhs.getUpdateAt().compareTo(rhs.getUpdateAt()) > 0) {
                    return -1;
                } else if (lhs.getUpdateAt().compareTo(rhs.getUpdateAt()) < 0) {
                    return 1;
                } else {
                    return 0;
                }
            }
        };
        Collections.sort(list, comparator);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
