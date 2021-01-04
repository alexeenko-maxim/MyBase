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

import braingame.amax.mybase.Models.DatabaseHelper;
import braingame.amax.mybase.Models.OpenFileDialog;
import braingame.amax.mybase.R;

import static braingame.amax.mybase.Models.DB.QUERY_SELECT_MIN_EN;
import static braingame.amax.mybase.Models.DB.QUERY_SELECT_NEW_WORD;

public class ActivityWordsEnRu extends AppCompatActivity {

    private SQLiteDatabase mDb;
    Button mAddAvatar;
    TextView mTextViewEn;
    EditText mInputRuAnswer;
    Button mBtnCheckAnswer, mBtnMissWord;


    private static String answer;
    private static int countWord;
    private static ArrayList<String> row = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words_enru);
        //-
        Bundle arguments = getIntent().getExtras();
        assert arguments != null;
        final int mTotalWords = (int) arguments.get("select_session");
        countWord = mTotalWords;
        System.out.println(mTotalWords);
        //-
        DatabaseHelper mDBHelper = new DatabaseHelper(this);
        mDb = mDBHelper.getWritableDatabase();

        mAddAvatar = findViewById(R.id.btn_add_avatar);
        mTextViewEn = findViewById(R.id.textViewEn);
        mBtnCheckAnswer = findViewById(R.id.btn_check_answer);
        mInputRuAnswer = findViewById(R.id.input_ru_answer);
        mBtnMissWord = findViewById(R.id.btn_miss_word);

        getRowWithNewWord();
        System.out.println(row.toString());
        showWord();



        try {
            mAddAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                    public void onClick(View v) {
                OpenFileDialog fileDialog = new OpenFileDialog(ActivityWordsEnRu.this);
                            fileDialog.show();
                    }
                    });
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        try {
            mBtnCheckAnswer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println(answer);
                    if (mInputRuAnswer.length()<1) toast("Введите ответ");

                    else if (answer.contains(mInputRuAnswer.getText())) {

                        toast("Правильно");
                        mInputRuAnswer.setText("");
                        try {
                            countWord--;
                            Thread.sleep(500);
                            if (countWord==0) {
                                createOneButtonAlertDialog("Поздравляем!", "Вы успешно завершили запланированную сессию. Нажмите \"ОК\" для продолжения и спланируйте новую ссесию");
                            }
                            else {
                                getRowWithNewWord();
                                showWord();
                            }

                        }
                        catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                    else {
                        toast("Неверно, вот некоторые варианты перевода: " + "\n" + answer);
                        try {
                            Thread.sleep(500);
                            updateStatDown(row.get(1));
                            getRowWithNewWord();
                            showWord();
                        }
                        catch (InterruptedException e) {
                            e.printStackTrace();
                        }


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
                updateStatDown(row.get(1));
                getRowWithNewWord();
                showWord();
                System.out.println(row.toString());

            }
        });


    }

    // создает диалоговое окно с 1й кнопкой
    private void createOneButtonAlertDialog(String title, String content) {
        // объект Builder для создания диалогового окна
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityWordsEnRu.this);
        // добавляем различные компоненты в диалоговое окно
        builder.setTitle(title);
        builder.setMessage(content);
        // устанавливаем кнопку, которая отвечает за позитивный ответ
        builder.setPositiveButton("OK",
                // устанавливаем слушатель
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        // по нажатию создаем всплывающее окно с типом нажатой конпки
                        Intent intent = new Intent(getApplicationContext(), ActivityWords.class);
                        startActivity(intent);
                    }
                });
        // объект Builder создал диалоговое окно и оно готово появиться на экране
        // вызываем этот метод, чтобы показать AlertDialog на экране пользователя
        builder.show();
    }


    private void showWord() {
        String str = row.get(1) + " " + row.get(2) + " [" + row.get(4) + "]";
        mTextViewEn.setText(str);
    }

    private void toast(String s) {
        Toast toastNull = Toast.makeText(ActivityWordsEnRu.this, s, Toast.LENGTH_LONG);
        toastNull.setGravity(Gravity.CENTER, 0, 0);
        toastNull.show();
    }



    private ArrayList<String> getRowWithNewWord() {
        Cursor cursor = mDb.rawQuery(QUERY_SELECT_NEW_WORD, null);
        cursor.moveToFirst();
        for (int i = 0; i<cursor.getColumnCount(); i++) {
            row.add(cursor.getString(i));
        }
        answer = row.get(3);
        cursor.close();
        return row;
    }

    private ArrayList<String> getRowWithOldWord() {
        Cursor cursor = mDb.rawQuery(QUERY_SELECT_MIN_EN, null);
        cursor.moveToFirst();
        for (int i = 0; i<cursor.getColumnCount(); i++) {
            row.add(cursor.getString(i));
        }
        cursor.close();
        return row;
    }




    private void updateStatDown(String en) {
        int tempValue = Integer.parseInt(row.get(5));
        ContentValues cv = new ContentValues();
        cv.put("stat", tempValue-10);
        cv.put("countdown", row.get(8)+1);
        cv.put("priority", "learn");
        mDb.update("words", cv, "en = ?", new String[] { en });

    }
//
//
//    private void updateCountUp(String en) {
//        ContentValues cv = new ContentValues();
//        int tempValue = Integer.parseInt(getNotNewWord(7));
//        cv.put("countup", tempValue+1);
//        mDb.update("words", cv, "en = ?", new String[] { en });
//        System.out.println(getNotNewWord(7));
//    }


}
