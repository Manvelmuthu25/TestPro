package org.chennaimetrorail.appv1.modal.weathermodal;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 102525 on 10/3/2017.
 */

public class Clouds {

    @SerializedName("all")
    public String all;

    public String getAll() {
        return all;
    }

    public void setAll(String all) {
        this.all = all;
    }
}
