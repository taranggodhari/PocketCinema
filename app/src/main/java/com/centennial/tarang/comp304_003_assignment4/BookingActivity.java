package com.centennial.tarang.comp304_003_assignment4;

//Author: Vrunda Shah(300900997), Tarang Godhari (300931365)
//COMP304_003Assignment4_BookingActivity
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BookingActivity extends AppCompatActivity {
    String showTime = null;
    String showDate, paymentDate, amountPaid, bookingStatus;
    int audienceId, movieId;
    TextView textViewMovieName, textViewDirector, textViewGenre, textViewPaymentDate, textViewPaymentAmount;
    DatabaseHelper db;
    EditText editTextDate;
    Button setDateButton;
    Calendar calendar;
    int year, month, day;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new DatabaseHelper(this);
        SharedPreferences mySharedPreferences = getSharedPreferences("MySharedPreferences", MODE_PRIVATE);
        Cursor cursor = db.getMovie(mySharedPreferences.getString("movieName", ""));
        audienceId = Integer.parseInt(db.getAudienceId(mySharedPreferences.getString("userName", "")));

        textViewMovieName = (TextView) findViewById(R.id.textViewMovieName);
        textViewDirector = (TextView) findViewById(R.id.textViewDirector);
        textViewGenre = (TextView) findViewById(R.id.textViewGenre);
        if (cursor.moveToFirst()) {
            movieId = Integer.parseInt(cursor.getString(0));
            textViewMovieName.setText(cursor.getString(1));
            textViewDirector.setText(cursor.getString(2));
            textViewGenre.setText(cursor.getString(3));
        }
        //getting calendar instance
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        //accessing EditText and Button
        editTextDate = (EditText) findViewById(R.id.editTextDate);
        setDateButton = (Button) findViewById(R.id.setDateButton);
        setDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(BookingActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        showDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        //sets date in EditText
                        editTextDate.setText(dayOfMonth + "-" + (month + 1) + "-" + year);
                    }
                }, year, month, day);
                //shows DatePickerDialog
                datePickerDialog.show();
            }
        });
        textViewPaymentDate = (TextView) findViewById(R.id.textViewPaymentDate);
        textViewPaymentAmount = (TextView) findViewById(R.id.textViewPaymentAmount);

        amountPaid = "8.99";
        paymentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

        textViewPaymentDate.setText(paymentDate);
        textViewPaymentAmount.setText("$" + amountPaid);

    }

    public void GoNext(View v) {
        RadioGroup timeGroup = (RadioGroup) findViewById(R.id.timeGroup);
        int checkedRoleRadio = timeGroup.getCheckedRadioButtonId();
        RadioButton rb = (RadioButton) findViewById(checkedRoleRadio);
        showTime = rb.getText().toString();
        bookingStatus = "Confirmed";

        //Save Booking Details
        final String fields[] = {"bookingId", "audienceId", "movieId", "paymentDate", "amountPaid", "showDate", "showTime", "bookingStatus"};
        final String record[] = new String[8];
        final int foreignRecord[] = new int[3];
        if (!(TextUtils.isEmpty(showDate))) {
            foreignRecord[1] = audienceId;
            foreignRecord[2] = movieId;
            record[3] = paymentDate;
            record[4] = amountPaid;
            record[5] = showDate;
            record[6] = showTime;
            record[7] = bookingStatus;
            //populate the row with some values
            long bookingId  = db.addBookingRecord("tbl_booking", fields, record, foreignRecord);
            SharedPreferences mySharedPreference = getSharedPreferences("MySharedPreferences", 0);
            SharedPreferences.Editor prefEditor = mySharedPreference.edit();
            prefEditor.putString("bookingId", String.valueOf(bookingId));
            prefEditor.commit();
            intent = new Intent(BookingActivity.this, CheckoutActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "Please select a Show Date", Toast.LENGTH_LONG).show();
        }
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
