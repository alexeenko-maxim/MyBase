package braingame.amax.mybase.Controllers;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import braingame.amax.mybase.R;

public class ActivityGrammar extends AppCompatActivity {

    SharedPreferences sPref = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grammar);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        sPref = getSharedPreferences("name", MODE_PRIVATE);
        String userName = sPref.getString("name", "");

    }

}
