package com.example.lisa.datensammlerapp;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

//TODO DataRecord implementieren
public class DataRecord {

    public void startRecordDate(String androidID)
    {
        JSONObject body = new JSONObject();
        try {
            body.put("AndroidID", androidID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Communicator com = new Communicator();
        com.execute("http://www.node.test.seda.com.de/starteMessung", body.toString());
    }

    public void stopRecordDate(String androidID)
    {
        JSONObject body = new JSONObject();
        try {
            body.put("AndroidID", androidID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Communicator com = new Communicator();
        com.execute("http://www.node.test.seda.com.de/beendeMessung", body.toString());
    }
    public void recordAccelometer(String androidID, double x, double y, double z)
    {
        JSONObject body = new JSONObject();
        try {
            body.put("AndroidID", androidID);
            body.put("Sensortyp", "Accelometer");
            body.put("Zeitstempel", System.currentTimeMillis()+ "");
            body.put("X", x + "");
            body.put("Y", y + "");
            body.put("Z", z + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Communicator com = new Communicator();
        com.execute("http://www.node.test.seda.com.de/schreibeSensor", body.toString());
    }
    public void recordGyroscope(String androidID, double x, double y, double z)
    {
        JSONObject body = new JSONObject();
        try {
            body.put("AndroidID", androidID);
            body.put("Sensortyp", "Gyroscope");
            body.put("Zeitstempel", System.currentTimeMillis() + "");
            body.put("X", x + "");
            body.put("Y", y + "");
            body.put("Z", z + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Communicator com = new Communicator();
        com.execute("http://www.node.test.seda.com.de/schreibeSensor", body.toString());
    }
    public void recordLocation(String androidID, String longitude, String langitude)
    {
        JSONObject body = new JSONObject();
        try {
            body.put("AndroidID", androidID);
            body.put("Sensortyp", "Location");
            body.put("Zeitstempel", System.currentTimeMillis() + "");
            body.put("Long", longitude + "");
            body.put("Lat", langitude + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Communicator com = new Communicator();
        com.execute("http://www.node.test.seda.com.de/schreibeSensor", body.toString());
    }
    public void recordLight(String androidID, double light)
    {
        JSONObject body = new JSONObject();
        try {
            body.put("AndroidID", androidID);
            body.put("Sensortyp", "Licht");
            body.put("Zeitstempel", System.currentTimeMillis() + "");
            body.put("CD", light + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Communicator com = new Communicator();
        com.execute("http://www.node.test.seda.com.de/schreibeSensor", body.toString());
    }

    public void recordRotaion(String androidId, float value, float value1, float value2) {
        //TODO
    }

    public void recordCompas(String androidId, float value, float value1, float value2) {
        //TODO
    }
}