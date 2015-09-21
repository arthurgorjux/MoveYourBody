package com.example.agorjux.myapplication;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.agorjux.moveyourbody.R;

import com.example.agorjux.myapplication.ClientRest.ClientRest;
import com.example.agorjux.myapplication.Fragments.DatePickerFragment;
import com.example.agorjux.myapplication.Fragments.TimePickerFragment;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by arthur on 13/06/15.
 */
public class CreateEventActivity extends Activity {

    static String sports;
    int nbPeopleValue = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        Intent myIntent = getIntent();
        String sports = myIntent.getStringExtra("sports");
        List<String> array = Arrays.asList(sports.split(","));
        Spinner sportsList = (Spinner) findViewById(R.id.list_sports_event);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
        android.R.layout.simple_spinner_item, array);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sportsList.setAdapter(dataAdapter);
        final TextView nbPeopleText = (TextView) findViewById(R.id.nb_people_event_label);
        final String nbPeopelLabel = nbPeopleText.getText().toString();
        SeekBar nbPeople = (SeekBar) findViewById(R.id.nb_people_event);
        nbPeopleText.setText(nbPeopelLabel + " : " + (nbPeople.getProgress() + 1));
        nbPeople.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressValue = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressValue = progress;
                nbPeopleText.setText(nbPeopelLabel + " : " + (progress + 1));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                nbPeopleValue = 1;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                nbPeopleValue = nbPeopleValue + progressValue;
            }
        });
    }



    public void showDatePicker(View v){
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), new String("datePicker"));
    }

    public void showTimePicker(View v){
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), new String("timePicker"));
    }

    public void submitCreateEvent(View v){
        List<Address> addresses;
        try {
            EditText eventTitleInput = (EditText)findViewById(R.id.title_event);
            EditText eventDescriptionInput = (EditText)findViewById(R.id.description_event);
            EditText eventDateInput = (EditText)findViewById(R.id.date_event);
            EditText eventHourInput = (EditText)findViewById(R.id.hour_event);
            EditText eventAddressStreetInput = (EditText)findViewById(R.id.address_street_event);
            EditText eventAddressCityInput = (EditText)findViewById(R.id.address_city_event);
            if(this.formValid(eventTitleInput, eventDescriptionInput, eventDateInput, eventHourInput, eventAddressStreetInput, eventAddressCityInput)){
                Geocoder geo = new Geocoder(this);
                String addressString = eventAddressStreetInput.getText().toString() + ", " + eventAddressCityInput.getText().toString();
                addresses  = geo.getFromLocationName(addressString, 1);
                Address eventAddress = addresses.get(0);

                Spinner spinner = (Spinner) findViewById(R.id.list_sports_event);
                String selectedSport = spinner.getSelectedItem().toString();

                SeekBar nbPeopleInput = (SeekBar) findViewById(R.id.nb_people_event);
                int nbPeople = nbPeopleInput.getProgress();

                String[] dateEventSplit = eventDateInput.getText().toString().split("/");
                String dateEvent = dateEventSplit[2] + "-" + dateEventSplit[0] + "-" + dateEventSplit[1];
                String[] hourEventSplit = eventHourInput.getText().toString().split(":");
                dateEvent += " " + eventHourInput.getText().toString() + ":00";

                RequestParams params = new RequestParams();
                params.put("title", eventTitleInput.getText().toString());
                params.put("description", eventDescriptionInput.getText().toString());
                params.put("date", dateEvent);
                params.put("latitude", eventAddress.getLatitude());
                params.put("longitude", eventAddress.getLongitude());
                params.put("sport", selectedSport);
                params.put("numberPeople", nbPeople+1);
                params.put("idUser", LoginActivity.idUser);
                final ProgressDialog progressBar = new ProgressDialog(this);
                ClientRest.client.post(ClientRest.baseUrl("event/create"), params, new JsonHttpResponseHandler() {
                    boolean created = false;
                    @Override
                    public void onStart() {
                        super.onStart();
                        progressBar.setMessage("Creating event ...");
                        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        progressBar.setIndeterminate(true);
                        progressBar.setProgress(50);
                        progressBar.show();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        int jumpTime = 50;

                        while(jumpTime <= 100) {
                            try {
                                Thread.sleep(100);
                                jumpTime += 50;
                                progressBar.setProgress(jumpTime);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        progressBar.hide();
                        progressBar.dismiss();
                        Intent intent = new Intent(CreateEventActivity.this, BoardActivity.class);
                        BoardActivity.createEvent = true;
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            // si co réussie
                            if (response.getString("status").equals("success")) {
                                created = true;
                                //finish();
                            }
                            // si co non réussie
                            else {
                                Toast.makeText(getApplicationContext(), "Error while creating event", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), "An error happened", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Check your network connection", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, org.apache.http.Header[] headers, Throwable throwable, org.json.JSONArray errorResponse) {
                        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                        Toast.makeText(getApplicationContext(), "Form sending failed", Toast.LENGTH_SHORT).show();
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                    }
                });

            }else{
                Toast.makeText(getApplicationContext(), "Form empty !", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean formValid(EditText title, EditText description, EditText date, EditText hour, EditText street, EditText city){
        if(!title.getText().toString().equals("") && !description.getText().toString().equals("")
           && !date.getText().toString().equals("") && !hour.getText().toString().equals("")
           && !street.getText().toString().equals("") && !city.getText().toString().equals("")){
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
