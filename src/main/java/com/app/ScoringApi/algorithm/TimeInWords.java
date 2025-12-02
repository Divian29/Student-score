package com.app.ScoringApi.algorithm;

import java.util.Scanner;

public class TimeInWords {
    private static final String[] numbers = {
            "zero", "one", "two", "three", "four", "five", "six", "seven", "eight",
            "nine", "ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen",
            "sixteen", "seventeen", "eighteen", "nineteen", "twenty", "twenty-one",
            "twenty-two", "twenty-three", "twenty-four", "twenty-five", "twenty-six",
            "twenty-seven", "twenty-eight", "twenty-nine"
    };

    public static String numberToWords(int n) {
        if (n >= 0 && n < numbers.length) {
            return numbers[n];
        }
        return Integer.toString(n);
    }

    public static String timeInWords(int H, int M) {
        if (H < 1 || H > 12 || M < 0 || M > 59) {
            return "Invalid input. Hours must be 1-12 and minutes 0-59.";
        }

        String result;
        if (M == 0) {
            result = numberToWords(H) + " o'clock";
        } else if (M == 15) {
            result = "quarter past " + numberToWords(H);
        } else if (M == 30) {
            result = "half past " + numberToWords(H);
        } else if (M == 45) {
            int nextHour = H == 12 ? 1 : H + 1;
            result = "quarter to " + numberToWords(nextHour);
        } else if (M < 30) {
            String minuteWord = M == 1 ? "minute" : "minutes";
            result = numberToWords(M) + " " + minuteWord + " past " + numberToWords(H);
        } else {
            int minutesTo = 60 - M;
            String minuteWord = minutesTo == 1 ? "minute" : "minutes";
            int nextHour = H == 12 ? 1 : H + 1;
            result = numberToWords(minutesTo) + " " + minuteWord + " to " + numberToWords(nextHour);
        }

        // Capitalize first letter
        return result.substring(0,1).toUpperCase() + result.substring(1);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            String[] parts = scanner.nextLine().trim().split(":");
            int H = Integer.parseInt(parts[0]);
            int M = Integer.parseInt(parts[1]);
            System.out.println(timeInWords(H, M));
        } catch (Exception e) {
            System.out.println("Invalid input. Please enter in H:M format with valid numbers.");
        } finally {
            scanner.close();
        }
    }
}
