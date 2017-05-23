package com.carrie.lldiary.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;

import com.carrie.lldiary.R;
import com.carrie.lldiary.R.id;
import com.carrie.lldiary.R.layout;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.fragment.FeedbackFragment;
import com.umeng.message.PushAgent;

/**
 * Demo Activity to use {@link com.umeng.fb.fragment.FeedbackFragment}
 */
public class ConversationDetailActivity extends FragmentActivity {

    private FeedbackFragment mFeedbackFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);
        PushAgent.getInstance(this).onAppStart();//统计应用启动数据
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            String conversation_id = getIntent().getStringExtra(FeedbackFragment.BUNDLE_KEY_CONVERSATION_ID);
            mFeedbackFragment = FeedbackFragment.newInstance(conversation_id);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container,mFeedbackFragment)
                    .commit();
        }
    }

    @Override
    protected void onNewIntent(android.content.Intent intent) {
        mFeedbackFragment.refresh();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
 

}
