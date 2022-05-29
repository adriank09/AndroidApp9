package com.example.app9;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.QuickContactBadge;
import android.widget.Toast;

public class CreateNewContactActivity extends AppCompatActivity {
    private ContactContract.ContactDbHelper dbHelper;
    private SQLiteDatabase db;
    private EditText etName;
    private EditText etEmail;
    private Button btnAddContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_contact);

        // Initialize
        dbHelper = new ContactContract.ContactDbHelper(getApplicationContext());
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        btnAddContact = findViewById(R.id.btnAddContact);

        // Gets the data repository in write mode
        db = dbHelper.getWritableDatabase();

        // Set up event handler
        btnAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etName.getText().toString();
                String email = etEmail.getText().toString();

                // Create a new map of values, where column names are the keys
                ContentValues values = new ContentValues();
                values.put(ContactContract.ContactEntry.COLUMN_NAME_NAME, name);
                values.put(ContactContract.ContactEntry.COLUMN_NAME_EMAIL, email);

                // Insert the new row, returning the primary key value of the new row
                long newRowId = db.insert(ContactContract.ContactEntry.TABLE_NAME, null, values);

                // If new contact was added to db successfully, we close and return to previous activity.
                if (newRowId > 0) {
                    finish();
                }
                // If not, we show an error
                else {
                    Toast.makeText(view.getContext(), "Error while adding new contact. newRowId: " + newRowId, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        // Optimal to close the database in onDestroy
        dbHelper.close();
        super.onDestroy();
    }
}