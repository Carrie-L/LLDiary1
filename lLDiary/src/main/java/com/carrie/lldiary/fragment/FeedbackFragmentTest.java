package com.carrie.lldiary.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.carrie.lldiary.R;
import com.carrie.lldiary.activity.ConversationDetailActivity;
import com.carrie.lldiary.activity.ConversationListActivity;
import com.carrie.lldiary.activity.SplashActivity;
import com.carrie.lldiary.base.BaseFragment;
import com.umeng.fb.FeedbackAgent;
import com.umeng.fb.fragment.FeedbackFragment;

public class FeedbackFragmentTest extends Fragment {
	private static final String TAG = SplashActivity.class.getName();
	public FeedbackFragmentTest() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
    	BaseFragment.sClsName="FeedBack";
    	
        View root = inflater.inflate(R.layout.fragment_feedback, container, false);

        root.findViewById(R.id.fb_fragment_activity_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), ConversationDetailActivity.class);
                String id = new FeedbackAgent(getActivity()).getDefaultConversation().getId();
                intent.putExtra(FeedbackFragment.BUNDLE_KEY_CONVERSATION_ID, id);
                startActivity(intent);
            }
        });

        root.findViewById(R.id.fb_help_activity_btn).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FeedbackAgent(getActivity()).startFeedbackActivity2();
            }
        });
        root.findViewById(R.id.sdk_fb_activity_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FeedbackAgent(getActivity()).startFeedbackActivity();
            }
        });
        root.findViewById(R.id.multi_conversation_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), ConversationListActivity.class);
                startActivity(intent);
            }
        });


        return root;
    }
}

