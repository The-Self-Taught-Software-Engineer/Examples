package codes.jakob.tstse.example.various;

import java.util.Scanner;

// ++++++++++++++++++++++++++++++++++++++++
// Java Equivalence – .equals() vs. ==
// ++++++++++++++++++++++++++++++++++++++++
@SuppressWarnings("ALL")
public class Equivalence {
    public static void main(String[] args) {
        String expectedInput = "TEST";
        String userInput = getUserInput();

        System.out.println(expectedInput == userInput);
        System.out.println(expectedInput.equals(userInput));
    }

    private static String getUserInput() {
        Scanner stdIn = new Scanner(System.in);
        return stdIn.next();
    }
}
