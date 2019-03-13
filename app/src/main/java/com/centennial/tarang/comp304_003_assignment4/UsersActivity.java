package com.centennial.tarang.comp304_003_assignment4;

//Author: Vrunda Shah(300900997), Tarang Godhari (300931365)
//COMP304_003Assignment4_UsersActivity
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class UsersActivity extends AppCompatActivity {
    DatabaseHelper db;
    String FullName, Email;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        db = new DatabaseHelper(this);
        // Find the ScrollView
        ScrollView usersListScrollView = (ScrollView) findViewById(R.id.usersListScrollView);
        //Add Linear Layout
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setBackgroundResource(R.color.colorSecondaryDark);
        LinearLayout.LayoutParams paramsLinearLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(paramsLinearLayout);
        linearLayout.setGravity(Gravity.CENTER);

        //get users from DatabaseHelper
        List users = db.getUsersList();
        for (int i = 0; i < users.size(); i++) {
            List user = (List) users.get(i);
            Email = user.get(1).toString();
            FullName = user.get(4).toString() + " " + user.get(5).toString();

            LinearLayout linearLayoutIn = new LinearLayout(this);

            linearLayoutIn.setOrientation(LinearLayout.VERTICAL);
            linearLayoutIn.setGravity(Gravity.CENTER);
            linearLayoutIn.setPadding(15, 15, 15, 15);
            linearLayoutIn.setLayoutParams(paramsLinearLayout);
            linearLayoutIn.setGravity(Gravity.CENTER);

            Button buttonUser = new Button(this);
            buttonUser.setId(Integer.parseInt(user.get(0).toString()));
            buttonUser.setLayoutParams(paramsLinearLayout);
            buttonUser.setBackgroundResource(R.drawable.white_button_rectangle);
            buttonUser.setTextColor(Color.WHITE);
            buttonUser.setText(FullName);
            buttonUser.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    intent = new Intent(UsersActivity.this, UserDetailActivity.class);
                    SharedPreferences mySharedPreference = getSharedPreferences("MySharedPreferences", 0);
                    SharedPreferences.Editor prefEditor = mySharedPreference.edit();
                    prefEditor.putString("userId", Integer.toString(v.getId()));
                    prefEditor.commit();
                    startActivity(intent);
                }
            });
            //Add TextView
            TextView textViewFullName = new TextView(this);
            textViewFullName.setTextSize(18);
            textViewFullName.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            textViewFullName.setTextColor(Color.WHITE);
            textViewFullName.setText(FullName);
            textViewFullName.setBackgroundResource(R.color.colorPrimaryDark);
            linearLayoutIn.addView(buttonUser);


            linearLayout.addView(linearLayoutIn);
        }
        usersListScrollView.addView(linearLayout);
    }
    //Method to handle the back button pressed
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
