package com.app.elisoft.carprojectprimary.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.app.elisoft.carprojectprimary.MainActivity;
import com.app.elisoft.carprojectprimary.R;
import com.app.elisoft.carprojectprimary.Utils.Keys;
import com.app.elisoft.carprojectprimary.Utils.SharedPrefsUtils;


public class OnBoardingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_on_boarding);

        Button dashboard = (Button) findViewById(R.id.button_dashboard);
        Button projector = (Button) findViewById(R.id.button_projector);

        dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPrefsUtils.setStringPreference(getApplicationContext(), Keys.PREF_ACTIVATE, Keys.PREF_ACTIVATE);
                SharedPrefsUtils.setStringPreference(getApplicationContext(), Keys.PREF_VIEW_TYPE, Keys.VIEW_DASHBOARD);

                Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(myIntent);
                finish();
            }
        });

        projector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPrefsUtils.setStringPreference(getApplicationContext(), Keys.PREF_ACTIVATE, Keys.PREF_ACTIVATE);
                SharedPrefsUtils.setStringPreference(getApplicationContext(), Keys.PREF_VIEW_TYPE, Keys.VIEW_PROJECTOR);

                Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(myIntent);
                finish();
            }
        });
    }


}
