package org.chennaimetrorail.appv1.modal.jsonmodal.travalcardmodals;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by 102525 on 2/1/2018.
 */

public class TravalCardlistModal {

    @SerializedName("status")
    private String status;
    @SerializedName("msg")
    private String msg;
    @SerializedName("cardlist")
    private List<CardlistModal> cardlistModal;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<CardlistModal> getCardlistModal() {
        return cardlistModal;
    }

    public void setCardlistModal(List<CardlistModal> cardlistModal) {
        this.cardlistModal = cardlistModal;
    }
}
