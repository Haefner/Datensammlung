package com.example.lisa.datensammlerapp;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import java.util.LinkedList;

public class Datensammlung extends AppCompatActivity {

    DataRecord datenaufnahme;
    //Test
    SensorManager sensorManager;
    SensorEventListener sensorEventListener;
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
    Integer acFrequenz = 1000000;

    /**
     * Angabe ob Messung des Gyroscope aktiv ist
     */
    Boolean swchGyState;
    Switch swchGy;
    TextView gyValX;
    TextView gyValY;
    TextView gyValZ;
    Spinner gyHz;
    Integer gyFrequenz = 1000000;

    /**
     * Angabe ob Messung des Rotation aktiv ist
     */
    Boolean swchCoState;
    Switch swchCo;
    TextView coValX;
    TextView coValY;
    TextView coValZ;
    TextView degrees;
    Spinner coHz;
    Integer coFrequenz = 1000000;

    /**
     * Angabe ob Messung des Kompass aktiv ist
     */
    Boolean swchRoState;
    Switch swchRo;
    TextView roValX;
    TextView roValY;
    TextView roValZ;
    Spinner roHz;
    Integer roFrequenz = 1000000;

    /**
     * Angabe ob die Daten aufgezeichnet werden
     */
    Boolean swchReState = false;
    Switch swchRe;

