package com.github.yihai.manager;

import com.github.yihai.common.util.SharedPreferencesUtils;
import com.github.yihai.ui.MyApplication;

/**
 * Created by ${sheldon} on 2017/7/11.
 * 公共参数
 */

public interface YiHaiHttpConstaint {
  String APK_CONFIG="config.xml"; //共享参数名
  String ALREADY_LOGIN_FLAG="already_login_flag"; //已登录
  String IS_VISITOR="is_visitor"; //是否为游客
  String IS_NOT_FIRST_LOGIN="is_not_first_login"; //第一次登陆
  String BASE_URL="http://"+ SharedPreferencesUtils.getString(MyApplication.getContext(),YiHaiHttpConstaint.SET_URL);//.
 // String BASE_URL="http://124.128.64.179:8091"; //BASE_URL
 // String BASE_WEB_URL="http://124.128.64.179:8091/WebPage/";
  String BASE_WEB_URL=BASE_URL+"/WebPage/";
  String USER_NAME="user_name";//用户名
  String PWD="pwd";//密码
  String LOGIN_API=BASE_URL+"/YiHaiBeiPinBeiJianInterface/LoginService.ashx";//登陆
 // String LOGIN_API="http://124.128.64.179:8091/APPInterfaceServices/LoginService.ashx";//登陆
  String DEPARTMENT_ID="department_id";//单位id
  String DanWei_ID="danweiid";//放心粮油单位id
  String USER_ID="user_id";//用户身份证号
  String ID="id";//用户id
  String DES_KEY="1234abcd";//DES加密key.
  String SET_URL="SET_URL";//设置url .
}

