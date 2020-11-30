package com.kuxuan.moneynote.api;


import com.kuxuan.moneynote.json.BaseJson;
import com.kuxuan.moneynote.json.BillJson;
import com.kuxuan.moneynote.json.CategoryJson;
import com.kuxuan.moneynote.json.ChartData;
import com.kuxuan.moneynote.json.ExportBillBean;
import com.kuxuan.moneynote.json.FindJson;
import com.kuxuan.moneynote.json.LoginJson;
import com.kuxuan.moneynote.json.MineJson;
import com.kuxuan.moneynote.json.NewCategoryJson;
import com.kuxuan.moneynote.json.NewChartData;
import com.kuxuan.moneynote.json.UploadBeanJson;
import com.kuxuan.moneynote.json.UserAllBillJson;
import com.kuxuan.moneynote.json.UserJson;
import com.kuxuan.moneynote.json.netbody.AllCategoryBody;
import com.kuxuan.moneynote.json.netbody.BillBody;
import com.kuxuan.moneynote.json.netbody.BindBody;
import com.kuxuan.moneynote.json.netbody.CategoryBody;
import com.kuxuan.moneynote.json.netbody.ChartBody;
import com.kuxuan.moneynote.json.netbody.CheckMobileBody;
import com.kuxuan.moneynote.json.netbody.CustomCategoryBody;
import com.kuxuan.moneynote.json.netbody.DeleteBody;
import com.kuxuan.moneynote.json.netbody.ExportBill;
import com.kuxuan.moneynote.json.netbody.LoginBody;
import com.kuxuan.moneynote.json.netbody.MessageBody;
import com.kuxuan.moneynote.json.netbody.NewChartBody;
import com.kuxuan.moneynote.json.netbody.PersonBody;
import com.kuxuan.moneynote.json.netbody.RegisterBody;
import com.kuxuan.moneynote.json.netbody.SendCodeBody;
import com.kuxuan.moneynote.json.netbody.SkinBean;
import com.kuxuan.moneynote.json.netbody.UpDataBody;
import com.kuxuan.moneynote.json.netbody.UserAllBillBody;
import com.kuxuan.moneynote.json.netbody.WeChatJson;
import com.kuxuan.moneynote.json.netbody.YearBody;

import java.util.ArrayList;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by Android Studio,网络访问类
 *
 * @author Xieshengqi
 *         Date：2016/9/26.
 */
public interface ApiService {


    /**
     * 发送验证码
     *
     *               //     * @param code_type 验证码类型 1 短信验证码 2 语音验证码
     *               //     * @param send_type 1：用户注册，2：动态用户和更改密码
     */
    @POST("sendCode")
    Observable<BaseJson<Object>> sendCode(@Body SendCodeBody sendCodeBody);
//
//    /**
//     * 校验验证码
//     *
//     * @param mobile       手机号
//     * @param validateCode validateCode
//     * @return object
//     */
//    @POST("validateCode")
//    Observable<BaseJson<Object>> validateCode(@Query("mobile") String mobile, @Query("verifyCode") String validateCode, @Query("verify") String verify);

    /**
     * 获取userid
     *
     * @return
     */
    @POST("getId")
    Observable<BaseJson<UserJson>> getUserId();

//    /**
//     * 注册
//     *
//     * @param mobile    手机号
//     * @param password  密码
//     * @param cPassword 确认密码
//     * @param code      验证码
//     * @return object
//     */
//    @POST("register")
//    Observable<BaseJson<LoginJson>> register(@Body RegisterBody registerBody);
    /**
     * 注册
     *
     * @param registerBody registerBody
     * @return object
     */
    @POST("register")
    Observable<BaseJson<LoginJson>> register(@Body RegisterBody registerBody);

    /**
     * 检测手机号是否被注册/绑定
     *
     * @param body body
     * @return object
     */
    @POST("checkMobile")
    Observable<BaseJson<Object>> checkMobile(@Body CheckMobileBody body);

    /**
     * 登录
     *
     * @param loginBody loginBody
     * @return object
     */
    @POST("login")
    Observable<BaseJson<LoginJson>> login(@Body LoginBody loginBody);

    /**
     * 找回密码
     *
     * @param registerBody registerBody
     * @return object
     */
    @POST("resetPassword")
    Observable<BaseJson<Object>> findPwd(@Body RegisterBody registerBody);

    /**
     * 用户账单明细接口(首页明细)
     *
     * @param billBody billBody
     * @return object
     */
    @POST("getUserAllBill")
    Observable<BaseJson<UserAllBillJson>> getUserAllBill(@Body UserAllBillBody billBody);

    /**
     * 删除账单接口
     *
     * @param body 删除实体类
     * @return object
     */
    @POST("deleteUserBill")
    Observable<BaseJson<Object>> deleteUserBill(@Body DeleteBody body);

    /**
     * 获取图表数据
     *
     * @param body 图表实体类
     * @return object
     */
    @POST("getChartsData")
    Observable<BaseJson<ArrayList<ChartData>>> getChartData(@Body ChartBody body);


    /**
     * 获取时间头（报表数据）
     *
     * @param body 图表实体类
     * @return object
     */
    @POST("getTimeHeader")
    Observable<BaseJson<ArrayList<NewChartData>>> getChartNewData(@Body ChartBody body);

    /**
     * 通过时间获取数据
     *
     * @param chartBody
     * @return
     */
    @POST("getTimeData")
    Observable<BaseJson<NewCategoryJson>> getChartNewCategoryData(@Body NewChartBody chartBody);

