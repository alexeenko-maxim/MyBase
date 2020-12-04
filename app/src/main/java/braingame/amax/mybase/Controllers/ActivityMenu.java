package braingame.amax.mybase.Controllers;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import braingame.amax.mybase.Models.DB;
import braingame.amax.mybase.Models.OpenFileDialog;
import braingame.amax.mybase.R;

public class ActivityMenu extends AppCompatActivity {

    Button mAddAvatar;
//    String userName = DB.getUserName();
//    TextView mUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


//        mUserName = findViewById(R.id.user_name);
//        mUserName.setText(userName);
        mAddAvatar = findViewById(R.id.btn_add_avatar);
        mAddAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenFileDialog fileDialog = new OpenFileDialog(ActivityMenu.this);
                fileDialog.show();
            }
        });

    }

}
