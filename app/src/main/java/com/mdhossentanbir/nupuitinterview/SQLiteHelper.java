package com.mdhossentanbir.nupuitinterview;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tanbir on 31-Mar-17.
 */

public class SQLiteHelper extends SQLiteOpenHelper {

    //Database info
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "contactdb";
    private static final String TABLE_CONTACT_INFO = "contactinfo";

    private static final String KEY_NAME = "name";
    private static final String KEY_NUMBER = "number";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create Table SQL Query
        String CREATE_TABLE_CONTACT_INFO = "CREATE TABLE " +TABLE_CONTACT_INFO+" ("+KEY_NAME+ " TEXT, "+KEY_NUMBER+" TEXT)";
        db.execSQL(CREATE_TABLE_CONTACT_INFO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_CONTACT_INFO);
        onCreate(db);
    }

    //Adding new value in Table
    public void addContact(ContactList contactList){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contactList.getName());
        values.put(KEY_NUMBER, contactList.getNumber());
        //inserting Row
        db.insert(TABLE_CONTACT_INFO, null, values);
        db.close();
    }

    //Getting All Contacts from Table
    public List<ContactList> getAllContact(){
        List<ContactList> contactLists = new ArrayList<ContactList>();
        //select all query
        String SelectQuery = "SELECT * FROM "+TABLE_CONTACT_INFO+" ORDER BY name ASC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(SelectQuery, null);

        //Looping through all rows and adding to list
        if (cursor.moveToFirst()){
            do {
                ContactList contact = new ContactList();
                contact.setName(cursor.getString(0));
                contact.setNumber(cursor.getString(1));
                contactLists.add(contact);
            }while (cursor.moveToNext());
        }
        return contactLists;
    }
}
