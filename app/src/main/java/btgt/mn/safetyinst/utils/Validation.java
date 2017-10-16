package btgt.mn.safetyinst.utils;

import android.widget.EditText;

/**
 * Created by turtuvshin on 10/16/17.
 */

public class Validation {

    public static boolean empty(EditText editText){
        if (editText.getText().toString().isEmpty()){
            editText.setError("Input empty");
            return true;
        } else
            editText.setError(null);
            return false;
    }
}
