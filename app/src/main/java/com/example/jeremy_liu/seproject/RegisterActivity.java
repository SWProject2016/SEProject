package com.example.jeremy_liu.seproject;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Jeremy_Liu on 2016/4/18.
 */
public class RegisterActivity extends Activity {
    private EditText mUserText;
    private EditText mPasswordText;
    private EditText mConPasswordText;
    private UserDbAdapter mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDbHelper = new UserDbAdapter(this);
        mDbHelper.open();
        setContentView(R.layout.register);

        mUserText = (EditText) findViewById(R.id.userNameAuto);
        mPasswordText = (EditText) findViewById(R.id.password);
        mPasswordText.setInputType(InputType.TYPE_CLASS_TEXT
                | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        mConPasswordText = (EditText) findViewById(R.id.conpasswordET);
        mConPasswordText.setInputType(InputType.TYPE_CLASS_TEXT
                | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        Button confirmButton = (Button) findViewById(R.id.regBT);
        Button cancelButton = (Button) findViewById(R.id.canBT);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String username = mUserText.getText().toString();
                String password = mPasswordText.getText().toString();
                String conPassword = mConPasswordText.getText().toString();
                if ((username == null || username.equalsIgnoreCase("")) || (password == null || password.equalsIgnoreCase("")) || (conPassword == null || conPassword.equalsIgnoreCase(""))) {
                    Toast.makeText(RegisterActivity.this, "The user name and password are necessary.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Cursor cursor = mDbHelper.getDiary(username);
                    if (cursor.moveToFirst()) {
                        Toast.makeText(RegisterActivity.this, "The user name already exists.",
                                Toast.LENGTH_SHORT).show();
                    } else if (!password.equals(conPassword)) {
                        Toast.makeText(RegisterActivity.this, "Two password is not consistent, please enter again.",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        mDbHelper.createUser(username, password);
                        Toast.makeText(RegisterActivity.this, "Register successfully. Wait for going to the login page...",
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.setClass(RegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }
            }

        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
            }

        });

    }
}
