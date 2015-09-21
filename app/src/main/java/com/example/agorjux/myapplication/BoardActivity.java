package com.example.agorjux.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.agorjux.moveyourbody.R;
import com.example.agorjux.myapplication.ClientRest.ClientRest;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by arthur on 13/06/15.
 */
public class BoardActivity extends Activity {

    static boolean firstView = true;
    static boolean createEvent = false;
    static boolean listEmpty = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        if(BoardActivity.firstView){
            Toast.makeText(getApplicationContext(), "You are now logged", Toast.LENGTH_SHORT).show();
            BoardActivity.firstView = false;
        }

        if(BoardActivity.createEvent){
            Toast.makeText(getApplicationContext(), "Your event is now created", Toast.LENGTH_SHORT).show();
            BoardActivity.createEvent = false;
        }

        if(BoardActivity.listEmpty){
            Toast.makeText(getApplicationContext(), "You have not event", Toast.LENGTH_SHORT).show();
            BoardActivity.listEmpty = false;
        }

    }

    public void myEvents(View v){
        Intent intent = new Intent(BoardActivity.this, OwnListEventsActivity.class);
        startActivity(intent);
    }

    public void searchEvents(View v){
        Intent intent = new Intent(BoardActivity.this, ListEventsActivity.class);
        startActivity(intent);
    }

    public void deconnect(View v){
        ClientRest.deconnexion();
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
        editor.putBoolean("connection", false);
        editor.putInt("idUser", -1);
        editor.commit();
        Intent intent = new Intent(BoardActivity.this, LoginActivity.class);
        BoardActivity.this.startActivity(intent);

    }

    public void createEvent(View v){
        this.getSports();
    }

    private void getSports(){
        ClientRest.client.get(ClientRest.baseUrl("event/getSports"), null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray sports = response.getJSONArray("result");
                    ArrayList<String> result = new ArrayList<String>();
                    for(int i = 0; i < sports.length(); i++){
                        String value = sports.getString(i);
                        result.add(value);
                    }
                    Intent intent = new Intent(BoardActivity.this, CreateEventActivity.class);
                    intent.putExtra("sports", TextUtils.join(",", result));
                    startActivity(intent);

                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "An error happened", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Check your network connection", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, Throwable throwable, JSONArray errorResponse) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Toast.makeText(getApplicationContext(), "Not found", Toast.LENGTH_SHORT).show();
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
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
