/**
 * @author jsandland
 * @createdOn 2/16/2024 at 12:50 PM
 * @projectName JaxenS_Chess
 * @packageName edu.neumont.csc180.controller;
 */
package edu.neumont.csc180.controller;

import edu.neumont.csc180.model.Piece;
import edu.neumont.csc180.model.PieceType;
import edu.neumont.csc180.model.pieces.*;
import edu.neumont.csc180.view.BoardCreator;
import edu.neumont.csc180.view.ChessboardViewController;
import edu.neumont.csc180.view.PlayerUI;

import java.awt.*;
import java.util.*;

public class ChessJavaFXManager {
    private Timer timer;
    private final static long PLAY_SPEED = 400L, PAUSE_DURATION = 800L;
    private Piece[][] pieces = new Piece[8][8];
    private ArrayList<String> moveLog = new ArrayList<>();
    private ArrayList<String> moveSymbolLog = new ArrayList<>();
    private Point[] whitePrevMoves = new Point[2], blackPrevMoves = new Point[2];
    private final ChessboardViewController fxController;
    public final SoundManager soundManager = new SoundManager();

    public ChessJavaFXManager(ChessboardViewController fxController) {
        this.fxController = fxController;
    }

    public boolean save() {
        boolean saved = ChessSaver.save(pieces, fxController.username);
        if (saved) {
            System.out.println("Save successful");
        } else {
            System.err.println("Save failed");
        }
        return saved;
    }


    public Piece[][] getPieces() {
        return pieces;
    }

    public void startNewGame(String username) {
        pieces = defaultPieces();
        save();
        loadContinuedGame(username);
    }

    public void loadContinuedGame(String username) {
        pieces = ChessLoader.load(username);

        fxController.createBoard(pieces);
        fxController.playerMove = true;
    }

    public void testSetup(Piece[][] pieces) {
        pieces[6][1] = new Rook(new Point(6,1), true);
        pieces[2][1] = new Rook(new Point(2,1), true);
        //pieces[2][1] = new Rook(new Point(2,1), false);
        pieces[3][7] = new King(new Point(3,7), false);
        pieces[0][0] = new King(new Point(0,0), true);
//        pieces[6][2] = new Pawn(new Point(6,2), false);
//        pieces[4][3] = new Bishop(new Point(4,3), false);
//        pieces[4][4] = new Bishop(new Point(4,4), true);
//        pieces[5][5] = new Rook(new Point(5,5), true);
        pieces[7][6] = new Queen(new Point(7,6), true);
//        pieces[2][3] = new Rook(new Point(2,3), false);
//        pieces[3][4] = new Rook(new Point(3,4), true);
    }

    public boolean playerSelectPiece(Piece piece) {

        return false;

        /*
        Piece selectedPiece = null;
        do {
            if(blackPrevMoves[0] != null) {
                BoardCreator.drawBoard(pieces, moveLog, moveSymbolLog, null, blackPrevMoves);
            } else {
                BoardCreator.drawBoard(pieces, moveLog, moveSymbolLog);
            }
            if(checkEndGame()) return false;

            Point selectedPoint = PlayerUI.getPointInput("Select a piece:", "  (Ex. 'C2')");
            if(selectedPoint == null) { //ff
                PlayerUI.endGameResultDisplay(2);
                return false;
            }
            if(selectedPoint.equals(new Point(-1, -1))) return true;
            selectedPiece = pieces[selectedPoint.x][selectedPoint.y];
            if(selectedPiece == null) {
                PlayerUI.sendMessageLn("Selected spot is invalid", Console.TextColor.RED);
                continue;
            }
            if(!selectedPiece.isWhite()) PlayerUI.sendMessageLn("Selected piece is not yours", Console.TextColor.RED);
        } while (selectedPiece == null || !selectedPiece.isWhite());

        ArrayList<Point> moveOptions = selectedPiece.getPossibleMoves(pieces, true);
        BoardCreator.drawBoard(pieces, moveOptions, selectedPiece.getPosition());

        Point moveTo = PlayerUI.getPointInput("Select a move:", "  (Enter nothing to go back)", moveOptions);
        if(moveTo.equals(new Point(-1, -1))) {
            return true;
        } else {
            String moveMessage = PlayerUI.formatMoveMessage(selectedPiece, moveTo);
            moveLog.addFirst(moveMessage);
            moveSymbolLog.addFirst(selectedPiece.getSymbol());
            whitePrevMoves[0] = selectedPiece.getPosition();
            movePiece(selectedPiece, moveTo);
            whitePrevMoves[1] = selectedPiece.getPosition();
        }

        BoardCreator.drawBoard(pieces, moveLog, moveSymbolLog, whitePrevMoves, null);
        checkEndGame();
        PlayerUI.sendMessageLn("", Console.TextColor.DEFAULT);

        try {
            Thread.sleep(PAUSE_DURATION);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        botMove(pieces, false);
        BoardCreator.createSeperaterLine();
        return !checkEndGame();

         */
    }

