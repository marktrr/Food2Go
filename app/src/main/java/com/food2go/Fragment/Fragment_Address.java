package com.food2go.Fragment;

/**
 * Created by Hy Minh Tran (Mark) on 12/08/2019
 */
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.room.Database;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.view.View;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.food2go.Common.Common;
import com.food2go.DB.OrderDB;
import com.food2go.Home;
import com.food2go.Model.Order;
import com.food2go.Model.Reports;
import com.food2go.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Address extends Fragment implements Spinner.OnItemSelectedListener, View.OnClickListener {
    EditText address, postalCode;
    Spinner province;
    Button btnPlaceOrder;
    RadioButton radioCash;
    final String[] provinceList = {"ON", "BC", "QC", "AB", "NS", "NB", "MB", "PE", "SK", "NL", "NT", "YT", "NU"};
    String provinceStr;
    String totalPrice;
    FirebaseDatabase db;
    DatabaseReference report;
    List<Order> cart = new ArrayList<>();

    public Fragment_Address() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_address, container, false);
        address = (EditText) view.findViewById(R.id.editAddress);
        postalCode = (EditText) view.findViewById(R.id.editPostalCode);
        province = (Spinner) view.findViewById(R.id.spinnerProvince);
        btnPlaceOrder = (Button) view.findViewById(R.id.btnPlaceOrder);
        radioCash = view.findViewById(R.id.radioCash);

        // Firebase
        db = FirebaseDatabase.getInstance();
        report = db.getReference("Reports");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (getContext(), android.R.layout.simple_spinner_item, provinceList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        province.setAdapter(adapter);

        province.setOnItemSelectedListener(this);
        btnPlaceOrder.setOnClickListener(this);

        if (getActivity().getIntent() != null) {
            totalPrice = getActivity().getIntent().getStringExtra("totalPrice");
            Bundle args = getActivity().getIntent().getBundleExtra("listFoods");
            cart = (List<Order>) args.getSerializable("arrayList");
        }

        return(view);
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
        switch (v.getId()) {
            case R.id.btnPlaceOrder:
                if(radioCash.isChecked()) {
                    showDialog();
                }
                else {
                    Toast.makeText(getContext(), "Please select payment method", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void showDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
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
                new OrderDB(getContext()).clearCart();
                Toast.makeText(getContext(), "Order place successfully.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getContext(), Home.class));
                getActivity().finish();
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
}
