/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;


public class MainActivity extends AppCompatActivity implements View.OnClickListener , View.OnKeyListener {

    TextView loginText;
    Boolean signupMode = true;
    EditText passwordEdit , usernameEdit;
    Button signupButton;
    ImageView logoImage;
    RelativeLayout layoutBack;

    public void showUsers(){
        Intent newIntent = new Intent(MainActivity.this , UserList.class);
        startActivity(newIntent);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN){
            logInUser(v);
        }
        return false;
    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.loginText){
            if(signupMode){
                signupMode = false;
                signupButton.setText("Login");
                loginText.setText("Or Signup");
            }
            else{
                signupMode = true;
                signupButton.setText("Signup");
                loginText.setText("Or Login");
            }
        }
        else if(v.getId() == R.id.logo || v.getId() == R.id.layout){
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken() , 0);
        }
    }

 public void logInUser(View view){



     if(passwordEdit.getText().toString().matches("") || usernameEdit.getText().toString().matches("")){
         Toast.makeText(MainActivity.this , "Username and Password Field Empty" , Toast.LENGTH_SHORT).show();
     }
   else{
         if(signupMode) {
             ParseUser user = new ParseUser();
             user.setUsername(usernameEdit.getText().toString());
             user.setPassword(passwordEdit.getText().toString());

             user.signUpInBackground(new SignUpCallback() {
                 @Override
                 public void done(ParseException e) {
                     if (e == null) {
                         Log.i("Login Success", "Ho gaya Login");
                         showUsers();
                     } else {
                         Log.i("Login Failed", "Nhi hua login");
                     }
                 }
             });
         }
         else{
             ParseUser.logInInBackground(usernameEdit.getText().toString(), passwordEdit.getText().toString(), new LogInCallback() {
                 @Override
                 public void done(ParseUser user, ParseException e) {
                     if(user != null){
                         Log.i("Success" , "Done");
                         showUsers();
                     }
                     else{
                         Toast.makeText(MainActivity.this , e.getMessage() , Toast.LENGTH_SHORT).show();
                     }
                 }
             });
         }
   }
 }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

      passwordEdit = (EditText) findViewById(R.id.passwordText);
      usernameEdit = (EditText) findViewById(R.id.usernameText);
      signupButton = (Button) findViewById(R.id.signup);
    loginText = (TextView) findViewById(R.id.loginText);
    loginText.setOnClickListener(this);
    passwordEdit.setOnKeyListener(this);

    layoutBack = (RelativeLayout) findViewById(R.id.layout);
    logoImage = (ImageView) findViewById(R.id.logo);

    layoutBack.setOnClickListener(this);
    logoImage.setOnClickListener(this);

    setTitle("Instagram");

    if(ParseUser.getCurrentUser() != null){
        showUsers();
    }

      ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }



}