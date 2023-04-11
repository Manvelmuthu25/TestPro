package org.chennaimetrorail.appv1.modal.jsonmodal.travalcardmodals;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 102525 on 2/19/2018.
 */

public class TransactionAllListModal {

    @SerializedName("CardTransId")
    private int CardTransId;
    @SerializedName("Registration_Id")
    private int Registration_Id;
    @SerializedName("UserName")
    private String UserName;
    @SerializedName("childname")
    private String childname;
    @SerializedName("Card_number")
    private String Card_number;
    @SerializedName("Trans_number")
    private String Trans_number;
    @SerializedName("Trans_Date")
    private String Trans_Date;
    @SerializedName("E_Mail")
    private String E_Mail;
    @SerializedName("RegisteredMobile")
    private String RegisteredMobile;
    @SerializedName("BankReferenceNo")
    private String BankReferenceNo;
    @SerializedName("TransReferenceNo")
    private String TransReferenceNo;
    @SerializedName("Price")
    private int Price;
    @SerializedName("Ref_ID")
    private int Ref_ID;
    @SerializedName("Status")
    private String Status;

    public String getChildname() {
        return childname;
    }

    public String getStatus() {
        return Status;
    }

    public String getUserName() {
        return UserName;
    }

    public String getTrans_number() {
        return Trans_number;
    }

    public String getTrans_Date() {
        return Trans_Date;
    }

    public int getRegistration_Id() {
        return Registration_Id;
    }

    public int getRef_ID() {
        return Ref_ID;
    }

    public int getPrice() {
        return Price;
    }

    public int getCardTransId() {
        return CardTransId;
    }

    public String getCard_number() {
        return Card_number;
    }

    public void setChildname(String childname) {
        this.childname = childname;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public void setTrans_number(String trans_number) {
        Trans_number = trans_number;
    }

    public void setTrans_Date(String trans_Date) {
        Trans_Date = trans_Date;
    }

    public void setRegistration_Id(int registration_Id) {
        Registration_Id = registration_Id;
    }

    public void setRef_ID(int ref_ID) {
        Ref_ID = ref_ID;
    }

    public void setPrice(int price) {
        Price = price;
    }

    public void setCardTransId(int cardTransId) {
        CardTransId = cardTransId;
    }

    public void setCard_number(String card_number) {
        Card_number = card_number;
    }

    public String getRegisteredMobile() {
        return RegisteredMobile;
    }

    public void setRegisteredMobile(String registeredMobile) {
        RegisteredMobile = registeredMobile;
    }

    public String getE_Mail() {
        return E_Mail;
    }

    public void setE_Mail(String e_Mail) {
        E_Mail = e_Mail;
    }

    public String getBankReferenceNo() {
        return BankReferenceNo;
    }

    public void setBankReferenceNo(String bankReferenceNo) {
        BankReferenceNo = bankReferenceNo;
    }

    public String getTransReferenceNo() {
        return TransReferenceNo;
    }

    public void setTransReferenceNo(String transReferenceNo) {
        TransReferenceNo = transReferenceNo;
    }
}