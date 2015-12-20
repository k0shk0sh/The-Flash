package com.fastaccess.tfl.helper;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by kosh20111 on 3/11/2015.
 * <p/>
 * Input Helper to validate stuff related to input fields.
 */
public class InputHelper {

    public static boolean isEmpty(String text) {
        return text == null || TextUtils.isEmpty(text) || isWhiteSpaces(text);
    }

    public static boolean isEmpty(Object text) {
        return text == null || TextUtils.isEmpty(text.toString())
                || isWhiteSpaces(text.toString());
    }

    public static boolean isEmpty(EditText text) {
        return isEmpty(text.getText().toString());
    }

    public static boolean isEmpty(TextView text) {
        return isEmpty(text.getText().toString());
    }

    public static boolean isEmpty(TextInputLayout txt) {
        return isEmpty(txt.getEditText());
    }

    public static boolean isDigit(String text) {
        return TextUtils.isDigitsOnly(text);
    }

    public static boolean isAlphaNumeric(String s) {
        String pattern = "^[a-zA-Z0-9]*$";
        return s.matches(pattern);
    }

    public static boolean isStartWithChar(String s) {
        String pattern = "^[a-zA-Z]*$";
        return s.substring(0, 1).matches(pattern);
    }

    protected static boolean isWhiteSpaces(String s) {
        return s != null && s.matches("\\s+");
    }

    public static String toNA(String value) {
        return isEmpty(value) ? "N/A" : value;
    }

    public static String formatSize(Context context, long size) {
        return Formatter.formatShortFileSize(context, size);
    }

    public static String toString(EditText editText) {
        return editText.getText().toString();
    }

    public static String toString(TextInputLayout textInputLayout) {
        return toString(textInputLayout.getEditText());
    }

}
