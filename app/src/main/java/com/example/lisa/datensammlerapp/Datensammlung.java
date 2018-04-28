package com.example.lisa.datensammlerapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Switch;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Datensammlung extends AppCompatActivity {

    DataRecord datenaufnahme = new DataRecord();

    SensorManager sensorManager;
    SensorEventListener sensorEventListener;
    LocationManager locationManager;
    LocationListener locationListener;


    /**
     * Angabe ob Messung des Accelerometer aktiv ist
     */
    Boolean swchAcState = false;
    Switch swchAc;
    TextView acValX;
    TextView acValY;
    TextView acValZ;

    /**
     * Angabe ob Messung des Gyroscope aktiv ist
     */
    Boolean swchGyState;
    Switch swchGy;
    TextView gyValX;
    TextView gyValY;
    TextView gyValZ;

    /**
     * Angabe ob Messung der GPS-Daten aktiv ist
     */
    Boolean swchLoState;
    Switch swchLo;
    TextView loVaLo;
    TextView loValLa;

    /**
     * Angabe ob Messung der Lichsensoren aktiv ist
     */
    Boolean swchLiState;
    Switch swchLi;
    TextView liValCa;

    /**
     * Angabe ob die Daten aufgezeichnet werden
     */
    Boolean swchReState = false;
    Switch swchRe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datensammlung);
        setUpIDs();
        setUpSensorManager();
        setUpSwitch();
        setUpLocationManager();
    }

    private void setUpLocationManager() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocationListener(getApplicationContext(),loVaLo,loValLa);
    }


    private void setUpSwitch() {
        //Record: sollen die Daten aufgezeichnet werden
        swchRe.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                swchReState = swchRe.isChecked();
                if (swchRe.isChecked()) {
                    datenaufnahme.startRecordDate();
                } else {
                    datenaufnahme.stopRecordDate();
                }
            }
        });
        //Auswertung welche Sensordaten gemessen werden sollen
        swchAc.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                swchAcState = swchAc.isChecked();
                if (swchAc.isChecked()) {
                    registerListener(SensorTyp.ACCELEROMETER, 1000000);
                } else {
                    deaktivateListener(SensorTyp.ACCELEROMETER);
                }
            }
        });
        swchGy.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                swchGyState = swchGy.isChecked();
                if (swchGy.isChecked()) {
                    registerListener(SensorTyp.GYROSCOPE, 1000000); //TODO Zeit auslesen und mit uebergeben
                } else {
                    deaktivateListener(SensorTyp.GYROSCOPE);
                }
            }
        });
        swchLo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                swchLoState = swchLo.isChecked();
                if (swchLo.isChecked()) {
                    registerListener(SensorTyp.LOCATION, 1000000); //TODO Zeit auslesen und mit uebergeben
                } else {
                    deaktivateListener(SensorTyp.LOCATION);
                }
            }
        });
        swchLi.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                swchLiState = swchLi.isChecked();
                if (swchLi.isChecked()) {
                    registerListener(SensorTyp.LIGHT, 1000000); //TODO Zeit auslesen und mit uebergeben
                } else {
                    deaktivateListener(SensorTyp.LIGHT);
                }
            }
        });


    }

    private void setUpSensorManager() {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }

            @Override
            public void onSensorChanged(SensorEvent event) {
                switch (event.sensor.getType()) {
                    //Beschleunigungssensor Drehmoment Winkelgeschwindigkeit
                    case Sensor.TYPE_GYROSCOPE:
                        gyValX.setText("X " + event.values[0] );
                        gyValY.setText("Y " + event.values[1] );
                        gyValZ.setText("Z " + event.values[2] );
                        break;
                    //Bewegungssensor Liniar
                    case Sensor.TYPE_ACCELEROMETER:
                        acValX.setText("X " + event.values[0] );
                        acValY.setText("Y " + event.values[1] );
                        acValZ.setText("Z " + event.values[2] );
                        break;
                    //Lichsensor
                    case Sensor.TYPE_LIGHT:
                        liValCa.setText("Lichtwert" + event.values[0]);
                        break;
                }

            }
        };

    }

    private void registerListener(SensorTyp sensorTyp, int time) {
        if (sensorTyp == SensorTyp.ACCELEROMETER) {
            sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), time);
        } else if (sensorTyp == SensorTyp.GYROSCOPE) {
            sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), time);
        } else if (sensorTyp == SensorTyp.LIGHT) {
            sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT), time);
        } else if (sensorTyp == SensorTyp.LOCATION) {
            //Prüfe ob Berechtigung für GPS vorliegt
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                swchLo.setChecked(false);
                swchLoState = false; //FIXME könnte Event auslösen
                // TODO Fehlermeldung anzeigen, da Berechtiung nicht vorhanden ist,
                // TODO prüfe ob hier deaktiviere Listener durch das Ändern der Variable ausgelöst werden kann
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            //Pruefe ob GPS aktiviert ist
            if (!locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                swchLo.setChecked(false);
                swchLoState = false; //FIXME könnte Event auslösen
                // TODO Fehlermeldung anzeigen, das GPS ausgeschaltet ist
                // TODO prüfe ob hier deaktiviere Listener durch das Ändern der Variable ausgelöst werden kann
                return;
            }
            //minDistance Angabe in Meter
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, time, 1, locationListener);
        } else {
            throw new RuntimeException("SensorTyp is not defined");
        }
    }

    private void deaktivateListener(SensorTyp sensorTyp) {
        if (sensorTyp == SensorTyp.ACCELEROMETER) {
            sensorManager.unregisterListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
        } else if (sensorTyp == SensorTyp.GYROSCOPE) {
            sensorManager.unregisterListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE));
        } else if (sensorTyp == SensorTyp.LIGHT) {
            sensorManager.unregisterListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT));
        } else if (sensorTyp == SensorTyp.LOCATION) {
            locationManager.removeUpdates(locationListener);
        } else {
            throw new RuntimeException("SensorTyp is not defined");
        }
    }

    protected void setUpIDs() {
        //ID's for Accelerometer

        //ID's for Gyroscope

        //ID's for Localisation

        //Sonstiges
        //swchRe = (Switch) findViewById(R.id.swchRe);

    }
}
