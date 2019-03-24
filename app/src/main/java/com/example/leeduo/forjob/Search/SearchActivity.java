package com.example.leeduo.forjob.Search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.leeduo.forjob.R;

/**
 * Created by LeeDuo on 2019/3/15.
 */

public class SearchActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        SearchFragment searchFragment = new SearchFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,searchFragment,"searchFragment").commit();
    }
}
