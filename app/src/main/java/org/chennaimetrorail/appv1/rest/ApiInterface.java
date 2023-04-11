package org.chennaimetrorail.appv1.rest;


import android.renderscript.Sampler;

import com.google.gson.JsonObject;

import org.chennaimetrorail.appv1.Constant;
import org.chennaimetrorail.appv1.modal.CheckdirectionResponse;
import org.chennaimetrorail.appv1.modal.Googleplacesphotos;
import org.chennaimetrorail.appv1.modal.Nfcmodel;
import org.chennaimetrorail.appv1.modal.book_a_cab.Bookcab_modals;
import org.chennaimetrorail.appv1.modal.book_a_cab.Ticketbooking;
import org.chennaimetrorail.appv1.modal.book_a_cab.freerideavailabiltydetails;
import org.chennaimetrorail.appv1.modal.jsonmodal.CurrentNewsResponse;
import org.chennaimetrorail.appv1.modal.jsonmodal.DeviceDetails;
import org.chennaimetrorail.appv1.modal.jsonmodal.FTTimeResponse;
import org.chennaimetrorail.appv1.modal.jsonmodal.FeedbackResponse;
import org.chennaimetrorail.appv1.modal.jsonmodal.Feeders;
import org.chennaimetrorail.appv1.modal.jsonmodal.FetchPushNotifiResponse;
import org.chennaimetrorail.appv1.modal.jsonmodal.MetroNetWorkstatus;
import org.chennaimetrorail.appv1.modal.jsonmodal.NeaerstStationList;
import org.chennaimetrorail.appv1.modal.jsonmodal.ParkingResponse;
import org.chennaimetrorail.appv1.modal.jsonmodal.Stationdetailsmodel;
import org.chennaimetrorail.appv1.modal.jsonmodal.TimeResponse;
import org.chennaimetrorail.appv1.modal.jsonmodal.forgetotp_modal;
import org.chennaimetrorail.appv1.modal.jsonmodal.nearbyjsonmodal.Nearbyplaces;
import org.chennaimetrorail.appv1.modal.jsonmodal.travalcardmodals.AddcardModal;
import org.chennaimetrorail.appv1.modal.jsonmodal.travalcardmodals.Cardvalidate;
import org.chennaimetrorail.appv1.modal.jsonmodal.travalcardmodals.ChangepassowordModal;
import org.chennaimetrorail.appv1.modal.jsonmodal.travalcardmodals.Login;
import org.chennaimetrorail.appv1.modal.jsonmodal.travalcardmodals.PaymentStatusModal;
import org.chennaimetrorail.appv1.modal.jsonmodal.travalcardmodals.PhoneVerifyModal;
import org.chennaimetrorail.appv1.modal.jsonmodal.travalcardmodals.RegisterModal;
import org.chennaimetrorail.appv1.modal.jsonmodal.travalcardmodals.RegistrationVerify;
import org.chennaimetrorail.appv1.modal.jsonmodal.travalcardmodals.Tips;
import org.chennaimetrorail.appv1.modal.jsonmodal.travalcardmodals.TravalCardlistModal;
import org.chennaimetrorail.appv1.modal.jsonmodal.travalcardmodals.TravalcardDeletMadol;
import org.chennaimetrorail.appv1.modal.jsonmodal.travalcardmodals.TravelcardHistorymodal;
import org.chennaimetrorail.appv1.modal.jsonmodal.travalcardmodals.TravelcardallHistorymodal;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {
    /*Download Database form server*/
    @GET("database/download_db.php?filename=dbcmrl_main_v40.db&secret_code="+Constant.strDBSecretCode+"")
    Call<ResponseBody> downloadFileWithFixedUrl();

/*Send Device Id to server*/
    @GET("api/RegisterDevices")
    Call<DeviceDetails> getDeviceDetails(@Query("deviceid") String deviceid,
                                         @Query("Registrationid") String registrationid,
                                         @Query("os") String os,
                                         @Query("secretcode") String secretcode,
                                         @Query("appv") String appv);

/*UpcomingTrainDetails Featching*/
    @GET("api/UpcomingTrains")
    Call<TimeResponse> getTrainDetails(@Query("stationid") String stationid,
                                       @Query("secretcode") String secretcode,
                                       @Query("appv") String appv,
                                       @Query("date") String date,
                                       @Query("time") String time);

    @GET("api/UpcomingTrainsFromToV2")
    Call<FTTimeResponse> getTrainDetailsUpcomingTrainsFromTo(@Query("fromstationid") String fromstationId,
                                                             @Query("tostationid") String ToStationId,
                                                             @Query("secretcode") String secretcode,
                                                             @Query("date") String date,
                                                             @Query("time") String time,
                                                             @Query("appv") String appv);
   /* api/TravelPlannerWithRoute/TrainSchedule?*/
    @GET("api/TravelPlannerWithRoute/TrainSchedule?")
    Call<FTTimeResponse> TravelPlannerUpcomingTrainsFromTo(@Query("fromstationId") String fromstationId,
                                                             @Query("ToStationId") String ToStationId,
                                                             @Query("secretcode") String secretcode,
                                                             @Query("date") String date,
                                                             @Query("time") String time,
                                                            @Query("appv") String appv);


    /*OLd */
  /* @GET("api/CurrentLocationandNews")
     Call<CurrentNewsResponse> getCurrentNewsResponse(@Query("ostype") String ostype,
                                                     @Query("latitude") double lat,
                                                     @Query("longitude") double lon,
                                                     @Query("secretcode") String secretcode,
                                                     @Query("appv") String appv);*/
   /*NEW*/


    @POST("api/CurrentLocationandNews/LatestNotification")
    Call<CurrentNewsResponse> getCurrentNewsResponse(@Body  JsonObject values);


    @POST("api/Passenger/FreeRideAvailabiltyDetails")
    Call<freerideavailabiltydetails>getfreerideavailabiltydetails(@Header("Authorization") String token, @Body JsonObject values);



    /* api/FetchPushNotification/FetchNotifications?*/
    @GET("api/FetchPushNotification/FetchNotifications?")
    Call<FetchPushNotifiResponse> fetchPushNotificationrs(@Query("ostype") String ostype,
                                                          @Query("secretcode") String secretcode,
                                                          @Query("appv") String appv);





    /*api/StationList/GetStationList?*/
    @GET("api/StationList/GetStationList?")
    Call<NeaerstStationList> getNearestMetro(@Query("latitude") double lat,
                                             @Query("longitude") double lon,
                                             @Query("secretcode") String secretcode,
                                             @Query("appv") String appv);


    @GET("api/ParkingTarrif/getTarrif")
    Call<ParkingResponse> getParkingTarrif(@Query("stationid") String stationid,
                                           @Query("secretcode") String secretcode,
                                           @Query("appv") String appv);



    @GET("api/StationFacilities/Get_Station_Facilities_List")
    Call<Stationdetailsmodel> getstationdetails(@Query("stationid") String stationid,
                                                @Query("secretcode") String secretcode,
                                                @Query("appv") String appv);

    @GET("api/FeedBack/Add_FeedBack")
    Call<FeedbackResponse> sendFeedback(@Query("username") String username,
                                        @Query("email") String email,
                                        @Query("contactno") String contactno,
                                        @Query("experience_in_train") String experience_in_train,
                                        @Query("upkeep_of_station") String upkeep_of_station,
                                        @Query("rest_room_facilities") String rest_room_facilities,
                                        @Query("security_services") String security_services,
                                        @Query("ticketing_experience") String ticketing_experience,
                                        @Query("parking_facilities") String parking_facilities,
                                        @Query("staff_friendliness") String staff_friendliness,
                                        @Query("facilities_at_station") String facilities_at_station,
                                        @Query("safety") String safety,
                                        @Query("total_experience_in_cmrl") String total_experience_in_cmrl,
                                        @Query("station") String station,
                                        @Query("comment") String message,
                                        @Query("secretcode") String secretcode,
                                        @Query("appv") String appv);
    @GET("api/FeederService/GetFeederService")
    Call<Feeders> GetFeederService(@Query("stationid") String stationid,
                                   @Query("secretcode") String secretcode,
                                   @Query("appv") String appv);


    @GET("api/MetroNetworkInformation/GetMetroNetworkInfo")
    Call<MetroNetWorkstatus> GetMetroNetworkInfo(@Query("secretcode") String secretcode,
                                                 @Query("appv") String appv);


    /*TravalCard Recharge............*/
   /* {
        "username": "7904705975",
            "userpassword": "manivel25",
            "secretcode": "$3cr3t",
            "tokenid": "",
            "appv": "2.0.0"
    }*/
   //Old
   /* @GET("api/CmrlAccount/Login")
    Call<Login> getT_LoginDetails(@Query("username") String username,
                                  @Query("userpassword") String userpassword,
                                  @Query("secretcode") String secretcode,
                                  @Query("tokenid") String tokenid,
                                  @Query("appv") String appv);
*/
    /*NEW*/


    @POST("api/Passenger/Login")
    Call<Login> getT_LoginDetails(@Body JsonObject values);

    /*old*/
   @GET("api/TravelCardRecharge/RetrivePassword")
    Call<PhoneVerifyModal> getForgetPasswordVerify(@Query("ForgetPassword") String ForgetPassword,
                                                   @Query("secretcode") String secretcode,
                                                   @Query("appv") String appv);



    /*old*/

//   @GET("api/Passenger/RetrivePassword")
//   Call<PhoneVerifyModal> getForgetPassword(@Query("ForgetPassword") String ForgetPassword,
//                                                   @Query("secretcode") String secretcode,
//                                                   @Query("appv") String appv);
//

    @POST("api/Passenger/RetrivePassword_v1")
    Call<PhoneVerifyModal> getForgetPassword(@Body JsonObject values);


    /*New*/
    @POST("api/Passenger/ForgotPassword_ValidateOTP")
    Call<forgetotp_modal> getForgetValidateOTP(@Body JsonObject values);

    /*old*/

   /* @GET("api/TravelCardRecharge/ChangePassword")
    Call<ChangepassowordModal> getChangeForgetPassword(@Query("MobileNumber") String MobileNumber,
                                                       @Query("ChangePassword") String ChangePassword,
                                                       @Query("secretcode") String secretcode,
                                                       @Query("appv") String appv);*/
/*  @GET("api/Passenger/RetrivePassword?")
    Call<ChangepassowordModal> getChangeForgetPassword(@Query("MobileNumber") String MobileNumber,
                                                       @Query("ChangePassword") String ChangePassword,
                                                       @Query("secretcode") String secretcode,
                                                       @Query("appv") String appv);*/



    /*New*/
    @POST("api/Passenger/ChangePassword_v1")

    Call<ChangepassowordModal> getChangeForgetPassword(@Header("Authorization") String token, @Body JsonObject values);

//    https://apidev2.chennaimetrorail.org/v2/api/Passenger/ForgotPassword_Update
    /*New*/
    @POST("api/Passenger/ForgotPassword_Update")
    Call<ChangepassowordModal> getUpdateForgetPassword(@Body JsonObject values);


    /*old*/

  /*  @GET("api/TravelCardRecharge/RegistrationVerify")
      Call<PhoneVerifyModal> getRegisterVerify(@Query("RegisteredMobilnumber") String RegisteredMobilnumber,
                                             @Query("secretcode") String secretcode,
                                             @Query("appv") String appv);
*/
  /*old*/
//
//    @GET("api/Passenger/RegistrationVerify")
//    Call<RegistrationVerify> getRegisterVerify(@Query("RegisteredMobilnumber") String RegisteredMobilnumber,
//                                               @Query("secretcode") String secretcode,
//                                               @Query("appv") String appv);

    /*NEW*/

    @POST("api/Passenger/RegistrationVerify_v1")
    Call<RegistrationVerify> RegisterVerify(@Body JsonObject values);




    /*old*/
   /* @GET("api/TravelCardRecharge/Registration")
    Call<RegisterModal> getRegisterUser(@Query("Name") String Name,
                                        @Query("DOB") String DOB,
                                        @Query("Gender") String Gender,
                                        @Query("RegisteredMobile") String RegisteredMobile,
                                        @Query("Email") String Email,
                                        @Query("Password") String Password,
                                        @Query("DeviceId") String DeviceId,
                                        @Query("secretcode") String secretcode,
                                        @Query("appv") String appv);*/

   /* @Headers({
            "Content-Type: application/json"

    })
*/

   @POST("api/Passenger/Registration")
    Call<RegisterModal> getRegisterUser(@Body JsonObject values);

    @GET("api/TravelCardRecharge/LoadTips")
    Call<Tips> getT_Tips(@Query("secretcode") String secretcode, @Query("appv") String appv);

    /*oid*//*****************************************************

    @GET("api/TravelCardRecharge/Add_card")
    Call<AddcardModal> getT_AddCardList(@Query("username") String username,
                                        @Query("password") String password,
                                        @Query("card_number") String card_number,
                                        @Query("nik_name") String nik_name,
                                        @Query("secretcode") String secretcode,
                                        @Query("appv") String appv);

 /*oid*/


    @POST("api/TravelCardRecharge/Add_card_v1")
    Call<AddcardModal> AddCardList(@Body JsonObject values);



    /*oid*/
    @GET("api/TravelCardRecharge/getCard")
    Call<TravalCardlistModal> getT_CardList(@Query("username") String username,
                                            @Query("password") String password,
                                            @Query("secretcode") String secretcode,
                                            @Query("appv") String appv);
/*NEW*/

   @POST("api/Passenger/TravelCard_GetCard")
    Call<TravalCardlistModal> getT_CardList(@Body JsonObject values);



   /* @GET("api/TravelCardRecharge/Delete_Card")  ****************************************************
    Call<TravalcardDeletMadol> deletT_CardList(@Query("username") String username,
                                               @Query("password") String password,
                                               @Query("card_number") String card_number,
                                               @Query("secretcode") String secretcode,
                                               @Query("appv") String appv);
*/



    @POST("api/TravelCardRecharge/Delete_Card_v1")
    Call<TravalcardDeletMadol> deletT_CardList(@Body JsonObject values);



    @GET("api/CmrlAccount/Login")
    Call<Ticketbooking> getticketsbookcab(@Query("username")String username,
                                          @Query("userpassword")String password,
                                          @Query("secretcode")String secretcode,
                                          @Query("tokenid")String tokenid,
                                          @Query("appv")String appv);

//
//    @GET("api/TravelCardRecharge/Add_Transaction_Status")  **************************************************
//    Call<PaymentStatusModal> add_Card_Transactionstatus(@Query("username") String username,
//                                                        @Query("password") String password,
//                                                        @Query("smart_card_number") String smart_card_number,
//                                                        @Query("transdate") String transdate,
//                                                        @Query("price") String price,
//                                                        @Query("appv") String appv,
//                                                        @Query("card_id") String card_id,
//                                                        //@Query("emailid") String emailid,
//                                                        @Query("mobilenumber") String mobilenumber,
//                                                        @Query("transactionsource") String transactionsource,
//                                                        @Query("secretcode") String secretcode);

    @POST("api/TravelCardRecharge/Add_Transaction_Status_v1")
    Call<PaymentStatusModal> add_Card_Transactionstatus(@Body JsonObject values);

/*
    @GET("api/TravelCardRecharge/getTransactionList")******************************************************
    Call<TravelcardHistorymodal> getTransactionListHistory(@Query("username") String username,
                                                           @Query("password") String password,
                                                           @Query("cardnumber") String cardnumber,
                                                           @Query("secretcode") String secretcode,
                                                           @Query("appv") String appv);*/


    @POST("api/TravelCardRecharge/getTransactionList_v1")
    Call<TravelcardHistorymodal> getTransactionListHistory(@Body JsonObject values);

/*

    @GET("api/TravelCardRecharge/getAllTransactionList")
    Call<TravelcardallHistorymodal> getAllTransactionList(@Query("username") String username,
                                                          @Query("passwd") String password,
                                                          @Query("stype") String stype,
                                                          @Query("secretcode") String secretcode,
                                                          @Query("appv") String appv);
*/


    @POST("api/TravelCardRecharge/getAllTransactionList_v1")
    Call<TravelcardallHistorymodal> getAllTransactionList(@Body JsonObject values);

/*

    @GET("api/TravelCardRecharge/Update_Card_Status")   *****************************************************
    Call <Cardvalidate> getupdate_CardList(@Query("username") String username,
                                           @Query("password") String password,
                                           @Query("card_number") String card_number,
                                           @Query("CardResult") String cardresult,
                                           @Query("secretcode") String secretcode,
                                           @Query("appv") String appv);

*/



    @POST("api/TravelCardRecharge/Update_Card_Status_v1")
    Call <Cardvalidate>getupdate_CardList(@Body JsonObject values);



    @GET("api/UserProfile/UserProfileBasedCardNumber")
    Call<Nfcmodel> getUserProfileBasedCardNumber(@Query("card_number") String card_number);



    @GET("api/UpcomingTrainsFromToV2/CheckDestinationAlert")

    Call <CheckdirectionResponse> CheckTrainDirection(@Query("previousstationid") String previousstationid,
                                                      @Query("currentstationid") String currentstationid,
                                                      @Query("tostationid") String tostationid,
                                                      @Query("secretcode") String secretcode,
                                                      @Query("appv") String appv);

    /*WhereToStayDine*/
    @GET("api/NearbyPlaces/GetNearbyPlaces")
    Call<Nearbyplaces> getNearbyPlaces(@Query("type") String type,
                                       @Query("location") String location,
                                       @Query("secretcode") String secretcode,
                                       @Query("appv") String appv);

    @GET("api/NearbyPlaces/GetPlacesPhoto")
    Call<Googleplacesphotos> getgooglephotos(@Query("maxwidth") String maxwidth,
                                             @Query("maxheight") String maxheight,
                                             @Query("photoreference") String photoreference,
                                             @Query("secretcode") String secretcode,
                                             @Query("appv") String appv);

/*old*/
/*    @GET("api/CmrlAccount/Login")
    Call<Bookcab_modals>getbookcab(@Query("username")String username,
                                   @Query("userpassword")String password,
                                   @Query("secretcode")String secretcode,
                                   @Query("tokenid")String tokenid,
                                   @Query("appv")String appv);*/
    /*NEW*/

  /*  @POST("api/Passenger/Login")
    Call<Login> getT_LoginDetails(@Body JsonObject values);*/

@POST("api/Passenger/Login")
Call<Bookcab_modals> getbookcab(@Body JsonObject values);




}
