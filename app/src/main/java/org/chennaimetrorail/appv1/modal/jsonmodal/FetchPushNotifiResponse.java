package org.chennaimetrorail.appv1.modal.jsonmodal;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FetchPushNotifiResponse {

    @SerializedName("status")
    private String status;
    @SerializedName("notifications_list")
    private List<NewsNotificationList> notifications_list;

    public String getStatus() {
        return status;
    }

    public void setNotifications_list(List<NewsNotificationList> notifications_list) {
        this.notifications_list = notifications_list;
    }

    public List<NewsNotificationList> getNotifications_list() {
        return notifications_list;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
