package com.github.yihai.ui.in;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.yihai.R;
import com.github.yihai.base.BaseFragment;
import com.github.yihai.bean.GoodsRightBean;
import com.github.yihai.bean.MaterialBean;
import com.github.yihai.common.util.AppUtils;
import com.github.yihai.common.util.LogUtils;
import com.github.yihai.manager.HttpUtil;
import com.github.yihai.ui.MyApplication;
import com.github.yihai.ui.goods.MaterialListAdapter;
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

public class InPlanFragment extends BaseFragment {

    @BindView(R.id.tv_rfid)
    TextView mTvRfid;
    @BindView(R.id.tv_qrcode)
    TextView mTvQrcode;
    @BindView(R.id.spn_material)
    Spinner mSpnMaterial;
    @BindView(R.id.spn_goods_right)
    Spinner mSpnGoodsRight;
    @BindView(R.id.et_goods_alias)
    EditText mEtGoodsAlias;
    @BindView(R.id.et_plan_name)
    EditText mEtPlanName;
    @BindView(R.id.ll_clear)
    LinearLayout llClear;
    @BindView(R.id.ll_in)
    LinearLayout llIn;
    Unbinder unbinder;

    private MaterialListAdapter mMaterialListAdapter;
    private GoodsRightListAdapter mGoodsRightListAdapter;
    private List<MaterialBean> mMaterialList;
    private List<GoodsRightBean> mGoodsRightList;
//    private ZebarPopUtil util;

    public static InPlanFragment newInstance() {
        Bundle args = new Bundle();
        InPlanFragment fragment = new InPlanFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_in_plan;
    }

    @Override
    protected void initView(View mContentView) {
        mEtPlanName.setText("入库计划"+ MyApplication.inPlanNo);
        mEtPlanName.setSelection(mEtPlanName.length());
        mMaterialListAdapter = new MaterialListAdapter(getContext());
        mGoodsRightListAdapter = new GoodsRightListAdapter(getContext());

        mSpnMaterial.setAdapter(mMaterialListAdapter);
        mSpnGoodsRight.setAdapter(mGoodsRightListAdapter);
    }

    @Override
    protected void initData() {
        showProgress(getString(R.string.hint_waiting));
        mMaterialList = new ArrayList<>();
        mGoodsRightList = new ArrayList<>();
        HttpUtil.getMaterialList(materialListCallback);
        if (ZebarHolder.mConnectedDevice == null) {
            skipAct(BluetoothListActivity.class);
        } else {
            ZebarHolder.eventHandler.setHandler(handler);
        }
    }

    @Override
    protected void bindEvent(View mContentView) {

    }

    @OnClick({R.id.tv_rfid, R.id.tv_qrcode, R.id.ll_clear, R.id.ll_in})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_rfid: {
//                if (ZebarHolder.mConnectedDevice == null) {
//                    skipAct(BluetoothListActivity.class);
//                } else {
////                    util.showPopup(mTvRfid);
//                    ZebarHolder.eventHandler.setHandler(handler);
//                    //     ZebarUtil.startScan(getContext(), ZebarHolder.eventHandler);
//                    showToast("请扫描");
//                }
                break;
            }
            case R.id.tv_qrcode:
                ZebarUtil.endScan();
                break;
            case R.id.ll_in: {
                if (TextUtils.isEmpty(mTvRfid.getText())) {
                    showToast(getString(R.string.hint_scan_null));
                    return;
                }
                if (mSpnMaterial.getSelectedItemPosition() == 0 || mSpnMaterial.getSelectedItem() == null) {
                    showToast(getString(R.string.hint_material));
                    return;
                }
                if (mSpnGoodsRight.getSelectedItemPosition() == 0 || mSpnGoodsRight.getSelectedItem() == null) {
                    showToast(getString(R.string.hint_goods_right));
                    return;
                }
                if (TextUtils.isEmpty(mEtPlanName.getText())) {
                    showToast(getString(R.string.hint_plan_name));
                    return;
                }
                String userName = AppUtils.getUserName(getContext());
                String materialCode = ((MaterialBean) mSpnMaterial.getSelectedItem()).getMaterialCode();
                String companyCode = ((GoodsRightBean) mSpnGoodsRight.getSelectedItem()).getCompanyCode();
                String identityMark = mEtGoodsAlias.getText().toString();
                String rfid = mTvRfid.getText().toString();
                String qrcode = "";
                String RuKuPlanName = mEtPlanName.getText().toString().trim();
                HttpUtil.postCreateInPlan(userName, materialCode, companyCode,
                        identityMark, rfid, qrcode, RuKuPlanName, inPlanCallback);
                break;
            }
            case R.id.ll_clear: {
                mSpnMaterial.setSelection(0);
                mSpnGoodsRight.setSelection(0);
                mTvRfid.setText("");
                mEtGoodsAlias.setText("");
//                mEtPlanName.setText("");
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
            if (mTvRfid != null && !TextUtils.isEmpty(data)) {
                mTvRfid.setText(data);
            }
//            synchronized (util) {
//                for (String item : util.getList()) {
//                    if (item.equals(data)) {
//                        Log.i("9999999", "11:" + data);
//                        return;
//                    }
//                }
//            }
//            Log.i("9999999", "22:" + data);
//            util.addData(data);
        }
    };

    StringCallback materialListCallback = new StringCallback() {

        @Override
        public void onSuccess(Response<String> response) {
            String content = response.body().toString();
            LogUtils.d(content);
            try {
                JSONObject jsonObject = new JSONObject(content);
                int code = jsonObject.getInt("code");
                mMaterialList.clear();
                if (code == 200) {
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Gson gson = new Gson();
                        MaterialBean materialBean = gson.fromJson(jsonArray.getJSONObject(i).toString(), MaterialBean.class);
                        mMaterialList.add(materialBean);
                    }
                } else {

                }
                mMaterialListAdapter.setDatas(mMaterialList);
                HttpUtil.getGoodsRightList(goodsRightListCallback);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(Response<String> response) {
            super.onError(response);
            showToast(getString(R.string.hint_net_error));
        }

    };
    StringCallback goodsRightListCallback = new StringCallback() {

        @Override
        public void onSuccess(Response<String> response) {
            String content = response.body().toString();
            LogUtils.d(content);
            try {
                JSONObject jsonObject = new JSONObject(content);
                int code = jsonObject.getInt("code");
                mGoodsRightList.clear();
                if (code == 200) {
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Gson gson = new Gson();
                        GoodsRightBean goodsRightBean = gson.fromJson(jsonArray.getJSONObject(i).toString(), GoodsRightBean.class);
                        mGoodsRightList.add(goodsRightBean);
                    }
                } else {

                }
                mGoodsRightListAdapter.setDatas(mGoodsRightList);
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
    StringCallback inPlanCallback = new StringCallback() {
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
                    mEtPlanName.setText("入库计划"+ (++MyApplication.inPlanNo));
                    mEtPlanName.setSelection(mEtPlanName.length());
                } else if (code == 201) {
                    String msg = jsonObject.getString("msg");
                    showToast(msg);
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
}
