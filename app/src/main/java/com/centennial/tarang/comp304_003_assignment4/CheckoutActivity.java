package com.centennial.tarang.comp304_003_assignment4;
//Author: Vrunda Shah(300900997), Tarang Godhari (300931365)
//COMP304_003Assignment4_CheckoutActivity
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class CheckoutActivity extends AppCompatActivity {
    DatabaseHelper db;
    String movieName, director, genre;
    String showDate, paymentDate, amountPaid, bookingStatus, showTime;
    TextView movieNameTextView, showTimeTextView, showDateTextView, amountPaidTextView, bookingStatusTextView;
    int audienceId, movieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ImageView imageView = (ImageView) findViewById(R.id.imageView);

        //fetch required fields for checkout
        movieNameTextView = (TextView) findViewById(R.id.movieName);
        showTimeTextView = (TextView) findViewById(R.id.showTime);
        showDateTextView = (TextView) findViewById(R.id.showDate);
        amountPaidTextView = (TextView) findViewById(R.id.amountPaid);
        bookingStatusTextView = (TextView) findViewById(R.id.bookingStatus);

        db = new DatabaseHelper(this);
        SharedPreferences mySharedPreferences = getSharedPreferences("MySharedPreferences", MODE_PRIVATE);
        Cursor cursor = db.getBookingDetails(Integer.parseInt(mySharedPreferences.getString("bookingId", "")));
        if (cursor.moveToFirst()) {
            audienceId = Integer.parseInt(cursor.getString(1));
            movieId = Integer.parseInt(cursor.getString(2));
            paymentDate = cursor.getString(3);
            amountPaid = cursor.getString(4);
            showDate = cursor.getString(5);
            showTime = cursor.getString(6);
            bookingStatus = cursor.getString(7);
        }
        cursor = db.getMovieById(movieId);
        if (cursor.moveToFirst()) {
            movieName = cursor.getString(1);
            director = cursor.getString(2);
            genre = cursor.getString(3);
        }

        movieNameTextView.setText(movieName);
        showTimeTextView.setText(showTime);
        showDateTextView.setText(showDate);
        amountPaidTextView.setText("$"+amountPaid);
        bookingStatusTextView.setText(bookingStatus);

        //set the imageView based on the name of movie
        switch (movieName) {
            case "Glass":
                //set the resource of the image view as movie poster
                imageView.setImageResource(R.drawable.glass_poster);
                break;
            case "Bumblebee":
                //set the resource of the image view as movie poster
                imageView.setImageResource(R.drawable.bumblebee_poster);
                break;
            case "Escape Room":
                //set the resource of the image view as movie poster
                imageView.setImageResource(R.drawable.escape_room_poster);
                break;
            case "Replicas":
                //set the resource of the image view as movie poster
                imageView.setImageResource(R.drawable.replicas_poster);
                break;
            case "The Mule":
                //set the resource of the image view as movie poster
                imageView.setImageResource(R.drawable.the_mule_poster);
                break;
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
