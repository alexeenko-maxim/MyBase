package braingame.amax.mybase.Controllers;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        final Dialog selectSessionEnRu = new Dialog(this);
        selectSessionEnRu.requestWindowFeature(Window.FEATURE_NO_TITLE);
        selectSessionEnRu.setContentView(R.layout.activity_words_enru_select_session);
        Objects.requireNonNull(selectSessionEnRu.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button mBtnSelectEasySessionEnRu = selectSessionEnRu.findViewById(R.id.btn_easy_session);
        Button mBtnSelectNormalSessionEnRu = selectSessionEnRu.findViewById(R.id.btn_normal_session);
        Button mBtnSelectHardSessionEnRu = selectSessionEnRu.findViewById(R.id.btn_hard_session);

        final Dialog selectSessionRuEn = new Dialog(this);
        selectSessionRuEn.requestWindowFeature(Window.FEATURE_NO_TITLE);
        selectSessionRuEn.setContentView(R.layout.activity_words_enru_select_session);
        Objects.requireNonNull(selectSessionEnRu.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button mBtnSelectEasySessionRuEn = selectSessionRuEn.findViewById(R.id.btn_easy_session);
        Button mBtnSelectNormalSessionRuEn = selectSessionRuEn.findViewById(R.id.btn_normal_session);
        Button mBtnSelectHardSessionRuEn = selectSessionRuEn.findViewById(R.id.btn_hard_session);

        Button mBtnGoLearnEnRu = findViewById(R.id.btn_go_learnEnRu);
        Button mBtnGoLearnRuEn = findViewById(R.id.btn_go_learnRuEn);
        Button mBtnGoAddWord = findViewById(R.id.btn_go_add_word);

        mBtnGoLearnEnRu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectSessionEnRu.show();
            }
        });

        mBtnGoLearnRuEn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectSessionRuEn.show();
            }
        });


        mBtnSelectEasySessionEnRu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivityWordsEnruEasy();
            }
        });

        mBtnSelectNormalSessionEnRu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivityWordsEnruNormal();
            }
        });

        mBtnSelectHardSessionEnRu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivityWordsEnruHard();
            }
        });

        mBtnSelectEasySessionRuEn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivityWordsRuenEasy();
            }
        });

        mBtnSelectNormalSessionRuEn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivityWordsRuenNormal();
            }
        });

        mBtnSelectHardSessionRuEn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivityWordsRuenHard();
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
    private void goToActivityWordsEnruEasy() {
        Intent intent = new Intent(this, ActivityWordsEnRu.class);
        intent.putExtra("select_session", 5);
        startActivity(intent);
    }
    private void goToActivityWordsEnruNormal() {
        Intent intent = new Intent(this, ActivityWordsEnRu.class);
        intent.putExtra("select_session", 10);
        startActivity(intent);
    }
    private void goToActivityWordsEnruHard() {
        Intent intent = new Intent(this, ActivityWordsEnRu.class);
        intent.putExtra("select_session", 25);
        startActivity(intent);
    }

    private void goToActivityWordsRuenEasy() {
        Intent intent = new Intent(this, ActivityWordsRuEn.class);
        intent.putExtra("select_session", 5);
        startActivity(intent);
    }
    private void goToActivityWordsRuenNormal() {
        Intent intent = new Intent(this, ActivityWordsRuEn.class);
        intent.putExtra("select_session", 10);
        startActivity(intent);
    }
    private void goToActivityWordsRuenHard() {
        Intent intent = new Intent(this, ActivityWordsRuEn.class);
        intent.putExtra("select_session", 25);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ActivityMenu.class);
        startActivity(intent);
    }
}
