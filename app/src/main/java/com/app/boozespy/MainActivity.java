package com.app.boozespy;

import android.content.res.Resources;
import android.location.Location;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        locationClient = LocationServices.getFusedLocationProviderClient(this);
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
                            System.out.println(location.toString());
                        } else {
                            //debugging so we know if location is returned NULL
                            System.out.println("Location: NULL");
                        }
                    }
                });

        setSearchBoxBehavior();
        configureProductViewer();
        showNoSearchYet();
    }


    /*
    Configures the search box to start web scrapping when the enters a search term.
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
                    showSearchInProgress();
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
        RecyclerView productViewer = (RecyclerView) findViewById(R.id.productViewer);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        productViewer.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        productViewer.setLayoutManager(layoutManager);
    }

    /*
    This web scrapper fires this method after scrapping is done.
     */
    public void updateCards(List<Product> products) {
        // Sorting logic can be placed here

        // Set the ProductViewers data source to the returned result
        ProductAdapter myAdapter = new ProductAdapter(products);
        if (products.isEmpty()) {
            showNoDataFound();
        } else {
            RecyclerView productViewer = (RecyclerView) findViewById(R.id.productViewer);
            productViewer.setAdapter(myAdapter);
            System.out.println(products);
            showProductViewer();
        }

    }



    /*
    First condition after opening app.
    Hide the empty productViewer and show "instruction image" in progress Group
    */
    public void showNoSearchYet() {
        findViewById(R.id.productViewer).setVisibility(View.INVISIBLE);
        findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);

        ImageView messageImage = (ImageView) findViewById(R.id.messageImage);
        Resources res = getResources();
        messageImage.setImageDrawable(res.getDrawable(R.drawable.ic_drink));
        messageImage.setVisibility(View.VISIBLE);

        findViewById(R.id.progressGroup).setVisibility(View.VISIBLE);
    }

    /*
    Hide old products and show progressBar inside progressGroup
     */
    public void showSearchInProgress() {
        findViewById(R.id.productViewer).setVisibility(View.INVISIBLE);
        findViewById(R.id.messageImage).setVisibility(View.INVISIBLE);
        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
        findViewById(R.id.progressGroup).setVisibility(View.VISIBLE);
    }

    /*
    Hide the progressGroup and show the results
    */
    public void showProductViewer() {
        findViewById(R.id.productViewer).setVisibility(View.VISIBLE);
        findViewById(R.id.progressGroup).setVisibility(View.INVISIBLE);
    }

    /*
    Search completed but no data to show
    Hide results and show error image in progressGroup
     */
    public void showNoDataFound() {
        findViewById(R.id.productViewer).setVisibility(View.INVISIBLE);
        findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);

        ImageView messageImage = (ImageView) findViewById(R.id.messageImage);
        Resources res = getResources();
        messageImage.setImageDrawable(res.getDrawable(R.drawable.no_data));
        messageImage.setVisibility(View.VISIBLE);

        findViewById(R.id.progressGroup).setVisibility(View.VISIBLE);
    }
}
