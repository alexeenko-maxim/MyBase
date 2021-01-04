package braingame.amax.mybase.Controllers;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import braingame.amax.mybase.Models.OpenFileDialog;
import braingame.amax.mybase.R;

public class ActivityMenu extends AppCompatActivity {

    SharedPreferences sPref = null;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        sPref = getSharedPreferences("braingame.amax.mybase", MODE_PRIVATE);

        Button mAddAvatar = findViewById(R.id.btn_add_avatar);
        mAddAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenFileDialog fileDialog = new OpenFileDialog(ActivityMenu.this);
                fileDialog.show();
            }
        });

    }


}
