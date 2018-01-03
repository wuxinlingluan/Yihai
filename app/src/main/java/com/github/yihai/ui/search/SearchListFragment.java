package com.github.yihai.ui.search;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.github.yihai.R;
import com.github.yihai.base.BaseFragment;
import com.github.yihai.base.recycle.BaseRecycleAdapter;
import com.github.yihai.base.recycle.RecycleItemBean;
import com.github.yihai.bean.GoodsRightBean;
import com.github.yihai.bean.MaterialBean;
import com.github.yihai.bean.SearchResultBean;
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
import butterknife.Unbinder;

/**
 * Created by zyq on 2017/12/13.
 */

public class SearchListFragment extends BaseFragment {
    private static final int TYPE_SEARCH = 1;

    @BindView(R.id.rl)
    RecyclerView rl;
    Unbinder unbinder;

    private BaseRecycleAdapter mAdapter;

    private List<RecycleItemBean> mResultItems;

    String materialCode = "";
    String companyCode = "";

    public static SearchListFragment newInstance() {
        Bundle args = new Bundle();
        SearchListFragment fragment = new SearchListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_search_list;
    }

    @Override
    protected void initView(View mContentView) {
        mAdapter = new BaseRecycleAdapter.Builder(getContext())
                .addType(TYPE_SEARCH, R.layout.item_activity_search, SearchResultItemHolder.class)
                .build();

        LinearLayoutManager LayoutManager = new LinearLayoutManager(getContext());
        rl.setLayoutManager(LayoutManager);
        rl.setAdapter(mAdapter);
        rl.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

    }

    @Override
    protected void initData() {
        mResultItems = new ArrayList<>();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        materialCode = ((SearchActivity) activity).getMaterialCode();
        companyCode = ((SearchActivity) activity).getCompanyCode();
    }

    @Override
    protected void bindEvent(View mContentView) {

        HttpUtil.getSearchResultList(searchResultCallback, materialCode, companyCode);

    }

    StringCallback searchResultCallback = new StringCallback() {
        @Override
        public void onStart(Request<String, ? extends Request> request) {
            super.onStart(request);
            showProgress(getActivity().getString(R.string.hint_waiting));
        }

        @Override
        public void onSuccess(Response<String> response) {
            String content = response.body().toString();
            LogUtils.d(content);
            try {
                JSONObject jsonObject = new JSONObject(content);
                int code = jsonObject.getInt("code");
                mResultItems.clear();
                if (code == 200) {
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Gson gson = new Gson();
                        SearchResultBean resultBean = gson.fromJson(jsonArray.getJSONObject(i).toString(), SearchResultBean.class);
                        RecycleItemBean<SearchResultBean> resultBeanRecycleItemBean = new RecycleItemBean<>();

                        resultBeanRecycleItemBean.setType(TYPE_SEARCH);
                        resultBeanRecycleItemBean.setData(resultBean);
                        mResultItems.add(resultBeanRecycleItemBean);

                    }
                } else {

                }
                mAdapter.setDatas(mResultItems);
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
