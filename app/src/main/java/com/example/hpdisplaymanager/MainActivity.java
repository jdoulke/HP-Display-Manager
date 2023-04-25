package com.example.hpdisplaymanager;

import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

     BottomNavigationView bottomNavigationView;

     LaptopFragment laptopFragment = new LaptopFragment();
     PrinterFragment printerFragment = new PrinterFragment();
     MonitorFragment monitorFragment = new MonitorFragment();
     DesktopFragment desktopFragment = new DesktopFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        navigationUtils();
    }





    @SuppressLint("NonConstantResourceId")
    private void navigationUtils() {

        bottomNavigationView = findViewById(R.id.bottomNavigation);

        getSupportFragmentManager().beginTransaction().replace(R.id.container, laptopFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(item -> {

            Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(40);
            switch (item.getItemId()){

                case R.id.laptops:
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, laptopFragment).commit();
                    break;
                case R.id.printers:
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, printerFragment).commit();
                    break;
                case R.id.monitors:
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, monitorFragment).commit();
                    break;
                case R.id.desktops:
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, desktopFragment).commit();
                    break;
            }

            return true;
        });

    }


    public void buttonsUtils(View view) {

        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(40);
        String idName = getResources().getResourceEntryName(view.getId());
        if(idName.contains("laptop")) {
            if (idName.contains("plus"))
                laptopFragment.plus(idName);
            else
                laptopFragment.minus(idName);
        }else if(idName.contains("desktop")){
            if (idName.contains("plus"))
                desktopFragment.plus(idName);
            else
                desktopFragment.minus(idName);
        }else if(idName.contains("monitor")){
            if (idName.contains("plus"))
                monitorFragment.plus(idName);
            else
                monitorFragment.minus(idName);
        }else {
            if (idName.contains("plus"))
                printerFragment.plus(idName);
            else
                printerFragment.minus(idName);
        }

    }



}