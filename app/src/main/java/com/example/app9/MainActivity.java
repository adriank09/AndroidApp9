package com.example.app9;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase db;

    protected SQLiteDatabase getDb() {
        return db;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnOpenCreateNewContact = findViewById(R.id.btnOpenCreateNewContact);
        btnOpenCreateNewContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CreateNewContactActivity.class);
                startActivity(intent);
            }
        });

        loadContacts();
    }

    protected void loadContacts() {
        SQLiteOpenHelper dbHelper = new ContactContract.ContactDbHelper(this);
        if (db == null) {
            db = dbHelper.getReadableDatabase();
        }

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                BaseColumns._ID,
                ContactContract.ContactEntry.COLUMN_NAME_NAME,
                ContactContract.ContactEntry.COLUMN_NAME_EMAIL
        };

        //// Filter results WHERE "title" = 'My Title'
        String selection = ContactContract.ContactEntry.COLUMN_NAME_NAME /*+ " = ?"*/;
        //String[] selectionArgs = { "My Title" };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                ContactContract.ContactEntry.COLUMN_NAME_NAME + " DESC";

        Cursor cursor = db.query(
                ContactContract.ContactEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null/*selection*/,              // The columns for the WHERE clause
                null/*selectionArgs*/,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        ArrayList<Contact> contacts = new ArrayList<>();
        // List itemIds = new ArrayList<>();
        while(cursor.moveToNext()) {
//            long itemId = cursor.getLong(
//                    cursor.getColumnIndexOrThrow(FeedEntry._ID));
//            itemIds.add(itemId);

            long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(ContactContract.ContactEntry._ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactContract.ContactEntry.COLUMN_NAME_NAME));
            String email = cursor.getString(cursor.getColumnIndexOrThrow(ContactContract.ContactEntry.COLUMN_NAME_EMAIL));

            Contact c = new Contact(itemId, name, email);
            contacts.add(c);
        }
        cursor.close();

        RecyclerView rvContacts = findViewById(R.id.rvContacts);
        ContactViewAdapter viewAdapter = new ContactViewAdapter(this);
        viewAdapter.setContacts(contacts);

        rvContacts.setAdapter(viewAdapter);
        rvContacts.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadContacts();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}