package org.chennaimetrorail.appv1.modal.jsonmodal;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FTTimeResponse {

    @SerializedName("status")
    private String status;
    @SerializedName("firsttrain")
    private String firsttrain;
    @SerializedName("lasttrain")
    private String lasttrain;
    @SerializedName("timetable")
    private List<FTDuration> timetable;
    @SerializedName("generalclassfare")
    private String generalclassfare;
    @SerializedName("specialclassfare")
    private String specialclassfare;
    @SerializedName("distance")
    private String distance;
    @SerializedName("duration")
    private String duration;
    @SerializedName("inbetweenstationcount")
    private String inbetweenstationcount;


    @SerializedName("reason")
    private String reason;






    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setFirsttrain(String firsttrain) {
        this.firsttrain = firsttrain;
    }

    public String getFirsttrain() {
        return firsttrain;
    }

    public void setLasttrain(String lasttrain) {
        this.lasttrain = lasttrain;
    }

    public String getLasttrain() {
        return lasttrain;
    }

    public void setTimetable(List<FTDuration> timetable) {
        this.timetable = timetable;
    }

    public List<FTDuration> getTimetable() {
        return timetable;
    }
    public void setGeneralclassfare(String generalclassfare) {
        this.generalclassfare = generalclassfare;
    }

    public String getGeneralclassfare() {
        return generalclassfare;
    }


    public void setSpecialclassfare(String specialclassfare) {
        this.specialclassfare = specialclassfare;
    }

    public String getSpecialclassfare() {
        return specialclassfare;
    }


    public void setDistance(String distance) {
      this.distance = distance;
    }

    public String getDistance() {
        return distance;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDuration() {
        return duration;
    }



    public void setInbetweenstationcount(String inbetweenstationcount) {
        this.inbetweenstationcount = inbetweenstationcount;
    }

    public String getInbetweenstationcount() {
        return inbetweenstationcount;
    }






    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }


}
