package com.github.yihai.manager.callback;

import java.util.List;

/**
 * Created by ${sheldon} on 2017/7/17.
 */

public class LoginBean {

    @Override
    public String toString() {
        return "LoginBean{" +
                "code=" + code +
                ", data=" + data +
                ", msg='" + msg + '\'' +
                '}';
    }

    /**
     * code : 200
     * data : {"result":[{"ChuangJianShiJian":"2016-05-25T10:21:37.88","DanWeiMingCheng":"杨森","ID":4157,"ID_ChuangJianZhe":0,"ID_DiQu":0,"ID_KuQu":"41,4125","ID_ShangJiDanWei":140,"ID_YongHuLeiXing":9,"ID_ZhiShuDanWei":0,"IsGuanLiGongSi":0,"KouLing":"e10adc3949ba59abbe56e057f20f883e","LastLogin":"2017-07-17T08:19:45.57","PaiXuHao":0,"ShangJiGongSi":0,"ShenQingGuanLiYuan":2,"ShunXuHao":0,"State":0,"TouXiang":"","YongHuMing":"372525198307203010","ZuZhiJiGouDaiMa":"","beizhu":"","id_DanWeiGuanLiJueSe":"0","sheng":"0","shi":"0","xian":"0"},{"BiYeYuanXiao":"","ChuShengDiQu":0,"ChuShengDiSheng":16,"ChuShengDiShi":161,"ChuShengNianYue":"","GongZhongID":0,"GuDingDianHua":"0531-87197280","ID":6971,"ID_BianZhiQingKuang":0,"ID_BuMen":763,"ID_DW":140,"ID_DanWei":140,"ID_GangWei":0,"ID_GongZhong":0,"ID_KuQu":"0","ID_XueLi":9,"ID_ZhiYeZiGe":0,"ID_ZhuBiao":0,"JiGuanQu":0,"JiGuanSheng":16,"JiGuanShi":161,"JiGuanXiangXiDiZhi":"","JiShuZhiCheng":"","JieShaoXinZhaoPian":"/upload/default/201605251028577877.jpg","LeiXing":128,"LiTuiXiuZhuangTai":"","LianXiFangShi":"15689699780","MinZu":"汉族","Phone":"15689699780","SheHeDanWeiID":140,"ShenFenZhengHao":"372525198307203010","ShenFenZhengImgPath":"/upload/default/201605251020105660.jpg","ShenHeDanWeiRenYuan":"4157","ShenHeDanWeiShenHeShiJian":"2016-05-25T10:35:30.53","ShenHeZhuangTai":2,"ShiFouZhuanYeRenYuan":0,"XianJuDi":"","XingBie":0,"XingMing":"杨森","XueWei":"","Xueli":0,"XueliLeiXing":9,"YongHuNiCheng":"杨哥","ZhengShuBianHao":"","ZhengZhiMianMao":"0","ZhuanYe":"","isLingDao":false}]}
     * msg : 登录成功！
     */

    private int code;
    private DataBean data;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataBean {
        private List<ResultBean> result;

        public List<ResultBean> getResult() {
            return result;
        }

        public void setResult(List<ResultBean> result) {
            this.result = result;
        }

        public static class ResultBean {
            @Override
            public String toString() {
                return "ResultBean{" +
                        "ID=" + ID +
                        ", LoginName=" + LoginName +
                        ", LoginPwd=" + LoginPwd +
                        '}';
            }

            /**
             * ID : 4157

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

            public void setLoginName(String loginName) {
                LoginName = loginName;
            }

            public String getLoginPwd() {
                return LoginPwd;
            }

            public void setLoginPwd(String loginPwd) {
                LoginPwd = loginPwd;
            }


        }
    }
}
