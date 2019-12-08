package com.food2go.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.food2go.Model.Users;
import com.food2go.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Hy Minh Tran (Mark) on 12/08/2019
 */
public class Fragment_SignUp extends Fragment implements View.OnClickListener {
    EditText phoneNumber, ID, password;
    Button SignUp;
    DatabaseReference users;

    public Fragment_SignUp() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        phoneNumber = view.findViewById(R.id.editNewPhone);
        ID = view.findViewById(R.id.editNewID);
        password = view.findViewById(R.id.editNewPassword);
        SignUp = view.findViewById(R.id.btnSubmitSignUp);

        SignUp.setOnClickListener(this);

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        users = db.getReference("Users");

        return(view);
    }

    @Override
    public void onClick(final View v) {
        if(validatedForm() != false) {
            if (v.getId() == R.id.btnSubmitSignUp) {
                // set message for sign in process
                final ProgressDialog dialog = new ProgressDialog(v.getContext());
                dialog.setMessage("Please wait...");
                dialog.show();

                users.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // if ID or user already exist
                        if (dataSnapshot.child(phoneNumber.getText().toString()).exists()) {
                            dialog.dismiss();
                            Toast.makeText(v.getContext(), "This phone number is already in use!", Toast.LENGTH_SHORT).show();
                        } else {
                            dialog.dismiss();
                            Users newUser = new Users(ID.getText().toString(), password.getText().toString(), phoneNumber.getText().toString());
                            users.child(phoneNumber.getText().toString()).setValue(newUser);
                            Toast.makeText(v.getContext(), "Sign up successful!", Toast.LENGTH_SHORT).show();
                            getActivity().finish();
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
