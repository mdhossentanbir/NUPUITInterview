package com.mdhossentanbir.nupuitinterview;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    public static String MyPREFERENCES = "MyPref";
    private RecyclerView recyclerView;
    ImageView imageView;
    private List<ContactList> contactLists;
    SQLiteHelper sqLite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        sqLite = new SQLiteHelper(this);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        imageView = (ImageView)findViewById(R.id.imageView);
        String imagePath = "drawable://"+R.drawable.profile_pic;
        ImageUtil.displayRoundImage(imageView,imagePath, null, this);
        contactLists = new ArrayList<>();

        LoadContact loadContact = new LoadContact();
        loadContact.execute();


    }

    class LoadContact extends AsyncTask<String, String, String> {
        ProgressDialog progDailog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progDailog = new ProgressDialog(MainActivity.this);
            progDailog.setMessage("Finding Contacts");
            progDailog.setIndeterminate(false);
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(false);
            progDailog.show();
        }
        @Override
        protected String doInBackground(String... aurl) {
            if (sharedPreferences.contains("table_exixts")){
            }else {
                readPhoneContacts(MainActivity.this);
            }

            return null;
        }
        @Override
        protected void onPostExecute(String unused) {
            contactLists = sqLite.getAllContact();
            CustomAdapter adapter = new CustomAdapter(contactLists);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
            progDailog.dismiss();
        }
    }



    public void readPhoneContacts(Context context)
    {      contactLists.clear();
        Cursor cursor = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        Integer contactsCount = cursor.getCount(); //get how many contacts you have in your contacts list
        if (contactsCount > 0)
        {
            while(cursor.moveToNext())
            {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)
                {
                    //the below cursor will give you details for multiple contacts
                    Cursor pCursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
                            new String[]{id}, null);
                    // continue till this cursor reaches to all phone numbers which are associated with a contact in the contact list
                    while (pCursor.moveToNext())
                    {
                        String phoneNo 	= pCursor.getString(pCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        //contactLists.add(new ContactList(contactName,phoneNo));
                        sqLite.addContact(new ContactList(contactName,phoneNo));

                    }
                    pCursor.close();
                }
            }
            cursor.close();
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("table_exixts",true);
        editor.commit();
    }
}
