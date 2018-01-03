package com.github.yihai.ui.out;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.yihai.R;
import com.github.yihai.base.BaseFragment;
import com.github.yihai.bean.OutPlanBean;
import com.github.yihai.common.util.LogUtils;
import com.github.yihai.manager.HttpUtil;
import com.github.yihai.ui.main.BluetoothListActivity;
import com.github.yihai.zebra.ZebarHolder;
import com.github.yihai.zebra.ZebarUtil;
import com.google.gson.Gson;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.zebra.rfid.api3.InvalidUsageException;
import com.zebra.rfid.api3.OperationFailureException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by dzl on 2017/12/12.
 */

public class OutShelvesFragment extends BaseFragment {

    @BindView(R.id.spin_plan)
    Spinner mSpinPlan;
    @BindView(R.id.ll_plan_choice)
    LinearLayout mPlanChoiceView;
    @BindView(R.id.ll_qr)
    LinearLayout mQrView;
    @BindView(R.id.iv_stop)
    ImageView mIvStop;
    @BindView(R.id.tv_rfid)
    TextView mTvRfid;
    @BindView(R.id.tv_goods_shelf)
    TextView mTvGoodsShelf;
    @BindView(R.id.tv_goods_floor)
    TextView mTvGoodsFloor;
    @BindView(R.id.tv_goods_location)
    TextView mTvGoodsLocation;
    @BindView(R.id.tv_material)
    TextView mTvMaterial;
    @BindView(R.id.tv_goods_right)
    TextView mTvGoodsRight;
    @BindView(R.id.tv_goods_alias)
    TextView mTvGoodsAlias;
    Unbinder unbinder;
    @BindView(R.id.ll_clear)
    LinearLayout llClear;
    @BindView(R.id.ll_out_shelves)
    LinearLayout llOutShelves;

    private OutPlanListAdapter mOutPlanAdapter;
    private List<OutPlanBean> mOutPlanList;
    private static final int OUT_STATE_SUCCESS = 1;

    public static OutShelvesFragment newInstance() {
        Bundle args = new Bundle();
        OutShelvesFragment fragment = new OutShelvesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_out_shelves;
    }

    @Override
    protected void initView(View mContentView) {
        mIvStop.setVisibility(View.GONE);
        mOutPlanAdapter = new OutPlanListAdapter(getContext());
        mSpinPlan.setAdapter(mOutPlanAdapter);
    }

    @Override
    protected void initData() {
        mOutPlanList = new ArrayList<>();
        HttpUtil.getOutPlanList(outPlanListCallback);
        if (ZebarHolder.mConnectedDevice == null) {
            skipAct(BluetoothListActivity.class);
        } else {
            ZebarHolder.eventHandler.setHandler(handler);
        }
    }

