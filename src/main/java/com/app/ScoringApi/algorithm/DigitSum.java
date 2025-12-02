package com.app.ScoringApi.algorithm;

public class DigitSum {
    // A. Recursive function to sum digits in a string
    public static int sumDigits(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        int first = s.charAt(0) - '0';    // convert char to digit
        return first + sumDigits(s.substring(1));
    }

    public static void main(String[] args) {
        String input = "1234445123444512344451234445123444512344451234445";
        System.out.println(sumDigits(input));   // 161
    }




}
