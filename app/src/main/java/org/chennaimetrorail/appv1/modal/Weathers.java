package org.chennaimetrorail.appv1.modal;

/**
 * Created by 102525 on 10/3/2017.
 */

public class Weathers {

    String temp;
    String humidity;
    String icon;
    String description;
    String speed;
    String dt_txt;
    String time_txt;

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getDescription() {
        return description;
    }

    public void setDt_txt(String dt_txt) {
        this.dt_txt =dt_txt;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getDt_txt() {
        return dt_txt;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHumidity() {
        return humidity;
    }

    public String getIcon() {
        return icon;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getTemp() {
        return temp;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTime_txt() {
        return time_txt;
    }

    public void setTime_txt(String time_txt) {
        this.time_txt = time_txt;
    }
}
