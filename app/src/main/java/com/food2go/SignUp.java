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

        phoneNumber = (EditText) findViewById(R.id.editNewPhone);
        ID = (EditText) findViewById(R.id.editNewID);
        password = (EditText) findViewById(R.id.editNewPassword);
        SignUp = (Button) findViewById(R.id.btnSubmitSignUp);

        SignUp.setOnClickListener(this);

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        users = db.getReference("Users");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSubmitSignUp:
                // set message for sign in process
                final ProgressDialog dialog = new ProgressDialog(SignUp.this);
                dialog.setMessage("Please wait...");
                dialog.show();

                users.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // if ID or user already exist
                        if(dataSnapshot.child(phoneNumber.getText().toString()).exists()) {
                            dialog.dismiss();
                            Toast.makeText(SignUp.this, "This phone already use for register!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            dialog.dismiss();
                            Users newUser = new Users(ID.getText().toString(), password.getText().toString());
                            users.child(phoneNumber.getText().toString()).setValue(newUser);
                            Toast.makeText(SignUp.this, "Sign up successfully!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                break;
        }
    }
}

