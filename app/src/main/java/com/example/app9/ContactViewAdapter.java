package com.example.app9;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ContactViewAdapter extends RecyclerView.Adapter<ContactViewAdapter.ContactViewHolder> {
    private ArrayList<Contact> contacts = new ArrayList<>();
    private Context context;

    public ContactViewAdapter(Context context) {
        this.context = context;
    }

    public void setContacts(ArrayList<Contact> contacts) {
        this.contacts = contacts;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_item_detail, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contact c = contacts.get(position);
        holder.tvName.setText(c.getName());
        holder.tvEmail.setText(c.getEmail());

        // Edit button click handler
        holder.btnEditContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Edit " + c.getName(), Toast.LENGTH_SHORT).show();
            }
        });

        // Remove button click handler
        holder.btnRemoveContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (context instanceof MainActivity) {
                    MainActivity activity = (MainActivity) context;
                    SQLiteDatabase db = activity.getDb();

                    // Define 'where' part of query.
                    String selection = ContactContract.ContactEntry._ID + " LIKE ?";
                    // Specify arguments in placeholder order.
                    String[] selectionArgs = { String.valueOf(c.getId()) };
                    // Issue SQL statement.
                    int deletedRows = db.delete(ContactContract.ContactEntry.TABLE_NAME, selection, selectionArgs);

                    Toast.makeText(view.getContext(), "Removed " + c.getName() + ", deletedRows: " + deletedRows, Toast.LENGTH_SHORT).show();
                    activity.loadContacts();
                }
                // Toast.makeText(holder.itemView.getContext(), "Remove " + c.getName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private TextView tvEmail;
        private Button btnRemoveContact;
        private Button btnEditContact;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            btnRemoveContact = itemView.findViewById(R.id.btnDeleteContact);
            btnEditContact = itemView.findViewById(R.id.btnEditContact);
        }
    }
}