    private void botTest() {
        TimerTask raceTask = new TimerTask() {
            boolean whiteMove = false;
            @Override
            public void run() {

                if(whitePrevMoves[0] != null && blackPrevMoves[0] != null) {
                    BoardCreator.drawBoard(pieces, moveLog, moveSymbolLog, whitePrevMoves, blackPrevMoves);
                } else {
                    BoardCreator.drawBoard(pieces, moveLog, moveSymbolLog);
                }
                boolean moved = botMove(pieces, whiteMove);
                if(!moved) timer.cancel();
                if(checkEndGame()) timer.cancel();
                BoardCreator.createSeperaterLine();
            }
        };
        timer = new Timer();
        timer.scheduleAtFixedRate(raceTask, 500L, PLAY_SPEED);
    }

    private boolean checkEndGame() {
        boolean checkWhite = true;
        for (int i = 0; i < 2; i++) {
            boolean wInCheck = logAnyChecks(pieces, checkWhite);
            if(!hasPossibleMoves(pieces, checkWhite)) {
                if(wInCheck) {
                    if(checkWhite) {
                        PlayerUI.endGameResultDisplay(2);
                    } else {
                        PlayerUI.endGameResultDisplay(1);
                    }
                } else {
                    PlayerUI.endGameResultDisplay(0);
                }
                return true;
            }
            checkWhite = !checkWhite;
        }
        return false;
    }

    public boolean botMove() {
        return botMove(pieces, false);
    }

    public boolean botMove(Piece[][] pieces, boolean white) {
        ArrayList<Piece> teamPieces = white ? findWhitePieces(pieces) : findBlackPieces(pieces);

        Collections.shuffle(teamPieces);
        for (int i = 0; i < teamPieces.size(); i++) {
            Piece chosen = teamPieces.get(i);
            if(chosen.getType() == PieceType.PAWN) {
                int ran = new Random().nextInt(2);
                if(ran == 1) {
                    return botMove(pieces, white);
                }
            }
            ArrayList<Point> moves = chosen.getPossibleMoves(pieces, true);
            if(moves != null && !moves.isEmpty()) {
                Collections.shuffle(moves);
                String moveMessage = PlayerUI.formatMoveMessage(chosen, moves.getFirst());
                Point lastPos = chosen.getPosition();
                moveLog.addFirst(moveMessage);
                moveSymbolLog.addFirst(chosen.getSymbol());
                if(white) {
                    whitePrevMoves[0] = chosen.getPosition();
                    movePiece(chosen, moves.getFirst());
                    whitePrevMoves[1] = chosen.getPosition();
                } else {
                    blackPrevMoves[0] = chosen.getPosition();
                    movePiece(chosen, moves.getFirst());
                    blackPrevMoves[1] = chosen.getPosition();
                }
                //BoardCreator.drawBoard(pieces, moveLog, moveSymbolLog, null, blackPrevMoves);
                ArrayList<Point> lastPosList = new ArrayList<>();
                lastPosList.add(lastPos);
                fxController.setBoard(pieces, lastPosList, chosen.getPosition());
                return true;
            }

            if(i + 1 == teamPieces.size()) {
                return false;
            }
        }
        return false;
    }

