package org.chennaimetrorail.appv1;

import org.chennaimetrorail.appv1.modal.Foundestationslist;
import org.chennaimetrorail.appv1.modal.IntermediateStations;
import org.chennaimetrorail.appv1.modal.LocalIntermediatelist;
import org.chennaimetrorail.appv1.modal.LocalMenus;
import org.chennaimetrorail.appv1.modal.StationLatitudeandLongitude;
import org.chennaimetrorail.appv1.modal.StationList;
import org.chennaimetrorail.appv1.modal.StationMacList;
import org.chennaimetrorail.appv1.modal.Steps;
import org.chennaimetrorail.appv1.modal.Weathers;
import org.chennaimetrorail.appv1.modal.jsonmodal.FeederAuto;
import org.chennaimetrorail.appv1.modal.jsonmodal.FeederBus;
import org.chennaimetrorail.appv1.modal.jsonmodal.FeederCar;
import org.chennaimetrorail.appv1.modal.jsonmodal.parkingZone;
import org.chennaimetrorail.appv1.modal.jsonmodal.travalcardmodals.TipsModal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 102525 on 7/26/2017.
 */

public class Constant {

    public static final String strWeatherMapOrgApiKey = "82a81b7c4f872cbfbfa3790cbeac79df"; // Weather service api key of test server
    public static final String strApiKey = "$3cr3t"; // service api key of cmrl server
    public static final String strDBSecretCode = "Navin123$"; // service api key of DBSecretCode
    public static final String os_type = "android";//OS Type

    /*database download database name*/

    public static final String db_name = "dbcmrl_main_v40.db";
    public static final String db_name_journal = "dbcmrl_main_v40.db-journal";

    /*Application Arrays*/
    public static final List<LocalMenus> local_menus_list = new ArrayList<LocalMenus>();
    public static final List<LocalMenus> saved_menus_list = new ArrayList<LocalMenus>();
    public static final List<StationList> station_list = new ArrayList<StationList>();
    public static final List<IntermediateStations> intermediate_station_list = new ArrayList<IntermediateStations>();
    public static final List<IntermediateStations> intermediate_station_alertlist = new ArrayList<IntermediateStations>();
    public static final List<Steps> steps_list = new ArrayList<Steps>();
    public static final List<Weathers> weathers_list = new ArrayList<Weathers>();
    public static final List<Weathers> Currentweatherlist = new ArrayList<Weathers>();

    /*Set Destination alert arrays Founded Station */
    public static final List<StationMacList> stationMacListss = new ArrayList<org.chennaimetrorail.appv1.modal.StationMacList>();
    public static final List<Foundestationslist> founded_station = new ArrayList<>();

    /*Travalcard Recharge.....*/
    public static final List<LocalIntermediatelist> intermediate_stationList = new ArrayList<>();
    public static List<TipsModal> tips_array = new ArrayList<TipsModal>();
    public static List<StationLatitudeandLongitude> stationLatitudeandLongitude = new ArrayList<StationLatitudeandLongitude>();
    public static List<FeederBus> feederBusList = new ArrayList<FeederBus>();
    public static List<FeederCar> feederCarList = new ArrayList<FeederCar>();
    public static List<FeederAuto> feederAutoList = new ArrayList<FeederAuto>();
    public static String station_image_url = "";
    public  static  List<parkingZone>parkingZones=new ArrayList<>();
    public interface ACTION {

//        public static String STARTFOREGROUND_ACTION = "org.chennaimetrorail.appv1.alertDestination.WifiBackgroundSrvice.action.startforeground";
//        public static String STOPFOREGROUND_ACTION = "org.chennaimetrorail.appv1.alertDestination.WifiBackgroundSrvice.action.stopforeground";
    }

    public interface NOTIFICATION_ID {
        public static int FOREGROUND_SERVICE = 101;
    }

}
