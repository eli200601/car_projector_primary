package com.app.elisoft.carprojectprimary;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.app.elisoft.carprojectprimary.Fragment.MainDashboardFragment;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Car Projector");
        setSupportActionBar(toolbar);

        getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new MainDashboardFragment()).commit();

    }



}
