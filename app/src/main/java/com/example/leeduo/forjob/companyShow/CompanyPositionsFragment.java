package com.example.leeduo.forjob.companyShow;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.lib.WheelView;
import com.bigkoo.pickerview.listener.OnDismissListener;
import com.example.leeduo.forjob.Adapters.SingleCompanyPositionsAdapter;
import com.example.leeduo.forjob.ConfigArgs;
import com.example.leeduo.forjob.HeadZoomScrollView;
import com.example.leeduo.forjob.JsonBean.JsonPositionsBean;
import com.example.leeduo.forjob.PositionShow.PositionShowActivity;
import com.example.leeduo.forjob.PositionShow.PositionShowFragment;
import com.example.leeduo.forjob.R;
import com.example.leeduo.forjob.RetrofitService;
import com.example.leeduo.forjob.Tools.CityJsonParseTool;
import com.example.leeduo.forjob.Tools.DialogShowTool;
import com.example.leeduo.forjob.Tools.ScreenTools;

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
 * Created by LeeDuo on 2019/2/9.
 */
//所属公司职位展示
public class CompanyPositionsFragment extends Fragment implements View.OnClickListener{
    private View mView;
    private RecyclerView recyclerView;
    private int companyId,page,size;
    private DividerItemDecoration dividerItemDecoration;
    private LinearLayoutManager linearLayoutManager;
    private SingleCompanyPositionsAdapter singleCompanyPositionsAdapter;
    private JsonPositionsBean jsonPositionsBean;
    private Retrofit retrofit;
    private RetrofitService retrofitService;
    private Observer<JsonPositionsBean> observer;
    private HeadZoomScrollView headZoomScrollView;
    private int delY = 0;
    private PositionShowFragment positionShowFragment;
    private Bundle bundle;
    private DialogShowTool dialogShowTool;
    private TextView labelAll,labelTechnology,labelProduct,labelDesign,labelOperating,labelMarket,labelSale,labelFunction,labelOthers,labelSchool;
    private String[] labels;
    private ArrayList<TextView> labelList;
    private int type = 0;
    private Handler handler;
    private TextView citySelect,experienceSelect,salarySelect;
    private ImageView citySelectImage,experienceSelectImage,salarySelectImage;
    private LinearLayout cityParentView,experienceParentView,salaryParentView;
    private boolean citySelectFlag,experienceSelectFlag,salarySelectFlag;
    private String selectCity,selectExperience;
    private Integer selectMinSalary,selectMaxSalary;
    private boolean canScroll = true;
//    private double screenHeight;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_company_positions,container,false);
        //展示加载对话框
        dialogShowTool = new DialogShowTool(getActivity());
        dialogShowTool.show();

        //提取信息
        bundle = getArguments();
        companyId = bundle.getInt("company_id",62);
        page = 1;
        size = 10;
//        screenHeight = getResources().getDimension(R.dimen.dp_570);
//        DisplayMetrics dm = getResources().getDisplayMetrics();
//        screenHeight = screenHeight / dm.density;
//        Log.d(TAG, "onCreateView:_________________ "+screenHeight);

        //筛选标签容器
        labelList = new ArrayList<>();

        //筛选标签视图绑定，设置点击事件，添加到列表中
        labelAll = mView.findViewById(R.id.label_all);
        labelAll.setOnClickListener(this);
        labelList.add(labelAll);
        labelTechnology = mView.findViewById(R.id.label_technology);
        labelTechnology.setOnClickListener(this);
        labelList.add(labelTechnology);
        labelProduct = mView.findViewById(R.id.label_product);
        labelProduct.setOnClickListener(this);
        labelList.add(labelProduct);
        labelDesign = mView.findViewById(R.id.label_design);
        labelDesign.setOnClickListener(this);
        labelList.add(labelDesign);
        labelOperating = mView.findViewById(R.id.label_operating);
        labelOperating.setOnClickListener(this);
        labelList.add(labelOperating);
        labelMarket = mView.findViewById(R.id.label_market);
        labelMarket.setOnClickListener(this);
        labelList.add(labelMarket);
        labelSale = mView.findViewById(R.id.label_sale);
        labelSale.setOnClickListener(this);
        labelList.add(labelSale);
        labelFunction = mView.findViewById(R.id.label_function);
        labelFunction.setOnClickListener(this);
        labelList.add(labelFunction);
        labelOthers = mView.findViewById(R.id.label_others);
        labelOthers.setOnClickListener(this);
        labelList.add(labelOthers);
        labelSchool = mView.findViewById(R.id.label_school);
        labelSchool.setOnClickListener(this);
        labelList.add(labelSchool);
        //筛选标签内容
        labels = new String[]{"全部","技术","产品","设计","运营","市场","销售","职能","其他","校招"};


