package com.example.hackathon_0.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hackathon_0.R;
import com.example.hackathon_0.viewmodel.LoginOTPPageViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginOTPPage extends AppCompatActivity {

    private EditText vEditTextOTP1, vEditTextOTP2, vEditTextOTP3, vEditTextOTP4, vEditTextOTP5, vEditTextOTP6;
    private String verificationId;
    private FirebaseAuth vFirebaseAuth;
    private Button vSubmitButtonLoginOTPActivity;

    private LoginOTPPageViewModel viewModel;


    public LoginOTPPage() {
        //no arguments constructor
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_otppage);

        viewModel= new ViewModelProvider(LoginOTPPage.this).get(LoginOTPPageViewModel.class);
        getSupportActionBar().hide();
        vFirebaseAuth = FirebaseAuth.getInstance();
        String vPhoneNumber = getIntent().getStringExtra("phonenumber");
        vSendVerificationCode(vPhoneNumber);

        vEditTextOTP1 = findViewById(R.id.editTextOTP1);
        vEditTextOTP2 = findViewById(R.id.editTextOTP2);
        vEditTextOTP3 = findViewById(R.id.editTextOTP3);
        vEditTextOTP4 = findViewById(R.id.editTextOTP4);
        vEditTextOTP5 = findViewById(R.id.editTextOTP5);
        vEditTextOTP6 = findViewById(R.id.editTextOTP6);

        class GenericTextWatcher implements TextWatcher {
            private View view;

            private GenericTextWatcher(View view) {
                this.view = view;
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // TODO Auto-generated method stub
                String text = editable.toString();
                switch (view.getId()) {

                    case R.id.editTextOTP1:
                        if (text.length() == 1)
                            vEditTextOTP2.requestFocus();
                        break;
                    case R.id.editTextOTP2:
                        if (text.length() == 1)
                            vEditTextOTP3.requestFocus();
                        else if (text.length() == 0)
                            vEditTextOTP1.requestFocus();
                        break;
                    case R.id.editTextOTP3:
                        if (text.length() == 1)
                            vEditTextOTP4.requestFocus();
                        else if (text.length() == 0)
                            vEditTextOTP2.requestFocus();
                        break;
                    case R.id.editTextOTP4:
                        if (text.length() == 1)
                            vEditTextOTP5.requestFocus();
                        else if (text.length() == 0)
                            vEditTextOTP3.requestFocus();
                        break;
                    case R.id.editTextOTP5:
                        if (text.length() == 1)
                            vEditTextOTP6.requestFocus();
                        else if (text.length() == 0)
                            vEditTextOTP4.requestFocus();
                        break;
                    case R.id.editTextOTP6:
                        if (text.length() == 0)
                            vEditTextOTP5.requestFocus();
                        break;

                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
            }
        }

        vEditTextOTP1.addTextChangedListener(new GenericTextWatcher(vEditTextOTP1));
        vEditTextOTP2.addTextChangedListener(new GenericTextWatcher(vEditTextOTP2));
        vEditTextOTP3.addTextChangedListener(new GenericTextWatcher(vEditTextOTP3));
        vEditTextOTP4.addTextChangedListener(new GenericTextWatcher(vEditTextOTP4));
        vEditTextOTP5.addTextChangedListener(new GenericTextWatcher(vEditTextOTP5));
        vEditTextOTP6.addTextChangedListener(new GenericTextWatcher(vEditTextOTP6));

        vSubmitButtonLoginOTPActivity = findViewById(R.id.submitButtonLoginOTPActivity);
        vSubmitButtonLoginOTPActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (vEditTextOTP1.getText().toString().isEmpty() || vEditTextOTP2.getText().toString().isEmpty() || vEditTextOTP3.getText().toString().isEmpty() || vEditTextOTP4.getText().toString().isEmpty() || vEditTextOTP5.getText().toString().isEmpty() || vEditTextOTP6.getText().toString().isEmpty()) {
                    vEditTextOTP6.setError("Enter OTP First");
                    return;
                }
                String code = vEditTextOTP1.getText().toString().trim() + vEditTextOTP2.getText().toString().trim() + vEditTextOTP3.getText().toString().trim() + vEditTextOTP4.getText().toString().trim() + vEditTextOTP5.getText().toString().trim() + vEditTextOTP6.getText().toString().trim();

                if (code.isEmpty() || code.length() < 6) {

                    vEditTextOTP6.setError("Enter code...");
                    vEditTextOTP6.requestFocus();
                    return;
                }
                verifyCode(code);

            }
        });

    }

    private void verifyCode(String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        login(credential);
    }

    public void login(PhoneAuthCredential credential){
        vFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Intent intent = new Intent(LoginOTPPage.this, Home.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                            startActivity(intent);

                        } else {
                            Toast.makeText(LoginOTPPage.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void vSendVerificationCode(String number){

        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(vFirebaseAuth)
                .setActivity(this)
                .setPhoneNumber(number)
                .setTimeout(30L, TimeUnit.SECONDS)
                .setCallbacks(mCallBack)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    public PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                vEditTextOTP1.setText(code.charAt(0));
                vEditTextOTP2.setText(code.charAt(1));
                vEditTextOTP3.setText(code.charAt(2));
                vEditTextOTP4.setText(code.charAt(3));
                vEditTextOTP5.setText(code.charAt(4));
                vEditTextOTP6.setText(code.charAt(5));
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(LoginOTPPage.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

}