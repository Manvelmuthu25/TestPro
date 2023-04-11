package org.chennaimetrorail.appv1.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.chennaimetrorail.appv1.Constant;
import org.chennaimetrorail.appv1.modal.Foundestationslist;
import org.chennaimetrorail.appv1.modal.IntermediateStations;
import org.chennaimetrorail.appv1.modal.LocalIntermediatelist;
import org.chennaimetrorail.appv1.modal.LocalMenus;
import org.chennaimetrorail.appv1.modal.StationList;
import org.chennaimetrorail.appv1.modal.StationMacList;

import java.io.File;
import java.util.List;

import static android.content.ContentValues.TAG;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_LOCAL_NAME = "dbcmrl_local.db";

    private static final String TABLE_STATION_NAME = "t_StationList";
    private static final String TABLE_STATION_MACDETIALS = "t_StationWifimacAddress";
    private static final String TABLE_LOCAL_MENU_NAME = "t_local_Menu";
    private static final String LOCAL_MENU_ID = "menu_Id";
    private static final String LOCAL_MENU_NAME = "menu_vame";
    private static final String LOCAL_MENU_CLASSNAME = "menu_classname";
    private static final String LOCAL_MENU_PRIORITY = "priority";
    private static final String LOCAL_MENU_ICON = "menu_icon";


    /*
     * For Set Destination alert storage intermediate stationList
     * */

    private static final String TABLE_LOCAL_INTERMEDIATES_STATION_LIST = "t_local_intermediates_station_list";
    private static final String LOCAL_I__ID = "Id";
    private static final String LOCAL_I_STATION_ID = "station_Id";
    private static final String LOCAL_I_STATION_SHORTNAME = "station_ShortName";
    private static final String LOCAL_I_STATION_LINECOLOUR = "station_LineColour";
    /*
     * For Set Destination alert storage Founded stationList
     * */
    private static final String TABLE_FOUNDED_STATION_LIST = "t_founded_station_list";
    private static final String LOCAL_FOUNDED_ID = "fId";
    private static final String LOCAL_FOUNDED_STATION_ID = "fstation_Id";

    SQLiteDatabase mDatabase;

    /*
     * Read getExternalFilesDir Database
     * */
    @SuppressLint("WrongConstant")
    public DatabaseHandler(Context context) {

        super(context, DATABASE_LOCAL_NAME, null, DATABASE_VERSION);
        mDatabase = context.openOrCreateDatabase(context.getExternalFilesDir(null) + File.separator + Constant.db_name, SQLiteDatabase.CREATE_IF_NECESSARY, null);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MENU_TABLE = "CREATE TABLE " + TABLE_LOCAL_MENU_NAME + "("
                + LOCAL_MENU_ID + " INTEGER PRIMARY KEY,"
                + LOCAL_MENU_NAME + " TEXT,"
                + LOCAL_MENU_CLASSNAME + " TEXT,"
                + LOCAL_MENU_PRIORITY + " TEXT,"
                + LOCAL_MENU_ICON + " TEXT" + ")";
        String CREATE_INTERMEDIATE_TABLE = "CREATE TABLE " + TABLE_LOCAL_INTERMEDIATES_STATION_LIST + "("
                + LOCAL_I__ID + " INTEGER PRIMARY KEY,"
                + LOCAL_I_STATION_ID + " TEXT,"
                + LOCAL_I_STATION_SHORTNAME + " TEXT,"
                + LOCAL_I_STATION_LINECOLOUR + " TEXT" + ")";
        String CREATE_FOUNDED_TABLE = "CREATE TABLE " + TABLE_FOUNDED_STATION_LIST + "("
                + LOCAL_FOUNDED_ID + " INTEGER PRIMARY KEY,"
                + LOCAL_FOUNDED_STATION_ID + " TEXT" + ")";
        db.execSQL(CREATE_MENU_TABLE);
        db.execSQL(CREATE_INTERMEDIATE_TABLE);
        db.execSQL(CREATE_FOUNDED_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCAL_MENU_NAME);
        // Create tables again
        onCreate(db);
    }

    // Adding local menus
    public void addMenu(LocalMenus localMenus) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(LOCAL_MENU_NAME, localMenus.getMenu_name());
        values.put(LOCAL_MENU_CLASSNAME, localMenus.getMenu_className());
        values.put(LOCAL_MENU_PRIORITY, localMenus.getMenu_priority());
        values.put(LOCAL_MENU_ICON, localMenus.getMenu_icon());

        // Inserting Row
        db.insert(TABLE_LOCAL_MENU_NAME, null, values);
        db.close(); // Closing database connection
    }

    // Getting all local menus
    public List<LocalMenus> getLocalAllMenus() {
        Constant.local_menus_list.clear();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_LOCAL_MENU_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                LocalMenus localMenus = new LocalMenus();
                localMenus.setId(Integer.parseInt(cursor.getString(0)));
                localMenus.setMenu_name(cursor.getString(1));
                localMenus.setMenu_className(cursor.getString(2));
                localMenus.setMenu_priority(cursor.getString(3));
                localMenus.setMenu_icon(cursor.getString(4));
                // Adding contact to list
                Constant.local_menus_list.add(localMenus);
            } while (cursor.moveToNext());
        }


        return Constant.local_menus_list;
    }

    // Updating single contact
    public int updateMenu(long _id, String name, String priority, String classname) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(LOCAL_MENU_NAME, name);
        values.put(LOCAL_MENU_PRIORITY, priority);
        values.put(LOCAL_MENU_CLASSNAME, classname);
        int i = db.update(TABLE_LOCAL_MENU_NAME, values, LOCAL_MENU_ID + " = " + _id, null);
        return i;
    }

    // Get All StationList
    public List<StationList> getAllStationdetails() {

        if (Constant.station_list != null) {
            Constant.station_list.clear();
        }
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_STATION_NAME;
        Cursor cursor = null;
        /* get cursor on it */
        try {
            cursor = mDatabase.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    StationList stationList = new StationList();
                    stationList.setStation_Id(cursor.getString(0));
                    stationList.setStation_Code(cursor.getString(1));
                    stationList.setStation_ShortName(cursor.getString(2));
                    stationList.setStation_LongName(cursor.getString(3));
                    stationList.setStation_Latitude(cursor.getString(4));
                    stationList.setStation_Longitude(cursor.getString(5));
                    stationList.setStation_Priority(cursor.getString(6));
                    stationList.setStation_LineColour(cursor.getString(7));
                    stationList.setGates_Directions(cursor.getString(8));
                    stationList.setStation_Contacts(cursor.getString(9));
                    stationList.setPlatform_Info(cursor.getString(10));
                    stationList.setStation_Type(cursor.getString(11));

                    // Adding contact to list
                    Constant.station_list.add(stationList);
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            /* fail */
            Log.d(TAG, " doesn't exist :(((");
        }
        return Constant.station_list;


    }

    // Getting intermediate StationList for Travel planner
    public List<IntermediateStations> getIntermediateStations(String f_station_id, String t_station_id) {

        if (Constant.intermediate_station_list != null) {
            Constant.intermediate_station_list.clear();
        }
        // Select All Query
        String selectQuery = "WITH Split(word, str, offsep) AS(VALUES( '',Trim((SELECT Intermediate_Stations FROM t_FareDetails WHERE From_Station_Id = " + f_station_id + " AND To_Station_Id = " + t_station_id + "), ', '),1 )UNION ALL SELECT Substr(str, 0, CASE WHEN Instr(str, ',') THEN Instr(str, ',') ELSE Length(str)+1 END),Ltrim(Substr(str, Instr(str, ',')), ', '),Instr(str, ',')\n" +
                "FROM Split WHERE offsep)SELECT Fare_Amount,Intermediate_Stations,Distance_Between,FirstClass_Fare,Instructions,Time_Duration,Substr(word, 1, 3) word,C.Station_LineColour,C.Station_ShortName,C.Station_Id\n" +
                "FROM t_FareDetails A INNER JOIN split B LEFT OUTER JOIN t_StationList C ON Substr(B.word, 1, 3) = C.Station_Id WHERE B.word !='' AND From_Station_Id = " + f_station_id + " AND To_Station_Id = " + t_station_id + "";

        Cursor cursor = null;
        /* get cursor on it */
        try {
            cursor = mDatabase.rawQuery(selectQuery, null);


            if (cursor.moveToFirst()) {
                do {
                    IntermediateStations intermediate_station_list = new IntermediateStations();
                    intermediate_station_list.setFare_amount(cursor.getString(0));
                    intermediate_station_list.setIntermediate_station(cursor.getString(1));
                    intermediate_station_list.setDistance_btw(cursor.getString(2));
                    intermediate_station_list.setFirst_class_fare(cursor.getString(3));
                    intermediate_station_list.setInstruction(cursor.getString(4));
                    intermediate_station_list.setTimeduration(cursor.getString(5));
                    intermediate_station_list.setWord(cursor.getString(6));
                    intermediate_station_list.setStationLine_colour(cursor.getString(7));
                    intermediate_station_list.setStation_shortName(cursor.getString(8));


                    // Adding contact to list
                    Constant.intermediate_station_list.add(intermediate_station_list);
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            /* fail */
            Log.d(TAG, " doesn't exist :(((" + e);
        }
        return Constant.intermediate_station_list;


    }

    // Getting intermediate StationList Set Destination Alert
    public List<IntermediateStations> getIntermediateStationsAlerts(String f_station_id, String t_station_id) {

        if (Constant.intermediate_station_alertlist != null) {
            Constant.intermediate_station_alertlist.clear();
        }
        // Select All Query
        String selectQuery = "WITH Split(word, str, offsep) AS(VALUES( '',Trim((SELECT '" + f_station_id + ",' || Intermediate_Stations || '," + t_station_id + "' FROM t_FareDetails WHERE From_Station_Id = " + f_station_id + " AND To_Station_Id = " + t_station_id + "), ', '),1 )UNION ALL SELECT Substr(str, 0, CASE WHEN Instr(str, ',') THEN Instr(str, ',') ELSE Length(str)+1 END),Ltrim(Substr(str, Instr(str, ',')), ', '),Instr(str, ',')\n" +
                "FROM Split WHERE offsep)SELECT Fare_Amount,Intermediate_Stations,Distance_Between,FirstClass_Fare,Instructions,Time_Duration,Substr(word, 1, 3) word,C.Station_LineColour,C.Station_ShortName,C.Station_Id\n" +
                "FROM t_FareDetails A INNER JOIN split B LEFT OUTER JOIN t_StationList C ON Substr(B.word, 1, 3) = C.Station_Id WHERE B.word !='' AND From_Station_Id = " + f_station_id + " AND To_Station_Id = " + t_station_id + "";

        Cursor cursor = null;
        /* get cursor on it */
        try {
            cursor = mDatabase.rawQuery(selectQuery, null);


            if (cursor.moveToFirst()) {
                do {
                    IntermediateStations intermediate_station_alertlist = new IntermediateStations();
                    intermediate_station_alertlist.setFare_amount(cursor.getString(0));
                    intermediate_station_alertlist.setIntermediate_station(cursor.getString(1));
                    intermediate_station_alertlist.setDistance_btw(cursor.getString(2));
                    intermediate_station_alertlist.setFirst_class_fare(cursor.getString(3));
                    intermediate_station_alertlist.setInstruction(cursor.getString(4));
                    intermediate_station_alertlist.setTimeduration(cursor.getString(5));
                    intermediate_station_alertlist.setWord(cursor.getString(6));
                    intermediate_station_alertlist.setStationLine_colour(cursor.getString(7));
                    intermediate_station_alertlist.setStation_shortName(cursor.getString(8));
                    intermediate_station_alertlist.setStatin_id(cursor.getString(9));


                    // Adding contact to list
                    Constant.intermediate_station_alertlist.add(intermediate_station_alertlist);
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            /* fail */
            Log.d(TAG, " doesn't exist :(((");
        }
        return Constant.intermediate_station_alertlist;


    }

    // Deleting local Menu
    public void deleteAllMenus() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_LOCAL_MENU_NAME);
    }


    // Getting All StationMacList
    public List<StationMacList> getAllStationMacdetails() {

        Constant.stationMacListss.clear();
        // Select All Query
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_STATION_MACDETIALS;
        Cursor cursor = null;
        /*Cursor c = db.query(TABLE_STATION_MACDETIALS, "Station_Name", null, null, null, null, "Station_Name"+" DESC");
         *//* get cursor on it */
        try {
            cursor = mDatabase.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    StationMacList stationMaclist = new StationMacList();
                    stationMaclist.setStation_id(cursor.getString(0));
                    stationMaclist.setStation_Name(cursor.getString(1));
                    stationMaclist.setWifi_Up(cursor.getString(2));
                    stationMaclist.setWifi_Down(cursor.getString(3));

                    // Adding contact to list
                    Constant.stationMacListss.add(stationMaclist);
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            /* fail */
            Log.d(TAG, " doesn't exist :(((");
        }
        return Constant.stationMacListss;


    }

    // Set Destination Insert new intermediate Station
    public void addIntermidiateStations(LocalIntermediatelist localIntermediatelist) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(LOCAL_I_STATION_ID, localIntermediatelist.getStation_Id());
        values.put(LOCAL_I_STATION_SHORTNAME, localIntermediatelist.getStation_ShortName());
        values.put(LOCAL_I_STATION_LINECOLOUR, localIntermediatelist.getStation_LineColour());

        // Inserting Row
        db.insert(TABLE_LOCAL_INTERMEDIATES_STATION_LIST, null, values);
        db.close(); // Closing database connection
    }

    // Deleting ALl intermediate station
    public void deleteAllIntermediatStation() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_LOCAL_INTERMEDIATES_STATION_LIST);
    }

    // Get All LocalIntermediateSlist
    public List<LocalIntermediatelist> getLocalIntermediateSlist() {

        Constant.intermediate_stationList.clear();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_LOCAL_INTERMEDIATES_STATION_LIST;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                LocalIntermediatelist localIntermediatelist = new LocalIntermediatelist();
                localIntermediatelist.setId(Integer.parseInt(cursor.getString(0)));
                localIntermediatelist.setStation_Id(cursor.getString(1));
                localIntermediatelist.setStation_ShortName(cursor.getString(2));
                localIntermediatelist.setStation_LineColour(cursor.getString(3));
                // Adding contact to list
                Constant.intermediate_stationList.add(localIntermediatelist);
            } while (cursor.moveToNext());
        }


        return Constant.intermediate_stationList;
    }

    // Set Destination Add new founded Station
    public void addfoundedStations(Foundestationslist foundestationslist) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(LOCAL_FOUNDED_STATION_ID, foundestationslist.getStation_Id());
        // Inserting Row
        db.insert(TABLE_FOUNDED_STATION_LIST, null, values);
        db.close(); // Closing database connection
    }

    //Set Destination Check already founded station or not  from founded StationList
    public boolean CheckIsDataAlreadyInDBorNot(String foundedValue) {
        SQLiteDatabase db = this.getWritableDatabase();

        String Query = "Select * from " + TABLE_FOUNDED_STATION_LIST + " where " + LOCAL_FOUNDED_STATION_ID + " = " + foundedValue;
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    // Delet ALl deleteAllFoundedStation
    public void deleteAllFoundedStation() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_FOUNDED_STATION_LIST);
    }

    // Get All getLocalFoundedStationList
    public List<Foundestationslist> getLocalFoundedStationlist() {

        Constant.founded_station.clear();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_FOUNDED_STATION_LIST;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        if (cursor.moveToFirst()) {
            do {
                Foundestationslist foundestationslist = new Foundestationslist();
                foundestationslist.setId(Integer.parseInt(cursor.getString(0)));
                foundestationslist.setStation_Id(cursor.getString(1));
                // Adding contact to list
                Constant.founded_station.add(foundestationslist);
            } while (cursor.moveToNext());
        }


        return Constant.founded_station;
    }

}
