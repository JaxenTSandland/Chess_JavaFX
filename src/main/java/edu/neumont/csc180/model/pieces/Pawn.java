/**
 * @author jsandland
 * @createdOn 2/16/2024 at 3:31 PM
 * @projectName JaxenS_Chess
 * @packageName edu.neumont.csc150.model;
 */
package edu.neumont.csc180.model.pieces;

import edu.neumont.csc180.model.Moveable;
import edu.neumont.csc180.model.Piece;
import edu.neumont.csc180.model.PieceType;

import java.awt.*;
import java.util.ArrayList;

public class Pawn extends Piece implements Moveable {

    private final static String PAWN_SYMBOL = PieceType.PAWN.getSymbol(false);
    private final static String SELECTED_PAWN_SYMBOL = PieceType.PAWN.getSymbol(true);
    public int movesMade;

    public Pawn(Point position, boolean white) {
        super(position, PAWN_SYMBOL, SELECTED_PAWN_SYMBOL, white, PieceType.PAWN);
        setFirstMove(true);
    }



    @Override
    public ArrayList<Point> getPossibleMoves(Piece[][] pieces, boolean checkKing) {
        ArrayList<Point> allMoves = new ArrayList<>();
        Point targetPosition = new Point(getPosition().x, getPosition().y);

        targetPosition.y += (directionModifier());
        if(pointOnBoard(targetPosition)) {
            //region Forward moves
            Piece targetPiece = pieces[targetPosition.x][targetPosition.y];
            if(targetPiece == null) {
                if(kingSafe(pieces, targetPosition, checkKing)) {
                    allMoves.add(new Point(targetPosition.x, targetPosition.y));
                }
                if(isFirstMove()) {
                    targetPosition.y += (directionModifier());
                    if(pointOnBoard(targetPosition)) {
                        targetPiece = pieces[targetPosition.x][targetPosition.y];
                        if(targetPiece == null) {
                            if(kingSafe(pieces, targetPosition, checkKing)) {
                                allMoves.add(new Point(targetPosition.x, targetPosition.y));
                            }
                        }
                    }
                }
            }
            //endregion

            //region Diagonal Moves
            targetPosition = new Point(getPosition().x, getPosition().y);
            //Check spot up and to the right
            targetPosition.y += directionModifier();
            targetPosition.x++;
            for (int i = 0; i < 2; i++) { //Check right then left diagonal
                if(pointOnBoard(targetPosition)) {
                    targetPiece = pieces[targetPosition.x][targetPosition.y];
                    if(validCapture(targetPiece, checkKing)) { //If enemy in target
                        if(kingSafe(pieces, targetPosition, checkKing)) {
                            allMoves.add(new Point(targetPosition.x, targetPosition.y));
                        }
                    } else {

                        //region En Passant
                        if(getMovesMade() == 2 && getPosition().y == 4) {
                            Point besideMe = new Point(targetPosition.x, getPosition().y); //Target x, my Y
                            Piece besidePiece = pieces[besideMe.x][besideMe.y];
                            if(validCapture(besidePiece, checkKing) && targetPiece == null && besidePiece.getType() == PieceType.PAWN) { //If enemy beside me and no one is in target
                                if(kingSafe(pieces, targetPosition, checkKing)) {
                                    allMoves.add(new Point(targetPosition.x, targetPosition.y));
                                }
                            }
                        }

                        //endregion

                    }
                    targetPosition.x -= 2;
                }
            }
            //endregion
        }



        return allMoves;
    }

    public int getMovesMade() {
        return movesMade;
    }

    public void addToMovesMade(int amount) {
        this.movesMade += amount;
    }

    private int directionModifier() {
        if(isWhite())
            return 1;
        else
            return -1;
    }

    @Override
    public void moveTo(Point moveTo) {
        setFirstMove(false);
        addToMovesMade(1);
        super.moveTo(moveTo);
    }


}
