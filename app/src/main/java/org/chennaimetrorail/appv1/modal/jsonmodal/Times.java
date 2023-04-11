package org.chennaimetrorail.appv1.modal.jsonmodal;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by 102525 on 7/17/2017.
 */

public class Times {

    @SerializedName("towards")
    private String towards;
    @SerializedName("firsttrain")
    private String firsttrain;
    @SerializedName("lasttrain")
    private String lasttrain;
    @SerializedName("timing")
    private List<TimeDuration> timing;


    public Times(String towards, List<TimeDuration> timing) {
        this.towards = towards;
        this.timing = timing;

    }

    public String getTowards() {
        return towards;
    }

    public void setTowards(String towards) {
        this.towards = towards;
    }

    public List<TimeDuration> getTiming() {
        return timing;
    }

    public void setTiming(List<TimeDuration> timing) {
        this.timing = timing;
    }

    public String getFirsttrain() {
        return firsttrain;
    }

    public void setFirsttrain(String firsttrain) {
        this.firsttrain = firsttrain;
    }

    public String getLasttrain() {
        return lasttrain;
    }

    public void setLasttrain(String lasttrain) {
        this.lasttrain = lasttrain;
    }
}
