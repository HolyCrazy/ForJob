package com.example.leeduo.forjob;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.ashokvarma.bottomnavigation.TextBadgeItem;
import com.example.leeduo.forjob.MainFragments.FifthFragment;
import com.example.leeduo.forjob.MainFragments.FirstFragment;
import com.example.leeduo.forjob.MainFragments.FourthFragment;
import com.example.leeduo.forjob.MainFragments.SecondFragment;
import com.example.leeduo.forjob.MainFragments.ThirdFragment;


//主界面
public class MenuActivity extends AppCompatActivity {

    private BottomNavigationBar bottomNavigationBar;//底部导航栏
    private TextBadgeItem textBadgeItemOne;//第一个图标右上角数字
    private TextBadgeItem textBadgeItemTwo;//第二个图标右上角数字
    private TextBadgeItem textBadgeItemThree;//第三个图标右上角数字
    private TextBadgeItem textBadgeItemFour;//第四个图标右上角数字
    private TextBadgeItem textBadgeItemFive;//第五个图标右上角数字
    private BottomNavigationItem bottomNavigationItemOne;//导航栏第一栏
    private BottomNavigationItem bottomNavigationItemTwo;//导航栏第二栏
    private BottomNavigationItem bottomNavigationItemThree;//导航栏第三栏
    private BottomNavigationItem bottomNavigationItemFour;//导航栏第四栏
    private BottomNavigationItem bottomNavigationItemFive;//导航栏第五栏
    //对应五个Fragment
    private FirstFragment firstFragment;
    private SecondFragment secondFragment;
    private ThirdFragment thirdFragment;
    private FourthFragment fourthFragment;
    private FifthFragment fifthFragment;

    private int locationPermission,phonePermission,storagePermission;
    private static final int LOCATION_REQUEST = 100;
    private static final int PHONE_REQUEST = 200;
    private static final int STORAGE_REQUEST = 300;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memu);

        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_bar);

        textBadgeItemInitAndSet();

        bottomNavigationBarSet();

        bottomNavigationBarAddItemAndInit();

        textBadgeItemAllHide();

        //fragmentCreate();


        //添加fragment
