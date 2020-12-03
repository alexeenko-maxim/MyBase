package braingame.amax.mybase.Controllers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import braingame.amax.mybase.R;

public class MainActivity extends AppCompatActivity {

    private FirebaseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

        Button mBtnStart = findViewById(R.id.btn_start);

        try {
            mBtnStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

    //                mAuth.signOut();
                    if (mCurrentUser == null) sendUserToLogin();
                    else sendUserToMainMenu();


                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        if(mCurrentUser == null){
//            sendUserToLogin();
//        }
//    }

    private void sendUserToLogin() {
        Intent loginIntent = new Intent(MainActivity.this, ActivityRegistration.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

    private void sendUserToMainMenu() {
        Intent goToMenu = new Intent(MainActivity.this, ActivityMenu.class);
        goToMenu.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        goToMenu.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(goToMenu);
        finish();
    }

}