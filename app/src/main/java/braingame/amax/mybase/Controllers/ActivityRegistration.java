package braingame.amax.mybase.Controllers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import braingame.amax.mybase.R;


public class ActivityRegistration extends Activity {

    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //---Обьявление переменных с привязкой
        final EditText mInputPhoneField = findViewById(R.id.phone_number_text);
        final Button mButtonSendPhone = findViewById(R.id.button_send_phone);
        final ProgressBar mProgressBar = findViewById(R.id.progress_Bar);
        final EditText mUserNameField = findViewById(R.id.input_userName_field);
        //---
        
        //---События по нажатию кнопки Зарегистрировать
        try {
            mButtonSendPhone.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v){
                    //---Если поле пустое ничего не делать
                    if (mUserNameField.getText().toString().isEmpty()){
                        Toast.makeText(ActivityRegistration.this, "Введите Имя пользователя", Toast.LENGTH_SHORT ).show();
                        return;
                    }

                    else if (mInputPhoneField.getText().toString().trim().isEmpty()){
                        Toast.makeText(ActivityRegistration.this, "Введите номер телефона", Toast.LENGTH_SHORT ).show();
                        return;
                    }

                    mProgressBar.setVisibility(View.VISIBLE);
                    mButtonSendPhone.setVisibility(View.INVISIBLE);

                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            "+7" + mInputPhoneField.getText().toString(),
                            60,
                            TimeUnit.SECONDS,
                            ActivityRegistration.this,
                            new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){

                                @Override
                                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                    mProgressBar.setVisibility(View.GONE);
                                    mButtonSendPhone.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onVerificationFailed(@NonNull FirebaseException e) {
                                    mProgressBar.setVisibility(View.GONE);
                                    mButtonSendPhone.setVisibility(View.VISIBLE);
                                    Toast.makeText(ActivityRegistration.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                    mProgressBar.setVisibility(View.GONE);
                                    mButtonSendPhone.setVisibility(View.VISIBLE);
                                    Intent intent = new Intent(getApplicationContext(), ActivityVerify.class);
                                    intent.putExtra("mobile", mInputPhoneField.getText().toString());
                                    intent.putExtra("verificationId", verificationId);
                                    intent.putExtra("user", mUserNameField.getText());
                                    startActivity(intent);
                                }
                            }
                    );
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }


    }
}
        



