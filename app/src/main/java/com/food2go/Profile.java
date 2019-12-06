package com.food2go;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Profile extends AppCompatActivity implements View.OnClickListener {

    //Buttons
    ImageButton btnAvatar;
    Button btnEditProfile;
    Button btnSave;

    //Text Fields
    EditText txtFirstName;
    EditText txtLastName;
    EditText txtEmail;
    EditText txtPhone;
    EditText txtPassword;

    //Labels
    TextView lblFirst;
    TextView lblLast;
    TextView lblEmail;
    TextView lblPassword;
    TextView lblPhone;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Labels
        lblFirst = findViewById(R.id.first_name_label);
        lblLast = findViewById(R.id.last_name_label);
        lblEmail = findViewById(R.id.email_label);
        lblPhone = findViewById(R.id.phone_label);
        lblPassword = findViewById(R.id.password_label);

        //Text Fields
        txtFirstName = findViewById(R.id.editFirstName);
        txtLastName = findViewById(R.id.editLastName);
        txtEmail = findViewById(R.id.editEmail);
        txtPhone = findViewById(R.id.editPhone);
        txtPassword = findViewById(R.id.editPassword);

        txtFirstName.setEnabled(false);
        txtLastName.setEnabled(false);
        txtEmail.setEnabled(false);
        txtPhone.setEnabled(false);
        txtPassword.setEnabled(false);

        //Buttons
        btnAvatar = findViewById(R.id.avatar);
        btnAvatar.setOnClickListener(this);
        btnEditProfile = findViewById(R.id.editProfile);
        btnEditProfile.setOnClickListener(this);
        btnSave = findViewById(R.id.save);
        btnSave.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.editProfile:
            {
                txtFirstName.setEnabled(true);
                txtLastName.setEnabled(true);
                txtEmail.setEnabled(true);
                txtPhone.setEnabled(true);
                txtPassword.setEnabled(true);
            }
            case R.id.save:
            {
                txtFirstName.setEnabled(false);
                txtLastName.setEnabled(false);
                txtEmail.setEnabled(false);
                txtPhone.setEnabled(false);
                txtPassword.setEnabled(false);
            }
        }
    }
}

