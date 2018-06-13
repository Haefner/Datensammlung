package com.example.lisa.datensammlerapp;


import android.os.Handler;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



//TODO DataRecord implementieren
public class DataRecord {
    JSONArray daten = new JSONArray();
    final String androidID;
    Datensammlung thread;
    int daUebertragen = 0;
//*#*#2846579#*#*


    final Handler timerHandler = new Handler();
    final Runnable timerRunnable = new Runnable() {

        @Override
        final public void run() {
            sendeDaten();
            //System.gc();
            timerHandler.postDelayed(this, 1000);
        }
    };

    public DataRecord(Datensammlung ithread, String androidID)
    {
        thread = ithread;
        this.androidID = androidID;
    }
    public void startRecordDate()
    {
       thread.runOnUiThread(new Runnable() {
             @Override
             public void run() {
                 thread.daVal.setText("0/0");
             }
          }
        );
        daten = new JSONArray();
        final JSONObject dBody = new JSONObject();
        try {
            dBody.put("AndroidID", androidID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        daten.put(dBody);
        final JSONObject sBody = new JSONObject();
        try {
            sBody.put("AndroidID", androidID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final Communicator com = new Communicator(this.thread);
        com.execute("http://www.node.test.seda.com.de/starteMessung", sBody.toString(), 0);
        timerHandler.postDelayed(timerRunnable, 1000);
    }

    public void stopRecordDate()
    {
        daUebertragen = 0;
        this.sendeDaten();
        final JSONObject sBody = new JSONObject();
        try {
            sBody.put("AndroidID", androidID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final Communicator com = new Communicator(this.thread);
        com.execute("http://www.node.test.seda.com.de/beendeMessung", sBody.toString(), "Messung beendet");
        timerHandler.removeCallbacks(timerRunnable);
    }
    public void recordAccelometer(double x, double y, double z)
    {
        final JSONObject iBody = new JSONObject();
        try {
            iBody.put("Sensortyp", "Accelometer");
            iBody.put("Zeitstempel", System.currentTimeMillis()+ "");
            iBody.put("X", x + "");
            iBody.put("Y", y + "");
            iBody.put("Z", z + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        daten.put(iBody);
    }
    public void recordGyroscope(double x, double y, double z)
    {
        final JSONObject iBody = new JSONObject();
        try {
            iBody.put("Sensortyp", "Gyroscope");
            iBody.put("Zeitstempel", System.currentTimeMillis() + "");
            iBody.put("X", x + "");
            iBody.put("Y", y + "");
            iBody.put("Z", z + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        daten.put(iBody);
    }

    public void recordRotation(double x, double y, double z)
    {
        final JSONObject iBody = new JSONObject();
        try {
            iBody.put("Sensortyp", "Rotation");
            iBody.put("Zeitstempel", System.currentTimeMillis() + "");
            iBody.put("X", x + "");
            iBody.put("Y", y + "");
            iBody.put("Z", z + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        daten.put(iBody);
    }

    public void recordCompass(double x, double y, double z)
    {
        final JSONObject iBody = new JSONObject();
        try {
            iBody.put("Sensortyp", "Magnetfeld");
            iBody.put("Zeitstempel", System.currentTimeMillis() + "");
            iBody.put("X", x + "");
            iBody.put("Y", y + "");
            iBody.put("Z", z + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        daten.put(iBody);
    }
    public void sendeDaten()
    {
        if (daten.length() > 1) {
            daUebertragen += daten.length()-1;

            final Communicator com = new Communicator(this.thread);
            com.execute("http://www.node.test.seda.com.de/schreibeSensor", daten.toString(), daUebertragen);
            daten = null;
            daten = new JSONArray();
            final JSONObject body = new JSONObject();
            try {
                body.put("AndroidID", androidID);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            daten.put(body);
        }
    }
}