    public void movePiece(Piece piece, Point target) {
        Point oldPosition = new Point(piece.getPosition());
        Piece targetPiece = pieces[target.x][target.y];
        boolean firstMove = piece.isFirstMove();
        pieces[piece.getPosition().x][piece.getPosition().y] = null;
        piece.moveTo(target);
        pieces[target.x][target.y] = piece;


        if (targetPiece != null) {
        } else { //If target is empty
            if (piece.getType() == PieceType.PAWN) {
                //Check for En Passant
                Piece enPassantVictim = pieces[target.x][oldPosition.y];
                if (enPassantVictim != null && enPassantVictim.isWhite() != piece.isWhite()) {
                    pieces[target.x][oldPosition.y] = null;
                }

            }

            //region Castle
            if(firstMove && (piece.getType() == PieceType.KING || piece.getType() == PieceType.ROOK)) {
                int colorMod = 0;
                if(!piece.isWhite()) {
                    colorMod = 7;
                }
                if(piece.getType() == PieceType.KING) { //King's perspective
                    Piece rook = pieces[0][colorMod];
                    if(rook != null && rook.getType() == PieceType.ROOK && rook.isFirstMove()){ //Left castle
                        if(target.equals(new Point(2, colorMod))) {
                            movePiece(rook, new Point(3, colorMod));
                            addToLog(PieceType.KING.getSymbol(false), piece.colorToString(true) + " castled");
                        }
                    }
                    rook = pieces[7][colorMod];
                    if(rook != null && rook.getType() == PieceType.ROOK && rook.isFirstMove()){ //Right castle
                        if(target.equals(new Point(6, colorMod))) {
                            movePiece(rook, new Point(5, colorMod));
                            addToLog(PieceType.KING.getSymbol(false), piece.colorToString(true) + " castled");
                        }
                    }

                } else if (piece.getType() == PieceType.ROOK) { //Rook's perspective
                    Piece king = pieces[4][colorMod];
                    if(king != null && king.isFirstMove() && king.getType() == PieceType.KING) {
                        if(target.equals(new Point(3, colorMod))) { //Left castle
                            movePiece(king, new Point(2, colorMod));
                            addToLog(PieceType.KING.getSymbol(false), piece.colorToString(true) + " castled");
                        } else if (target.equals(new Point(5, colorMod))) { //Right castle
                            movePiece(king, new Point(6, colorMod));
                            addToLog(PieceType.KING.getSymbol(false), piece.colorToString(true) + " castled");
                        }
                    }
                }

            }
            //endregion
        }
        if(piece.getType() == PieceType.PAWN) {
            //Check for pawn promotion
            int promoHeight = 7;
            if(!piece.isWhite()) promoHeight = 0;
            if(piece.getPosition().y == promoHeight) {
                Piece newQueen = new Queen(new Point(piece.getPosition().x, piece.getPosition().y), piece.isWhite());
                pieces[piece.getPosition().x][piece.getPosition().y] = newQueen;
                addToLog(newQueen.getSymbol(), piece.colorToString(true) + " pawn promoted to queen");
            }
        }
    }

    private void addToLog(String symbol, String message) {
        moveSymbolLog.addFirst(symbol);
        moveLog.addFirst(message);
    }

