package frameworks.utils;

import android.text.TextUtils;
import android.util.Patterns;

/**
 * Created by sandeepgoyal on 12/05/18.
 */

public class Validator {
    public static boolean validateEmail(String email) {
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    public static boolean validateMobile(String mobile) {
        return Patterns.PHONE.matcher(mobile).matches();
    }
}
