package braingame.amax.mybase.Controllers;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import braingame.amax.mybase.R;

import static braingame.amax.mybase.Models.DatabaseQuery.USERNAME;
import static braingame.amax.mybase.Models.HelpFunction.checkIsEmpty;

public class ActivityVerify extends AppCompatActivity {
    SharedPreferences mSettings;
    private String verificationId;
    private EditText mInputCode1, mInputCode2, mInputCode3, mInputCode4, mInputCode5, mInputCode6;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        Bundle arguments = getIntent().getExtras();
        assert arguments != null;
        String name = String.valueOf(arguments.get("user"));
        System.out.println("Активность activity_verify получила значение из активности activity_registration: " + name);



        mSettings = getSharedPreferences(USERNAME, MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = mSettings.edit();

        prefEditor.putString(USERNAME, name);
        prefEditor.apply();

        TextView mTextMobile = findViewById(R.id.text_mobile);

        mTextMobile.setText(String.format("+7%s", getIntent().getStringExtra("mobile")));

        mInputCode1 = findViewById(R.id.input_code_1);
        mInputCode2 = findViewById(R.id.input_code_2);
        mInputCode3 = findViewById(R.id.input_code_3);
        mInputCode4 = findViewById(R.id.input_code_4);
        mInputCode5 = findViewById(R.id.input_code_5);
        mInputCode6 = findViewById(R.id.input_code_6);
        TextView mBtnGoBack = findViewById(R.id.btn_go_back);

        setupCodeInput();

        checkIsEmpty(mInputCode1,mInputCode2,mInputCode3,mInputCode4,mInputCode5,mInputCode6);


        final ProgressBar mProgressBar = findViewById(R.id.progress_bar);
        final Button mVerifyBtn = findViewById(R.id.verify_btn);

        verificationId = getIntent().getStringExtra("verificationId");

        try {
            mVerifyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //---Проверка на пустые поля---
                    if (checkIsEmpty(mInputCode1, mInputCode2, mInputCode3, mInputCode4, mInputCode5, mInputCode6)) {
                        Toast.makeText(ActivityVerify.this, "Введите проверочный код", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //---Склейка всех полей в одну строку---
                    String code = mInputCode1.getText().toString() +
                            mInputCode2.getText().toString() +
                            mInputCode3.getText().toString() +
                            mInputCode4.getText().toString() +
                            mInputCode5.getText().toString() +
                            mInputCode6.getText().toString();

                    if (verificationId != null) {
                        mProgressBar.setVisibility(View.VISIBLE);
                        mVerifyBtn.setVisibility(View.INVISIBLE);
                        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verificationId, code);
                        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                                .addOnCompleteListener(task -> {
                                    mProgressBar.setVisibility(View.GONE);
                                    mVerifyBtn.setVisibility(View.VISIBLE);
                                    if (task.isSuccessful()) {
                                        prefEditor.putString(USERNAME, name);
                                        prefEditor.apply();
                                        Intent intent1 = new Intent(ActivityVerify.this.getApplicationContext(), ActivityVerifyCongrad.class);
                                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        ActivityVerify.this.startActivity(intent1);
                                    } else {
                                        Toast.makeText(ActivityVerify.this, "Введен неверный проверочный код", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        try {
            findViewById(R.id.text_resend).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            "+7" + getIntent().getStringExtra("mobile"), 60, TimeUnit.SECONDS, ActivityVerify.this,
                            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                                @Override
                                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                                }

                                @Override
                                public void onVerificationFailed(@NonNull FirebaseException e) {

                                    Toast.makeText(ActivityVerify.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onCodeSent(@NonNull String newVerificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                    verificationId = newVerificationId;
                                    Toast.makeText(ActivityVerify.this, "Код отправлен", Toast.LENGTH_SHORT).show();

                                }
                            }
                    );
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        try {
            mBtnGoBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent goToAuth = new Intent(ActivityVerify.this, ActivityRegistration.class);
                    goToAuth.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    goToAuth.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(goToAuth);
                    finish();
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupCodeInput() {
        mInputCode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    mInputCode2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mInputCode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    mInputCode3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mInputCode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    mInputCode4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mInputCode4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    mInputCode5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mInputCode5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    mInputCode6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

}
