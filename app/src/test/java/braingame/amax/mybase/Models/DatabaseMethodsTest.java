package braingame.amax.mybase.Models;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static braingame.amax.mybase.Models.DatabaseQuery.COLLUMN_NAMES_DT;
import static braingame.amax.mybase.Models.DatabaseQuery.DATABASE_NAMES_TABLE;

public class DatabaseMethodsTest {

    @Test
    public static ArrayList<String> getWord(SQLiteDatabase mDb) {
        String dt = nowDateTime(0);
        System.out.println("--- Вызван метод getWord");
        ArrayList<String> tempArr = new ArrayList<>();
        Cursor cursor = mDb.query(
                DATABASE_NAMES_TABLE,
                null,
                COLLUMN_NAMES_DT + " LIKE ?", new String[]{"%" + dt + "%"},
                null,
                null,
                null,
                "1");
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getColumnCount(); i++) {
            tempArr.add(cursor.getString(i));
        }
        cursor.close();
        System.out.println("--- Метод getWord() вернул значение = " + tempArr);
        return tempArr;
    }

    public static String nowDateTime(int increase) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.MINUTE, increase);
        System.out.println(sdf.format(calendar.getTime()));
        return sdf.format(calendar.getTime());
    }
}