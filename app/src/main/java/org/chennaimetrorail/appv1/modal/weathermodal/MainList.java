package org.chennaimetrorail.appv1.modal.weathermodal;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 102525 on 10/3/2017.
 */

public class MainList {

    @SerializedName("temp")
    private int temp;

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }
}
