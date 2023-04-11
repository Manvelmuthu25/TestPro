package org.chennaimetrorail.appv1.modal.weathermodal;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 102525 on 10/3/2017.
 */

public class Wind {
    @SerializedName("speed")
    public String speed;
    @SerializedName("deg")
    public String deg;

    public String getDeg() {
        return deg;
    }

    public void setDeg(String deg) {
        this.deg = deg;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }
}
