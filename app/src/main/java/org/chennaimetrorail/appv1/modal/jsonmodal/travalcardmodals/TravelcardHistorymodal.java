package org.chennaimetrorail.appv1.modal.jsonmodal.travalcardmodals;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by 102525 on 2/15/2018.
 */

public class TravelcardHistorymodal {
    @SerializedName("status")
    private String status;
    @SerializedName("Transaction_List")
    private List<TransactionListModal> Transaction_List;

    public String getStatus() {
        return status;
    }

    public List<TransactionListModal> getTransaction_List() {
        return Transaction_List;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTransaction_List(List<TransactionListModal> transaction_List) {
        Transaction_List = transaction_List;
    }
}
