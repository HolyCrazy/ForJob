package com.example.leeduo.forjob.MainFragments;


import android.content.Intent;
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
import android.webkit.WebView;

import android.webkit.WebViewClient;
import android.widget.TextView;

import com.example.leeduo.forjob.Adapters.SingleCompanyPositionsAdapter;
import com.example.leeduo.forjob.ConfigArgs;
import com.example.leeduo.forjob.JsonBean.AnalyzerJsonBean.AnalyzerSalaryJobBean;
import com.example.leeduo.forjob.JsonBean.JsonPositionsBean;
import com.example.leeduo.forjob.PositionShow.PositionShowActivity;
import com.example.leeduo.forjob.R;
import com.example.leeduo.forjob.RetrofitService;
import com.example.leeduo.forjob.Search.SearchActivity;
import com.example.leeduo.forjob.Tools.JavaScripter;

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
//点击第一个导航栏图标展示的页面
public class FirstFragment extends Fragment implements View.OnClickListener{
    private View mView;
    private WebView webView;
    private TextView searchTextView;
    private Retrofit retrofit;
    private RetrofitService retrofitService;
    private RecyclerView hotPositionList;
    private JsonPositionsBean jsonPositionsBean;
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private SingleCompanyPositionsAdapter singleCompanyPositionsAdapter;
    private Handler handler;
    private Observer<JsonPositionsBean> observer;
    private int size;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_one,container,false);

        size = 10;

        webView = mView.findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.loadUrl("file:///android_asset/scatterMap.html");
        webView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        retrofit = new Retrofit.Builder()
                .baseUrl(ConfigArgs.SERVER_WEB)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        retrofitService = retrofit.create(RetrofitService.class);

        new Thread(new Runnable() {
            @Override
            public void run() {
                retrofitService.getAnalyzerSalaryJobBean("Android",10)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .safeSubscribe(new Observer<AnalyzerSalaryJobBean[]>() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {

                            }

                            @Override
                            public void onNext(@NonNull AnalyzerSalaryJobBean[] analyzerSalaryJobBeen) {
                                final ArrayList<JavaScripter.MapBean> mapBeanArrayList = new ArrayList<JavaScripter.MapBean>();
                                for(int i =0;i<analyzerSalaryJobBeen.length;i++){
                                    JavaScripter.MapBean mapBean = new JavaScripter.MapBean();
                                    mapBean.setCity(analyzerSalaryJobBeen[i].getCity().getName());
                                    mapBean.setLatitude(analyzerSalaryJobBeen[i].getCity().getLatitude());
                                    mapBean.setLongitude(analyzerSalaryJobBeen[i].getCity().getLongitude());
                                    mapBean.setValue((int) analyzerSalaryJobBeen[i].getAvg_price().getValue() *7);
                                    mapBeanArrayList.add(mapBean);

                                }

                                webView.setWebViewClient(new WebViewClient(){
                                    @Override
                                    public void onPageFinished(WebView view, String url) {
                                        super.onPageFinished(view, url);
                                        JavaScripter javaScripter = new JavaScripter();
                                        JavaScripter.ScatterMapWorker scatterMapWorker = (JavaScripter.ScatterMapWorker) javaScripter.setMapType(JavaScripter.Map.scatterMap);

                                        String s = scatterMapWorker.pushData(mapBeanArrayList).parse().create();
                                        webView.evaluateJavascript("javascript:reset("+s+")",null);
                                    }


                                });
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

            }

            @Override
            public void onComplete() {

            }
        };

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 0:
                        synchronized (Object.class){
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    retrofitService.getSingleCompanyPositionsJsonString(1,size,null,null,null,null,null,null)
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribeOn(Schedulers.io())
                                            .safeSubscribe(observer);

                                }
                            }).start();
                        }
                        break;
                }
            }
        };

        searchTextView = mView.findViewById(R.id.search_text);
        searchTextView.setOnClickListener(this);

        hotPositionList = mView.findViewById(R.id.hot_position_list);
        jsonPositionsBean = new JsonPositionsBean();
        linearLayoutManager = new LinearLayoutManager(getActivity());
        dividerItemDecoration = new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.single_company_positions_item_divider));
        hotPositionList.setLayoutManager(linearLayoutManager);
        hotPositionList.addItemDecoration(dividerItemDecoration);
        singleCompanyPositionsAdapter = new SingleCompanyPositionsAdapter(jsonPositionsBean,getContext());
        hotPositionList.setAdapter(singleCompanyPositionsAdapter);
        handler.sendEmptyMessage(0);

        hotPositionList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastPosition;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == RecyclerView.SCROLL_STATE_IDLE){
                    if(lastPosition == singleCompanyPositionsAdapter.getItemCount() - 1){
                        size +=10;
                        handler.sendEmptyMessage(0);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastPosition = linearLayoutManager.findLastVisibleItemPosition();
            }
        });

        singleCompanyPositionsAdapter.setOnItemClickListener(new SingleCompanyPositionsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                //参数传递，页面跳转
                Intent intent = new Intent();
                intent.putExtra("position_id", singleCompanyPositionsAdapter.getPositionId(position));
                intent.putExtra("company_id", singleCompanyPositionsAdapter.getCompanyId(position));
                intent.setClass(getActivity(), PositionShowActivity.class);
                startActivity(intent);
            }
        });






        return mView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search_text:
                Intent searchIntent = new Intent();
                searchIntent.setClass(getActivity(), SearchActivity.class);
                startActivity(searchIntent);
                break;
        }
    }
}