    /**
     * Angabe wie viele Datensätze aufgezeichnet wurden vs übertragene
     */
    TextView daVal;
    int zuUebertragen = 0;

    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            timerHandler.postDelayed(this, 200);
            if (!daVal.getText().toString().startsWith("Fehler:")) {
                daVal.setText(daVal.getText().toString().replaceAll("/[0-9]+", "/" + zuUebertragen));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datensammlung);
        setUpIDs();
        setUpFreuenzBereich();
        setUpSensorManager();
       // setUpLocationManager();
        setUpSwitch();
        datenaufnahme = new DataRecord(this, Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    private void setUpFreuenzBereich() {
        frequenzbereich = new LinkedList<>();
        frequenzbereich.add(1000000); //1 Sekunde
        frequenzbereich.add(250000); //viertel Selkunde
        frequenzbereich.add(100000); // 1/10 Sekunde
        frequenzbereich.add(10000); // 1/100 Sekunde

        ArrayAdapter<Integer> frequenzAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, frequenzbereich);
        frequenzAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        acHz.setAdapter(frequenzAdapter);
        gyHz.setAdapter(frequenzAdapter);
        roHz.setAdapter(frequenzAdapter);
        coHz.setAdapter(frequenzAdapter);

        acHz.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                acFrequenz = Integer.valueOf(String.valueOf(acHz.getSelectedItem()));
                //pruefe ob Messung laeut falls ja, deaktiviere Listener und aktiviere sie neu
                if (swchAc.isChecked()) {
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

                gyFrequenz = Integer.valueOf(String.valueOf(gyHz.getSelectedItem()));
                //pruefe ob Messung laeut falls ja, deaktiviere Listener und aktiviere sie neu
                if (swchGy.isChecked()) {
                    deaktivateListener(SensorTyp.GYROSCOPE);
                    registerListener(SensorTyp.GYROSCOPE, gyFrequenz);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        roHz.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                roFrequenz = Integer.valueOf(String.valueOf(roHz.getSelectedItem()));
                //pruefe ob Messung laeut falls ja, deaktiviere Listener und aktiviere sie neu
                if (swchRo.isChecked()) {
                    deaktivateListener(SensorTyp.ROTATION);
                    registerListener(SensorTyp.ROTATION, roFrequenz);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        coHz.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                coFrequenz = Integer.valueOf(String.valueOf(coHz.getSelectedItem()));
                //pruefe ob Messung laeut falls ja, deaktiviere Listener und aktiviere sie neu
                if (swchCo.isChecked()) {
                    deaktivateListener(SensorTyp.COMPASS);
                    registerListener(SensorTyp.COMPASS, coFrequenz);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    }

//    private void setUpLocationManager() {
//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        locationListener = new MyLocationListener(getApplicationContext(), loValLo, loValLa);
//    }


    private void setUpSwitch() {
        //Record: sollen die Daten aufgezeichnet werden
        swchRe.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                swchReState = swchRe.isChecked();
                if (swchRe.isChecked()) {
                    daVal.setText("0/0");
                    datenaufnahme.startRecordDate();
                    timerHandler.postDelayed(timerRunnable, 200);
                    //acValX.setText(androidId);
                } else {
                    datenaufnahme.stopRecordDate();
                    zuUebertragen = 0;
                    timerHandler.removeCallbacks(timerRunnable);
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
        //Compss
        swchCo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                swchCoState = swchCo.isChecked();
                if (swchCo.isChecked()) {
                    registerListener(SensorTyp.COMPASS, coFrequenz);
                } else {
                    deaktivateListener(SensorTyp.COMPASS);
                }
            }
        });
        //Rotation
        swchRo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                swchRoState = swchRo.isChecked();
                if (swchRo.isChecked()) {
                    registerListener(SensorTyp.ROTATION, roFrequenz);
                } else {
                    deaktivateListener(SensorTyp.ROTATION);
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
                        gyValX.setText("" + event.values[0]);
                        gyValY.setText("" + event.values[1]);
                        gyValZ.setText("" + event.values[2]);
                        if (swchReState) {
                            datenaufnahme.recordGyroscope(event.values[0], event.values[1], event.values[2]);
                            zuUebertragen +=1;
                            /*if (!daVal.getText().toString().startsWith("Fehler:")) {
                                String tmp[] = daVal.getText().toString().split("/");
                                daVal.setText(daVal.getText().toString().replaceAll("/[0-9]+", "/" + (Integer.parseInt(tmp[1]) + 1)));
                            }*/
                        }
                        break;
                    //Bewegungssensor Liniar
                    case Sensor.TYPE_ACCELEROMETER:
                        acValX.setText("" + event.values[0]);
                        acValY.setText("" + event.values[1]);
                        acValZ.setText("" + event.values[2]);
                        if (swchReState) {
                            datenaufnahme.recordAccelometer(event.values[0], event.values[1], event.values[2]);
                            zuUebertragen +=1;
                            /*if (!daVal.getText().toString().startsWith("Fehler:")) {
                                String tmp[] = daVal.getText().toString().split("/");
                                daVal.setText(daVal.getText().toString().replaceAll("/[0-9]+", "/" + (Integer.parseInt(tmp[1]) + 1)));
                            }*/
                        }
                        break;
                    //Rotation
                    case Sensor.TYPE_ROTATION_VECTOR:
                        roValX.setText("" + event.values[0]);
                        roValY.setText("" + event.values[1]);
                        roValZ.setText("" + event.values[2]);
                        if (swchReState) {
                            datenaufnahme.recordRotation(event.values[0], event.values[1], event.values[2]);
                            zuUebertragen +=1;
                            /*
                            if (!daVal.getText().toString().startsWith("Fehler:")) {
                                String tmp[] = daVal.getText().toString().split("/");
                                daVal.setText(daVal.getText().toString().replaceAll("/[0-9]+", "/" + (Integer.parseInt(tmp[1]) + 1)));
                            }*/
                        }

                        //Berechne Winkel
                        float[] orientation = new float[3];
                        float[] rMat = new float[9];
                        // calculate th rotation matrix
                        SensorManager.getRotationMatrixFromVector(rMat, event.values);
                        // get the azimuth value (orientation[0]) in degree
                        int grad=(int) (Math.toDegrees(SensorManager.getOrientation(rMat, orientation)[0]) + 360) % 360;
                        degrees.setText(Integer.toString(grad));
                        break;
                    //Compass
                    case Sensor.TYPE_MAGNETIC_FIELD:
                        coValX.setText("" + event.values[0]);
                        coValY.setText("" + event.values[1]);
                        coValZ.setText("" + event.values[2]);
                        if (swchReState) {
                            datenaufnahme.recordCompass(event.values[0], event.values[1], event.values[2]);
                            zuUebertragen +=1;
                            /*if (!daVal.getText().toString().startsWith("Fehler:")) {
                                String tmp[] = daVal.getText().toString().split("/");
                                daVal.setText(daVal.getText().toString().replaceAll("/[0-9]+", "/" + (Integer.parseInt(tmp[1]) + 1)));
                            }*/
                        }
                        break;
                }

            }
        };

    }