    private boolean checkForChecks(Piece[][] pieces, boolean checkWhiteKing) {
        ArrayList<Piece> enemies = null;
        if(checkWhiteKing) enemies = findBlackPieces(pieces);
        else enemies = findWhitePieces(pieces);

        for (int e = 0; e < enemies.size(); e++) {
            ArrayList<Point> moves = enemies.get(e).getPossibleMoves(pieces, false);
            if(moves != null && !moves.isEmpty()) {
                for (int m = 0; m < moves.size(); m++) {
                    Piece target = pieces[moves.get(m).x][moves.get(m).y];
                    if(target != null && target.getType() == PieceType.KING && enemies.get(e).isWhite() != target.isWhite()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean hasPossibleMoves(Piece[][] pieces, boolean white) {
        ArrayList<Piece> allies = null;
        if(white) allies = findWhitePieces(pieces);
        else allies = findBlackPieces(pieces);

        for (int a = 0; a < allies.size(); a++) {
            ArrayList<Point> moves = allies.get(a).getPossibleMoves(pieces, true);
            if(!moves.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private boolean logAnyChecks(Piece[][] pieces, boolean checkWhite) {
        boolean inCheck = checkForChecks(pieces, checkWhite);
        if(inCheck) {
            if (checkWhite) {
                addToLog(PieceType.KING.getSymbol(false), "White's king is in check!");
            } else {
                addToLog(PieceType.KING.getSymbol(false), "Black's king is in check!");
            }
        }
        return inCheck;
    }

    public static ArrayList<Piece> findBlackPieces(Piece[][] pieces) {
        ArrayList<Piece> blacks = new ArrayList<>();
        for (int y = 0; y < pieces.length; y++) {
            for (int x = 0; x < pieces[0].length; x++) {
                Piece piece = pieces[x][y];
                if(piece != null) {
                    if(!piece.isWhite()) blacks.add(piece);
                }
            }
        }
        return blacks;
    }

    public static ArrayList<Piece> findWhitePieces(Piece[][] pieces) {
        ArrayList<Piece> whites = new ArrayList<>();
        for (int y = 0; y < pieces.length; y++) {
            for (int x = 0; x < pieces[0].length; x++) {
                Piece piece = pieces[x][y];
                if(piece != null) {
                    if(piece.isWhite()) whites.add(piece);
                }
            }
        }
        return whites;
    }

    private static Piece[][] defaultPieces() {
        Piece[][] newPieces = new Piece[8][8];

        //Pawns
        boolean white = true;
        for (int z = 0; z < 4; z++) {
            int y = 0;
            switch(z) {
                case 0:
                    y = 1;
                    white = true;
                    break;
                case 1:
                    y = 6;
                    white = false;
                    break;
                case 2:
                    y = 0;
                    white = true;
                    break;
                case 3:
                    y = 7;
                    white = false;
                    break;
            }
            for (int x = 0; x < 8; x++) {

                if(z == 0 || z == 1) {
                    //region pawns
                    Piece pawn = new Pawn(new Point(x, y), white);
                    newPieces[x][y] = pawn;
                    //endregion
                } else {
                    //region everything else
                    Piece piece = null;
                    Point position = new Point(x, y);

                    int switchSpot = x;
                    if(switchSpot >= 4) switchSpot = 7 - switchSpot;

                    switch(switchSpot) {
                        case 0:
                            piece = new Rook(position, white);
                            break;
                        case 1:
                            piece = new Knight(position, white);
                            break;
                        case 2:
                            piece = new Bishop(position, white);
                            break;
                        case 3:
                            if(x == 3) {
                                piece = new Queen(position, white);
                            } else {
                                piece = new King(position, white);
                            }
                            break;
                        default:
                            piece = new Pawn(position, white);

                    }
                    if(piece != null) newPieces[x][y] = piece;
                    //endregion
                }


            }
        }
        return newPieces;
    }

    public void updateChessboard() {
        fxController.setBoard(pieces);
    }

    public void updateChessboard(Piece selectedPiece) {
        if (selectedPiece != null) {
            fxController.setBoard(pieces, selectedPiece.getPossibleMoves(pieces, true), selectedPiece.getPosition());
        } else {
            fxController.setBoard(pieces);
        }
    }

}
