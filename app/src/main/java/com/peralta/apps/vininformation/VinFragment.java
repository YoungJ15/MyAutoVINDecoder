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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Josermando on 3/5/2016.
 */
public class VinFragment extends Fragment {

    private EditText vinEditText;
    private Button vinButton;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        View layout = inflater.inflate(R.layout.vin_fragment, container, false);
        vinEditText = (EditText) layout.findViewById(R.id.vinEditText);
        vinButton = (Button) layout.findViewById(R.id.vinButton);
        vinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String vinText;
                vinText = vinEditText.getText().toString().trim();
                new FetchVinTask().execute(vinText);
            }
        });

        return layout;
    }



    public class FetchVinTask extends AsyncTask<String, Void, String[]>{

        private final String LOG_TAG = FetchVinTask.class.getSimpleName();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String[] strings) {
            Log.v(LOG_TAG + " Valor del PostExecute", strings.toString());
            if(strings != null){
     //           Intent intent = new Intent(getActivity().getBaseContext(), DetailsActivity.class);
     //           intent.putExtra("make", strings.toString());
            }
        }

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
            //JSON Objects Names to Extract
            final String EDP_MAKE = "make";
            final String EDP_MODEL = "model";
            final String EDP_DRIVE = "drivenWheels";
            final String EDP_SQUISH_VIN = "squishVin";
            final String EDP_TRANSMISSION = "transmission";
            final String EDP_ENGINE = "engine";
            final String EDP_MPG = "MPG";
            final String EDP_YEARS = "years";

            JSONObject VINJSON = new JSONObject(VINJSONString);
            JSONObject JSONMake = VINJSON.getJSONObject(EDP_MAKE);
            JSONObject JSONModel = VINJSON.getJSONObject(EDP_MODEL);
            JSONObject JSONTransmission = VINJSON.getJSONObject(EDP_TRANSMISSION);
            JSONObject JSONENGINE = VINJSON.getJSONObject(EDP_ENGINE);
            JSONObject JSONMPG = VINJSON.getJSONObject(EDP_MPG);
            JSONArray JSONYEAR = VINJSON.getJSONArray(EDP_YEARS);
            Log.v(LOG_TAG+" Year Array:",JSONYEAR.toString());

            Log.v(LOG_TAG + " VIN JSON", VINJSONString.toString());
            String [] resultString = new String [10];

            String make = JSONMake.getString("name");
            String model = JSONModel.getString("name");
            String drive = VINJSON.getString(EDP_DRIVE);
            String transmissionType = JSONTransmission.getString("transmissionType");
            String numberOfSpeed = JSONTransmission.getString("numberOfSpeeds");
            String squishVin = VINJSON.getString(EDP_SQUISH_VIN);
            String doors = VINJSON.getString("numOfDoors");
            String cylinders = JSONENGINE.getString("cylinder");
            String horsePower = JSONENGINE.getString("horsepower");
            String litters = JSONENGINE.getString("size");
            String mpgHighway = JSONMPG.getString("highway");
            String mpgCity = JSONMPG.getString("city");
            //String year = JSONYEAR.getJSONObject(1);
            //Log.v(LOG_TAG+" Year: ",year);

            resultString[1] = make + model + drive + transmissionType + numberOfSpeed + doors + mpgHighway + mpgCity + squishVin;

            Intent intent = new Intent(getActivity().getBaseContext(), DetailsActivity.class);
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
            //intent.putExtra("year",year);
            intent.putExtra("squishVin", squishVin);

            getActivity().startActivity(intent);

            return resultString;
        }

      }
    }
