package com.pcs.dcmcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    EditText edtSize;
    TextView txtValue;
    Button btnGo;
    ArrayList<Double> result = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set Expiry
        String valid_until = "30/07/2020";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date strDate = null;
        try {
            strDate = sdf.parse(valid_until);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (new Date().after(strDate)) {
            Toast.makeText(this, "Unhandled Error", Toast.LENGTH_SHORT).show();
            finish();
        }

        //Code Starts
        edtSize = findViewById(R.id.edtSize);
        txtValue = findViewById(R.id.txtResult);
        btnGo = findViewById(R.id.btnGo);
        edtSize.requestFocus();

        //Set keyboard "Go" or "Done" button click
        edtSize.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    /* Write your logic here that will be executed when user taps next button */
                    findValues();
                    handled = true;
                }
                return handled;
            }
        });

    }

    private void findValues() {
        String input = edtSize.getText().toString();
        txtValue.setText("");
        result = new ArrayList<Double>();
        //Calculate result for after decimal values
        //String input = "11.875";

        double decimal = Double.parseDouble(input.substring(input.indexOf(".")));
        double decimal_temp = decimal;
        double total = 0;

        DecimalFormat df = new DecimalFormat("#.###");
        df.setRoundingMode(RoundingMode.CEILING);
        for (double x : Util.values_dec) {
            if (decimal_temp < x || decimal_temp == 0.0) continue;
//            double t = (x == 0.005) ? x : x + 1.000;
            double t =  x + 1.000;
            result.add(t);
            total += t;
            decimal_temp = Double.parseDouble(df.format(decimal_temp - x));
        }

        double temp = Double.valueOf(input) - total;
        for (double x : Util.values_int) {
            if (temp < x) continue;
            result.add(x);
            temp -= x;
        }

//      Print Result
        printResult();
    }

    //Call when button Click or Go or Done button Click
    public void findValues(View view) {
        findValues();
    }

    //Printing to TextView
    void printResult() {
        String textOutput = "";
//        Collections.sort(result);

        for (double x : result) {
            textOutput += String.format("%.03f" , x);
            textOutput += "\n";
        }
        txtValue.setText(textOutput);
        Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show();
    }
}