    @Override
    protected void bindEvent(View mContentView) {
        mSpinPlan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    return;
                }
                OutPlanBean outPlanBean = mOutPlanAdapter.getItem(position);
                if (outPlanBean != null) {
                    if (!TextUtils.isEmpty(outPlanBean.getHuoJia())) {
                        mTvGoodsShelf.setText(outPlanBean.getHuoJia());
                    }
                    if (!TextUtils.isEmpty(outPlanBean.getHuoCeng())) {
                        mTvGoodsFloor.setText(outPlanBean.getHuoCeng());
                    }
                    if (!TextUtils.isEmpty(outPlanBean.getHuoCeng())) {
                        mTvGoodsLocation.setText(outPlanBean.getHuoWei());
                    }
                    if (!TextUtils.isEmpty(outPlanBean.getMaterialName())) {
                        mTvMaterial.setText(outPlanBean.getMaterialName());
                    }
                    if (!TextUtils.isEmpty(outPlanBean.getCompanyName())) {
                        mTvGoodsRight.setText(outPlanBean.getCompanyName());
                    }
                    if (!TextUtils.isEmpty(outPlanBean.getIdentityMark())) {
                        mTvGoodsAlias.setText(outPlanBean.getIdentityMark());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @OnClick(R.id.tv_rfid)
    public void onViewClicked() {

    }

    @OnClick({R.id.ll_plan_choice, R.id.ll_qr, R.id.ll_clear, R.id.ll_out_shelves})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_plan_choice:
                mOutPlanList = new ArrayList<>();
                HttpUtil.getOutPlanList(outPlanListCallback);
                break;
            case R.id.ll_qr:
                break;
            case R.id.ll_clear:
                mSpinPlan.setSelection(0);
                mTvRfid.setText("");
                mTvGoodsShelf.setText("");
                mTvGoodsFloor.setText("");
                mTvGoodsLocation.setText("");
                mTvMaterial.setText("");
                mTvGoodsRight.setText("");
                mTvGoodsAlias.setText("");
                break;
            case R.id.ll_out_shelves:

                if (mSpinPlan.getSelectedItemPosition() == 0) {
                    showToast("请先选择出库计划");
                    return;
                }
                OutPlanBean outPlanBean1 = (OutPlanBean) mSpinPlan.getSelectedItem();
                if (!mTvRfid.getText().toString().equals(outPlanBean1.getMark1())) {
                    showToast(getString(R.string.hint_scan_not_equal));
                    return;
                }
                /*0创建
                1已确认出库
                9作废*/

                int position = mSpinPlan.getSelectedItemPosition();
                OutPlanBean outPlanBean = (OutPlanBean) mOutPlanAdapter.getItem(position);
                String rfid = mTvRfid.getText().toString().trim();
                String goods_sheleves = mTvGoodsShelf.getText().toString().trim();
                String goods_floor = mTvGoodsFloor.getText().toString().trim();
                String goods_location = mTvGoodsLocation.getText().toString().trim();
                String material = mTvMaterial.getText().toString().trim();
                String goods_rights = mTvGoodsRight.getText().toString().trim();
                String goods_alias = mTvGoodsAlias.getText().toString().trim();

                HttpUtil.postOutUpdate(
                        outPlanBean.getID(),
                        OUT_STATE_SUCCESS,
                        outUpdateCallback);
                break;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ZebarHolder.eventHandler.removeHandler();
        try {
            ZebarUtil.unbindEvent(ZebarHolder.eventHandler);
        } catch (InvalidUsageException e) {
            e.printStackTrace();
        } catch (OperationFailureException e) {
            e.printStackTrace();
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String data = msg.getData().getString("data");
            if (mTvRfid != null && !TextUtils.isEmpty(data)) {
                mTvRfid.setText(data);
            }
        }
    };
    StringCallback outPlanListCallback = new StringCallback() {
        @Override
        public void onStart(Request<String, ? extends Request> request) {
            super.onStart(request);
            showProgress(getString(R.string.hint_waiting));
        }

        @Override
        public void onSuccess(Response<String> response) {
            String content = response.body().toString();
            LogUtils.d(content);
            try {
                JSONObject jsonObject = new JSONObject(content);
                int code = jsonObject.getInt("code");
                mOutPlanList.clear();
                if (code == 200) {
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Gson gson = new Gson();
                        OutPlanBean inPlanBean = gson.fromJson(jsonArray.getJSONObject(i).toString(), OutPlanBean.class);
                        mOutPlanList.add(inPlanBean);
                    }
                }
               // else {
          //          showToast(getString(R.string.hint_plan_null));
           //     }
                mOutPlanAdapter.setDatas(mOutPlanList);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onError(Response<String> response) {
            super.onError(response);
            showToast(getString(R.string.hint_net_error));
        }

        @Override
        public void onFinish() {
            super.onFinish();
            hideProgress();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    StringCallback outUpdateCallback = new StringCallback() {
        @Override
        public void onStart(Request<String, ? extends Request> request) {
            super.onStart(request);
            showProgress(getString(R.string.hint_waiting));
        }

        @Override
        public void onSuccess(Response<String> response) {
            String content = response.body().toString();
            LogUtils.d(content);
            try {
                JSONObject jsonObject = new JSONObject(content);
                int code = jsonObject.getInt("code");
                if (code == 200) {
                    showToast(jsonObject.getString("msg"));
                    mSpinPlan.setSelection(0);
                    mTvRfid.setText("");
                    mTvGoodsShelf.setText("");
                    mTvGoodsFloor.setText("");
                    mTvGoodsLocation.setText("");
                    mTvMaterial.setText("");
                    mTvGoodsRight.setText("");
                    mTvGoodsAlias.setText("");

                    HttpUtil.getOutPlanList(outPlanListCallback);
                } else {
                    showToast(getString(R.string.hint_submit_error));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onError(Response<String> response) {
            super.onError(response);
            showToast(getString(R.string.hint_net_error));
        }

        @Override
        public void onFinish() {
            super.onFinish();
            hideProgress();
        }
    };

}
