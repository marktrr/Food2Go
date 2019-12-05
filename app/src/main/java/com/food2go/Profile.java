package com.food2go;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Profile extends AppCompatActivity implements View.OnClickListener {

    private ImageButton btnAvatar;
    private Button btnEditProfile;
    private EditText txtFirstName;
    private EditText txtLastName;
    private EditText txtEmail;
    private EditText txtPhone;
    private EditText txtPassword;

    //Labels
    private TextView lblFirst;
    private TextView lblLast;
    private TextView lblEmail;
    private TextView lblPassword;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        btnAvatar = findViewById(R.id.btnAvatar);
        btnEditProfile = findViewById(R.id.editProfile);
        lblFirst = findViewById(R.id.first_name_label);
        txtFirstName = findViewById(R.id.editFirstName);
        lblEmail = findViewById(R.id.email_label);
        txtEmail = findViewById(R.id.editEmail);
        lblPassword = findViewById(R.id.password_label);
        txtPassword = findViewById(R.id.editPassword);
    }
    @Override
    public void onClick(View v) {

    }
}

