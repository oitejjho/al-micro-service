package amlan.common.utils;

public class PdpaUtils {


    private static String EMPTY = "";

    public static char EMAIL_MASKING_CHARACTER = 'X';
    public static int EMAIL_MASKING_LENGTH = 3;
    public static int EMAIL_MASKING_START_POSITION = 0;

    public static String maskEmail(String email) {
        return mask(email, EMAIL_MASKING_CHARACTER, EMAIL_MASKING_LENGTH, EMAIL_MASKING_START_POSITION);
    }

    private static String mask(String target, char maskingCharacter, int maskingLength, int maskingStartPosition) {
        if (target == null || target.equals(EMPTY)) {
            return "";
        }

        if (target.length() < maskingLength) {
            return target;
        }

        StringBuffer targetBuffer = new StringBuffer(target.replaceAll("[^a-zA-Z0-9]", ""));
        String maskString = new String(new char[maskingLength]).replace("\0", Character.toString(maskingCharacter));
        int start = maskingStartPosition;
        if (maskingStartPosition == Integer.MAX_VALUE)
            start = targetBuffer.length() - maskingLength;

        String result = targetBuffer.replace(start, (start + maskingLength), maskString).toString();
        return result;
    }
}
