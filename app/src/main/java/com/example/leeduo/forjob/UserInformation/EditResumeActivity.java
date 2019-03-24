package com.example.leeduo.forjob.UserInformation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.leeduo.forjob.R;

/**
 * Created by LeeDuo on 2019/1/28.
 */

public class EditResumeActivity extends AppCompatActivity {
    private ResumeOnlineFragment resumeOnlineFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_resume_online);
        resumeOnlineFragment = new ResumeOnlineFragment();
        FragmentManager fragmentManager =getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //fragmentTransaction.setCustomAnimations(R.anim.slide_right_in,R.anim.slide_right_out,R.anim.slide_right_in,R.anim.slide_right_out);
        fragmentTransaction.add(R.id.fragment_container,resumeOnlineFragment,"resumeOnlineFragment");
        fragmentTransaction.commit();
    }
}
