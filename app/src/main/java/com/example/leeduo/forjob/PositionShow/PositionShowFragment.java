package com.example.leeduo.forjob.PositionShow;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.leeduo.forjob.Adapters.RecommendPositionAdapter;
import com.example.leeduo.forjob.ConfigArgs;
import com.example.leeduo.forjob.JsonBean.AnalyzerJsonBean.AnalyzerKeysJobBean;
import com.example.leeduo.forjob.JsonBean.JsonPositionBean;
import com.example.leeduo.forjob.JsonBean.JsonRecommendHit;
import com.example.leeduo.forjob.JsonBean.JsonSingleCompanyBean;
import com.example.leeduo.forjob.R;
import com.example.leeduo.forjob.RetrofitService;
import com.example.leeduo.forjob.Tools.DialogShowTool;
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
 * Created by LeeDuo on 2019/2/10.
 */

public class PositionShowFragment extends Fragment implements View.OnClickListener{
    private View mView;
    private ImageView companyLogo,backImage;
    private TextView title,positionName,positionLocation,positionExperience,positionDegree,
            positionType,positionSalary,positionAdvantage,positionDescription;
    private Bundle bundle;
    private int companyId,positionId;
    private Retrofit retrofit;
    private RetrofitService retrofitService;
    private String positionNameString;
    private NestedScrollView nestedScrollView;
    private DialogShowTool dialogShowTool;
    private LinearLayout parentContent;
    private RecyclerView recommendPositionList;
    private JsonRecommendHit[] jsonRecommendHits;
    private RecommendPositionAdapter recommendPositionAdapter;
    private String position,city;
    private String hideFragment;
    private LinearLayout recommendParent;
    private int size =5;
    private TextView positionKeyWord;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_position_show,container,false);

        //加载对话框显示
        dialogShowTool = new DialogShowTool(getActivity());

        //视图绑定
        companyLogo = mView.findViewById(R.id.company_logo);
        title = mView.findViewById(R.id.title);
        positionName = mView.findViewById(R.id.position_name);
        positionLocation = mView.findViewById(R.id.position_location);
        positionExperience = mView.findViewById(R.id.position_experience);
        positionDegree = mView.findViewById(R.id.position_degree);
        positionType = mView.findViewById(R.id.position_type);
        positionSalary = mView.findViewById(R.id.position_salary);
        positionAdvantage = mView.findViewById(R.id.position_advantage);
        positionDescription = mView.findViewById(R.id.position_description);
        nestedScrollView = mView.findViewById(R.id.nested_scroll_view);
        backImage = mView.findViewById(R.id.back_image);
        parentContent = mView.findViewById(R.id.parent_content);
        recommendPositionList = mView.findViewById(R.id.recommend_position_list);
        recommendParent = mView.findViewById(R.id.recommend_parent);
        positionKeyWord = mView.findViewById(R.id.position_key_word);

        parentContent.setVisibility(View.INVISIBLE);
        recommendParent.setVisibility(View.GONE);
        dialogShowTool.show();

        recommendPositionList.setNestedScrollingEnabled(false);
        recommendPositionList.setFocusable(false);
        recommendPositionList.setLayoutManager(new LinearLayoutManager(getContext()));
        jsonRecommendHits = new JsonRecommendHit[0];
        recommendPositionAdapter = new RecommendPositionAdapter(getContext(),jsonRecommendHits);
        recommendPositionList.setAdapter(recommendPositionAdapter);



        //提取参数
        bundle  = getArguments();
        companyId = bundle.getInt("company_id",62);
        positionId = bundle.getInt("position_id",62);
        hideFragment = bundle.getString("hide_fragment","positionShowFragment");
        bundle = null;


        //设置网络请求
        retrofit = new Retrofit.Builder()
                .baseUrl(ConfigArgs.SERVER_WEB)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        retrofitService = retrofit.create(RetrofitService.class);

        //开启新线程进行网络请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                retrofitService.getSinglePositionJsonString(positionId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .safeSubscribe(new Observer<JsonPositionBean>() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {

                            }

                            @Override
                            public void onNext(@NonNull JsonPositionBean jsonPositionBean) {
                                //数据填充
                                positionNameString = jsonPositionBean.getContent().getTitle();
                                positionName.setText(positionNameString);
                                positionLocation.setText(jsonPositionBean.getContent().getCity());
                                positionExperience.setText(jsonPositionBean.getContent().getExperience());
                                positionDegree.setText(jsonPositionBean.getContent().getDegree());
                                positionType.setText(jsonPositionBean.getContent().getPositionType());
                                positionSalary.setText(jsonPositionBean.getContent().getMinSalary()+"k"+"-"+jsonPositionBean.getContent().getMaxSalary()+"k");
                                positionAdvantage.setText(jsonPositionBean.getContent().getAdvantage());
                                positionDescription.setText(jsonPositionBean.getContent().getDescription());

                                position = positionNameString;
                                city = jsonPositionBean.getContent().getCity();
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Thread.sleep(200);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        dialogShowTool.dismiss();
                                    }
                                }).start();
                                parentContent.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onComplete() {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Thread.sleep(200);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        dialogShowTool.dismiss();
                                    }
                                }).start();
                                parentContent.setVisibility(View.VISIBLE);

                            }
                        });
            }
        }).start();

        //图片请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                retrofitService.getSingleCompanyJsonString(companyId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .safeSubscribe(new Observer<JsonSingleCompanyBean>() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {

                            }

                            @Override
                            public void onNext(@NonNull JsonSingleCompanyBean jsonSingleCompanyBean) {
                                Picasso.with(getActivity()).
                                        load("http://"+jsonSingleCompanyBean.getContent().getCompanyLogo()).
                                        placeholder(R.drawable.upload_pic_loadding).
                                        error(R.drawable.upload_pic_fail).
                                        into(companyLogo);
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
                while (city == null && position == null){
                    if(city != null && position != null)
                        break;
                }
                retrofitService.getAnalyzerKeysJobBean(position,10)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .safeSubscribe(new Observer<AnalyzerKeysJobBean[]>() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {

                            }

                            @Override
                            public void onNext(@NonNull AnalyzerKeysJobBean[] analyzerKeysJobBeen) {
                                String s ="职位核心：";
                                for(AnalyzerKeysJobBean mAnalyzerKeysJobBean : analyzerKeysJobBeen){
                                    s+=mAnalyzerKeysJobBean.getKey()+" ";
                                }
                                positionKeyWord.setText(s);
                                s = null;
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
                retrofitService.getRecommendPositions(position,city,size)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .safeSubscribe(new Observer<JsonRecommendHit[]>() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {

                            }

                            @Override
                            public void onNext(@NonNull JsonRecommendHit[] jsonRecommendHits) {
                                Log.d(TAG, "onNext: _______________________");
                                recommendPositionAdapter.setData(jsonRecommendHits);
                                recommendParent.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                Log.d(TAG, "onError: ___________________"+e.getMessage());

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }
        }).start();


        recommendPositionAdapter.setOnItemTouchListener(new RecommendPositionAdapter.OnItemTouchListener() {
            @Override
            public void OnItemTouchListener(int position, View view, MotionEvent event) {
                switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        view.setBackgroundColor(Color.parseColor("#f0f0f0"));
                        break;
                    case MotionEvent.ACTION_UP:
                        view.setBackgroundColor(Color.parseColor("#33f6f6f6"));
                        int positionId = Integer.valueOf(recommendPositionAdapter.getJsonRecommendHits()[position].get_source().getJob_id());
                        int companyId = Integer.valueOf(recommendPositionAdapter.getJsonRecommendHits()[position].get_source().getCompany_id());
                        bundle = new Bundle();
                        bundle.putInt("position_id",positionId);
                        bundle.putInt("company_id",companyId);
                        bundle.putString("hide_fragment","positionShowFragment"+positionId);
                        PositionShowFragment positionShowFragment = new PositionShowFragment();
                        positionShowFragment.setArguments(bundle);
                        changeFragment(positionShowFragment,"positionShowFragment"+positionId,hideFragment);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        view.setBackgroundColor(Color.parseColor("#33f6f6f6"));
                        break;
                }
            }
        });



        //设置滑动监听
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(scrollY > 100 && positionNameString.getBytes().length<=33){
                    title.setText(positionNameString);
                }else{
                    title.setText("职位详情");
                }
            }
        });


        backImage.setOnClickListener(this);

        return mView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_image:
                if(getActivity().getSupportFragmentManager().getBackStackEntryCount() == 0)
                    getActivity().finish();
                else
                    getActivity().getSupportFragmentManager().popBackStack();

                break;
        }
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
}
