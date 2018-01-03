package com.github.yihai.ui.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.yihai.R;
import com.github.yihai.base.BaseActivity;
import com.github.yihai.bean.GoodsFloorBean;
import com.github.yihai.bean.GoodsLocationBean;
import com.github.yihai.bean.GoodsShelfBean;
import com.github.yihai.common.util.AppUtils;
import com.github.yihai.common.util.LogUtils;
import com.github.yihai.manager.HttpUtil;
import com.github.yihai.ui.check.StorageCheckActivity;
import com.github.yihai.ui.goods.GoodsHolder;
import com.github.yihai.ui.in.StorageInActivity;
import com.github.yihai.ui.out.StorageOutActivity;
import com.github.yihai.ui.search.SearchActivity;
import com.github.yihai.zebra.ZebarHolder;
import com.google.gson.Gson;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.custom_simple_bar)
    Toolbar toolbar;
    @BindView(R.id.iv_in)
    ImageView ivIn;
    @BindView(R.id.iv_out)
    ImageView ivOut;
    @BindView(R.id.iv_check)
    ImageView ivCheck;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.custom_title)
    TextView tvTitle;
    @BindView(R.id.custom_action)
    TextView tvAction;

    private List<GoodsShelfBean> mGoodsShelfList;
    private List<GoodsFloorBean> mGoodsFloorList;
    private List<GoodsLocationBean> mGoodsLocationList;
    private long firstTime = 0;
    private int requestCount = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void initView() {
        setSupportActionBar(toolbar);
        tvTitle.setText(R.string.title_main);
    }


    @Override
    public void initData() {
        mGoodsShelfList = new ArrayList<>();
        mGoodsFloorList = new ArrayList<>();
        mGoodsLocationList = new ArrayList<>();
        showProgress(getString(R.string.hint_waiting));
        HttpUtil.getGoodsShelfList(goodsShelfCallback,1);
    }

    @Override
    public void bindEvent() {

    }


    @OnClick({R.id.custom_action, R.id.iv_in, R.id.iv_out, R.id.iv_check, R.id.iv_search, R.id.custom_back})
    public void onViewClicked(View view) {
        if (requestCount > 0) {
            return;
        }
        switch (view.getId()) {
            case R.id.custom_action: {
                new AlertDialog.Builder(this)
                        .setMessage(R.string.dialog_exit_msg)
                        .setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                AppUtils.exit(MainActivity.this.getApplicationContext());
                            }
                        })
                        .setNegativeButton(R.string.action_cancel, null)
                        .create().show();
                break;
            }
            case R.id.iv_in:
               if (showDisconnectHint()) return;
                skipAct(StorageInActivity.class);
                break;
            case R.id.iv_out:
                if (showDisconnectHint()) return;
                skipAct(StorageOutActivity.class);
                break;
            case R.id.iv_check:
          //       if (showDisconnectHint()) return;
                skipAct(StorageCheckActivity.class);
                break;
            case R.id.iv_search:
                skipAct(SearchActivity.class);
                break;
            case R.id.custom_back: {
                skipAct(BluetoothListActivity.class);
                break;
            }
        }
    }

    private boolean showDisconnectHint() {
        if (ZebarHolder.mConnectedReader == null || !ZebarHolder.mConnectedReader.isConnected()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("未连接到扫描设备,是否打开连接界面?").setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    skipAct(BluetoothListActivity.class);
                }
            });
            builder.setNegativeButton(R.string.action_cancel,null);
            builder.create().show();
            return true;
        }
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        long secondTime = System.currentTimeMillis();
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (secondTime - firstTime < 2000) {
                System.exit(0);
            } else {
                showToast("再按一次退出程序");
                firstTime = System.currentTimeMillis();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    StringCallback goodsShelfCallback = new StringCallback() {
        @Override
        public void onSuccess(Response<String> response) {
            String content = response.body().toString();
            LogUtils.d(content);
            try {
                JSONObject jsonObject = new JSONObject(content);
                int code = jsonObject.getInt("code");
                mGoodsShelfList.clear();
                if (code == 200) {
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Gson gson = new Gson();
                        GoodsShelfBean goodsShelfBean = gson.fromJson(jsonArray.getJSONObject(i).toString(), GoodsShelfBean.class);
                        mGoodsShelfList.add(goodsShelfBean);
                    }
                } else {

                }
                GoodsHolder.getInstance().setmGoodsShelfList(mGoodsShelfList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFinish() {
            super.onFinish();
            requestCount--;
            HttpUtil.getGoodsFloorList(goodsFloorCallback,1);
        }
    };
    StringCallback goodsFloorCallback = new StringCallback() {
        @Override
        public void onSuccess(Response<String> response) {
            String content = response.body().toString();
            LogUtils.d(content);
            try {
                JSONObject jsonObject = new JSONObject(content);
                int code = jsonObject.getInt("code");
                mGoodsFloorList.clear();
                if (code == 200) {
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Gson gson = new Gson();
                        GoodsFloorBean goodsFloorBean = gson.fromJson(jsonArray.getJSONObject(i).toString(), GoodsFloorBean.class);
                        mGoodsFloorList.add(goodsFloorBean);
                    }
                } else {

                }
                GoodsHolder.getInstance().setmGoodsFloorList(mGoodsFloorList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFinish() {
            super.onFinish();
            requestCount--;
            HttpUtil.getGoodsLocationList(goodsLocationCallback,1);
        }
    };
    StringCallback goodsLocationCallback = new StringCallback() {
        @Override
        public void onSuccess(Response<String> response) {
            String content = response.body().toString();
            LogUtils.d(content);
            try {
                JSONObject jsonObject = new JSONObject(content);
                int code = jsonObject.getInt("code");
                mGoodsLocationList.clear();
                if (code == 200) {
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Gson gson = new Gson();
                        GoodsLocationBean goodsLocationBean = gson.fromJson(jsonArray.getJSONObject(i).toString(), GoodsLocationBean.class);
                        mGoodsLocationList.add(goodsLocationBean);
                    }
                } else {

                }
                GoodsHolder.getInstance().setmGoodsLocationList(mGoodsLocationList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFinish() {
            super.onFinish();
            requestCount--;
            hideProgress();
        }
    };
}
