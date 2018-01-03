package com.github.yihai.bean;

import java.io.Serializable;

/**
 * Created by dzl on 2017/12/13.
 */

public class InPlanBean implements Serializable{
    private String ID;
    private String MaterialCode;
    private String MaterialName;
    private String CompanyCode;
    private String CompanyName;
    private String IdentityMark;
    private String Mark1;
    private String Mark2;
    private String Creator;
    private String CreateTime;
    private String Operator;
    private String OperateTime;
    private int State;
    private String RuKuPlanName;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getMaterialCode() {
        return MaterialCode;
    }

    public void setMaterialCode(String materialCode) {
        MaterialCode = materialCode;
    }

    public String getCompanyCode() {
        return CompanyCode;
    }

    public void setCompanyCode(String companyCode) {
        CompanyCode = companyCode;
    }

    public String getIdentityMark() {
        return IdentityMark;
    }

    public void setIdentityMark(String identityMark) {
        IdentityMark = identityMark;
    }

    public String getMark1() {
        return Mark1;
    }

    public void setMark1(String mark1) {
        Mark1 = mark1;
    }

    public String getMark2() {
        return Mark2;
    }

    public void setMark2(String mark2) {
        Mark2 = mark2;
    }

    public String getCreator() {
        return Creator;
    }

    public void setCreator(String creator) {
        Creator = creator;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public String getOperator() {
        return Operator;
    }

    public void setOperator(String operator) {
        Operator = operator;
    }

    public String getOperateTime() {
        return OperateTime;
    }

    public void setOperateTime(String operateTime) {
        OperateTime = operateTime;
    }

    public int getState() {
        return State;
    }

    public void setState(int state) {
        State = state;
    }

    public String getRuKuPlanName() {
        return RuKuPlanName;
    }

    public void setRuKuPlanName(String ruKuPlanName) {
        RuKuPlanName = ruKuPlanName;
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
}
