package com.github.yihai.bean;

/**
 * Created by zyq on 2017/12/13.
 */

public class SearchResultBean {
    private String HW_NO;    ;
    private String HC_NO;
    private String HJ_NO;
    private String MaterialName;
    private String CompanyName;
    private String IdentityMark;

    public String getHW_NO() {
        return HW_NO;
    }

    public void setHW_NO(String HW_NO) {
        this.HW_NO = HW_NO;
    }

    public String getHC_NO() {
        return HC_NO;
    }

    public void setHC_NO(String HC_NO) {
        this.HC_NO = HC_NO;
    }

    public String getHJ_NO() {
        return HJ_NO;
    }

    public void setHJ_NO(String HJ_NO) {
        this.HJ_NO = HJ_NO;
    }

    public String getMaterialName() {
        return MaterialName;
    }

    public void setMaterialName(String materialName) {
        MaterialName = materialName;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public String getIdentityMark() {
        return IdentityMark;
    }

    public void setIdentityMark(String identityMark) {
        IdentityMark = identityMark;
    }


}
