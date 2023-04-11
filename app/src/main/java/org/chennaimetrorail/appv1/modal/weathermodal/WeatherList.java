package org.chennaimetrorail.appv1.modal.weathermodal;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by 102525 on 10/3/2017.
 */

public class WeatherList {

    @SerializedName("dt")
    private int dt;

    @SerializedName("main")
    private List<MainList> main;

    @SerializedName("weather")
    private List<WeatherDetails> weather;

    @SerializedName("clouds")
    private List<Clouds> clouds;

    @SerializedName("wind")
    private List<Wind> wind;


    @SerializedName("dt_txt")
    public int dt_txt;


    public int getDt() {
        return dt;
    }

    public void setMain(List<MainList> main) {
        this.main = main;
    }

    public int getDt_txt() {
        return dt_txt;
    }

    public void setClouds(List<Clouds> clouds) {
        this.clouds = clouds;
    }

    public List<Clouds> getClouds() {
        return clouds;
    }

    public void setDt(int dt) {
        this.dt = dt;
    }

    public List<MainList> getMain() {
        return main;
    }

    public void setDt_txt(int dt_txt) {
        this.dt_txt = dt_txt;
    }

    public List<WeatherDetails> getWeather() {
        return weather;
    }

    public void setWeather(List<WeatherDetails> weather) {
        this.weather = weather;
    }

    public List<Wind> getWind() {
        return wind;
    }

    public void setWind(List<Wind> wind) {
        this.wind = wind;
    }

}