    /**
     * Aktiviert das Aufzeichnen der Daten
     *
     * @param sensorTyp ACCELEROMETER, GYROSCOPE, LOCATION, LIGHT
     * @param time      Zeit in Microsekunden
     */
    private void registerListener(SensorTyp sensorTyp, int time) {
        if (sensorTyp == SensorTyp.ACCELEROMETER) {
            sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), time);
        } else if (sensorTyp == SensorTyp.GYROSCOPE) {
            sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), time);
        } else if (sensorTyp == SensorTyp.ROTATION) {
            sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR), time);
        } else if (sensorTyp == SensorTyp.COMPASS) {
            sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), time);
        }
//        } else if (sensorTyp == SensorTyp.LOCATION) {
//            //Pruefe ob Berechtigung fuer GPS vorliegt
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                swchLo.setChecked(false);
//                swchLoState = false;
//                // Meldung anzeigen, dass die Berechtiung nicht vorhanden ist, und erteilt werden muss
//                ActivityCompat.requestPermissions(Datensammlung.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//                return;
//            }
//            //Pruefe ob GPS aktiviert ist
//            if (!locationManager
//                    .isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//                swchLo.setChecked(false);
//                swchLoState = false;
//                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
//                return;
//            }
//            //minDistance Angabe in Meter
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, time, 0.001f, locationListener);
//
//        }
        else {
            throw new RuntimeException("SensorTyp is not defined");
        }
    }

    private void deaktivateListener(SensorTyp sensorTyp) {
        if (sensorTyp == SensorTyp.ACCELEROMETER) {
            sensorManager.unregisterListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
        } else if (sensorTyp == SensorTyp.GYROSCOPE) {
            sensorManager.unregisterListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE));
        } else if (sensorTyp == SensorTyp.ROTATION) {
            sensorManager.unregisterListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR));
        } else if (sensorTyp == SensorTyp.COMPASS) {
            sensorManager.unregisterListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD));
//        } else if (sensorTyp == SensorTyp.LOCATION) {
//            locationManager.removeUpdates(locationListener);
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
        acValX.setText("");
        acValY.setText("");
        acValZ.setText("");
        acHz = findViewById(R.id.acHz);

        //ID's for Gyroscope
        swchGy = findViewById(R.id.swchGy);
        gyValX = findViewById(R.id.gyValX);
        gyValY = findViewById(R.id.gyValY);
        gyValZ = findViewById(R.id.gyValZ);
        gyValX.setText("");
        gyValY.setText("");
        gyValZ.setText("");
        gyHz = findViewById(R.id.gyHz);

        //ID's for Rotation
        swchRo = findViewById(R.id.swchRo);
        roValX = findViewById(R.id.roValX);
        roValY = findViewById(R.id.roValY);
        roValZ = findViewById(R.id.roValZ);
        roValX.setText("");
        roValY.setText("");
        roValZ.setText("");
        degrees = findViewById(R.id.degrees);
        roHz = findViewById(R.id.roHz);

        //ID's for Compass
        swchCo = findViewById(R.id.swchCo);
        coValX = findViewById(R.id.coValX);
        coValY = findViewById(R.id.coValY);
        coValZ = findViewById(R.id.coValZ);
        coValX.setText("");
        coValY.setText("");
        coValZ.setText("");
        coHz = findViewById(R.id.coHz);

        //Sonstiges
        //Record
        swchRe = findViewById(R.id.swchRe);
        daVal = findViewById(R.id.daVal);
        daVal.setText("");

    }
}