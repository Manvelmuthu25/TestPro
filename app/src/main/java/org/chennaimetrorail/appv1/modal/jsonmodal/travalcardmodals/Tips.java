package org.chennaimetrorail.appv1.modal.jsonmodal.travalcardmodals;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by 102525 on 1/30/2018.
 */

public class Tips {

    @SerializedName("status")
    private String status;
    @SerializedName("tips")
    private List<TipsModal> tips;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<TipsModal> getTips() {
        return tips;
    }

    public void setTips(List<TipsModal> tips) {
        this.tips = tips;
    }
}
