package com.centennial.tarang.comp304_003_assignment4;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.List;

public class TicketBookedActivity extends AppCompatActivity {
    DatabaseHelper db;
    String bookingId,audienceId, movieId;
    String movieName, userName,bookingStatus;
//    paymentDate, amountPaid, showDate,showTime, bookingStatus;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_booked);
        db = new DatabaseHelper(this);
        // Find the ScrollView
        ScrollView bookedTicketScrollView = (ScrollView) findViewById(R.id.bookedTicketScrollView);
        //Add Linear Layout
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setBackgroundResource(R.color.colorSecondaryDark);
        LinearLayout.LayoutParams paramsLinearLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(paramsLinearLayout);
        linearLayout.setGravity(Gravity.CENTER);

        List bookedTickets = db.getBookingList();
        for (int i = 0; i < bookedTickets.size(); i++) {
            List ticket = (List) bookedTickets.get(i);
            bookingId = ticket.get(0).toString();
            audienceId = ticket.get(1).toString();
            movieId = ticket.get(2).toString();
//            paymentDate = user.get(3).toString();
//            amountPaid = user.get(4).toString();
//            showDate = user.get(5).toString();
//            showTime = user.get(6).toString();
            bookingStatus = ticket.get(7).toString();
            Cursor cursor = db.getUserById(Integer.parseInt(audienceId));
            if (cursor.moveToFirst()) {
                userName = cursor.getString(2);
            }
            cursor = db.getMovieById(Integer.parseInt(movieId));
            if (cursor.moveToFirst()) {
                movieName = cursor.getString(1);
            }

            LinearLayout linearLayoutIn = new LinearLayout(this);
            linearLayoutIn.setOrientation(LinearLayout.VERTICAL);
            linearLayoutIn.setGravity(Gravity.CENTER);
            linearLayoutIn.setPadding(15, 15, 15, 15);
            linearLayoutIn.setLayoutParams(paramsLinearLayout);
            linearLayoutIn.setGravity(Gravity.CENTER);

            TextView textViewUserName = new TextView(this);
            textViewUserName.setTextSize(18);
            textViewUserName.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            textViewUserName.setTextColor(Color.WHITE);
            textViewUserName.setText(userName);
            textViewUserName.setBackgroundResource(R.color.colorPrimaryDark);

            Button buttonTicket = new Button(this);
            buttonTicket.setId(Integer.parseInt(ticket.get(0).toString()));
            buttonTicket.setLayoutParams(paramsLinearLayout);
            buttonTicket.setBackgroundResource(R.drawable.white_button_rectangle);
            if(bookingStatus.equals("Confirmed")) {
                buttonTicket.setTextColor(Color.WHITE);
            }else{
                buttonTicket.setTextColor(Color.RED);
            }
            buttonTicket.setText(movieName);
            buttonTicket.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    intent = new Intent(TicketBookedActivity.this, ManageTicketActivity.class);
                    SharedPreferences mySharedPreference = getSharedPreferences("MySharedPreferences", 0);
                    SharedPreferences.Editor prefEditor = mySharedPreference.edit();
                    prefEditor.putString("bookingId", Integer.toString(v.getId()));
                    prefEditor.commit();
                    startActivity(intent);
                }
            });
            linearLayoutIn.addView(textViewUserName);
            linearLayoutIn.addView(buttonTicket);
            linearLayout.addView(linearLayoutIn);
        }
        bookedTicketScrollView.addView(linearLayout);
    }
}
