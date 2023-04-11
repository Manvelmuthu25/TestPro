package org.chennaimetrorail.appv1.modal.jsonmodal;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by 102525 on 8/21/2017.
 */

public class CurrentNewsResponse {

    @SerializedName("status")
    private String newsstatus;
    @SerializedName("current_location")
    private String current_location;
    @SerializedName("description")
    private String description;
    @SerializedName("icon")
    private String icon;
    @SerializedName("temp")
    private String temp;
    @SerializedName("notifications_list")
    private List<NewsNotificationList> notifications_list;

    public String getNewsstatus() {
        return newsstatus;
    }

    public void setNewsstatus(String newsstatus) {
        this.newsstatus = newsstatus;
    }

    public String getCurrent_location() {
        return current_location;
    }

    public void setCurrent_location(String current_location) {
        this.current_location = current_location;
    }

    public List<NewsNotificationList> getNotifications_list() {
        return notifications_list;
    }

    public void setNotifications_list(List<NewsNotificationList> notifications_list) {
        this.notifications_list = notifications_list;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }
}
