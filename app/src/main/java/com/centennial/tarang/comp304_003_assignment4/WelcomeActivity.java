package com.centennial.tarang.comp304_003_assignment4;

//Author: Vrunda Shah(300900997), Tarang Godhari (300931365)
//COMP304_003Assignment4_WelcomeActivity

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class WelcomeActivity extends AppCompatActivity {
    TextView userNameText;
    DatabaseHelper db;
    Intent intent;
String userName, userRole;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        setContentView(R.layout.activity_welcome);
        db = new DatabaseHelper(this);
        // Retrieving from shared preferences
        SharedPreferences mySharedPreferences = getSharedPreferences("MySharedPreferences", MODE_PRIVATE);
        userName = mySharedPreferences.getString("userName", "");
        userRole = mySharedPreferences.getString("userRole", "");
        //Display welcome message with respective username
        userNameText = (TextView) findViewById(R.id.userNameText);
        userNameText.setText("Welcome " + userName + " !");


        // Find the ScrollView
        ScrollView movieScrollView = (ScrollView) findViewById(R.id.movieScrollView);
        //Add Linear Layout
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setBackgroundResource(R.color.colorPrimaryDark);
        LinearLayout.LayoutParams paramsLinearLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(paramsLinearLayout);
        linearLayout.setGravity(Gravity.CENTER);

        List movies = db.getMovieNameList("tbl_movies");
        for (Object movie : movies) {
            //Add TextView
            TextView textView = new TextView(this);
            textView.setTextSize(18);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            textView.setTextColor(Color.WHITE);
            textView.setText(movie.toString());
            // Add Buttons
            ImageButton button = new ImageButton(this);
            LinearLayout.LayoutParams paramsImageButton = new LinearLayout.LayoutParams(250, 250);
            button.setLayoutParams(paramsImageButton);
            button.setScaleType(ImageView.ScaleType.FIT_CENTER);
            button.setOnClickListener(imageButtonClick);
            switch (movie.toString()) {
                case "Glass":
                    button.setId(R.id.Glass);
                    button.setImageResource(R.drawable.glass_poster);
                    break;
                case "Bumblebee":
                    button.setId(R.id.Bumblebee);
                    button.setImageResource(R.drawable.bumblebee_poster);
                    break;
                case "Escape Room":
                    button.setId(R.id.EscapeRoom);
                    button.setImageResource(R.drawable.escape_room_poster);
                    break;
                case "Replicas":
                    button.setId(R.id.Replicas);
                    button.setImageResource(R.drawable.replicas_poster);
                    break;
                case "The Mule":
                    button.setId(R.id.TheMule);
                    button.setImageResource(R.drawable.the_mule_poster);
                    break;
            }
            linearLayout.addView(button);
            linearLayout.addView(textView);
//            linearLayout.addView(linearLayout);
            // Create a LinearLayout element
            // Add the LinearLayout element to the ScrollView
        }
        movieScrollView.addView(linearLayout);
    }

    //SharedPreferences for MovieList and and forward it to BookingActivity
    View.OnClickListener imageButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            SharedPreferences mySharedPreference = getSharedPreferences("MySharedPreferences", 0);
            SharedPreferences.Editor prefEditor = mySharedPreference.edit();
            switch (view.getId()) {
                case R.id.Glass:
                    prefEditor.putString("movieName", "Glass");
                    prefEditor.commit();
                    intent = new Intent(WelcomeActivity.this, BookingActivity.class);
                    startActivity(intent);
                    break;
                case R.id.Bumblebee:
                    prefEditor.putString("movieName", "Bumblebee");
                    prefEditor.commit();
                    intent = new Intent(WelcomeActivity.this, BookingActivity.class);
                    startActivity(intent);
                    break;
                case R.id.EscapeRoom:
                    prefEditor.putString("movieName", "Escape Room");
                    prefEditor.commit();
                    intent = new Intent(WelcomeActivity.this, BookingActivity.class);
                    startActivity(intent);
                    break;
                case R.id.Replicas:
                    prefEditor.putString("movieName", "Replicas");
                    prefEditor.commit();
                    intent = new Intent(WelcomeActivity.this, BookingActivity.class);
                    startActivity(intent);
                    break;
                case R.id.TheMule:
                    prefEditor.putString("movieName", "The Mule");
                    prefEditor.commit();
                    intent = new Intent(WelcomeActivity.this, BookingActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    };
}
