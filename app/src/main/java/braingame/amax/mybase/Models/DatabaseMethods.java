package braingame.amax.mybase.Models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
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

    public static void updateStatUp(SQLiteDatabase mDb, HashMap<Integer, ArrayList<String>> hashMap, String en) {
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
            mDb.insert(DATABASE_NAMES_TABLE, null, cv);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
