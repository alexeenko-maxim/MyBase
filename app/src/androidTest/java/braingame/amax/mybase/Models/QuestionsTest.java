package braingame.amax.mybase.Models;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import junit.framework.TestCase;

import java.util.ArrayList;

import static braingame.amax.mybase.Models.DB.QUERY_SELECT_NEW_WORD;

class QuestionsTest extends TestCase {

    private SQLiteDatabase mDb;

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
}