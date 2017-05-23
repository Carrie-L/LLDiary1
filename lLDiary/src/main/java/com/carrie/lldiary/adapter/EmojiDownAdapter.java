package com.carrie.lldiary.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.carrie.lldiary.App;
import com.carrie.lldiary.R;
import com.carrie.lldiary.dao.EmojiUpdateTime;
import com.carrie.lldiary.entity.EmojiUrl;
import com.carrie.lldiary.utils.AppUtils;
import com.carrie.lldiary.utils.LogUtil;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.DownloadFileListener;

//import com.sina.org.apache.http.util.EncodingUtils;

/**
 * Created by Administrator on 2016/5/2 0002.
 */
public class EmojiDownAdapter extends RecyclerView.Adapter<EmojiDownAdapter.DownViewHolder> {
    private static final String TAG = "EmojiDownAdapter";
    private String mRootDir;
    private ArrayList<EmojiUpdateTime> files;
    private Context mContext;
    private EmojiUrl mEmojiUrl;
    private String[] mUrls;

    private int mCurrPos;

    private OnDownClickListener mListener;
    private DownViewHolder mViewHolder;

    public EmojiDownAdapter(Context context,ArrayList<EmojiUpdateTime> files) {
        this.mContext=context;
        this.files = files;

        mRootDir= App.mRootDir+"Emoji/";
        if(!new File(mRootDir).exists()){
            new File(mRootDir).mkdir();
        }

    }

    @Override
    public DownViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_emoji_down,viewGroup,false);
        return new DownViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DownViewHolder downViewHolder,final int i) {
        mCurrPos=i;
        mViewHolder=downViewHolder;
        LogUtil.i(TAG,"mCurrPos="+mCurrPos);

        downViewHolder.iv_emoji.setImageBitmap(BitmapFactory.decodeFile(files.get(i).getPath()));
        downViewHolder.tv_name.setText(files.get(i).getName());

        if(files.get(i).getStatus()==1){
            downViewHolder.btn_down.setText("已下载");
            downViewHolder.btn_down.setClickable(false);
            downViewHolder.btn_down.setBackgroundColor(mContext.getResources().getColor(R.color.main_light_gray));
        }else{
            downViewHolder.btn_down.setText("下载");
            downViewHolder.btn_down.setClickable(true);

            downViewHolder.btn_down.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LogUtil.i(TAG,"i="+i);
                    if(mListener!=null){
                        mListener.onClick(downViewHolder.btn_down,i);
                    }
                }
            });
        }



    }


    @Override
    public int getItemCount() {
        return files.size();
    }

    public class DownViewHolder extends RecyclerView.ViewHolder{
        private ImageView iv_emoji;
        private TextView tv_name;
        private Button btn_down;

        public DownViewHolder(View itemView) {
            super(itemView);

            iv_emoji= (ImageView) itemView.findViewById(R.id.iv_emoji_show);
            tv_name= (TextView) itemView.findViewById(R.id.tv_emoji_name);
            btn_down= (Button) itemView.findViewById(R.id.btn_down_emoji);




        }
    }

    /**
     * 解析json
     */
    private void getData(String fullPath){
        String content= readFile(fullPath);
        mEmojiUrl = new Gson().fromJson(content,EmojiUrl.class);
        mUrls=mEmojiUrl.url;
   }

    /**
     * 下载单个图片
     * @param dir
     */
    private void downEmoji(String dir){
        for(int i=0;i<mUrls.length;i++){
            BmobFile bmobFile=new BmobFile(i+".png","",mUrls[i].toString());
            bmobFile.download(mContext,new File(dir+i+".png"), new DownloadFileListener() {
                @Override
                public void onSuccess(String s) {
                    AppUtils.toast0(mContext,"下载成功:"+s);
                }

                @Override
                public void onFailure(int arg0, String s) {
                    AppUtils.toast0(mContext,"下载失败:"+arg0+","+s);
                    AppUtils.failedLog("下载失败",arg0,s);
                }

                @Override
                public void onProgress(Integer progress, long total) {
                   LogUtil.i(TAG,"progress="+progress);
                   LogUtil.i(TAG,"progress/total="+progress/total);
                }
            });
        }
    }

    /**
     * 全路径
     * @param path
     * @return
     */
    private String readFile(String path) {
        LogUtil.i(TAG,"path="+path);
        String content="";
        File file = new File(path);

        if(file.exists()){
            LogUtil.i(TAG,"文件存在");
        }else{
            LogUtil.i(TAG,"文件bu存在");
        }
        int length = 0;
        try {
            FileInputStream fis = new FileInputStream(new File(path));
            length = fis.available();
            byte [] buffer = new byte[length];
            fis.read(buffer);

//            content = EncodingUtils.getString(buffer, "UTF-8");

            LogUtil.i(TAG,"content111111111111="+content);

            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return content;

    }

    public interface  OnDownClickListener{
        void onClick(Button button,int pos);
    }

    public void setOnDownClickListener(OnDownClickListener listener){
        mListener=listener;
    }


}


