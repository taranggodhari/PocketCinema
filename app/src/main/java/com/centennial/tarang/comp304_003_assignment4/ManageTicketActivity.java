package com.centennial.tarang.comp304_003_assignment4;

//Author: Vrunda Shah(300900997), Tarang Godhari (300931365)
//COMP304_003Assignment4_ManageTicketActivity
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class ManageTicketActivity extends AppCompatActivity {
    int bookingId, audienceId, movieId;
    DatabaseHelper db;
    String movieName, userName;
    String showDate, paymentDate, amountPaid, bookingStatus, showTime;
    TextView movieNameTextView, userNameTextView, showTimeTextView, showDateTextView, amountPaidTextView, paymentDateTextView;
    RadioButton cancelRadio, confirmRadio;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_ticket);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new DatabaseHelper(this);
        SharedPreferences mySharedPreferences = getSharedPreferences("MySharedPreferences", MODE_PRIVATE);
        bookingId = Integer.parseInt(mySharedPreferences.getString("bookingId", ""));
        Cursor cursor = db.getBookingDetails(bookingId);
        if (cursor.moveToFirst()) {
            audienceId = Integer.parseInt(cursor.getString(1));
            movieId = Integer.parseInt(cursor.getString(2));
            paymentDate = cursor.getString(3);
            amountPaid = cursor.getString(4);
            showDate = cursor.getString(5);
            showTime = cursor.getString(6);
            bookingStatus = cursor.getString(7);
        }
        cursor = db.getUserById(audienceId);
        if (cursor.moveToFirst()) {
            userName = cursor.getString(2);
        }
        cursor = db.getMovieById(movieId);
        if (cursor.moveToFirst()) {
            movieName = cursor.getString(1);
        }

        //fetch each textvalue for booked ticket for respective audience
        movieNameTextView = (TextView) findViewById(R.id.textViewMovieName);
        userNameTextView = (TextView) findViewById(R.id.textViewUserName);
        showTimeTextView = (TextView) findViewById(R.id.textViewShowTime);
        showDateTextView = (TextView) findViewById(R.id.textViewShowDate);
        amountPaidTextView = (TextView) findViewById(R.id.textViewPaymentAmount);
        paymentDateTextView = (TextView) findViewById(R.id.textViewPaymentDate);
        confirmRadio = (RadioButton) findViewById(R.id.confirmRadio);
        cancelRadio = (RadioButton) findViewById(R.id.cancelRadio);
        movieNameTextView.setText(movieName);
        userNameTextView.setText(userName);
        showTimeTextView.setText(showTime);
        showDateTextView.setText(showDate);
        amountPaidTextView.setText(amountPaid);
        paymentDateTextView.setText(paymentDate);

        //Check bookingStatus
        if (bookingStatus.equals("Confirmed")) {
            cancelRadio.setChecked(false);
            confirmRadio.setChecked(true);
        } else {
            confirmRadio.setChecked(false);
            cancelRadio.setChecked(true);
        }

    }

    //Upadte ticket by updating bookingStatus
    public void UpdateTicket(View v) {
        final String fields[] = {"bookingId", "audienceId", "movieId", "paymentDate", "amountPaid", "showDate", "showTime", "bookingStatus"};
        final String record[] = new String[8];
        final int foreignRecord[] = new int[3];
        RadioGroup statusGroup = (RadioGroup) findViewById(R.id.bookingStatusGroup);
        int checkedRoleRadio = statusGroup.getCheckedRadioButtonId();
        RadioButton rb = (RadioButton) findViewById(checkedRoleRadio);
        switch (rb.getId()) {
            case R.id.cancelRadio:
                bookingStatus = "Cancelled";
                break;
            case R.id.confirmRadio:
                bookingStatus = "Confirmed";
                break;
        }
        foreignRecord[0] = bookingId;
        foreignRecord[1] = audienceId;
        foreignRecord[2] = movieId;
        record[3] = paymentDate;
        record[4] = amountPaid;
        record[5] = showDate;
        record[6] = showTime;
        record[7] = bookingStatus;

        //populate the row with some values
        db.updateBookingRecord("tbl_booking", fields, record, foreignRecord);
        Toast.makeText(this, "Ticket Updated", Toast.LENGTH_SHORT).show();
        intent = new Intent(ManageTicketActivity.this, TicketBookedActivity.class);
        startActivity(intent);
    }


    public void DeleteTicket(View v) {
        db.deleteRecord("tbl_booking", "bookingId", Integer.toString(bookingId));
        Toast.makeText(this, "Ticket Deleted", Toast.LENGTH_SHORT).show();
        intent = new Intent(ManageTicketActivity.this, TicketBookedActivity.class);
        startActivity(intent);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}