package org.chennaimetrorail.appv1.modal.jsonmodal.travalcardmodals;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 102525 on 1/30/2018.
 */

public class LoginDetails {

    @SerializedName("registrationid")
    private int registrationid;
    @SerializedName("username")
    private String username;
    @SerializedName("dob")
    private String dob;
    @SerializedName("gender")
    private String gender;
    @SerializedName("registeredmobile")
    private String registeredmobile;
    @SerializedName("email")
    private String email;

    public String getRegisteredmobile() {
        return registeredmobile;
    }

    public void setRegisteredmobile(String registeredmobile) {
        this.registeredmobile = registeredmobile;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getRegistrationid() {
        return registrationid;
    }

    public void setRegistrationid(int registrationid) {
        this.registrationid = registrationid;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }


}
