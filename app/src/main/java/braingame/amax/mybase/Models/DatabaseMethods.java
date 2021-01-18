package braingame.amax.mybase.Models;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Objects;

public class DatabaseMethods extends DatabaseQuery{

    public static ArrayList<String> createArrayWithNewWord(SQLiteDatabase mDb) {
        System.out.println("--- Вызван метод createArrayWithNewWord()");
        ArrayList<String> tempArr = new ArrayList<>();
        Cursor cursor = mDb.rawQuery(QUERY_SELECT_NEW_WORD, null);
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getColumnCount(); i++) {
            tempArr.add(cursor.getString(i));
        }
        cursor.close();
        System.out.println("--- Метод createArrayWithNewWord() вернул значение = " + tempArr);
        return tempArr;
    }

    public static ArrayList<String> createArrayWithOldWord(SQLiteDatabase mDb) {
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

    public static void createWordsArray(SQLiteDatabase mDb, HashMap<Integer, ArrayList<String>> hashMap, int sizeArray) {
        System.out.println("--- Метод createWordsArray получил на вход значение = " + sizeArray);

        for (int i = 0, j = sizeArray; i < sizeArray; i++, j++) {
            hashMap.put(i, DatabaseMethods.createArrayWithNewWord(mDb));
            hashMap.put(j, DatabaseMethods.createArrayWithOldWord(mDb));
        }
        System.out.println("--- Метод createWordsArray отдает значение: " + hashMap.toString());

    }

    public static void updateStatDown(SQLiteDatabase mDb, HashMap<Integer, ArrayList<String>> hashMap, String en) {
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

    public static void updateAfterTrueAnswer(SQLiteDatabase mDb, ArrayList arrayList, String en) {
        System.out.println("--- Метод updateAfterTrueAnswer получил на вход значение = " + en);
        int tempValue1 = Integer.parseInt(String.valueOf(arrayList.get(5))) + 10;
        int tempValue2 = Integer.parseInt(String.valueOf(arrayList.get(7))) + 1;
        int increase = Integer.parseInt(String.valueOf(arrayList.get(7)));
        System.out.println(tempValue1);
        System.out.println(tempValue2);
        System.out.println(increase);
        ContentValues cv = new ContentValues();
        cv.put(COLLUMN_NAMES_STAT, tempValue1);
        cv.put(COLLUMN_NAMES_COUNTUP, tempValue2);
        cv.put(COLLUMN_NAMES_PRIORITY, "learn");
        cv.put(COLLUMN_NAMES_DT, nowDateTime(increase*24));
        try {
            mDb.update(TABLE_NAME,
                    cv,
                    "en = ?", new String[]{en});
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateAfterFalseAnswer(SQLiteDatabase mDb, ArrayList arrayList, String en) {
        System.out.println("--- Метод updateStatDown получил на вход значение = " + en);
        int countup = Integer.parseInt(String.valueOf(arrayList.get(7)));

        if (countup!=0){

        }
        int tempValue1 = Integer.parseInt(String.valueOf(arrayList.get(5))) - 10;
        int tempValue2 = Integer.parseInt(String.valueOf(arrayList.get(7))) - 1;
        System.out.println(tempValue1);
        System.out.println(tempValue2);
        ContentValues cv = new ContentValues();
        cv.put(COLLUMN_NAMES_STAT, tempValue1);
        cv.put(COLLUMN_NAMES_COUNTUP, tempValue2);
        cv.put(COLLUMN_NAMES_PRIORITY, "learn");
        cv.put(COLLUMN_NAMES_DT, nowDateTime(0));
        try {
            mDb.update(TABLE_NAME,
                    cv,
                    "en = ?", new String[]{en});
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String getTotalWordInDB(SQLiteDatabase mDb) {
        Cursor cursor = mDb.rawQuery(QUERY_GET_TOTAL_WORDS, null);
        ArrayList<String> tempArr = new ArrayList<>();
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getColumnCount(); i++) {
            tempArr.add(cursor.getString(i));
        }
        cursor.close();
        return tempArr.get(0);
    }

    public static String getTotalYourWords(SQLiteDatabase mDb) {
        Cursor cursor = mDb.rawQuery(QUERY_GET_TOTAL_YOUR_WORDS, null);
        ArrayList<String> tempArr = new ArrayList<>();
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getColumnCount(); i++) {
            tempArr.add(cursor.getString(i));
        }
        cursor.close();
        return tempArr.get(0);
    }

    public static void addWordInDB(SQLiteDatabase mDb, String en, String trans, String ru, String part_of_speech) {
        ContentValues cv = new ContentValues();
        cv.put(COLLUMN_NAMES_EN, en);
        cv.put(COLLUMN_NAMES_TRANS, trans);
        cv.put(COLLUMN_NAMES_RU, ru);
        cv.put(COLLUMN_NAMES_PART_OF_SPEECH, part_of_speech);
        try {
            mDb.insert(TABLE_NAME, null, cv);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String nowDateTime(int increase) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.HOUR, +increase);
        System.out.println(sdf.format(calendar.getTime()));
        return sdf.format(calendar.getTime());
    }

    public static void getTodayWord(SQLiteDatabase mDb, ArrayList arrayList) {
        String dt = nowDateTime(0);
        System.out.println("--- Вызван метод getTodayWord");
        System.out.println("--- dt = " + dt);
        Cursor cursor = mDb.query(
                true,
                TABLE_NAME,
                null,
                COLLUMN_NAMES_DT + " <= ? AND dt != ?", new String[] {dt,""},
                null,
                null,
                null,
                "1");
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getColumnCount(); i++) {
            arrayList.add(cursor.getString(i));
        }
        cursor.close();
        System.out.println("--- Метод getTodayWord установил значение tempArr = " + arrayList);

    }

    public static void getNewWord(SQLiteDatabase mDb, ArrayList arrayList, int newWordsIterator) {
        if (newWordsIterator<=10) {
            String dt = nowDateTime(0);
            System.out.println("--- Вызван метод getNewWord");
            System.out.println("--- dt = " + dt);

            Cursor cursor = mDb.query(
                    false,
                    TABLE_NAME,
                    null,
                    COLLUMN_NAMES_PRIORITY + " = ?", new String[] {"new"},
                    null,
                    null,
                    "random()",
                    "1");
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                arrayList.add(cursor.getString(i));
            }
            cursor.close();
            System.out.println("--- Метод getNewWord вернул значение = " + arrayList);
        }


    }

    public static int getCountTodayWord(SQLiteDatabase mDb) {
        String dt = nowDateTime(0);
        System.out.println("--- Вызван метод getWord");
        System.out.println("--- dt = " + dt);
        ArrayList<String> tempArr = new ArrayList<>();
        Cursor cursor = mDb.rawQuery(QUERY_GET_TOTAL_TODAY_WORDS, new String[] {dt});
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getColumnCount(); i++) {
            tempArr.add(cursor.getString(i));
        }
        cursor.close();
        System.out.println("--- Метод getWord() вернул значение = " + tempArr);
        return Integer.parseInt(tempArr.get(0));
    }


}
