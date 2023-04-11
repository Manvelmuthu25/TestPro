package org.chennaimetrorail.appv1.modal.jsonmodal;

import com.google.gson.annotations.SerializedName;
import org.chennaimetrorail.appv1.modal.StationLatitudeandLongitude;

import java.util.List;

/**
 * Created by 102526 on 4/3/2018.
 */

public class Stationdetailsmodel {
    @SerializedName("status")
    private String status;
    @SerializedName("image")
    private String image;
    @SerializedName("image_temp")
    private String image_temp;
    @SerializedName("stationfacilities")
    private List<Stationfacilities> stationfacilities;

    @SerializedName("stationmapdetails")
    private List<Stationmapdetails> stationmapdetails;

    @SerializedName("stationLatitudeandLongitude")
    private List<StationLatitudeandLongitude> stationLatitudeandLongitude;
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Stationfacilities> getStationfacilities() {
        return stationfacilities;
    }

    public void setStationfacilities(List<Stationfacilities> stationfacilities) {
        this.stationfacilities = stationfacilities;
    }

    public List<Stationmapdetails> getStationmapdetails() {
        return stationmapdetails;
    }

    public void setStationmapdetails(List<Stationmapdetails> stationmapdetails) {
        this.stationmapdetails = stationmapdetails;
    }

    public List<StationLatitudeandLongitude> getStationLatitudeandLongitude() {
        return stationLatitudeandLongitude;
    }

    public void setStationLatitudeandLongitude(List<StationLatitudeandLongitude> stationLatitudeandLongitude) {
        this.stationLatitudeandLongitude = stationLatitudeandLongitude;
    }

    public String getImage_temp() {
        return image_temp;
    }

    public void setImage_temp(String image_temp) {
        this.image_temp = image_temp;
    }
}

