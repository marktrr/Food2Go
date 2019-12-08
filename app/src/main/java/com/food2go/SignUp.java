package com.food2go;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.food2go.Model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Hy Minh Tran (Mark) on 12/03/2019
 */

public class SignUp extends AppCompatActivity  implements View.OnClickListener {
    EditText phoneNumber, ID, password;
    Button SignUp;
    DatabaseReference users;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        phoneNumber = findViewById(R.id.editNewPhone);
        ID = findViewById(R.id.editNewID);
        password = findViewById(R.id.editNewPassword);
        SignUp = findViewById(R.id.btnSubmitSignUp);

        SignUp.setOnClickListener(this);

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        users = db.getReference("Users");
    }

    @Override
    public void onClick(View v) {
        if(validatedForm() != false) {
            if (v.getId() == R.id.btnSubmitSignUp) {
                // set message for sign in process
                final ProgressDialog dialog = new ProgressDialog(SignUp.this);
                dialog.setMessage("Please wait...");
                dialog.show();

                users.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // if ID or user already exist
                        if (dataSnapshot.child(phoneNumber.getText().toString()).exists()) {
                            dialog.dismiss();
                            Toast.makeText(SignUp.this, "This phone number is already in use!", Toast.LENGTH_SHORT).show();
                        } else {
                            dialog.dismiss();
                            Users newUser = new Users(ID.getText().toString(), password.getText().toString(), phoneNumber.getText().toString());
                            users.child(phoneNumber.getText().toString()).setValue(newUser);
                            Toast.makeText(SignUp.this, "Sign up successful!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        }
    }
    //validation for the profile form.
    public boolean validatedForm() {

        boolean isValid = true;

        Pattern pattern;
        Matcher match;

        //phone number Regex Pattern
        //1-519-111-1111
        String phone_regex = "(\\d-)?\\d{3}-\\d{3}-\\d{4}";
        //password Regex Pattern
        String password_regex = "((?=.*[a-z])(?=.*d)(?=.*[@#!$%])(?=.*[A-Z]).{6,35})";

        if (ID.getText().toString().isEmpty()) {
            //please enter an email
            ID.setError("Name is require");
            isValid = false;
        }
        else{
            if (ID.getText().toString().trim().length() < 2) {
                ID.setError("Name must be 2 characters or longer");
                isValid = false;
            }
        }

        if (phoneNumber.getText().toString().isEmpty()) {
            //please enter an email
            phoneNumber.setError("Phone number is required");
            isValid = false;

        } else {
            pattern = Pattern.compile(phone_regex);
            match = pattern.matcher(phoneNumber.getText().toString());
            if (!match.matches()) {
                phoneNumber.setError("Phone needs to be in XXX-XXX-XXXX format");
                isValid = false;
            }
        }
        if (password.getText().toString().isEmpty()) {
            //please enter a first name
            password.setError("Password is required");
            isValid = false;

        } else {
            pattern = Pattern.compile(password_regex);
            match = pattern.matcher(password.getText().toString());
            if (!match.matches()) {
                password.setError("Password must contain at least one lowercase" +
                        " letter, digit, special char, capital, and be between 6 to 35 letters ");
                isValid = false;
            }
        }
        return isValid;
    }


}

