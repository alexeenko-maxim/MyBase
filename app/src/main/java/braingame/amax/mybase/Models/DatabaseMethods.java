package braingame.amax.mybase.Models;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

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
}
