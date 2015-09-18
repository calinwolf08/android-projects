package com.example.calin.contact;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;


public class MainActivity extends ActionBarActivity {

    SQLiteDatabase contactsDB = null;

    Button createDBButton, addContactButton, deleteContactButton,
        getContactsButton, deleteDBButton;

    EditText nameEditText, emailEditText, contactListEditText,
        idEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createDBButton = (Button) findViewById(R.id.createDBButton);
        addContactButton = (Button) findViewById(R.id.addContactButton);
        deleteContactButton = (Button) findViewById(R.id.deleteContactButton);
        getContactsButton = (Button) findViewById(R.id.getContactsButton);
        deleteDBButton = (Button) findViewById(R.id.deleteDBButton);

        nameEditText = (EditText) findViewById(R.id.nameEditText);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        contactListEditText = (EditText) findViewById(R.id.contactListEditText);
        idEditText = (EditText) findViewById(R.id.idEditText);

    }

    public void createDatabase(View view) {

        try {

            contactsDB = this.openOrCreateDatabase("MyContacts",
                    MODE_PRIVATE, null); //null handles errors -- can be handler

            contactsDB.execSQL("CREATE TABLE IF NOT EXISTS contacts" +
                    "(id integer primary key, name VARCHAR, email VARCHAR);");

            File database = getApplicationContext().getDatabasePath("MyContacts.db");

            if(!database.exists()) {

                Toast.makeText(this, "database created", Toast.LENGTH_SHORT).show();

            }

        }
        catch (Exception e) {
            Log.e("CONTACTS ERROR", "error creating database");
        }

        addContactButton.setClickable(true);
        deleteContactButton.setClickable(true);
        getContactsButton.setClickable(true);
        deleteDBButton.setClickable(true);

    }

    public void addContact(View view) {

        String contactName = nameEditText.getText().toString();
        String contactEmail = emailEditText.getText().toString();

        contactsDB.execSQL("INSERT INTO contacts (name, email) VALUES (' " +
            contactName + "', '" + contactEmail + "');");

    }

    public void getContacts(View view) {

        Cursor cursor = contactsDB.rawQuery("SELECT * FROM contacts", null);

        int idColumn = cursor.getColumnIndex("id");
        int nameColumn = cursor.getColumnIndex("name");
        int emailColumn = cursor.getColumnIndex("email");

        cursor.moveToFirst();

        String contactList = "";

        if((cursor != null) && (cursor.getCount() > 0)) {

            do {

                String id = cursor.getString(idColumn);
                String name = cursor.getString(nameColumn);
                String email = cursor.getString(emailColumn);

                contactList = contactList + id + " : " + name + " : " + email + "\n";

            }while(cursor.moveToNext());

            contactListEditText.setText(contactList);
        } else {

            Toast.makeText(this, "No Results to Show",
                    Toast.LENGTH_SHORT).show();
            contactListEditText.setText("");

        }

    }

    public void deleteContact(View view) {

        String id = idEditText.getText().toString();

        contactsDB.execSQL("DELETE FROM contacts WHERE id = " + id + ";");

    }

    public void deleteDatabase(View view) {

        contactsDB.close();
        this.deleteDatabase("MyContacts");
        addContactButton.setClickable(false);
        deleteContactButton.setClickable(false);
        getContactsButton.setClickable(false);
        deleteDBButton.setClickable(false);

    }

    @Override
    protected void onDestroy() {

        contactsDB.close();
        //could close on pause as well and open on resume etc

        super.onDestroy();
    }
}
