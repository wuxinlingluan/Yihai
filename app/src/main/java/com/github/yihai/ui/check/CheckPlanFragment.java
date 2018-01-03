package com.github.yihai.ui.check;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.github.yihai.R;
import com.github.yihai.base.BaseFragment;
import com.github.yihai.bean.GoodsFloorBean;
import com.github.yihai.bean.GoodsLocationBean;
import com.github.yihai.bean.GoodsRightBean;
import com.github.yihai.bean.GoodsShelfBean;
import com.github.yihai.bean.MaterialBean;
import com.github.yihai.common.util.AppUtils;
import com.github.yihai.common.util.LogUtils;
import com.github.yihai.manager.HttpUtil;
import com.github.yihai.ui.MyApplication;
import com.github.yihai.ui.goods.GoodsFloorListAdapter;
import com.github.yihai.ui.goods.GoodsHolder;
import com.github.yihai.ui.goods.GoodsLocationListAdapter;
import com.github.yihai.ui.goods.GoodsShelfListAdapter;
import com.github.yihai.ui.goods.MaterialListAdapter;
import com.github.yihai.ui.in.GoodsRightListAdapter;
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

public class CheckPlanFragment extends BaseFragment {

    @BindView(R.id.spn_goods_shelf)
    Spinner spnGoodsShelf;
    @BindView(R.id.spn_goods_floor)
    Spinner spnGoodsFloor;
    @BindView(R.id.spn_goods_location)
    Spinner spnGoodsLocation;
    @BindView(R.id.spn_material)
    Spinner spnMaterial;
    @BindView(R.id.spn_goods_right)
    Spinner spnGoodsRight;
    @BindView(R.id.view_ok)
    LinearLayout mOkView;
    @BindView(R.id.et_plan_name)
    EditText etPlanName;

    private GoodsShelfListAdapter mGoodsShelfListAdapter;
    private GoodsFloorListAdapter mGoodsFloorListAdapter;
    private GoodsLocationListAdapter mGoodsLocationListAdapter;
    private MaterialListAdapter mMaterialListAdapter;
    private GoodsRightListAdapter mGoodsRightListAdapter;

    private List<GoodsShelfBean> mGoodsShelfList;
    private List<GoodsFloorBean> mGoodsFloorList;
    private List<GoodsLocationBean> mGoodsLocationList;
    private List<MaterialBean> mMaterialList;
    private List<GoodsRightBean> mGoodsRightList;

    public static CheckPlanFragment newInstance() {
        Bundle args = new Bundle();
        CheckPlanFragment fragment = new CheckPlanFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_check_plan;
    }

    @Override
    protected void initView(View mContentView) {
        etPlanName.setText("盘库计划"+MyApplication.checkPlanNo);
        etPlanName.setSelection(etPlanName.length());
        mMaterialListAdapter = new MaterialListAdapter(getContext());
        mGoodsRightListAdapter = new GoodsRightListAdapter(getContext());
        mGoodsShelfListAdapter = new GoodsShelfListAdapter(getContext());
        mGoodsFloorListAdapter = new GoodsFloorListAdapter(getContext());
        mGoodsLocationListAdapter = new GoodsLocationListAdapter(getContext());

        spnGoodsShelf.setAdapter(mGoodsShelfListAdapter);
        spnGoodsFloor.setAdapter(mGoodsFloorListAdapter);
        spnGoodsLocation.setAdapter(mGoodsLocationListAdapter);
        spnMaterial.setAdapter(mMaterialListAdapter);
        spnGoodsRight.setAdapter(mGoodsRightListAdapter);
    }

    @Override
    protected void initData() {
        mGoodsShelfList = GoodsHolder.getInstance().getmGoodsShelfList();
        mGoodsFloorList = GoodsHolder.getInstance().getmGoodsFloorList();
        mGoodsLocationList = GoodsHolder.getInstance().getmGoodsLocationList();
        mMaterialList = new ArrayList<>();
        mGoodsRightList = new ArrayList<>();

        mGoodsShelfListAdapter.setDatas(mGoodsShelfList);

        HttpUtil.getMaterialList(materialListCallback);
    }

