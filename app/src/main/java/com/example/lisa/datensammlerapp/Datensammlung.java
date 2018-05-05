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
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.io.IOException;
import java.util.LinkedList;

public class Datensammlung extends AppCompatActivity {

    DataRecord datenaufnahme = new DataRecord();
    //Test
    SensorManager sensorManager;
    SensorEventListener sensorEventListener;
    LocationManager locationManager;
    LocationListener locationListener;
    LinkedList<Integer> frequenzbereich;

    /**
     * Angabe ob Messung des Accelerometer aktiv ist
     */
    Boolean swchAcState = false;
    Switch swchAc;
    TextView acValX;
    TextView acValY;
    TextView acValZ;
    Spinner acHz;
    Integer acFrequenz=1000000;

    /**
     * Angabe ob Messung des Gyroscope aktiv ist
     */
    Boolean swchGyState;
    Switch swchGy;
    TextView gyValX;
    TextView gyValY;
    TextView gyValZ;
    Spinner gyHz;
    Integer gyFrequenz=1000000;

    /**
     * Angabe ob Messung der GPS-Daten aktiv ist
     */
    Boolean swchLoState;
    Switch swchLo;
    TextView loValLo;
    TextView loValLa;



    /**
     * Angabe ob Messung der Lichsensoren aktiv ist
     */
    Boolean swchLiState;
    Switch swchLi;
    TextView liValCa;
    Spinner liHz;
    Integer liFrequenz=1000000;

    /**
     * Angabe ob die Daten aufgezeichnet werden
     */
    Boolean swchReState = false;
    Switch swchRe;

    String androidId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datensammlung);
        setUpIDs();
        setUpFreuenzBereich();
        setUpSensorManager();
        setUpLocationManager();
        setUpSwitch();
        androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    private void setUpFreuenzBereich() {
        frequenzbereich=new LinkedList<>();
        frequenzbereich.add(1000000); //1 Sekunde
        frequenzbereich.add(250000); //viertel Selkunde
        frequenzbereich.add(100000); // 1/10 Sekunde
        frequenzbereich.add(10000); // 1/100 Sekunde

        ArrayAdapter<Integer>  frequenzAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, frequenzbereich);
        frequenzAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        acHz.setAdapter(frequenzAdapter);
        gyHz.setAdapter(frequenzAdapter);
        liHz.setAdapter(frequenzAdapter);

        acHz.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                acFrequenz=Integer.valueOf(String.valueOf(acHz.getSelectedItem()));
                //pruefe ob Messung laeut falls ja, deaktiviere Listener und aktiviere sie neu
                if(swchAc.isChecked())
                {
                    deaktivateListener(SensorTyp.ACCELEROMETER);
                    registerListener(SensorTyp.ACCELEROMETER, acFrequenz);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        gyHz.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                gyFrequenz=Integer.valueOf(String.valueOf(gyHz.getSelectedItem()));
                //pruefe ob Messung laeut falls ja, deaktiviere Listener und aktiviere sie neu
                if(swchAc.isChecked())
                {
                    deaktivateListener(SensorTyp.GYROSCOPE);
                    registerListener(SensorTyp.GYROSCOPE, gyFrequenz);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        liHz.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                liFrequenz=Integer.valueOf(String.valueOf(liHz.getSelectedItem()));
                //pruefe ob Messung laeut falls ja, deaktiviere Listener und aktiviere sie neu
                if(swchAc.isChecked())
                {
                    deaktivateListener(SensorTyp.LIGHT);
                    registerListener(SensorTyp.LIGHT, gyFrequenz);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


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
                    datenaufnahme.startRecordDate(androidId);
                    //acValX.setText(androidId);
                } else {
                    datenaufnahme.stopRecordDate(androidId);
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
                    registerListener(SensorTyp.ACCELEROMETER, acFrequenz);
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
                    registerListener(SensorTyp.GYROSCOPE, gyFrequenz);
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
                    registerListener(SensorTyp.LOCATION, 1000000);
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
                    registerListener(SensorTyp.LIGHT, liFrequenz);
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
                        gyValX.setText(""+ event.values[0] );
                        gyValY.setText(""+event.values[1] );
                        gyValZ.setText(""+  event.values[2] );
                        if (swchReState)
                        {
                            datenaufnahme.recordGyroscope(androidId, event.values[0], event.values[1], event.values[2]);
                        }
                        break;
                    //Bewegungssensor Liniar
                    case Sensor.TYPE_ACCELEROMETER:
                        acValX.setText("" + event.values[0] );
                        acValY.setText("" + event.values[1] );
                        acValZ.setText("" + event.values[2] );
                        if (swchReState)
                        {
                            datenaufnahme.recordAccelometer(androidId, event.values[0], event.values[1], event.values[2]);
                        }
                        break;
                    //Lichsensor
                    case Sensor.TYPE_LIGHT:
                        liValCa.setText("" + event.values[0]);
                        if (swchReState)
                        {
                            datenaufnahme.recordLight(androidId, event.values[0]);
                        }
                        break;
                }

            }
        };

    }

    /**
     * Aktiviert das Aufzeichnen der Daten
     * @param sensorTyp ACCELEROMETER, GYROSCOPE, LOCATION, LIGHT
     * @param time Zeit in Microsekunden
     */
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
        acHz = findViewById(R.id.acHz);

        //ID's for Gyroscope
        swchGy = findViewById(R.id.swchGy);
        gyValX = findViewById(R.id.gyValX);
        gyValY = findViewById(R.id.gyValY);
        gyValZ = findViewById(R.id.gyValZ);
        gyHz = findViewById(R.id.gyHz);

        //ID's for Localisation
        swchLo = findViewById(R.id.swchLo);
        loValLo = findViewById(R.id.loValLo);
        loValLa=findViewById(R.id.loValLa);


        //Id's für Licht
        swchLi = findViewById(R.id.swchLi);
        liValCa = findViewById(R.id.liValCa);
        liHz = findViewById(R.id.liHz);

        //Sonstiges
        //Record
        swchRe = findViewById(R.id.swchRe);

    }
}