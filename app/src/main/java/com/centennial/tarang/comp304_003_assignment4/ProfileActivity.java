package com.centennial.tarang.comp304_003_assignment4;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity {
    EditText editTextUserName,editTextEmail,editTextFirstName, editTextLastName, editTextAddress, editTextCity, editTextPostalCode;

    String audienceId,email, userName, password, firstName, lastName, address, city, postalCode;
    DatabaseHelper db;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        db = new DatabaseHelper(this);
        SharedPreferences mySharedPreferences = getSharedPreferences("MySharedPreferences", MODE_PRIVATE);
        String username = mySharedPreferences.getString("userName", "");
        Cursor cursor = db.getUserByUserName(username);
        if (cursor.moveToFirst()) {
            audienceId = cursor.getString(0);
            email = cursor.getString(1);
            userName = cursor.getString(2);
            password = cursor.getString(3);
            firstName = cursor.getString(4);
            lastName = cursor.getString(5);
            address = cursor.getString(6);
            city = cursor.getString(7);
            postalCode = cursor.getString(8);
        }
        editTextUserName = (EditText) findViewById(R.id.editTextUserName);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextFirstName = (EditText) findViewById(R.id.editTextFirstName);
        editTextLastName = (EditText) findViewById(R.id.editTextLastName);
        editTextAddress = (EditText) findViewById(R.id.editTextAddress);
        editTextCity = (EditText) findViewById(R.id.editTextCity);
        editTextPostalCode = (EditText) findViewById(R.id.editTextPostalCode);

        editTextUserName.setText(userName);
        editTextEmail.setText(email);
        editTextFirstName.setText(firstName);
        editTextLastName.setText(lastName);
        editTextAddress.setText(address);
        editTextCity.setText(city);
        editTextPostalCode.setText(postalCode);
    }

    public void OnUpdate(View v) {
        final String fields[] = {"audienceId", "email", "userName", "password", "firstName", "lastName", "address", "city", "postalCode"};
        final String record[] = new String[9];
        if (checkEnteredData()) {
            record[0] = audienceId;
            record[1] = email;
            record[2] = userName;
            record[3] = password;
            record[4] = editTextFirstName.getText().toString();
            record[5] = editTextLastName.getText().toString();
            record[6] = editTextAddress.getText().toString();
            record[7] = editTextCity.getText().toString();
            record[8] = editTextPostalCode.getText().toString();
            //populate the row with some values
            db.updateRecord("tbl_audience",fields,record);
            if (db.checkUser(userName)) {
                intent = new Intent(ProfileActivity.this, WelcomeActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "Something Went Wrong! Restart App", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Please fill up required details", Toast.LENGTH_LONG).show();
        }
    }
    //Validation for each and every editTexts
    public boolean checkEnteredData() {
        boolean isValid = true;
        //First Name isEmpty Validation
        if (TextUtils.isEmpty(editTextFirstName.getText().toString())) {
            editTextFirstName.setError(getString(R.string.error_message_first_name));
            isValid = false;
        }
        //Last Name isEmpty Validation
        if (TextUtils.isEmpty(editTextLastName.getText().toString())) {
            editTextLastName.setError(getString(R.string.error_message_last_name));
            isValid = false;
        }
        //Check Address Length
        if (editTextAddress.getText().toString().trim().length() <= 0) {
            editTextAddress.setError(getString(R.string.error_message_address));
            isValid = false;
        }
        //Check City Length
        if (editTextCity.getText().toString().trim().length() <= 0) {
            editTextCity.setError(getString(R.string.error_message_city));
            isValid = false;
        }
        //Check PostalCode length
        if (editTextPostalCode.getText().toString().trim().length() <= 0) {
            editTextPostalCode.setError(getString(R.string.error_message_postal_code));
            isValid = false;
        }
        //if all values are valid return true else false
        return isValid;
    }

}

