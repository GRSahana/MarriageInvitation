package com.ubikasoftwares.marriageinvitation.bin;

import android.text.TextUtils;
import android.util.Patterns;

public class Validation {

    static String regexname = "^[\\p{L} .'-]+$";
    static String regexphone = "^[1-9]{1}[0-9]{9}$";
    static String regexNumber = "[0-9]+";
    static String regexGst = "\\d{2}[A-Z]{5}\\d{4}[A-Z]{1}[1-9A-Z]{1}[Z]{1}[A-Z\\d]{1}";
    static String regexPin = "(\\d{6})";
    static String regexWeb = "^(http:\\/\\/|https:\\/\\/)?(www.)?([a-zA-Z0-9]+).[a-zA-Z0-9]*.[a-z]{3}.?([a-z]+)?$";

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static boolean isValidName(String name){
        return name.matches(regexname);
    }

    public static boolean isValidPhone(String phone){
        return phone.matches(regexphone);
    }

    public static boolean isValidGst(String gst){
        return gst.matches(regexGst);
    }

    public static boolean isValidPin(String pin){
        return pin.matches(regexPin);
    }

    public static boolean isValidWeb(String web){
        return web.matches(regexWeb);
    }

    public static boolean isValidNumber(String number){
        return number.matches(regexNumber);
    }



}
