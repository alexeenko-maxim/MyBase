package braingame.amax.mybase.Controllers;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import braingame.amax.mybase.R;

public class ActivityStatistic extends AppCompatActivity {

    TextView mTextViewMenu, mTextViewExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mTextViewMenu = findViewById(R.id.back_to_menu);
        mTextViewExit = findViewById(R.id.exit);

        mTextViewExit.setOnClickListener(v -> {
            finishAffinity();
        });
        mTextViewMenu.setOnClickListener(v -> {
            Intent intent = new Intent(ActivityStatistic.this, ActivityMenu.class);
            startActivity(intent);
        });
    }

}
