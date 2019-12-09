package com.food2go;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

public class Profile extends AppCompatActivity implements View.OnClickListener
{
    //Image Picker
    private String cameraFilePath;
    private static final byte GALLERY_REQUEST_CODE = 100;
    private static final byte CAMERA_REQUEST_CODE = 101;


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
    //Database
    DatabaseReference db = FirebaseDatabase.getInstance().getReference();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //Text Fields
        txtFirstName = findViewById(R.id.editFirstName);
        txtLastName = findViewById(R.id.editLastName);
        txtEmail = findViewById(R.id.editEmail);
        txtPhone = findViewById(R.id.editPhone);
        txtPassword = findViewById(R.id.updatePassword);

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

    private void SaveUser()
    {
        db.child("Users").child("profile").child("email").setValue(txtEmail.getText().toString());
        db.child("Users").child("profile").child("first name").setValue(txtFirstName.getText().toString());
        db.child("Users").child("profile").child("last name").setValue(txtLastName.getText().toString());
        db.child("Users").child("profile").child("phone").setValue(Integer.parseInt(txtPhone.getText().toString()));
        db.child("Users").child("profile").child("password").setValue(txtPassword.getText().toString());
    }
    //Gallery
    private void PickFromGallery()
    {
        //Create an Intent with action as ACTION_PICK
        Intent gallery_intent=new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        gallery_intent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        gallery_intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        // Launching the Intent
        startActivityForResult(gallery_intent,GALLERY_REQUEST_CODE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // Result code is RESULT_OK only if the user selects an Image
        if (resultCode == Activity.RESULT_OK)
        {
            if (requestCode == GALLERY_REQUEST_CODE)
            {
                //data.getData returns the content URI for the selected Image
                Uri selectedImage = data.getData();
                btnAvatar.setImageURI(selectedImage);
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                // Get the cursor
                Cursor cursor = getContentResolver().query(Objects.requireNonNull(selectedImage), filePathColumn, null, null, null);
                // Move to first row
                Objects.requireNonNull(cursor).moveToFirst();
                //Get the column index of MediaStore.Images.Media.DATA
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                //Gets the String value in the column
                String imgDecodableString = cursor.getString(columnIndex);

                cursor.close();
                // Set the Image in ImageView after decoding the String
                btnAvatar.setImageBitmap(BitmapFactory.decodeFile(imgDecodableString));
            }
            else if (requestCode == CAMERA_REQUEST_CODE)
            {
                btnAvatar.setImageURI(Uri.parse(cameraFilePath));
            }
        }
    }
    //Camera
    private void CaptureFromCamera() {
        try
        {
            Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", createImageFile()));
            startActivityForResult(camera_intent, CAMERA_REQUEST_CODE);
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    private File createImageFile() throws IOException
    {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        //This is the directory in which the file will be created. This is the default location of Camera photos
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for using again
        cameraFilePath = "file://" + image.getAbsolutePath();
        return image;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.avatar:
            {
                PickFromGallery();
            }
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
                if(validatedForm() != false) {
                    txtFirstName.setEnabled(false);
                    txtLastName.setEnabled(false);
                    txtEmail.setEnabled(false);
                    txtPhone.setEnabled(false);
                    txtPassword.setEnabled(false);
                    SaveUser();
                    Toast.makeText(Profile.this, "Saved!", Toast.LENGTH_SHORT).show();

                }
            }
        }
    }
    //validation for the profile form.
    public boolean validatedForm() {

        boolean isValid = true;

        //email regex pattern
        String email_regex = "^[A-Za-z0-9+_.-]+@[a-z]{2,}+.[a-z]{2,}$";
        Pattern pattern;
        Matcher match;

        //phone number Regex Pattern
        //1-519-111-1111
        String phone_regex = "(\\d-)?\\d{3}-\\d{3}-\\d{4}";

        //password Regex Pattern
        String password_regex = "((?=.*[a-z])(?=.*d)(?=.*[@#!$%])(?=.*[A-Z]).{6,35})";

        //check if any fields are empt
        if (txtFirstName.getText().toString().isEmpty()) {
            //please enter a first name
            txtFirstName.setError("First Name is required");
            isValid = false;
        } else {
            //make sure the name is longer than minuim length.
            if (txtFirstName.getText().toString().trim().length() < 3) {
                txtFirstName.setError("First Name must be longer than 3 characters");
                isValid = false;
            }
        }
        if (txtLastName.getText().toString().isEmpty()) {
            //please enter a first name
            txtLastName.setError("Last Name is required");
            isValid = false;
        } else {
            //make sure the name length is longer than minimum length.
            if (txtLastName.getText().toString().trim().length() < 2) {
                txtLastName.setError("Last name must be longer than 2 characters");
                isValid = false;
            }
        }
        if (txtEmail.getText().toString().isEmpty()) {
            //please enter an email
            txtEmail.setError("Email is required");
            isValid = false;

        } else {
            pattern = Pattern.compile(email_regex);
            match = pattern.matcher(txtEmail.getText().toString());
            if (!match.matches()) {
                txtEmail.setError("Email is invalid");
                isValid = false;
            }
        }
        if (txtPhone.getText().toString().isEmpty()) {
            //please enter an email
            txtPhone.setError("Phone number is required");
            isValid = false;

        } else {
            pattern = Pattern.compile(phone_regex);
            match = pattern.matcher(txtPhone.getText().toString());
            if (!match.matches()) {
                txtPhone.setError("Phone needs to be in XXX-XXX-XXXX format");
                isValid = false;
            }
        }
        if (txtPassword.getText().toString().isEmpty()) {
            txtPassword.setError("Password is required");
            isValid = false;

        } else {
            pattern = Pattern.compile(password_regex);
            match = pattern.matcher(txtPassword.getText().toString());
            if (!match.matches()) {
                txtPassword.setError("Password must contain at least one lowercase" +
                        " letter, digit, special char, capital, and be between 6 to 16 letters ");
                isValid = false;
            }
        }
        return isValid;
    }
}

