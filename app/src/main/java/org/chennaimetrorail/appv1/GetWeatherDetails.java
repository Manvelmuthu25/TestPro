package org.chennaimetrorail.appv1;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by 102525 on 10/3/2017.
 */

public class GetWeatherDetails {

    private static final String OPEN_WEATHER_MAP_API = "http://api.openweathermap.org/data/2.5/forecast?";

    public static JSONObject getJSON(){
        String urls = OPEN_WEATHER_MAP_API+"id=1264527&units=metric";
        try {
            URL url = new URL(String.format(urls));
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.addRequestProperty("x-api-key", Constant.strWeatherMapOrgApiKey);

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(1024);
            String tmp="";
            while((tmp=reader.readLine())!=null)
                json.append(tmp).append("\n");
            reader.close();

            JSONObject data = new JSONObject(json.toString());

            Log.d("weeather_data",""+data);

            // This value will be 404 if the request was not
            // successful
            if(data.getInt("cod") != 200){
                return null;
            }

            return data;
        }catch(Exception e){
            return null;
        }
    }


}