//        addFragment(firstFragment,"firstFragment");
//        addFragment(secondFragment,"secondFragment");
//        addFragment(thirdFragment,"thirdFragment");
//        addFragment(fourthFragment,"fourthFragment");
//        addFragment(fifthFragment,"fifthFragment");
//        showFragment(firstFragment);
//        hideFragment(secondFragment);
//        hideFragment(thirdFragment);
//        hideFragment(fourthFragment);
//        hideFragment(fifthFragment);

        firstFragment = new FirstFragment();
        addFragment(firstFragment,"firstFragment");
        showFragment(firstFragment);



        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {

                 switch (position){
//                     case 0:
//                         showFragment(firstFragment);
//                         hideFragment(secondFragment);
//                         hideFragment(thirdFragment);
//                         hideFragment(fourthFragment);
//                         hideFragment(fifthFragment);
//                         break;
//                     case 1:
//                         showFragment(secondFragment);
//                         hideFragment(firstFragment);
//                         hideFragment(thirdFragment);
//                         hideFragment(fourthFragment);
//                         hideFragment(fifthFragment);
//                         break;
//                     case 2:
//                         showFragment(thirdFragment);
//                         hideFragment(secondFragment);
//                         hideFragment(firstFragment);
//                         hideFragment(fourthFragment);
//                         hideFragment(fifthFragment);
//                         break;
//                     case 3:
//                         showFragment(fourthFragment);
//                         hideFragment(secondFragment);
//                         hideFragment(thirdFragment);
//                         hideFragment(firstFragment);
//                         hideFragment(fifthFragment);
//                         break;
//                     case 4:
//                         showFragment(fifthFragment);
//                         hideFragment(secondFragment);
//                         hideFragment(thirdFragment);
//                         hideFragment(fourthFragment);
//                         hideFragment(firstFragment);
//                         break;
                     case 0:
                         if(firstFragment == null)
                             firstFragment = new FirstFragment();
                         if(getSupportFragmentManager().findFragmentByTag("firstFragment") == null)
                             addFragment(firstFragment,"firstFragment");
                         showFragment(firstFragment);
                         hideFragment(secondFragment);
                         hideFragment(thirdFragment);
                         hideFragment(fourthFragment);
                         hideFragment(fifthFragment);
                         break;
                     case 1:
                         if(secondFragment == null)
                             secondFragment = new SecondFragment();
                         if(getSupportFragmentManager().findFragmentByTag("secondFragment") == null)
                             addFragment(secondFragment,"secondFragment");
                         showFragment(secondFragment);
                         hideFragment(firstFragment);
                         hideFragment(thirdFragment);
                         hideFragment(fourthFragment);
                         hideFragment(fifthFragment);
                         break;
                     case 2:
                         if(thirdFragment == null)
                             thirdFragment = new ThirdFragment();
                         if(getSupportFragmentManager().findFragmentByTag("thirdFragment") == null)
                             addFragment(thirdFragment,"thirdFragment");
                         showFragment(thirdFragment);
                         hideFragment(secondFragment);
                         hideFragment(firstFragment);
                         hideFragment(fourthFragment);
                         hideFragment(fifthFragment);
                         break;
                     case 3:
                         if(fourthFragment == null)
                             fourthFragment = new FourthFragment();
                         if(getSupportFragmentManager().findFragmentByTag("fourthFragment") == null)
                             addFragment(fourthFragment,"fourthFragment");
                         showFragment(fourthFragment);
                         hideFragment(secondFragment);
                         hideFragment(thirdFragment);
                         hideFragment(firstFragment);
                         hideFragment(fifthFragment);
                         break;
                     case 4:
                         if(fifthFragment == null)
                             fifthFragment = new FifthFragment();
                         if(getSupportFragmentManager().findFragmentByTag("fifthFragment") == null)
                             addFragment(fifthFragment,"fifthFragment");
                         showFragment(fifthFragment);
                         hideFragment(secondFragment);
                         hideFragment(thirdFragment);
                         hideFragment(fourthFragment);
                         hideFragment(firstFragment);
                         break;
                 }
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });

        locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        phonePermission = ContextCompat.checkSelfPermission(this,Manifest.permission.READ_PHONE_STATE);
        storagePermission = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE);

        new Thread(new Runnable() {
            @Override
            public void run() {
                if(locationPermission != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MenuActivity.this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},LOCATION_REQUEST);
                }
                if(phonePermission != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MenuActivity.this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},LOCATION_REQUEST);
                }
                if(storagePermission != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MenuActivity.this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},LOCATION_REQUEST);
                }
            }
        }).start();

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == LOCATION_REQUEST){
            if(grantResults.length>0 && grantResults[0] != PackageManager.PERMISSION_GRANTED){
               MenuActivity.this.finish();
            }
        }
        if(requestCode == LOCATION_REQUEST){
            if(grantResults.length>0 && grantResults[1] != PackageManager.PERMISSION_GRANTED){
                MenuActivity.this.finish();
            }
        }
        if(requestCode == LOCATION_REQUEST){
            if(grantResults.length>0 && grantResults[2] != PackageManager.PERMISSION_GRANTED){
                MenuActivity.this.finish();
            }
        }
    }
    //右上角图标初始化并设置
    private void textBadgeItemInitAndSet(){
        textBadgeItemOne = new TextBadgeItem();
        textBadgeItemOne.setBorderWidth(4);
        textBadgeItemOne.setBackgroundColorResource(R.color.colorAccent);
        textBadgeItemOne.setAnimationDuration(200);
        textBadgeItemOne.setText("1");
        textBadgeItemOne.setHideOnSelect(false);

        textBadgeItemTwo = new TextBadgeItem();
        textBadgeItemTwo.setBorderWidth(4);
        textBadgeItemTwo.setBackgroundColorResource(R.color.colorAccent);
        textBadgeItemTwo.setAnimationDuration(200);
        textBadgeItemTwo.setText("1");
        textBadgeItemTwo.setHideOnSelect(false);

        textBadgeItemThree = new TextBadgeItem();
        textBadgeItemThree.setBorderWidth(4);
        textBadgeItemThree.setBackgroundColorResource(R.color.colorAccent);
        textBadgeItemThree.setAnimationDuration(200);
        textBadgeItemThree.setText("1");
        textBadgeItemThree.setHideOnSelect(false);

        textBadgeItemFour = new TextBadgeItem();
        textBadgeItemFour.setBorderWidth(4);
        textBadgeItemFour.setBackgroundColorResource(R.color.colorAccent);
        textBadgeItemFour.setAnimationDuration(200);
        textBadgeItemFour.setText("1");
        textBadgeItemFour.setHideOnSelect(false);

        textBadgeItemFive = new TextBadgeItem();
        textBadgeItemFive.setBorderWidth(4);
        textBadgeItemFive.setBackgroundColorResource(R.color.colorAccent);
        textBadgeItemFive.setAnimationDuration(200);
        textBadgeItemFive.setText("1");
        textBadgeItemFive.setHideOnSelect(false);
    }
    //隐藏右上角图标
    private void textBadgeItemAllHide(){
        textBadgeItemOne.hide();
        textBadgeItemTwo.hide();
        textBadgeItemThree.hide();
        textBadgeItemFour.hide();
        textBadgeItemFive.hide();
    }
    //底部导航栏设置
    private void bottomNavigationBarSet(){
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottomNavigationBar.setActiveColor(R.color.colorGreen);
        bottomNavigationBar.setInActiveColor("#8e8e8e");
        bottomNavigationBar.setBarBackgroundColor("#f6f6f6");
    }
    //向底部导航栏添加五个栏
    private void bottomNavigationBarAddItemAndInit(){
        bottomNavigationItemOne = new BottomNavigationItem(R.drawable.icon_c_home_pre,"首页");
        bottomNavigationItemOne.setInactiveIcon(getResources().getDrawable(R.drawable.icon_c_home_nor));
        bottomNavigationItemOne.setBadgeItem(textBadgeItemOne);
        bottomNavigationBar.addItem(bottomNavigationItemOne);
        bottomNavigationItemTwo = new BottomNavigationItem(R.drawable.icon_company_pre,"公司");
        bottomNavigationItemTwo.setInactiveIcon(getResources().getDrawable(R.drawable.icon_company_nor));
        bottomNavigationItemTwo.setBadgeItem(textBadgeItemTwo);
        bottomNavigationBar.addItem(bottomNavigationItemTwo);
        bottomNavigationItemThree = new BottomNavigationItem(R.drawable.icon_c_message_pre,"消息");
        bottomNavigationItemThree.setInactiveIcon(getResources().getDrawable(R.drawable.icon_c_message_nor));
        bottomNavigationItemThree.setBadgeItem(textBadgeItemThree);
        bottomNavigationBar.addItem(bottomNavigationItemThree);
        bottomNavigationItemFour = new BottomNavigationItem(R.drawable.icon_community_pre,"言职");
        bottomNavigationItemFour.setInactiveIcon(getResources().getDrawable(R.drawable.icon_community_nor));
        bottomNavigationItemFour.setBadgeItem(textBadgeItemFour);
        bottomNavigationBar.addItem(bottomNavigationItemFour);
        bottomNavigationItemFive = new BottomNavigationItem(R.drawable.icon_c_user_pre,"我");
        bottomNavigationItemFive.setInactiveIcon(getResources().getDrawable(R.drawable.icon_c_user_nor));
        bottomNavigationItemFive.setBadgeItem(textBadgeItemFive);
        bottomNavigationBar.addItem(bottomNavigationItemFive);
        //选中第一个，初始化
        bottomNavigationBar.setFirstSelectedPosition(0);
        bottomNavigationBar.initialise();
    }
//    //生成五个Fragment
//    private void fragmentCreate(){
//        firstFragment = new FirstFragment();
//        secondFragment = new SecondFragment();
//        thirdFragment = new ThirdFragment();
//        fourthFragment = new FourthFragment();
//        fifthFragment = new FifthFragment();
//    }

    //添加Fragment
    private void addFragment(Fragment fragment,String fragmentTag){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if(!fragmentManager.getFragments().contains(fragment)){
            fragmentTransaction.add(R.id.fragment_container,fragment,fragmentTag);
        }
        fragmentTransaction.commit();
    }
    //显示Fragment
    private void showFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.show(fragment);
        fragmentTransaction.commit();
    }
    //隐藏Fragment
    private void hideFragment(Fragment fragment){
        if (fragment == null)
            return;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.hide(fragment);
        fragmentTransaction.commit();
    }
}
