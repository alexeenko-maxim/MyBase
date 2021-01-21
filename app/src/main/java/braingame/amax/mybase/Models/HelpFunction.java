package braingame.amax.mybase.Models;

import android.widget.EditText;

public class HelpFunction {


    public static boolean checkIsEmpty(EditText mInputCode1, EditText mInputCode2, EditText mInputCode3, EditText mInputCode4, EditText mInputCode5, EditText mInputCode6) {
        return mInputCode1.length()<1 | mInputCode2.length()<1 |mInputCode3.length()<1 |mInputCode4.length()<1 |mInputCode5.length()<1 |mInputCode6.length()<1;
    }


}
