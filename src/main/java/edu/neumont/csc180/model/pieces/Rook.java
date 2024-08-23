/**
 * @author jsandland
 * @createdOn 2/16/2024 at 5:51 PM
 * @projectName JaxenS_Chess
 * @packageName edu.neumont.csc150.model.pieces;
 */
package edu.neumont.csc180.model.pieces;

import edu.neumont.csc180.model.*;
import java.awt.*;
import java.util.ArrayList;

public class Rook extends Piece implements Moveable {

    private final static String ROOK_SYMBOL = PieceType.ROOK.getSymbol(false);
    private final static String SELECTED_ROOK_SYMBOL = PieceType.ROOK.getSymbol(true);
    public Rook(Point position, boolean white) {
        super(position, ROOK_SYMBOL, SELECTED_ROOK_SYMBOL, white, PieceType.ROOK);
    }

    @Override
    public ArrayList<Point> getPossibleMoves(Piece[][] pieces, boolean checkKing) {
        ArrayList<Point> allMoves = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            allMoves.addAll(moveStraight(pieces, 8, i + 1, checkKing));
        }

        if(isFirstMove()) {
            int y = 0;
            if(!isWhite()) {
                y = 7;
            }
            //Right castle
            if(getPosition().equals(new Point(7, y))) {
                Piece king = findKing(pieces);
                if(king != null && king.isFirstMove())
                for (int m = 0; m < allMoves.size(); m++) {
                    if(allMoves.get(m).equals(new Point(5, y))) {
                        if(!king.kingSafe(pieces, new Point(6, y), true)) {
                            allMoves.remove(allMoves.get(m));
                        }
                    }
                }
            }
            //Left castle
            if(getPosition().equals(new Point(0, y))) {
                Piece king = findKing(pieces);
                if(king != null && king.isFirstMove())
                for (int m = 0; m < allMoves.size(); m++) {
                    if(allMoves.get(m).equals(new Point(3, y))) {
                        if(!king.kingSafe(pieces, new Point(2, y), true)) {
                            allMoves.remove(allMoves.get(m));
                        }
                    }
                }
            }
        }

        return allMoves;
    }

}
