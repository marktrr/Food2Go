package com.food2go;
/**
 * Created by Hy Minh Tran (Mark) on 12/03/2019
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.food2go.Common.Common;
import com.food2go.Model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class SignIn extends AppCompatActivity implements View.OnClickListener {
    EditText phoneNumber, password;
    Button btnSignIn;
    DatabaseReference users;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        phoneNumber = findViewById(R.id.editPhone);
        password = findViewById(R.id.editPassword);
        btnSignIn = findViewById(R.id.btnSubmitSignIn);

        btnSignIn.setOnClickListener(this);

        // declare firebase database
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        users = db.getReference("Users");
    }

    @Override
    public void onClick(View v)
    {
        if(validate() != false) {
            if (v.getId() == R.id.btnSubmitSignIn) {
                // set message for sign in process
                final ProgressDialog dialog = new ProgressDialog(SignIn.this);
                dialog.setMessage("Sign in...");
                dialog.show();

                users.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // if user exist
                        if (dataSnapshot.child(phoneNumber.getText().toString()).exists()) {
                            dialog.dismiss();
                            // Get User Information

                            Users user = dataSnapshot.child(phoneNumber.getText().toString()).getValue(Users.class);
                            user.setPhoneNumber(phoneNumber.getText().toString()); //set the phone of user
                            if (user.getPassword().equals(password.getText().toString())) {
                                Toast.makeText(SignIn.this, "Sign in successful!", Toast.LENGTH_SHORT).show();
                                Intent homeIntent = new Intent(SignIn.this, Home.class);
                                Common.currentUser = user;
                                startActivity(homeIntent);
                                finish();
                            } else {
                                Toast.makeText(SignIn.this, "Incorrect email or password", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            dialog.dismiss();
                            Toast.makeText(SignIn.this, "User not exist", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        }

    }

    //Check if fields are empty
    public boolean validate(){
     boolean isValid = true;

        if (phoneNumber.getText().toString().isEmpty()) {
            //please enter a first name
            phoneNumber.setError("Phone number is required");
            isValid = false;
        }
        if (password.getText().toString().isEmpty()) {
            //please enter a first name
            password.setError("Password is required");
            isValid = false;
        }
     return  isValid;
    }
}
