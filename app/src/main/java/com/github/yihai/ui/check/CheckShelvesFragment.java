package com.github.yihai.ui.check;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.yihai.R;
import com.github.yihai.base.BaseFragment;
import com.github.yihai.base.recycle.BaseRecycleAdapter;
import com.github.yihai.base.recycle.OnItemClickListener;
import com.github.yihai.base.recycle.RecycleItemBean;
import com.github.yihai.bean.CheckPlanBean;
import com.github.yihai.bean.CheckPlanDetailBean;
import com.github.yihai.bean.MaterialBean;
import com.github.yihai.common.util.AppUtils;
import com.github.yihai.common.util.LogUtils;
import com.github.yihai.common.util.SharedPreferencesUtils;
import com.github.yihai.manager.HttpUtil;
import com.github.yihai.manager.YiHaiHttpConstaint;
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
import butterknife.OnClick;

/**
 * Created by dzl on 2017/12/12.
 */

public class CheckShelvesFragment extends BaseFragment {
    @BindView(R.id.tv_need_scan)
    TextView tvNeedScan;
    @BindView(R.id.tv_already_scan)
    TextView tvAlreadyScan;
    @BindView(R.id.ll_plan_choice)
    LinearLayout llPlanChoice;
    private boolean isStartScan;//开始盘库
    private boolean isItemUpdate; //是否点击事件
    private static final int TYPE_CHECK = 1;
    private static final int TYPE_DIVIDER_LINE = 2;
    private TextView tvRfid;
    private TextView tvMaterialinfo;
    private TextView tvGoodsRight;
    private TextView tvGoodsAlias;
    private TextView tvGoodsShelf;
    private TextView tvGoodsFloor;
    private TextView tvGoodsLocation;
    @BindView(R.id.recycle_view)
    RecyclerView mRecycleView;
    @BindView(R.id.spin_plan)
    Spinner mSpnPlan;
    @BindView(R.id.ll_qr)
    LinearLayout llQr;
    @BindView(R.id.iv_stop)
    ImageView ivStop;
    private BaseRecycleAdapter mErrorAdapter;
    private List<RecycleItemBean> mErrorItems;
    private MaterialListAdapter mMaterialListAdapter;
    private List<MaterialBean> mMaterialList;//下拉框spinner 数据
    private Dialog dialog;
    private ImageView ivRescan;
    private ImageView ivUpdate;
    private LinearLayout llConfirm;
    private EditText etTitle;
    private EditText etContent;
    private String pkjhID;
    private ImageView ivLight;
    private int num = 0;
    public static CheckShelvesFragment newInstance() {
        Bundle args = new Bundle();
        CheckShelvesFragment fragment = new CheckShelvesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_check_shelves;
    }

    @Override
    protected void initView(View mContentView) {
        mMaterialListAdapter = new MaterialListAdapter(getContext(),"选择计划");
        mSpnPlan.setAdapter(mMaterialListAdapter);
        mErrorAdapter = new BaseRecycleAdapter.Builder(getContext())
                .addType(TYPE_CHECK, R.layout.item_storage_check2, CheckGoodsItemHolder.class)
                .addType(TYPE_DIVIDER_LINE, R.layout.view_divider_item, SimpleItemHolder.class)
                .build();
        FullyLinearLayoutManager LayoutManager = new FullyLinearLayoutManager(getContext());
        mRecycleView.setLayoutManager(LayoutManager);
        mRecycleView.setNestedScrollingEnabled(false);
        mRecycleView.setAdapter(mErrorAdapter);
        mRecycleView.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        initDialog();
    }

