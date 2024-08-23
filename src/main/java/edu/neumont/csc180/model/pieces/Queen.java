/**
 * @author jsandland
 * @createdOn 2/16/2024 at 5:57 PM
 * @projectName JaxenS_Chess
 * @packageName edu.neumont.csc150.model.pieces;
 */
package edu.neumont.csc180.model.pieces;

import edu.neumont.csc180.model.*;

import java.awt.*;
import java.util.ArrayList;

public class Queen extends Piece implements Moveable {

    private final static String QUEEN_SYMBOL = PieceType.QUEEN.getSymbol(false);
    private final static String SELECTED_QUEEN_SYMBOL = PieceType.QUEEN.getSymbol(true);
    public Queen(Point position, boolean white) {
        super(position, QUEEN_SYMBOL, SELECTED_QUEEN_SYMBOL, white, PieceType.QUEEN);
    }
    @Override
    public ArrayList<Point> getPossibleMoves(Piece[][] pieces, boolean checkKing) {
        ArrayList<Point> allMoves = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            allMoves.addAll(moveDiagonally(pieces, 8, i + 1, checkKing));
        }
        for (int i = 0; i < 4; i++) {
            allMoves.addAll(moveStraight(pieces, 8, i + 1, checkKing));
        }
        return allMoves;

    }


}
