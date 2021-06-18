package com.example.hackathon_0.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.example.hackathon_0.CountryData;
import com.example.hackathon_0.R;

public class LoginPage extends AppCompatActivity {

    ImageButton vForwardArrowImageButtonLoginActivity;
    EditText vEditTextPhone;
    String vUserPhoneNumber;
    private Spinner vSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        getSupportActionBar().hide();

        vForwardArrowImageButtonLoginActivity = findViewById(R.id.forwardArrowImageButtonLoginActivity);
        vEditTextPhone = findViewById(R.id.editTextPhone);
        vSpinner = findViewById(R.id.spinnerCountries);

        vSpinner.setAdapter(new ArrayAdapter<String>(LoginPage.this, android.R.layout.simple_spinner_dropdown_item, CountryData.countryNames));
//        String code = CountryData.countryAreaCodes[spinner.getSelectedItemPosition()];
        vForwardArrowImageButtonLoginActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code = CountryData.countryAreaCodes[vSpinner.getSelectedItemPosition()];

                vUserPhoneNumber = vEditTextPhone.getText().toString().trim();

                if (vUserPhoneNumber.isEmpty() || vUserPhoneNumber.length() < 10) {
                    vEditTextPhone.setError("Valid Phone number is required");
                    vEditTextPhone.requestFocus();
                    return;
                }
                String phoneNumber = "+" + code + vUserPhoneNumber;


                Intent vLoginOTPPageIntentLoginPageActivity = new Intent(LoginPage.this, LoginOTPPage.class);
                vLoginOTPPageIntentLoginPageActivity.putExtra("phonenumber",phoneNumber);
                startActivity(vLoginOTPPageIntentLoginPageActivity);

            }
        });

    }
}