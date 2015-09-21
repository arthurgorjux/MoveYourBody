package com.example.agorjux.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.agorjux.myapplication.ClientRest.ClientRest;
import com.loopj.android.http.JsonHttpResponseHandler;

import com.agorjux.moveyourbody.R;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends Activity {

    public static int idUser = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button logIn = (Button) findViewById(R.id.login_button);
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connect(v);
            }
        });
        Button signUp = (Button) findViewById(R.id.signup_button);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup(v);
            }
        });

        Boolean connection = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("connection", false);

        if(connection){
            int idUser = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getInt("idUser", -1);
            LoginActivity.idUser = idUser;
            Intent boardIntent = new Intent(this, BoardActivity.class);
            LoginActivity.this.startActivity(boardIntent);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }

    public void signup(View v){
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    public void connect(View v){
        EditText loginText = (EditText) findViewById(R.id.login_username_input);
        EditText passwordText = (EditText) findViewById(R.id.login_password_input);

        String loginString = loginText.getText().toString();
        String passwordString = passwordText.getText().toString();

        if(this.loginValid(loginString, passwordString)){
            ClientRest.client.get(ClientRest.baseUrl("user/login/login/" + loginString + "/password/" + passwordString), null, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {

                        // si co réussie
                        if (response.getString("status").equals("success") && response.getInt("id") != 0) {

                            Intent accueil_intent = new Intent(LoginActivity.this, BoardActivity.class);
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                            editor.putBoolean("connection", true);
                            editor.putInt("idUser", response.getInt("id"));
                            editor.commit();
                            LoginActivity.idUser = response.getInt("id");
                            BoardActivity.firstView = true;
                            LoginActivity.this.startActivity(accueil_intent);
                            finish();
                        }
                        // si co non réussie
                        else {
                            Toast.makeText(getApplicationContext(), R.string.login_invalid_credentials_toast, Toast.LENGTH_SHORT).show();
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
    }

    private boolean loginValid(String login, String password){
        return (!login.equals("") && !password.equals(""));
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
