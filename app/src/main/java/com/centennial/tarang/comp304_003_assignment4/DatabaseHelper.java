package com.centennial.tarang.comp304_003_assignment4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "Cinema.db";
    private static final int DB_VERSION = 1;

    //class constructor
    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    //create the database
    public void createDatabase(Context context) {
        SQLiteDatabase db = getWritableDatabase();
        db = context.openOrCreateDatabase(
                DB_NAME,
                SQLiteDatabase.CREATE_IF_NECESSARY,
                null);

    }

    //Base onCreate method to initialize the sql Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        //
        String sqlAudience = "CREATE TABLE  tbl_audience  (audienceId  INTEGER PRIMARY KEY AUTOINCREMENT, email  TEXT NOT NULL UNIQUE, userName  TEXT NOT NULL UNIQUE, password  TEXT NOT NULL, firstName  TEXT NOT NULL, lastName  TEXT NOT NULL, address  TEXT NOT NULL, city  TEXT NOT NULL, postalCode  TEXT NOT NULL);";
        String sqlAdmin = "CREATE TABLE  tbl_admin  (employeeId  INTEGER PRIMARY KEY AUTOINCREMENT, userName  TEXT NOT NULL UNIQUE, password  TEXT NOT NULL,  firstName  TEXT NOT NULL,  lastName  TEXT NOT NULL);";
        String sqlMovies = "CREATE TABLE  tbl_movies  (movieId  INTEGER PRIMARY KEY AUTOINCREMENT, movieName  TEXT NOT NULL, director  TEXT NOT NULL, genre  TEXT NOT NULL);";
        String sqlBooking = "CREATE TABLE  tbl_booking  (bookingId  INTEGER PRIMARY KEY AUTOINCREMENT, audienceId  INTEGER, movieId  INTEGER, paymentDate  TEXT NOT NULL,  amountPaid  REAL NOT NULL,  showDate  TEXT NOT NULL,  showTime  TEXT NOT NULL,  bookingStatus  TEXT NOT NULL, FOREIGN KEY( movieId ) REFERENCES  tbl_movies ( movieId ) ON UPDATE CASCADE, FOREIGN KEY( audienceId ) REFERENCES  tbl_audience ( audienceId ) ON UPDATE CASCADE);";

        db.execSQL(sqlAudience);
        db.execSQL(sqlAdmin);
        db.execSQL(sqlMovies);
        db.execSQL(sqlBooking);
    }

    //Drop existing tables and call onCreate to upgrade the database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sqlAudience = "DROP TABLE IF EXISTS tbl_audience";
        String sqlAdmin = "DROP TABLE IF EXISTS tbl_admin";
        String sqlMovies = "DROP TABLE IF EXISTS tbl_movies";
        String sqlBooking = "DROP TABLE IF EXISTS tbl_booking";
        db.execSQL(sqlAudience);
        db.execSQL(sqlAdmin);
        db.execSQL(sqlMovies);
        db.execSQL(sqlBooking);

        onCreate(db);
    }

    /////////////////////////
    // Database operations
    /////////////////////////
    // Add a new record
    void addRecord(String tableName, String fields[], String record[]) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        for (int i = 1; i < record.length; i++)
            values.put(fields[i], record[i]);
        // Insert the row
        db.insert(tableName, null, values);
        db.close(); //close database connection
    }

    //Adding booking details
    long addBookingRecord(String tableName, String fields[], String record[], int foreignRecord[]) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        for (int i = 1; i < foreignRecord.length; i++)
            values.put(fields[i], foreignRecord[i]);
        for (int i = 3; i < record.length; i++)
            values.put(fields[i], record[i]);
        // Insert the row
        long id = db.insert(tableName, null, values);
        db.close(); //close database connection
        return id;
    }

    // Read booking details by booking id
    public Cursor getBookingDetails(int bookingId) {
        // Select required records
        String selectQuery = "SELECT  * FROM tbl_booking WHERE bookingId = '" + bookingId + "';";
        //get the readable database
        SQLiteDatabase db = this.getReadableDatabase();
        //run the raw query and return the cursor
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    // Read all records
    public List getTable(String tableName) {
        List table = new ArrayList(); //to store all rows
        // Select all records
        String selectQuery = "SELECT  * FROM " + tableName;
        //get the readable database
        SQLiteDatabase db = this.getReadableDatabase();
        //run the raw query and return the cursor
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList row = new ArrayList(); //to store one row
        //scroll over rows and store each row in an array list object
        if (cursor.moveToFirst()) {
            do { // for each row
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    row.add(cursor.getString(i));
                }

                table.add(row); //add row to the list

            } while (cursor.moveToNext());
        }

        // return table as a list
        return table;
    }

    // Read Movie Name list from the table tbl_movies
    public List getMovieNameList(String tableName) {
        //Initialize the Arraylist of table
        List table = new ArrayList(); //to store all rows
        // Select all records
        String selectQuery = "SELECT  * FROM  " + tableName;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //Condition to fetch and return only the list of movieName
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String name = cursor.getString(cursor.getColumnIndex("movieName"));
                table.add(name);
                cursor.moveToNext();
            }
        }
        // return table as a list
        return table;
    }

    // Read all records
    public Cursor getMovie(String movieName) {
        // Select movie record using the movieName
        String selectQuery = "SELECT  * FROM tbl_movies WHERE movieName = '" + movieName + "';";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    //Get the row of movie using the movieId
    public Cursor getMovieById(int movieId) {
        // Select all records
        String selectQuery = "SELECT  * FROM tbl_movies WHERE movieId = " + movieId + ";";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    // Update a record
    public int updateRecord(String tableName, String fields[], String record[]) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        for (int i = 1; i < record.length; i++)

                values.put(fields[i], record[i]);

        // updating row with given id = record[0]
        return db.update(tableName, values, fields[0] + " = ?",
                new String[]{record[0]});
    }

    // Delete a record with a given id
    public void deleteRecord(String tableName, String idName, String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tableName, idName + " = ?",
                new String[]{id});
        db.close();
    }

    // Read the audienceId using userName
    public String getAudienceId(String userName) {
        // Select all records
        String selectQuery = "SELECT  * FROM tbl_audience WHERE userName = '" + userName + "';";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndex("audienceId"));
        }

        // return table as a list
        return "0";
    }

    //Get the row of movie using the user
    public Cursor getUserByUserName(String userName) {
        // Select all records
        String selectQuery = "SELECT  * FROM tbl_audience WHERE userName = '" + userName + "';";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }
    //Get the row of movie using the user
    public Cursor getUserById(int userId) {
        // Select all records
        String selectQuery = "SELECT  * FROM tbl_audience WHERE audienceId = " + userId + ";";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }
    // Read all records
    public List getUsersList() {
        List table = new ArrayList(); //to store all rows
        // Select all records
        String selectQuery = "SELECT  * FROM tbl_audience;";
        //get the readable database
        SQLiteDatabase db = this.getReadableDatabase();
        //run the raw query and return the cursor
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList row = new ArrayList(); //to store one row
        //scroll over rows and store each row in an array list object
        if (cursor.moveToFirst()) {
            do { // for each row
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    row.add(cursor.getString(i));
                    if(row.size() ==  9){
                        table.add(row); //add row to the list
                        row = new ArrayList();
                    }
                }

               // table.add(row); //add row to the list

            } while (cursor.moveToNext());
        }

        // return table as a list
        return table;
    }
    // This method to check user exist or not
    public boolean checkUser(String userName) {
        SQLiteDatabase db = this.getReadableDatabase();
        // query user table with condition
        Cursor cursor = db.rawQuery("SELECT audienceId FROM tbl_audience WHERE userName = '" + userName + "';", null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

    //This method to check user exist or not
    public boolean checkUser(String userName, String password, String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + tableName + " WHERE userName = '" + userName + "' AND password = '" + password + "';", null);
        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }
        return false;
    }

    //Seed the movie database
    public void SeedMovies() {
        //create an array of table fields
        final String fields[] = {"movieId", "movieName", "director", "genre"};
        //Create dummy records
        final String record[] = new String[4];
        //populate the record array
        record[1] = "Glass";
        record[2] = "M. Night Shyamalan";
        record[3] = "Science Fiction";
        //run the addRecord method to create the movie
        this.addRecord("tbl_movies", fields, record);

        //populate the record array
        record[1] = "Bumblebee";
        record[2] = "Travis Knight";
        record[3] = "Action";
        //run the addRecord method to create the movie
        this.addRecord("tbl_movies", fields, record);

        //populate the record array
        record[1] = "Escape Room";
        record[2] = "Adam Robitel";
        record[3] = "Adventure";
        //run the addRecord method to create the movie
        this.addRecord("tbl_movies", fields, record);

        //populate the record array
        record[1] = "Replicas";
        record[2] = "Jeffrey Nachmanoff";
        record[3] = "Mystery";
        //run the addRecord method to create the movie
        this.addRecord("tbl_movies", fields, record);

        //populate the record array
        record[1] = "The Mule";
        record[2] = "Clint Eastwood";
        record[3] = "Suspense";
        //run the addRecord method to create the movie
        this.addRecord("tbl_movies", fields, record);
    }

    public void SeedAdmin() {
        final String fields[] = {"employeeId", "userName", "password", "firstName", "lastName"};
        final String record[] = new String[5];
        record[1] = "admin";
        record[2] = "password";
        record[3] = "Jenna";
        record[4] = "Doe";
        this.addRecord("tbl_admin", fields, record);
        record[1] = "admindoe";
        record[2] = "password";
        record[3] = "John";
        record[4] = "Doe";
        this.addRecord("tbl_admin", fields, record);
    }

    //     This method to check user exist or not
    public boolean checkAdmin(String userName) {
        SQLiteDatabase db = this.getReadableDatabase();
        // query user table with condition
        Cursor cursor = db.rawQuery("SELECT employeeId FROM tbl_admin WHERE userName = '" + userName + "';", null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }

        return false;
    }
}

