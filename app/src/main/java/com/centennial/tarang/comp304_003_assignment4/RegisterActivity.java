package com.centennial.tarang.comp304_003_assignment4;

//Author: Vrunda Shah(300900997), Tarang Godhari (300931365)
//COMP304_003Assignment4_RegisterActivity

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {
    EditText userName, firstName, lastName, emailAddress, password, address, city, postalCode;
    Button btnRegister;
    private Intent intent;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new DatabaseHelper(this);
        final String fields[] = {"audienceId", "email", "userName", "password", "firstName", "lastName", "address", "city", "postalCode"};
        final String record[] = new String[9];
        // Handle Save button
        btnRegister = (Button) findViewById(R.id.button_sign_up);
        //Get all the Edit texts form items from View
        userName = (EditText) findViewById(R.id.editTextUsername);
        firstName = (EditText) findViewById(R.id.editTextFirstName);
        lastName = (EditText) findViewById(R.id.editTextLastName);
        emailAddress = (EditText) findViewById(R.id.editTextEmailAddress);
        password = (EditText) findViewById(R.id.editTextPassword);
        address = (EditText) findViewById(R.id.editTextAddress);
        city = (EditText) findViewById(R.id.editTextCity);
        postalCode = (EditText) findViewById(R.id.editTextPostalCode);
        //Pass entered values to database
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (checkEnteredData()) {
                    record[1] = emailAddress.getText().toString();
                    record[2] = userName.getText().toString();
                    record[3] = password.getText().toString();
                    record[4] = firstName.getText().toString();
                    record[5] = lastName.getText().toString();
                    record[6] = address.getText().toString();
                    record[7] = city.getText().toString();
                    record[8] = postalCode.getText().toString();
                    //populate the row with some values
                    db.addRecord("tbl_audience", fields, record);

                    //If the user is registered then redirect user to Welcome Activity
                    if (db.checkUser(userName.getText().toString())) {
                        SharedPreferences mySharedPreference = getSharedPreferences("MySharedPreferences", 0);
                        SharedPreferences.Editor prefEditor = mySharedPreference.edit();
                        prefEditor.putString("userName", userName.getText().toString());
                        prefEditor.commit();

                        intent = new Intent(RegisterActivity.this, WelcomeActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Something Went Wrong! Restart App", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please fill up required details", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //Validation for each and every editTexts
    public boolean checkEnteredData() {
        boolean isValid = true;
        //First Name isEmpty Validation
        if (TextUtils.isEmpty(firstName.getText().toString())) {
            firstName.setError(getString(R.string.error_message_first_name));
            isValid = false;
        }
        //Last Name isEmpty Validation
        if (TextUtils.isEmpty(lastName.getText().toString())) {
            lastName.setError(getString(R.string.error_message_last_name));
            isValid = false;
        }
        //Username isEmpty Validation
        if (TextUtils.isEmpty(userName.getText().toString())) {
            userName.setError(getString(R.string.error_message_username));
            isValid = false;
        }
        //Email Validation by REGEX
        if (!emailAddress.getText().toString().trim().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
            emailAddress.setError(getString(R.string.error_message_email));
            isValid = false;
        }
        //Check password Length
        if (password.getText().toString().trim().length() < 8) {
            password.setError(getString(R.string.error_message_password));
            isValid = false;
        }
        //Check Address Length
        if (address.getText().toString().trim().length() <= 0) {
            address.setError(getString(R.string.error_message_address));
            isValid = false;
        }
        //Check City Length
        if (city.getText().toString().trim().length() <= 0) {
            city.setError(getString(R.string.error_message_city));
            isValid = false;
        }
        //Check PostalCode length
        if (postalCode.getText().toString().trim().length() <= 0) {
            postalCode.setError(getString(R.string.error_message_postal_code));
            isValid = false;
        }
        //if all values are valid return true else false
        return isValid;
    }

}
