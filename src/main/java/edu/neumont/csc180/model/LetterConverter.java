/**
 * @author jsandland
 * @createdOn 2/25/2024 at 11:54 PM
 * @projectName JaxenS_Chess
 * @packageName edu.neumont.csc180.model;
 */
package edu.neumont.csc180.model;

public class LetterConverter {

    public static String numToLetter(int number) {
        char unicodeSymbol = (char) (0x0041 + number);
        return unicodeSymbol + "";
    }

    public static int letterToNum(String letter) {
        switch(letter.toLowerCase()) {
            case "a":
                return 0;
            case "b":
                return 1;
            case "c":
                return 2;
            case "d":
                return 3;
            case "e":
                return 4;
            case "f":
                return 5;
            case "g":
                return 6;
            case "h":
                return 7;
        }
        return -1;
    }

}
