package com.kuxuan.sqlite.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

/**
 * Created by xieshengqi on 2018/3/27.
 */

@Entity(indexes = {@Index(value = "bill_id, user_id", unique = true)})
public class CategoryDB {


    /*
  bill_id  long  账单id(唯一主键）
 demo  String  账单备注
 name  String  账单名字
 type  int     账单类型（收入还是支出）
 accout  long  账单金额
 category_id   类别id
 year  int     账单年
 month  int    账单月
 day    int    账单日
 user_id  int  用户id
    * */


    //
    @Id(autoincrement = true)
    private Long id;
    /***
     * 账单id（规则：year+month+day+ff+mm+ss+毫秒+4位随机数）
     */
    private String bill_id;


    /**
     * 账单备注
     */
    private String demo;

    /**
     * 账单名字
     */
    private String name;


    /**
     * 账单类型（收入还是支出）
     */
    private int type;

    /**
     * 类别图片
     */
    private String image_path;
    /**
     * 账单金额
     */
    private double account;

    /**
     * 类别id
     */
    private int category_id;

    /**
     * 账单年
     */
    private int year;
    /**
     * 账单月
     */
    private int month;
    /**
     * 账单日
     */
    private int day;


    /**
     * 创建时间
     */
    private long createTime;

    /**
     * 更新时间
     */
    private long updateTime;


    /**
     * 日期（账单存储日期）
     */
    private long time;
    




    /**
     * 是否删除
     */
    private int status;


    /**
     * 账单用户id(如果账单id不等于-1的时候，说明这个账单是有用户的，不然就是离线记账没登录的)
     */
    private int user_id;


    /**
     * 是否需要同步
     */
    private boolean isNeedSync;


    @Generated(hash = 1744030977)
    public CategoryDB(Long id, String bill_id, String demo, String name, int type,
            String image_path, double account, int category_id, int year, int month, int day,
            long createTime, long updateTime, long time, int status, int user_id,
            boolean isNeedSync) {
        this.id = id;
        this.bill_id = bill_id;
        this.demo = demo;
        this.name = name;
        this.type = type;
        this.image_path = image_path;
        this.account = account;
        this.category_id = category_id;
        this.year = year;
        this.month = month;
        this.day = day;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.time = time;
        this.status = status;
        this.user_id = user_id;
        this.isNeedSync = isNeedSync;
    }


    @Generated(hash = 2085389353)
    public CategoryDB() {
    }


    public String getBill_id() {
        return this.bill_id;
    }


    public void setBill_id(String bill_id) {
        this.bill_id = bill_id;
    }


    public String getDemo() {
        return this.demo;
    }


    public void setDemo(String demo) {
        this.demo = demo;
    }


    public String getName() {
        return this.name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public int getType() {
        return this.type;
    }


    public void setType(int type) {
        this.type = type;
    }


    public String getImage_path() {
        return this.image_path;
    }


    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }


    public double getAccount() {
        return this.account;
    }


    public void setAccount(double account) {
        this.account = account;
    }


    public int getCategory_id() {
        return this.category_id;
    }


    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }


    public int getYear() {
        return this.year;
    }


    public void setYear(int year) {
        this.year = year;
    }


    public int getMonth() {
        return this.month;
    }


    public void setMonth(int month) {
        this.month = month;
    }


    public int getDay() {
        return this.day;
    }


    public void setDay(int day) {
        this.day = day;
    }


    public long getCreateTime() {
        return this.createTime;
    }


    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }


    public long getUpdateTime() {
        return this.updateTime;
    }


    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }


    public int getStatus() {
        return this.status;
    }


    public void setStatus(int status) {
        this.status = status;
    }


    public int getUser_id() {
        return this.user_id;
    }


    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }


    public boolean getIsNeedSync() {
        return this.isNeedSync;
    }


    public void setIsNeedSync(boolean isNeedSync) {
        this.isNeedSync = isNeedSync;
    }


    public Long getId() {
        return this.id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    @Override
    public String toString() {
        return "CategoryDB{" +
                "id=" + id +
                ", bill_id='" + bill_id + '\'' +
                ", demo='" + demo + '\'' +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", image_path='" + image_path + '\'' +
                ", account=" + account +
                ", category_id=" + category_id +
                ", year=" + year +
                ", month=" + month +
                ", day=" + day +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", status=" + status +
                ", user_id=" + user_id +
                ", isNeedSync=" + isNeedSync +
                '}';
    }


    public long getTime() {
        return this.time;
    }


    public void setTime(long time) {
        this.time = time;
    }
}
