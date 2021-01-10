package braingame.amax.mybase.Controllers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import braingame.amax.mybase.R;

public class ActivityMain extends AppCompatActivity {



    private FirebaseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

        Button mBtnStart = findViewById(R.id.btn_start);

        try {
            mBtnStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //---Если пользователь не найден > выполнить метод перехода на страницу регистрации
                    if (mCurrentUser == null) sendUserToLogin();
                    //---Иначе выполнить метод перехода в главное меню
                    else sendUserToMainMenu();
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    //---Метод перехода на страницу регистрации---
    private void sendUserToLogin() {
        Intent loginIntent = new Intent(ActivityMain.this, ActivityRegistration.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }
    //---Метод перехода на страницу главного меню---
    private void sendUserToMainMenu() {
        Intent goToMenu = new Intent(ActivityMain.this, ActivityMenu.class);
        goToMenu.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        goToMenu.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(goToMenu);
        finish();
    }

}