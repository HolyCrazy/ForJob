package com.example.leeduo.forjob.companyShow;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.leeduo.forjob.R;

/**
 * Created by LeeDuo on 2019/2/7.
 */
//公司信息展示界面
public class CompanyShowActivity extends AppCompatActivity {
    private CompanyShowFragment companyShowFragment;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_show);
        //获取传递来的company_id
        Intent intent = getIntent();
        //加载Fragment
        companyShowFragment = new CompanyShowFragment();
        //参数继续传递
        Bundle bundle = new Bundle();
        bundle.putInt("company_id",intent.getIntExtra("company_id",62));
        companyShowFragment.setArguments(bundle);
        //添加Fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container,companyShowFragment,"companyShowFragment");
        fragmentTransaction.commit();
    }
}