    @Override
    protected void bindEvent(View mContentView) {
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
                        if (goodsLocationBean.getHC_ID().equals(mGoodsFloorListAdapter.getItem(position ).getID())
                                &&goodsLocationBean.getHJ_ID().equals(mGoodsFloorListAdapter.getItem(position ).getHJ_ID()))  {
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

    @OnClick({R.id.view_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.view_ok: {
                if (TextUtils.isEmpty(etPlanName.getText())) {
                    showToast(getString(R.string.hint_plan_name));
                    return;
                }
                String material = "";
                String companyCode = "";

                String HuoJia = "";
                String HuoCeng = "";
                String HuoWei = "";
                if (spnGoodsShelf.getSelectedItem() != null && spnGoodsShelf.getSelectedItemPosition() > 0) {
                    //选择了货架
                    HuoJia = ((GoodsShelfBean) spnGoodsShelf.getSelectedItem()).getID();
                }
                if (spnGoodsFloor.getSelectedItem() != null && spnGoodsFloor.getSelectedItemPosition() > 0) {
                    //选择了货层
                    HuoCeng = ((GoodsFloorBean) spnGoodsFloor.getSelectedItem()).getID();
                }
                if (spnGoodsLocation.getSelectedItem() != null && spnGoodsLocation.getSelectedItemPosition() > 0) {
                    //选择了货位
                    HuoWei = ((GoodsLocationBean) spnGoodsLocation.getSelectedItem()).getID();
                }
          /*              String type = "0";
              String TypeID = "";
              if (spnGoodsLocation.getSelectedItem() != null && spnGoodsLocation.getSelectedItemPosition() > 0) {
                  //选择了货位
                  type = "3";
                  TypeID = ((GoodsLocationBean) spnGoodsLocation.getSelectedItem()).getID();
              } else if (spnGoodsFloor.getSelectedItem() != null && spnGoodsFloor.getSelectedItemPosition() > 0) {
                  //选择了货层
                  type = "2";
                  TypeID = ((GoodsFloorBean) spnGoodsFloor.getSelectedItem()).getID();
              } else if (spnGoodsShelf.getSelectedItem() != null && spnGoodsShelf.getSelectedItemPosition() > 0) {
                  //选择了货架
                  type = "1";
                  TypeID = ((GoodsShelfBean) spnGoodsShelf.getSelectedItem()).getID();
              }
          */
                boolean isSelectMaterial = spnMaterial.getSelectedItem() != null && spnMaterial.getSelectedItemPosition() > 0;
                boolean isSelectRight = spnGoodsRight.getSelectedItem() != null && spnGoodsRight.getSelectedItemPosition() > 0;
                if (isSelectMaterial || isSelectRight) {
                    //选择了物料或者货权
//                    type = "0";
                    if (isSelectMaterial) {
                        material = ((MaterialBean) spnMaterial.getSelectedItem()).getMaterialCode();
                    }
                    if (isSelectRight) {
                        companyCode = ((GoodsRightBean) spnGoodsRight.getSelectedItem()).getCompanyCode();
                    }
                } else {
                    //没有选择物料货权和其他的条件
                    if ((spnGoodsShelf.getSelectedItem() == null || spnGoodsShelf.getSelectedItemPosition() <= 0)) {
                        //选择了货位
                        showToast(getString(R.string.hint_select_null));
                        return;
                    }
                }
                HttpUtil.postCreateCheckPlan(AppUtils.getUserName(getContext()),
                        etPlanName.getText().toString().trim(),
                        material, companyCode, HuoJia, HuoCeng, HuoWei, checkPlanCallback);
                break;
            }
        }
    }

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

    StringCallback checkPlanCallback = new StringCallback() {
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
                    etPlanName.setText("盘库计划"+(++MyApplication.checkPlanNo));
                    etPlanName.setSelection(etPlanName.length());
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
}
