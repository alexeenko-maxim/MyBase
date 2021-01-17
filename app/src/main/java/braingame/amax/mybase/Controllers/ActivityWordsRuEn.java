package braingame.amax.mybase.Controllers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

        DatabaseMethods.createWordsArray(mDb, hashMap, mTotalWords);
        nextQuestion();

        try {
            mBtnCheckAnswer.setOnClickListener(v -> {
                if (mInputEnAnswer.length() < 1) toast("Введите ответ", Toast.LENGTH_LONG);
                else if (checkAnswer(answer)) {
                    toast("Правильно", Toast.LENGTH_SHORT);
                    totalScore++;
                    System.out.println("Количество правильных ответов = " + totalScore);
                    mInputEnAnswer.setText("");
//                    updateAfterTrueAnswer(mDb, hashMap, Objects.requireNonNull(hashMap.get(iterator)).get(1));
                    iterator++;
                    System.out.println(iterator);
                    nextQuestion();
                } else {
                    toast("Неверно, вот некоторые варианты перевода: " + "\n" + Arrays.toString(answer), Toast.LENGTH_LONG);
                    mInputEnAnswer.setText("");
                    DatabaseMethods.updateStatDown(mDb, hashMap, Objects.requireNonNull(hashMap.get(iterator)).get(1));
                    iterator++;
                    nextQuestion();
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        mBtnMissWord.setOnClickListener(v -> {
            DatabaseMethods.updateStatDown(mDb, hashMap, Objects.requireNonNull(hashMap.get(iterator)).get(1));
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
        if (iterator != sumWord) {
            System.out.println("--- Метод nextQuestion() сравнил iterator и sumWord, они не равны");
            System.out.println("--- Метод nextQuestion() вызвал метод  showWord(iterator)");
            showWord(iterator);
        } else {
            iterator = 0;
            System.out.println("--- Метод nextQuestion() сравнил iterator и sumWord, они равны");
            createOneButtonAlertDialog("Вы успешно завершили запланированную сессию. \nВы правильно перевели " + totalScore + " слов из " + sumWord + "\nНажмите \"ОК\" для продолжения и спланируйте новую ссесию");
            System.out.println("--- Метод nextQuestion() вызвал метод createOneButtonAlertDialog");
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
