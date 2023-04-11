package org.chennaimetrorail.appv1.modal.jsonmodal;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 102525 on 8/21/2017.
 */

public class NewsNotificationList {
    @SerializedName("os")
    private String os;
    @SerializedName("messageid")
    private String messageid;
    @SerializedName("messagetext")
    private String messagetext;
    @SerializedName("noti_dd")
    private String noti_dd;
    @SerializedName("noti_mmm")
    private String noti_mmm;
    @SerializedName("createdtime")
    private String createdtime;
    @SerializedName("noti_date")
    private String noti_date;
    @SerializedName("noti_time")
    private String noti_time;

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getMessageid() {
        return messageid;
    }

    public void setMessageid(String messageid) {
        this.messageid = messageid;
    }

    public String getMessagetext() {
        return messagetext;
    }

    public void setMessagetext(String messagetext) {
        this.messagetext = messagetext;
    }

    public String getNoti_dd() {
        return noti_dd;
    }

    public void setNoti_dd(String noti_dd) {
        this.noti_dd = noti_dd;
    }

    public String getNoti_mmm() {
        return noti_mmm;
    }

    public void setNoti_mmm(String noti_mmm) {
        this.noti_mmm = noti_mmm;
    }

    public String getCreatedtime() {
        return createdtime;
    }

    public void setCreatedtime(String createdtime) {
        this.createdtime = createdtime;
    }


    public String getNoti_date() {
        return noti_date;
    }

    public void setNoti_date(String noti_date) {
        this.noti_date = noti_date;
    }

    public String getNoti_time() {
        return noti_time;
    }

    public void setNoti_time(String noti_time) {
        this.noti_time = noti_time;
    }
}
