package com.github.yihai.base;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.github.yihai.R;

import butterknife.BindView;

/**
 * 带有toolbar的activity基类
 * Created by dzl on 2017/5/26.
 */

public abstract class BaseNormalToolBarActivity extends BaseActivity implements OnTabSelectListener {

    //toolbar
    @BindView(R.id.custom_simple_bar)
    protected Toolbar mToolBar;
    @BindView(R.id.view_content)
    protected View mToolBarView;
    @BindView(R.id.custom_back)
    protected TextView mBackTv;
    @BindView(R.id.custom_action)
    protected TextView mActionTv;
    @BindView(R.id.custom_action2)
    protected TextView mAction2Tv;

    @BindView(R.id.tabs)
    protected SegmentTabLayout tabLayout;

    private boolean isToolBarEnable = true;

    @Override
    protected Toolbar initToolBar(View contentView) {
        if (isToolBarEnable) {
            mBackTv.setOnClickListener(toolbarClickListener);
            tabLayout.setTabData(setToolBarTitle());
            tabLayout.setOnTabSelectListener(this);
        }
        return mToolBar;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    protected abstract String[] setToolBarTitle();

    private View.OnClickListener toolbarClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.custom_back: {
                    BaseNormalToolBarActivity.this.finish();
                    break;
                }
            }
        }
    };

    public void setToolBarEnable(boolean toolBarEnable) {
        isToolBarEnable = toolBarEnable;
    }

}
