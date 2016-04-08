package com.peralta.apps.vininformation;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import model.CarDetails;
import model.MechanicalDetails;
import model.PerformanceDetails;

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
    private TextView cylinderView;
    private TextView horsePowerView;
    private TextView littersView;
    private TextView yearView;
    private TextView manufacturerEngineCodeView;
    private TextView valvesView;
    private TextView carNameView;
    private TextView vehicleStyleView;
    private TextView vehicleTypeView;
    private TextView engineCodeView;
    private TextView configurationView;
    private TextView fuelTypeView;

    private ImageView carImage;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getViewValues();
        setUpAdView();
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

        CarDetails carDetailsObject = (CarDetails) intent.getSerializableExtra("carDetailsObject");
        MechanicalDetails mechanicalDetailsObject = (MechanicalDetails) intent.getSerializableExtra("mechanicalDetailsObject");
        PerformanceDetails performanceDetailsObject = (PerformanceDetails) intent.getSerializableExtra("performanceDetailsObject");

        //Values for the CarDetailsObject
        String make = carDetailsObject.getMake();
        String model = carDetailsObject.getModel();
        String drive = carDetailsObject.getDrive();
        String doors = carDetailsObject.getDoors();
        String squishVin = carDetailsObject.getSquishVin();
        String carName = carDetailsObject.getCarName();
        String year = carDetailsObject.getYear();
        String styleId = carDetailsObject.getStyleId();
        String vehicleStyle = carDetailsObject.getVehicleStyle();
        String vehicleType = carDetailsObject.getVehicleType();

        //Values for the MechanicalDetailsObject
        String transmissionType = mechanicalDetailsObject.getTransmissionType();
        String numberOfSpeeds = mechanicalDetailsObject.getNumberOfSpeed();
        String mpgHighway = performanceDetailsObject.getMpgHighway();
        String mpgCity = performanceDetailsObject.getMpgCity();
        String cylinder = mechanicalDetailsObject.getCylinders();
        String horsePower = mechanicalDetailsObject.getHorsePower();
        String litters = mechanicalDetailsObject.getLitters();
        String manufacturerEngineCode = mechanicalDetailsObject.getManufacturerEngineCode();
        String totalValves = mechanicalDetailsObject.getTotalValves();
        String engineCode = mechanicalDetailsObject.getEngineCode();

        //Values for the PerformanceDetailsObject
        String configuration = performanceDetailsObject.getConfiguration();
        String fuelType = performanceDetailsObject.getFuelType();


        Log.v("StyleID Value ", styleId);

        //Brand Segment
        makeView.setText(make);
        modelView.setText(model);
        yearView.setText(year);

        //Details Segment
        carNameView.setText(carName);
        doorsView.setText(doors);
        driveView.setText(drive);
        vehicleStyleView.setText(vehicleStyle);
        vehicleTypeView.setText(vehicleType);

        //Engine Segment
        littersView.setText(litters);
        cylinderView.setText(cylinder);
        horsePowerView.setText(horsePower);
        valvesView.setText(totalValves);
        engineCodeView.setText(engineCode);

        //Mechanical Segment
        transmissionTypeView.setText(transmissionType);
        numberOfSpeedsView.setText(numberOfSpeeds);
        manufacturerEngineCodeView.setText(manufacturerEngineCode);

        //Performance Segment
        mpgHighwayView.setText(mpgHighway);
        mpgCityView.setText(mpgCity);
        configurationView.setText(configuration);
        fuelTypeView.setText(fuelType);;









        CarImageTask task = new CarImageTask();

        if(!styleId.equalsIgnoreCase("Data not found")){
            task.execute(styleId);
        }
    }

    private void getViewValues(){

        //ImageView Segment
        carImage = (ImageView) findViewById(R.id.photoImgView);

        //Brand Segment
        makeView = (TextView) findViewById(R.id.makeTextView);
        modelView = (TextView) findViewById(R.id.modelTextView);
        yearView = (TextView) findViewById(R.id.yearView);

        //Details Segment
        carNameView = (TextView) findViewById(R.id.carNameTextView);
        doorsView = (TextView) findViewById(R.id.doorsView);
        driveView = (TextView) findViewById(R.id.driveTextView);
        vehicleStyleView = (TextView) findViewById(R.id.styleTextView);
        vehicleTypeView = (TextView) findViewById(R.id.typeTextView);

        //Engine Segment
        littersView = (TextView) findViewById(R.id.littersView);
        cylinderView = (TextView) findViewById(R.id.cylinderView);
        horsePowerView = (TextView) findViewById(R.id.horsePowerView);
        valvesView = (TextView) findViewById(R.id.totalValvesTextView);
        engineCodeView = (TextView) findViewById(R.id.engineCodeView);

        //Mechanical Segment
        transmissionTypeView = (TextView) findViewById(R.id.transmissionTypeView);
        numberOfSpeedsView = (TextView) findViewById(R.id.numberOfSpeedView);
        manufacturerEngineCodeView = (TextView) findViewById(R.id.manufacturerEngineCodeTextView);

        //Performance Segment
        mpgHighwayView = (TextView) findViewById(R.id.mpgHighwayView);
        mpgCityView = (TextView) findViewById(R.id.mpgCityView);
        configurationView = (TextView) findViewById(R.id.configurationView);
        fuelTypeView = (TextView) findViewById(R.id.fuelTypeView);

        //Adview
        mAdView = (AdView) findViewById(R.id.ad_view);

    }

    private void setUpAdView(){
        AdRequest adRequest = new AdRequest.Builder().setGender(AdRequest.GENDER_MALE).build();
        mAdView.loadAd(adRequest);
    }

    public class CarImageTask extends AsyncTask<String, Void, String[]>{
        private final String LOG_TAG = CarImageTask.class.getSimpleName();

        @Override
        protected String[] doInBackground(String... params) {
            Log.v(LOG_TAG, "Pararms count: " + params.length);
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            //This variable will contain the raw JSON Response
            String imageJSONString = null;

            try {
                //Contructing the URL for the query and the other constant query parameter
                final String BASE_URL = "http://api.edmunds.com/v1/api/vehiclephoto/service/findphotosbystyleid?";
                final String QUERY_PARM = "q";
                final String STYLEID = "styleId";
                final String API_KEY = "api_key";

                Uri builtUri = Uri.parse(BASE_URL).buildUpon().
                        appendQueryParameter(STYLEID, params[0]).
                        appendQueryParameter(API_KEY, getString(R.string.apiID)).build();

                URL url = new URL(builtUri.toString().replaceAll("%2F","/"));
                Log.v(LOG_TAG,"Built Uri and URL: "+url);
                //Creating the Request and opening the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();//Reading the input into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;

                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    //Stream was empty, no need for parsing
                    return null;
                };
                imageJSONString = buffer.toString();
                Log.v(LOG_TAG, "Image JSON String: " + imageJSONString);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error", e);
                //If no weather data was returned, there is no need for parsing
                return null;

            } finally {
                //Closing resources
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            try {
                return getImageListFromJSON(imageJSONString);
            } catch (Exception e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        private String[] getImageListFromJSON(String imageJSONString) throws JSONException{

            String [] resultString = new String [10];
            //JSON Objects Names to Extract
            final String AUTHOR = "authorNames";
            final String CHILDREN = "children";
            final String CAPTION_TRANSCRIPT = "captionTranscript";
            final String SUBTYPE = "subType";
            final String PHOTO_SRC = "photoSrcs";

            Log.v(LOG_TAG+" imageJsonString: ",imageJSONString);
            String imageJSON = imageJSONString.substring(1, imageJSONString.length()-1);
            JSONObject carImageJSON = new JSONObject(imageJSON.trim());

            Log.v(LOG_TAG+ " imageJSON: ",imageJSON);
            Log.v(LOG_TAG+ " CarImageJSON: ",carImageJSON.toString().replaceAll("\\\\",""));
            JSONArray imageArray = carImageJSON.getJSONArray(PHOTO_SRC);
            Log.v(LOG_TAG +"Image Array Length: ",imageArray.length()+" Image Array: "+imageArray.toString().replaceAll("\\\\",""));

            resultString[0] = imageArray.toString().replaceAll("\\\\","");
            Log.v(LOG_TAG+ " resultString ",resultString[0]);
            return resultString;
        }
        public void downloadImage(String url){
            //"http://media.ed.edmunds-media.com/acura/mdx/2006/oem/2006_acura_mdx_4dr-suv_touring_fbdg_oem_2_150.jpg"

            Picasso.with(DetailsActivity.this)
                    .load(url)
                    .fit()
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(carImage);
        }

        @Override
        protected void onPostExecute(String[] strings) {
            String initialUrl = "http://media.ed.edmunds-media.com/";
            if(strings != null) {
                Log.v(LOG_TAG + " Strings length: ", String.valueOf(strings.length));
                Log.v(LOG_TAG + " Strings value: ", strings[0].substring(3, strings[0].indexOf(",") - 1));

                downloadImage(initialUrl + strings[0].substring(3, strings[0].indexOf(",") - 1));
            }
        }
    }
}
