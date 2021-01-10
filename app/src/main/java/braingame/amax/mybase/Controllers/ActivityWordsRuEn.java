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
import android.view.View;
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

import braingame.amax.mybase.Models.DB;
import braingame.amax.mybase.Models.DatabaseHelper;
import braingame.amax.mybase.R;

import static braingame.amax.mybase.Models.DB.COLLUMN_NAMES_EN;
import static braingame.amax.mybase.Models.DB.COLLUMN_NAMES_RU;
import static braingame.amax.mybase.Models.DB.COLLUMN_NAMES_TRANS;
import static braingame.amax.mybase.Models.DB.QUERY_SELECT_MIN_EN;
import static braingame.amax.mybase.Models.DB.QUERY_SELECT_NEW_WORD;
import static braingame.amax.mybase.Models.DB.USERNAME;

public class ActivityWordsRuEn extends AppCompatActivity {

    private static final Logger loger = Logger.getLogger(ActivityWordsRuEn.class.getName());


    SharedPreferences mUserName;
    private SQLiteDatabase mDb;
    Button mAddAvatar;
    TextView mTextViewRu, mUser, mBackToMenu, mExit;
    EditText mInputEnAnswer;
    Button mBtnCheckAnswer, mBtnMissWord;


    private static String[] answer;
    private static String[] question;
    private static int sumWord;
    private static int iterator = 0;
    private static int totalScore = 0;
    private static final HashMap<Integer, ArrayList<String>> hashMap = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words_ruen);

        mUserName = getSharedPreferences(DB.USERNAME, MODE_PRIVATE);
        Bundle arguments = getIntent().getExtras();
        //-Скрытие строки состояния-//
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //*
        assert arguments != null;
        int mTotalWords = (int) arguments.get("select_session");
        sumWord = mTotalWords * 2;
        System.out.println("Выбрана ссесия - " + mTotalWords);
        //-
        DatabaseHelper mDBHelper = new DatabaseHelper(this);
        mDb = mDBHelper.getWritableDatabase();

        //        mAddAvatar = findViewById(R.id.btn_add_avatar);
        //        mAddAvatar.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View v) {
        //                OpenFileDialog fileDialog = new OpenFileDialog(ActivityWordsRuEn.this);
        //                fileDialog.show();
        //            }
        //        });

        mTextViewRu = findViewById(R.id.textViewRu);
        mBtnCheckAnswer = findViewById(R.id.btn_check_answer);
        mInputEnAnswer = findViewById(R.id.input_en_answer);
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


        try {
            mBtnCheckAnswer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mInputEnAnswer.length() < 1)
                        toast("Введите ответ", Toast.LENGTH_LONG);
                    else if (checkAnswer(answer)) {
                        toast("Правильно", Toast.LENGTH_SHORT);
                        totalScore++;
                        loger.info("Количество правильных ответов = " + totalScore);
                        mInputEnAnswer.setText("");
                        updateStatUp(Objects.requireNonNull(hashMap.get(iterator)).get(1));
                        iterator++;
                        System.out.println(iterator);
                        check();
                    } else {
                        toast("Неверно, вот некоторые варианты перевода: " + "\n" + Arrays.toString(answer), Toast.LENGTH_LONG);
                        mInputEnAnswer.setText("");
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
                toast("Вот некоторые варианты перевода: " + "\n" + Arrays.toString(answer), Toast.LENGTH_LONG);
                iterator++;
                check();
            }
        });

    }

    private ArrayList<String> createArrayWithNewWord() {
        ArrayList<String> tempArr = new ArrayList<>();
        Cursor cursor = mDb.rawQuery(QUERY_SELECT_NEW_WORD, null);
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getColumnCount(); i++) {
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
        for (int i = 0; i < cursor.getColumnCount(); i++) {
            tempArr.add(cursor.getString(i));
        }
        cursor.close();
        System.out.println(tempArr);

        return tempArr;
    }

    private boolean checkAnswer(String[] arr) {
        loger.info("Метод checkAnswer получил на вход массив = " + Arrays.toString(arr));

        System.out.println("Згачение на входе " + Arrays.toString(arr));
        boolean temp = false;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].contentEquals(mInputEnAnswer.getText())) {
                System.out.println("Сравниваю " + arr[i] + " c " + mInputEnAnswer.getText());
                temp = true;
                break;
            } else temp = false;
        }
        return temp;
    }

        private ArrayList<String> createMoreAnswers (String[] question){
            loger.info("Метод createMoreAnswers получил на вход значение " + Arrays.toString(question));
            ArrayList<String> tempArr = new ArrayList<>();

            Cursor cursor = mDb.query(false,
                    DB.DATABASE_NAMES_TABLE,
                    new String[] {COLLUMN_NAMES_EN, COLLUMN_NAMES_TRANS},
                    COLLUMN_NAMES_RU + " LIKE ?",
                    new String[] { question[0] + "%" },
                    null,
                    null,
                    null,
                    "5");
            cursor.moveToFirst();

            while (cursor.moveToNext()){
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    tempArr.add(cursor.getString(i));
                    loger.info("Добавлен элемент" + tempArr.get(i) );
                }
                cursor.moveToNext();
            }



