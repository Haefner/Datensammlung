package com.example.lisa.datensammlerapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

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
    EditText acHz;

    /**
     * Angabe ob Messung des Gyroscope aktiv ist
     */
    Boolean swchGyState;
    Switch swchGy;
    TextView gyValX;
    TextView gyValY;
    TextView gyValZ;
    EditText gyHz;

    /**
     * Angabe ob Messung der GPS-Daten aktiv ist
     */
    Boolean swchLoState;
    Switch swchLo;
    TextView loValLo;
    TextView loValLa;
    EditText loHz;

    /**
     * Angabe ob Messung der Lichsensoren aktiv ist
     */
    Boolean swchLiState;
    Switch swchLi;
    TextView liValCa;
    EditText liHz;

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
        setUpLocationManager();
        setUpSwitch();
    }

    private void setUpLocationManager() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocationListener(getApplicationContext(), loValLo,loValLa);
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

        //Accelorometer
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
        //Gyroscope
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
        //Location
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
        //Licht
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
            //Pruefe ob Berechtigung fuer GPS vorliegt
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                swchLo.setChecked(false);
                swchLoState = false;
                // Meldung anzeigen, dass die Berechtiung nicht vorhanden ist, und erteilt werden muss
                ActivityCompat.requestPermissions(Datensammlung.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                return;
            }
            //Pruefe ob GPS aktiviert ist
            if (!locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                swchLo.setChecked(false);
                swchLoState = false;
                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                return;
            }
            //minDistance Angabe in Meter
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, time, 0.001f, locationListener);

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
        swchAc = findViewById(R.id.swchAc);
        acValX = findViewById(R.id.acValX);
        acValY = findViewById(R.id.acValY);
        acValZ = findViewById(R.id.acValZ);
        //acHz = findViewById(R.id.acHz);

        //ID's for Gyroscope
        swchGy = findViewById(R.id.swchGy);
        gyValX = findViewById(R.id.gyValX);
        gyValY = findViewById(R.id.gyValY);
        gyValZ = findViewById(R.id.gyValZ);
        //gyHz = findViewById(R.id.gyHz);

        //ID's for Localisation
        swchLo = findViewById(R.id.swchLo);
        loValLo = findViewById(R.id.loValLo);
        loValLa = findViewById(R.id.loValLa);
      //  loHz = findViewById(R.id.loHz);

        //Id's für Licht
        swchLi = findViewById(R.id.swchLi);
        liValCa = findViewById(R.id.liValCa);
      //  liHz = findViewById(R.id.liHz);

        //Sonstiges
        //Record
        swchRe = findViewById(R.id.swchRe);

    }
}
