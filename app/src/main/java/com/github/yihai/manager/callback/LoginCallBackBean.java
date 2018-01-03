package com.github.yihai.manager.callback;

import java.util.List;

/**
 * Created by ${sheldon} on 2017/7/11.
 */

public class LoginCallBackBean {

    /**
     * code : 200
     * msg : 获取用户信息成功！
     * result : [{"ID":1,"LoginName":"admin","LoginPwd":"202cb962ac59075b964b07152d234b70"}]
     */

    private int code;
    private String msg;
    private List<ResultBean> result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * ID : 1
         * LoginName : admin
         * LoginPwd : 202cb962ac59075b964b07152d234b70
         */

        private int ID;
        private String LoginName;
        private String LoginPwd;

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public String getLoginName() {
            return LoginName;
        }

        public void setLoginName(String LoginName) {
            this.LoginName = LoginName;
        }

        public String getLoginPwd() {
            return LoginPwd;
        }

        public void setLoginPwd(String LoginPwd) {
            this.LoginPwd = LoginPwd;
        }
    }
}
