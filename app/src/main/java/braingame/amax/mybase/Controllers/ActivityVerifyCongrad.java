package braingame.amax.mybase.Controllers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import braingame.amax.mybase.R;


public class ActivityVerifyCongrad extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_congrad);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Button mBtnGoToMainMenu = findViewById(R.id.btn_go_to_main_menu);

        try {
            mBtnGoToMainMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), ActivityMenu.class);
                    startActivity(intent);
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
}
        



