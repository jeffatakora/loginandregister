package com.example.jeff.loginandregister;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class SqliteHelper extends SQLiteOpenHelper {

    //DATABASE NAME
    public static final String DATABASE_NAME = "jeffdb";

    //DATABASE VERSION
    public static final int DATABASE_VERSION = 1;

    //TABLE NAME
    public static final String TABLE_USERS = "users";
    public static final String TABLE_MENU = "menu";
    public static final String TABLE_ORDER = "placed_order";

    //TABLE USERS COLUMNS
    public static final String KEY_ID = "id";
    public static final String KEY_USER_NAME = "username";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";

    //TABLE MENU COLUMNS
    public static final String O_NAME = "order_name";
    public static final String O_NO = "order_no";


    //TABLE ORDER COLUMNS
    public static final String P_ID = "_id";
    public static final String P_USER_ID = "user_id";
    public static final String P_ORDER_ID = "order_id";

    //SQL for creating users table
    public static final String SQL_TABLE_USERS = " CREATE TABLE " + TABLE_USERS
            + " ( "
            + KEY_ID + " INTEGER PRIMARY KEY, "
            + KEY_USER_NAME + " TEXT, "
            + KEY_EMAIL + " TEXT, "
            + KEY_PASSWORD + " TEXT"
            + " ) ";


    public static final String SQL_TABLE_MENU = "CREATE TABLE " + TABLE_MENU
            + "("
            + O_NO + " INTEGER PRIMARY KEY, "
            + O_NAME + " TEXT NOT NULL"
            + " ) ";

    public static final String SQL_TABLE_ORDER = "CREATE TABLE " + TABLE_ORDER + "("

            + P_USER_ID + " TEXT, " + P_ORDER_ID + " INTEGER, "
            + " FOREIGN KEY (" + P_USER_ID + ") REFERENCES " + TABLE_USERS + " (" + KEY_EMAIL + "), "
            + " FOREIGN KEY (" + P_ORDER_ID + ") REFERENCES " + TABLE_ORDER + " (" + O_NO + " )"
            + ")" ;

    public SqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //Create Tables when oncreate gets called
        sqLiteDatabase.execSQL(SQL_TABLE_USERS);
        sqLiteDatabase.execSQL(SQL_TABLE_MENU);
        sqLiteDatabase.execSQL(SQL_TABLE_ORDER);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //drop table to create new one if database version updated
        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS " + TABLE_USERS);
        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS " + TABLE_MENU);
        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS " + TABLE_ORDER);
    }

    //using this method we can add users to user table
    public void addUser(User user) {

        //get writable database
        SQLiteDatabase db = this.getWritableDatabase();

        //create content values to insert
        ContentValues values = new ContentValues();

        //Put username in  @values
        values.put(KEY_USER_NAME, user.getName());

        //Put email in  @values
        values.put(KEY_EMAIL, user.getEmail());

        //Put password in  @values
        values.put(KEY_PASSWORD, user.getPassword());

        // insert row
        long todo_id = db.insert(TABLE_USERS, null, values);
    }


    //using this method we can add order
    public void addorder(String order){
        //get writable database
        SQLiteDatabase db = this.getWritableDatabase();

        //create content values to insert
        ContentValues values = new ContentValues();

        //Put order in  @values
        values.put(O_NAME, order);

        // insert row
        long todo_id = db.insert(TABLE_ORDER, null, values);
    }

//get single customer
	public User getSingleCust(String username){
        String query = "Select * FROM " + TABLE_USERS + "WHERE" + KEY_USER_NAME + " = " + "'" + username + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        User user1 = new User(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3));
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            user1.setId(cursor.getString(0));
            user1.setName(cursor.getString(1));
            cursor.close();
        } else {
            user1 = null;
        }
        db.close();
        return user1;
	}

    //updating customer info
    public int updateUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_USER_NAME, user.getName());
        cv.put(KEY_EMAIL, user.getEmail());
        cv.put(KEY_PASSWORD, user.getPassword());

        //updating a row
        return db.update(TABLE_USERS,cv,KEY_ID + " = ?", new String[] {String.valueOf(user.getId())});

    }
    //deleting customer info
    public void deleteUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USERS, KEY_ID + " = ?", new String[] {String.valueOf(user.getId())});
        db.close();
    }

    public User Authenticate(User user) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,// Selecting Table
                new String[]{KEY_ID, KEY_USER_NAME, KEY_EMAIL, KEY_PASSWORD},//Selecting columns want to query
                KEY_EMAIL + "=?",
                new String[]{user.email},//Where clause
                null, null, null);

        if (cursor != null && cursor.moveToFirst()&& cursor.getCount()>0) {
            //if cursor has value then in user database there is user associated with this given email
            User user1 = new User(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));

            //Match both passwords check they are same or not
            if (user.password.equalsIgnoreCase(user1.password)) {
                return user1;
            }
        }

        //if user password does not matches or there is no record with that email then return @false
        return null;
    }

    public boolean isEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,// Selecting Table
                new String[]{KEY_ID, KEY_USER_NAME, KEY_EMAIL, KEY_PASSWORD},//Selecting columns want to query
                KEY_EMAIL + "=?",
                new String[]{email},//Where clause
                null, null, null);

        if (cursor != null && cursor.moveToFirst()&& cursor.getCount()>0) {
            //if cursor has value then in user database there is user associated with this given email so return true
            return true;
        }

        //if email does not exist return false
        return false;
    }


}