//
//            for (int i = 0; i < cursor.getColumnCount(); i++) {
//                tempArr.add(cursor.getString(i));
//                loger.info("Добавлен элемент" + tempArr.get(i) );
//            }

            cursor.close();
            loger.info("Метод createMoreAnswers отдает значение: " + tempArr.toString());
            return tempArr;
        }

        private void createWordsArray ( int sizeArray){
            for (int i = 0, j = sizeArray; i < sizeArray; i++, j++) {
                hashMap.put(i, createArrayWithNewWord());
                hashMap.put(j, createArrayWithOldWord());
            }
            System.out.println(hashMap);
        }

        private void showWord ( int index){
            String str = Objects.requireNonNull(hashMap.get(index)).get(3);
            question = Objects.requireNonNull(hashMap.get(index)).get(3).split(", ");
            answer = (Objects.requireNonNull(hashMap.get(index)).get(1) + " " + Objects.requireNonNull(hashMap.get(index)).get(2)).split(", ");
            mTextViewRu.setText(str);
            createMoreAnswers(question);

            System.out.println(Arrays.toString(answer));
        }

        private void check () {
            if (iterator != sumWord) showWord(iterator);
            else {
                iterator = 0;
                createOneButtonAlertDialog("Вы успешно завершили запланированную сессию. \nВы правильно перевели " + totalScore + " слов из " + sumWord + "\nНажмите \"ОК\" для продолжения и спланируйте новую ссесию");
            }
            System.out.println("Iterator = " + iterator + " / sumWord = " + sumWord);
        }

        private void updateStatDown (String en){
            int tempValue1 = Integer.parseInt(Objects.requireNonNull(hashMap.get(0)).get(5)) - 10;
            int tempValue2 = Integer.parseInt(Objects.requireNonNull(hashMap.get(0)).get(8)) + 1;
            System.out.println(tempValue1);
            System.out.println(tempValue2);
            ContentValues cv = new ContentValues();
            cv.put("stat", tempValue1);
            cv.put("countdown", tempValue2);
            cv.put("priority", "learn");
            try {
                mDb.update("words", cv, "en = ?", new String[]{en});
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void updateStatUp (String en){
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

        private void createOneButtonAlertDialog (String content){
            AlertDialog.Builder builder = new AlertDialog.Builder(ActivityWordsRuEn.this);
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

        private void toast (String s,int time){
            Toast toastNull = Toast.makeText(ActivityWordsRuEn.this, s, time);
            toastNull.setGravity(Gravity.CENTER, 0, 0);
            toastNull.show();
        }

        @Override
        public void onBackPressed () {
            Intent intent = new Intent(this, ActivityWords.class);
            startActivity(intent);
        }

    }
