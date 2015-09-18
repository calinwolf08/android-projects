package com.example.calin.contentresolver;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {

    static final Uri CONTENT_URL = Uri.parse(
            "content://com.example.calin.contentprovider.ContactProvider/cpcontacts"
    );

    TextView contactsTextView = null;
    EditText deleteIDEditText, idLookupEditText, addNameEditText;

    ContentResolver resolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resolver = getContentResolver();

        contactsTextView = (TextView) findViewById(R.id.contactsTextView);
        deleteIDEditText = (EditText) findViewById(R.id.deleteIDEditText);
        idLookupEditText = (EditText) findViewById(R.id.idLookupEditText);
        addNameEditText = (EditText) findViewById(R.id.addNameEditText);

        getContacts();
    }

    public void getContacts(){

        String[] projection = new String[]{"id", "name"};

        Cursor cursor = resolver.query(CONTENT_URL, projection, null, null, null);

        String contactList = "";

        if(cursor.moveToFirst()) {

            do {

                String id = cursor.getString(cursor.getColumnIndex("id"));

                String name = cursor.getString(cursor.getColumnIndex("name"));

                contactList = contactList + id + " : " + name + "\n";

            }while(cursor.moveToNext());

        }

        contactsTextView.setText(contactList);

    }

    public void deleteContact(View view) {

        String idToDelete = deleteIDEditText.getText().toString();

        long idDeleted = resolver.delete(CONTENT_URL, "id = ? ", new String[]{idToDelete});

        getContacts();
    }

    public void lookupContact(View view) {

        String idToFind = idLookupEditText.getText().toString();

        String[] projection = {"id", "name"};

        Cursor cursor = resolver.query(CONTENT_URL, projection,
                "id = ?", new String[]{idToFind}, null);

        String contact = "";

        if(cursor.moveToFirst()) {

            do {

                String id = cursor.getString(cursor.getColumnIndex("id"));

                String name = cursor.getString(cursor.getColumnIndex("name"));

                contact = contact + id + " : " + name + "\n";

            }while(cursor.moveToNext());

        } else
            Toast.makeText(this, "Contact Not Found", Toast.LENGTH_SHORT).show();

        contactsTextView.setText(contact);

    }

    public void addContact(View view) {

        String nameToAdd = addNameEditText.getText().toString();

        ContentValues values = new ContentValues();
        values.put("name", nameToAdd);

        resolver.insert(CONTENT_URL, values);

        getContacts();

    }

    public void showContacts(View view) {

        getContacts();

    }
}
