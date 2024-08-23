/**
 * @author jsandland
 * @createdOn 2/16/2024 at 5:53 PM
 * @projectName JaxenS_Chess
 * @packageName edu.neumont.csc180.model.pieces;
 */
package edu.neumont.csc180.model.pieces;

import edu.neumont.csc180.model.*;

import java.awt.*;
import java.util.ArrayList;

public class Bishop extends Piece implements Moveable {

    private final static String BISHOP_SYMBOL = PieceType.BISHOP.getSymbol(false);
    private final static String SELECTED_BISHOP_SYMBOL = PieceType.BISHOP.getSymbol(true);
    public Bishop(Point position, boolean white) {
    super(position, BISHOP_SYMBOL, SELECTED_BISHOP_SYMBOL, white, PieceType.BISHOP);
    }

    @Override
    public ArrayList<Point> getPossibleMoves(Piece[][] pieces, boolean checkKing) {
        ArrayList<Point> allMoves = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            allMoves.addAll(moveDiagonally(pieces, 8, i + 1, checkKing));
        }

        return allMoves;
    }
}
