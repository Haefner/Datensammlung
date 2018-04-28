package com.example.lisa.datensammlerapp;

import android.content.Context;
import android.location.Address;

import android.location.Geocoder;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MyLocationListener implements LocationListener {
    private Context applicationContext;

    TextView loValLo;
    TextView loValLa;

    private String city;
    private String state;
    private String country;
    private String postalCode;
    private String knownName;
    private String longitude;
    private String latitude;

    public MyLocationListener(Context context, TextView loValLo, TextView loValLa)
    {
        this.applicationContext=context;
        this.loValLo = loValLo;
        this.loValLa = loValLa;
    }


    private void findAddressValues(android.location.Location loc) {
        longitude =""+ loc.getLongitude();
        latitude = ""+ loc.getLatitude();

        Geocoder gcd = new Geocoder(applicationContext, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(loc.getLatitude(),
                    loc.getLongitude(), 1);
            if (addresses.size() > 0) {
                 city = addresses.get(0).getLocality();
                 state = addresses.get(0).getAdminArea();
                 country = addresses.get(0).getCountryName();
                 postalCode = addresses.get(0).getPostalCode();
                 knownName = addresses.get(0).getFeatureName();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(android.location.Location location) {

        findAddressValues(location);
        loValLa.setText(loValLa.toString());
        loValLo.setText(loValLo.toString());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Location: ");
        sb.append(", longitude='").append(longitude).append('\'');
        sb.append(", latitude='").append(latitude).append('\'');
        sb.append(", city='").append(city).append('\'');
        sb.append(", state='").append(state).append('\'');
        sb.append(", country='").append(country).append('\'');
        sb.append(", postalCode='").append(postalCode).append('\'');
        sb.append(", knownName='").append(knownName).append('\'');
        return sb.toString();
    }
}
