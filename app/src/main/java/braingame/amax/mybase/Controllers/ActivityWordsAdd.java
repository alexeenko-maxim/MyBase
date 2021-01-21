package braingame.amax.mybase.Controllers;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import braingame.amax.mybase.Models.DatabaseHelper;
import braingame.amax.mybase.Models.DatabaseMethods;
import braingame.amax.mybase.R;


public class ActivityWordsAdd extends AppCompatActivity {
    SharedPreferences sPref = null;
    SQLiteDatabase mDb;
    Button mBtnAddWord, mBtnGoBack;
    TextView mTextViewMenu, mTextViewUserName, mTextViewExit, mTextViewTotalWordInDB, mTextViewTotalYourWords, mTextViewTitleAddWord, mTextViewWordEn, mTextViewWordTrans, mTextViewWordRu, mTextViewWordPartOfSpeech;
    String temp;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words_add);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initDataBaseHelper();

        sPref = getSharedPreferences("name", MODE_PRIVATE);
        String userName = sPref.getString("name", "");

        DatabaseMethods.getTotalWordInDB(mDb);
        temp = DatabaseMethods.getTotalWordInDB(mDb);

        mBtnAddWord = findViewById(R.id.btn_add_word);
        mBtnGoBack = findViewById(R.id.btn_go_back);
        mTextViewMenu = findViewById(R.id.back_to_menu);
        mTextViewExit = findViewById(R.id.exit);
        mTextViewUserName = findViewById(R.id.user_name);

        mTextViewTotalWordInDB = findViewById(R.id.total_words_inDB);
        mTextViewTotalYourWords = findViewById(R.id.total_your_words);
        mTextViewTitleAddWord = findViewById(R.id.addWord);
        mTextViewWordEn = findViewById(R.id.editTextEn);
        mTextViewWordTrans = findViewById(R.id.editTextTrans);
        mTextViewWordRu = findViewById(R.id.editTextTextRu);
        mTextViewWordPartOfSpeech = findViewById(R.id.editTextPartOfSpeech);
        mTextViewTotalWordInDB.setText("Количество слов в базе данных: " + DatabaseMethods.getTotalWordInDB(mDb));
        mTextViewTotalYourWords.setText("Количество Вами добавленных слов: " + DatabaseMethods.getTotalYourWords(mDb));
        mTextViewUserName.setText(userName);
        try {
            mBtnAddWord.setOnClickListener(v -> {
                if (checkFillUp()) {
                    toast("Заполните все поля", Toast.LENGTH_LONG);
                    System.out.println("Значение полей: " + mTextViewWordEn.length() + " " + mTextViewWordTrans.length() + " " + mTextViewWordRu.length() + " " + mTextViewWordPartOfSpeech.length());
                } else {
                    DatabaseMethods.addWordInDB(mDb, mTextViewWordEn.getText().toString(),
                            mTextViewWordTrans.getText().toString(),
                            mTextViewWordRu.getText().toString(),
                            mTextViewWordPartOfSpeech.getText().toString());
                    clearFields();
                    toast("Слово добавленно", Toast.LENGTH_SHORT);
                    mTextViewTotalWordInDB.setText("Количество слов в базе данных: " + DatabaseMethods.getTotalWordInDB(mDb));
                    mTextViewTotalYourWords.setText("Количество Вами добавленных слов: " + DatabaseMethods.getTotalYourWords(mDb));
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        try {
            mBtnGoBack.setOnClickListener(v -> {
                Intent intent = new Intent(this, ActivityWords.class);
                startActivity(intent);
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        mTextViewMenu.setOnClickListener(v -> {
            Intent intent = new Intent(ActivityWordsAdd.this, ActivityMenu.class);
            startActivity(intent);
        });

        mTextViewExit.setOnClickListener(v -> finishAffinity());
    }//onCreate

    private boolean checkFillUp() {
        return mTextViewWordEn.length() < 1 | mTextViewWordTrans.length() < 1 | mTextViewWordRu.length() < 1 | mTextViewWordPartOfSpeech.length() < 1;
    }

    private void clearFields() {
        mTextViewWordEn.setText("");
        mTextViewWordTrans.setText("");
        mTextViewWordRu.setText("");
        mTextViewWordPartOfSpeech.setText("");
    }

    private void initDataBaseHelper() {
        System.out.println("--- Вызван метод initDataBaseHelper()");
        DatabaseHelper mDBHelper = new DatabaseHelper(this);
        mDb = mDBHelper.getWritableDatabase();
    }

    private void toast(String s, int time) {
        System.out.println("--- Вызван метод toast");
        Toast toastNull = Toast.makeText(ActivityWordsAdd.this, s, time);
        toastNull.setGravity(Gravity.CENTER, 0, 0);
        toastNull.show();
    }


}
