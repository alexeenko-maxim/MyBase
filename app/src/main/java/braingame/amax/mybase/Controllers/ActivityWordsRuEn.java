package braingame.amax.mybase.Controllers;

import android.app.AlertDialog;
import android.content.Context;
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
import java.util.logging.Logger;

import braingame.amax.mybase.Models.DatabaseHelper;
import braingame.amax.mybase.Models.DatabaseMethods;
import braingame.amax.mybase.R;

import static braingame.amax.mybase.Models.DatabaseMethods.getAnalogAnswer;
import static braingame.amax.mybase.Models.DatabaseMethods.getCountTodayWord;
import static braingame.amax.mybase.Models.DatabaseMethods.getNewWord;
import static braingame.amax.mybase.Models.DatabaseMethods.getTodayWord;
import static braingame.amax.mybase.Models.DatabaseMethods.updateAfterFalseAnswer;
import static braingame.amax.mybase.Models.DatabaseMethods.updateAfterTrueAnswer;
import static braingame.amax.mybase.Models.DatabaseQuery.USERNAME;

public class ActivityWordsRuEn extends AppCompatActivity {

    private static final Logger loger = Logger.getLogger(ActivityWordsRuEn.class.getName());

    SharedPreferences mUserName;
    private SQLiteDatabase mDb;
    TextView mTextViewRu, mUser, mBackToMenu, mExit;
    EditText mInputEnAnswer;
    Button mBtnCheckAnswer, mBtnMissWord;

    private static String[] answer;
    private static int sumNewWord;
    private static int newWordsIterator = 0;
    private static int repeatWordsIterator = 0;
    private static int totalScore = 0;
    private static int countRepeatWords = 0;

    private static final ArrayList<String> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words_ruen);
        hideStatusBar();

        mUserName = getSharedPreferences(USERNAME, MODE_PRIVATE);

        int mTotalWords = setSessionLength();

        initDataBaseHelper();

        mTextViewRu = findViewById(R.id.textViewRu);
        mBtnCheckAnswer = findViewById(R.id.btn_check_answer);
        mInputEnAnswer = findViewById(R.id.input_en_answer);
        mBtnMissWord = findViewById(R.id.btn_miss_word);
        mUser = findViewById(R.id.user_name);
        mBackToMenu = findViewById(R.id.back_to_menu);
        mExit = findViewById(R.id.exit);
        mUser.setText(mUserName.getString(USERNAME, String.valueOf(Context.MODE_PRIVATE)));

        countRepeatWords = getCountTodayWord(mDb);
        System.out.println("Количество слов которые нужно повторить сегодня: " + countRepeatWords);

        getNewWord(mDb, arrayList, sumNewWord);
        showWord();
        System.out.println(arrayList.toString());

        getAnalogAnswer(mDb,arrayList.get(3));


        try {
            mBtnCheckAnswer.setOnClickListener(v -> {
                if (mInputEnAnswer.length() < 1) toast("Введите ответ", Toast.LENGTH_LONG);
                else if (checkAnswer(answer) | checkAnalogAnswer()) {
                    toast("Правильно", Toast.LENGTH_SHORT);
                    totalScore++;
                    System.out.println("Количество правильных ответов = " + totalScore);
                    mInputEnAnswer.setText("");
                    updateAfterTrueAnswer(mDb, arrayList, arrayList.get(1));
                    newWordsIterator++;
                    loger.info("После правильного ответа iterator = " + newWordsIterator);
                    updateNewWord();
                    updateRepeatWord();

                    finishSession();
                } else {
                    toast("Неверно, вот некоторые варианты перевода: " + "\n" + Arrays.toString(answer) + ", " + getAnalogAnswer(mDb,arrayList.get(3)).toString(), Toast.LENGTH_LONG);
                    mInputEnAnswer.setText("");
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


    private void createOneButtonAlertDialog(String content) {
        System.out.println("--- Вызван метод createOneButtonAlertDialog");
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityWordsRuEn.this);
        builder.setTitle("Поздравляем!");
        builder.setMessage(content);
        builder.setPositiveButton("OK",
                (dialog, which) -> {
                    Intent intent = new Intent(getApplicationContext(), ActivityWords.class);
                    startActivity(intent);
                });
        builder.show();
    }

    private boolean checkAnswer(String[] arr) {
        System.out.println("--- Вызван метод checkAnswer() значение на входе  = " + Arrays.toString(arr));
        boolean temp = false;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].contentEquals(mInputEnAnswer.getText())) {
                System.out.println("--- Метод checkAnswer() сравнивает " + arr[i] + " c " + mInputEnAnswer.getText());
                temp = true;
                break;
            } else temp = false;
        }
        System.out.println("--- Метод checkAnswer() вернул значение = " + temp);
        return temp;
    }

    private boolean checkAnalogAnswer (){
        System.out.println("--- Вызван метод checkAnalogAnswer() значение на входе  = ");
        ArrayList<String> tempArr = getAnalogAnswer(mDb,arrayList.get(3));
        boolean temp = false;
        for (int i =0; i<tempArr.size(); i++){
            if (tempArr.get(i).contentEquals(mInputEnAnswer.getText())) {
                temp = true;
                break;
            }
        }
        return temp;
    }

    private int setSessionLength() {
        System.out.println("--- Вызван метод setSessionLength()");
        Bundle arguments = getIntent().getExtras();
        assert arguments != null;
        int mTotalWords = (int) arguments.get("select_session");
        sumNewWord = mTotalWords * 2;
        System.out.println("--- Метод setSessionLength() вернул значение = " + mTotalWords);
        return mTotalWords;
    }

    private void hideStatusBar() {
        System.out.println("--- Вызван метод hideStatusBar()");
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void initDataBaseHelper() {
        System.out.println("--- Вызван метод initDataBaseHelper()");
        DatabaseHelper mDBHelper = new DatabaseHelper(this);
        mDb = mDBHelper.getWritableDatabase();
    }

    private void showWord() {
        System.out.println("Сработал метод showWord()");
        System.out.println(arrayList.toString());
        String strRu = arrayList.get(3);
        answer = arrayList.get(1).split(", ");
        loger.info("Метод showWord уснановил згачение переменной answer = " + Arrays.toString(answer));
        mTextViewRu.setText(strRu);
        System.out.println(Arrays.toString(answer));

    }

    private void toast(String s, int time) {
        System.out.println("--- Вызван метод toast");
        Toast toastNull = Toast.makeText(ActivityWordsRuEn.this, s, time);
        toastNull.setGravity(Gravity.CENTER, 0, 0);
        toastNull.show();
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
        if (repeatWordsIterator >= countRepeatWords) {
            System.out.println("--- Сработало условие в методе updateRepeatWord()");
            System.out.println("--- repeatWordsIterator равный: " + repeatWordsIterator + " Сравнился с countRepeatWords равный:" + countRepeatWords);
            loger.info("Метод check сравнил iterator и sumWord - они равны, поэтому переменная iterator сброшена на 0");
            createOneButtonAlertDialog("Вы повторили все необходимые слова и завершили сессию. \nВы правильно перевели " + totalScore + " слов из " + sumNewWord + "\nНажмите \"ОК\" для продолжения и спланируйте новую ссесию");
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ActivityWords.class);
        startActivity(intent);
    }
}
