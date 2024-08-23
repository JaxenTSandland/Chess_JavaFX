/**
 * @author jsandland
 * @createdOn 2/16/2024 at 3:13 PM
 * @projectName JaxenS_Chess
 * @packageName edu.neumont.csc180.model;
 */
package edu.neumont.csc180.model;

import edu.neumont.csc180.controller.ChessManager;
import edu.neumont.csc180.model.pieces.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class Piece {

    private PieceType type;
    private Point position;
    private String symbol;

    private boolean firstMove;
    private String selectedSymbol;
    private boolean white;

    protected  Piece(Point position, String symbol, String selectedSymbol, boolean white, PieceType type) {
        setPosition(position);
        setWhite(white);
        setSymbol(symbol);
        setSelectedSymbol(selectedSymbol);
        setType(type);
        setFirstMove(true);
    }

    public PieceType getType() {
        return type;
    }

    public void setType(PieceType type) {
        this.type = type;
    }

    public boolean isWhite() {
        return white;
    }

    public void setWhite(boolean white) {
        this.white = white;
    }
    public String getSelectedSymbol() {
        return selectedSymbol;
    }

    private void setSelectedSymbol(String selectedSymbol) {
        this.selectedSymbol = selectedSymbol;
    }
    public Point getPosition() {
        return position;
    }

    private void setPosition(Point position) {
        if(position.x < 0) position.x = 0;
        if(position.y < 0) position.y = 0;
        if(position.x >= 8) position.x = 7;
        if(position.y >= 8) position.y = 7;
        this.position = position;
    }


    public String getSymbol() {
        return symbol;
    }

    protected void setSymbol(String symbol) {
        if(symbol == null || symbol.isEmpty()) {
            this.symbol = "" + '\uFE56';
        } else {
            this.symbol = symbol;
        }
    }

    public boolean isFirstMove() {
        return firstMove;
    }

    protected void setFirstMove(boolean firstMove) {
        this.firstMove = firstMove;
    }

    public boolean isOccupiedBy(Point target, ArrayList<Piece> pieces) {
        for (int a = 0; a < pieces.size(); a++) {
            Point position = pieces.get(a).getPosition();
            if(position.x == target.x && position.y == target.y) {
                return true;
            }
        }

        return false;
    }

    public static boolean pointOnBoard(Point target) {
        if(target.x >= 8 || target.x < 0 || target.y >= 8 || target.y < 0) return false;
        return true;
    }

    public void moveTo(Point moveTo) {
        setFirstMove(false);
        setPosition(moveTo);
    }

    protected boolean inPosition(Point target) {
        if(getPosition().equals(new Point(target.x, target.y))) return true;
        return false;
    }

    protected boolean isAlly(Piece piece) {
        if(piece == null) return false;
        if(piece.isWhite() == this.isWhite()) return true;
        return false;
    }

    public String colorToString(boolean cap) {
        if(isWhite()) {
            if(cap) {
                return "White";
            } else {
                return "white";
            }
        } else {
            if(cap) {
                return "Black";
            } else {
                return "black";
            }
        }
    }

    public abstract ArrayList<Point> getPossibleMoves(Piece[][] pieces, boolean checkKing);

    protected ArrayList<Point> moveDiagonally(Piece[][] pieces, int amount, int quadrant, boolean checkKing) {
        ArrayList<Point> potentialMoves = new ArrayList<>();
        Point target = new Point(getPosition().x, getPosition().y);
        for (int i = 0; i < amount; i++) {
            switch(quadrant) {
                case 1:
                    target = new Point(target.x + 1, target.y + 1);
                    break;
                case 2:
                    target = new Point(target.x - 1, target.y + 1);
                    break;
                case 3:
                    target = new Point(target.x - 1, target.y - 1);
                    break;
                case 4:
                    target = new Point(target.x + 1, target.y - 1);
                    break;
                default:
                    throw new RuntimeException("Invalid quadrant");
            }

            if(pointOnBoard(target)){
                if(possibleMove(pieces, target, checkKing)) {
                    if(kingSafe(pieces, target, checkKing)) {
                        potentialMoves.add(target);
                    }
                }
                if(pieces[target.x][target.y] != null) break;
            } else {
                break;
            }

        }
        return potentialMoves;
    }

    protected ArrayList<Point> moveStraight(Piece[][] pieces, int amount, int direction, boolean checkKing) {
        ArrayList<Point> potentialMoves = new ArrayList<>();
        for (int v = 1; v < amount + 1; v++) {
            Point target = null;
            switch(direction) {
                case 1:
                    target = new Point(getPosition().x, getPosition().y + v);
                    break;
                case 2:
                    target = new Point(getPosition().x + v, getPosition().y);
                    break;
                case 3:
                    target = new Point(getPosition().x, getPosition().y - v);
                    break;
                case 4:
                    target = new Point(getPosition().x - v, getPosition().y);
                    break;
                default:
                    throw new RuntimeException("Invalid direction");
            }

            if(pointOnBoard(target)){
                if(possibleMove(pieces, target, checkKing)) {
                    if(kingSafe(pieces, target, checkKing)) {
                        potentialMoves.add(target);
                    }
                }
                if(pieces[target.x][target.y] != null) break;
            } else {
                break;
            }

        }
        return potentialMoves;
    }

    protected boolean possibleMove(Piece[][] pieces, Point targetPosition, boolean careAboutKing) {
        if(!pointOnBoard(targetPosition)) {
            return false;
        }
        Piece target = pieces[targetPosition.x][targetPosition.y];
        if(target == null) return true;
        else if (isAlly(target)) {
            return false;
        }
        if(target.getType() == PieceType.KING && careAboutKing) {
            return false;
        }
        return !isAlly(target);
    }

    protected boolean validCapture(Piece target, boolean careAboutKing) {
        if(target == null) return false;
        if(target.getType() == PieceType.KING && careAboutKing) return false;
        if(!isAlly(target)) return true;
        return false;
    }

    //region king safe methods
    public boolean kingSafe(Piece[][] pieces, Point targetPos, boolean check) {

        if(!check) return true;

        Piece[][] testPieces = new Piece[8][8];

        for (int y = 0; y < pieces.length; y++) {
            for (int x = 0; x < pieces[0].length; x++) {
                testPieces[x][y] = pieces[x][y];
            }
        }

        testPieces[getPosition().x][getPosition().y] = null;
        Piece king = null;
        if(pieces[getPosition().x][getPosition().y] == null) {
            throw new RuntimeException("Original piece disappeared");
        } else if (getType() == PieceType.KING) {
            testPieces[targetPos.x][targetPos.y] = new King(targetPos, isWhite());
            king = testPieces[targetPos.x][targetPos.y];
        } else {
            testPieces[targetPos.x][targetPos.y] = new Pawn(targetPos, isWhite());
            //Find king
            king = findKing(testPieces);
        }
        if(king == null) throw new RuntimeException("No king found");


        ArrayList<Piece> enemies = null;
        if(isWhite()) {
            enemies = ChessManager.findBlackPieces(testPieces);
        } else {
            enemies = ChessManager.findWhitePieces(testPieces);
        }

        for (int x = 0; x < enemies.size(); x++) {
            Piece testPiece = enemies.get(x);
            if(testPiece != null && !isAlly(testPiece)) {
                ArrayList<Point> possibleMoves = testPiece.getPossibleMoves(testPieces, false);
                if(possibleMoves != null && !possibleMoves.isEmpty()) {
                    for (int i = 0; i < possibleMoves.size(); i++) {
                        Point move = possibleMoves.get(i);
                        if(move.x == king.getPosition().x && move.y == king.getPosition().y) {
                            return false;
                        }
                    }
                }

            }
        }
        return true;
    }

    protected Piece findKing(Piece[][] pieces) {
        Piece king = null;
        for (int x = 0; x < pieces.length; x++) {
            if(king != null) break;
            for (int y = 0; y < pieces[0].length; y++) {
                Piece maybeKing = pieces[x][y];
                if(maybeKing != null && maybeKing.getType() == PieceType.KING && isAlly(maybeKing)) {
                    king = maybeKing;
                    break;
                }
            }
        }
        return king;
    }
}