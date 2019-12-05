package com.food2go;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.food2go.Model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
        if (v.getId() == R.id.btnSubmitSignIn)
        {
            // set message for sign in process
            final ProgressDialog dialog = new ProgressDialog(SignIn.this);
            dialog.setMessage("Sign in...");
            dialog.show();

            users.addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    // if user exist
                    if (dataSnapshot.child(phoneNumber.getText().toString()).exists())
                    {
                        dialog.dismiss();
                        // Get User Information
                        Users user = dataSnapshot.child(phoneNumber.getText().toString()).getValue(Users.class);
                        if (user.getPassword().equals(password.getText().toString()))
                        {
                            Toast.makeText(SignIn.this, "Sign in successful!", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(SignIn.this, "Incorrect username/phone or password", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        dialog.dismiss();
                        Toast.makeText(SignIn.this, "User not exist", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
