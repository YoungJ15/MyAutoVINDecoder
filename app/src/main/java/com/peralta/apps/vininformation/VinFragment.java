package com.peralta.apps.vininformation;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.ads.AdRequest;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.InterstitialAd;
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

import utils.CheckNetwork;

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.about:
                item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Dialog dialog = new Dialog(getActivity());
                        dialog.setContentView(R.layout.about_dialog);
                        dialog.setTitle(R.string.app_name);
                        dialog.show();
                        Log.d("About option clicked", String.valueOf(item.getItemId()));
                        return true;
                    }
                });
                break;
            case R.id.exit:

               getActivity().finish();
                Log.d("Exit option clicked", String.valueOf(item.getItemId()));
        }
        return super.onOptionsItemSelected(item);
    }

    private void requestNewInterstitial(){
        PublisherAdRequest adRequest = new PublisherAdRequest.Builder().setGender(PublisherAdRequest.GENDER_MALE).addTestDevice("E3E4253CB2F3CB3CC2E697C997236F0E").build();
        interstitialAd.loadAd(adRequest);
    }



    public class FetchVinTask extends AsyncTask<String, Void, String[]>{

        private final String LOG_TAG = FetchVinTask.class.getSimpleName();

       /** @Override
        protected void onPostExecute(String[] strings) {
            Log.v(LOG_TAG + " Valor del PostExecute", strings.toString());
            if(strings != null){

            }
        }
        **/

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
                Log.v(LOG_TAG, "Car JSON String: " + VINJSONString);
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

            final String EDP_MPG = "MPG";
            final String EDP_YEARS = "years";

            Intent intent = new Intent(getActivity().getBaseContext(), DetailsActivity.class);

            JSONObject VINJSON = new JSONObject(VINJSONString);
            JSONObject JSONMake = null;
            JSONObject JSONModel = null;
            JSONObject JSONTransmission = null;
            JSONObject JSONENGINE = null;
            JSONObject JSONMPG = null;
            JSONArray JSONYEARArray = null;
            JSONObject JSONYEAR = null;

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
            }
            Log.v(LOG_TAG+" Year Array:",JSONYEARArray.toString());
            Log.v(LOG_TAG+" Year Object",JSONYEAR.toString());

            Log.v(LOG_TAG + " VIN JSON", VINJSONString.toString());
            String [] resultString = new String [10];

            //Strings to put into the Intent
            String noValue = "Data not found";
            String make = null;
            String model = null;
            String drive = null;
            String transmissionType = null;
            String numberOfSpeed = null;
            String squishVin = null;
            String doors = null;
            String cylinders = null;
            String horsePower = null;
            String litters = null;
            String mpgHighway = null;
            String mpgCity = null;
            String year = null;

            if(JSONMake!= null && JSONMake.has("name")){
                make = JSONMake.getString("name");
            }
            else{
                make = noValue;
            }
            if(JSONModel != null && JSONModel.has("name")) {
                model = JSONModel.getString("name");
            }
            else{
                model = noValue;
            }
            if(VINJSON.has("drivenWheels")) {
                drive = VINJSON.getString("drivenWheels");
            }
            else{
                drive = noValue;
            }
            if(JSONTransmission != null && JSONTransmission.has("transmissionType")) {
                transmissionType = JSONTransmission.getString("transmissionType");
           }
            else{
                transmissionType = noValue;
            }
            if(JSONTransmission != null && JSONTransmission.has("numberOfSpeeds") ) {
                numberOfSpeed = JSONTransmission.getString("numberOfSpeeds");
            }
            else{
                numberOfSpeed = noValue;
            }
            if(VINJSON.has("squishVin") ) {
                squishVin = VINJSON.getString("squishVin");
            }
            else{
                squishVin = noValue;
            }
            if(VINJSON.has("numOfDoors")  ) {
                doors = VINJSON.getString("numOfDoors");
            }
            else{
                doors = noValue;
            }
            if(JSONENGINE != null && JSONENGINE.has("cylinder") ) {
                cylinders = JSONENGINE.getString("cylinder");
            }
            else{
                cylinders = noValue;
            }
            if(JSONENGINE != null && JSONENGINE.has("horsepower")) {
                horsePower = JSONENGINE.getString("horsepower");
            }
            else{
                horsePower = noValue;
            }
            if(JSONENGINE != null && JSONENGINE.has("size")) {
                litters = JSONENGINE.getString("size");
            }
            else{
                litters = noValue;
            }
            if(JSONMPG != null && JSONMPG.has("highway")) {
                mpgHighway = JSONMPG.getString("highway");
            }
            else{
                mpgHighway = noValue;
            }
            if(JSONMPG != null && JSONMPG.has("city")) {
                mpgCity = JSONMPG.getString("city");
            }
            else{
                mpgCity = noValue;
            }
            if(JSONYEAR != null && JSONYEAR.has("year")) {
                year = JSONYEAR.getString("year");
            }
            else{
                year = noValue;
            }

            Log.v(LOG_TAG+" Year: ",year);

            resultString[1] = make + model + drive + transmissionType + numberOfSpeed + doors + mpgHighway + mpgCity + squishVin + year;

            intent.putExtra("make", make);
            intent.putExtra("model", model);
            intent.putExtra("drive", drive);
            intent.putExtra("transmissionType", transmissionType);
            intent.putExtra("numberOfSpeeds", numberOfSpeed);
            intent.putExtra("doors", doors);
            intent.putExtra("mpgHighway", mpgHighway);
            intent.putExtra("mpgCity", mpgCity);
            intent.putExtra("cylinder", cylinders);
            intent.putExtra("horsepower", horsePower);
            intent.putExtra("litters", litters);
            intent.putExtra("year",year);
            intent.putExtra("squishVin", squishVin);

            getActivity().startActivity(intent);

            return resultString;
        }

      }
    }
