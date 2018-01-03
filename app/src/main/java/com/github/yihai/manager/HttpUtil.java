package com.github.yihai.manager;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

/**
 * Created by dzl on 2017/12/13.
 */

public class HttpUtil {
    /**
     * 获取物料列表
     *
     * @param callback
     * @Author dzl
     */
    public static void getMaterialList(StringCallback callback) {
        OkGo.<String>post(YiHaiHttpConstaint.BASE_URL + "/CommonServices/GetMaterialService.ashx")
                .tag("getMaterialList")
                .execute(callback);
    }

    /**
     * 获取货权列表
     *
     * @param callback
     * @Author dzl
     */
    public static void getGoodsRightList(StringCallback callback) {
        OkGo.<String>post(YiHaiHttpConstaint.BASE_URL + "/CommonServices/GetCompanyService.ashx")
                .tag("getGoodsRightList")
                .execute(callback);
    }

    /**
     * 提交盘库结果
     *
     * @param callback
     * @Author dzl
     */
    public static void postCheckResult(String PanKuID,int State,StringCallback callback) {
        OkGo.<String>post(YiHaiHttpConstaint.BASE_URL + "/PanKuServices/PanKuPlanUpdateService.ashx")
                .tag("postCheckResult").params("PanKuID",PanKuID).params("State",State)
                .execute(callback);
    }

    /**
     * 获取入库计划列表
     *
     * @param callback
     * @Author dzl
     */
    public static void getInPlanList(StringCallback callback) {
        OkGo.<String>post(YiHaiHttpConstaint.BASE_URL + "/CommonServices/GetPlanService.ashx")
                .tag("getInPlanList")
                .execute(callback);
    }

    /**
     * 获取出库计划列表
     *
     * @param callback
     * @Author dzl
     */
    public static void getOutPlanList(StringCallback callback) {
        OkGo.<String>post(YiHaiHttpConstaint.BASE_URL + "/ChuKuServices/GetChuKuPlanService.ashx")
                .tag("getOutPlanList")
                .execute(callback);
    }

    /**
     * 获取盘库计划列表
     * <p>
     * ID
     * PanKuPlanName
     * Creator
     * CreateTime
     * PKType
     * UnionID
     * CompanyCode
     * MaterialCode
     * Operator
     * OperateTime
     * State
     *
     * @param callback
     * @Author dzl
     */
    public static void getCheckPlanList(StringCallback callback) {
        OkGo.<String>post(YiHaiHttpConstaint.BASE_URL + "/PanKuServices/GetPanKuPlanService.ashx")
                .tag("getCheckPlanList")
                .execute(callback);
    }

    /**
     * 获取货架列表
     *
     * @param callback
     * @Author dzl
     */
    public static void getGoodsShelfList(StringCallback callback,int Type) {
        OkGo.<String>post(YiHaiHttpConstaint.BASE_URL + "/CommonServices/GetHuoJianService.ashx")
                .tag("getGoodsShelfList").params("Type",Type)
                .execute(callback);
    }

    /**
     * 获取货层列表
     *
     * @param callback
     * @Author dzl
     */
    public static void getGoodsFloorList(StringCallback callback,int Type) {
        OkGo.<String>post(YiHaiHttpConstaint.BASE_URL + "/CommonServices/GetHuoCengService.ashx")
                .tag("getGoodsFloorList").params("Type",Type)
                .execute(callback);
    }

    /**
     * 获取货位列表
     *
     * @param callback
     * @Author dzl
     */
    public static void getGoodsLocationList(StringCallback callback,int Type) {
        OkGo.<String>post(YiHaiHttpConstaint.BASE_URL + "/CommonServices/GetHuoWeiService.ashx")
                .tag("getGoodsLocationList").params("Type",Type)
                .execute(callback);
    }




    /**
     * 根据物料获取详情
     *
     * @param callback
     * @Author dzl
     */
    public static void getGoodsDetail(String MaterialCode, StringCallback callback) {
        OkGo.<String>post(YiHaiHttpConstaint.BASE_URL + "/ChuKuServices/GetPlanByMaterialService.ashx")
                .tag("getGoodsDetail")
                .params("MaterialCode", MaterialCode)
                .execute(callback);
    }



    /**
     * 获取盘库列表
     *
     * @param PlanID
     * @param callback
     * @Author dzl
     */
    public static void getCheckPlanDetailList(String PlanID, StringCallback callback) {
        OkGo.<String>post(YiHaiHttpConstaint.BASE_URL + "/PanKuServices/GetPanKuPlanDetailService.ashx")
                .tag("getCheckPlanDetailList")
                .params("PlanID",PlanID)
                .execute(callback);
    }

    /**
     * 创建入库计划
     * 参数说明：
     * UserName：用户名
     * MaterialCode：物料代码
     * CompanyCode：货权公司代码
     * RuKuPlanName：入库计划
     * IdentityMark：标识名（可为空）
     * Mark1：RFID扫描结果（可为空）
     * Mark2：二维码扫描结果（可为空）
     *
     * @param callback
     * @Author dzl
     */
    public static void postCreateInPlan(String userName, String materialCode,
                                        String commpanyCode, String identityMark,
                                        String rfid, String qrcode, String RuKuPlanName,
                                        StringCallback callback) {
        OkGo.<String>post(YiHaiHttpConstaint.BASE_URL + "/RuKuServices/RuKuPlanService.ashx")
                .tag("postCreateInPlan")
                .params("UserName", userName)
                .params("RuKuPlanName", RuKuPlanName)
                .params("MaterialCode", materialCode)
                .params("CompanyCode", commpanyCode)
                .params("IdentityMark", identityMark)
                .params("Mark1", rfid)
                .params("Mark2", qrcode)
                .execute(callback);
    }

