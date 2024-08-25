/**
 * @author jsandland
 * @createdOn 8/23/2024 at 10:32 PM
 * @projectName JaxenS_ChessJavaFX
 * @packageName edu.neumont.csc180.controller;
 */
package edu.neumont.csc180.controller;

import edu.neumont.csc180.model.Piece;
import edu.neumont.csc180.model.PieceType;
import edu.neumont.csc180.model.pieces.*;

import java.awt.*;
import java.util.ArrayList;

public class ChessLoader {
    public static Piece[][] load(String username) {
        Piece[][] pieces = new Piece[8][8];
        String wKingLoc = SQLDatabase.getColumnValue("wKing", username);
        String wQueenLoc = SQLDatabase.getColumnValue("wQueen", username);
        String wKnightLoc = SQLDatabase.getColumnValue("wKnight", username);
        String wRookLoc = SQLDatabase.getColumnValue("wRook", username);
        String wBishopLoc = SQLDatabase.getColumnValue("wBishop", username);
        String wPawnLoc = SQLDatabase.getColumnValue("wPawn", username);

        String bKingLoc = SQLDatabase.getColumnValue("bKing", username);
        String bQueenLoc = SQLDatabase.getColumnValue("bQueen", username);
        String bKnightLoc = SQLDatabase.getColumnValue("bKnight", username);
        String bRookLoc = SQLDatabase.getColumnValue("bRook", username);
        String bBishopLoc = SQLDatabase.getColumnValue("bBishop", username);
        String bPawnLoc = SQLDatabase.getColumnValue("bPawn", username);

        ArrayList<Point> points;



        addPiecesToArray(wKingLoc, pieces, PieceType.KING, true);
        addPiecesToArray(wQueenLoc, pieces, PieceType.QUEEN, true);
        addPiecesToArray(wRookLoc, pieces, PieceType.ROOK, true);
        addPiecesToArray(wKnightLoc, pieces, PieceType.KNIGHT, true);
        addPiecesToArray(wBishopLoc, pieces, PieceType.BISHOP, true);
        addPiecesToArray(wPawnLoc, pieces, PieceType.PAWN, true);

        addPiecesToArray(bKingLoc, pieces, PieceType.KING, false);
        addPiecesToArray(bQueenLoc, pieces, PieceType.QUEEN, false);
        addPiecesToArray(bRookLoc, pieces, PieceType.ROOK, false);
        addPiecesToArray(bKnightLoc, pieces, PieceType.KNIGHT, false);
        addPiecesToArray(bBishopLoc, pieces, PieceType.BISHOP, false);
        addPiecesToArray(bPawnLoc, pieces, PieceType.PAWN, false);

        return pieces;
    }

    private static ArrayList<Point> findAllPoints(String location) {
        ArrayList<Point> points = new ArrayList<>();

        for (int i = 0; i < location.length() / 2; i++) {
            Point point = new Point();
            int x = Character.getNumericValue(location.charAt(i * 2));
            int y = Character.getNumericValue(location.charAt((i * 2) + 1));
            point.x = x;
            point.y = y;
            points.add(point);
        }

        return points;
    }

    private static void addPiecesToArray(String locations, Piece[][] pieces, PieceType type, boolean white) {
        if (locations == null || locations.isEmpty()) return;

        ArrayList<Point> points = findAllPoints(locations);
        for (int i = 0; i < points.size(); i++) {
            switch (type) {
                case KING:
                    pieces[points.get(i).x][points.get(i).y] = new King(points.get(i), white);
                    break;
                case PAWN:
                    pieces[points.get(i).x][points.get(i).y] = new Pawn(points.get(i), white);
                    break;
                case QUEEN:
                    pieces[points.get(i).x][points.get(i).y] = new Queen(points.get(i), white);
                    break;
                case ROOK:
                    pieces[points.get(i).x][points.get(i).y] = new Rook(points.get(i), white);
                    break;
                case KNIGHT:
                    pieces[points.get(i).x][points.get(i).y] = new Knight(points.get(i), white);
                    break;
                case BISHOP:
                    pieces[points.get(i).x][points.get(i).y] = new Bishop(points.get(i), white);
                    break;

            }

        }
    }

    public static boolean userHasEmptySave(String username) {
        String wKingCords = SQLDatabase.getColumnValue("wKing", username);
        return wKingCords == null || wKingCords.isEmpty();
    }
}
