/**
 * @author jsandland
 * @createdOn 8/19/2024 at 8:32 PM
 * @projectName JaxenS_ChessJavaFX
 * @packageName edu.neumont.csc180.controller;
 */
package edu.neumont.csc180.view;

import edu.neumont.csc180.controller.ChessJavaFXManager;
import edu.neumont.csc180.model.Piece;
import edu.neumont.csc180.model.PieceType;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.awt.*;
import java.io.FileInputStream;
import java.util.ArrayList;


public class ChessboardViewController {

    @FXML
    private GridPane chessboardGrid;




    private ImageView[][] imageViews;
    private ChessJavaFXManager chessJavaFXManager;
    private Piece selectedPiece;
    public boolean playerMove;

    @FXML
    private Button endGamePane;

    @FXML
    void backToMainMenuButtonClicked(ActionEvent event) {

    }

    @FXML
    void newGameButtonClicked(ActionEvent event) {

    }

    public void setChessJavaFXManager(ChessJavaFXManager chessJavaFXManager) {
        this.chessJavaFXManager = chessJavaFXManager;
    }

    public ImageView[][] createBoard(Piece[][] pieces) {
        imageViews = new ImageView[8][8];

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ImageView imageView = new ImageView();
                imageView.setPreserveRatio(true);
                imageView.setFitWidth(80);
                imageView.setFitHeight(80);

                final int finalRow = row;
                final int finalCol = col;

                StackPane pane = new StackPane(imageView);
                pane.setOnMouseClicked(event -> squareClicked(finalRow, finalCol));
                pane.getStyleClass().add("image-view-pane");
                if ((row + col) % 2 == 0) {
                    pane.getStyleClass().add("blackSquare");
                }
                pane.setRotate(180);
                chessboardGrid.add(pane, row, col);
                imageViews[row][col] = imageView;
            }
        }



        setBoard(pieces);

        return imageViews;
    }

    private void squareClicked(int row, int col) {
        System.out.println("squareClicked");
        if (!playerMove) return;

        Piece piece = chessJavaFXManager.getPieces()[row][col];
        StackPane parentPane = (StackPane) imageViews[row][col].getParent();
        if (piece != null && piece.isWhite() && !parentPane.getStyleClass().contains("possibleMove")) {
            if (imageViews[row][col] == null) return;
            selectedPiece = piece;

        } else if (parentPane.getStyleClass().contains("possibleMove") && selectedPiece != null) { //Move piece to new square
            chessJavaFXManager.movePiece(selectedPiece, new Point(row, col));
            selectedPiece = null;
            playerMove = chessJavaFXManager.botMove();
            return;
        } else if (!parentPane.getStyleClass().contains("possibleMove") && selectedPiece != null) { //Unselect highlighted piece
            selectedPiece = null;
        }


        chessJavaFXManager.updateChessboard(selectedPiece);
    }

    public void setBoard(Piece[][] pieces) {
        setBoard(pieces, null, null);
    }

    public void setBoard(Piece[][] pieces, ArrayList<Point> possibleMoves, Point selectedSpot) {
        for (int row = 0; row < pieces.length; row++) {
            for (int col = 0; col < pieces[0].length; col++) {
                setSquare(row, col, pieces, possibleMoves, selectedSpot);
            }
        }
    }

    public void setSquare(int row, int col, Piece[][] pieces, ArrayList<Point> possibleMoves, Point selectedSpot) {
        clearStylingFromSquare(row, col);
        Piece piece = pieces[row][col];
        StackPane parentPane = (StackPane) imageViews[row][col].getParent();

        if (piece != null) {
            PieceType type = piece.getType();
            char colorModifier = (piece.isWhite()) ? 'w' : 'b';
            String imageName = colorModifier + type.toString().substring(0, 1).toUpperCase() + type.toString().substring(1).toLowerCase();

            if (!piece.isWhite()) { //Set special properties for black pieces
                parentPane.getStyleClass().add("enemySquare");
                addGlowToImageView(imageViews[row][col], Color.WHITE);
            }


            if (!addStyleClassIfSelectedSpot(row, col, selectedSpot)) {
                if (selectedSpot != null) {
                    Piece selectedPieceInSpot = chessJavaFXManager.getPieces()[selectedSpot.x][selectedSpot.y];
                    addStyleClassIfPossibleMove(row, col, possibleMoves, selectedPieceInSpot.isWhite());
                } else {
                    addStyleClassIfPossibleMove(row, col, possibleMoves);
                }

            }

            setImage(row, col, imageName);

        } else {
            setImage(row, col, "");
            if (selectedSpot != null) {
                Piece selectedPiece = chessJavaFXManager.getPieces()[selectedSpot.x][selectedSpot.y];
                addStyleClassIfPossibleMove(row, col, possibleMoves, selectedPiece.isWhite());
            }
        }
    }

    private void addGlowToImageView(ImageView imageView, Color color) {
        DropShadow borderGlow = new DropShadow();
        borderGlow.setOffsetY(0f);
        borderGlow.setOffsetX(0f);
        borderGlow.setColor(color);
        borderGlow.setWidth(30);
        borderGlow.setHeight(30);

        imageView.setEffect(borderGlow);
    }

    private boolean addStyleClassIfPossibleMove(int row, int col, ArrayList<Point> possibleMoves) {
        return addStyleClassIfPossibleMove(row, col, possibleMoves, true);
    }

    private boolean addStyleClassIfPossibleMove(int row, int col, ArrayList<Point> possibleMoves, boolean isWhite) {
        if (possibleMoves == null || possibleMoves.isEmpty()) return false;

        StackPane parent = (StackPane) imageViews[row][col].getParent();

        boolean pointFound = findPointIsInArray(new Point(row, col), possibleMoves);
        if (pointFound) {
            if (isWhite) {
                parent.getStyleClass().add("possibleMove");
            } else {
                parent.getStyleClass().add("enemyMove");
            }
            return true;
        }

        return false;
    }

    private boolean addStyleClassIfSelectedSpot(int row, int col, Point selectedSpot) {
        if (selectedSpot != null) {
            StackPane parent = (StackPane) imageViews[row][col].getParent();
            if (row == selectedSpot.x && col == selectedSpot.y) {
                parent.getStyleClass().add("selectedSquare");
                return true;
            }
        }

        return false;
    }


    private void clearStylingFromSquare(int row, int col) {
        if (row < 0 || row >= 8 || col < 0 || col >= 8) return;
        StackPane parent = (StackPane) imageViews[row][col].getParent();
        parent.getStyleClass().remove("selectedSquare");
        parent.getStyleClass().remove("possibleMove");
        parent.getStyleClass().remove("enemySquare");
        parent.getStyleClass().remove("enemyMove");
        parent.setEffect(null);
        imageViews[row][col].setEffect(null);
    }


    private boolean findPointIsInArray(Point point, ArrayList<Point> pointList) {
        for (int p = 0; p < pointList.size(); p++) {
            if(pointList.get(p).x == point.x && pointList.get(p).y == point.y) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param row
     * @param col
     * @param imageName "/images/pawn.png" would just be "pawn"
     */
    private void setImage(int row, int col, String imageName) {
        if (imageName.isEmpty()) {
            imageViews[row][col].setImage(null);
            return;
        }

        String imagePath = "src\\main\\resources\\edu\\neumont\\csc180\\images\\" + imageName + ".png";
        try {
            FileInputStream inputstream = new FileInputStream(imagePath);
            Image image = new Image(inputstream);
            imageViews[row][col].setImage(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
