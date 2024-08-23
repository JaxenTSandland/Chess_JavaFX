/**
 * @author jsandland
 * @createdOn 2/16/2024 at 3:55 PM
 * @projectName JaxenS_Chess
 * @packageName edu.neumont.csc180.model;
 */
package edu.neumont.csc180.model;

import edu.neumont.csc180.view.Console;

public enum OccupiedType {

    BLACK, WHITE, BLANK;


    public Console.TextColor toColor() {
        switch (name())
        {
            case "BLACK":
                return Console.TextColor.BLACK;
            case "WHITE":
                return Console.TextColor.DEFAULT;
            default:
                return Console.TextColor.RED;
        }
    }
}
