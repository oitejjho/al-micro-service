package amlan.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    public static final String EMAIL_VALIDATION_REGEX = "^(.+)@(.+)$";

    public static boolean isValidEmail(String input, String regex){
        if(regex == null  || regex.isEmpty())
            regex = EMAIL_VALIDATION_REGEX;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }
}
