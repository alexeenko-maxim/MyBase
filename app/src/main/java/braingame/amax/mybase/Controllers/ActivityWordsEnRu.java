package braingame.amax.mybase.Controllers;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.logging.Logger;

import braingame.amax.mybase.Models.DatabaseHelper;
import braingame.amax.mybase.R;

import static braingame.amax.mybase.Models.DatabaseQuery.QUERY_SELECT_MIN_EN;
import static braingame.amax.mybase.Models.DatabaseQuery.QUERY_SELECT_NEW_WORD;
import static braingame.amax.mybase.Models.DatabaseQuery.USERNAME;

public class ActivityWordsEnRu extends AppCompatActivity {

    private static final Logger loger = Logger.getLogger(ActivityWordsEnRu.class.getName());

    SharedPreferences mUserName;
    private SQLiteDatabase mDb;
    TextView mTextViewEn, mUser, mBackToMenu, mExit, mTextViewTrans, mTextViewPartOfSpeech;
    EditText mInputRuAnswer;
    Button mBtnCheckAnswer, mBtnMissWord;


    private static String[] answer;
    private static int sumWord;
    private static int iterator = 0;
    private static int totalScore = 0;
    private static final HashMap<Integer, ArrayList<String>> hashMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words_enru);
        //-
        mUserName = getSharedPreferences(USERNAME, MODE_PRIVATE);
        Bundle arguments = getIntent().getExtras();
        //-Скрытие строки состояния-//
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //*
        assert arguments != null;
        int mTotalWords = (int) arguments.get("select_session");
        sumWord = mTotalWords*2;
        loger.info("Выбрана ссесия - " + mTotalWords);
        loger.info("sumWord = " + sumWord);

        //-
        DatabaseHelper mDBHelper = new DatabaseHelper(this);
        mDb = mDBHelper.getWritableDatabase();

        mTextViewEn = findViewById(R.id.textViewEn);
        mTextViewTrans = findViewById(R.id.textViewTrans);
        mTextViewPartOfSpeech = findViewById(R.id.textViewPartOfSpeech);
        mBtnCheckAnswer = findViewById(R.id.btn_check_answer);
        mInputRuAnswer = findViewById(R.id.input_ru_answer);
        mBtnMissWord = findViewById(R.id.btn_miss_word);
        mUser = findViewById(R.id.user_name);
        mBackToMenu = findViewById(R.id.back_to_menu);
        mExit = findViewById(R.id.exit);
        mUser.setText(mUserName.getString(USERNAME, String.valueOf(Context.MODE_PRIVATE)));

        loger.info("Состояние переменной sumWord = " + sumWord);
        loger.info("Состояние переменной mTotalWords = " + mTotalWords);
        loger.info("Состояние переменной totalScore = " + totalScore);
        loger.info("Состояние переменной answer = " + Arrays.toString(answer));

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
            mBtnCheckAnswer.setOnClickListener(v -> {
                if (mInputRuAnswer.length()<1) toast("Введите ответ", Toast.LENGTH_LONG);
                else if (checkAnswer(answer)) {
                    toast("Правильно", Toast.LENGTH_SHORT);
                    totalScore++;
                    loger.info("Количество правильных ответов = " + totalScore);
                    mInputRuAnswer.setText("");
                    updateStatUp(Objects.requireNonNull(hashMap.get(iterator)).get(1));
                    iterator++;
                    loger.info("После правильного ответа iterator = " + iterator);
                    check();
                }else {
                    toast("Неверно, вот некоторые варианты перевода: " + "\n" + Arrays.toString(answer), Toast.LENGTH_LONG);
                    mInputRuAnswer.setText("");
                    updateStatDown(Objects.requireNonNull(hashMap.get(iterator)).get(1));
                    iterator++;
                    loger.info("После неправильного ответа iterator = " + iterator);
                    check();
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        mBtnMissWord.setOnClickListener(v -> {
            updateStatDown(Objects.requireNonNull(hashMap.get(iterator)).get(1));
            System.out.println(Objects.requireNonNull(hashMap.get(iterator)).get(1));
            toast("Вот некоторые варианты перевода: " + "\n" + Arrays.toString(answer), Toast.LENGTH_LONG);
            iterator++;
            loger.info("После пропуска ответа iterator = " + iterator);
            check();
        });
    }//----------OnCreated

    private void showWord(int index) {
        loger.info("Метод showWord получил на вход значение = " + index);
        String strEn = Objects.requireNonNull(hashMap.get(index)).get(1);
        String strTrans = Objects.requireNonNull(hashMap.get(index)).get(2);
        String strPartOfSpeech = Objects.requireNonNull(hashMap.get(index)).get(4);
        answer = Objects.requireNonNull(hashMap.get(index)).get(3).split(", ");
        loger.info("Метод showWord уснановил згачение переменной answer = " + Arrays.toString(answer));
        mTextViewEn.setText(strEn);
        mTextViewTrans.setText(strTrans);
        mTextViewPartOfSpeech.setText(strPartOfSpeech);
        System.out.println(Arrays.toString(answer));

    }

    private void createWordsArray(int sizeArray){
        loger.info("Метод createWordsArray получил на вход значение = " + sizeArray);

        for (int i = 0, j = sizeArray; i<sizeArray; i++,j++){
            hashMap.put(i,createArrayWithNewWord());
            hashMap.put(j,createArrayWithOldWord());
        }
        System.out.println(hashMap);
    }

private void check(){
        if (iterator!=sumWord) {
            showWord(iterator);
            loger.info("Метод check сравнил iterator и sumWord - они не равны, поэтому вызван метод showWord");

        }
        else {
            loger.info("Метод check сравнил iterator и sumWord - они равны, поэтому переменная iterator сброшена на 0");
            iterator = 0;
            createOneButtonAlertDialog("Вы успешно завершили запланированную сессию. \nВы правильно перевели " + totalScore + " слов из " + sumWord + "\nНажмите \"ОК\" для продолжения и спланируйте новую ссесию");
        }
    System.out.println("Iterator = " + iterator + " / sumWord = " + sumWord);
}

private boolean checkAnswer(String[] arr){
    loger.info("Метод checkAnswer получил на вход массив = " + Arrays.toString(arr));

    System.out.println("Згачение на входе " + Arrays.toString(arr));
        boolean temp = false;
        for (int i = 0; i<arr.length; i++) {
            if (arr[i].contentEquals(mInputRuAnswer.getText())) {
                System.out.println("Сравниваю " + arr[i] + " c " + mInputRuAnswer.getText());
                temp = true;
                break;
            }
            else temp = false;
    }
    loger.info("Метод checkAnswer отдал на выход значени = " + temp);
    return temp;
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

    private void toast(String s, int time) {
        Toast toastNull = Toast.makeText(ActivityWordsEnRu.this, s, time);
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
        System.out.println(tempArr);
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
        System.out.println(tempArr);

        return tempArr;
    }

    private void updateStatDown(String en) {
        int tempValue1 = Integer.parseInt(Objects.requireNonNull(hashMap.get(0)).get(5))-10;
        int tempValue2 = Integer.parseInt(Objects.requireNonNull(hashMap.get(0)).get(8))+1;
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
        int tempValue1 = Integer.parseInt(Objects.requireNonNull(hashMap.get(0)).get(5))+10;
        int tempValue2 = Integer.parseInt(Objects.requireNonNull(hashMap.get(0)).get(7))+1;
        System.out.println(tempValue1);
        System.out.println(tempValue2);
        ContentValues cv = new ContentValues();
        cv.put("stat", tempValue1);
        cv.put("countup", tempValue2);
        cv.put("priority", "learn");
        try {
            mDb.update("words", cv, "en = ?", new String[] { en });
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ActivityWords.class);
        startActivity(intent);
    }
}
