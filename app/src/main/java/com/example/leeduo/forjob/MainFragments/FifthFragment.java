package com.example.leeduo.forjob.MainFragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.leeduo.forjob.UserInformation.EditResumeActivity;
import com.example.leeduo.forjob.R;

/**
 * Created by LeeDuo on 2019/1/26.
 */

public class FifthFragment extends Fragment implements View.OnClickListener{
    private View mView;
    private LinearLayout userInfoSet,attachResume,deliveryResume,attentCompany,positionCollect;
    private LinearLayout jobExcept,privacySetting,myWallet,notification,setting,feedBack;
    private Intent intent;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_five,container,false);

        findView();

        setClickListener();

        return mView;
    }


    private void findView(){
        userInfoSet = mView.findViewById(R.id.user_info_set);

        attachResume = mView.findViewById(R.id.attach_resume);
        deliveryResume = mView.findViewById(R.id.resume_delivery);
        attentCompany = mView.findViewById(R.id.attent_company);
        positionCollect = mView.findViewById(R.id.position_collect);

        jobExcept = mView.findViewById(R.id.job_except);
        privacySetting = mView.findViewById(R.id.privacy_setting);
        myWallet = mView.findViewById(R.id.my_wallet);
        notification = mView.findViewById(R.id.notification);
        setting = mView.findViewById(R.id.setting);
        feedBack = mView.findViewById(R.id.feedback);
    }
    private void setClickListener(){
        userInfoSet.setOnClickListener(this);
        attachResume.setOnClickListener(this);
        deliveryResume.setOnClickListener(this);
        attentCompany.setOnClickListener(this);
        positionCollect.setOnClickListener(this);

        jobExcept.setOnClickListener(this);
        privacySetting.setOnClickListener(this);
        myWallet.setOnClickListener(this);
        notification.setOnClickListener(this);
        setting.setOnClickListener(this);
        feedBack.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.user_info_set:
                intent = new Intent(mView.getContext(),EditResumeActivity.class);
                startActivity(intent);
                break;
            case R.id.attach_resume:
                break;
            case R.id.resume_delivery:
                break;
            case R.id.attent_company:
                break;
            case R.id.position_collect:
                break;
            case R.id.job_except:
                break;
            case R.id.privacy_setting:
                break;
            case R.id.my_wallet:
                break;
            case R.id.notification:
                break;
            case R.id.setting:
                break;
            case R.id.feedback:
                break;
        }
    }
}
