/**
 * @author jsandland
 * @createdOn 2/16/2024 at 12:52 PM
 * @projectName JaxenS_Chess
 * @packageName edu.neumont.csc150.view;
 */
package edu.neumont.csc180.view;

import edu.neumont.csc180.model.LetterConverter;
import edu.neumont.csc180.model.OccupiedType;
import edu.neumont.csc180.model.Piece;

import java.awt.*;
import java.util.ArrayList;

public abstract class BoardCreator {

    private static final Console.TextColor POSSIBLE_MOVE_COLOR = Console.TextColor.GREEN;
    private static final Console.TextColor WHITE_MOVE_COLOR = Console.TextColor.BLUE;

    private static final Console.TextColor BLACK_MOVE_COLOR = Console.TextColor.RED;

    private final static String BLANK_SQUARE = "" + '\u25AC';

    //region linesUI
    private final static String HORIZONTAL_LINE = "" + '\u2501';
    private final static String VERTICAL_LINE = "" + '\u2503';
    private final static String BOTTOM_LEFT_CORNER = "" + '\u2517';
    private final static String BOTTOM_RIGHT_CORNER = "" + '\u251B';
    private final static String TOP_LEFT_CORNER = "" + '\u250F';
    private final static String TOP_RIGHT_CORNER = "" + '\u2513';
    //endregion
    private final static int HORIZONTAL_EDGE_LENGTH = 25;

    public static void drawBoard(Piece[][] pieces, ArrayList<String> messages, ArrayList<String> movePieceLog) {
        drawBoard(pieces, null, null, messages, movePieceLog, null, null);
    }

    public static void drawBoard(Piece[][] pieces, ArrayList<Point> possibleMoves, Point selectedSpot) {
        drawBoard(pieces, possibleMoves, selectedSpot, null, null, null, null);
    }

    public static void drawBoard(Piece[][] pieces, ArrayList<String> moveLog, ArrayList<String> movePieceLog,
                                 Point[] whiteLog, Point[] blackLog) {
        drawBoard(pieces, null, null, moveLog, movePieceLog, whiteLog, blackLog);
    }

    public static void drawBoard(Piece[][] pieces, ArrayList<Point> possibleMoves, Point selectedSpot,
                                 ArrayList<String> moveLog, ArrayList<String> movePieceLog,
                                 Point[] whiteLog, Point[] blackLog) {

        for (int y = 7; y >= 0; y--) {
            if(y == 7) {
                Console.write("  ");
                createTopBottomEdge(true);
            }
            for (int x = 0; x < 8; x++) {
                if (x == 0) {
                    Console.write(" " + (y + 1) + "", Console.TextColor.WHITE);
                    Console.write(VERTICAL_LINE + " ");
                }

                String inhabitedSymbol = "";

                OccupiedType type = OccupiedType.BLANK;

                Piece pieceInSpot = pieces[x][y];
                if(pieceInSpot != null) {
                    inhabitedSymbol = pieceInSpot.getSymbol();
                    type = pieceInSpot.isWhite() ? OccupiedType.WHITE : OccupiedType.BLACK;
                }

                if(inhabitedSymbol == "") {
                    inhabitedSymbol = BLANK_SQUARE;
                    if(x % 2 == y % 2) {
                        type = OccupiedType.WHITE;
                    } else {
                        type = OccupiedType.BLACK;
                    }
                }
                Console.TextColor symbolColor = type.toColor();


                //Turn spot blue if it is possible to move to by selected piece
                if(possibleMoves != null && !possibleMoves.isEmpty()) {
                    for (int p = 0; p < possibleMoves.size(); p++) {
                        if(possibleMoves.get(p).x == x && possibleMoves.get(p).y == y) {
                            Piece selectedPiece = pieces[selectedSpot.x][selectedSpot.y];
                            if(inhabitedSymbol != BLANK_SQUARE && pieceInSpot.isWhite() == selectedPiece.isWhite()) {
                                throw new RuntimeException("FRIENDLY FIRE");
                            }
                            if(selectedPiece.isWhite()) {
                                symbolColor = POSSIBLE_MOVE_COLOR;
                            } else {
                                symbolColor = BLACK_MOVE_COLOR;
                            }

                            break;
                        }
                    }
                }

                if(selectedSpot != null && selectedSpot.x == x && selectedSpot.y == y) {
                    Piece selectedPiece = pieces[selectedSpot.x][selectedSpot.y];
                    if(selectedPiece != null && !selectedPiece.isWhite()) {
                        inhabitedSymbol = selectedPiece.getSelectedSymbol();
                        symbolColor = Console.TextColor.RED;
                    } else {
                        symbolColor = POSSIBLE_MOVE_COLOR;
                        inhabitedSymbol = selectedPiece.getSelectedSymbol();
                    }
                }
                if(whiteLog != null) {
                    if ((whiteLog[0].x == x && whiteLog[0].y == y) || (whiteLog[1].x == x && whiteLog[1].y == y)) {
                        symbolColor = WHITE_MOVE_COLOR;
                    }
                }
                if(blackLog != null) {
                    if((blackLog[0].x == x && blackLog[0].y == y) || (blackLog[1].x == x && blackLog[1].y == y)) {
                        symbolColor = BLACK_MOVE_COLOR;
                        if(selectedSpot != null) {
                            inhabitedSymbol = pieceInSpot.getSelectedSymbol();
                        }
                    }

                }


                Console.write(inhabitedSymbol + " ", symbolColor);


                if (x == 7) {
                    Console.write(VERTICAL_LINE);
                    if(moveLog != null && moveLog.size() >= y + 1) {
                        Console.TextColor color = moveLog.get(y).startsWith("Black") ? Console.TextColor.BLACK : Console.TextColor.DEFAULT;
                        Console.TextColor pieceColor = color == Console.TextColor.BLACK ? Console.TextColor.BLACK : Console.TextColor.DEFAULT;
                        Console.write("     " + VERTICAL_LINE + movePieceLog.get(y), pieceColor, "- " + moveLog.get(y), color);
                    }
                }

            }

            Console.writeLn("");
            if (y == 0)  {
                Console.write("  ");
                Console.write(BOTTOM_LEFT_CORNER);
                for (int i = 0; i < 8; i++) {
                    Console.write(HORIZONTAL_LINE);
                    Console.write(LetterConverter.numToLetter(i), Console.TextColor.WHITE);
                    if(i != 2) {
                        Console.write(HORIZONTAL_LINE);
                    }
                }
                Console.writeLn(BOTTOM_RIGHT_CORNER);
            }
        }
    }

    private static void createTopBottomEdge(boolean top) {

        for (int i = 0; i < HORIZONTAL_EDGE_LENGTH; i++) {
            if(i == 0) {
                if(top) Console.write(TOP_LEFT_CORNER);
                if(!top) Console.write(BOTTOM_LEFT_CORNER);
            } else if (i + 1 == HORIZONTAL_EDGE_LENGTH) {
                if(top) Console.write(TOP_RIGHT_CORNER);
                if(!top) Console.write(BOTTOM_RIGHT_CORNER);
            } else {
                Console.write(HORIZONTAL_LINE);
            }
        }
        Console.writeLn("");
    }

    public static void createSeperaterLine() {
        Console.writeLn("-------------------------------------------------------------");
    }

}