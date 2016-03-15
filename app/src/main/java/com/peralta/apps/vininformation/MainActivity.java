package com.peralta.apps.vininformation;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import utils.CheckNetwork;

public class MainActivity extends AppCompatActivity {
   // Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(!CheckNetwork.isInternetAvailable(this)) {
            Toast.makeText(this, "Failed to load. Internet connection is required", Toast.LENGTH_SHORT).show();
        }
        else{
            getFragmentManager().beginTransaction().add(R.id.container, new VinFragment()).commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String msg = "";
        switch (item.getItemId()){
            case R.id.about:
                msg = "About";
                Log.v("About option clicked", String.valueOf(item.getItemId()));
                break;
            case R.id.exit:
                msg = "Exit";
                Log.v("Exit option clicked", String.valueOf(item.getItemId()));
                finish();
                break;
        }
        Toast.makeText(this, msg + " Clicked",Toast.LENGTH_LONG).show();
        return super.onOptionsItemSelected(item);
    }
}
