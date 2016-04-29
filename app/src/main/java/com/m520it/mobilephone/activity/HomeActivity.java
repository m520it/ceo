package com.m520it.mobilephone.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.m520it.mobilephone.R;

public class HomeActivity extends Activity {

    private GridView mHomeGv;
    private String[] mItemDatas;
    private int[] mItemPictures;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mHomeGv = (GridView) findViewById(R.id.home_gv);
        mItemDatas = new String[]{"手机防盗", "通讯卫士", "软件管理", "进程管理", "流量统计", "手机杀毒", "缓存清理", "高级工具", "设置中心"};
        mItemPictures = new int[]{R.drawable.safe, R.drawable.callmsgsafe, R.drawable.app, R.drawable.taskmanager, R.drawable.netmanager, R.drawable.trojan, R.drawable.sysoptimize, R.drawable.atools, R.drawable.settings};
        mHomeGv.setAdapter(new BaseAdapter() {
            public int getCount() {

                return mItemDatas.length;
            }

            public Object getItem(int position) {
                return null;
            }

            public long getItemId(int position) {
                return 0;
            }
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = View.inflate(HomeActivity.this, R.layout.home_list, null);
                ImageView ivItem = (ImageView) view.findViewById(R.id.iv_item);
                TextView tvItem = (TextView) view.findViewById(R.id.tv_item);
                ivItem.setImageResource(mItemPictures[position]);
                tvItem.setText(mItemDatas[position]);
                return view;
            }
        });
        //给GridView设置条目监听事件,跳转页面
        mHomeGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 8:
                        Intent intent = new Intent(HomeActivity.this, SettingActivity.class);
                       startActivity(intent);
                        break;
                }
            }
        });
    }
}