    /*
    * 初始化dialog
    * */
    private void initDialog() {
        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.pop_check_deal);
        ivRescan = (ImageView) dialog.findViewById(R.id.iv_rescan);    // 重新扫描
        ivUpdate = (ImageView) dialog.findViewById(R.id.iv_update);   // 提交
        tvRfid = (TextView) dialog.findViewById(R.id.tv_rfid); // 货架
        tvGoodsShelf = (TextView) dialog.findViewById(R.id.tv_goods_shelf); // 货架
        tvGoodsFloor = (TextView) dialog.findViewById(R.id.tv_goods_floor); // 货层
        tvGoodsLocation = (TextView) dialog.findViewById(R.id.tv_goods_location); // 货位
        tvMaterialinfo = (TextView) dialog.findViewById(R.id.tv_material); // 物料信息
        tvGoodsRight = (TextView) dialog.findViewById(R.id.tv_goods_right); // 货权信息
        tvGoodsAlias = (TextView) dialog.findViewById(R.id.tv_goods_alias); // 别名
        llConfirm = (LinearLayout) dialog.findViewById(R.id.ll_confirm); // 别名
        etTitle = (EditText) dialog.findViewById(R.id.et_title); // 标题
        etContent = (EditText) dialog.findViewById(R.id.et_content); // 内容
        ivLight = (ImageView) dialog.findViewById(R.id.iv_light); // 灯
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        lp.width = AppUtils.getWindowWidth(getContext().getApplicationContext()) * 10 / 10; // 宽度
        lp.height = AppUtils.getWindowWidth(getContext().getApplicationContext()) * 17 / 10; // 高度
        dialogWindow.setAttributes(lp);
    }

    @Override
    protected void initData() {
        mMaterialList = new ArrayList<>();
        mErrorItems = new ArrayList<>();
        HttpUtil.getCheckPlanList(checkPlanListCallback); //获取计划
    }

    @Override
    protected void bindEvent(final View mContentView) {
        mSpnPlan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mErrorAdapter.getData().clear();
                mErrorAdapter.notifyDataSetChanged();
                if (position != 0) {
                    pkjhID = mMaterialListAdapter.getItem(position).getMaterialCode();
                    HttpUtil.getCheckPlanDetailList(pkjhID, checkDetailListCallback);
                } else {
                    mErrorAdapter.getData().clear();
                    mErrorAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mErrorAdapter.addOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position, RecycleItemBean data) {
                final CheckPlanDetailBean checkPlanDetailBean = (CheckPlanDetailBean) data.getData();
                isItemUpdate=true;
                if (data.getType() == TYPE_CHECK) {
                    if (isStartScan) {
                        dialog.show();
                        tvGoodsShelf.setText(checkPlanDetailBean.getHuoJia());
                        tvGoodsFloor.setText(checkPlanDetailBean.getHuoCeng());
                        tvGoodsLocation.setText(checkPlanDetailBean.getHuoWei());
                        tvMaterialinfo.setText(checkPlanDetailBean.getMaterialName());
                        tvGoodsRight.setText(checkPlanDetailBean.getCompanyName());
                        tvGoodsAlias.setText(checkPlanDetailBean.getIdentityMark());
                        tvRfid.setText(checkPlanDetailBean.getMark1());
                        if (checkPlanDetailBean.isSuccess) { //判断当前是否已经扫描到
                            ivLight.setImageResource(R.drawable.ic_scan_success);
                        } else {
                            ivLight.setImageResource(R.drawable.ic_scan_error);
                        }
                    }
                }
                ivUpdate.setOnClickListener(new View.OnClickListener() { //提交
                    @Override
                    public void onClick(View v) {
                        submitErrorItem(checkPlanDetailBean); //提交当前条目错误信息
                    }
                });
                llConfirm.setOnClickListener(new View.OnClickListener() { //提交
                    @Override
                    public void onClick(View v) {
                        submitErrorItem(checkPlanDetailBean);
                    }
                });
            }
        });
        ivRescan.setOnClickListener(new View.OnClickListener() { //重新扫描
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


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

    private void submitErrorItem(CheckPlanDetailBean checkPlanDetailBean) {
        String mContent = etContent.getText().toString().trim();
        if (TextUtils.isEmpty(etTitle.getText().toString().trim()) || TextUtils.isEmpty(mContent)) {
            showToast("标题和内容不能为空");
            return;
        }
        String userName = SharedPreferencesUtils.getString(getContext(), YiHaiHttpConstaint.USER_NAME);
        HttpUtil.postShelvesError(userName, pkjhID, mContent, 3, inActionCallback);
        checkPlanDetailBean.setSuccess(true);
        dialog.dismiss();
        mErrorAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.ll_qr, R.id.iv_stop,R.id.ll_plan_choice})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_qr: //开始
                if (mSpnPlan.getSelectedItemPosition() == 0) {
                    showToast(getString(R.string.hint_plan_select));
                    return;
                }
                if (!isStartScan) {
                    new AlertDialog.Builder(getContext())
                            .setMessage(R.string.dialog_start_scan)
                            .setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startScan();
                                }
                            })
                            .setNegativeButton(R.string.action_cancel, null)
                            .create().show();
                } else {
                    startScan();
                }
                break;
            case R.id.iv_stop: //结束
                submitSheves();
                break;
            case R.id.ll_plan_choice://sp
                if (mMaterialList == null || mMaterialList.size() == 0) {
                    showToast(getString(R.string.hint_plan_null));
                    return;
                }
                mMaterialListAdapter.setDatas(mMaterialList);
                mSpnPlan.performClick();
                break;
        }
    }

    /*
    * 提交盘库
    * */
    private void submitSheves() {
        if (mSpnPlan.getSelectedItemPosition() == 0) {
            showToast(getString(R.string.hint_plan_select));
            return;
        }
        for (int i = 0; i < mErrorAdapter.getData().size(); i++) {
            if (mErrorAdapter.getData().get(i).getType() == TYPE_CHECK) {
                if (!((CheckPlanDetailBean) mErrorAdapter.getData().get(i).getData()).isSuccess) {
                    showToast("请将所有库存盘点完毕再进行提交");
                    return;
                }
            }
        }
        new AlertDialog.Builder(getContext())
                .setMessage(R.string.dialog_end_scan)
                .setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isItemUpdate=false;
                        MaterialBean selectedItem = (MaterialBean) mSpnPlan.getSelectedItem();
                        HttpUtil.postCheckResult(selectedItem.getMaterialCode(), 2, inActionCallback);
                    }
                })
                .setNegativeButton(R.string.action_cancel, null)
                .create().show();
    }

    /*
    * 开始扫描
    * */
    private void startScan() {
        isStartScan = true;
        mSpnPlan.setEnabled(false);
        if (ZebarHolder.mConnectedDevice == null) {
            skipAct(BluetoothListActivity.class);
        } else {
            ZebarHolder.eventHandler.setHandler(handler);
            showToast("请扫描");
        }
    }

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            String data = msg.getData().getString("data");
            synchronized (mErrorAdapter) {
                if (mErrorAdapter.getData() != null) {
                    boolean isFind = false;//是否查询到RFID
                    for (int i = 0; i < mErrorAdapter.getData().size(); i++) {
                        RecycleItemBean recycleItemBean = mErrorAdapter.getData().get(i);
                        if (recycleItemBean.getType() != TYPE_CHECK) {
                            continue;
                        }
                        CheckPlanDetailBean checkPlanDetailBean = (CheckPlanDetailBean) recycleItemBean.getData();
                        if (checkPlanDetailBean.isSuccess) {
                            continue;
                        }
                        if (checkPlanDetailBean != null) {
                            if (TextUtils.equals(data, checkPlanDetailBean.getMark1())) {
                                isFind = true;
                                num++;
                                tvAlreadyScan.setText("已盘数量:" + num);
                                RecycleItemBean<CheckPlanDetailBean> remove = mErrorAdapter.remove(i);
                                remove.getData().setSuccess(true);
                                mErrorAdapter.add(remove);
                                RecycleItemBean dividerRecycle = mErrorAdapter.remove(i);
                                mErrorAdapter.add(dividerRecycle);
                                break;
                            }
                        }
                    }
                    if (!isFind) {
                        //  showToast("计划列表没有对应的RFID");
                    }
                }
            }
            Log.i("9999999", "22:" + data);
        }
    };

    StringCallback checkPlanListCallback = new StringCallback() {
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
                        CheckPlanBean mcheckPlanBean = gson.fromJson(jsonArray.getJSONObject(i).toString(), CheckPlanBean.class);
                        MaterialBean materialBean=new MaterialBean();
                        materialBean.setMaterialCode(mcheckPlanBean.getID());
                        materialBean.setMaterialName(mcheckPlanBean.getPanKuPlanName());
                        mMaterialList.add(materialBean);
                    }
                }
                //else {
              //      showToast(getString(R.string.hint_plan_null));
             //   }
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
    StringCallback checkDetailListCallback = new StringCallback() {
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
                mErrorItems.clear();
                if (code == 200) {
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    tvNeedScan.setText("预盘数量:" + jsonArray.length());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Gson gson = new Gson();
                        CheckPlanDetailBean checkPlanBean = gson.fromJson(jsonArray.getJSONObject(i).toString(), CheckPlanDetailBean.class);
                        RecycleItemBean<CheckPlanDetailBean> recycleItemBean = new RecycleItemBean<>();
                        recycleItemBean.setType(TYPE_CHECK);
                        recycleItemBean.setData(checkPlanBean);
                        mErrorItems.add(recycleItemBean);
                        RecycleItemBean dividerRecycle = new RecycleItemBean();
                        dividerRecycle.setType(TYPE_DIVIDER_LINE);
                        mErrorItems.add(dividerRecycle);
                    }
                } else if (code == 201) {
                    String msg = jsonObject.getString("msg");
                    showToast(msg);
                }
      //          else {
       //             showToast(getString(R.string.hint_plan_null));
          //      }
                mErrorAdapter.addAll(mErrorItems);
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
                    if (!isItemUpdate){
                        mErrorAdapter.getData().clear();
                        mErrorAdapter.notifyDataSetChanged();
                        mMaterialList.remove(mSpnPlan.getSelectedItemPosition()-1);
                        mMaterialListAdapter.setDatas(mMaterialList);
                        tvNeedScan.setText("预盘数量:");
                        tvAlreadyScan.setText("已盘数量:");
                        num=0;
                        mSpnPlan.setSelection(0);
                    }
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
            if (!isItemUpdate){
                isStartScan = false;
                mSpnPlan.setEnabled(true);
            }
        }
    };


    @OnClick(R.id.ll_plan_choice)
    public void onViewClicked() {
    }
}
