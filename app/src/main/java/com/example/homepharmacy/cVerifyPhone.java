package com.example.homepharmacy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class cVerifyPhone extends AppCompatActivity {
    Button verify;
    EditText enterdPhone;
    ProgressBar progressBar;
    String verifycodeSystem;
    FirebaseDatabase rootNode;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);

        enterdPhone = findViewById(R.id.verification_code_entered_by_user);
        verify = findViewById(R.id.verify_btn);
        progressBar = findViewById(R.id.progress_bar);

        progressBar.setVisibility(View.GONE);

        final String phoneNu = getIntent().getStringExtra("phoneNo");
        String as = getIntent().getStringExtra("type");
        String name = getIntent().getStringExtra("name");
        String password = getIntent().getStringExtra("password");

        sendVerifyCode(phoneNu);
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code2 = enterdPhone.getText().toString();
                if(code2.isEmpty() || code2.length()<6){
                    Toast.makeText(cVerifyPhone.this, "Wrong OTP", Toast.LENGTH_SHORT).show();
                    enterdPhone.requestFocus();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                verifyCode(code2);
            }
        });
    }

    private void sendVerifyCode(String phoneNu) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+94" + phoneNu,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                TaskExecutors.MAIN_THREAD,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            verifycodeSystem = s;

        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                progressBar.setVisibility(View.VISIBLE);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(cVerifyPhone.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    private void verifyCode(String code) {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verifycodeSystem, code);
        signInCredential(credential);
    }

    private void signInCredential(PhoneAuthCredential credential) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(cVerifyPhone.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        rootNode = FirebaseDatabase.getInstance();
                        reference = rootNode.getReference("login");
                        String phone = getIntent().getStringExtra("phoneNo");
                        String as = getIntent().getStringExtra("type");
                        String name = getIntent().getStringExtra("name");
                        String password = getIntent().getStringExtra("password");

                        if (task.isSuccessful()) {
                            cUserHelperClass user = new cUserHelperClass(as, name, password, phone);
                            reference.child(phone).setValue(user);
                            Toast.makeText(getApplicationContext(), "Registered Successfully...", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(),cHome.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            Toast.makeText(cVerifyPhone.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}