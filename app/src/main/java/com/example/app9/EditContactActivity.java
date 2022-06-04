package com.example.app9;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditContactActivity extends AppCompatActivity {

    private EditText etEditContactName;
    private EditText etEditContactEmail;
    private Contact c;
    private Button btnUpdateContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        etEditContactName = findViewById(R.id.etEditContactName);
        etEditContactEmail = findViewById(R.id.etEditContactEmail);
        btnUpdateContact = findViewById(R.id.btnUpdateContact);

        btnUpdateContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteOpenHelper dbHelper = new ContactContract.ContactDbHelper(getApplicationContext());
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                // New value for one column
                String name = etEditContactName.getText().toString();
                String email = etEditContactEmail.getText().toString();

                ContentValues values = new ContentValues();
                values.put(ContactContract.ContactEntry.COLUMN_NAME_NAME, name);
                values.put(ContactContract.ContactEntry.COLUMN_NAME_EMAIL, email);

                // Which row to update, based on the title
                String selection = ContactContract.ContactEntry._ID + " LIKE ?";
                String[] selectionArgs = { Long.toString(c.getId()) };

                int count = db.update(
                        ContactContract.ContactEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);

                Toast.makeText(view.getContext(), "Count: " + count, Toast.LENGTH_SHORT).show();
            }
        });


        Intent intent = getIntent();
        c = (Contact) intent.getBundleExtra("editContextExtra").getSerializable("editContact");

        if (c != null) {
            Log.d("TEST", c.getName());

            etEditContactName.setText(c.getName());
            etEditContactEmail.setText(c.getEmail());
        }
    }
}