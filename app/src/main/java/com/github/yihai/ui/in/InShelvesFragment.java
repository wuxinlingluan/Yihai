package com.github.yihai.ui.in;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.yihai.R;
import com.github.yihai.base.BaseFragment;
import com.github.yihai.bean.GoodsFloorBean;
import com.github.yihai.bean.GoodsLocationBean;
import com.github.yihai.bean.GoodsShelfBean;
import com.github.yihai.bean.InPlanBean;
import com.github.yihai.common.util.AppUtils;
import com.github.yihai.common.util.LogUtils;
import com.github.yihai.manager.HttpUtil;
import com.github.yihai.ui.goods.GoodsFloorListAdapter;
import com.github.yihai.ui.goods.GoodsLocationListAdapter;
import com.github.yihai.ui.goods.GoodsShelfListAdapter;
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
import butterknife.OnClick;

/**
 * Created by dzl on 2017/12/12.
 */

public class InShelvesFragment extends BaseFragment {

    @BindView(R.id.iv_stop)
    ImageView ivStop;
    @BindView(R.id.ll_plan_choice)
    LinearLayout mPlanChoiceView;
    @BindView(R.id.ll_qr)
    LinearLayout mQrView;
    @BindView(R.id.spin_plan)
    Spinner mSpinPlan;
    @BindView(R.id.ll_clear)
    LinearLayout mClearView;
    @BindView(R.id.ll_in)
    LinearLayout mInView;
    @BindView(R.id.tv_rfid)
    TextView tvRfid;
    @BindView(R.id.tv_material)
    TextView tvMaterial;
    @BindView(R.id.tv_goods_right)
    TextView tvGoodsRight;
    @BindView(R.id.tv_goods_alias)
    TextView tvGoodsAlias;
    @BindView(R.id.spn_goods_shelf)
    Spinner spnGoodsShelf;
    @BindView(R.id.spn_goods_floor)
    Spinner spnGoodsFloor;
    @BindView(R.id.spn_goods_location)
    Spinner spnGoodsLocation;

    private InPlanListAdapter mInPlanAdapter;
    private GoodsShelfListAdapter mGoodsShelfListAdapter;
    private GoodsFloorListAdapter mGoodsFloorListAdapter;
    private GoodsLocationListAdapter mGoodsLocationListAdapter;
    private List<InPlanBean> mInPlanList;
    private List<GoodsShelfBean> mGoodsShelfList;
    private List<GoodsFloorBean> mGoodsFloorList;
    private List<GoodsLocationBean> mGoodsLocationList;

    public static InShelvesFragment newInstance() {
        Bundle args = new Bundle();
        InShelvesFragment fragment = new InShelvesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_in_shelves;
    }

    @Override
    protected void initView(View mContentView) {
        ivStop.setVisibility(View.GONE);
        mInPlanAdapter = new InPlanListAdapter(getContext());
        mSpinPlan.setAdapter(mInPlanAdapter);
    }

