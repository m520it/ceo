package com.m520it.mobilephone.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.m520it.mobilephone.R;

/**
 * 设置中心的自定义控件
 */
public class SettingItemView extends RelativeLayout{

    private TextView mTvTitle;
    private TextView mTvDesc;
    private CheckBox mCbStatus;

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }
    public SettingItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }
    public SettingItemView(Context context) {
        super(context);
        initView();
    }
    private void initView(){
        View.inflate(getContext(), R.layout.view_setting_item,this);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mTvDesc = (TextView) findViewById(R.id.tv_desc);
        mCbStatus = (CheckBox) findViewById(R.id.cb_status);
    }
    public void setTitle(String title){
        mTvTitle.setText(title);
    }
    public void setDesc(String desc){
        mTvDesc.setText(desc);
    }
    //返回当前复选框的勾选状态
    public boolean isChecked(){
        return mCbStatus.isChecked();
    }
    //设置勾选状态
    public void setChecked(boolean checkedStatus){
        mCbStatus.setChecked(checkedStatus);
    }
 }
