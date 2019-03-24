package com.example.leeduo.forjob.PositionShow;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.leeduo.forjob.R;

/**
 * Created by LeeDuo on 2019/3/24.
 */

public class PositionShowActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_position_show);

        int positionId = getIntent().getIntExtra("position_id",62);
        int companyId = getIntent().getIntExtra("company_id",62);
        PositionShowFragment positionShowFragment = new PositionShowFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position_id",positionId);
        bundle.putInt("company_id",companyId);
        positionShowFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,positionShowFragment,"positionShowFragment").commit();

    }
}