        //筛选相关视图绑定
        citySelect = mView.findViewById(R.id.city_select);
        experienceSelect = mView.findViewById(R.id.experience_select);
        salarySelect = mView.findViewById(R.id.salary_select);
        citySelectImage = mView.findViewById(R.id.city_select_image);
        experienceSelectImage = mView.findViewById(R.id.experience_select_image);
        salarySelectImage = mView.findViewById(R.id.salary_select_image);
        cityParentView = mView.findViewById(R.id.city_parent_view);
        cityParentView.setOnClickListener(this);
        experienceParentView = mView.findViewById(R.id.experience_parent_view);
        experienceParentView.setOnClickListener(this);
        salaryParentView = mView.findViewById(R.id.salary_parent_view);
        salaryParentView.setOnClickListener(this);
        //标志位初始化，实现双击
        citySelectFlag = false;
        experienceSelectFlag = false;
        salarySelectFlag = false;

        //搜索请求参数初始化
        selectCity = null;//城市
        selectMinSalary = null;//最小工资
        selectMaxSalary = null;//最大工资
        selectExperience = null;//工作经验


        //recyclerView相关视图绑定初始化
        recyclerView = mView.findViewById(R.id.position_recycler_view);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        dividerItemDecoration = new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getActivity().getDrawable(R.drawable.single_company_positions_item_divider));
        recyclerView.addItemDecoration(dividerItemDecoration);


        //初始化设置空数据
        jsonPositionsBean = new JsonPositionsBean();
        singleCompanyPositionsAdapter = new SingleCompanyPositionsAdapter(jsonPositionsBean,getActivity());
        recyclerView.setAdapter(singleCompanyPositionsAdapter);

        //设置网络请求
        retrofit = new Retrofit.Builder()
                .baseUrl(ConfigArgs.SERVER_WEB)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        retrofitService = retrofit.create(RetrofitService.class);
        observer = new Observer<JsonPositionsBean>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull JsonPositionsBean jsonPositionsBean) {

                //根据请求返回数据数量，从而判断footer是否可视
                if (jsonPositionsBean.getContent().length < size){
                    singleCompanyPositionsAdapter.setFootViewVisible(false);
                }else {
                    singleCompanyPositionsAdapter.setFootViewVisible(true);
                }
                //设置数据
                singleCompanyPositionsAdapter.setData(jsonPositionsBean);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                //失败，设置空数据
                singleCompanyPositionsAdapter.setData(jsonPositionsBean);
                dialogShowTool.dismiss();
            }

            @Override
            public void onComplete() {
                dialogShowTool.dismiss();
            }
        };


        //开启新线程，进行网络请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                retrofitService.getSingleCompanyPositionsJsonString(page,size,companyId,null,selectCity,selectMinSalary,selectMaxSalary,selectExperience)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .safeSubscribe(observer);
            }
        }).start();

