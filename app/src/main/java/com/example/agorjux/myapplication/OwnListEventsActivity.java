package com.example.agorjux.myapplication;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.agorjux.myapplication.ClientRest.ClientRest;
import com.loopj.android.http.JsonHttpResponseHandler;

import com.agorjux.moveyourbody.R;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by arthur on 15/08/15.
 */
public class OwnListEventsActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_events);
        final ListView listeEvents = (ListView) findViewById(R.id.liste_events_perso);
        final List<EventsModel> events = new ArrayList<>();
        ClientRest.client.get(ClientRest.baseUrl("event/getEventsForUser/idUser/" + LoginActivity.idUser + ""), null, new JsonHttpResponseHandler() {
            boolean getEvents = true;
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getString("status").equals("success")) {
                        JSONArray result = response.getJSONArray("result");
                        for (int i = 0; i < result.length(); i++) {
                            JSONObject event = result.getJSONObject(i);
                            int id = event.getInt("id");
                            String title = event.getString("title");
                            String description = event.getString("description");
                            String dateEvent = event.getString("date");
                            long latitude = event.getLong("latitude");
                            long longitude = event.getLong("longitude");
                            String sport = event.getString("sport");
                            int people = event.getInt("numberPeople");
                            int idUser = event.getInt("idUser");
                            EventsModel eventModel = new EventsModel(id, title, description, dateEvent, latitude, longitude, sport, people, idUser);
                            events.add(eventModel);
                        }
                        EventsJoinAdapter adapter = new EventsJoinAdapter(getApplicationContext(), events);

                        listeEvents.setAdapter(adapter);
                        //listeEvents.setBackgroundColor(getResources().getColor(R.color.background_app));
                    }
                    else if(response.getString("status").equals("fail")){
                        //redirection + message
                        Intent intent = new Intent(OwnListEventsActivity.this, BoardActivity.class);
                        Toast.makeText(getApplicationContext(), "There is no event", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                        finish();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "An error happened", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Check your network connection", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, Throwable throwable, JSONArray errorResponse) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Intent intent = new Intent(OwnListEventsActivity.this, BoardActivity.class);
                BoardActivity.listEmpty = true;
                startActivity(intent);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });

    }

    /*@Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String item = (String) getListAdapter().getItem(position);
        Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show();
    }*/

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
