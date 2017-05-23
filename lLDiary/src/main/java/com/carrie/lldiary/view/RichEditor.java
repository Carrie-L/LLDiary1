package com.carrie.lldiary.view;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Created by new on 2017/4/14.
 */

public class RichEditor extends android.support.v7.widget.AppCompatTextView {
    public RichEditor(Context context) {
        super(context);
    }

    public RichEditor(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RichEditor(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(Context context){

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
