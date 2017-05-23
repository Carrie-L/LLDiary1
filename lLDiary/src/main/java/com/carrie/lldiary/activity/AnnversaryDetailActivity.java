package com.carrie.lldiary.activity;

import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.carrie.lldiary.R;
import com.carrie.lldiary.base.BaseActivity;
import com.carrie.lldiary.base.MyBaseActivity;
import com.carrie.lldiary.dao.Ann;
import com.carrie.lldiary.utils.AppUtils;
import com.carrie.lldiary.utils.LogUtil;
import com.carrie.lldiary.utils.Rotate3dAnimation;
import com.carrie.lldiary.view.TitleView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.umeng.message.PushAgent;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.carrie.lldiary.R.id.tv;
import static com.carrie.lldiary.R.id.tv_ann_name;
import static com.carrie.lldiary.R.id.tv_name;
import static com.carrie.lldiary.activity.AnniversaryActivity.ANN_EDIT;

/**
 * Created by Administrator on 2016/8/29 0029.
 */
public class AnnversaryDetailActivity extends BaseActivity {
    private static final String TAG = "AnnversaryDetailActivity";
    private Intent intent;
    private Ann ann;
    private Integer mPos;
    private boolean mIsNameFront;//正面，显示名字；反面，显示关系
    private boolean mIsDayFront;//正面，显示名字；反面，显示关系

    @BindView(R.id.tv_ann_big)
    protected TextView tv_big;
    @BindView(R.id.tv_ann_name)
    protected TextView tv_name;
    @BindView(R.id.iv_ann_icon)
    protected ImageView iv_icon;

    @Override
    protected void initPre() {
        mLayoutId = R.layout.activity_ann_detail;
        mTitle = getString(R.string.title_activity_anniversary);
        mRightIcon=R.drawable.ic_edit;
    }

    @Override
    public void initData() {
        ButterKnife.bind(this);
        setData();

        tv_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsNameFront) {
                    tv_name.setText(ann.getPerson());
                    mIsNameFront = false;
                    m3AnimationBackToFront();
                } else {
                    tv_name.setText(ann.getRelationship());
                    mIsNameFront = true;
                    m3AnimationFrontToBack();
                }
            }
        });
        iv_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void setData() {
        Intent intent = getIntent();
        ann = intent.getParcelableExtra("Ann");
        showText();
        titleView.setOnRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AnnversaryDetailActivity.this, AnniversaryEditActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("Ann", ann);
                intent.putExtra("pos", ann);
                startActivityForResult(intent, ANN_EDIT);
            }
        });
    }

    private void showText(){
        StringBuilder sbTime = new StringBuilder();
        sbTime.append(ann.durTime.split("天")[0]).append("天\n").append(ann.durTime.split("天")[1]);
        SpannableString ss = new SpannableString(sbTime.toString());
        AbsoluteSizeSpan span = new AbsoluteSizeSpan(64, true);
        ss.setSpan(span, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tv_big.setText(ss);
        tv_name.setText(ann.getPerson());
        iv_icon.setImageResource(AppUtils.getResourceIdentifier(getResources(), ann.getIcon(), "drawable"));
        sbTime=null;
    }

    private void m3AnimationFrontToBack() {
        int width = tv_name.getWidth();
        int height =  tv_name.getHeight();
        LogUtil.i(TAG, "width=" + width + ",height=" + height);

        Rotate3dAnimation animation = new Rotate3dAnimation(180, 0, width / 2, height / 2, width / 2, true);
        animation.setDuration(1000);
        animation.setInterpolator(new AccelerateInterpolator());
        tv_name.startAnimation(animation);
    }

    private void m3AnimationBackToFront() {
        int width = tv_name.getWidth();
        int height =  tv_name.getHeight();
        LogUtil.i(TAG, "width=" + width + ",height=" + height);

        Rotate3dAnimation animation = new Rotate3dAnimation(0,180, width / 2, height / 2, width / 2, true);
        animation.setDuration(1000);
        animation.setInterpolator(new DecelerateInterpolator());
        tv_name.startAnimation(animation);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtil.e(TAG,"onActivityResult:详情—— 收到了————requestCode="+requestCode+",resultCode="+resultCode);
        if(resultCode==RESULT_OK){
            ann=data.getParcelableExtra("Ann");
            showText();

           setResult(RESULT_OK,data);
        }
    }


}