    @Override
    protected void initData() {
        mGoodsShelfListAdapter = new GoodsShelfListAdapter(getContext());
        mGoodsFloorListAdapter = new GoodsFloorListAdapter(getContext());
        mGoodsLocationListAdapter = new GoodsLocationListAdapter(getContext());
        mGoodsShelfList = new ArrayList<>(); //货架
        mGoodsFloorList = new ArrayList<>();//货层
        mGoodsLocationList = new ArrayList<>();//货位
        spnGoodsShelf.setAdapter(mGoodsShelfListAdapter);
        spnGoodsFloor.setAdapter(mGoodsFloorListAdapter);
        spnGoodsLocation.setAdapter(mGoodsLocationListAdapter);
        mInPlanList = new ArrayList<>();
        HttpUtil.getInPlanList(inPlanListCallback);
        HttpUtil.getGoodsShelfList(goodsShelfCallback, 2);
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
                InPlanBean inPlanBean = mInPlanAdapter.getItem(position);
                tvMaterial.setText(inPlanBean.getMaterialName());
                tvGoodsRight.setText(inPlanBean.getCompanyName());
                tvGoodsAlias.setText(inPlanBean.getIdentityMark());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spnGoodsShelf.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    spnGoodsFloor.setEnabled(false);
                } else {
                    spnGoodsFloor.setEnabled(true);

                    GoodsShelfBean goodsShelfBean = mGoodsShelfList.get(position - 1);
                    List<GoodsFloorBean> floorBeanList = new ArrayList<>();
                    for (int i = 0; i < mGoodsFloorList.size(); i++) {
                        GoodsFloorBean goodsFloorBean = mGoodsFloorList.get(i);
                        if (goodsFloorBean.getHJ_ID().equals(goodsShelfBean.getID())) {
                            floorBeanList.add(goodsFloorBean);
                        }
                    }
                    mGoodsFloorListAdapter.setDatas(floorBeanList);
                }
                spnGoodsLocation.setEnabled(false);
                spnGoodsFloor.setSelection(0);
                spnGoodsLocation.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spnGoodsFloor.setEnabled(false);
                spnGoodsLocation.setEnabled(false);
            }
        });
        spnGoodsFloor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    spnGoodsLocation.setEnabled(false);
                } else {
                    spnGoodsLocation.setEnabled(true);

                    List<GoodsLocationBean> goodsLocationBeanArrayList = new ArrayList<>();
                    for (int i = 0; i < mGoodsLocationList.size(); i++) {
                        GoodsLocationBean goodsLocationBean = mGoodsLocationList.get(i);
                        if (goodsLocationBean.getHC_ID().equals(mGoodsFloorListAdapter.getItem(position).getID())
                                && goodsLocationBean.getHJ_ID().equals(mGoodsFloorListAdapter.getItem(position).getHJ_ID())) {
                            goodsLocationBeanArrayList.add(goodsLocationBean);
                        }
                    }
                    mGoodsLocationListAdapter.setDatas(goodsLocationBeanArrayList);
                }
                spnGoodsLocation.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spnGoodsLocation.setEnabled(false);
            }
        });
    }

    @OnClick({R.id.ll_plan_choice, R.id.ll_qr, R.id.ll_clear, R.id.ll_in, R.id.tv_rfid})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_plan_choice: {
                if (mInPlanList == null || mInPlanList.size() == 0) {
                    showToast(getString(R.string.hint_plan_null));
                    return;
                }
                mInPlanAdapter.setDatas(mInPlanList);
                mSpinPlan.performClick();
                break;
            }
            case R.id.ll_qr:
                break;
            case R.id.ll_clear: {
                Reset();
                break;
            }
            case R.id.ll_in: {
                if (TextUtils.isEmpty(tvRfid.getText())) {
                    showToast(getString(R.string.hint_scan_null));
                    return;
                }
                InPlanBean inPlanBean1 = (InPlanBean) mSpinPlan.getSelectedItem();
                if (!tvRfid.getText().toString().equals(inPlanBean1.getMark1())) {
                    showToast(getString(R.string.hint_scan_not_equal));
                    return;
                }
                if (mSpinPlan.getSelectedItemPosition() == 0 || mSpinPlan.getSelectedItem() == null) {
                    showToast(getString(R.string.hint_plan_select));
                    return;
                }
                if (spnGoodsShelf.getSelectedItemPosition() == 0 || spnGoodsShelf.getSelectedItem() == null) {
                    showToast(getString(R.string.hint_goods_shelf));
                    return;
                }
                if (spnGoodsFloor.getSelectedItemPosition() == 0 || spnGoodsFloor.getSelectedItem() == null) {
                    showToast(getString(R.string.hint_goods_floor));
                    return;
                }
                if (spnGoodsLocation.getSelectedItemPosition() == 0 || spnGoodsLocation.getSelectedItem() == null) {
                    showToast(getString(R.string.hint_goods_location));
                    return;
                }
                GoodsLocationBean goodsLocationBean = (GoodsLocationBean) spnGoodsLocation.getSelectedItem();
                InPlanBean inPlanBean = (InPlanBean) mSpinPlan.getSelectedItem();
                HttpUtil.postInAction(
                        AppUtils.getUserName(getContext()),
                        inPlanBean.getID(),
                        goodsLocationBean.getID(),
                        inActionCallback);
                break;
            }
            case R.id.tv_rfid: {
                break;
            }
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
            if (tvRfid != null && !TextUtils.isEmpty(data)) {
                tvRfid.setText(data);
            }
        }
    };
    StringCallback inPlanListCallback = new StringCallback() {
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
                mInPlanList.clear();
                if (code == 200) {
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Gson gson = new Gson();
                        InPlanBean inPlanBean = gson.fromJson(jsonArray.getJSONObject(i).toString(), InPlanBean.class);
                        mInPlanList.add(inPlanBean);
                    }
                }
               // else {
          //          showToast(getString(R.string.hint_plan_null));
           //     }
                mInPlanAdapter.setDatas(mInPlanList);
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
    StringCallback inActionCallback = new StringCallback() {
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
                    showToast(getString(R.string.hint_submit_success));
                    Reset();

                    mInPlanList = new ArrayList<>();
                    HttpUtil.getInPlanList(inPlanListCallback);
                } else if (code == 201) {
                    String msg = jsonObject.getString("msg");
                    showToast(msg);
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
                mGoodsShelfListAdapter.setDatas(mGoodsShelfList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFinish() {
            super.onFinish();
            HttpUtil.getGoodsFloorList(goodsFloorCallback, 2);
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
                mGoodsFloorListAdapter.setDatas(mGoodsFloorList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFinish() {
            super.onFinish();
            HttpUtil.getGoodsLocationList(goodsLocationCallback, 2);
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
                mGoodsLocationListAdapter.setDatas(mGoodsLocationList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFinish() {
            super.onFinish();
            hideProgress();
        }
    };
    private void Reset()
    {
        mSpinPlan.setSelection(0);
        spnGoodsShelf.setSelection(0);
        spnGoodsFloor.setSelection(0);
        spnGoodsLocation.setSelection(0);
        tvRfid.setText("");
        tvMaterial.setText("");
        tvGoodsRight.setText("");
        tvGoodsAlias.setText("");
    }
}
