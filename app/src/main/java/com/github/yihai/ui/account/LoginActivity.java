package com.github.yihai.ui.account;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.yihai.R;
import com.github.yihai.base.BaseActivity;
import com.github.yihai.common.util.AppUtils;
import com.github.yihai.common.util.MD5Tools;
import com.github.yihai.common.util.SharedPreferencesUtils;
import com.github.yihai.manager.YiHaiHttpConstaint;
import com.github.yihai.manager.callback.JsonCallback;
import com.github.yihai.manager.callback.LoginCallBackBean;
import com.github.yihai.ui.main.MainActivity;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dzl on 2017/12/12.
 */

public class LoginActivity extends BaseActivity {

    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.tv_fotget_password)
    TextView tvFotgetPassword;
    @BindView(R.id.tv_register)
    TextView tvRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @Override
    public void initView() {
    }

    @Override
    public void initData() {
        String userName = AppUtils.getUserName(this);
        if (!TextUtils.isEmpty(userName)) {
            etUsername.setText(userName);
        }
    }

    @Override
    public void bindEvent() {

    }

    @OnClick({R.id.btn_login, R.id.tv_fotget_password})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                String mName = etUsername.getText().toString().trim();
                String mPassWord = etPassword.getText().toString().trim();
                if (TextUtils.isEmpty(mName)) {
                    Toast.makeText(this, "用户名不能为空", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(mPassWord)) {
                    Toast.makeText(this, "密码不能为空", Toast.LENGTH_LONG).show();
                    return;
                }
                String md5 = MD5Tools.getMD5(mPassWord);
                getLoginData(mName, md5);
                break;
            case R.id.tv_fotget_password:
                break;
        }
    }

    private void getLoginData(String name, String pwd) {
        OkGo.<LoginCallBackBean>post(YiHaiHttpConstaint.BASE_URL + "/LoginService.ashx").tag(this).params("UserName", name)
                .params("Password", pwd).execute(new JsonCallback<LoginCallBackBean>(LoginCallBackBean.class) {
            @Override
            public void onSuccess(Response<LoginCallBackBean> response) {
                hideProgress();
                if (response.body().getCode() == 200) {
                    getCallBack(response.body().getResult().get(0));
                } else if (response.body().getCode() == 201) {
                    showToast("登录错误!" + response.body().getMsg());
                }
            }

            @Override
            public void onError(Response<LoginCallBackBean> response) {
                super.onError(response);
                hideProgress();
                if (response.body() != null) {
                    showToast("登录失败!" + response.body().getMsg() == null ? "" : response.body().getMsg());
                } else
                    showToast(getString(R.string.hint_net_error));
            }

            @Override
            public void onStart(Request<LoginCallBackBean, ? extends Request> request) {
                super.onStart(request);
                mProgressDialog.setCanceledOnTouchOutside(false);
                showProgress("正在登录");

            }
        });
    }

    /*
    * 解析数据
    * */
    private void getCallBack(LoginCallBackBean.ResultBean loginCallBackBean) {
        putLogin(loginCallBackBean);
//        boolean isNotFirst = SharedPreferencesUtils.getBoolean(getBaseContext(),
//                YiHaiHttpConstaint.ALREADY_LOGIN_FLAG);//获取是否登录
//        if (isNotFirst) {
//            SharedPreferencesUtils.putBoolean(getBaseContext(),
//                    YiHaiHttpConstaint.ALREADY_LOGIN_FLAG, true);//是否登录
//        }
        skipAct(MainActivity.class);
        finish();
    }

    private void putLogin(LoginCallBackBean.ResultBean loginCallBackBean) {

        String userName = loginCallBackBean.getLoginName();
        int id = loginCallBackBean.getID();

        SharedPreferencesUtils.putString(getBaseContext(), YiHaiHttpConstaint.USER_NAME, userName);
        SharedPreferencesUtils.putString(getBaseContext(), YiHaiHttpConstaint.ID, id + "");

    }

    @OnClick(R.id.tv_register)
    public void onViewClicked() { //弹出
        inputTitleDialog();
    }


    /*
    * 设置弹出输入框
    * */
    private void inputTitleDialog() {
        final EditText inputServer = new EditText(this);
        inputServer.setFocusable(true);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("设置ip").setView(inputServer).setNegativeButton(
                "取消", null);
        builder.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String inputName = inputServer.getText().toString();
                        SharedPreferencesUtils.putString(getBaseContext(),YiHaiHttpConstaint.SET_URL,inputName);
                    }
                });
        builder.show();
    }
}
