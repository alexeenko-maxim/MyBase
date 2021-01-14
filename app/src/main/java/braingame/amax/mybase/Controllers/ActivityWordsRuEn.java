package braingame.amax.mybase.Controllers;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
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

import braingame.amax.mybase.Models.DatabaseHelper;
import braingame.amax.mybase.Models.DatabaseMethods;
import braingame.amax.mybase.R;

import static braingame.amax.mybase.Models.DatabaseQuery.COLLUMN_NAMES_COUNTDOWN;
import static braingame.amax.mybase.Models.DatabaseQuery.COLLUMN_NAMES_EN;
import static braingame.amax.mybase.Models.DatabaseQuery.COLLUMN_NAMES_PRIORITY;
import static braingame.amax.mybase.Models.DatabaseQuery.COLLUMN_NAMES_RU;
import static braingame.amax.mybase.Models.DatabaseQuery.COLLUMN_NAMES_STAT;
import static braingame.amax.mybase.Models.DatabaseQuery.COLLUMN_NAMES_TRANS;
import static braingame.amax.mybase.Models.DatabaseQuery.DATABASE_NAMES_TABLE;
import static braingame.amax.mybase.Models.DatabaseQuery.QUERY_SELECT_MIN_EN;
import static braingame.amax.mybase.Models.DatabaseQuery.USERNAME;

public class ActivityWordsRuEn extends AppCompatActivity {

    SharedPreferences mUserName;
    private SQLiteDatabase mDb;
    TextView mTextViewRu, mUser, mBackToMenu, mExit;
    EditText mInputEnAnswer;
    Button mBtnCheckAnswer, mBtnMissWord;

    private static String[] answer;
    private static int sumWord;
    private static int iterator = 0;
    private static int totalScore = 0;
    private static final HashMap<Integer, ArrayList<String>> hashMap = new HashMap<>();


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

        createWordsArray(mTotalWords);
        nextQuestion();

