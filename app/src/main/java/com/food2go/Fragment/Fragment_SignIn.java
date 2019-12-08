package com.food2go.Fragment;

/**
 * Created by Hy Minh Tran (Mark) on 12/08/2019
 */
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.food2go.Common.Common;
import com.food2go.Home;
import com.food2go.Model.Users;
import com.food2go.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import io.paperdb.Paper;

public class Fragment_SignIn extends Fragment implements View.OnClickListener{
    EditText phoneNumber, password;
    Button btnSignIn;
    DatabaseReference users;
    CheckBox remember;

    public Fragment_SignIn() {

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signin, container, false);
        phoneNumber = view.findViewById(R.id.editPhone);
        password = view.findViewById(R.id.editPassword);
        btnSignIn = view.findViewById(R.id.btnSubmitSignIn);
        remember = view.findViewById(R.id.checkboxRemember);

        btnSignIn.setOnClickListener(this);
        Paper.init(this.getContext());

        // declare firebase database
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        users = db.getReference("Users");

        return(view);
    }

    @Override
    public void onClick(final View v)
    {
        if (v.getId() == R.id.btnSubmitSignIn)
        {
            if(remember.isChecked()) {
                Paper.book().write("User", phoneNumber.getText().toString());
                Paper.book().write("Password", password.getText().toString());
            }
            // set message for sign in process
            final ProgressDialog dialog = new ProgressDialog(this.getContext());
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
                            Toast.makeText(v.getContext(), "Sign in successful!", Toast.LENGTH_SHORT).show();
                            Intent homeIntent = new Intent(v.getContext(), Home.class);
                            Common.currentUser = user;
                            startActivity(homeIntent);
                            getActivity().finish();
                        }
                        else
                        {
                            Toast.makeText(v.getContext(), "Incorrect email or password", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        dialog.dismiss();
                        Toast.makeText(v.getContext(), "User not exist", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    //
                }
            });
        }
    }
}
