package braingame.amax.mybase.Controllers;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import braingame.amax.mybase.R;

public class ActivitySentens extends AppCompatActivity {
    SharedPreferences sPref = null;
    TextView mTextViewMenu, mTextViewExit, mUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sentens);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        sPref = getSharedPreferences("name", MODE_PRIVATE);
        String userName = sPref.getString("name", "");

        mTextViewMenu = findViewById(R.id.back_to_menu);
        mTextViewExit = findViewById(R.id.exit);
        mUserName = findViewById(R.id.user_name);
        mUserName.setText(userName);


        mTextViewExit.setOnClickListener(v -> {
            finishAffinity();
        });
        mTextViewMenu.setOnClickListener(v -> {
            Intent intent = new Intent(ActivitySentens.this, ActivityMenu.class);
            startActivity(intent);
        });
    }

}
