package com.app.elisoft.carprojectprimary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.app.elisoft.carprojectprimary.Activity.OnBoardingActivity;
import com.app.elisoft.carprojectprimary.Fragment.MainDashboardFragment;
import com.app.elisoft.carprojectprimary.Utils.Keys;
import com.app.elisoft.carprojectprimary.Utils.SharedPrefsUtils;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (SharedPrefsUtils.getStringPreference(getApplicationContext(), Keys.PREF_ACTIVATE) == null) {
            Log.d(TAG, "This is New user");
            Intent myIntent = new Intent(getApplicationContext(), OnBoardingActivity.class);
            this.startActivity(myIntent);
            this.finish();
            return;
        } else {
            Log.d(TAG, "Not New User");
            Log.d(TAG, "This is: " + SharedPrefsUtils.getStringPreference(getApplicationContext(),Keys.PREF_VIEW_TYPE));
        }

        setView();

    }

    public void setView(){
        String viewType = SharedPrefsUtils.getStringPreference(getApplicationContext(), Keys.PREF_VIEW_TYPE);

        switch (viewType) {
            case Keys.VIEW_DASHBOARD: {
                setContentView(R.layout.activity_main);

                toolbar = (Toolbar) findViewById(R.id.toolbar);
                toolbar.setTitle("Car Projector - Dashboard");
                setSupportActionBar(toolbar);

                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new MainDashboardFragment()).commit();
                break;
            }
            case Keys.VIEW_PROJECTOR: {
                // To be done
                requestWindowFeature(Window.FEATURE_NO_TITLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

                setContentView(R.layout.activity_main);

                toolbar = (Toolbar) findViewById(R.id.toolbar);
                toolbar.setTitle("Car Projector - Projector");
                toolbar.setBackgroundColor(getResources().getColor(R.color.colorBlack));
                setSupportActionBar(toolbar);

                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new MainDashboardFragment()).commit();
                break;
            }
        }
    }
}
