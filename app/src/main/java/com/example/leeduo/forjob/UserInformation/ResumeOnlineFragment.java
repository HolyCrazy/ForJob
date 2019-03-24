package com.example.leeduo.forjob.UserInformation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.leeduo.forjob.R;

/**
 * Created by LeeDuo on 2019/1/28.
 */

public class ResumeOnlineFragment extends Fragment implements View.OnClickListener {
    private View mView;
    private LinearLayout userPrivateInfoSet;
    private PrivateUserInfoFragment privateUserInfoFragment;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_resume_online,container,false);
        privateUserInfoFragment = new PrivateUserInfoFragment();
        userPrivateInfoSet = mView.findViewById(R.id.user_private_info_set);
        userPrivateInfoSet.setOnClickListener(this);
        return mView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.user_private_info_set:
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_right_in,0,0,R.anim.slide_right_out);
                if(!fragmentManager.getFragments().contains(privateUserInfoFragment)){
                    fragmentTransaction.add(R.id.fragment_container,privateUserInfoFragment,"privateUserInfoFragment");
                }
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.hide(getActivity().getSupportFragmentManager().findFragmentByTag("resumeOnlineFragment"));
                fragmentTransaction.commit();
                break;
        }
    }
}
