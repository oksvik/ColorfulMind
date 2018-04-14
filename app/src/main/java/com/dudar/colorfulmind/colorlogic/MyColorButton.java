package com.dudar.colorfulmind.colorlogic;

import android.content.Context;
import android.util.AttributeSet;

import com.dudar.colorfulmind.R;

public class MyColorButton extends android.support.v7.widget.AppCompatImageButton {

    private int bgColorId;

    public MyColorButton(Context context) {
        super(context);
    }

    public MyColorButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyColorButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setBackgroundResource(int resId) {
        super.setBackgroundResource(resId);
        bgColorId = resId;
    }

    public int getBgColorId(){
        return bgColorId;
    }

    public void refreshColor(){
        setBackgroundResource(R.color.white);
        bgColorId = 0;
    }
}
