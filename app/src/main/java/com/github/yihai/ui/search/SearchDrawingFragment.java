package com.github.yihai.ui.search;

import android.app.Activity;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.github.yihai.R;
import com.github.yihai.base.BaseFragment;
import com.github.yihai.bean.SearchResultBean;
import com.github.yihai.common.util.AppUtils;
import com.github.yihai.common.util.LogUtils;
import com.github.yihai.manager.HttpUtil;
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
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/12/13.
 */

public class SearchDrawingFragment extends BaseFragment {

    String materialCode = "";
    String companyCode = "";
    Unbinder unbinder;
    @BindView(R.id.shelf1)
    GoodShelfView shelf1;
    @BindView(R.id.shelf2)
    GoodShelfView shelf2;
    private String huowei = "";
    private List<PointBean> pointBeanList1;
    private List<PointBean> pointBeanList2;

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_search_drawing;
    }

    public static SearchDrawingFragment newInstance() {
        Bundle args = new Bundle();
        SearchDrawingFragment fragment = new SearchDrawingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        materialCode = ((SearchActivity) activity).getMaterialCode();
        companyCode = ((SearchActivity) activity).getCompanyCode();
        HttpUtil.getSearchResultList(searchResultCallback, materialCode, companyCode);
    }

    @Override
    protected void initView(View mContentView) {

    }

    @Override
    protected void initData() {
        pointBeanList1 = new ArrayList<>();
        pointBeanList2 = new ArrayList<>();
    }

    @Override
    protected void bindEvent(View mContentView) {

    }

    StringCallback searchResultCallback = new StringCallback() {
        @Override
        public void onStart(Request<String, ? extends Request> request) {
            super.onStart(request);
            showProgress("请稍后");
        }

        @Override
        public void onSuccess(Response<String> response) {
            String content = response.body().toString();
            LogUtils.d(content);
            try {
                JSONObject jsonObject = new JSONObject(content);
                int code = jsonObject.getInt("code");
                pointBeanList1.clear();
                pointBeanList2.clear();
                if (code == 200) {
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Gson gson = new Gson();
                        SearchResultBean resultBean = gson.fromJson(jsonArray.getJSONObject(i).toString(), SearchResultBean.class);
                        huowei = resultBean.getHW_NO();
                        PointBean pointBean = AppUtils.getGoodsSite(huowei);
                        if(pointBean.getZ()==001)
                        {
                            pointBeanList1.add(pointBean);
                        }else if(pointBean.getZ()==002)
                        {
                            pointBeanList2.add(pointBean);
                        }

                    }
                } else {

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
