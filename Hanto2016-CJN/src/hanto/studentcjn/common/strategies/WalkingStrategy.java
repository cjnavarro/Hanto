/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Copyright ©2016 Chris J. Navarro
 *******************************************************************************/

package hanto.studentcjn.common.strategies;

import hanto.common.HantoCoordinate;
import hanto.common.HantoPieceType;
import hanto.studentcjn.common.HantoBoard;
import hanto.studentcjn.common.HantoCoordinateImpl;
import hanto.studentcjn.common.MovementStrategy;
import hanto.tournament.HantoMoveRecord;

import java.util.LinkedList;
import java.util.List;

/**
 * Basic walking movement strategy as outlined in the hanto manual. This class acts
 * as a verifier to confirm that a move can happen
 * 
 * @author chrisnavarro
 *
 */
public class WalkingStrategy implements MovementStrategy{

	private final int maxSpaces;
	private AStarSearch aStarSearcher;

	/**
	 * constructor for walk strategy
	 * 
	 * @param maxSpaces
	 */
	public WalkingStrategy(int maxSpaces) {
		this.maxSpaces = maxSpaces;
	}

	/* (non-Javadoc)
	 * @see hanto.studentcjn.common.MovementStategy#movePiece(hanto.common.HantoCoordinate, hanto.common.HantoCoordinate, hanto.studentcjn.common.HantoBoard)
	 */
	@Override
	public boolean isValid(HantoCoordinate from, HantoCoordinate to, HantoBoard board) {
		HantoCoordinate origin = new HantoCoordinateImpl(from);
		HantoCoordinate destination = new HantoCoordinateImpl(to);

		aStarSearcher = new AStarSearch(board, origin, destination);

		//return list of hanto coordinates that make the path
		final LinkedList<HantoCoordinate> movesToTarget = (LinkedList<HantoCoordinate>) aStarSearcher.getMovesList();

		if(movesToTarget.size() - 1 > maxSpaces || movesToTarget.isEmpty()) {
			return false;
		}

		origin = movesToTarget.removeLast();

		for(int i = 0; i < maxSpaces; i++) {
			if(movesToTarget.isEmpty()) {
				break;
			}

			destination = movesToTarget.removeLast();

			if(oneSpaceAway(origin, destination)
					&& canSlide(origin, destination, board)) {
				origin = destination;
			} else {
				return false;
			}
		}

		return true;

	}

	/**
	 * Makes sure a HantoPiece attempting to walk is only moves one space at a time
	 * 
	 * @param from old piece coordinate 
	 * @param to piece coordinate being moved to
	 * @return true if new coordinate is on spot away
	 */
	private static boolean oneSpaceAway(HantoCoordinate from, HantoCoordinate to) {

		final int fromX = from.getX();
		final int fromY = from.getY();

		final int toX = to.getX();
		final int toY = to.getY();

		return ((toX == fromX) && (toY ==  fromY + 1))
				|| ((toX == fromX + 1) && (toY ==  fromY))
				|| ((toX == fromX + 1) && (toY ==  fromY - 1))
				|| ((toX == fromX) && (toY ==  fromY - 1))
				|| ((toX == fromX - 1) && (toY ==  fromY))
				|| ((toX == fromX - 1) && (toY ==  fromY + 1));
	}

	/**
	 * Checks that a pieces is making a valid slide in accordance with the
	 * walking strategy
	 * 
	 * @param from old coordinate
	 * @param to new coordinate
	 * @param board
	 * @return true is the piece can make a valid slide
	 */
	private static boolean canSlide(HantoCoordinate from, HantoCoordinate to, HantoBoard board) {
		final int fromX = from.getX();
		final int fromY = from.getY();

		final int toX = to.getX();
		final int toY = to.getY();

		if((toX == fromX) && (toY ==  fromY + 1)) {
			if(board.containsPiece(new HantoCoordinateImpl(fromX - 1, fromY + 1)) 
					&& board.containsPiece(new HantoCoordinateImpl(fromX + 1, fromY))) {
				return false;
			}
		} else if((toX == fromX + 1) && (toY ==  fromY)) {
			if(board.containsPiece(new HantoCoordinateImpl(fromX, fromY + 1)) 
					&& board.containsPiece(new HantoCoordinateImpl(fromX + 1, fromY - 1))) {
				return false;
			}
		} else if((toX == fromX + 1) && (toY ==  fromY - 1)) {
			if(board.containsPiece(new HantoCoordinateImpl(fromX + 1, fromY)) 
					&& board.containsPiece(new HantoCoordinateImpl(fromX, fromY - 1))) {
				return false;
			}
		} else if((toX == fromX) && (toY ==  fromY - 1)) {
			if(board.containsPiece(new HantoCoordinateImpl(fromX - 1, fromY)) 
					&& board.containsPiece(new HantoCoordinateImpl(fromX + 1, fromY - 1))) {
				return false;
			}
		} else if((toX == fromX - 1) && (toY ==  fromY)) {
			if(board.containsPiece(new HantoCoordinateImpl(fromX - 1, fromY + 1)) 
					&& board.containsPiece(new HantoCoordinateImpl(fromX, fromY - 1))) {
				return false;
			}
		} else if((toX == fromX - 1) && (toY ==  fromY + 1)) {
			if(board.containsPiece(new HantoCoordinateImpl(fromX - 1, fromY)) 
					&& board.containsPiece(new HantoCoordinateImpl(fromX, fromY + 1))) {
				return false;
			}
		}

		return true;
	}

	@Override
	public List<HantoMoveRecord> generateMoveList(HantoCoordinate coordStart, HantoBoard board, HantoPieceType pieceType) {

		List<HantoMoveRecord> moves = new LinkedList<HantoMoveRecord>();
		HantoCoordinateImpl coord = new HantoCoordinateImpl(coordStart);
		int x = coord.getX();
		int y = coord.getY();
		
		HantoCoordinateImpl top = new HantoCoordinateImpl(x, y + 1);
		HantoCoordinateImpl topRight = new HantoCoordinateImpl(x + 1, y);
		HantoCoordinateImpl bottomRight = new HantoCoordinateImpl(x + 1, y - 1);
		HantoCoordinateImpl bottom = new HantoCoordinateImpl(x, y - 1);
		HantoCoordinateImpl bottomLeft = new HantoCoordinateImpl(x - 1, y);
		HantoCoordinateImpl topLeft = new HantoCoordinateImpl(x - 1, y = 1);

		if(!board.containsPiece(top) && isValid(coord, top, board)) {
			moves.add(new HantoMoveRecord(pieceType, coord, top));
		}
		if(!board.containsPiece(topRight) && isValid(coord, topRight, board)) {
			moves.add(new HantoMoveRecord(pieceType, coord, topRight));
		}
		if(!board.containsPiece(bottomRight) && isValid(coord, bottomRight, board)) {
			moves.add(new HantoMoveRecord(pieceType, coord, bottomRight));
		}
		if(!board.containsPiece(bottom) && isValid(coord, bottom, board)) {
			moves.add(new HantoMoveRecord(pieceType, coord, bottom));
		}
		if(!board.containsPiece(bottomLeft) && isValid(coord, bottomLeft, board)) {
			moves.add(new HantoMoveRecord(pieceType, coord, bottomLeft));
		}
		if(!board.containsPiece(topLeft) && isValid(coord, topLeft, board)) {
			moves.add(new HantoMoveRecord(pieceType, coord, topLeft));
		}
		
				
		return moves;
	}
}
