/**
 * @author jsandland
 * @createdOn 2/22/2024 at 10:27 AM
 * @projectName JaxenS_Chess
 * @packageName edu.neumont.csc180.model;
 */
package edu.neumont.csc180.model;

public enum PieceType {
    PAWN, ROOK, BISHOP, KNIGHT, QUEEN, KING;

    public String getSymbol(boolean selected) {
        switch (name()) {
            case "PAWN":
                return selected ? "" + '\u2659': "" + '\u265F';
            case "KING":
                return selected ? "" + '\u2654': "" + '\u265A';
            case "QUEEN":
                return selected ? "" + '\u2655': "" + '\u265B';
            case "ROOK":
                return selected ? "" + '\u2656': "" + '\u265C';
            case "KNIGHT":
                return selected ? "" + '\u2658': "" + '\u265E';
            case "BISHOP":
                return selected ? "" + '\u2657': "" + '\u265D';
            default:
                return null;
        }
    }

}
