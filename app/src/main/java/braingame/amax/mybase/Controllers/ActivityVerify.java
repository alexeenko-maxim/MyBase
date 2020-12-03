package braingame.amax.mybase.Controllers;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import braingame.amax.mybase.R;

public class ActivityVerify extends AppCompatActivity {

    private String verificationId;
    private EditText mInputCode1, mInputCode2, mInputCode3, mInputCode4, mInputCode5, mInputCode6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_);

        TextView mTextMobile = findViewById(R.id.text_mobile);

        mTextMobile.setText(String.format("+7%s", getIntent().getStringExtra("mobile")));

        mInputCode1 = findViewById(R.id.input_code_1);
        mInputCode2 = findViewById(R.id.input_code_2);
        mInputCode3 = findViewById(R.id.input_code_3);
        mInputCode4 = findViewById(R.id.input_code_4);
        mInputCode5 = findViewById(R.id.input_code_5);
        mInputCode6 = findViewById(R.id.input_code_6);

        setupCodeInput();

        final ProgressBar mProgressBar = findViewById(R.id.progress_bar);
        final Button mVerifyBtn = findViewById(R.id.verify_btn);

        verificationId = getIntent().getStringExtra("verificationId");

        mVerifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInputCode1.getText().toString().isEmpty()
                        || mInputCode2.getText().toString().isEmpty()
                        || mInputCode3.getText().toString().isEmpty()
                        || mInputCode4.getText().toString().isEmpty()
                        || mInputCode5.getText().toString().isEmpty()
                        || mInputCode6.getText().toString().isEmpty()) {
                    Toast.makeText(ActivityVerify.this, "Введите проверочный код", Toast.LENGTH_SHORT).show();
                    return;
                }

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
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    mProgressBar.setVisibility(View.GONE);
                                    mVerifyBtn.setVisibility(View.VISIBLE);
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(getApplicationContext(), ActivityVerifyCongrad.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(ActivityVerify.this, "Введен неверный проверочный код", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

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
