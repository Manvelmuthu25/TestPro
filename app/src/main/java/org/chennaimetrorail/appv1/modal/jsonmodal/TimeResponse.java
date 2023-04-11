package org.chennaimetrorail.appv1.modal.jsonmodal;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by 102525 on 7/17/2017.
 */

public class TimeResponse {

    @SerializedName("status")
    private String status;
    @SerializedName("timetable")
    private List<Times> timetable;

    @SerializedName("reason")
    private String reason;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Times> getTimetable() {
        return timetable;
    }

    public void setTimetable(List<Times> timetable) {
        this.timetable = timetable;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
