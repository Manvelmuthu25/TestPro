package org.chennaimetrorail.appv1.modal.jsonmodal;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 102525 on 7/17/2017.
 */

public class TimeDuration {

    @SerializedName("train_time")
    private String train_time;
    @SerializedName("platform")
    private String platform;

    public String getTrain_time() {
        return train_time;
    }

    public void setTrain_time(String train_time) {
        this.train_time = train_time;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }
}
