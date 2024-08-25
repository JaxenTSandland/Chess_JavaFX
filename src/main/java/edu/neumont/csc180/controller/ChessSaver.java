/**
 * @author jsandland
 * @createdOn 8/24/2024 at 12:44 AM
 * @projectName JaxenS_ChessJavaFX
 * @packageName edu.neumont.csc180.controller;
 */
package edu.neumont.csc180.controller;

import edu.neumont.csc180.model.Piece;

import java.util.ArrayList;

public class ChessSaver {
    private static String wKing = "", wQueen = "", wKnight = "", wRook = "", wBishop = "", wPawn = "";
    private static String bKing = "", bQueen = "", bKnight = "", bRook = "", bBishop = "", bPawn = "";

    public static boolean save(Piece[][] pieces, String username) {
        // Reset all the static variables at the beginning of the method
        wKing = "";
        wQueen = "";
        wKnight = "";
        wRook = "";
        wBishop = "";
        wPawn = "";
        bKing = "";
        bQueen = "";
        bKnight = "";
        bRook = "";
        bBishop = "";
        bPawn = "";

        for (int x = 0; x < pieces.length; x++) {
            for (int y = 0; y < pieces[1].length; y++) {
                addPieceToSave(pieces[x][y]);
            }
        }

        return SQLDatabase.saveBoard(username, wKing, wQueen, wBishop, wRook, wKnight, wPawn, bKing, bQueen, bBishop, bRook, bKnight, bPawn);

    }

    private static void addPieceToSave(Piece piece) {
        if (piece == null) return;
        String pieceLocationData = "" + piece.getPosition().x + piece.getPosition().y;
        switch (piece.getType()) {
            case KING:
                if (piece.isWhite()) wKing = pieceLocationData;
                else bKing = pieceLocationData;
                break;
            case QUEEN:
                if (piece.isWhite()) wQueen += pieceLocationData;
                else bQueen += pieceLocationData;
                break;
            case KNIGHT:
                if (piece.isWhite()) wKnight += pieceLocationData;
                else bKnight += pieceLocationData;
                break;
            case BISHOP:
                if (piece.isWhite()) wBishop += pieceLocationData;
                else bBishop += pieceLocationData;
                break;
            case ROOK:
                if (piece.isWhite()) wRook += pieceLocationData;
                else bRook += pieceLocationData;
                break;
            case PAWN:
                if (piece.isWhite()) wPawn += pieceLocationData;
                else bPawn += pieceLocationData;
                break;
        }
    }

}
