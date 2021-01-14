package braingame.amax.mybase.Controllers;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import braingame.amax.mybase.Models.DatabaseHelper;
import braingame.amax.mybase.R;

import static braingame.amax.mybase.Models.DatabaseQuery.COLLUMN_NAMES_EN;
import static braingame.amax.mybase.Models.DatabaseQuery.COLLUMN_NAMES_PART_OF_SPEECH;
import static braingame.amax.mybase.Models.DatabaseQuery.COLLUMN_NAMES_RU;
import static braingame.amax.mybase.Models.DatabaseQuery.COLLUMN_NAMES_TRANS;
import static braingame.amax.mybase.Models.DatabaseQuery.DATABASE_NAMES_TABLE;
import static braingame.amax.mybase.Models.DatabaseQuery.QUERY_GET_TOTAL_WORDS;
import static braingame.amax.mybase.Models.DatabaseQuery.QUERY_GET_TOTAL_YOUR_WORDS;


public class ActivityWordsAdd extends AppCompatActivity {
    final String TAG = "INFO";
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
        getTotalWordInDB();
        temp = getTotalWordInDB();

        mBtnAddWord = findViewById(R.id.btn_add_word);
        mBtnGoBack = findViewById(R.id.btn_go_back);
        mTextViewMenu = findViewById(R.id.back_to_menu);
        mTextViewUserName = findViewById(R.id.user_name);
        mTextViewExit = findViewById(R.id.exit);
        mTextViewTotalWordInDB = findViewById(R.id.total_words_inDB);
        mTextViewTotalYourWords = findViewById(R.id.total_your_words);
        mTextViewTitleAddWord = findViewById(R.id.addWord);
        mTextViewWordEn = findViewById(R.id.editTextEn);
        mTextViewWordTrans = findViewById(R.id.editTextTrans);
        mTextViewWordRu = findViewById(R.id.editTextTextRu);
        mTextViewWordPartOfSpeech = findViewById(R.id.editTextPartOfSpeech);
        mTextViewTotalWordInDB.setText("Количество слов в базе данных: " + getTotalWordInDB());
        mTextViewTotalYourWords.setText("Количество Вами добавленных слов: " + getTotalYourWords());

        try {
            mBtnAddWord.setOnClickListener(v -> {
                if (checkFillUp()) {
                    toast("Заполните все поля", Toast.LENGTH_LONG);
                    System.out.println("Значение полей: " + mTextViewWordEn.length() + " " + mTextViewWordTrans.length() + " " + mTextViewWordRu.length() + " " + mTextViewWordPartOfSpeech.length());
                } else {
                    addWordInDB(mTextViewWordEn.getText().toString(),
                            mTextViewWordTrans.getText().toString(),
                            mTextViewWordRu.getText().toString(),
                            mTextViewWordPartOfSpeech.getText().toString());
                    clearFields();
                    toast("Слово добавленно", Toast.LENGTH_SHORT);
                    mTextViewTotalWordInDB.setText("Количество слов в базе данных: " + getTotalWordInDB());
                    mTextViewTotalYourWords.setText("Количество Вами добавленных слов: " + getTotalYourWords());
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }


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

    private String getTotalWordInDB() {
        Cursor cursor = mDb.rawQuery(QUERY_GET_TOTAL_WORDS, null);
        ArrayList<String> tempArr = new ArrayList<>();
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getColumnCount(); i++) {
            tempArr.add(cursor.getString(i));
        }
        cursor.close();
        Log.d(TAG, tempArr.get(0));
        return tempArr.get(0);
    }

    private String getTotalYourWords() {
        Cursor cursor = mDb.rawQuery(QUERY_GET_TOTAL_YOUR_WORDS, null);
        ArrayList<String> tempArr = new ArrayList<>();
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getColumnCount(); i++) {
            tempArr.add(cursor.getString(i));
        }
        cursor.close();
        Log.d(TAG, tempArr.get(0));
        return tempArr.get(0);
    }

    private void addWordInDB(String en, String trans, String ru, String part_of_speech) {
        ContentValues cv = new ContentValues();
        cv.put(COLLUMN_NAMES_EN, en);
        cv.put(COLLUMN_NAMES_TRANS, trans);
        cv.put(COLLUMN_NAMES_RU, ru);
        cv.put(COLLUMN_NAMES_PART_OF_SPEECH, part_of_speech);
        try {
            mDb.insert(DATABASE_NAMES_TABLE, null, cv);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
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
