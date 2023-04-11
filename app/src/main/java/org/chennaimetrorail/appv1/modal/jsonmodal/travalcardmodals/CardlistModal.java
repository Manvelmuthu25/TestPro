package org.chennaimetrorail.appv1.modal.jsonmodal.travalcardmodals;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 102525 on 2/1/2018.
 */

public class CardlistModal {
    @SerializedName("card_id")
    private int card_id;
    @SerializedName("RegistrationId")
    private int RegistrationId;
    @SerializedName("card_number")
    private String card_number;
    @SerializedName("nik_name")
    private String nik_name;
    @SerializedName("username")
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getCard_id() {
        return card_id;
    }

    public void setCard_id(int card_id) {
        this.card_id = card_id;
    }

    public int getRegistrationId() {
        return RegistrationId;
    }

    public void setRegistrationId(int registrationId) {
        RegistrationId = registrationId;
    }

    public String getCard_number() {
        return card_number;
    }

    public void setCard_number(String card_number) {
        this.card_number = card_number;
    }

    public String getNik_name() {
        return nik_name;
    }

    public void setNik_name(String nik_name) {
        this.nik_name = nik_name;
    }


}
