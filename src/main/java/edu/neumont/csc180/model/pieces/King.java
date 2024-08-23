/**
 * @author jsandland
 * @createdOn 2/16/2024 at 5:58 PM
 * @projectName JaxenS_Chess
 * @packageName edu.neumont.csc180.model.pieces;
 */
package edu.neumont.csc180.model.pieces;

import edu.neumont.csc180.model.Moveable;
import edu.neumont.csc180.model.Piece;
import edu.neumont.csc180.model.PieceType;

import java.awt.*;
import java.util.ArrayList;

public class King extends Piece implements Moveable {

    private final static String KING_SYMBOL = PieceType.KING.getSymbol(false);
    private final static String SELECTED_KING_SYMBOL = PieceType.KING.getSymbol(true);
    public King(Point position, boolean white) {
        super(position, KING_SYMBOL, SELECTED_KING_SYMBOL, white, PieceType.KING);
    }

    @Override
    public ArrayList<Point> getPossibleMoves(Piece[][] pieces, boolean checkKing) {
        ArrayList<Point> allMoves = new ArrayList<>();

        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                if(x == 1 && y == 1) continue;
                Point targetPosition = new Point(x + getPosition().x - 1, y + getPosition().y - 1);
                if(!pointOnBoard(targetPosition)) continue;

                if(pieces[targetPosition.x][targetPosition.y] == null) {
                    if(kingSafe(pieces, targetPosition, checkKing)) {
                        allMoves.add(new Point(targetPosition.x, targetPosition.y));
                    }
                }
                if(validCapture(pieces[targetPosition.x][targetPosition.y], checkKing)) {
                    if(kingSafe(pieces, targetPosition, checkKing)) {
                        allMoves.add(new Point(targetPosition.x, targetPosition.y));
                    }
                }
            }
        }
        if(isFirstMove()) {
            ArrayList<Point> castleMoves = castle(pieces, checkKing);
            if(castleMoves != null && !castleMoves.isEmpty()) {
                allMoves.addAll(castleMoves);
            }
        }

        return allMoves;
    }

    private ArrayList<Point> castle(Piece[][] pieces, boolean check) {
        ArrayList<Point> castleMoves = new ArrayList<>();
        if(!isFirstMove() || getType() != PieceType.KING || getType() != PieceType.ROOK) {
            int y = 0;
            if(!isWhite()) {
                y = 7;
            }
            Piece king = pieces[4][y];
            if(king == null) return null;

            if(king.getType() == PieceType.KING && king.isFirstMove()) {
                //Check left castle
                Piece rook = pieces[0][y];
                if(rook != null && rook.getType() == PieceType.ROOK && rook.isFirstMove()) {
                    if(pieces[1][y] == null && pieces[2][y] == null && pieces[3][y] == null) {
                        int x = 2;
                        if (getType() == PieceType.ROOK) {
                            x = 3;
                        }
                        Point castleTarget = new Point(x, y);
                        if(kingSafe(pieces, castleTarget, check)) {
                            castleMoves.add(castleTarget);
                        }
                    }
                }

                //Check right castle
                rook = pieces[7][y];
                if(rook != null && rook.getType() == PieceType.ROOK && rook.isFirstMove()) {
                    if(pieces[6][y] == null && pieces[5][y] == null) {
                        int x = 6;
                        if (getType() == PieceType.ROOK) {
                            x = 5;
                        }
                        Point castleTarget = new Point(x, y);
                        if(kingSafe(pieces, castleTarget, check)) {
                            castleMoves.add(castleTarget);
                        }
                    }
                }
            } else {
                return null;
            }
            return castleMoves;
        }
        return null;
    }
}
