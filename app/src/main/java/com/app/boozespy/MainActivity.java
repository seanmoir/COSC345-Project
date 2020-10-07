package com.app.boozespy;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Collections;
import java.util.List;

/**
 * Main entry point for application, setups UI, handles keyboard listeners
 *
 * @author Sean Moir, Ubaada
 */
public class MainActivity extends AppCompatActivity {

    private FusedLocationProviderClient locationClient;
    private String locationText;

    /**
     * Empty constructor
     */
    public MainActivity() {
    }

    /**
     * called on start of the app, onCreate hook
     *
     * @param savedInstanceState saved state of program
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        locationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_DENIED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_DENIED) {

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
                                locationText = location.getLatitude() + ", " + location.getLongitude();
                                System.out.println("LOCATION: " + locationText);
                            } else {
                                //if failed to get location, use geology building
                                locationText = "-45.865022,170.515118";
                                System.out.println("Location: NULL, thus use geology building for reference");
                            }
                        }
                    });
        } else {
            System.out.println("FAILED DUE TO LACK OF PERMISSIONS, thus use geology building");
            locationText = "-45.865022,170.515118";
        }

        setSearchBoxBehavior();
        configureProductViewer();
        showNoSearchYet();
    }


    /**
     * Configures the search box to start web scrapping when the enters a search term.
     */
    public void setSearchBoxBehavior() {
        final EditText search = findViewById(R.id.searchBox);

        search.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER) &&
                        (search.getText().length() > 0)) {
                    // Perform action on key press
                    v.clearFocus();
                    hideSoftKeyboard();
                    showSearchInProgress();
                    new DownloadProducts(MainActivity.this).execute(new DownloadParams(search.getText().toString(), locationText));
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * Hides Keyboard
     */
    public void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    /**
     * Configure the product viewer to display product cards.
     */
    public void configureProductViewer() {
        RecyclerView productViewer = findViewById(R.id.productViewer);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        productViewer.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        productViewer.setLayoutManager(layoutManager);
    }


    /**
     * This web scrapper fires this method after scrapping is done.
     *
     * @param products Products downloaded from the web
     */
    public void updateCards(List<Product> products) {
        // Sorting logic can be placed here, sorting only by price for now
        int i, j, min_idx;
        int n = products.size();
        for (i = 0; i < n-1; i++)
        {
            min_idx = i;
            for (j = i+1; j < n; j++)
                if (products.get(j).getPrice() < products.get(min_idx).getPrice())
                    min_idx = j;
            Collections.swap(products,min_idx,i);
        }

        // Set the ProductViewers data source to the returned result
        ProductAdapter myAdapter = new ProductAdapter(products, MainActivity.this);
        if (products.isEmpty()) {
            showNoDataFound();
        } else {
            RecyclerView productViewer = findViewById(R.id.productViewer);
            productViewer.setAdapter(myAdapter);
            System.out.println(products);
            showProductViewer();
        }

    }


    /**
     * First condition after opening app.
     * Hide the empty productViewer and show "instruction image" in progress Group
     */
    public void showNoSearchYet() {
        findViewById(R.id.productViewer).setVisibility(View.INVISIBLE);
        findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);

        ImageView messageImage = findViewById(R.id.messageImage);
        Resources res = getResources();
        messageImage.setImageDrawable(res.getDrawable(R.drawable.ic_drink));
        messageImage.setVisibility(View.VISIBLE);

        findViewById(R.id.progressGroup).setVisibility(View.VISIBLE);
    }


    /**
     * Hide old products and show progressBar inside progressGroup
     */
    public void showSearchInProgress() {
        findViewById(R.id.productViewer).setVisibility(View.INVISIBLE);
        findViewById(R.id.messageImage).setVisibility(View.INVISIBLE);
        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
        findViewById(R.id.progressGroup).setVisibility(View.VISIBLE);
    }


    /**
     * Hide the progressGroup and show the results
     */
    public void showProductViewer() {
        findViewById(R.id.productViewer).setVisibility(View.VISIBLE);
        findViewById(R.id.progressGroup).setVisibility(View.INVISIBLE);
    }


    /**
     * Search completed but no data to show
     * Hide results and show error image in progressGroup
     */
    public void showNoDataFound() {
        findViewById(R.id.productViewer).setVisibility(View.INVISIBLE);
        findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);

        ImageView messageImage = findViewById(R.id.messageImage);
        Resources res = getResources();
        messageImage.setImageDrawable(res.getDrawable(R.drawable.no_data));
        messageImage.setVisibility(View.VISIBLE);

        findViewById(R.id.progressGroup).setVisibility(View.VISIBLE);
    }
}
