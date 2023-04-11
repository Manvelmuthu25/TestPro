package org.chennaimetrorail.appv1.modal;

import com.google.gson.annotations.SerializedName;

public class Nfcmodel {

    /*{
    "status": "success",
    "mobilenumber": "9940765435",
    "profilename": "Raja Vignesh"
}*/

    @SerializedName("status")
    private String status;
    @SerializedName("mobilenumber")
    private  String mobilenumber;

    @SerializedName("profilename")
    private  String profilename;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMobilenumber() {
        return mobilenumber;
    }

    public void setMobilenumber(String mobilenumber) {
        this.mobilenumber = mobilenumber;
    }

    public String getProfilename() {
        return profilename;
    }

    public void setProfilename(String profilename) {
        this.profilename = profilename;
    }
}
