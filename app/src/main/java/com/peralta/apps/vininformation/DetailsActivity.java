package com.peralta.apps.vininformation;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.w3c.dom.Text;

public class DetailsActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView makeView;
    private TextView modelView;
    private TextView driveView;
    private TextView transmissionTypeView;
    private TextView numberOfSpeedsView;
    private TextView doorsView;
    private TextView mpgHighwayView;
    private TextView mpgCityView;
    private TextView squishVinView;
    private TextView cylinderView;
    private TextView horsePowerView;
    private TextView littersView;
    private TextView yearView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getViewValues();
        getIntentData();
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
                new AlertDialog.Builder(this)
                        .setMessage("Message")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //code if yes
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
                break;
            case R.id.exit:
                msg = "Exit";
                Log.v("Exit option clicked", String.valueOf(item.getItemId()));
                finish();
                break;
        }
        Toast.makeText(this, msg + " Clicked", Toast.LENGTH_LONG).show();
        return super.onOptionsItemSelected(item);
    }


    private void getIntentData() {

        Intent intent = getIntent();
        String make = intent.getStringExtra("make");
        String model = intent.getStringExtra("model");
        String drive = intent.getStringExtra("drive");
        String transmissionType = intent.getStringExtra("transmissionType");
        String numberOfSpeeds = intent.getStringExtra("numberOfSpeeds");
        String doors = intent.getStringExtra("doors");
        String mpgHighway = intent.getStringExtra("mpgHighway");
        String mpgCity = intent.getStringExtra("mpgCity");
        String squishVin = intent.getStringExtra("squishVin");
        String cylinder = intent.getStringExtra("cylinder");
        String horsePower = intent.getStringExtra("horsepower");
        String litters = intent.getStringExtra("litters");
        String year = intent.getStringExtra("year");

        makeView.setText(make);
        modelView.setText(model);
        driveView.setText(drive);
        transmissionTypeView.setText(transmissionType);
        numberOfSpeedsView.setText(numberOfSpeeds);
        doorsView.setText(doors);
        mpgHighwayView.setText(mpgHighway);
        mpgCityView.setText(mpgCity);
       // squishVinView.setText(squishVin);
        cylinderView.setText(cylinder);
        horsePowerView.setText(horsePower);
        littersView.setText(litters);
        yearView.setText(year);

    }

    private void getViewValues(){
        makeView = (TextView) findViewById(R.id.makeTextView);
        modelView = (TextView) findViewById(R.id.modelTextView);
        driveView = (TextView) findViewById(R.id.driveTextView);
        transmissionTypeView = (TextView) findViewById(R.id.transmissionTypeView);
        numberOfSpeedsView = (TextView) findViewById(R.id.numberOfSpeedView);
        doorsView = (TextView) findViewById(R.id.doorsView);
        mpgHighwayView = (TextView) findViewById(R.id.mpgHighwayView);
        mpgCityView = (TextView) findViewById(R.id.mpgCityView);
        cylinderView = (TextView) findViewById(R.id.cylinderView);
        horsePowerView = (TextView) findViewById(R.id.horsePowerView);
        littersView = (TextView) findViewById(R.id.littersView);
        yearView = (TextView) findViewById(R.id.yearView);
    }
}
