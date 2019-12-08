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
                    }
                    else {
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
