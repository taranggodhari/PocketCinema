package com.centennial.tarang.comp304_003_assignment4;

//Author: Vrunda Shah(300900997), Tarang Godhari (300931365)
//COMP304_003Assignment4_UserDetailActivity
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class UserDetailActivity extends AppCompatActivity {
    DatabaseHelper db;
    Intent intent;
    String userId;
    String audienceId, email, userName, password, firstName, lastName, address, city, postalCode;
    TextView textViewUserName, textViewEmail, textViewFirstName, textViewLastName, textViewAddress, textViewCity, textViewPostalCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //get data for SignedIn user and update fields that user want to modify
        SharedPreferences mySharedPreferences = getSharedPreferences("MySharedPreferences", MODE_PRIVATE);
        userId = mySharedPreferences.getString("userId", "");
        db = new DatabaseHelper(this);
        Cursor cursor = db.getUserById(Integer.parseInt(userId));
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
        textViewUserName = (TextView) findViewById(R.id.textViewUserName);
        textViewEmail = (TextView) findViewById(R.id.textViewEmail);
        textViewFirstName = (TextView) findViewById(R.id.textViewFirstName);
        textViewLastName = (TextView) findViewById(R.id.textViewLastName);
        textViewAddress = (TextView) findViewById(R.id.textViewAddress);
        textViewCity = (TextView) findViewById(R.id.textViewCity);
        textViewPostalCode = (TextView) findViewById(R.id.textViewPostalCode);

        //Update user data
        textViewUserName.setText(userName);
        textViewEmail.setText(email);
        textViewFirstName.setText(firstName);
        textViewLastName.setText(lastName);
        textViewAddress.setText(address);
        textViewCity.setText(city);
        textViewPostalCode.setText(postalCode);

    }

    //Admin can delete a user
    public void DeleteUser(View v) {
        db.deleteRecord("tbl_audience", "audienceId", userId);
        if (!db.checkUser(userName)) {
            intent = new Intent(UserDetailActivity.this, UsersActivity.class);
            startActivity(intent);
        }
    }

    //Return to previous activity
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
