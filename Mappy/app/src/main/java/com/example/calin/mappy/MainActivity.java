package com.example.calin.mappy;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;


public class MainActivity extends Activity {

    EditText addressEditText, finalAddressEditText;

    LatLng addressPos, finalAddressPos;

    Marker addressMarker;

    static final LatLng myPos = new LatLng(40, -79);

    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addressEditText = (EditText) findViewById(R.id.addressEditText);
        finalAddressEditText = (EditText) findViewById(R.id.finalAddressEditText);

        try {

            if(googleMap == null) {

                googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

            }

            googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

            googleMap.setMyLocationEnabled(true);

            googleMap.setTrafficEnabled(true);

            googleMap.setIndoorEnabled(true);

            googleMap.setBuildingsEnabled(true);

            googleMap.getUiSettings().setZoomControlsEnabled(true);

            Marker marker = googleMap.addMarker(new MarkerOptions().position(myPos).title("Hello"));


        }catch (Exception e) {
            e.printStackTrace();
        }



    }

    public void showAddressMarker(View view) {

        String newAddress = addressEditText.getText().toString();

        if(newAddress != null) {

            new PlaceAMarker().execute(newAddress);

        }

    }

    public void getDirections(View view) {

        String startingAddress = addressEditText.getText().toString();
        String finalAddress = finalAddressEditText.getText().toString();

        if( (startingAddress.equals("")) || finalAddress.equals("")) {

            Toast.makeText(this, "enter values", Toast.LENGTH_SHORT).show();

        } else {

            new GetDirections().execute(startingAddress, finalAddress);

        }

    }

    class PlaceAMarker extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            String startAddress = params[0];

            startAddress = startAddress.replaceAll(" ", "%20");

            getLatLng(startAddress, false);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            addressMarker = googleMap.addMarker(new MarkerOptions().position(addressPos).title("address"));
        }
    }

    class GetDirections extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            String startAddress = params[0];

            startAddress = startAddress.replaceAll(" ", "%20");

            getLatLng(startAddress, false);

            String endAddress = params[1];

            endAddress = endAddress.replaceAll(" ", "%20");

            getLatLng(endAddress, true);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            String geoUriString = "http://maps.google.com/maps?addr=" +
                    addressPos.latitude + "," +
                    addressPos.longitude + "&daddr=" +
                    finalAddressPos.latitude + "," +
                    finalAddressPos.longitude;

            System.out.println(geoUriString);

            Intent mapCall = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUriString));

            startActivity(mapCall);
        }
    }

    protected void getLatLng(String address, boolean setDestination) {

        String uri = "http://maps.google.com/maps/api/geocode/json?address=" +
                address + "&sensor=false";

        HttpGet httpGet = new HttpGet(uri);

        HttpClient client = new DefaultHttpClient();

        HttpResponse response;

        StringBuilder stringBuilder = new StringBuilder();

        try {
            response = client.execute(httpGet);

            HttpEntity entitiy = response.getEntity();

            InputStream stream = entitiy.getContent();

            int byteData;

            while((byteData = stream.read()) != -1) {

                stringBuilder.append((char) byteData);

            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        double lat = 0.0, lng = 0.0;

        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(stringBuilder.toString());

            lng = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location").getDouble("lng");

            lat = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location").getDouble("lat");


            if(setDestination) {

                finalAddressPos = new LatLng(lat, lng);

            } else {

                addressPos = new LatLng(lat, lng);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
