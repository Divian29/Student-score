package com.app.ScoringApi.algorithm;

public class DigitRoot {
    public static int sumDigits(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        int first = s.charAt(0) - '0';
        return first + sumDigits(s.substring(1));
    }

    // B. Digital root function
    public static int digitalRoot(String s) {
        int sum = sumDigits(s);

        if (sum < 10) {
            return sum;   // base case: single digit found
        }

        // convert sum back to string and repeat
        return digitalRoot(Integer.toString(sum));
    }

    public static void main(String[] args) {
        String input = "1234445";
        System.out.println(digitalRoot(input));   // Output: 5
    }
}
