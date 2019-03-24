package com.example.leeduo.forjob.UserLoginAndRegistered;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leeduo.forjob.ConfigArgs;
import com.example.leeduo.forjob.JsonBean.JsonCaptchaBean;
import com.example.leeduo.forjob.JsonBean.JsonLoginSuccessBean;
import com.example.leeduo.forjob.MenuActivity;
import com.example.leeduo.forjob.R;
import com.example.leeduo.forjob.RetrofitService;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by LeeDuo on 2019/3/23.
 */

public class UserLoginActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText userName,userPassword;
    private Button getPasswordButton;
    private TextView userLogin;
    private boolean getPasswordFromSMS = false;
    private RetrofitService retrofitService;
    private int captcha = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        userName = (EditText) findViewById(R.id.user_name);
        userPassword = (EditText) findViewById(R.id.user_password);
        getPasswordButton = (Button) findViewById(R.id.get_password_button);
        userLogin = (TextView) findViewById(R.id.user_login);

        getPasswordButton.setOnClickListener(this);
        userLogin.setOnClickListener(this);

        retrofitService = new Retrofit.Builder()
                .baseUrl(ConfigArgs.SERVER_WEB)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RetrofitService.class);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.get_password_button:
                if(!getPasswordFromSMS){
                    getPasswordButton.setBackground(getResources().getDrawable(R.drawable.cancel_attention_button_shape));
                    getPasswordButton.setTextColor(Color.parseColor("#666666"));
                    getPasswordFromSMS = true;
                    if(!userName.getText().toString().trim().equals("")){
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                retrofitService.getCaptcha(userName.getText().toString())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribeOn(Schedulers.io())
                                        .safeSubscribe(new Observer<JsonCaptchaBean>() {
                                            @Override
                                            public void onSubscribe(@NonNull Disposable d) {

                                            }

                                            @Override
                                            public void onNext(@NonNull JsonCaptchaBean jsonCaptchaBean) {
                                                captcha = jsonCaptchaBean.getContent();

                                            }

                                            @Override
                                            public void onError(@NonNull Throwable e) {
                                                getPasswordButton.setBackground(getResources().getDrawable(R.drawable.attention_button_shape2));
                                                getPasswordButton.setTextColor(Color.parseColor("#0CC388"));
                                                getPasswordFromSMS = false;
                                            }

                                            @Override
                                            public void onComplete() {

                                            }
                                        });
                            }
                        }).start();
                    }
                }else{
                    Toast.makeText(this,"验证码已发送",Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.user_login:
                if(String.valueOf(captcha).equals(userPassword.getText().toString()) && !userName.getText().toString().equals("") && !userPassword.getText().toString().equals("")){
                    synchronized (Object.class){
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                retrofitService.getUserId(userName.getText().toString(),captcha)
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribeOn(Schedulers.io())
                                        .safeSubscribe(new Observer<JsonLoginSuccessBean>() {
                                            @Override
                                            public void onSubscribe(@NonNull Disposable d) {

                                            }

                                            @Override
                                            public void onNext(@NonNull JsonLoginSuccessBean jsonLoginSuccessBean) {
                                                Toast.makeText(UserLoginActivity.this,"登陆成功",Toast.LENGTH_SHORT).show();
                                                SharedPreferences sharedPreferences = getSharedPreferences(ConfigArgs.USER_INFO, Context.MODE_PRIVATE);
                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                editor.putString("state","true");
                                                editor.putString("userId",jsonLoginSuccessBean.getContent());
                                                editor.commit();

                                                Intent intent = new Intent();
                                                intent.setClass(UserLoginActivity.this, MenuActivity.class);
                                                startActivity(intent);
                                                UserLoginActivity.this.finish();
                                            }

                                            @Override
                                            public void onError(@NonNull Throwable e) {
                                                Toast.makeText(UserLoginActivity.this,"登陆失败",Toast.LENGTH_SHORT).show();
                                            }

                                            @Override
                                            public void onComplete() {

                                            }
                                        });
                            }
                        }).start();

                    };
                }else{
                    Toast.makeText(this,"登陆失败",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
