package org.chennaimetrorail.appv1.modal.jsonmodal.travalcardmodals;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 102525 on 1/30/2018.
 */

public class TipsModal {

    @SerializedName("id")
    private String id;
    @SerializedName("tipsName")
    private String tipsName;
    @SerializedName("description")
    private String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTipsName() {
        return tipsName;
    }

    public void setTipsName(String tipsName) {
        this.tipsName = tipsName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
