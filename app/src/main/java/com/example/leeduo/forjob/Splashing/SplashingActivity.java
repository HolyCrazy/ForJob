package com.example.leeduo.forjob.Splashing;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.leeduo.forjob.ConfigArgs;
import com.example.leeduo.forjob.MenuActivity;
import com.example.leeduo.forjob.R;
import com.example.leeduo.forjob.UserLoginAndRegistered.UserLoginActivity;

/**
 * Created by LeeDuo on 2019/1/27.
 */
//开屏页
public class SplashingActivity extends AppCompatActivity {

    private Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashing);


        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        startActivity(new Intent(SplashingActivity.this,UserLoginActivity.class));
                        overridePendingTransition(R.anim.activity_anim_scale_enter,0);
                        SplashingActivity.this.finish();
                        break;
                    case 2:
                        startActivity(new Intent(SplashingActivity.this,MenuActivity.class));
                        overridePendingTransition(R.anim.activity_anim_scale_enter,0);
                        SplashingActivity.this.finish();
                        break;
                }
            }
        };

        SharedPreferences sharedPreferences = getSharedPreferences(ConfigArgs.USER_INFO, Context.MODE_PRIVATE);
        if(sharedPreferences.getString("state","false").equals("true") && !sharedPreferences.getString("userId","").equals(""))
            handler.sendEmptyMessageDelayed(2,1000);
        else
            handler.sendEmptyMessageDelayed(1,1000);
    }
}
