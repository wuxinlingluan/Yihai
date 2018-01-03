package com.github.yihai.ui.search;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.yihai.R;
import com.github.yihai.base.BaseActivity;
import com.github.yihai.bean.GoodsRightBean;
import com.github.yihai.bean.MaterialBean;
import com.github.yihai.common.util.LogUtils;
import com.github.yihai.manager.HttpUtil;
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
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 查询功能
 * Created by zyq on 2017/12/13.
 */

public class SearchActivity extends BaseActivity {
    @BindView(R.id.custom_back)
    TextView customBack;
    @BindView(R.id.custom_title)
    TextView customTitle;

    SearchDrawingFragment drawingFragment;
    SearchListFragment listFragment;
    @BindView(R.id.view_content)
    RelativeLayout viewContent;
    @BindView(R.id.custom_simple_bar)
    Toolbar customSimpleBar;
    @BindView(R.id.container)
    FrameLayout container;
    @BindView(R.id.spin_material)
    Spinner spinMaterial;
    @BindView(R.id.spin_huoquan)
    Spinner spinHuoquan;
    @BindView(R.id.iv_refresh)
    ImageView ivRefresh;

    private GoodsRightListAdapter huoquanListAdapter;
    private MaterialListAdapter materialListAdapter;
    private List<GoodsRightBean> listHuoquanBean;
    private List<MaterialBean> listMaterialBean;
    private String materialCode = "";
    private String companyCode = "";
    private String huowei = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);
        ButterKnife.bind(this);
    }

    @Override
    public void initView() {
        customTitle.setText("查询");
//        Bundle bundle = new Bundle();
//
//        listFragment.setArguments();

        listFragment = SearchListFragment.newInstance();
        drawingFragment = SearchDrawingFragment.newInstance();
        replaceFragmentToActivity(listFragment, R.id.container);

        materialListAdapter = new MaterialListAdapter(this, getString(R.string.tip_material_select));
        huoquanListAdapter = new GoodsRightListAdapter(this, getString(R.string.action_select_company));

        spinMaterial.setAdapter(materialListAdapter);
        spinHuoquan.setAdapter(huoquanListAdapter);
    }

    @Override
    public void initData() {
        listMaterialBean = new ArrayList<>();
        listHuoquanBean = new ArrayList<>();
        HttpUtil.getMaterialList(materialListCallback);
    }

    @Override
    public void bindEvent() {
        spinMaterial.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                materialCode = ((MaterialBean) spinMaterial.getSelectedItem()).getMaterialCode();
                companyCode = ((GoodsRightBean) spinHuoquan.getSelectedItem()).getCompanyCode();
                if (spinMaterial.getSelectedItemPosition() == 0) {
                    materialCode = "";
                }
                if (spinHuoquan.getSelectedItemPosition() == 0) {
                    companyCode = "";
                }
                if (listFragment.isVisible()) {
                    HttpUtil.getSearchResultList(listFragment.searchResultCallback, materialCode, companyCode);
                } else if (drawingFragment.isVisible()) {
                    HttpUtil.getSearchResultList(drawingFragment.searchResultCallback, materialCode, companyCode);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinHuoquan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String materialCode = ((MaterialBean) spinMaterial.getSelectedItem()).getMaterialCode();
                String companyCode = ((GoodsRightBean) spinHuoquan.getSelectedItem()).getCompanyCode();
                if (spinMaterial.getSelectedItemPosition() == 0) {
                    materialCode = "";
                }
                if (spinHuoquan.getSelectedItemPosition() == 0) {
                    companyCode = "";
                }
                if (listFragment.isVisible()) {
                    HttpUtil.getSearchResultList(listFragment.searchResultCallback, materialCode, companyCode);
                } else if (drawingFragment.isVisible()) {
                    HttpUtil.getSearchResultList(drawingFragment.searchResultCallback, materialCode, companyCode);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
                listMaterialBean.clear();
                if (code == 200) {
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Gson gson = new Gson();
                        MaterialBean materialBean = gson.fromJson(jsonArray.getJSONObject(i).toString(), MaterialBean.class);
                        listMaterialBean.add(materialBean);
                    }
                } else {

                }
                materialListAdapter.setDatas(listMaterialBean);
                HttpUtil.getGoodsRightList(huoquanListCallback);
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
    StringCallback huoquanListCallback = new StringCallback() {

        @Override
        public void onSuccess(Response<String> response) {
            String content = response.body().toString();
            LogUtils.d(content);
            try {
                JSONObject jsonObject = new JSONObject(content);
                int code = jsonObject.getInt("code");
                listHuoquanBean.clear();
                if (code == 200) {
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Gson gson = new Gson();
                        GoodsRightBean huoquanBean = gson.fromJson(jsonArray.getJSONObject(i).toString(), GoodsRightBean.class);
                        listHuoquanBean.add(huoquanBean);
                    }
                } else {

                }
                huoquanListAdapter.setDatas(listHuoquanBean);
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

    public String getMaterialCode() {
        return materialCode;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    @OnClick({R.id.custom_back, R.id.iv_refresh})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.custom_back:
                finish();
                break;
            case R.id.iv_refresh:
                materialCode = ((MaterialBean) spinMaterial.getSelectedItem()).getMaterialCode();
                if(materialCode.equals("-1"))
                {
                    materialCode="";
                }
                companyCode = ((GoodsRightBean) spinHuoquan.getSelectedItem()).getCompanyCode();
                if(companyCode.equals("-1"))
                {
                    companyCode="";
                }
                if (listFragment.isVisible()) {
                    showFragmentToActivity( listFragment,drawingFragment, R.id.container);
                    HttpUtil.getSearchResultList(drawingFragment.searchResultCallback, materialCode, companyCode);
                } else {
                    showFragmentToActivity( drawingFragment,listFragment, R.id.container);
                    HttpUtil.getSearchResultList(listFragment.searchResultCallback, materialCode, companyCode);
                }

                break;
        }
    }
}
