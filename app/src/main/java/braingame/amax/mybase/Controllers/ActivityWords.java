package braingame.amax.mybase.Controllers;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import braingame.amax.mybase.Models.OpenFileDialog;
import braingame.amax.mybase.R;

public class ActivityWords extends AppCompatActivity {

    Button mAddAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words);

        final Dialog selectSession = new Dialog(this);
        selectSession.requestWindowFeature(Window.FEATURE_NO_TITLE);
        selectSession.setContentView(R.layout.activity_words_enru_select_session);
        Objects.requireNonNull(selectSession.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        Button mBtnSelectEasySession = selectSession.findViewById(R.id.btn_easy_session);
        Button mBtnSelectNormalSession = selectSession.findViewById(R.id.btn_normal_session);
        Button mBtnSelectHardSession = selectSession.findViewById(R.id.btn_hard_session);
        Button mBtnGoLearnEnRu = findViewById(R.id.btn_go_learnEnRu);
        Button mBtnGoLearnRuEn = findViewById(R.id.btn_go_learnRuEn);
        Button mBtnGoAddWord = findViewById(R.id.btn_go_add_word);

        mBtnGoLearnEnRu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectSession.show();
            }
        });

        mBtnSelectEasySession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivityWordsEnru();
            }


        });




        mAddAvatar = findViewById(R.id.btn_add_avatar);
        mAddAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenFileDialog fileDialog = new OpenFileDialog(ActivityWords.this);
                fileDialog.show();
            }
        });

    }
    private void goToActivityWordsEnru() {
        Intent intent = new Intent(this, ActivityWordsEnRu.class);
        intent.putExtra("select_session", 5);
        startActivity(intent);
    }
}
