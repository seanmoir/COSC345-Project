package com.app.boozespy;

import android.location.Location;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        final EditText search = findViewById(R.id.searchItem);
        search.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER) &&
                    (search.getText().length() > 0)) {
                    // Perform action on key press
                    try {
                        Toast.makeText(MainActivity.this,
                                LiqourLandProducts(search.getText().toString()).toString(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return true;
                }
                return false;
            }
        });
    }


    //Ubaada's web scrapping work integrated into the app by Sean
    public static List<Product> LiqourLandProducts(String searchTerm) throws IOException {
        List<Product> productList = new ArrayList<>();
        String url = "https://www.shop.liquorland.co.nz/Search.aspx?k=" + searchTerm;
        Map<String, String> cookies = new HashMap<>();
        cookies.put("VisitorIsAdult", "True");
        cookies.put("sessiondob", "01/01/1900");
        cookies.put("selectedStore", "933");
        Document doc = Jsoup.connect(url).cookies(cookies).get();

        Elements scrappedList = doc.getElementsByClass("itemContainer");
        for (Element product : scrappedList) {
            Product newProd = new Product();
            newProd.setName(product.selectFirst(".w2mItemName").text());
            newProd.setPrice(Double.parseDouble(product.selectFirst("span.value , span.SpecialPriceFormat2").text().replace("$","")));
            newProd.setImgUrl("http://www.shop.liquorland.co.nz/" + product.selectFirst("a img").attr("src"));
            newProd.setUrl("http://www.shop.liquorland.co.nz/" + product.selectFirst("a").attr("href"));

            productList.add(newProd);
        }
        return productList;
    }
}
