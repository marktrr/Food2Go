package com.food2go;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Database;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.view.View;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.food2go.Common.Common;
import com.food2go.DB.OrderDB;
import com.food2go.Model.Order;
import com.food2go.Model.Reports;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Address extends AppCompatActivity implements Spinner.OnItemSelectedListener, View.OnClickListener {
    EditText address, postalCode;
    Spinner province;
    Button btnPlaceOrder;
    final String[] provinceList = {"ON", "BC", "QC", "AB", "NS", "NB", "MB", "PE", "SK", "NL", "NT", "YT", "NU"};
    String provinceStr;
    String totalPrice;
    FirebaseDatabase db;
    DatabaseReference report;
    List<Order> cart = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        address = (EditText) findViewById(R.id.editAddress);
        postalCode = (EditText) findViewById(R.id.editPostalCode);
        province = (Spinner) findViewById(R.id.spinnerProvince);
        btnPlaceOrder = (Button) findViewById(R.id.btnPlaceOrder);

        // Firebase
        db = FirebaseDatabase.getInstance();
        report = db.getReference("Reports");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, provinceList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        province.setAdapter(adapter);

        province.setOnItemSelectedListener(this);
        btnPlaceOrder.setOnClickListener(this);

        if (getIntent() != null) {
            totalPrice = getIntent().getStringExtra("totalPrice");
            Bundle args = getIntent().getBundleExtra("listFoods");
            cart = (List<Order>) args.getSerializable("arrayList");
        }
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        ((TextView) v).setTextColor(Color.WHITE);
        if(parent.getId() == R.id.spinnerProvince){
            provinceStr = String.valueOf(parent.getItemAtPosition(position));
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing
    }

    @Override
    public void onClick(View v) {
        if(validation() != false) {
            switch (v.getId()) {
                case R.id.btnPlaceOrder:
                    showDialog();
                    break;
            }
        }
    }

    public void showDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(Address.this);
        dialog.setTitle("Place Order");
        dialog.setMessage("Are you sure to place order?");

        dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // add order to firebase
                String fullAddress = address.getText().toString() + ", " + provinceStr + ", CA, " + postalCode.getText().toString();
                Reports newOrder = new Reports(
                        Common.currentUser.getPhoneNumber(),
                        Common.currentUser.getId(),
                        fullAddress,
                        totalPrice,
                        "0",
                        cart);

                //add to firebase
                report.child(String.valueOf(System.currentTimeMillis())).setValue(newOrder);
                //clean cart
                new OrderDB(getBaseContext()).clearCart();
                Toast.makeText(Address.this, "Order place successfully.", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(getIntent());
            }
        });

        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialog.show();
    }
    public boolean validation(){
        boolean isValid = true;

        Pattern pattern;
        Matcher match;
        //phone number Regex Pattern
        //1-519-111-1111
        String postalCode_regex = "^(?!.*[DFIOQU])[A-VXY][0-9][A-Z]●?[0-9][A-Z][0-9]$";

        if (address.getText().toString().isEmpty()) {
            //please enter an email
            address.setError("Address is required");
            isValid = false;
        }
        if(postalCode.getText().toString().isEmpty())
        {
            postalCode.setError("Postal Code is required");
            isValid = false;
        }
        else {
            pattern = Pattern.compile(postalCode_regex);
            match = pattern.matcher(postalCode.getText().toString());
            if (!match.matches()) {
                postalCode.setError("Must be a valid Canadian Postal Code");
                isValid = false;
            }
        }

        return  isValid;
    }
}