    /**
     * 创建出库计划
     * 参数说明：
     * UserName：用户名
     * ChuKuPlanName：出库计划名称
     * MaterialCode：物料代码
     * CompanyCode：货权代码
     * Stock_ID：实时库存ID
     *
     * @param callback
     */
    public static void postCreateOutPlan(String userName, String ChuKuPlanName,
                                         String materialCode, String CompanyCode,
                                         String Stock_ID, StringCallback callback) {
        OkGo.<String>post(YiHaiHttpConstaint.BASE_URL + "/ChuKuServices/ChuKuPlanService.ashx")
                .tag("postCreateOutPlan")
                .params("UserName", userName)
                .params("ChuKuPlanName", ChuKuPlanName)
                .params("MaterialCode", materialCode)
                .params("CompanyCode", CompanyCode)
                .params("Stock_ID", Stock_ID)
                .execute(callback);
    }

    /**
     * 创建盘库计划
     * 参数说明：
     * UserName：用户名
     * PanKuPlanName：盘库计划名称
     * MaterialCode：物料代码
     * CompanyCode：货权代码
     * Type：盘点类型
     * TypeID：盘点类型对应ID
     *
     * @param callback
     * @Author dzl
     */
    @Deprecated
    public static void postCreateCheckPlan(String userName, String PanKuPlanName,
                                           String MaterialCode, String CompanyCode,
                                           String Type, String TypeID,
                                           StringCallback callback) {
        OkGo.<String>post(YiHaiHttpConstaint.BASE_URL + "/PanKuServices/PanKuPlanService.ashx")
                .tag("postCreateOutPlan")
                .params("UserName", userName)
                .params("PanKuPlanName", PanKuPlanName)
                .params("MaterialCode", MaterialCode)
                .params("CompanyCode", CompanyCode)
                .params("Type", Type)
                .params("TypeID", TypeID)
                .execute(callback);
    }

    /**
     * 创建盘库计划
     * 参数说明：
     * UserName：用户名
     * PanKuPlanName：盘库计划名称
     * MaterialCode：物料代码
     * CompanyCode：货权代码
     * Type：盘点类型
     * TypeID：盘点类型对应ID
     *
     * @param callback
     * @Author dzl
     */
    @Deprecated
    public static void postCreateCheckPlan(String userName, String PanKuPlanName,
                                           String MaterialCode, String CompanyCode,
                                           String HuoJia, String HuoCeng, String HuoWei,
                                           StringCallback callback) {
        OkGo.<String>post(YiHaiHttpConstaint.BASE_URL + "/PanKuServices/PanKuPlanService.ashx")
                .tag("postCreateOutPlan")
                .params("UserName", userName)
                .params("PanKuPlanName", PanKuPlanName)
                .params("MaterialCode", MaterialCode)
                .params("CompanyCode", CompanyCode)
                .params("HuoJia", HuoJia)
                .params("HuoCeng", HuoCeng)
                .params("HuoWei", HuoWei)
                .execute(callback);
    }

    /**
     * 货物上架
     * 参数说明：
     * UserName：用户姓名
     * PlanID：入库计划ID
     * HuoWeiID：货位ID
     *
     * @param callback
     * @Author dzl
     */
    public static void postInAction(String userName, String PlanID, String HuoWeiID,
                                    StringCallback callback) {
        OkGo.<String>post(YiHaiHttpConstaint.BASE_URL + "/RuKuServices/RuKuPlanUpdateService.ashx")
                .tag("postInAction")
                .params("UserName", userName)
                .params("PlanID", PlanID)
                .params("HuoWeiID", HuoWeiID)
                .execute(callback);
    }
    /**
     * 出库确认
     * 参数说明：
     * PlanID：出库计划ID
     * State：状态（详情请参考文档中的数据库表）
     * @param
     * @Author zyq
     */
    public static void postOutUpdate(String PlanID, int state,
                                    StringCallback callback) {
        OkGo.<String>post(YiHaiHttpConstaint.BASE_URL + "/ChuKuServices/ChuKuPlanUpdateService.ashx")
                .tag("postOutUpdate")
                .params("PlanID", PlanID)
                .params("State", state)
                .execute(callback);
    }

    /**
      *  盘库错误记录
     */
    public static void postShelvesError(String UserName,String PKJHID, String Note,int state,
                                     StringCallback callback) {
        OkGo.<String>post(YiHaiHttpConstaint.BASE_URL + "/PanKuServices/PanKuRecordService.ashx")
                .tag("postShelvesError")
                .params("UserName", UserName).params("PKJHID",PKJHID).params("Note",Note)
                .params("State", state)
                .execute(callback);
    }
    /**
     *
     * 获取查询结果列表
     *
     * @param callback
     */
    public static void getSearchResultList(StringCallback callback, String materialCode, String companyCode) {
        OkGo.<String>post(YiHaiHttpConstaint.BASE_URL + "/ChaXunServices/HuoWuChaXunService.ashx")
                .tag("getSearchResultList")
                .params("MaterialCode", materialCode)
                .params("CompanyCode", companyCode)
                .execute(callback);
    }


}
