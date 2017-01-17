/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design. The course was
 * taken at Worcester Polytechnic Institute. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License
 * v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Copyright ©2016 Chris J. Navarro
 *******************************************************************************/

package hanto.studentcjn.common;

import java.util.List;

import hanto.common.HantoCoordinate;
import hanto.common.HantoPiece;
import hanto.common.HantoPlayerColor;

/**
 * The HantoBoard identifies board currently in use.
 * 
 * @author cjnavarro
 * @see <a
 *      href="http://www.vbforums.com/showthread.php?663283-Hexagonal-coordinate-system">
 *      Hexagonal Coordinate System</a>
 * @version April 2, 2016
 */
public interface HantoBoard {
	
	/**
	 * Grabs the Hanto piece at a particular coordinate
	 * 
	 * @param coord that is being examined
	 * @return HantoPiece at the specific coordinate
	 */
	HantoPiece getPieceAtCoord(HantoCoordinate coord);
	
	/**
	 * Gets the color of the piece at that spot
	 * 
	 * @param coord being examined
	 * @return returns color or null if no piece is there
	 */
	HantoPlayerColor getPieceColorAtCoord(HantoCoordinate coord);
	
	/**
	 * Checks and returns true if piece exists at spot
	 * 
	 * @param coord being examined
	 * @return true is piece exists at spot
	 */
	boolean containsPiece(HantoCoordinate coord);
	
	/**
	 * Places the pieces on the board and clears its original spot (if being moved)
	 * 
	 * @param from old location of piece
	 * @param to new location of new piece
	 * @param piece being place
	 */
	void placePieceAtCoord(HantoCoordinate from, HantoCoordinate to, HantoPiece piece);

	/**
	 * Clear the Hanto board for a new game
	 */
	void clearBoard();
	
	/**
	 * Calculates the distance between two hexes
	 * 
	 * @param from
	 * @param to
	 * @return the total distance
	 */
	int distanceBetween(HantoCoordinate from, HantoCoordinate to);
	
	/**
	 * Checks if a coord has any pieces associated with it
	 * 
	 * @param destination
	 * @return true is piece is adjacent to another
	 */
	boolean hasAdjacent(HantoCoordinate destination);

	/**
	 * Prints the board
	 * @return string rep of board
	 */
	String printBoard();

	/**
	 * Creates a master list of all pieces that can be moved for a particular player
	 * 
	 * @param bluesTurn which player is list being generated for
	 * @param firstTurn is it the first move
	 * @param secondTurn
	 * @return list of all possible coordinates
	 */
	List<HantoCoordinate> generateCanMoveList(boolean bluesTurn, boolean firstTurn, boolean secondTurn);

	/**
	 * Creates a master list of all coordinates that can have a piece added for a particular player
	 * 
	 * @param bluesTurn which player is list being generated for
	 * @param firstTurn is it the first move
	 * @param secondMove 
	 * @return list of all possible coordinates
	 */
	List<HantoCoordinate> generateCanPlaceCoordinates(boolean bluesTurn, boolean firstTurn, boolean secondMove);


}
