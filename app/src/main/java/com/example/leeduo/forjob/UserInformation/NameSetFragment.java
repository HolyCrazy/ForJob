package com.example.leeduo.forjob.UserInformation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.leeduo.forjob.R;

/**
 * Created by LeeDuo on 2019/1/30.
 */

public class NameSetFragment extends Fragment {
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_name_set,container,false);
        return view;
    }

//    @Override
//    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
//        if(enter){
//            return AnimationUtils.loadAnimation(getActivity(),R.anim.slide_bottom_in);
//        }else{
//            return AnimationUtils.loadAnimation(getActivity(),R.anim.slide_right_out);
//        }
//    }
}
