package com.example.leeduo.forjob.UserInformation;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.lib.WheelView;
import com.example.leeduo.forjob.R;
import com.example.leeduo.forjob.Tools.CityJsonParseTool;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by LeeDuo on 2019/1/29.
 */

public class PrivateUserInfoFragment extends Fragment implements View.OnClickListener {
    private View mView;
    private ImageView userImage;
    private NameSetFragment nameSetFragment;
    private TelephoneSetFragment telephoneSetFragment;
    private MailSetFragment mailSetFragment;
    private FrameLayout userName,userBirthday,userSex,userTelNumber,userMail,userLocationCity,userIdentity;
    private CityJsonParseTool tool;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_private_user_info_set,container,false);
        findView();
        setClickListener();
        nameSetFragment = new NameSetFragment();
        telephoneSetFragment = new TelephoneSetFragment();
        mailSetFragment = new MailSetFragment();
        tool = new CityJsonParseTool();
        return mView;
    }
    private void findView(){
        userImage = mView.findViewById(R.id.userImage);
        userName = mView.findViewById(R.id.userName);
        userBirthday = mView.findViewById(R.id.userBirthday);
        userSex = mView.findViewById(R.id.userSex);
        userTelNumber = mView.findViewById(R.id.userTelNumber);
        userMail = mView.findViewById(R.id.userMail);
        userLocationCity = mView.findViewById(R.id.userLocationCity);
        userIdentity = mView.findViewById(R.id.userIdentity);
    }
    private void setClickListener(){
        userImage.setOnClickListener(this);
        userIdentity.setOnClickListener(this);
        userLocationCity.setOnClickListener(this);
        userMail.setOnClickListener(this);
        userBirthday.setOnClickListener(this);
        userName.setOnClickListener(this);
        userSex.setOnClickListener(this);
        userTelNumber.setOnClickListener(this);
    }
    private void changeWindowAlpha(float alpha){
        WindowManager.LayoutParams layoutParams = getActivity().getWindow().getAttributes();
        layoutParams.alpha = alpha;
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getActivity().getWindow().setAttributes(layoutParams);
    }
    private void popupWindowInit(PopupWindow popupWindow){
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setAnimationStyle(R.style.PopupAnimation);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                changeWindowAlpha(1.0f);
            }
        });
    }
    private void changeFragment(Fragment fragment,String fragmentTag){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_bottom_in,0,0,R.anim.slide_right_out);
        if(!fragmentManager.getFragments().contains(fragment)){
            fragmentTransaction.add(R.id.fragment_container,fragment,fragmentTag);
        }
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.hide(getActivity().getSupportFragmentManager().findFragmentByTag("privateUserInfoFragment"));
        fragmentTransaction.commit();
    }
    private void timePickerSetAndShow(){
        Calendar selectTime = Calendar.getInstance();
        selectTime.set(2003,0,1);
        Calendar startTime = Calendar.getInstance();
        startTime.set(1949,12,31);
        Calendar stopTime = Calendar.getInstance();
        stopTime.set(2018,11,31);
        TimePickerView timePickerView = new TimePickerView.Builder(getActivity(), new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {

            }
        }).setType(new boolean[]{true,true,false,false,false,false})
                .setLineSpacingMultiplier(2.0f)
                .setDate(selectTime)
                .setRangDate(startTime,stopTime)
                .setLabel("","","","","","")
                .setContentSize(18)
                .setSubmitColor(getActivity().getResources().getColor(R.color.colorGreen))
                .setCancelColor(Color.parseColor("#666666"))
                .setTitleBgColor(Color.WHITE)
                .setSubCalSize(14)
                .setDividerType(WheelView.DividerType.FILL)
                .build();
        timePickerView.show();
    }
    private void sexPickerSetAndShow(){
        OptionsPickerView optionsPickerView = new OptionsPickerView.Builder(getActivity(), new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {

            }
        }).setLineSpacingMultiplier(2.0f)
                .setLabels("","","")
                .setContentTextSize(20)
                .setSubmitColor(getActivity().getResources().getColor(R.color.colorGreen))
                .setCancelColor(Color.parseColor("#666666"))
                .setTitleBgColor(Color.WHITE)
                .setSubCalSize(14)
                .setDividerType(WheelView.DividerType.FILL)
                .build();
        ArrayList arrayList = new ArrayList();
        arrayList.add("男");
        arrayList.add("女");
        //arrayList.add("保密");
        optionsPickerView.setPicker(arrayList);
        optionsPickerView.show();
    }
    private void cityPickerSetAndShow(){
        tool.parseProvinceJson(tool.getJson("china.json",getActivity()));
        OptionsPickerView optionsPickerView2 = new OptionsPickerView.Builder(getActivity(), new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {

            }
        }).setLineSpacingMultiplier(2.0f)
                .setLabels("","","")
                .setContentTextSize(20)
                .setSubmitColor(getActivity().getResources().getColor(R.color.colorGreen))
                .setCancelColor(Color.parseColor("#666666"))
                .setTitleBgColor(Color.WHITE)
                .setSubCalSize(14)
                .setDividerType(WheelView.DividerType.FILL)
                .build();
        optionsPickerView2.setPicker(tool.getProvinceBeanList(),tool.getCityList());
        optionsPickerView2.show();
    }
    private void identityPickerSetAndShow(){
        OptionsPickerView optionsPickerView1 = new OptionsPickerView.Builder(getActivity(), new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {

            }
        }).setLineSpacingMultiplier(2.0f)
                .setLabels("","","")
                .setContentTextSize(18)
                .setSubmitColor(getActivity().getResources().getColor(R.color.colorGreen))
                .setCancelColor(Color.parseColor("#666666"))
                .setTitleBgColor(Color.WHITE)
                .setSubCalSize(14)
                .setDividerType(WheelView.DividerType.FILL)
                .build();
        ArrayList arrayList1 = new ArrayList();
        arrayList1.add("非学生");
        arrayList1.add("学生");

        optionsPickerView1.setPicker(arrayList1);
        optionsPickerView1.show();
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.userImage:
                View containView = LayoutInflater.from(getActivity()).inflate(R.layout.userimage_choose_layout,null,false);
                final PopupWindow popupWindow = new PopupWindow(containView,ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT,true);
                popupWindowInit(popupWindow);
                TextView textView = containView.findViewById(R.id.change_image_cancel);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
                popupWindow.showAtLocation(v, Gravity.BOTTOM,0,0);
                changeWindowAlpha(0.5f);
                break;
            case R.id.userName:
                changeFragment(nameSetFragment,"nameSetFragment");
                break;
            case R.id.userBirthday:
                timePickerSetAndShow();
                break;
            case R.id.userSex:
                sexPickerSetAndShow();
                break;
            case R.id.userTelNumber:
                changeFragment(telephoneSetFragment,"telephoneSetFragment");
                break;
            case R.id.userMail:
                changeFragment(mailSetFragment,"mailSetFragment");
                break;
            case R.id.userLocationCity:
                cityPickerSetAndShow();
                break;
            case R.id.userIdentity:
                identityPickerSetAndShow();
                break;
        }
    }
}
