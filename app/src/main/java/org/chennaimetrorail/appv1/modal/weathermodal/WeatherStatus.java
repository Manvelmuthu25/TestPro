package org.chennaimetrorail.appv1.modal.weathermodal;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by 102525 on 10/3/2017.
 */

public class WeatherStatus {

    @SerializedName("cod")
    private String statuscod;
    @SerializedName("list")
    private List<WeatherList> list;

    public String getStatuscod() {
        return statuscod;
    }

    public void setStatuscod(String statuscod) {
        this.statuscod = statuscod;
    }

    public List<WeatherList> getList() {
        return list;
    }

    public void setList(List<WeatherList> list) {
        this.list = list;
    }
}
