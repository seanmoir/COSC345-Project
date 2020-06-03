package com.app.boozespy;

import android.app.Activity;
import android.location.Location;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private FusedLocationProviderClient locationClient;
    // Reference to product viewer to referesh data source upon each search
    RecyclerView recyclerView;

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
                        e.printStackTrace();
                    }
                })

                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            gpsLocation.setText(location.toString());
                        } else {
                            //debugging so we know if location is returned NULL
                            gpsLocation.setText("NULL");
                        }
                    }
                });

        setSearchBoxBehavior();
        configureProductViewer();
    }


    /*
    Configures the search box to start web scrapping when the enters a search term.
     */
    public void setSearchBoxBehavior() {
        final EditText search = findViewById(R.id.searchItem);

        search.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER) &&
                        (search.getText().length() > 0)) {
                    // Perform action on key press

                    new DownloadProducts(MainActivity.this).execute(search.getText().toString());
                    return true;
                }
                return false;
            }
        });
    }

    /*
    Configure the product viewer to display product cards.
     */
    public void configureProductViewer() {
        recyclerView = (RecyclerView) findViewById(R.id.productViewer);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    /*
    This web scrapper fires this method after scrapping is done.
     */
    public void updateCards(List<Product> products) {
        // Sorting logic can be placed here

        // Set the ProductViewers data source to the returned result
        ProductAdapter myAdapter = new ProductAdapter(products);
        recyclerView.setAdapter(myAdapter);
        System.out.println(products);
    }
}
