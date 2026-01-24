
package Config;

import java.time.LocalDate;

public class Validations {

    // ✅ AGE VALIDATION
    public static int validateAge(int age, int legalAge) {
        if (age == 0) {
            return 0; // exit trigger
        }

        if (age < 0) {
            throw new IllegalArgumentException("Age cannot be negative.");
        }

        if (age < legalAge) {
            throw new IllegalArgumentException(
                "Invalid age: must be at least " + legalAge + " years old."
            );
        }

        return age;
    }

    // ✅ CHOICE VALIDATION
    public static int validateChoice(int choice, int first, int last) {
        if (choice == 0) {
            return 0; // exit trigger
        }

        if (choice < first || choice > last) {
            throw new IllegalArgumentException(
                "Invalid choice: must be between " + first + " and " + last + "."
            );
        }

        return choice;
    }

    // ✅ CURRENT DATE
    public static String getCurrentDate() {
        return LocalDate.now().toString();
    }

    // ✅ INTEGER VALIDATION
    public static int validateInteger(int value) {
        return value; 
    }

    // ✅ STRING VALIDATION
    public static String validateString(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("Input cannot be empty.");
        }

        if (input.equals("0")) {
            return "0"; // exit trigger
        }

        if (!input.matches("[a-zA-Z ]+")) {
            throw new IllegalArgumentException(
                "Invalid input: only letters and spaces are allowed."
            );
        }

        return input.trim();
    }

    // ✅ EXIT TRIGGER (Not neccessary)
//    public static boolean isExit(Object value) {
//        if (value == null) return false;
//
//        if (value instanceof Integer && ((Integer) value) == 0) {
//            return true;
//        }
//
//        if (value instanceof String && value.equals("0")) {
//            return true;
//        }
//
//        return false;
//    }
}

