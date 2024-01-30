package typingtest;

import java.util.Arrays;
import java.util.List;

/**
 * Adapted from Utilities from HW2 in COMP128
 * @author Shilad Sen, Bret Jackson
 */
public class Utils {
    /**
     * Splits a string into a list of words
     * @param string
     * @return
     */
    public static List<String> splitWords(String string) {
        return Arrays.asList(string.replaceAll("\\p{P}", "").toLowerCase().split("\\s+"));
    }
}
