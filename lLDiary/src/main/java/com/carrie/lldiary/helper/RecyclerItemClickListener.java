package com.carrie.lldiary.helper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * 封装 RecylerView 的OnItemClickListener
 * <p>使用方法：
 * <p>mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
	<p>	mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, 
	<p>  new RecyclerItemClickListener.OnItemClickListener() {
    <p>		@Overridepublic void onItemClick(View view, int position) {
     <p>    	ToastUtil.show(OneActivity.this, position + "");
     <p>    	}
    <p>	}));
 * @author Administrator
 *
 */
public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {private OnItemClickListener mListener;

    public interface OnItemClickListener {public void onItemClick(View view, int position);
    }

    GestureDetector mGestureDetector;

    public RecyclerItemClickListener(Context context, OnItemClickListener listener) {
        mListener = listener;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    @Override public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        View childView = view.findChildViewUnder(e.getX(), e.getY());
        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
            mListener.onItemClick(childView, view.getChildAdapterPosition(childView));
            return true;
        }
        return false;
    }

    @Override public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) { }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}