package com.example.leeduo.forjob.Search;

import android.content.Context;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.lib.WheelView;
import com.bigkoo.pickerview.listener.OnDismissListener;
import com.example.leeduo.forjob.Adapters.RecommendPositionAdapter;
import com.example.leeduo.forjob.Adapters.SearchPositionAdapter;
import com.example.leeduo.forjob.companyShow.CompanyShowActivity;
import com.example.leeduo.forjob.ConfigArgs;
import com.example.leeduo.forjob.JsonBean.JsonCompaniesBean;
import com.example.leeduo.forjob.JsonBean.JsonRecommendHit;
import com.example.leeduo.forjob.PositionShow.PositionShowFragment;
import com.example.leeduo.forjob.R;
import com.example.leeduo.forjob.RetrofitService;
import com.example.leeduo.forjob.Tools.CityJsonParseTool;
import com.squareup.picasso.Picasso;

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
 * Created by LeeDuo on 2019/3/15.
 */

public class SearchFragment extends Fragment implements View.OnClickListener{
    private View mView;
    private LinearLayout citySelect;
    private EditText searchEdit;
    private ImageView editClear;
    private LinearLayout cancelBack;
    private RecyclerView searchListView;
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private SearchPositionAdapter searchPositionAdapter ;
    private Retrofit retrofit;
    private String cityString;
    private Observer<JsonRecommendHit[]> observer;
    private Bundle bundle;
    private String keyWord;
    private int size;
    private boolean requestFlag;
    private ImageView companyLogo;
    private TextView companyShortName;
    private TextView companyFullName;
    private TextView cityText;
    private TextView positionNumber;
    private TextView interviewText;
    private LinearLayout companyParent;
    private Handler handler;
    private final int COMPANY_REQUEST = 1;
    private final int POSITION_REQUEST = 2;
    private int companyId;
    private TextView citySelectText;
    private ImageView citySelectImage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_search,container,false);

        companyLogo = mView.findViewById(R.id.company_logo);
        companyShortName = mView.findViewById(R.id.company_short_name);
        companyFullName = mView.findViewById(R.id.company_full_name);
        cityText = mView.findViewById(R.id.company_city);
        positionNumber = mView.findViewById(R.id.company_position_number);
        interviewText = mView.findViewById(R.id.company_interview);
        companyParent = mView.findViewById(R.id.company_parent);
        companyParent.setOnClickListener(this);

        citySelectText = mView.findViewById(R.id.city_select_text);
        citySelectImage = mView.findViewById(R.id.city_select_image);

        citySelect = mView.findViewById(R.id.city_select);
        searchEdit = mView.findViewById(R.id.search_edit);
        searchEdit.setFocusable(true);
        editClear = mView.findViewById(R.id.edit_clear);
        cancelBack = mView.findViewById(R.id.cancel_back);
        searchListView = mView.findViewById(R.id.search_list);

        citySelect.setOnClickListener(this);
        editClear.setOnClickListener(this);
        cancelBack.setOnClickListener(this);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        dividerItemDecoration = new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getActivity().getDrawable(R.drawable.company_list_item_divider2));
        searchListView.setLayoutManager(linearLayoutManager);
        searchListView.addItemDecoration(dividerItemDecoration);
        searchPositionAdapter = new SearchPositionAdapter(getActivity(),new JsonRecommendHit[]{});
        searchListView.setAdapter(searchPositionAdapter);

        cityString = "北京";
        size = 10;
        requestFlag = true;

        retrofit = new Retrofit.Builder()
                .baseUrl(ConfigArgs.SERVER_WEB)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        observer = new Observer<JsonRecommendHit[]>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull JsonRecommendHit[] jsonRecommendHits) {
                if(jsonRecommendHits.length < size)
                    requestFlag = false;
                searchPositionAdapter.setData(jsonRecommendHits);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch(msg.what){
                    case COMPANY_REQUEST:
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                synchronized (searchEdit){
                                    retrofit.create(RetrofitService.class)
                                            .getCompaniesJsonString(1,1,keyWord,null,null,null,null)
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribeOn(Schedulers.io())
                                            .safeSubscribe(new Observer<JsonCompaniesBean>() {
                                                @Override
                                                public void onSubscribe(@NonNull Disposable d) {

                                                }

                                                @Override
                                                public void onNext(@NonNull JsonCompaniesBean jsonCompaniesBean) {
                                                    companyId = jsonCompaniesBean.getContent()[0].getCompanyId();
                                                    companyParent.setVisibility(View.VISIBLE);
                                                    Picasso.with(getActivity())
                                                            .load("http://"+jsonCompaniesBean.getContent()[0].getCompanyLogo().toString())
                                                            .placeholder(R.drawable.upload_pic_loadding)
                                                            .error(R.drawable.upload_pic_fail)
                                                            .into(companyLogo);
                                                    companyShortName.setText(jsonCompaniesBean.getContent()[0].getCompanyShortName());
                                                    companyFullName.setText(jsonCompaniesBean.getContent()[0].getCompanyFullName());
                                                    cityText.setText(jsonCompaniesBean.getContent()[0].getCity());
                                                    positionNumber.setText(jsonCompaniesBean.getContent()[0].getPositionNumber()+"个在招职位");
                                                    interviewText.setText("面试评分:"+jsonCompaniesBean.getContent()[0].getInterviewRemarkNum());
                                                    searchPositionAdapter.setData(new JsonRecommendHit[]{});
                                                }

                                                @Override
                                                public void onError(@NonNull Throwable e) {
                                                    companyParent.setVisibility(View.GONE);
                                                    Log.d(TAG, "onError: _______________"+e.getMessage());

                                                }

                                                @Override
                                                public void onComplete() {

                                                }
                                            });
                                }
                            }
                        }).start();

                        break;
                    case POSITION_REQUEST:
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                synchronized (searchEdit){
                                    retrofit.create(RetrofitService.class)
                                            .getRecommendPositions(keyWord,cityString,size)
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribeOn(Schedulers.io())
                                            .safeSubscribe(observer);
                                }
                            }
                        }).start();
                        break;
                }
            }
        };

        searchPositionAdapter.setOnItemTouchListener(new RecommendPositionAdapter.OnItemTouchListener() {
            @Override
            public void OnItemTouchListener(int position, View view, MotionEvent event) {
                switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        view.setBackgroundColor(Color.parseColor("#ececec"));
                        break;
                    case MotionEvent.ACTION_UP:
                        view.setBackgroundColor(Color.parseColor("#f6f6f6"));
                        int positionId = Integer.valueOf(searchPositionAdapter.getJsonRecommendHits()[position].get_source().getJob_id());
                        int companyId = Integer.valueOf(searchPositionAdapter.getJsonRecommendHits()[position].get_source().getCompany_id());
                        bundle = new Bundle();
                        bundle.putInt("position_id",positionId);
                        bundle.putInt("company_id",companyId);
                        bundle.putString("hide_fragment","positionShowFragment"+positionId);
                        PositionShowFragment positionShowFragment = new PositionShowFragment();
                        positionShowFragment.setArguments(bundle);
                        changeFragment(positionShowFragment,"positionShowFragment"+positionId,"searchFragment");
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        view.setBackgroundColor(Color.parseColor("#f6f6f6"));
                        break;
                }
            }
        });

        searchListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastPosition = 0;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == RecyclerView.SCROLL_STATE_IDLE){
                    if(lastPosition == searchPositionAdapter.getItemCount() - 1  && requestFlag){
                        handler.sendMessage(handler.obtainMessage(COMPANY_REQUEST));
                        synchronized (searchEdit){
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    retrofit.create(RetrofitService.class).getRecommendPositions(keyWord,cityString,size+=10)
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribeOn(Schedulers.io())
                                            .safeSubscribe(observer);
                                }
                            }).start();
                        }
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastPosition = linearLayoutManager.findLastVisibleItemPosition();
            }
        });


        searchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    //doSomething
                    hideKeyBoard(searchEdit);
                    keyWord = searchEdit.getText().toString();
                    size = 10;
                    if(!keyWord.equals("")){
                        handler.sendMessage(handler.obtainMessage(COMPANY_REQUEST));
                        handler.sendMessage(handler.obtainMessage(POSITION_REQUEST));
                    }
                    searchListView.post(new Runnable() {
                        @Override
                        public void run() {
                            searchListView.smoothScrollToPosition(0);
                        }
                    });
                    return true;
                }
                return false;
            }
        });

        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals(""))
                    editClear.setVisibility(View.GONE);
                else
                    editClear.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mView.postDelayed(new Runnable() {
            @Override
            public void run() {
                showKeyBoard(searchEdit);
            }
        },500);

        return mView;
    }
    //跳转Fragment
    private void changeFragment(Fragment fragment,String fragmentTag,String hideFragmentTag){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_right_in,0,0,R.anim.slide_right_out);
        if(!fragmentManager.getFragments().contains(fragment)){
            fragmentTransaction.add(R.id.fragment_container,fragment,fragmentTag);
        }
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.hide(getActivity().getSupportFragmentManager().findFragmentByTag(hideFragmentTag));
        fragmentTransaction.commit();
    }


    private void showKeyBoard(View editText){
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if(inputMethodManager != null)
            inputMethodManager.showSoftInput(editText,0);
    }

    private void hideKeyBoard(View editText){
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if(inputMethodManager != null)
            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(),0);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.city_select:
                hideKeyBoard(searchEdit);
                cityPickerSetAndShow();
                break;
            case R.id.edit_clear:
                searchEdit.setText("");
                break;
            case R.id.cancel_back:
                getActivity().finish();
                break;
            case R.id.company_parent:
                Intent intent = new Intent();
                intent.setClass(getActivity(),CompanyShowActivity.class);
                intent.putExtra("company_id",companyId);
                startActivity(intent);
                break;
        }
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
                //去掉市
                if(tool.getCityList().get(options1).get(options2).replace("市","").equals("不限")){//选择不限时
                    cityString = "北京";
                    citySelectText.setText(cityString);
                }else{
                    cityString = tool.getCityList().get(options1).get(options2).replace("市","");
                    citySelectText.setText(cityString);
                }

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
            }
        });
        optionsPickerView2.show();//显示
    }
}
