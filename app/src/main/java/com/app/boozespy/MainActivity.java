package com.app.boozespy;

import android.location.Location;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class MainActivity extends AppCompatActivity {
    private FusedLocationProviderClient locationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationClient = LocationServices.getFusedLocationProviderClient(this);

        final TextView gpsLocation = findViewById(R.id.gpsPrintOut);

        locationClient.getLastLocation()
                //print why failed to get location to stdout for debugging purposes
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println(e);
                    }
                })

                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            gpsLocation.setText(location.toString());
                        } else {
                            gpsLocation.setText("NULL");
                        }
                    }
                });
    }
}
