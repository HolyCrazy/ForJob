package com.example.leeduo.forjob.MainFragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.lib.WheelView;
import com.bigkoo.pickerview.listener.OnDismissListener;
import com.example.leeduo.forjob.Adapters.CompaniesListAdapter;
import com.example.leeduo.forjob.companyShow.CompanyShowActivity;
import com.example.leeduo.forjob.ConfigArgs;
import com.example.leeduo.forjob.JsonBean.JsonCompaniesBean;
import com.example.leeduo.forjob.R;
import com.example.leeduo.forjob.RetrofitService;
import com.example.leeduo.forjob.Tools.CityJsonParseTool;
import com.example.leeduo.forjob.Tools.DialogShowTool;

import java.util.ArrayList;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by LeeDuo on 2019/1/26.
 */
//点击第二个导航栏图标展示的页面
public class SecondFragment extends Fragment implements View.OnClickListener{
    private View mView;
    private RecyclerView companyRecyclerView;
    private CompaniesListAdapter companiesListAdapter;
    private DividerItemDecoration dividerItemDecoration;
    private LinearLayoutManager linearLayoutManager;
    private int size = 10;//初始化请求数据10条
    private int page =1;
    private JsonCompaniesBean jsonCompaniesBean;
    private Retrofit retrofit;
    private Intent intent;
    private RetrofitService retrofitService;
    private Observer<JsonCompaniesBean> observer;
    private DialogShowTool dialogShowTool;
    private LinearLayout cityParentView,selectParentView,sortParentView;
    private TextView cityText,selectText,sortText;
    private ImageView cityImage,selectImage,sortImage;
    private boolean cityClickFlag,selectClickFlag,sortClickFlag;
    private String city,financeStage,companySize,industryField;
    private Handler handler;
    private ArrayList<TextView> arrayListFinance,arrayListSize,arrayListIndustry;
    private int financeType,sizeType,industryType;
    private PopupWindow popupWindow;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_two,container,false);
        //加载中对话框
        dialogShowTool = new DialogShowTool(getActivity());
        dialogShowTool.show();
        //recyclerView相关初始化
        companyRecyclerView = mView.findViewById(R.id.company_list);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        companyRecyclerView.setLayoutManager(linearLayoutManager);
        dividerItemDecoration = new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getActivity().getDrawable(R.drawable.companies_list_item_divider));
        companyRecyclerView.addItemDecoration(dividerItemDecoration);

        //为适配器设置默认空白对象
        jsonCompaniesBean = new JsonCompaniesBean();
        companiesListAdapter = new CompaniesListAdapter(jsonCompaniesBean,getActivity());
        companyRecyclerView.setAdapter(companiesListAdapter);

        //网络请求参数初始化
        city = null;//城市
        financeStage = null;//融资规模
        companySize = null;//公司规模
        industryField = null;//产业领域

        //筛选参数初始化
        financeType = 8;//融资类型
        sizeType = 6;//公司规模类型
        industryType = 0;//产业类型
        //开启新线程进行网络请求
        new Thread(new Runnable() {
            @Override
            public void run() {

                retrofit = new Retrofit.Builder()
                        .baseUrl(ConfigArgs.SERVER_WEB)
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .build();

                retrofitService = retrofit.create(RetrofitService.class);
                //异步回调
                observer = new Observer<JsonCompaniesBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull JsonCompaniesBean jsonCompaniesBean) {//请求成功时
                        //每次多请求十条数据，若返回数据小于请求总数，说明数据库达所有数据已经被请求
                        if(jsonCompaniesBean.getContent().length < size){
                            //设置加载栏不可见
                            companiesListAdapter.setFooterViewIsVisible(false);
                        }else{
                            //设置可见，继续请求
                            companiesListAdapter.setFooterViewIsVisible(true);
                        }
                        //为适配器设置数据
                        companiesListAdapter.setData(jsonCompaniesBean);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        //发生错误，设置空数据
                        companiesListAdapter.setData(jsonCompaniesBean);
                        //加载中对话框消失
                        dialogShowTool.dismiss();
                    }

                    @Override
                    public void onComplete() {//请求完成，无论成功失败，都将调用
                        dialogShowTool.dismiss();
                    }
                };
                //开始请求
                retrofitService.getCompaniesJsonString(page,size,null,city,financeStage,companySize,industryField)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .safeSubscribe(observer);
            }
        }).start();

        //为适配器设置Item点击事件
        companiesListAdapter.setOnItemTouchListener(new CompaniesListAdapter.OnItemTouchListener() {
            @Override
            public void OnItemTouchListener(int position,View view,MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        view.setBackgroundColor(Color.parseColor("#33f6f6f6"));
                        break;
                    case MotionEvent.ACTION_UP://点击成功，查看公司信息
                        view.setBackgroundColor(Color.parseColor("#ffffff"));
                        intent = new Intent(getActivity(),CompanyShowActivity.class);//跳转公司展示页面
                        intent.putExtra("company_id",companiesListAdapter.getCompanyId(position));//传递参数公司的ID
                        startActivity(intent);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        view.setBackgroundColor(Color.parseColor("#ffffff"));
                        break;
                }

            }
        });

        //为recyclerView设置滑动监听
        companyRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int lastPosition = 0;//上一次滑动的位置
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == RecyclerView.SCROLL_STATE_IDLE){//当滚动停止时
                    if(lastPosition == companiesListAdapter.getItemCount()-1){//最后一条数据是否出现
                        if(companiesListAdapter.isFooterViewIsVisible()){//底部加载栏是为可见，可见-》请求数据
                            //请求数据，数据size数量加10
                            retrofitService.getCompaniesJsonString(page,size+=10,null,city,financeStage,companySize,industryField)
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribeOn(Schedulers.io())
                                    .safeSubscribe(observer);
                        }
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //滚动监听，获取当前底部最后一个可见的item
                lastPosition = linearLayoutManager.findLastVisibleItemPosition();
            }
        });


        //筛选相关容器
        cityParentView = mView.findViewById(R.id.city_parent_view);//城市
        cityParentView.setOnClickListener(this);
        selectParentView = mView.findViewById(R.id.select_parent_view);//筛选
        selectParentView.setOnClickListener(this);
        sortParentView = mView.findViewById(R.id.sort_parent_view);//排序
        sortParentView.setOnClickListener(this);

        //筛选相关文字
        cityText = mView.findViewById(R.id.city_text);
        selectText = mView.findViewById(R.id.select_text);
        sortText = mView.findViewById(R.id.sort_text);

        //筛选相关图片
        cityImage = mView.findViewById(R.id.city_image);
        selectImage = mView.findViewById(R.id.select_image);
        sortImage = mView.findViewById(R.id.sort_image);

        //筛选相关标志位，实现双击
        cityClickFlag = false;
        selectClickFlag = false;
        sortClickFlag = false;

        //异步处理筛选后的网络请求
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                retrofitService.getCompaniesJsonString(page,size,null,city,financeStage,companySize,industryField)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .safeSubscribe(observer);
            }
        };


        return mView;
    }

    //点击事件处理
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //容器点击事件，第一次点击弹出，第二次点击消失，三个只能有一个处于选择状态
            case R.id.city_parent_view:
                if(!cityClickFlag){
                    cityText.setTextColor(getResources().getColor(R.color.colorGreen));
                    cityImage.setImageDrawable(getResources().getDrawable(R.drawable.icon_arrow_up));
                    cityClickFlag = !cityClickFlag;
                    //其他的容器还原
                    selectText.setTextColor(Color.parseColor("#333333"));
                    selectImage.setImageDrawable(getResources().getDrawable(R.drawable.icon_arrow_down));
                    if(selectClickFlag){
                        selectClickFlag = !selectClickFlag;
                    }

                    sortText.setTextColor(Color.parseColor("#333333"));
                    sortImage.setImageDrawable(getResources().getDrawable(R.drawable.icon_arrow_down));
                    if(sortClickFlag){
                        sortClickFlag = !sortClickFlag;
                    }
                    //显示城市选择View
                    cityPickerSetAndShow();
                }else{
                    cityText.setTextColor(Color.parseColor("#333333"));
                    cityImage.setImageDrawable(getResources().getDrawable(R.drawable.icon_arrow_down));
                    cityClickFlag = !cityClickFlag;
                }
                break;
            case R.id.select_parent_view:
                if(!selectClickFlag){
                    selectText.setTextColor(getResources().getColor(R.color.colorGreen));
                    selectImage.setImageDrawable(getResources().getDrawable(R.drawable.icon_arrow_up));
                    selectClickFlag = !selectClickFlag;

                    cityText.setTextColor(Color.parseColor("#333333"));
                    cityImage.setImageDrawable(getResources().getDrawable(R.drawable.icon_arrow_down));
                    if(cityClickFlag){
                        cityClickFlag = !cityClickFlag;
                    }

                    sortText.setTextColor(Color.parseColor("#333333"));
                    sortImage.setImageDrawable(getResources().getDrawable(R.drawable.icon_arrow_down));
                    if(sortClickFlag){
                        sortClickFlag = !sortClickFlag;
                    }
                    //显示筛选弹窗
                    selectPopWindowUp();
                }else{
                    selectText.setTextColor(Color.parseColor("#333333"));
                    selectImage.setImageDrawable(getResources().getDrawable(R.drawable.icon_arrow_down));
                    selectClickFlag = !selectClickFlag;
                }
                break;
            case R.id.sort_parent_view://排序，功能暂时没实现
                if(!sortClickFlag){
                    sortText.setTextColor(getResources().getColor(R.color.colorGreen));
                    sortImage.setImageDrawable(getResources().getDrawable(R.drawable.icon_arrow_up));
                    sortClickFlag = !sortClickFlag;

                    selectText.setTextColor(Color.parseColor("#333333"));
                    selectImage.setImageDrawable(getResources().getDrawable(R.drawable.icon_arrow_down));
                    if(selectClickFlag){
                        selectClickFlag = !selectClickFlag;
                    }

                    cityText.setTextColor(Color.parseColor("#333333"));
                    cityImage.setImageDrawable(getResources().getDrawable(R.drawable.icon_arrow_down));
                    if(cityClickFlag){
                        cityClickFlag = !cityClickFlag;
                    }
                }else{
                    sortText.setTextColor(Color.parseColor("#333333"));
                    sortImage.setImageDrawable(getResources().getDrawable(R.drawable.icon_arrow_down));
                    sortClickFlag = !sortClickFlag;
                }
                break;

            //筛选内部点击事件，点击效果原理为同组内部交换，每组内部标签只有一个处于选中状态，
            // labelF为与融资规模相关的标签
            case R.id.labelf1:
                labelfClick(0);
                break;
            case R.id.labelf2:
                labelfClick(1);
                break;
            case R.id.labelf3:
                labelfClick(2);
                break;
            case R.id.labelf4:
                labelfClick(3);
                break;
            case R.id.labelf5:
                labelfClick(4);
                break;
            case R.id.labelf6:
                labelfClick(5);
                break;
            case R.id.labelf7:
                labelfClick(6);
                break;
            case R.id.labelf8:
                labelfClick(7);
                break;
            case R.id.labelf9:
                labelfClick(8);
                break;
            /////////////////////////////////
            //labelS为与公司规模相关的标签
            case R.id.labels1:
                labelsClick(0);
                break;
            case R.id.labels2:
                labelsClick(1);
                break;
            case R.id.labels3:
                labelsClick(2);
                break;
            case R.id.labels4:
                labelsClick(3);
                break;
            case R.id.labels5:
                labelsClick(4);
                break;
            case R.id.labels6:
                labelsClick(5);
                break;
            case R.id.labels7:
                labelsClick(6);
                break;
            ////////////////////////////////
            //labelI为与公司产业相关的标签
            case R.id.labeli1:
                labeliClick(0);
                break;
            case R.id.labeli2:
                labeliClick(1);
                break;
            case R.id.labeli3:
                labeliClick(2);
                break;
            case R.id.labeli4:
                labeliClick(3);
                break;
            case R.id.labeli5:
                labeliClick(4);
                break;
            case R.id.labeli6:
                labeliClick(5);
                break;
            case R.id.labeli7:
                labeliClick(6);
                break;
            case R.id.labeli8:
                labeliClick(7);
                break;
            case R.id.labeli9:
                labeliClick(8);
                break;
            case R.id.labeli10:
                labeliClick(9);
                break;
            case R.id.labeli11:
                labeliClick(10);
                break;
            case R.id.labeli12:
                labeliClick(11);
                break;
            case R.id.labeli13:
                labeliClick(12);
                break;
            case R.id.labeli14:
                labeliClick(13);
                break;
            case R.id.labeli15:
                labeliClick(14);
                break;
            case R.id.labeli16:
                labeliClick(15);
                break;
            case R.id.labeli17:
                labeliClick(16);
                break;
            case R.id.labeli18:
                labeliClick(17);
                break;
            case R.id.labeli19:
                labeliClick(18);
                break;
            case R.id.labeli20:
                labeliClick(19);
                break;
            case R.id.labeli21:
                labeliClick(20);
                break;
            case R.id.labeli22:
                labeliClick(21);
                break;
            //筛选界面确定标签
            case R.id.select_it:
                if(financeType == 8){//默认不限
                    financeStage = null;
                }else{
                    financeStage = arrayListFinance.get(financeType).getText().toString();
                }
                if(sizeType == 6){//默认不限
                    companySize = null;
                }else{
                    companySize = arrayListSize.get(sizeType).getText().toString();
                }
                if(industryType == 0){//默认不限
                    industryField = null;
                }else{
                    industryField = arrayListIndustry.get(industryType).getText().toString();
                }
                size = 10;//请求数据数量初始化
                handler.sendEmptyMessage(0);//发送消息
                popupWindow.dismiss();//弹窗消失
                break;
        }
    }

    //F标签交换
    private void labelfClick(int financeType){
        arrayListFinance.get(this.financeType).setTextColor(Color.parseColor("#333333"));
        arrayListFinance.get(this.financeType).setBackground(getResources().getDrawable(R.drawable.unselect_text_background));
        this.financeType=financeType;
        arrayListFinance.get(financeType).setTextColor(getResources().getColor(R.color.colorGreen));
        arrayListFinance.get(financeType).setBackground(getResources().getDrawable(R.drawable.select_text_background));
    }
    //S标签交换
    private void labelsClick(int sizeType){
        arrayListSize.get(this.sizeType).setTextColor(Color.parseColor("#333333"));
        arrayListSize.get(this.sizeType).setBackground(getResources().getDrawable(R.drawable.unselect_text_background));
        this.sizeType=sizeType;
        arrayListSize.get(sizeType).setTextColor(getResources().getColor(R.color.colorGreen));
        arrayListSize.get(sizeType).setBackground(getResources().getDrawable(R.drawable.select_text_background));
    }
    //I标签交换
    private void labeliClick(int industryType){
        arrayListIndustry.get(this.industryType).setTextColor(Color.parseColor("#333333"));
        arrayListIndustry.get(this.industryType).setBackground(getResources().getDrawable(R.drawable.unselect_text_background));
        this.industryType=industryType;
        arrayListIndustry.get(industryType).setTextColor(getResources().getColor(R.color.colorGreen));
        arrayListIndustry.get(industryType).setBackground(getResources().getDrawable(R.drawable.select_text_background));
    }
    //城市选择pickerView
    private void cityPickerSetAndShow(){
        //城市JSON数据解析工具
        final CityJsonParseTool tool = new CityJsonParseTool();
        //解析
        tool.parseProvinceJson(tool.getJson("china.json",getActivity()));
        //pickerView
        OptionsPickerView optionsPickerView2 = new OptionsPickerView.Builder(getActivity(), new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //点击确认时执行
                size = 10;//数据初始化
                //去掉市
                if(tool.getCityList().get(options1).get(options2).replace("市","").equals("不限")){//选择不限时
                    city = null;
                }else{
                    city = tool.getCityList().get(options1).get(options2).replace("市","");
                }
                //发送消息
                handler.sendEmptyMessage(0);
                //UI显示
                cityText.setText(tool.getCityList().get(options1).get(options2).replace("市",""));

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
        optionsPickerView2.setPicker(tool.getProvinceBeanList(),tool.getCityList());//联动填充数据
        optionsPickerView2.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(Object o) {//pickerView消失时
                cityText.setTextColor(Color.parseColor("#333333"));
                cityImage.setImageDrawable(getResources().getDrawable(R.drawable.icon_arrow_down));
                cityClickFlag = !cityClickFlag;
            }
        });
        optionsPickerView2.show();//显示
    }

    //筛选弹窗
    private void selectPopWindowUp(){
        //加载视图
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.company_select_layout,null);
        if(popupWindow == null){
            //初始化
            popupWindow = new PopupWindow(contentView,ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT,true);
            //标签绑定设置点击事件
            bindViewAndSetListener(contentView);
        }
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setAnimationStyle(R.style.PopupAnimation);//动画
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {//弹窗消失时
                selectText.setTextColor(Color.parseColor("#333333"));
                selectImage.setImageDrawable(getResources().getDrawable(R.drawable.icon_arrow_down));
                selectClickFlag = !selectClickFlag;
            }
        });
        popupWindow.showAsDropDown(selectParentView);//弹窗显示

        //获取三组标签内容
        try {
            arrayListFinance.get(financeType).setTextColor(getResources().getColor(R.color.colorGreen));
            arrayListFinance.get(financeType).setBackground(getResources().getDrawable(R.drawable.select_text_background));
        }catch (NullPointerException e){}
        try {
            arrayListSize.get(sizeType).setTextColor(getResources().getColor(R.color.colorGreen));
            arrayListSize.get(sizeType).setBackground(getResources().getDrawable(R.drawable.select_text_background));
        }catch (NullPointerException e){}
        try {
            arrayListIndustry.get(industryType).setTextColor(getResources().getColor(R.color.colorGreen));
            arrayListIndustry.get(industryType).setBackground(getResources().getDrawable(R.drawable.select_text_background));
        }catch (NullPointerException e){}



    }
    //标签绑定
    private void bindViewAndSetListener(View contentView){
        TextView labelf1,labelf2,labelf3,labelf4,labelf5,labelf6,labelf7,labelf8,labelf9,
                labels1,labels2,labels3,labels4,labels5,labels6,labels7,
                labeli1,labeli2,labeli3,labeli4,labeli5,labeli6,labeli7,labeli8,labeli9,
                labeli10,labeli11,labeli12,labeli13,labeli14,labeli15,labeli16,labeli17,labeli18,
                labeli19,labeli20,labeli21,labeli22,
                selectIt;
        arrayListFinance = new ArrayList<>();
        arrayListSize = new ArrayList<>();
        arrayListIndustry = new ArrayList<>();

        labelf1 = contentView.findViewById(R.id.labelf1);
        labelf1.setOnClickListener(this);
        arrayListFinance.add(labelf1);
        labelf2 = contentView.findViewById(R.id.labelf2);
        labelf2.setOnClickListener(this);
        arrayListFinance.add(labelf2);
        labelf3 = contentView.findViewById(R.id.labelf3);
        labelf3.setOnClickListener(this);
        arrayListFinance.add(labelf3);
        labelf4 = contentView.findViewById(R.id.labelf4);
        labelf4.setOnClickListener(this);
        arrayListFinance.add(labelf4);
        labelf5 = contentView.findViewById(R.id.labelf5);
        labelf5.setOnClickListener(this);
        arrayListFinance.add(labelf5);
        labelf6 = contentView.findViewById(R.id.labelf6);
        labelf6.setOnClickListener(this);
        arrayListFinance.add(labelf6);
        labelf7 = contentView.findViewById(R.id.labelf7);
        labelf7.setOnClickListener(this);
        arrayListFinance.add(labelf7);
        labelf8 = contentView.findViewById(R.id.labelf8);
        labelf8.setOnClickListener(this);
        arrayListFinance.add(labelf8);
        labelf9 = contentView.findViewById(R.id.labelf9);
        labelf9.setOnClickListener(this);
        arrayListFinance.add(labelf9);
//////////////////////////////////////////////////////////////////
        labels1 = contentView.findViewById(R.id.labels1);
        labels1.setOnClickListener(this);
        arrayListSize.add(labels1);
        labels2 = contentView.findViewById(R.id.labels2);
        labels2.setOnClickListener(this);
        arrayListSize.add(labels2);
        labels3 = contentView.findViewById(R.id.labels3);
        labels3.setOnClickListener(this);
        arrayListSize.add(labels3);
        labels4 = contentView.findViewById(R.id.labels4);
        labels4.setOnClickListener(this);
        arrayListSize.add(labels4);
        labels5 = contentView.findViewById(R.id.labels5);
        labels5.setOnClickListener(this);
        arrayListSize.add(labels5);
        labels6 = contentView.findViewById(R.id.labels6);
        labels6.setOnClickListener(this);
        arrayListSize.add(labels6);
        labels7 = contentView.findViewById(R.id.labels7);
        labels7.setOnClickListener(this);
        arrayListSize.add(labels7);
////////////////////////////////////////////////////////////////////
        labeli1 = contentView.findViewById(R.id.labeli1);
        labeli1.setOnClickListener(this);
        arrayListIndustry.add(labeli1);
        labeli2 = contentView.findViewById(R.id.labeli2);
        labeli2.setOnClickListener(this);
        arrayListIndustry.add(labeli2);
        labeli3 = contentView.findViewById(R.id.labeli3);
        labeli3.setOnClickListener(this);
        arrayListIndustry.add(labeli3);
        labeli4 = contentView.findViewById(R.id.labeli4);
        labeli4.setOnClickListener(this);
        arrayListIndustry.add(labeli4);
        labeli5 = contentView.findViewById(R.id.labeli5);
        labeli5.setOnClickListener(this);
        arrayListIndustry.add(labeli5);
        labeli6 = contentView.findViewById(R.id.labeli6);
        labeli6.setOnClickListener(this);
        arrayListIndustry.add(labeli6);
        labeli7 = contentView.findViewById(R.id.labeli7);
        labeli7.setOnClickListener(this);
        arrayListIndustry.add(labeli7);
        labeli8 = contentView.findViewById(R.id.labeli8);
        labeli8.setOnClickListener(this);
        arrayListIndustry.add(labeli8);
        labeli9 = contentView.findViewById(R.id.labeli9);
        labeli9.setOnClickListener(this);
        arrayListIndustry.add(labeli9);
        labeli10 = contentView.findViewById(R.id.labeli10);
        labeli10.setOnClickListener(this);
        arrayListIndustry.add(labeli10);
        labeli11 = contentView.findViewById(R.id.labeli11);
        labeli11.setOnClickListener(this);
        arrayListIndustry.add(labeli11);
        labeli12 = contentView.findViewById(R.id.labeli12);
        labeli12.setOnClickListener(this);
        arrayListIndustry.add(labeli12);
        labeli13 = contentView.findViewById(R.id.labeli13);
        labeli13.setOnClickListener(this);
        arrayListIndustry.add(labeli13);
        labeli14 = contentView.findViewById(R.id.labeli14);
        labeli14.setOnClickListener(this);
        arrayListIndustry.add(labeli14);
        labeli15 = contentView.findViewById(R.id.labeli15);
        labeli15.setOnClickListener(this);
        arrayListIndustry.add(labeli15);
        labeli16 = contentView.findViewById(R.id.labeli16);
        labeli16.setOnClickListener(this);
        arrayListIndustry.add(labeli16);
        labeli17 = contentView.findViewById(R.id.labeli17);
        labeli17.setOnClickListener(this);
        arrayListIndustry.add(labeli17);
        labeli18 = contentView.findViewById(R.id.labeli18);
        labeli18.setOnClickListener(this);
        arrayListIndustry.add(labeli18);
        labeli19 = contentView.findViewById(R.id.labeli19);
        labeli19.setOnClickListener(this);
        arrayListIndustry.add(labeli19);
        labeli20 = contentView.findViewById(R.id.labeli20);
        labeli20.setOnClickListener(this);
        arrayListIndustry.add(labeli20);
        labeli21 = contentView.findViewById(R.id.labeli21);
        labeli21.setOnClickListener(this);
        arrayListIndustry.add(labeli21);
        labeli22 = contentView.findViewById(R.id.labeli22);
        labeli22.setOnClickListener(this);
        arrayListIndustry.add(labeli22);

        selectIt = contentView.findViewById(R.id.select_it);
        selectIt.setOnClickListener(this);
    }

}
