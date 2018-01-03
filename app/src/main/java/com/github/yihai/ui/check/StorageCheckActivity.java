package com.github.yihai.ui.check;

import android.os.Bundle;
import android.view.View;

import com.github.yihai.R;
import com.github.yihai.base.BaseNormalToolBarActivity;
import com.github.yihai.zebra.ZebarUtil;

/**
 * Created by dzl on 2017/12/12.
 */

public class StorageCheckActivity extends BaseNormalToolBarActivity {
    CheckPlanFragment mCheckPlanFragment;
    CheckShelvesFragment mCheckShelvesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage_in);
        ZebarUtil.setAntenna(270);  //设置扫描强度
    }

    @Override
    protected String[] setToolBarTitle() {
        return new String[]{
                getString(R.string.title_make_plan),
                getString(R.string.title_select_plan)
        };
    }

    @Override
    public void initView() {
        mCheckPlanFragment = CheckPlanFragment.newInstance();
        mCheckShelvesFragment = CheckShelvesFragment.newInstance();
        mAction2Tv.setText(R.string.action_check);
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
            replaceFragmentToActivity(mCheckPlanFragment, R.id.container);
        } else if (position == 1) {
            replaceFragmentToActivity(mCheckShelvesFragment, R.id.container);
        }
    }


    @Override
    public void onTabReselect(int position) {

    }
}
