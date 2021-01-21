package braingame.amax.mybase.Controllers;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import braingame.amax.mybase.R;

public class ActivityMenu extends AppCompatActivity {

    SharedPreferences sPref = null;
    TextView mTextViewExit, mUserName;
    Button mBtnGoWords, mBtnGoSentens, mBtnGoGrammar, mBtnGoPhrase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        sPref = getSharedPreferences("name", MODE_PRIVATE);

        String userName = sPref.getString("name", "");

        System.out.println(userName);




        mTextViewExit = findViewById(R.id.exit);
        mBtnGoWords = findViewById(R.id.btn_go_words);
        mBtnGoSentens = findViewById(R.id.btn_go_sentens);
        mBtnGoGrammar = findViewById(R.id.btn_go_grammar);
        mBtnGoPhrase = findViewById(R.id.btn_go_phrase);
        mUserName = findViewById(R.id.user_name);
        mUserName.setText(userName);

        mTextViewExit.setOnClickListener(v -> {
            finishAffinity();
        });
        mBtnGoWords.setOnClickListener(v -> {
            Intent intent = new Intent(this, ActivityWords.class);
            startActivity(intent);
        });
        mBtnGoSentens.setOnClickListener(v -> {
            Intent intent = new Intent(this, ActivitySentens.class);
            startActivity(intent);
        });
        mBtnGoGrammar.setOnClickListener(v -> {
            Intent intent = new Intent(this, ActivityGrammar.class);
            startActivity(intent);
        });
        mBtnGoPhrase.setOnClickListener(v -> {
            Intent intent = new Intent(this, ActivityPhraseVerb.class);
            startActivity(intent);
        });
    }


}
