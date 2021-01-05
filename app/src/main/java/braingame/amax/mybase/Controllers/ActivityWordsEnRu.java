package braingame.amax.mybase.Controllers;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import braingame.amax.mybase.Models.DatabaseHelper;
import braingame.amax.mybase.R;

import static braingame.amax.mybase.Models.DB.QUERY_SELECT_MIN_EN;
import static braingame.amax.mybase.Models.DB.QUERY_SELECT_NEW_WORD;

public class ActivityWordsEnRu extends AppCompatActivity {

    private SQLiteDatabase mDb;
    Button mAddAvatar;
    TextView mTextViewEn;
    EditText mInputRuAnswer;
    Button mBtnCheckAnswer, mBtnMissWord;

//исправить скобки (ал) в бд
    private static String answer;
    private static int sumWord;
    private static int iterator = 0;
    private static int totalScore = 0;
    private static HashMap<Integer, ArrayList<String>> hashMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words_enru);
        //-
        Bundle arguments = getIntent().getExtras();
        assert arguments != null;
        int mTotalWords = (int) arguments.get("select_session");
        sumWord = mTotalWords*2;
        System.out.println("Выбрана ссесия - " + mTotalWords);
        //-
        DatabaseHelper mDBHelper = new DatabaseHelper(this);
        mDb = mDBHelper.getWritableDatabase();

        mAddAvatar = findViewById(R.id.btn_add_avatar);
        mTextViewEn = findViewById(R.id.textViewEn);
        mBtnCheckAnswer = findViewById(R.id.btn_check_answer);
        mInputRuAnswer = findViewById(R.id.input_ru_answer);
        mBtnMissWord = findViewById(R.id.btn_miss_word);

        createWordsArray(mTotalWords);
        check();
//
//        try {
//            mAddAvatar.setOnClickListener(new View.OnClickListener() {
//                @Override
//                    public void onClick(View v) {
//                OpenFileDialog fileDialog = new OpenFileDialog(ActivityWordsEnRu.this);
//                            fileDialog.show();
//                    }
//                    });
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }

        try {
            mBtnCheckAnswer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mInputRuAnswer.length()<1) toast("Введите ответ");
                    else if (answer.contains(mInputRuAnswer.getText())) {
                        toast("Правильно");
                        totalScore++;
                        mInputRuAnswer.setText("");
                        updateStatUp(Objects.requireNonNull(hashMap.get(iterator)).get(1));
                        iterator++;
                        System.out.println(iterator);
                        check();
                    }else {
                        toast("Неверно, вот некоторые варианты перевода: " + "\n" + answer);
                        mInputRuAnswer.setText("");
                        updateStatDown(Objects.requireNonNull(hashMap.get(iterator)).get(1));
                        iterator++;
                        check();
                    }
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        mBtnMissWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStatDown(Objects.requireNonNull(hashMap.get(iterator)).get(1));
                System.out.println(Objects.requireNonNull(hashMap.get(iterator)).get(1));
                iterator++;
                check();
            }
        });
    }//----------OnCreated

    private void showWord(int index) {
        String str = Objects.requireNonNull(hashMap.get(index)).get(1) + " " + Objects.requireNonNull(hashMap.get(index)).get(2) + " [" + Objects.requireNonNull(hashMap.get(index)).get(4) + "]";
        answer = Objects.requireNonNull(hashMap.get(index)).get(3);
        mTextViewEn.setText(str);
        System.out.println(answer);
    }

    private void createWordsArray(int sizeArray){
        for (int i = 0, j = sizeArray; i<sizeArray; i++,j++){
            hashMap.put(i,createArrayWithNewWord());
            hashMap.put(j,createArrayWithOldWord());
        }
        System.out.println(hashMap);
    }

private void check(){
        if (iterator!=sumWord) showWord(iterator);
        else {
            createOneButtonAlertDialog("Вы успешно завершили запланированную сессию. \nВы правильно перевели " + totalScore + " слов из " + sumWord + "\nНажмите \"ОК\" для продолжения и спланируйте новую ссесию");
        }
    System.out.println("Iterator = " + iterator + " / sumWord = " + sumWord);
}

    private void createOneButtonAlertDialog(String content) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityWordsEnRu.this);
        builder.setTitle("Поздравляем!");
        builder.setMessage(content);
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        Intent intent = new Intent(getApplicationContext(), ActivityWords.class);
                        startActivity(intent);
                    }
                });
        builder.show();
    }

    private void toast(String s) {
        Toast toastNull = Toast.makeText(ActivityWordsEnRu.this, s, Toast.LENGTH_LONG);
        toastNull.setGravity(Gravity.CENTER, 0, 0);
        toastNull.show();
    }


    private ArrayList<String> createArrayWithNewWord() {
        ArrayList<String> tempArr = new ArrayList<>();
        Cursor cursor = mDb.rawQuery(QUERY_SELECT_NEW_WORD, null);
        cursor.moveToFirst();
        for (int i = 0; i<cursor.getColumnCount(); i++) {
            tempArr.add(cursor.getString(i));
        }
        cursor.close();
        return tempArr;
    }

    private ArrayList<String> createArrayWithOldWord() {
        ArrayList<String> tempArr = new ArrayList<>();
        Cursor cursor = mDb.rawQuery(QUERY_SELECT_MIN_EN, null);
        cursor.moveToFirst();
        for (int i = 0; i<cursor.getColumnCount(); i++) {
            tempArr.add(cursor.getString(i));
        }
        cursor.close();
        return tempArr;
    }

    private void updateStatDown(String en) {
        int tempValue1 = Integer.parseInt((String) Objects.requireNonNull(hashMap.get(0)).get(5))-10;
        int tempValue2 = Integer.parseInt((String) Objects.requireNonNull(hashMap.get(0)).get(8))+1;
        System.out.println(tempValue1);
        System.out.println(tempValue2);
        ContentValues cv = new ContentValues();
        cv.put("stat", tempValue1);
        cv.put("countdown", tempValue2);
        cv.put("priority", "learn");
        try {
            mDb.update("words", cv, "en = ?", new String[] { en });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateStatUp(String en) {
        int tempValue1 = Integer.parseInt((String) Objects.requireNonNull(hashMap.get(0)).get(5))+10;
        int tempValue2 = Integer.parseInt((String) Objects.requireNonNull(hashMap.get(0)).get(7))+1;
        System.out.println(tempValue1);
        System.out.println(tempValue2);
        ContentValues cv = new ContentValues();
        cv.put("stat", tempValue1);
        cv.put("countdown", tempValue2);
        cv.put("priority", "learn");
        try {
            mDb.update("words", cv, "en = ?", new String[] { en });
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

}
