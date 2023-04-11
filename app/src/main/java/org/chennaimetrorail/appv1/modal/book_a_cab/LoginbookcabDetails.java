package org.chennaimetrorail.appv1.modal.book_a_cab;

import com.google.gson.annotations.SerializedName;

public class LoginbookcabDetails {
    @SerializedName("registrationid")
    private String registrationid;
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


    public String getRegistrationid() {
        return registrationid;
    }

    public void setRegistrationid(String registrationid) {
        this.registrationid = registrationid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRegisteredmobile() {
        return registeredmobile;
    }

    public void setRegisteredmobile(String registeredmobile) {
        this.registeredmobile = registeredmobile;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }


}
