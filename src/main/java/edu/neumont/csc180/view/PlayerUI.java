/**
 * @author jsandland
 * @createdOn 2/27/2024 at 11:55 PM
 * @projectName JaxenS_Chess
 * @packageName edu.neumont.csc150.view;
 */
package edu.neumont.csc180.view;

import edu.neumont.csc180.model.LetterConverter;
import edu.neumont.csc180.model.*;

import java.awt.*;
import java.util.ArrayList;

public class PlayerUI {

    public static int mainMenu() {
        Console.writeLn("   _____ _                   \n" +
                "  / ____| |                  \n" +
                " | |    | |__   ___  ___ ___ \n" +
                " | |    | '_ \\ / _ \\/ __/ __|\n" +
                " | |____| | | |  __/\\__ \\__ \\\n" +
                "  \\_____|_| |_|\\___||___/___/", Console.TextColor.BLUE);
        Console.writeLn("Jaxen S.\n", Console.TextColor.WHITE);
        Console.writeLn("""
                1) Play
                2) Exit""");
        int response = Console.getIntInput("", 1, 3);
        return response;
    }
    public static Point getPointInput(String message) {
        return getPointInput(message, null, null);
    }
    public static Point getPointInput(String message, String message2) {
        return getPointInput(message, message2, null);
    }
    public static Point getPointInput(String message, String message2, ArrayList<Point> possible) {
        do {
            Console.write(message, Console.TextColor.DEFAULT);
            if(message2 != null) {
                Console.write(message2, Console.TextColor.WHITE);
            }
            String cord = Console.getStringInput("", true);
            if(cord.equals("ff")) return null;
            if(cord.isEmpty()) return new Point(-1, -1);
            Point cordPoint = cordParse(cord);
            if(cordPoint != null) {
                if(possible == null) return cordPoint;
                for (int i = 0; i < possible.size(); i++) {
                    if(cordPoint.x == possible.get(i).x && cordPoint.y == possible.get(i).y) {
                        return cordPoint;
                    }
                }
            }
        } while (true);
    }

    public static void pressAnythingToContinue() {
        Console.getStringInput("-------------------------------------------------" +
                "\nPress anything to continue\n" +
                "-------------------------------------------------", true, Console.TextColor.CYAN);
    }

    private static Point cordParse(String cord) {
        if(cord.length() != 2) {
            sendMessageLn("Invalid input", Console.TextColor.RED);
            return null; //Wrong size of input
        }
        String[] splitCord = cord.split("");
        int y = -1;
        try {
            y = Integer.parseInt(splitCord[1]) - 1;
        } catch (NumberFormatException ex) {
            sendMessageLn("Invalid input", Console.TextColor.RED);
            return null; //Y is not a number
        }
        int x = LetterConverter.letterToNum(splitCord[0]);
        if(y >= 8 || y < 0 || x >= 8 || x <= -1) {
            sendMessageLn("Out of bounds", Console.TextColor.RED);
            return null; //Invalid x or y
        }
        Point target = new Point(x, y);
        if(Piece.pointOnBoard(target)) {
            return new Point(x, y);
        } else {
            return null;
        }

    }

    public static void sendMessageLn(String message, Console.TextColor textColor) {
        Console.writeLn(message, textColor);
    }

    public static void sendMessage(String message, Console.TextColor textColor) {
        Console.write(message, textColor);
    }


    public static String formatMoveMessage(Piece piece, Point position2){
        String side = piece.isWhite() ? "White" : "Black";
        String message = side + " " + piece.getType().toString().toLowerCase() + " moved to " + LetterConverter.numToLetter(position2.x) + (position2.y + 1);
        return message;
    }

    public static void endGameResultDisplay(int win) {
        switch(win) {
            case 0:
                Console.writeLn("   _____   _             _                              _          \n" +
                        "  / ____| | |           | |                            | |         \n" +
                        " | (___   | |_    __ _  | |   ___   _ __ ___     __ _  | |_    ___ \n" +
                        "  \\___ \\  | __|  / _` | | |  / _ \\ | '_ ` _ \\   / _` | | __|  / _ \\\n" +
                        "  ____) | | |_  | (_| | | | |  __/ | | | | | | | (_| | | |_  |  __/\n" +
                        " |_____/   \\__|  \\__,_| |_|  \\___| |_| |_| |_|  \\__,_|  \\__|  \\___|\n" +
                        "                                                                   \n" +
                        "                                                                   ");
                break;
            case 1:
                Console.writeLn(" __   __                               _           _ \n" +
                    " \\ \\ / /   ___    _   _    __      __ (_)  _ __   | |\n" +
                    "  \\ V /   / _ \\  | | | |   \\ \\ __ / / | | | '_ \\  | |\n" +
                    "   | |   | (_) | | |_| |    \\ V  V /  | | | | | | |_|\n" +
                    "   |_|    \\___/   \\__,_|     \\_/\\_/   |_| |_| |_| (_)");
                break;
            case 2:
                Console.writeLn(" __   __                    _                               __\n" +
                        " \\ \\ / /   ___    _   _    | |   ___    ___    ___     _   / /\n" +
                        "  \\ V /   / _ \\  | | | |   | |  / _ \\  / __|  / _ \\   (_) | | \n" +
                        "   | |   | (_) | | |_| |   | | | (_) | \\__ \\ |  __/    _  | | \n" +
                        "   |_|    \\___/   \\__,_|   |_|  \\___/  |___/  \\___|   (_) | | \n" +
                        "                                                           \\_\\");
                break;
        }
    }
}