//        headZoomScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
//            @Override
//            public void onScrollChanged() {
//                int y = headZoomScrollView.getScrollY();
//                Log.d(TAG, "onScrollChanged: _________________"+y);
//                if(y<screenHeight){//////////////////////////////////////////////////////////////////////////////////
//                    if(canScroll){
//                        recyclerView.setNestedScrollingEnabled(false);
//                        canScroll = !canScroll;
//                    }
//                }else {
//                    if(!canScroll){
//                        recyclerView.setNestedScrollingEnabled(true);
//                        canScroll = !canScroll;
//                    }
//
//                }
//
//            }
//        });





        //设置滚动监听
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int lastPosition = 0;//最后一个视图位置

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == RecyclerView.SCROLL_STATE_IDLE){//滚动停止
                    if(lastPosition == singleCompanyPositionsAdapter.getItemCount()-1){//最后一个数据出现
                        if(singleCompanyPositionsAdapter.isFootViewVisible()){//footer可视

                            if(type == 0){//不带筛选请求
                                retrofitService.getSingleCompanyPositionsJsonString(page,size+=10,companyId,null,selectCity,selectMinSalary,selectMaxSalary,selectExperience)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .safeSubscribe(observer);
                            }else{//带筛选请求
                                retrofitService.getSingleCompanyPositionsJsonString(page,size+=10,companyId,labels[type],selectCity,selectMinSalary,selectMaxSalary,selectExperience)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .safeSubscribe(observer);
                            }

                        }
                    }
                }
            }

            @Override
            public void onScrolled(final RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastPosition = linearLayoutManager.findLastVisibleItemPosition();
                //上划
//                if(dy>0){
//                    //没到底
//                    if (headZoomScrollView.getChildAt(0)  != null && headZoomScrollView.getChildAt(0) .getMeasuredHeight() > headZoomScrollView.getScrollY() + headZoomScrollView.getHeight()) {
//                        delY += dy;
//                      //上弹
//                        synchronized (Object.class){
//                            headZoomScrollView.post(new Runnable() {
//                                @Override
//                                public void run() {//将滚动量，传递给外部ScrollView
//                                    recyclerView.setNestedScrollingEnabled(false);//禁止recyclerView滚动
//                                    headZoomScrollView.scrollBy(0,delY);//scroll View滚动
//                                    recyclerView.setNestedScrollingEnabled(true);//recycler View恢复滚动
//                                }
//
//                            });
//                        }
//                    }else {//其他情况
//                        delY = 0;
//                        recyclerView.setNestedScrollingEnabled(true);
//                    }
//                }
            }
        });
        //recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setFocusable(false);//开始时，不会展示到recycler View的位置

        //异步处理网络请求
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what == 0){
                    retrofitService.getSingleCompanyPositionsJsonString(page,size+=10,companyId,null,selectCity,selectMinSalary,selectMaxSalary,selectExperience)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .safeSubscribe(observer);
                }else{
                    retrofitService.getSingleCompanyPositionsJsonString(page,size+=10,companyId,labels[msg.what],selectCity,selectMinSalary,selectMaxSalary,selectExperience)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .safeSubscribe(observer);
                }
            }
        };




        //adapter设置Item点击事件
        singleCompanyPositionsAdapter.setOnItemClickListener(new SingleCompanyPositionsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {

                //参数传递，页面跳转
//                bundle.putInt("position_id", singleCompanyPositionsAdapter.getPositionId(position));
//                positionShowFragment = new PositionShowFragment();
//                positionShowFragment.setArguments(bundle);
//                changeFragment(positionShowFragment,"positionShowFragment");
                Intent intent= new Intent();
                intent.putExtra("position_id",singleCompanyPositionsAdapter.getPositionId(position));
                intent.putExtra("company_id",companyId);
                intent.setClass(getActivity(), PositionShowActivity.class);
                startActivity(intent);
            }
        });

        return mView;
    }
    //跳转Fragment
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

    //点击事件处理
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //标签交换原理
            case R.id.label_all:
                if(type != 0){
                    setClickLabel(labelAll);
                    changeLabel(type);
                    type = 0;
                    size = 10;
                    handler.sendEmptyMessage(type);
                }
                break;
            case R.id.label_technology:
                if(type != 1){
                    setClickLabel(labelTechnology);
                    changeLabel(type);
                    type = 1;
                    size = 10;
                    handler.sendEmptyMessage(type);
                }
                break;
            case R.id.label_product:
                if(type != 2){
                    setClickLabel(labelProduct);
                    changeLabel(type);
                    type = 2;
                    size = 10;
                    handler.sendEmptyMessage(type);
                }
                break;
            case R.id.label_design:
                if(type != 3){
                    setClickLabel(labelDesign);
                    changeLabel(type);
                    type = 3;
                    size = 10;
                    handler.sendEmptyMessage(type);
                }
                break;
            case R.id.label_operating:
                if(type != 4){
                    setClickLabel(labelOperating);
                    changeLabel(type);
                    type = 4;
                    size = 10;
                    handler.sendEmptyMessage(type);
                }

                break;
            case R.id.label_market:
                if(type != 5){
                    setClickLabel(labelMarket);
                    changeLabel(type);
                    type = 5;
                    size = 10;
                    handler.sendEmptyMessage(type);
                }
                break;
            case R.id.label_sale:
                if(type != 6){
                    setClickLabel(labelSale);
                    changeLabel(type);
                    type = 6;
                    size = 10;
                    handler.sendEmptyMessage(type);
                }
                break;
            case R.id.label_function:
                if(type != 7){
                    setClickLabel(labelFunction);
                    changeLabel(type);
                    type = 7;
                    size = 10;
                    handler.sendEmptyMessage(type);
                }
                break;
            case R.id.label_others:
                if(type != 8){
                    setClickLabel(labelOthers);
                    changeLabel(type);
                    type = 8;
                    size = 10;
                    handler.sendEmptyMessage(type);
                }
                break;
            case R.id.label_school:
                if(type != 9){
                    setClickLabel(labelSchool);
                    changeLabel(type);
                    type = 9;
                    size = 10;
                    handler.sendEmptyMessage(type);
                }
                break;

            //筛选点击
            case R.id. city_parent_view:
                if(!citySelectFlag){//第一次点击
                    headZoomScrollViewFocusDown();
                    citySelect.setTextColor(getResources().getColor(R.color.colorGreen));
                    citySelectImage.setImageDrawable(getResources().getDrawable(R.drawable.icon_arrow_up));
                    citySelectFlag = !citySelectFlag;

                    experienceSelect.setTextColor(Color.parseColor("#333333"));
                    experienceSelectImage.setImageDrawable(getResources().getDrawable(R.drawable.icon_arrow_down));
                    if(experienceSelectFlag){
                        experienceSelectFlag = !experienceSelectFlag;
                    }

                    salarySelect.setTextColor(Color.parseColor("#333333"));
                    salarySelectImage.setImageDrawable(getResources().getDrawable(R.drawable.icon_arrow_down));
                    if(salarySelectFlag){
                        salarySelectFlag = !salarySelectFlag;
                    }

                    cityPickerSetAndShow();
                }else{
                    citySelect.setTextColor(Color.parseColor("#333333"));
                    citySelectImage.setImageDrawable(getResources().getDrawable(R.drawable.icon_arrow_down));
                    citySelectFlag = !citySelectFlag;
                }
                break;
            case R.id.experience_parent_view:
                if(!experienceSelectFlag){//第一次点击
                    headZoomScrollViewFocusDown();
                    experienceSelect.setTextColor(getResources().getColor(R.color.colorGreen));
                    experienceSelectImage.setImageDrawable(getResources().getDrawable(R.drawable.icon_arrow_up));
                    experienceSelectFlag = !experienceSelectFlag;

                    citySelect.setTextColor(Color.parseColor("#333333"));
                    citySelectImage.setImageDrawable(getResources().getDrawable(R.drawable.icon_arrow_down));
                    if(citySelectFlag){
                        citySelectFlag = !citySelectFlag;
                    }

                    salarySelect.setTextColor(Color.parseColor("#333333"));
                    salarySelectImage.setImageDrawable(getResources().getDrawable(R.drawable.icon_arrow_down));
                    if(salarySelectFlag){
                        salarySelectFlag = !salarySelectFlag;
                    }

                    experiencePickerSetAndShow();
                }else{
                    experienceSelect.setTextColor(Color.parseColor("#333333"));
                    experienceSelectImage.setImageDrawable(getResources().getDrawable(R.drawable.icon_arrow_down));
                    experienceSelectFlag = !experienceSelectFlag;
                }
                break;
            case R.id. salary_parent_view:
                if(!salarySelectFlag){//第一次点击
                    headZoomScrollViewFocusDown();
                    salarySelect.setTextColor(getResources().getColor(R.color.colorGreen));
                    salarySelectImage.setImageDrawable(getResources().getDrawable(R.drawable.icon_arrow_up));
                    salarySelectFlag = !salarySelectFlag;

                    citySelect.setTextColor(Color.parseColor("#333333"));
                    citySelectImage.setImageDrawable(getResources().getDrawable(R.drawable.icon_arrow_down));
                    if(citySelectFlag){
                        citySelectFlag = !citySelectFlag;
                    }

                    experienceSelect.setTextColor(Color.parseColor("#333333"));
                    experienceSelectImage.setImageDrawable(getResources().getDrawable(R.drawable.icon_arrow_down));
                    if(experienceSelectFlag){
                        experienceSelectFlag = !experienceSelectFlag;
                    }

                    salaryPickerSetAndShow();
                }else{
                    salarySelect.setTextColor(Color.parseColor("#333333"));
                    salarySelectImage.setImageDrawable(getResources().getDrawable(R.drawable.icon_arrow_down));
                    salarySelectFlag = !salarySelectFlag;
                }
                break;
        }

    }
    //label点击效果
    private void setClickLabel(TextView textView){
        textView.setTextColor(Color.parseColor("#f6f6f6"));
        textView.setBackgroundColor(getResources().getColor(R.color.colorGreen));
    }
    //label失去点击效果
    private void changeLabel(int type){
        labelList.get(type).setTextColor(Color.parseColor("#666666"));
        labelList.get(type).setBackgroundColor(Color.parseColor("#ececec"));
    }

    //Scroll View 滚到底
    private void headZoomScrollViewFocusDown(){
        synchronized (Object.class){
            headZoomScrollView.post(new Runnable() {
                @Override
                public void run() {
                    headZoomScrollView.fullScroll(ScrollView.FOCUS_DOWN);
                }
            });
        }

    }

    //城市选择Pick View
    private void cityPickerSetAndShow(){
        final CityJsonParseTool tool = new CityJsonParseTool();
        tool.parseProvinceJson(tool.getJson("china.json",getActivity()));
        OptionsPickerView optionsPickerView2 = new OptionsPickerView.Builder(getActivity(), new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                size = 10;
                if(tool.getCityList().get(options1).get(options2).replace("市","").equals("不限")){
                    selectCity = null;
                }else{
                    selectCity = tool.getCityList().get(options1).get(options2).replace("市","");
                }
                handler.sendEmptyMessage(type);
                citySelect.setText(tool.getCityList().get(options1).get(options2).replace("市",""));

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
        optionsPickerView2.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(Object o) {
                citySelect.setTextColor(Color.parseColor("#333333"));
                citySelectImage.setImageDrawable(getResources().getDrawable(R.drawable.icon_arrow_down));
                citySelectFlag = !citySelectFlag;
            }
        });
        optionsPickerView2.show();
    }

    //工作经验pick View
    private void experiencePickerSetAndShow(){
        final ArrayList<String> arrayList1 = new ArrayList();
        arrayList1.add("经验不限");
        arrayList1.add("应届毕业生");
        arrayList1.add("1年以下");
        arrayList1.add("1-3年");
        arrayList1.add("3-5年");
        arrayList1.add("5-10年");
        arrayList1.add("10年及以上");
        arrayList1.add("不要求");
        OptionsPickerView optionsPickerView1 = new OptionsPickerView.Builder(getActivity(), new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                size = 10;
                if(options1 == 0){
                    selectExperience = null;
                }else if(options1 == 7){
                    selectExperience = "经验不限";
                }else{
                    selectExperience = arrayList1.get(options1);
                }
                handler.sendEmptyMessage(type);
                experienceSelect.setText(arrayList1.get(options1));
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

        optionsPickerView1.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(Object o) {
                experienceSelect.setTextColor(Color.parseColor("#333333"));
                experienceSelectImage.setImageDrawable(getResources().getDrawable(R.drawable.icon_arrow_down));
                experienceSelectFlag = !experienceSelectFlag;
            }
        });
        optionsPickerView1.setPicker(arrayList1);
        optionsPickerView1.show();
    }

    //工资选择pickView
    private void salaryPickerSetAndShow(){
        final ArrayList<String> arrayList1 = new ArrayList();
        arrayList1.add("薪资不限");
        arrayList1.add("2k以下");
        arrayList1.add("2k-5k");
        arrayList1.add("5k-10k");
        arrayList1.add("10k-15k");
        arrayList1.add("15k-25k");
        arrayList1.add("25k-50k");
        arrayList1.add("50k以上");
        OptionsPickerView optionsPickerView1 = new OptionsPickerView.Builder(getActivity(), new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                size = 10;
                switch (options1){
                    case 0:
                        selectMinSalary = null;
                        selectMaxSalary = null;
                        break;
                    case 1:
                        selectMinSalary = null;
                        selectMaxSalary = 2;
                        break;
                    case 2:
                        selectMinSalary = 2;
                        selectMaxSalary = 5;
                        break;
                    case 3:
                        selectMinSalary = 5;
                        selectMaxSalary = 10;
                        break;
                    case 4:
                        selectMinSalary = 10;
                        selectMaxSalary = 15;
                        break;
                    case 5:
                        selectMinSalary = 15;
                        selectMaxSalary = 25;
                        break;
                    case 6:
                        selectMinSalary = 25;
                        selectMaxSalary = 50;
                        break;
                    case 7:
                        selectMinSalary = 50;
                        selectMaxSalary = null;
                        break;


                }
                handler.sendEmptyMessage(type);
                salarySelect.setText(arrayList1.get(options1));
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

        optionsPickerView1.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(Object o) {
                salarySelect.setTextColor(Color.parseColor("#333333"));
                salarySelectImage.setImageDrawable(getResources().getDrawable(R.drawable.icon_arrow_down));
                salarySelectFlag = !salarySelectFlag;
            }
        });
        optionsPickerView1.setPicker(arrayList1);
        optionsPickerView1.show();
    }
}
