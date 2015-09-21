package com.example.agorjux.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
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

import com.agorjux.moveyourbody.R;

import com.example.agorjux.myapplication.ClientRest.ClientRest;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

/**
 * Created by arthur on 12/06/15.
 */
public class SignUpActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.com_parse_ui_parse_signup_fragment);

        Button signUp = (Button) findViewById(R.id.create_account);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup(v);
            }
        });
    }

    public void signup(View V){
        // Get formular data
        EditText usernameText = (EditText) findViewById(R.id.signup_username_input);
        EditText passwordText = (EditText) findViewById(R.id.signup_password_input);
        EditText passwordConfirmText = (EditText) findViewById(R.id.signup_confirm_password_input);
        EditText mailText = (EditText) findViewById(R.id.signup_email_input);
        EditText nameText = (EditText) findViewById(R.id.signup_name_input);

        String usernameString = usernameText.getText().toString();
        String passwordString = passwordText.getText().toString();
        String passwordConfirmString = passwordConfirmText.getText().toString();
        String mailString = mailText.getText().toString();
        String nameString = nameText.getText().toString();

        //if(this.signupValid(usernameString, passwordString, passwordConfirmString, mailString, nameString)){

        //}
        String signupValid = this.signupValid(usernameString, passwordString, passwordConfirmString, mailString, nameString);
        if(signupValid.equals("notmatch")){
            // password don't match
            Toast.makeText(getApplicationContext(), "Passwords don't match", Toast.LENGTH_SHORT).show();
        }else if(signupValid.equals("empty")){
            // few inputs are empty
            Toast.makeText(getApplicationContext(), "Input(s) empty", Toast.LENGTH_SHORT).show();
        }else if(signupValid.equals("notvalid")){
            // mail not valid
            Toast.makeText(getApplicationContext(), "Mail is not valid", Toast.LENGTH_SHORT).show();
        }else{
            // form is ok

            RequestParams params = new RequestParams();
            params.put("login", usernameString);
            params.put("password", passwordString);
            params.put("mail", mailString);
            params.put("name", nameString);

            final ProgressDialog progressBar = new ProgressDialog(this);

            ClientRest.client.post(ClientRest.baseUrl("user/signup/"), params, new JsonHttpResponseHandler() {

                @Override
                public void onStart() {
                    super.onStart();
                    progressBar.setMessage("Creating account ...");
                    progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progressBar.setIndeterminate(true);
                    progressBar.setProgress(50);
                    progressBar.show();
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    progressBar.setProgress(100);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    progressBar.hide();
                    progressBar.dismiss();
                    if(LoginActivity.idUser != 0){
                        Intent intent = new Intent(SignUpActivity.this, BoardActivity.class);
                        BoardActivity.firstView = true;
                        startActivity(intent);
                    }else{
                        Toast.makeText(getApplicationContext(), "Form sending failed", Toast.LENGTH_SHORT).show();
                    }
                    finish();
                }


                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        // si co réussie
                        if (response.getString("status").equals("success")) {
                            Toast.makeText(getApplicationContext(), "Signup ok", Toast.LENGTH_SHORT).show();
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                            editor.putBoolean("connection", true);
                            editor.putInt("idUser", response.getInt("id"));
                            editor.commit();
                            LoginActivity.idUser = response.getInt("id");

                        }
                        // si co non réussie
                        else if(response.getString("status").equals("username already exists")) {
                            Toast.makeText(getApplicationContext(), "Username already exists", Toast.LENGTH_SHORT).show();
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

        }

    }

    private String signupValid(String login, String password, String passwordConfirm, String mail, String name){
        final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}$", Pattern.CASE_INSENSITIVE);
        if(!login.equals("") && !password.equals("") && !passwordConfirm.equals("") && !mail.equals("") && !name.equals("")){
            if (EMAIL_PATTERN.matcher(mail).matches()) {
                if (password.equals(passwordConfirm)) {
                    return "ok";
                }
                return "notmatch";
            }else{
                return "notvalid";
            }
        }
        return "empty";
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
