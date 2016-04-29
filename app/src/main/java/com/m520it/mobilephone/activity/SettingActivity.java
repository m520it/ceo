package com.m520it.mobilephone.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.m520it.mobilephone.R;
import com.m520it.mobilephone.view.SettingItemView;

/**
 * 设置中心
 */
public class SettingActivity extends AppCompatActivity {

    private SettingItemView mSivUpdate;
    private SharedPreferences mSp;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mSivUpdate = (SettingItemView) findViewById(R.id.siv_update);
        //用sharedPreferences来保存更新状态,以便下次进来界面的时候保证状态
        mSp = getSharedPreferences("SETTING", MODE_PRIVATE);
        mSivUpdate.setTitle("自动更新设置");
        //从用sharedPreferences中拿到状态值,默认情况下是开启自动更新
        boolean auto_update = mSp.getBoolean("AUTO_UPDATE", true);
        if (auto_update){
            mSivUpdate.setDesc("自动更新已开启");
            mSivUpdate.setChecked(true);
        }else{
            mSivUpdate.setDesc("自动更新已关闭");
            mSivUpdate.setChecked(false);
        }
        mSivUpdate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //判断当前勾选状态
                if (mSivUpdate.isChecked()){
                    //如果当前已经勾选
                    mSivUpdate.setChecked(false);
                    mSivUpdate.setDesc("自动更新已关闭");
                    mSp.edit().putBoolean("AUTO_UPDATE",false).commit();
                }else{
                    mSivUpdate.setChecked(true);
                    mSivUpdate.setDesc("自动更新已开启");
                    mSp.edit().putBoolean("AUTO_UPDATE",true).commit();

                }
            }
        });
    }
}
