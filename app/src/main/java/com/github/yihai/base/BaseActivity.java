package com.github.yihai.base;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import butterknife.ButterKnife;

/**
 * Created by ${sheldon} on 2017/12/11.
 */

public abstract class BaseActivity extends AppCompatActivity {

    /**
     * 页面布局的 根view
     */
    protected View mContentView;
    /**
     * 来自页面的类名
     */
    protected String fromClassName;
    /**
     * 来自页面的标题名
     */
    protected String fromName;
    protected ProgressDialog mProgressDialog;

    /***************************视图基础控制***************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置不能横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        mProgressDialog = new ProgressDialog(this);
        //创建管理
        initManage();
    }

    protected void initManage() {
        //Activity管理
        ActivityPageManager.getInstance().addActivity(this);
    }

    @Override
    public void setContentView(int layoutResID) {
        View view = LayoutInflater.from(this).inflate(layoutResID, null);
        setContentView(view);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        ButterKnife.bind(this);
        mContentView = view;
        //初始化页面
        Toolbar toolbar = initToolBar(mContentView);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        initView();
        initData();
        bindEvent();
    }

    protected Toolbar initToolBar(View mContentView) {
        return null;
    }

    /**
     * 初始化view
     */
    public abstract void initView();

    /**
     * 初始化Data
     */
    public abstract void initData();

    /**
     * 绑定事件
     */
    public abstract void bindEvent();

    @Override
    protected void onStart() {
        super.onStart();

    }

    /**
     * 跳转页面
     *
     * @param clazz
     */
    public void skipAct(Class clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    /**
     * 跳转页面
     *
     * @param clazz
     */
    public void skipAct(Class clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * 将 Fragment添加到Acitvtiy
     *
     * @param fragment
     * @param frameId
     */
    protected void replaceFragmentToActivity(@NonNull Fragment fragment, int frameId) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(frameId, fragment);
        transaction.commit();
    }

    /**
     * 将 Fragment添加到Acitvtiy
     *
     * @param fragment
     * @param frameId
     */
    protected void addFragmentToActivity(@NonNull Fragment fragment, int frameId) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(frameId, fragment);
        transaction.commit();
    }

    protected void showFragmentToActivity(Fragment fromFragment, Fragment toFragment, int frameId) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (!toFragment.isAdded()) {
            transaction.hide(fromFragment).add(frameId, toFragment).commit();
        } else {
            transaction.hide(fromFragment).show(toFragment).commit();
        }
    }

    protected void showFragmentToActivity(Fragment fromFragment, Fragment toFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(fromFragment).show(toFragment).commit();
    }

    protected void removeFragmentFromActivity(Fragment removeFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (!removeFragment.isAdded()) {
            transaction.remove(removeFragment).commit();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mProgressDialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        //子activity使用onDestroy时，最后调用    super.onDestroy();
        super.onDestroy();
        //Acitvity 释放子view资源
        ActivityPageManager.unbindReferences(mContentView);
        ActivityPageManager.getInstance().removeActivity(this);
        mContentView = null;
//        //检测内存泄露
    /*    RefWatcher refWatcher = CustomApplication.getRefWatcher(this);
        if (refWatcher != null) {
            refWatcher.watch(this);
        }*/
    }

    protected void showProgress(String msg) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCanceledOnTouchOutside(false);
        }
        mProgressDialog.setMessage(msg);
        mProgressDialog.show();
    }

    protected void hideProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    protected void showToast(String msg) {
        Toast.makeText(BaseActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
}