        try {
            mBtnCheckAnswer.setOnClickListener(v -> {
                if (mInputEnAnswer.length() < 1) toast("Введите ответ", Toast.LENGTH_LONG);
                else if (checkAnswer(answer)) {
                    toast("Правильно", Toast.LENGTH_SHORT);
                    totalScore++;
                    System.out.println("Количество правильных ответов = " + totalScore);
                    mInputEnAnswer.setText("");
                    updateStatUp(Objects.requireNonNull(hashMap.get(iterator)).get(1));
                    iterator++;
                    System.out.println(iterator);
                    nextQuestion();
                } else {
                    toast("Неверно, вот некоторые варианты перевода: " + "\n" + Arrays.toString(answer), Toast.LENGTH_LONG);
                    mInputEnAnswer.setText("");
                    updateStatDown(Objects.requireNonNull(hashMap.get(iterator)).get(1));
                    iterator++;
                    nextQuestion();
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
            nextQuestion();
        });

    }

    private int setSessionLength() {
        System.out.println("--- Вызван метод setSessionLength()");
        Bundle arguments = getIntent().getExtras();
        assert arguments != null;
        int mTotalWords = (int) arguments.get("select_session");
        sumWord = mTotalWords * 2;
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



    private ArrayList<String> createArrayWithOldWord() {
        System.out.println("--- Вызван метод createArrayWithOldWord()");
        ArrayList<String> tempArr = new ArrayList<>();
        Cursor cursor = mDb.rawQuery(QUERY_SELECT_MIN_EN, null);
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getColumnCount(); i++) {
            tempArr.add(cursor.getString(i));
        }
        cursor.close();
        System.out.println("--- Метод createArrayWithOldWord() вернул значение = " + tempArr);
        return tempArr;
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

    private ArrayList<String> createAnalogAnswersArray(String question) {
        System.out.println("--- Метод createMoreAnswers получил на вход значение = " + question);
        ArrayList<String> tempArr = new ArrayList<>();
        Cursor cursor = mDb.query(false,
                DATABASE_NAMES_TABLE,
                new String[]{COLLUMN_NAMES_EN, COLLUMN_NAMES_TRANS},
                COLLUMN_NAMES_RU + " LIKE ?",
                new String[]{question + "%"},
                null,
                null,
                null,
                "5");
        cursor.moveToFirst();

        while (cursor.moveToNext()) {
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                tempArr.add(cursor.getString(i));
            }
            cursor.moveToNext();
        }
        cursor.close();
        System.out.println("--- Метод createMoreAnswers отдает значение: " + tempArr.toString());
        return tempArr;
    }

    private void createWordsArray(int sizeArray) {
        System.out.println("--- Метод createWordsArray получил на вход значение = " + sizeArray);

        for (int i = 0, j = sizeArray; i < sizeArray; i++, j++) {
            hashMap.put(i, DatabaseMethods.createArrayWithNewWord(mDb));
            hashMap.put(j, createArrayWithOldWord());
        }
        System.out.println("--- Метод createWordsArray отдает значение: " + hashMap.toString());

    }

    private void showWord(int index) {
        System.out.println("--- Метод showWord получил на вход значение = " + index);
        String str = Objects.requireNonNull(hashMap.get(index)).get(3);
        String[] temp = Objects.requireNonNull(hashMap.get(index)).get(3).split(", ");
        String question = temp[0];
        answer = (Objects.requireNonNull(hashMap.get(index)).get(1) + ", " + Objects.requireNonNull(hashMap.get(index)).get(2)).split(", ");
        System.out.println("--- Метод showWord задал значение переменной answer = " + Arrays.toString(answer));
        mTextViewRu.setText(str);
        System.out.println("--- Метод showWord установил значение поля mTextViewRu = " + str);
    }

    private void nextQuestion() {
        System.out.println("--- Вызван метод nextQuestion()");
        if (iterator != sumWord){
            System.out.println("--- Метод nextQuestion() сравнил iterator и sumWord, они не равны");
            System.out.println("--- Метод nextQuestion() вызвал метод  showWord(iterator)");
            showWord(iterator);
        }

        else {
            iterator = 0;
            System.out.println("--- Метод nextQuestion() сравнил iterator и sumWord, они равны");
            createOneButtonAlertDialog("Вы успешно завершили запланированную сессию. \nВы правильно перевели " + totalScore + " слов из " + sumWord + "\nНажмите \"ОК\" для продолжения и спланируйте новую ссесию");
            System.out.println("--- Метод nextQuestion() вызвал метод createOneButtonAlertDialog");
        }
    }

    private void updateStatDown(String en) {
        System.out.println("--- Метод updateStatDown получил на вход значение = " + en);
        int tempValue1 = Integer.parseInt(Objects.requireNonNull(hashMap.get(0)).get(5)) - 10;
        int tempValue2 = Integer.parseInt(Objects.requireNonNull(hashMap.get(0)).get(8)) + 1;
        System.out.println(tempValue1);
        System.out.println(tempValue2);
        ContentValues cv = new ContentValues();
        cv.put(COLLUMN_NAMES_STAT, tempValue1);
        cv.put(COLLUMN_NAMES_COUNTDOWN, tempValue2);
        cv.put(COLLUMN_NAMES_PRIORITY, "learn");
        try {
            mDb.update("words", cv, "en = ?", new String[]{en});
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateStatUp(String en) {
        System.out.println("--- Метод updateStatDown получил на вход значение = " + en);
        int tempValue1 = Integer.parseInt(Objects.requireNonNull(hashMap.get(0)).get(5)) + 10;
        int tempValue2 = Integer.parseInt(Objects.requireNonNull(hashMap.get(0)).get(7)) + 1;
        System.out.println(tempValue1);
        System.out.println(tempValue2);
        ContentValues cv = new ContentValues();
        cv.put("stat", tempValue1);
        cv.put("countup", tempValue2);
        cv.put("priority", "learn");
        try {
            mDb.update("words", cv, "en = ?", new String[]{en});
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

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

    private void toast(String s, int time) {
        System.out.println("--- Вызван метод toast");
        Toast toastNull = Toast.makeText(ActivityWordsRuEn.this, s, time);
        toastNull.setGravity(Gravity.CENTER, 0, 0);
        toastNull.show();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ActivityWords.class);
        startActivity(intent);
    }

}
