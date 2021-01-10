package braingame.amax.mybase.Controllers;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import braingame.amax.mybase.Models.OpenFileDialog;
import braingame.amax.mybase.R;

public class ActivityPhraseVerb extends AppCompatActivity {

    Button mAddAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phrase_verb);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mAddAvatar = findViewById(R.id.btn_add_avatar);
        mAddAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenFileDialog fileDialog = new OpenFileDialog(ActivityPhraseVerb.this);
                fileDialog.show();
            }
        });

    }

}
