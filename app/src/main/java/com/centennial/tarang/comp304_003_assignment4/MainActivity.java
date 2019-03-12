package com.centennial.tarang.comp304_003_assignment4;

//Author: Vrunda Shah(300900997), Tarang Godhari (300931365)
//COMP304_003Assignment4_MainActivity 

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Intent intent;
    String tableName = "tbl_audience";
    EditText userName, password;

    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);
        db.createDatabase(getApplicationContext());
        //Seed Movie Table
        if (db.getTable("tbl_movies").size() <= 0) {
            db.SeedMovies();
        }
        if (db.getTable("tbl_admin").size() <= 0) {
            db.SeedAdmin();
        }
//        db.dbInitialize(tables, tableCreatorString);
//        final DatabaseHelper db = new DatabaseHelper(this);
    }

    // OnClick Method When User Clicks "Login Button"
    public void GoNext(View v) {
        // Creating intent and setting this and next activity
        switch (v.getId()) {
            case R.id.login:
                //Get intent obj
                intent = new Intent(MainActivity.this, MainActivity.class);
                // Username and password of user
                userName = (EditText) findViewById(R.id.editTextUsername);
                password = (EditText) findViewById(R.id.editTextPassword);
                //Radiobutton to get UserROles
                RadioGroup roleGroup = (RadioGroup) findViewById(R.id.roleGroup);
                int checkedRoleRadio = roleGroup.getCheckedRadioButtonId();
                RadioButton rb = (RadioButton) findViewById(checkedRoleRadio);
                //Getting userRole
                String userRole = rb.getText().toString();
                tableName ="tb_" + userRole.toLowerCase();

                if (db.checkUser(userName.getText().toString(), password.getText().toString(), tableName)) {
                    SharedPreferences mySharedPreference = getSharedPreferences("MySharedPreferences", 0);
                    SharedPreferences.Editor prefEditor = mySharedPreference.edit();
                    prefEditor.putString("userName", userName.getText().toString());
                    prefEditor.putString("userRole", userRole);
                    prefEditor.commit();
//                    switch (userRole) {
//                        case "Admin":
//                            tableName = "tbl_admin";
//                            break;
//                        case "Audience":
//                            tableName = "tbl_audience";
//                            break;
//                    }

                    intent = new Intent(MainActivity.this, WelcomeActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Username, Password or Role is Invalid", Toast.LENGTH_LONG).show();
                }
                //  Start the activity
                break;
            case R.id.register:
                intent = new Intent(MainActivity.this, RegisterActivity.class);
                //  Start the activity
                startActivity(intent);
                break;
        }

    }
}
