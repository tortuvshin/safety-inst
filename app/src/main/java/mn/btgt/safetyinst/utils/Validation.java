package mn.btgt.safetyinst.utils;

import android.widget.EditText;

/**
 * Author: Turtuvshin Byambaa.
 * Project: Safety Inst
 * URL: https://www.github.com/tortuvshin
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
