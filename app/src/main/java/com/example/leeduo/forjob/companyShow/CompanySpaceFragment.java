package com.example.leeduo.forjob.companyShow;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.HorizontalScrollView;

import com.example.leeduo.forjob.ConfigArgs;
import com.example.leeduo.forjob.HeadZoomScrollView;
import com.example.leeduo.forjob.JsonBean.AnalyzerJsonBean.AnalyzerJobCompanyBean;
import com.example.leeduo.forjob.JsonBean.AnalyzerJsonBean.AnalyzerKeysCompanyBean;
import com.example.leeduo.forjob.R;
import com.example.leeduo.forjob.RetrofitService;
import com.example.leeduo.forjob.Tools.JavaScripter;
import com.example.leeduo.forjob.Tools.ScreenTools;

import java.lang.ref.SoftReference;
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

public class CompanySpaceFragment extends Fragment {
    private View mView;
    private WebView jobWebView,wordWebView;
    private Retrofit retrofit;
    private int companyId;
    private JavaScripter javaScripter;
    private RetrofitService retrofitService;
    private NestedScrollView nestedScrollView;
    private HeadZoomScrollView headZoomScrollView;
    private int delY;
//    private double screenHeight;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_company_space,container,false);

//        screenHeight = getResources().getDimension(R.dimen.dp_570);
//        DisplayMetrics dm = getResources().getDisplayMetrics();
//        screenHeight = screenHeight / dm.density;

        companyId = getArguments().getInt("company_id",62);

        jobWebView = mView.findViewById(R.id.job_web_view);
        jobWebView.getSettings().setJavaScriptEnabled(true);
        jobWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        jobWebView.loadUrl("file:///android_asset/squareMap.html");
        jobWebView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        wordWebView = mView.findViewById(R.id.word_web_view);
        wordWebView.getSettings().setJavaScriptEnabled(true);
        wordWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wordWebView.loadUrl("file:///android_asset/wordCloudMap.html");
        wordWebView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        nestedScrollView = mView.findViewById(R.id.scroll_parent);


        retrofit = new Retrofit.Builder()
                .baseUrl(ConfigArgs.SERVER_WEB)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitService = retrofit.create(RetrofitService.class);
        javaScripter = new JavaScripter();

        new Thread(new Runnable() {
            @Override
            public void run() {
                retrofitService
                        .getAnalyzerJobCompanyBean(companyId,8)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .safeSubscribe(new Observer<AnalyzerJobCompanyBean[]>() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {

                            }

                            @Override
                            public void onNext(@NonNull AnalyzerJobCompanyBean[] analyzerJobCompanyBeen) {
                                ArrayList<String> xLabel = new ArrayList<String>();
                                ArrayList<Integer> yLabel = new ArrayList<Integer>();
                                for(int i=0;i<analyzerJobCompanyBeen.length;i++){
                                    xLabel.add(analyzerJobCompanyBeen[i].getKey());
                                    yLabel.add(analyzerJobCompanyBeen[i].getDoc_count());
                                }
                                JavaScripter.SquareMapWorker squareMapWorker = (JavaScripter.SquareMapWorker) javaScripter.setMapType(JavaScripter.Map.squareMap);
                                final String js = squareMapWorker.pushData(xLabel,yLabel).parse().create();
                                jobWebView.setWebViewClient(new WebViewClient(){
                                    @Override
                                    public void onPageFinished(WebView view, String url) {
                                        super.onPageFinished(view, url);
                                        jobWebView.evaluateJavascript("javascript:reset("+js+")",null);
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

        new Thread(new Runnable() {
            @Override
            public void run() {
                retrofitService.getAnalyzerKeysCompanyBean(companyId,20)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .safeSubscribe(new Observer<AnalyzerKeysCompanyBean[]>() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {

                            }

                            @Override
                            public void onNext(@NonNull AnalyzerKeysCompanyBean[] analyzerKeysCompanyBeen) {
                                ArrayList<String> words = new ArrayList<String>();
                                ArrayList<Integer> values = new ArrayList<Integer>();
                                for(int i=0;i<analyzerKeysCompanyBeen.length;i++){
                                    words.add(analyzerKeysCompanyBeen[i].getKey());
                                    values.add(analyzerKeysCompanyBeen[i].getDoc_count()*50);
                                }
                                JavaScripter.WordCloudMapWorker wordCloudMapWorker = (JavaScripter.WordCloudMapWorker) javaScripter.setMapType(JavaScripter.Map.wordCloudMap);
                                final String js = wordCloudMapWorker.pushData(words,values).parse().create();
                                wordWebView.setWebViewClient(new WebViewClient(){
                                    @Override
                                    public void onPageFinished(WebView view, String url) {
                                        super.onPageFinished(view, url);
                                        wordWebView.evaluateJavascript("javascript:reset("+js+")",null);
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

    //传入Scroll View
    public void setHeadZoomScrollView(HeadZoomScrollView headZoomScrollView) {
        this.headZoomScrollView = headZoomScrollView;
    }
}
