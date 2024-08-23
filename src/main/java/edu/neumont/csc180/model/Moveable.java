/**
 * @author jsandland
 * @createdOn 2/16/2024 at 3:28 PM
 * @projectName JaxenS_Chess
 * @packageName edu.neumont.csc180.model;
 */
package edu.neumont.csc180.model;

import java.awt.*;
import java.util.ArrayList;

public interface Moveable {

    Point getPosition();

    boolean isOccupiedBy(Point target, ArrayList<Piece> pieces);

    ArrayList<Point> getPossibleMoves(Piece[][] pieces, boolean checkKing);

    void moveTo(Point moveTo);

}
