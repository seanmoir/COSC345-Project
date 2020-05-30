package com.app.boozespy;

import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private String address = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
     public void onEnter(View view) {
         EditText editBox = findViewById(R.id.address);
         address = editBox.getText().toString();
         System.out.println(address);
     }
}
