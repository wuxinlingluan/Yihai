package com.github.yihai.ui.out;

import android.os.Bundle;
import android.view.View;

import com.github.yihai.R;
import com.github.yihai.base.BaseNormalToolBarActivity;
import com.github.yihai.zebra.ZebarUtil;

/**
 * Created by dzl on 2017/12/12.
 */

public class StorageOutActivity extends BaseNormalToolBarActivity {
    OutPlanFragment mOutPlanFragment;
    OutShelvesFragment mOutShelvesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage_in);
        ZebarUtil.setAntenna(70);  //设置扫描强度
    }

    @Override
    protected String[] setToolBarTitle() {
        return new String[]{
                getString(R.string.title_make_plan),
                getString(R.string.title_scan_confirm)
        };
    }

    @Override
    public void initView() {
        mOutPlanFragment = OutPlanFragment.newInstance();
        mOutShelvesFragment = OutShelvesFragment.newInstance();
        mAction2Tv.setText(R.string.action_out);
        mAction2Tv.setVisibility(View.VISIBLE);
    }

    @Override
    public void initData() {

    }

    @Override
    public void bindEvent() {
        onTabSelect(0);
    }

    @Override
    public void onTabSelect(int position) {
        if (position == 0) {
            replaceFragmentToActivity(mOutPlanFragment, R.id.container);
        } else if (position == 1) {
            replaceFragmentToActivity(mOutShelvesFragment, R.id.container);
        }
    }

    @Override
    public void onTabReselect(int position) {

    }

}
