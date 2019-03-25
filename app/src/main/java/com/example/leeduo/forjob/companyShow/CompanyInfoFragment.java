package com.example.leeduo.forjob.companyShow;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.NestedScrollView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.leeduo.forjob.ConfigArgs;
import com.example.leeduo.forjob.HeadZoomScrollView;
import com.example.leeduo.forjob.JsonBean.JsonSingleCompanyBean;
import com.example.leeduo.forjob.LocationShowFragment;
import com.example.leeduo.forjob.R;
import com.example.leeduo.forjob.RetrofitService;
import com.example.leeduo.forjob.Tools.ScreenTools;

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
 * Created by LeeDuo on 2019/2/9.
 */

public class CompanyInfoFragment extends Fragment {
    private View mView;
    private Bundle bundle;
    private int companyId;
    private TextView companyInfo,companyCity,companyLocation,companyFullName,companyRegisteredTime,companyAuthorityAddress,companyLegalPerson;
    private LinearLayout companyLocationParent;
    private Retrofit retrofit;
    private double latitude,longitude;
    private LocationShowFragment locationShowFragment;
    private String locationString,companyShortNameString;
    private NestedScrollView nestedScrollView;
    private HeadZoomScrollView headZoomScrollView;
    //private double screenHeight;
    private int delY =0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_company_info,container,false);

        bundle = getArguments();
        companyId = bundle.getInt("company_id",62);
        //screenHeight = ScreenTools.getScreenHeight(getContext())*0.29;
//        screenHeight = getResources().getDimension(R.dimen.dp_570);
//        DisplayMetrics dm = getResources().getDisplayMetrics();
//        screenHeight = screenHeight / dm.density;
//        Log.d(TAG, "onCreateView: "+screenHeight);

        companyInfo = mView.findViewById(R.id.company_info);
        companyCity = mView.findViewById(R.id.company_city);
        companyLocation = mView.findViewById(R.id.company_location);
        companyFullName = mView.findViewById(R.id.company_full_name);
        companyRegisteredTime = mView.findViewById(R.id.company_registered_time);
        companyAuthorityAddress = mView.findViewById(R.id.company_authority_address);
        companyLegalPerson = mView.findViewById(R.id.company_legal_person);
        companyLocationParent = mView.findViewById(R.id.company_location_parent);
        nestedScrollView = mView.findViewById(R.id.scroll_parent);

        //nestedScrollView.setNestedScrollingEnabled(true);
        retrofit = new Retrofit.Builder()
                .baseUrl(ConfigArgs.SERVER_WEB)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofit.create(RetrofitService.class).getSingleCompanyJsonString(companyId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new Observer<JsonSingleCompanyBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull JsonSingleCompanyBean jsonSingleCompanyBean) {
                        companyInfo.setText(jsonSingleCompanyBean.getContent().getDescription());
                        companyCity.setText(jsonSingleCompanyBean.getContent().getRegisteredAddress().split("区")[0] + "区");
                        companyLocation.setText(jsonSingleCompanyBean.getContent().getRegisteredAddress().split("区")[1]);
                        companyFullName.setText(jsonSingleCompanyBean.getContent().getCompanyFullName());
                        companyRegisteredTime.setText(jsonSingleCompanyBean.getContent().getOperatingPeriod().split("至")[0]);
                        companyAuthorityAddress.setText(jsonSingleCompanyBean.getContent().getRegistrationAuthority());
                        companyLegalPerson.setText(jsonSingleCompanyBean.getContent().getPerson().getPersonName());

                        latitude = Double.valueOf(jsonSingleCompanyBean.getContent().getLatitude());
                        longitude = Double.valueOf(jsonSingleCompanyBean.getContent().getLongitude());

                        locationString = jsonSingleCompanyBean.getContent().getRegisteredAddress();
                        companyShortNameString = jsonSingleCompanyBean.getContent().getCompanyShortName();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });


        companyLocationParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationShowFragment = new LocationShowFragment();
                bundle = new Bundle();
                bundle.putDouble("latitude",latitude);
                bundle.putDouble("longitude",longitude);
                bundle.putString("location",locationString);
                bundle.putString("company_short_name",companyShortNameString);
                locationShowFragment.setArguments(bundle);
                changeFragment(locationShowFragment,"locationShowFragment");

            }
        });

//        headZoomScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
//            @Override
//            public void onScrollChanged() {
//                int y = headZoomScrollView.getScrollY();
//                if(y<screenHeight){
//                    synchronized (this){
//                        try{
//                            nestedScrollView.post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    nestedScrollView.setNestedScrollingEnabled(false);
//                                    if(nestedScrollView.getParent() != null)
//                                    nestedScrollView.getParent().requestDisallowInterceptTouchEvent(false);
//                                }
//                            });
//                        }catch(Exception e){}
//
//                    }
//                }else {
//                    synchronized (this){
//                        try{
//                            nestedScrollView.post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    nestedScrollView.setNestedScrollingEnabled(true);
//                                    if(nestedScrollView.getParent() != null)
//                                    nestedScrollView.getParent().requestDisallowInterceptTouchEvent(true);
//                                }
//                            });
//                        }catch(Exception e){}
//
//                    }
//                }
//
//            }
//
//        });
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(final NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int dy = scrollY;
                //上划
                if(dy>0){
                    //没到底
                    if (headZoomScrollView.getChildAt(0)  != null && headZoomScrollView.getChildAt(0) .getMeasuredHeight() > headZoomScrollView.getScrollY() + headZoomScrollView.getHeight()) {
                        delY += dy;
                        //上弹
                        synchronized (Object.class){
                            headZoomScrollView.post(new Runnable() {
                                @Override
                                public void run() {//将滚动量，传递给外部ScrollView
                                    v.setNestedScrollingEnabled(false);//禁止recyclerView滚动
                                    headZoomScrollView.scrollBy(0,delY);//scroll View滚动
                                    v.setNestedScrollingEnabled(true);//recycler View恢复滚动
                                }

                            });
                        }
                    }else {//其他情况
                        delY = 0;
                        v.setNestedScrollingEnabled(true);
                    }
                }
            }

        });

        return mView;
    }
    private void changeFragment(Fragment fragment,String fragmentTag){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_right_in,0,0,R.anim.slide_right_out);
        if(!fragmentManager.getFragments().contains(fragment)){
            fragmentTransaction.add(R.id.fragment_container,fragment,fragmentTag);
        }
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.hide(getActivity().getSupportFragmentManager().findFragmentByTag("companyShowFragment"));
        fragmentTransaction.commit();
    }
    //传入Scroll View
    public void setHeadZoomScrollView(HeadZoomScrollView headZoomScrollView) {
        this.headZoomScrollView = headZoomScrollView;
    }

}
