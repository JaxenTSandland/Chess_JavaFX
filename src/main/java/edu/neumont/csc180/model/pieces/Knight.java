/**
 * @author jsandland
 * @createdOn 2/16/2024 at 4:24 PM
 * @projectName JaxenS_Chess
 * @packageName edu.neumont.csc150.model;
 */
package edu.neumont.csc180.model.pieces;

import edu.neumont.csc180.model.Moveable;
import edu.neumont.csc180.model.Piece;
import edu.neumont.csc180.model.PieceType;

import java.awt.*;
import java.util.ArrayList;

public class Knight extends Piece implements Moveable {

    private final static String KNIGHT_SYMBOL = PieceType.KNIGHT.getSymbol(false);
    private final static String SELECTED_KNIGHT_SYMBOL = PieceType.KNIGHT.getSymbol(true);
    public Knight(Point position, boolean white) {
        super(position, KNIGHT_SYMBOL, SELECTED_KNIGHT_SYMBOL, white, PieceType.KNIGHT);
    }
    @Override
    public ArrayList<Point> getPossibleMoves(Piece[][] pieces, boolean checkKing) {
        ArrayList<Point> allMoves = new ArrayList<>();

        int yMod = 1, xMod = 1;
        for (int i = 0; i < 4; i++) {
            switch(i) {
                case 1:
                    xMod = -1;
                    break;
                case 2:
                    yMod = -1;
                    break;
                case 3:
                    xMod = 1;
                    break;
            }

            Point targetPosition = new Point(getPosition().x + (2 * xMod), getPosition().y + (yMod));
            if(possibleMove(pieces, targetPosition, checkKing)) {
                if(kingSafe(pieces, targetPosition, checkKing)) {
                    allMoves.add(targetPosition);
                }
            }
            targetPosition = new Point(getPosition().x + (xMod), getPosition().y + (2 * yMod));
            if(possibleMove(pieces, targetPosition, checkKing)) {
                if(kingSafe(pieces, targetPosition, checkKing)) {
                    allMoves.add(targetPosition);
                }
            }

        }
        return allMoves;
    }
}
