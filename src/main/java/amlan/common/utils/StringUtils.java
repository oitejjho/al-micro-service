package amlan.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    public static final String EMAIL_VALIDATION_REGEX = "^(.+)@(.+)$";
    public static final String PASSWORD_VALIDATION_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";

    public static boolean isValidEmail(String input){
        Pattern pattern = Pattern.compile(EMAIL_VALIDATION_REGEX);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

    public static boolean isValidPassword(String input){
        Pattern pattern = Pattern.compile(PASSWORD_VALIDATION_REGEX);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }
}
