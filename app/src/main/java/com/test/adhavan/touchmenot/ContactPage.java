package com.test.adhavan.touchmenot;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class ContactPage extends AppCompatActivity {
    /* -----------FOR MORE THAN 1 CONTACTS----------
    static contact[] contacts = new contact[1];
    public int no_of_contacts = 0;
    */
    private static Button submit_button;
    private static EditText phoneno_field, name_field;
    public static String phoneno, name;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("temp", "onCreate: create worked ");
        setContentView(R.layout.activity_contacts);
        initstuff();
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name_field.getText().toString().isEmpty() || phoneno_field.getText().toString().isEmpty()){
                    Log.e("temp:", "onClick:triggered ");
                    Toast.makeText(ContactPage.this, "ENTER THE DETAILS!", Toast.LENGTH_LONG).show();
                }
                else {
                    phoneno = phoneno_field.getText().toString();
                    name = name_field.getText().toString();
                    boolean contact_saved = makecontact();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("made_contact", contact_saved);
                    startActivity(intent);
                }
            }
        });
    }
    void initstuff(){
        submit_button = findViewById(R.id.submit_button);
        phoneno_field = findViewById(R.id.phoneno_field);
        name_field = findViewById(R.id.name_field);
    }
    boolean makecontact(){
        /*
        contacts[no_of_contacts].setName(name);
        contacts[no_of_contacts].setPhnumber(phoneno);
        */
        SharedPreferences sharedPreferences = getSharedPreferences("contacts_preferences",0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.contact1_name),name);
        editor.putString(getString(R.string.contact1_no),phoneno);
        boolean contact_saved;
        contact_saved = editor.commit();
        return contact_saved;
    }
}
