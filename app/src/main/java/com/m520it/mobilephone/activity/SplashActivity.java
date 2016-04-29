package com.m520it.mobilephone.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.m520it.mobilephone.R;
import com.m520it.mobilephone.utils.StreamUtil;

import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SplashActivity extends AppCompatActivity {
    private static final int CODE_UPDATE_DIALOG = 0;
    private static final int CODE_ENTER_HOME = 1;
    private TextView mTvVersion;
    private String mDownloadUrl;
    private String mDescription;
    private int mVersionCode;
    private String mVersionName;
    private TextView mTvProgress;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CODE_UPDATE_DIALOG:
                    showUpdateDialog();
                    break;
                case CODE_ENTER_HOME:
                    enterHome();
                    break;
            }
        }
    };
    private RelativeLayout mRlRoot;
    private ImageView mIv;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mTvVersion = (TextView) findViewById(R.id.tv_version);
        mTvProgress = (TextView) findViewById(R.id.tv_progress);
        mRlRoot = (RelativeLayout) findViewById(R.id.rl_root);
        mIv = (ImageView) findViewById(R.id.iv);
        mTvVersion.setText("版本名:" + getVersionName());
        SharedPreferences sp = getSharedPreferences("SETTING", MODE_PRIVATE);
        boolean auto_update = sp.getBoolean("AUTO_UPDATE", true);
        if (auto_update) {
            checkUpdate();
        } else {
            //如果没有开启自动更新,handler发送一个延迟消息,2秒后进入主界面
            handler.sendEmptyMessageDelayed(CODE_ENTER_HOME, 2000);
        }
        //设置渐变动画
        AlphaAnimation anim = new AlphaAnimation(0.3f, 1.0f);
        anim.setDuration(3000);
       // mRlRoot.startAnimation(anim);
        RotateAnimation rotate = new RotateAnimation(0, 360000, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(2000);
        //mIv.startAnimation(rotate);
    }


    /**
     * 动态获取当前版本信息
     */
    private int getVersionCode() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            int versionCode = packageInfo.versionCode;
            return versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    private String getVersionName() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String versionName = packageInfo.versionName;
            return versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 从服务器获取版本信息并校验
     */
    private void checkUpdate() {
        final long startTime = System.currentTimeMillis();
        //访问网络操作要新开启一个子线程
        new Thread() {
            public void run() {
                Message msg = new Message();
                HttpURLConnection conn = null;
                try {
                    URL url = new URL("http://192.168.30.231/update.json");
                    conn = (HttpURLConnection) url.openConnection();
                    conn.connect();
                    int responseCode = conn.getResponseCode();
                    if (responseCode == 200) {
                        InputStream is = conn.getInputStream();
                        String result = StreamUtil.readFromStream(is);
                        //解析json
                        JSONObject json = new JSONObject(result);
                        //从服务器拿到版本名,版本号
                        mVersionName = json.getString("versionName");
                        mVersionCode = json.getInt("versionCode");
                        mDescription = json.getString("description");
                        mDownloadUrl = json.getString("downloadUrl");
                        //版本校验
                        if (mVersionCode > getVersionCode()) {
                            msg.what = CODE_UPDATE_DIALOG;
                        } else {
                            //没有版本更新
                            msg.what = CODE_ENTER_HOME;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    long endTime = System.currentTimeMillis();
                    long usedTime = endTime - startTime;
                    //强制睡眠2秒,保证splash页面的正常刷新
                    if (usedTime < 2000) {
                        try {
                            Thread.sleep(2000 - usedTime);
                            handler.sendMessage(msg);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
            }
        }.start();
    }

    //弹出升级对话框
    private void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("最新版本:" + mVersionName);
        builder.setMessage(mDescription);
        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                download();
            }
        });
        builder.setNegativeButton("下次再说", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                enterHome();
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                enterHome();
            }
        });
        builder.create();
        builder.show();
    }

    //下载更新
    private void download() {
        //开始下载设置可见
        mTvProgress.setVisibility(View.VISIBLE);
        HttpUtils utils = new HttpUtils();
        //target:下载目标存放路径
        String target = Environment.getExternalStorageDirectory() + "/update.apk";
        utils.download(mDownloadUrl, target, new RequestCallBack<File>() {
            //文件下载进度
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
                Log.v("520it", "下载进度" + current / total);
                mTvProgress.setText("下载进度:" + current * 100 / total + "%");
            }

            public void onSuccess(ResponseInfo<File> responseInfo) {
                Toast.makeText(SplashActivity.this, "下载成功", Toast.LENGTH_LONG).show();
                //下载成功后跳转到下载界面
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setDataAndType(Uri.fromFile(responseInfo.result), "application/vnd.android.package-archive");
                startActivityForResult(intent, 0);
            }

            public void onFailure(HttpException e, String s) {
                Toast.makeText(SplashActivity.this, "下载失败", Toast.LENGTH_LONG).show();
            }
        });
    }

    //取消安装,回调此方法,进入主界面
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        enterHome();
        super.onActivityResult(requestCode, resultCode, data);
    }

    //进入主界面
    private void enterHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
