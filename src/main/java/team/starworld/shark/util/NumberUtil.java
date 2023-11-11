package team.starworld.shark.util;

import java.math.BigDecimal;
import java.util.Optional;


@SuppressWarnings("UnpredictableBigDecimalConstructorCall")
public class NumberUtil {

    public static final char[] NUMBER_SUB = "₀₁₂₃₄₅₆₇₈₉".toCharArray();
    public static final char[] NUMBER_SUPER = "⁰¹²³⁴⁵⁶⁷⁸⁹".toCharArray();

    public static boolean isLong (String string) {
        return ObjectUtil.tryExecute(() -> Long.parseLong(string), Optional::isPresent);
    }

    public static boolean isInteger (String string) {
        return ObjectUtil.tryExecute(() -> Integer.parseInt(string), Optional::isPresent);
    }

    public static boolean isDouble (String string) {
        return ObjectUtil.tryExecute(() -> Double.parseDouble(string), Optional::isPresent);
    }

    public static boolean isFloat (String string) {
        return ObjectUtil.tryExecute(() -> Float.parseFloat(string), Optional::isPresent);
    }

    public static boolean isShort (String string) {
        return ObjectUtil.tryExecute(() -> Short.parseShort(string), Optional::isPresent);
    }

    @SuppressWarnings("DuplicatedCode")
    public static String numberToSub (Number number) {
        var string = BigDecimal.valueOf(number.doubleValue()).toString();
        var builder = new StringBuilder();
        for (var i = 0; i < string.length(); i ++) {
            var character = string.charAt(i);
            if (Character.toString(character).matches("[0-9]"))
                builder.append(NUMBER_SUB[Integer.parseInt(Character.toString(character))]);
            else builder.append(character);
        }
        return builder.toString();
    }

    @SuppressWarnings("DuplicatedCode")
    public static String numberToSuper (Number number) {
        var string = new BigDecimal(number.doubleValue()).toString();
        var builder = new StringBuilder();
        for (var i = 0; i < string.length(); i ++) {
            var character = string.charAt(i);
            if (Character.toString(character).matches("[0-9]"))
                builder.append(NUMBER_SUPER[Integer.parseInt(Character.toString(character))]);
            else builder.append(character);
        }
        return builder.toString();
    }

}
