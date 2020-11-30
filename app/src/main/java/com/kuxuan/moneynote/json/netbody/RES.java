package com.kuxuan.moneynote.json.netbody;

/**
 * Created by Allence on 2018/4/25 0025.
 */

public class RES {

    //        {
    //               "banner": "http://adminsuishouji.oss-cn-beijing.aliyuncs.com/Uploads/20171228/1514446901392766.png",
    //                "color": "0x578FFA ",
    //                "mine_banner": "http://adminsuishouji.oss-cn-beijing.aliyuncs.com/Uploads/20171227/1514346854943974.png",
    //                "preview_img": "http://adminsuishouji.oss-cn-beijing.aliyuncs.com/Uploads/20171228/1514448652986807.png"
    //        }
    String banner;
    String color;
    String mine_banner;
    String preview_img;

    boolean isDownload;


    public boolean isDownload() {
        return isDownload;
    }

    public void setDownload(boolean download) {
        isDownload = download;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getMine_banner() {
        return mine_banner;
    }

    public void setMine_banner(String mine_banner) {
        this.mine_banner = mine_banner;
    }

    public String getPreview_img() {
        return preview_img;
    }

    public void setPreview_img(String preview_img) {
        this.preview_img = preview_img;
    }
}
