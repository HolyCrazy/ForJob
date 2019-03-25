package com.example.leeduo.forjob.companyShow;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.leeduo.forjob.Adapters.ViewPagerFragmentAdapter;
import com.example.leeduo.forjob.ConfigArgs;
import com.example.leeduo.forjob.HeadZoomScrollView;
import com.example.leeduo.forjob.JsonBean.JsonSingleCompanyBean;
import com.example.leeduo.forjob.R;
import com.example.leeduo.forjob.RetrofitService;
import com.example.leeduo.forjob.Tools.ScreenTools;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;

/**
 * Created by LeeDuo on 2019/2/7.
 */
//公司信息展示界面，下属三个界面，CompanyInfoFragment,CompanyPositionsFragment,CompanySpaceFragment
public class CompanyShowFragment extends Fragment implements View.OnClickListener{
    private View mView,dividerLine,tabLine;
    private HeadZoomScrollView headZoomScrollView;
    //private ScrollView headZoomScrollView;
    private FrameLayout toolBarFrame;
    private ImageView backImage,shareImage;
    private TextView titleText,tabText;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ViewPagerFragmentAdapter viewPagerFragmentAdapter;
    private ArrayList<Fragment> fragmentArrayList;
    private CompanyInfoFragment companyInfoFragment;
    private CompanyPositionsFragment companyPositionsFragment;
    private CompanySpaceFragment companySpaceFragment;
    private int companyId;
    private Bundle bundle;

    private ImageView companyShowImage,companyLogo;
    private TextView companyShortName,companyLocation,companyFinanceStage,companySize,companyIndustryField;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_company_show, container, false);
        //获取参数
        bundle = getArguments();
        companyId = bundle.getInt("company_id",62);
        //绑定View
        findView();
        //View Pager设置
        viewPagerSet();


        tabSet();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                //tab设置