    /**
     * 获取个人中心数据
     *
     * @return object
     */
    @POST("personalCenter")
    Observable<BaseJson<MineJson>> getMineData();


    /**
     * 获取账单数据
     *
     * @param yearBody 年实体类
     * @return object
     */
    @POST("getMyBill")
    Observable<BaseJson<BillJson>> getMineBillData(@Body YearBody yearBody);

    /**
     * 获取全部类别
     *
     * @return object
     */
    @POST("allcategorys")
    Observable<BaseJson<CategoryJson>> getCategoryData(@Body AllCategoryBody body);

    /**
     * 获取全部类别（没登录）
     * @return
     */
    @POST("allcategorysnoLogin")
    Observable<BaseJson<CategoryJson>> getCategoryDataNoLogin(@Body AllCategoryBody body);


    @POST("getSkin")
    Observable<SkinBean> getSkinData();


    @GET
    Call<ResponseBody> downloadPic(@Url String fileUrl);


    /**
     * 移除记账类别
     *
     * @param categoryBody 类别实体类
     * @return object
     */
    @POST("removeCategory")
    Observable<BaseJson<Object>> removeCategory(@Body CategoryBody categoryBody);

    /**
     * 添加类别
     *
     * @param categoryBody 类别实体类
     * @return object
     */

    @POST("addSystemCategory")
    Observable<BaseJson<Object>> addCategory(@Body CategoryBody categoryBody);


    /**
     * 系统类别删除
     *
     * @param categoryBody
     * @return
     */
    @POST("delSystemCategory")
    Observable<BaseJson<Object>> delsystemCategory(@Body CategoryBody categoryBody);

    /**
     * 自定义类别删除
     *
     * @param categoryBody
     * @return
     */
    @POST("delCustomCategory")
    Observable<BaseJson<Object>> delCustormCategory(@Body CustomCategoryBody categoryBody);

    /**
     * 添加自定义记账类别
     *
     * @param categoryBody 类别实体类
     * @return object
     */
    @POST("addCustomCategory")
    Observable<BaseJson<Object>> addCustomCategory(@Body CustomCategoryBody categoryBody);

    /**
     * 修改头像
     *
     * @param part 头像数据
     * @return object
     */
    @Multipart
    @POST("updatePersonalCenter")
    Observable<BaseJson<Object>> updateAvatar(@Part MultipartBody.Part part);

    /**
     * 修改个人信息
     *
     * @param personBody 个人信息实体类
     * @return object
     */
    @POST("updatePersonalCenter")
    Observable<BaseJson<Object>> updatePersonData(@Body PersonBody personBody);

    /**
     * 绑定手机
     *
     * @return object
     */
    @POST("bindMobile")
    Observable<BaseJson<Object>> bindPhone(@Body BindBody bindBody);
    /**
     * 记账
     *
     * @param billBody 账单实体类
     * @return object
     */
    @POST("addUserBill")
    Observable<BaseJson<Object>> addBill(@Body BillBody billBody);

    /**
     * 修改账单
     *
     * @param billBody 账单实体类
     * @return object
     */
    @POST("updateUserBill")
    Observable<BaseJson<Object>> uploadBill(@Body BillBody billBody);

    /**
     * 获取发现列表
     *
     * @return object
     */
    @POST("getFindList")
    Observable<BaseJson<ArrayList<FindJson>>> getFindLists();


    /**
     * 微信登录
     *
     * @param weChatJson 实体类
     * @return object
     */
    @POST("weChatLogin")
    Observable<BaseJson<LoginJson>> weChatLogin(@Body WeChatJson weChatJson);

    /**
     * 微信绑定
     *
     * @param weChatJson 实体类
     * @return object
     */
    @POST("bindWeChat")
    Observable<BaseJson<Object>> bindWeChat(@Body WeChatJson weChatJson);

    /**
     * 添加用户信息
     *
     * @param messageBody 实体类
     * @return object
     */
    @POST("feedBack")
    Observable<BaseJson<Object>> addUserMessage(@Body MessageBody messageBody);


    /**
     * 导出账单
     */
    @POST("applyExport")
    Observable<ExportBill> exportBill(@Body ExportBillBean exportBillBean);


    /**
     * 登出
     *
     * @return object
     */
    @POST("logout")
    Observable<BaseJson<Object>> logOut();

    /**
     * 修改密码
     *
     * @param oldPassword 旧密码
     * @param password    新密码
     * @param cPassword   确认密码
     * @return object
     */
    @POST("updatePassword")
    Observable<BaseJson<Object>> updatePassword(
                                                @Query("old_password") String oldPassword,
                                                @Query("password") String password,
                                                @Query("c_password") String cPassword);


    /**
     * 检测类别下是否有账单
     *
     * @param categoryId 账单id
     * @return object
     */
    @POST("checkBillByCategoryId")
    Observable<BaseJson<Object>> checkBillByCategoryId(@Query("category_id") String categoryId);


    /**
     * 下载数据（同步）
     *
     * @param page
     * @return
     */
    @POST("downData")
    Observable<BaseJson<UploadBeanJson>> getDownLoadData(@Query("maxId") int page);

    /**
     * 上传数据
     *
     * @param dataBody
     * @return
     */
    @POST("upData")
    Observable<BaseJson<Object>> upData(@Body UpDataBody dataBody);


    /**
     * 设置预算
     * @param month_budget
     * @return
     */
    @POST("setMonthBudget")
    Observable<BaseJson<Object>> setBudgetData(@Query("month_budget")String month_budget);

    @POST("getNewToken")
    Observable<BaseJson<Object>> getNewToken();

}




