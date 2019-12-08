package com.food2go;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.food2go.Common.Common;
import com.food2go.Model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

/**
 * Created by Hy Minh Tran (Mark) on 12/03/2019
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnSignIn, btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Paper.init(this);

        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignUp = findViewById(R.id.btnSignUp);

        btnSignIn.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);

        //Save user information if checkbox is checked
        String user = Paper.book().read("User");
        String password = Paper.book().read("Password");
        if (user != null && password != null) {
            if(!user.isEmpty() && !password.isEmpty()) {
                doLogin(user, password);
            }
        }
    }

    public void doLogin(final String username, final String password) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference users = db.getReference("Users");

        final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
        dialog.setMessage("Sign in...");
        dialog.show();

        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // if user exist
                if (dataSnapshot.child(username).exists()) {
                    Users user = dataSnapshot.child(username).getValue(Users.class);
                    user.setPhoneNumber(username); //set the phone of user
                    if (user.getPassword().equals(password)) {
                        Toast.makeText(MainActivity.this, "Sign in successful!", Toast.LENGTH_SHORT).show();
                        Intent homeIntent = new Intent(MainActivity.this, Home.class);
                        Common.currentUser = user;
                        startActivity(homeIntent);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "Incorrect email or password", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    dialog.dismiss();
                    Toast.makeText(MainActivity.this, "User not exist", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSignIn:
                Intent signIn = new Intent(this, SignIn.class);
                this.startActivity(signIn);
                break;
            case R.id.btnSignUp:
                Intent signUp = new Intent(this, SignUp.class);
                this.startActivity(signUp);
                break;
        }
    }
}
