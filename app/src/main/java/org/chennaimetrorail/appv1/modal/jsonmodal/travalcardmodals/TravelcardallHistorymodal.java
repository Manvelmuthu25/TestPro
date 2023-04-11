package org.chennaimetrorail.appv1.modal.jsonmodal.travalcardmodals;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by 102525 on 2/19/2018.
 */

public class TravelcardallHistorymodal {
    @SerializedName("status")
    private String status;
    @SerializedName("All_Transaction_List")
    private List<TransactionAllListModal> All_Transaction_List;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<TransactionAllListModal> getAll_Transaction_List() {
        return All_Transaction_List;
    }

    public void setAll_Transaction_List(List<TransactionAllListModal> all_Transaction_List) {
        All_Transaction_List = all_Transaction_List;
    }
}