//                tabSet();
//            }
//        }).start();

        //开新线程进行网络请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(ConfigArgs.SERVER_WEB)
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .build();
                RetrofitService retrofitService = retrofit.create(RetrofitService.class);
                retrofitService.getSingleCompanyJsonString(companyId)//请求公司信息
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<JsonSingleCompanyBean>() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {

                            }

                            @Override
                            public void onNext(@NonNull JsonSingleCompanyBean jsonSingleCompanyBean) {
                                //数据填充
                                Picasso.with(getActivity()).
                                        load("http://"+jsonSingleCompanyBean.getContent().getCompanyLogo().toString()).
                                        placeholder(R.drawable.upload_pic_loadding).
                                        error(R.drawable.upload_pic_fail).
                                        into(companyShowImage);
                                Picasso.with(getActivity()).
                                        load("http://"+jsonSingleCompanyBean.getContent().getCompanyLogo().toString()).
                                        placeholder(R.drawable.upload_pic_loadding).
                                        error(R.drawable.upload_pic_fail).
                                        into(companyLogo);
                                titleText.setText(jsonSingleCompanyBean.getContent().getCompanyShortName());
                                companyShortName.setText(jsonSingleCompanyBean.getContent().getCompanyShortName());
                                companyLocation.setText(jsonSingleCompanyBean.getContent().getCity());
                                companyFinanceStage.setText(jsonSingleCompanyBean.getContent().getFinanceStage());
                                companySize.setText(jsonSingleCompanyBean.getContent().getCompanySize());
                                companyIndustryField.setText(jsonSingleCompanyBean.getContent().getIndustryField());
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }
        }).start();

        //交互事件
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabText = tab.getCustomView().findViewById(R.id.tab_item_text);
                tabText.setTextAppearance(R.style.textAppearanceBold);
                tabLine = tab.getCustomView().findViewById(R.id.green_line);
                tabLine.setVisibility(View.VISIBLE);
            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tabText = tab.getCustomView().findViewById(R.id.tab_item_text);
                tabText.setTextAppearance(R.style.textAppearanceNormal);
                tabLine = tab.getCustomView().findViewById(R.id.green_line);
                tabLine.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //对自定义组件可以拉动的Scroll View设置滑动监听
        headZoomScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int y = headZoomScrollView.getScrollY();//获取纵向滚动数值
                int height = toolBarFrame.getHeight();//获取顶部toolbar的高度
                try {
                    if(y <= 0){//向下滚动
                        toolBarFrame.setBackgroundColor(Color.argb(0,246,246,246));
                        titleText.setTextColor(Color.argb(0,246,246,246));
                        backImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_back_white));
                        shareImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_share_company_white));
                        dividerLine.setVisibility(View.INVISIBLE);
                    }else if(y > 0 && y <= (height+200)){//向上滚动距离为height+200范围内
                        float scale = (float) y/(height+200);//计算滚动占比
                        float alpha = 255*scale>246?246:255*scale;//计算透明度
                        float textColor = (255 - 255*scale)<51?51:(255 - 255*scale);//计算字体颜色
                        //图标设置
                        if(alpha>125){
                            backImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_back_normal));
                            shareImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_share_company_gray));
                            dividerLine.setVisibility(View.VISIBLE);
                        }else{
                            backImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_back_white));
                            shareImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_share_company_white));
                            dividerLine.setVisibility(View.INVISIBLE);
                        }
                        //字色，背景设置
                        titleText.setTextColor(Color.argb((int) alpha,(int) textColor,(int) textColor,(int) textColor));
                        toolBarFrame.setBackgroundColor(Color.argb((int) alpha,246,246,246));
                    }else{//其他情况
                        toolBarFrame.setBackgroundColor(Color.argb(255,246,246,246));
                        titleText.setTextColor(Color.argb(255,51,51,51));
                        backImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_back_normal));
                        shareImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_share_company_gray));
                        dividerLine.setVisibility(View.VISIBLE);
                    }
                }catch (Exception e){

                }
            }
        });

        backImage.setOnClickListener(this);

        return mView;
    }


    //tab layout初始化
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void tabSet(){
        mTabLayout.getTabAt(0).setCustomView(R.layout.tab_layout_item);
        tabText =mTabLayout.getTabAt(0).getCustomView().findViewById(R.id.tab_item_text);
        tabLine = mTabLayout.getTabAt(0).getCustomView().findViewById(R.id.green_line);
        tabLine.setVisibility(View.INVISIBLE);
        tabText.setTextSize(16);
        tabText.setText("基本信息");
        tabText.setTextColor(Color.parseColor("#333333"));

        mTabLayout.getTabAt(1).setCustomView(R.layout.tab_layout_item);
        tabText =mTabLayout.getTabAt(1).getCustomView().findViewById(R.id.tab_item_text);
        tabLine = mTabLayout.getTabAt(1).getCustomView().findViewById(R.id.green_line);
        tabLine.setVisibility(View.VISIBLE);
        tabText.setTextSize(16);
        tabText.setText("在招职位");
        tabText.setTextColor(Color.parseColor("#333333"));
        tabText.setTextAppearance(R.style.textAppearanceBold);


        mTabLayout.getTabAt(2).setCustomView(R.layout.tab_layout_item);
        tabText =mTabLayout.getTabAt(2).getCustomView().findViewById(R.id.tab_item_text);
        tabLine = mTabLayout.getTabAt(2).getCustomView().findViewById(R.id.green_line);
        tabLine.setVisibility(View.INVISIBLE);
        tabText.setTextSize(16);
        tabText.setText("企业数据");
        tabText.setTextColor(Color.parseColor("#333333"));
    }
    //绑定视图
    private void findView(){
        headZoomScrollView = mView.findViewById(R.id.head_zoom_scroll);
        toolBarFrame = mView.findViewById(R.id.frame_tool_bar);
        dividerLine = mView.findViewById(R.id.divider_line);
        dividerLine.setVisibility(View.INVISIBLE);
        backImage =mView.findViewById(R.id.back_image);
        shareImage = mView.findViewById(R.id.share_image);
        titleText = mView.findViewById(R.id.title_text);
        mTabLayout = mView.findViewById(R.id.my_tab_layout);
        mViewPager = mView.findViewById(R.id.my_view_pager);

        companyShowImage = mView.findViewById(R.id.company_show_image);
        companyLogo =mView.findViewById(R.id.company_logo);
        companyShortName =mView.findViewById(R.id.company_short_name);
        companyLocation =mView.findViewById(R.id.company_location);
        companyFinanceStage =mView.findViewById(R.id.company_finance_stage);
        companySize = mView.findViewById(R.id.company_size);
        companyIndustryField = mView.findViewById(R.id.company_industry_field);
    }
    //设置viewPager及其和tabLayout的联动
    private void viewPagerSet(){
        fragmentArrayList = new ArrayList<>();

        companyInfoFragment = new CompanyInfoFragment();
        companyInfoFragment.setArguments(bundle);
        companyInfoFragment.setHeadZoomScrollView(headZoomScrollView);

        companyPositionsFragment = new CompanyPositionsFragment();
        companyPositionsFragment.setHeadZoomScrollView(headZoomScrollView);
        companyPositionsFragment.setArguments(bundle);

        companySpaceFragment = new CompanySpaceFragment();
        companySpaceFragment.setHeadZoomScrollView(headZoomScrollView);
        companySpaceFragment.setArguments(bundle);

        fragmentArrayList.add(companyInfoFragment);
        fragmentArrayList.add(companyPositionsFragment);
        fragmentArrayList.add(companySpaceFragment);
        viewPagerFragmentAdapter = new ViewPagerFragmentAdapter(getActivity().getSupportFragmentManager(),fragmentArrayList);
        mViewPager.setAdapter(viewPagerFragmentAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        mViewPager.setCurrentItem(1);
        //////////////////////////////////////////////////////////////////////////////
        float screenHeight = getResources().getDimension(R.dimen.dp_120);
        ViewGroup.LayoutParams l = mViewPager.getLayoutParams();
        l.height = (int) (ScreenTools.getScreenHeight(getActivity()) -screenHeight);
        mViewPager.setLayoutParams(l);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.gc();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.back_image:
                getActivity().finish();
                break;
        }
    }
}
