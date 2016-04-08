package com.peralta.apps.vininformation;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;

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

/**
 * Created by Josermando on 3/5/2016.
 */
public class VinFragment extends Fragment {

    private EditText vinEditText;
    private Button vinButton;

    private PublisherInterstitialAd interstitialAd;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.vin_fragment, container, false);

        interstitialAd = new PublisherInterstitialAd(getActivity());
        interstitialAd.setAdUnitId(getString(R.string.interstitial_unit_id));
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
            }
        });
        requestNewInterstitial();

        vinEditText = (EditText) layout.findViewById(R.id.vinEditText);
        vinButton = (Button) layout.findViewById(R.id.vinButton);
        vinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String vinText;
                vinText = vinEditText.getText().toString().trim();
                if(vinText.length() < 17){
                    Toast.makeText(getActivity(),"VIN must be 17 numbers", Toast.LENGTH_LONG).show();
                    vinEditText.requestFocus();
                }
                else{
                    if (interstitialAd.isLoaded()) {
                        interstitialAd.show();
                    }
                    new FetchVinTask().execute(vinText);
                }

            }
        });

        return layout;
    }

    private void requestNewInterstitial(){
        PublisherAdRequest adRequest = new PublisherAdRequest.Builder().setGender(PublisherAdRequest.GENDER_MALE).addTestDevice("E3E4253CB2F3CB3CC2E697C997236F0E").build();
        interstitialAd.loadAd(adRequest);
    }

    public class FetchVinTask extends AsyncTask<String, Void, String[]>{

        private final String LOG_TAG = FetchVinTask.class.getSimpleName();

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String[] doInBackground(String... params) {
            Log.v(LOG_TAG, "Pararms count: " + params.length);
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            //This variable will contain the raw JSON Response
            String VINJSONString = null;

            try {
                //Contructing the URL for the query and the other constant query parameter
                final String BASE_URL = "https://api.edmunds.com/api/vehicle/v2/vins/"+params[0];
                final String API_KEY = "api_key";

                Uri builtUri = Uri.parse(BASE_URL).buildUpon().
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
                    return null;
                }
                VINJSONString = buffer.toString();
                Log.v(LOG_TAG, "CarDetails JSON String: " + VINJSONString);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error", e);
                return null;

            }finally {
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
                return getVINInfoFromJSON(VINJSONString);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        private String[] getVINInfoFromJSON(String VINJSONString) throws  JSONException{

            CarDetails carDetailsObject = new CarDetails();
            MechanicalDetails mechanicalDetailsObject = new MechanicalDetails();
            PerformanceDetails performanceDetailsObject = new PerformanceDetails();

            final String EDP_MPG = "MPG";
            final String EDP_YEARS = "years";
            final String noValue = "Data not found";

            Intent intent = new Intent(getActivity().getBaseContext(), DetailsActivity.class);

            JSONObject VINJSON = new JSONObject(VINJSONString);
            JSONObject JSONMake = null;
            JSONObject JSONModel = null;
            JSONObject JSONTransmission = null;
            JSONObject JSONENGINE = null;
            JSONObject JSONMPG = null;
            JSONArray JSONYEARArray = null;
            JSONObject JSONYEAR = null;
            JSONArray JSONSTYLESArray = null;
            JSONObject JSONSTYLES = null;
            JSONObject JSONCATEGORIES = null;

            if(VINJSON.has("make")){
                JSONMake = VINJSON.getJSONObject("make");
            }
            if(VINJSON.has("model")){
                JSONModel = VINJSON.getJSONObject("model");
            }
            if(VINJSON.has("transmission")){
                JSONTransmission = VINJSON.getJSONObject("transmission");
            }
            if(VINJSON.has("engine")){
                JSONENGINE = VINJSON.getJSONObject("engine");
            }
            if(VINJSON.has("MPG")){
                JSONMPG = VINJSON.getJSONObject(EDP_MPG);
            }
            if(VINJSON.has("years")){
                JSONYEARArray = VINJSON.getJSONArray(EDP_YEARS);
                JSONYEAR = JSONYEARArray.getJSONObject(0);
                JSONObject JSONSTYLEID = null;
                for(int i = 0; i< JSONYEARArray.length(); i++){
                    if(JSONYEAR.has("styles")){
                        JSONSTYLES = JSONYEARArray.getJSONObject(i);
                        JSONSTYLESArray = JSONSTYLES.getJSONArray("styles");
                        JSONSTYLEID = JSONSTYLESArray.getJSONObject(0);
                    }
                    if(JSONSTYLEID != null && JSONSTYLEID.has("id")){
                        carDetailsObject.setStyleId(JSONSTYLEID.getString("id"));
                    }
                    else{
                        carDetailsObject.setStyleId(noValue);
                    }
                    if(JSONSTYLEID != null && JSONSTYLEID.has("name")){
                        carDetailsObject.setCarName(JSONSTYLEID.getString("name"));
                    }
                    else{
                        carDetailsObject.setCarName(noValue);
                    }
                }
            }
            if(VINJSON.has("categories")){
                JSONCATEGORIES = VINJSON.getJSONObject("categories");
            }

            Log.v(LOG_TAG+" Year Array:",JSONYEARArray.toString());
            Log.v(LOG_TAG+" Year Object",JSONYEAR.toString());

            Log.v(LOG_TAG + " VIN JSON", VINJSONString.toString());
            String [] resultString = new String [10];

            //Strings to put into the Intent

            if(JSONMake!= null && JSONMake.has("name")){
                carDetailsObject.setMake(JSONMake.getString("name"));
            }
            else{
                carDetailsObject.setMake(noValue);
            }
            if(JSONModel != null && JSONModel.has("name")) {
                carDetailsObject.setModel(JSONModel.getString("name"));
            }
            else{
                carDetailsObject.setModel(noValue);
            }
            if(VINJSON.has("drivenWheels")) {
                carDetailsObject.setDrive(VINJSON.getString("drivenWheels"));
            }
            else{
                carDetailsObject.setDrive(noValue);
            }
            if(JSONTransmission != null && JSONTransmission.has("transmissionType")) {
                mechanicalDetailsObject.setTransmissionType(JSONTransmission.getString("transmissionType"));
           }
            else{
                mechanicalDetailsObject.setTransmissionType(noValue);
            }
            if(JSONTransmission != null && JSONTransmission.has("numberOfSpeeds") ) {
                mechanicalDetailsObject.setNumberOfSpeed(JSONTransmission.getString("numberOfSpeeds"));
            }
            else{
                mechanicalDetailsObject.setNumberOfSpeed(noValue);
            }
            if(VINJSON.has("squishVin") ) {
                carDetailsObject.setSquishVin(VINJSON.getString("squishVin"));
            }
            else{
                carDetailsObject.setSquishVin(noValue);
            }
            if(VINJSON.has("numOfDoors")  ) {
                carDetailsObject.setDoors(VINJSON.getString("numOfDoors"));
            }
            else{
                carDetailsObject.setDoors(noValue);
            }
            if(JSONENGINE != null && JSONENGINE.has("cylinder") ) {
                mechanicalDetailsObject.setCylinders(JSONENGINE.getString("cylinder"));
            }
            else{
                mechanicalDetailsObject.setCylinders(noValue);
            }
            if(JSONENGINE != null && JSONENGINE.has("horsepower")) {
                mechanicalDetailsObject.setHorsePower(JSONENGINE.getString("horsepower"));
            }
            else{
                mechanicalDetailsObject.setHorsePower(noValue);
            }
            if(JSONENGINE != null && JSONENGINE.has("size")) {
                mechanicalDetailsObject.setLitters(JSONENGINE.getString("size"));
            }
            else{
                mechanicalDetailsObject.setLitters(noValue);
            }
            if(JSONENGINE != null && JSONENGINE.has("manufacturerEngineCode")){
                mechanicalDetailsObject.setManufacturerEngineCode(JSONENGINE.getString("manufacturerEngineCode"));
            }
            else{
                mechanicalDetailsObject.setManufacturerEngineCode(noValue);
            }
            if(JSONENGINE != null && JSONENGINE.has("totalValves")){
                mechanicalDetailsObject.setTotalValves(JSONENGINE.getString("totalValves"));
            }
            else{
                mechanicalDetailsObject.setTotalValves(noValue);
            }
            if(JSONENGINE != null && JSONENGINE.has("configuration")){
                performanceDetailsObject.setConfiguration(JSONENGINE.getString("configuration"));
            }
            else{
                performanceDetailsObject.setConfiguration(noValue);
            }

            if(JSONENGINE != null && JSONENGINE.has("fuelType")){
                performanceDetailsObject.setFuelType(JSONENGINE.getString("fuelType"));
            }
            else{
                performanceDetailsObject.setFuelType(noValue);
            }

            if(JSONENGINE != null && JSONENGINE.has("code")){
                mechanicalDetailsObject.setEngineCode(JSONENGINE.getString("code"));
            }
            else{
                mechanicalDetailsObject.setEngineCode(noValue);
            }


            if(JSONMPG != null && JSONMPG.has("highway")) {
                performanceDetailsObject.setMpgHighway(JSONMPG.getString("highway"));
            }
            else{
                performanceDetailsObject.setMpgHighway(noValue);
            }
            if(JSONMPG != null && JSONMPG.has("city")) {
                performanceDetailsObject.setMpgCity(JSONMPG.getString("city"));
            }
            else{
                performanceDetailsObject.setMpgCity(noValue);
            }
            if(JSONYEAR != null && JSONYEAR.has("year")) {
                carDetailsObject.setYear(JSONYEAR.getString("year"));
            }
            else{
                carDetailsObject.setYear(noValue);
            }
            if(JSONCATEGORIES != null && JSONCATEGORIES.has("vehicleStyle")){
                carDetailsObject.setVehicleStyle(JSONCATEGORIES.getString("vehicleStyle"));
            }
            else{
                carDetailsObject.setVehicleStyle(noValue);
            }
            if(JSONCATEGORIES != null && JSONCATEGORIES.has("vehicleType")){
                carDetailsObject.setVehicleType(JSONCATEGORIES.getString("vehicleType"));
            }
            else{
                carDetailsObject.setVehicleType(noValue);
            }


            Log.v(LOG_TAG+" Year: ", carDetailsObject.getYear());

            resultString[1] =   carDetailsObject.getMake() +
                                carDetailsObject.getModel() +
                                carDetailsObject.getDrive() +
                                mechanicalDetailsObject.getTransmissionType() +
                                mechanicalDetailsObject.getNumberOfSpeed() +
                                carDetailsObject.getDoors() +
                                performanceDetailsObject.getMpgHighway() +
                                performanceDetailsObject.getMpgCity() +
                                carDetailsObject.getSquishVin() +
                                carDetailsObject.getYear() +
                                carDetailsObject.getStyleId();

            intent.putExtra("carDetailsObject", carDetailsObject);
            intent.putExtra("mechanicalDetailsObject", mechanicalDetailsObject);
            intent.putExtra("performanceDetailsObject", performanceDetailsObject);
            getActivity().startActivity(intent);

            return resultString;
        }

      }
    }
