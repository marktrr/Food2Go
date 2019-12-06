package com.food2go;

import androidx.appcompat.app.AppCompatActivity;

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

public class Address extends AppCompatActivity implements Spinner.OnItemSelectedListener {
    EditText address, postalCode;
    Spinner province;
    Button btnPlaceOrder;
    final String[] provinceList = {"ON", "BC", "QC", "AB", "NS", "NB", "MB", "PE", "SK", "NL", "NT", "YT", "NU"};
    String provinceStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        address = (EditText) findViewById(R.id.editAddress);
        postalCode = (EditText) findViewById(R.id.editPostalCode);
        province = (Spinner) findViewById(R.id.spinnerProvince);
        btnPlaceOrder = (Button) findViewById(R.id.btnPlaceOrder);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, provinceList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        province.setAdapter(adapter);

        province.setOnItemSelectedListener(this);
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
}
