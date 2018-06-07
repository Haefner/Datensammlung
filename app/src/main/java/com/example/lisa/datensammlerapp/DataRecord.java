package com.example.lisa.datensammlerapp;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

//TODO DataRecord implementieren
public class DataRecord {
//*#*#2846579#*#*
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
        JSONArray array = new JSONArray();
        JSONObject body = new JSONObject();
        try {
            body.put("AndroidID", androidID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        array.put(body);
        body = new JSONObject();
        try {
            body.put("Sensortyp", "Accelometer");
            body.put("Zeitstempel", System.currentTimeMillis()+ "");
            body.put("X", x + "");
            body.put("Y", y + "");
            body.put("Z", z + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        array.put(body);
        Communicator com = new Communicator();
        com.execute("http://www.node.test.seda.com.de/schreibeSensor", array.toString());
    }
    public void recordGyroscope(String androidID, double x, double y, double z)
    {
        JSONArray array = new JSONArray();
        JSONObject body = new JSONObject();
        try {
            body.put("AndroidID", androidID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        array.put(body);
        body = new JSONObject();
        try {
            body.put("Sensortyp", "Gyroscope");
            body.put("Zeitstempel", System.currentTimeMillis() + "");
            body.put("X", x + "");
            body.put("Y", y + "");
            body.put("Z", z + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        array.put(body);
        Communicator com = new Communicator();
        com.execute("http://www.node.test.seda.com.de/schreibeSensor", array.toString());
    }

    public void recordRotation(String androidID, double x, double y, double z)
    {
        JSONArray array = new JSONArray();
        JSONObject body = new JSONObject();
        try {
            body.put("AndroidID", androidID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        array.put(body);
        body = new JSONObject();
        try {
            body.put("Sensortyp", "Rotation");
            body.put("Zeitstempel", System.currentTimeMillis() + "");
            body.put("X", x + "");
            body.put("Y", y + "");
            body.put("Z", z + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        array.put(body);
        Communicator com = new Communicator();
        com.execute("http://www.node.test.seda.com.de/schreibeSensor", array.toString());
    }

    public void recordCompass(String androidID, double x, double y, double z)
    {
        JSONArray array = new JSONArray();
        JSONObject body = new JSONObject();
        try {
            body.put("AndroidID", androidID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        array.put(body);
        body = new JSONObject();
        try {
            body.put("Sensortyp", "Magnetfeld");
            body.put("Zeitstempel", System.currentTimeMillis() + "");
            body.put("X", x + "");
            body.put("Y", y + "");
            body.put("Z", z + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        array.put(body);
        Communicator com = new Communicator();
        com.execute("http://www.node.test.seda.com.de/schreibeSensor", array.toString());
    }
}