package braingame.amax.mybase.Controllers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
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
import java.util.logging.Logger;

import braingame.amax.mybase.Models.DatabaseHelper;
import braingame.amax.mybase.Models.DatabaseMethods;
import braingame.amax.mybase.R;

import static braingame.amax.mybase.Models.DatabaseMethods.USERNAME;
import static braingame.amax.mybase.Models.DatabaseMethods.getCountTodayWord;
import static braingame.amax.mybase.Models.DatabaseMethods.getNewWord;
import static braingame.amax.mybase.Models.DatabaseMethods.getTodayWord;
import static braingame.amax.mybase.Models.DatabaseMethods.updateAfterFalseAnswer;
import static braingame.amax.mybase.Models.DatabaseMethods.updateAfterTrueAnswer;

public class ActivityWordsEnRu extends AppCompatActivity {

    private static final Logger loger = Logger.getLogger(ActivityWordsEnRu.class.getName());

    SharedPreferences mUserName;
    private SQLiteDatabase mDb;
    TextView mTextViewEn, mUser, mBackToMenu, mExit, mTextViewTrans, mTextViewPartOfSpeech;
    EditText mInputRuAnswer;
    Button mBtnCheckAnswer, mBtnMissWord;


    private static String[] answer;
    private static int sumNewWord;
    private static int newWordsIterator = 0;
    private static int repeatWordsIterator = 0;
    private static int totalScore = 0;
    private static int countRepeatWords = 0;

    private static final HashMap<Integer, ArrayList<String>> hashMap = new HashMap<>();
    private static final ArrayList<String> arrayList = new ArrayList<>();

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
        sumNewWord = mTotalWords;
        loger.info("--- Выбрана ссесия - " + mTotalWords);
        loger.info("--- sumWord = " + sumNewWord);

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

        loger.info("Состояние переменной sumWord = " + sumNewWord);
        loger.info("Состояние переменной mTotalWords = " + mTotalWords);
        loger.info("Состояние переменной totalScore = " + totalScore);
        loger.info("Состояние переменной answer = " + Arrays.toString(answer));

        countRepeatWords=getCountTodayWord(mDb);
        System.out.println("Количество слов которые нужно повторить сегодня: " + countRepeatWords);
        getNewWord(mDb, arrayList, sumNewWord);
        showWord();
        System.out.println(arrayList.toString());

        try {
            mBtnCheckAnswer.setOnClickListener(v -> {
                if (mInputRuAnswer.length() < 1) toast("Введите ответ", Toast.LENGTH_LONG);
                else if (checkAnswer(answer)) {
                    toast("Правильно", Toast.LENGTH_SHORT);
                    totalScore++;
                    loger.info("Количество правильных ответов = " + totalScore);
                    mInputRuAnswer.setText("");
                    updateAfterTrueAnswer(mDb, arrayList, arrayList.get(1));
                    newWordsIterator++;
                    loger.info("После правильного ответа iterator = " + newWordsIterator);
                    updateNewWord();
                    updateRepeatWord();
                    finishSession();
                } else {
                    toast("Неверно, вот некоторые варианты перевода: " + "\n" + Arrays.toString(answer), Toast.LENGTH_LONG);
                    mInputRuAnswer.setText("");
                    updateAfterFalseAnswer(mDb, arrayList, arrayList.get(1));
                    newWordsIterator++;
                    loger.info("После неправильного ответа iterator = " + newWordsIterator);
                    updateNewWord();
                    updateRepeatWord();
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        mBtnMissWord.setOnClickListener(v -> {
            try {
                updateAfterFalseAnswer(mDb, arrayList, arrayList.get(1));
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
            finally {
                System.out.println(arrayList.get(1));
                toast("Вот некоторые варианты перевода: " + "\n" + Arrays.toString(answer), Toast.LENGTH_LONG);
                newWordsIterator++;
                loger.info("После пропуска ответа iterator = " + newWordsIterator);
                updateNewWord();
            }

        });
    }//----------OnCreated

    private void showWord() {
        System.out.println("Сработал метод showWord()");
        System.out.println(arrayList.toString());
        String strEn = arrayList.get(1);
        String strTrans = arrayList.get(2);
        String strPartOfSpeech = arrayList.get(4);
        answer = arrayList.get(3).split(", ");
        loger.info("Метод showWord уснановил згачение переменной answer = " + Arrays.toString(answer));
        mTextViewEn.setText(strEn);
        mTextViewTrans.setText(strTrans);
        mTextViewPartOfSpeech.setText(strPartOfSpeech);
        System.out.println(Arrays.toString(answer));
    }

    private void updateNewWord() {
        if (newWordsIterator <= sumNewWord) {
            System.out.println("Сработал метод updateNewWord()");
            arrayList.clear();
            DatabaseMethods.getNewWord(mDb, arrayList, newWordsIterator);
            showWord();
        }
    }

    private void updateRepeatWord() {
        if (newWordsIterator > sumNewWord) {
            System.out.println("Сработал метод updateRepeatWord()");
            arrayList.clear();
            System.out.println("Массив очищен");
            getTodayWord(mDb, arrayList);
            showWord();
            repeatWordsIterator++;
            System.out.println("Значение repeatWordsIterator: " + repeatWordsIterator);
            System.out.println("Значение countRepeatWords: " + countRepeatWords);
            loger.info("Метод check сравнил iterator и sumWord - они не равны, поэтому вызван метод showWord");

        }
        System.out.println("Iterator = " + newWordsIterator + " / sumWord = " + sumNewWord);
    }

    private void finishSession() {
        if(repeatWordsIterator>=countRepeatWords){
            System.out.println("--- Сработало условие в методе updateRepeatWord()");
            System.out.println("--- repeatWordsIterator равный: " + repeatWordsIterator + " Сравнился с countRepeatWords равный:" + countRepeatWords);
            loger.info("Метод check сравнил iterator и sumWord - они равны, поэтому переменная iterator сброшена на 0");
            createOneButtonAlertDialog("Вы повторили все необходимые слова и завершили сессию. \nВы правильно перевели " + totalScore + " слов из " + sumNewWord + "\nНажмите \"ОК\" для продолжения и спланируйте новую ссесию");
        }
    }

    private boolean checkAnswer(String[] arr) {
        loger.info("Метод checkAnswer получил на вход массив = " + Arrays.toString(arr));

        System.out.println("Згачение на входе " + Arrays.toString(arr));
        boolean temp = false;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].contentEquals(mInputRuAnswer.getText())) {
                System.out.println("Сравниваю " + arr[i] + " c " + mInputRuAnswer.getText());
                temp = true;
                break;
            } else temp = false;
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ActivityWords.class);
        startActivity(intent);
    }
}
