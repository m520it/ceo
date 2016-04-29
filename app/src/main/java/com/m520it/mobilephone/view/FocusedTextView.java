package com.m520it.mobilephone.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;
public class FocusedTextView extends TextView {
    public FocusedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public FocusedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public FocusedTextView(Context context) {
        super(context);
    }
    public boolean isFocused() {
        return true;
    }
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
    }
}
