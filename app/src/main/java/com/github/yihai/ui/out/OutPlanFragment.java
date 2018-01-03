package com.github.yihai.ui.out;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.yihai.R;
import com.github.yihai.base.BaseFragment;
import com.github.yihai.bean.GoodsBean;
import com.github.yihai.bean.MaterialBean;
import com.github.yihai.common.util.AppUtils;
import com.github.yihai.common.util.LogUtils;
import com.github.yihai.manager.HttpUtil;
import com.github.yihai.ui.MyApplication;
import com.github.yihai.ui.goods.MaterialListAdapter;
import com.github.yihai.widget.GoodShelfView;
import com.github.yihai.widget.PointBean;
import com.google.gson.Gson;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

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

public class OutPlanFragment extends BaseFragment {

    @BindView(R.id.ll_plan_choice)
    LinearLayout mPlanChoiceView;
    @BindView(R.id.ll_sure)
    LinearLayout mSureView;
    @BindView(R.id.tv_goods_shelf)
    TextView mTvGoodShelf;
    @BindView(R.id.tv_goods_floor)
    TextView mTvGoodsFloor;
    @BindView(R.id.tv_goods_location)
    TextView mTvGoodsLocation;
    @BindView(R.id.spin_plan)
    Spinner mSpinPlan;
    @BindView(R.id.et_plan_name)
    EditText etPlanName;
    @BindView(R.id.iv_sure)
    ImageView ivSure;
    @BindView(R.id.shelf1)
    GoodShelfView shelf1;
    @BindView(R.id.shelf2)
    GoodShelfView shelf2;
    @BindView(R.id.ll_clear)
    LinearLayout llClear;
    @BindView(R.id.ll_in)
    LinearLayout llIn;
    private List<PointBean> pointBeanList1;
    private List<PointBean> pointBeanList2;

    private MaterialListAdapter mMaterialListAdapter;
    private List<MaterialBean> mMaterialList;

    private GoodsBean mGoodsBean;

    public static OutPlanFragment newInstance() {
        Bundle args = new Bundle();
        OutPlanFragment fragment = new OutPlanFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_out_plan;
    }

    @Override
    protected void initView(View mContentView) {
        etPlanName.setText("出库计划"+ MyApplication.outPlanNo);
        etPlanName.setSelection(etPlanName.length());
        mMaterialListAdapter = new MaterialListAdapter(getContext(), getString(R.string.tip_material_select));
        mSpinPlan.setAdapter(mMaterialListAdapter);
    }

    @Override
    protected void initData() {
        pointBeanList1 = new ArrayList<>();
        pointBeanList2 = new ArrayList<>();
        mMaterialList = new ArrayList<>();
        HttpUtil.getMaterialList(materialListCallback);
    }

    @Override
    protected void bindEvent(View mContentView) {
        mSpinPlan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mTvGoodShelf.setText("");
                mTvGoodsFloor.setText("");
                mTvGoodsLocation.setText("");
                if (position != 0) {
                    HttpUtil.getGoodsDetail(mMaterialListAdapter.getItem(position).getMaterialCode(), goodsDetailCallback);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @OnClick({R.id.ll_plan_choice, R.id.ll_in ,R.id.ll_clear})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_in: {
                if (mSpinPlan.getSelectedItemPosition() == 0 || mSpinPlan.getSelectedItem() == null) {
                    showToast(getString(R.string.hint_material));
                    return;
                }
                if (TextUtils.isEmpty(etPlanName.getText())) {
                    showToast(getString(R.string.hint_plan_name));
                    return;
                }
                if(TextUtils.isEmpty(mTvGoodsLocation.getText()) ||
                        TextUtils.isEmpty(mTvGoodsLocation.getText()) ||
                        TextUtils.isEmpty(mTvGoodsLocation.getText()))
                {
                    showToast(getString(R.string.hint_location_null));
                    return;
                }
                HttpUtil.postCreateOutPlan(
                        AppUtils.getUserName(getContext()),
                        etPlanName.getText().toString().trim(),
                        ((MaterialBean) mSpinPlan.getSelectedItem()).getMaterialCode(),
                        mGoodsBean.getCompanyCode(), mGoodsBean.getStockID(),
                        outPlanCallback);
                break;
            }
            case R.id.ll_plan_choice: {
                if (mMaterialList == null || mMaterialList.size() == 0) {
                    showToast(getString(R.string.hint_material_null));
                    return;
                }
                mMaterialListAdapter.setDatas(mMaterialList);
                mSpinPlan.performClick();
                break;
            }
            case R.id.ll_clear: {
                Reset();
//                etPlanName.setText("");
                break;
            }
        }
    }

    StringCallback materialListCallback = new StringCallback() {
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

    StringCallback goodsDetailCallback = new StringCallback() {
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
                mMaterialList.clear();
                pointBeanList1.clear();
                pointBeanList2.clear();
                if (code == 200) {
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    if (jsonArray != null && jsonArray.length() > 0) {
                        Gson gson = new Gson();
                        mGoodsBean = gson.fromJson(jsonArray.getJSONObject(0).toString(), GoodsBean.class);
                        mTvGoodShelf.setText(mGoodsBean.getHuoJia());
                        mTvGoodsFloor.setText(mGoodsBean.getHuoCeng());
                        mTvGoodsLocation.setText(mGoodsBean.getHuoWei());

                        String huowei = mGoodsBean.getHuoWei();
                        PointBean pointBean = AppUtils.getGoodsSite(huowei);
                        if (pointBean.getZ() == 001) {
                            pointBeanList1.add(pointBean);
                        } else if (pointBean.getZ() == 002) {
                            pointBeanList2.add(pointBean);
                        }
                    }
                } else if (code == 201) {
                    String msg = jsonObject.getString("msg");
                    showToast(msg);
                } else {
                    showToast(getString(R.string.hint_submit_error));
                }

                shelf1.setGoodsList(pointBeanList1);
                shelf2.setGoodsList(pointBeanList2);
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
    StringCallback outPlanCallback = new StringCallback() {
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
//                    int planno=MyApplication.getOutPlanNo()+1;
//                    MyApplication.setOutPlanNo(planno);
                    etPlanName.setText("出库计划"+ (++MyApplication.outPlanNo));
                    etPlanName.setSelection(etPlanName.length());
                    Reset();
                }
                else if (code == 201) {
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
    private void Reset()
    {
        mSpinPlan.setSelection(0);
        mTvGoodShelf.setText("");
        mTvGoodsFloor.setText("");
        mTvGoodsLocation.setText("");
    }
}
