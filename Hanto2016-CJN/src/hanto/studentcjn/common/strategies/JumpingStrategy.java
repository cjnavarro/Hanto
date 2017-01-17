/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package hanto.studentcjn.common.strategies;

import java.util.LinkedList;
import java.util.List;

import hanto.common.HantoCoordinate;
import hanto.common.HantoPieceType;
import hanto.studentcjn.common.HantoBoard;
import hanto.studentcjn.common.HantoCoordinateImpl;
import hanto.studentcjn.common.MovementStrategy;
import hanto.tournament.HantoMoveRecord;

/**
 * Jumping strategy implementation
 * 
 * @author chrisnavarro
 *
 */
public class JumpingStrategy implements MovementStrategy {

	/**
	 * Constructor for jump strategy
	 * @param maxSpaces maximum spaces the piece can jump
	 */
	public JumpingStrategy(int maxSpaces) {

	}

	@Override
	public boolean isValid(HantoCoordinate from, HantoCoordinate to, HantoBoard board) {
		final String direction = chooseDirection(from, to);
		final int dist = board.distanceBetween(from, to);

		if(dist == 1) {
			return false;
		}

		final int fromX = from.getX();
		final int fromY = from.getY();

		switch(direction) {
		case "up":
			for(int i = 1; i < dist; i++) {
				if(!board.containsPiece(new HantoCoordinateImpl(fromX, fromY + i))) {
					return false;
				}
			}
			break;
		case "upRight":
			for(int i = 1; i < dist; i++) {
				if(!board.containsPiece(new HantoCoordinateImpl(fromX + i , fromY))) {
					return false;
				}
			}
			break;
		case "downRight":
			for(int i = 1; i < dist; i++) {
				if(!board.containsPiece(new HantoCoordinateImpl(fromX + i, fromY - i))) {
					return false;
				}
			}
			break;
		case "down":
			for(int i = 1; i < dist; i++) {
				if(!board.containsPiece(new HantoCoordinateImpl(fromX, fromY - i))) {
					return false;
				}
			}
			break;
		case "downLeft":
			for(int i = 1; i < dist; i++) {
				if(!board.containsPiece(new HantoCoordinateImpl(fromX - i, fromY))) {
					return false;
				}
			}
			break;
		case "upLeft":
			for(int i = 1; i < dist; i++) {
				if(!board.containsPiece(new HantoCoordinateImpl(fromX - i, fromY + i))) {
					return false;
				}
			}
			break;		
		}

		return true;
	}

	/**
	 * Sets which direction the piece is jumping initially
	 * @param from
	 * @param to
	 * @return string of jumping direction
	 */
	public String chooseDirection(HantoCoordinate from, HantoCoordinate to) {
		final int fromX = from.getX();
		final int fromY = from.getY();

		final int toX = to.getX();
		final int toY = to.getY();

		if(fromX == toX && fromY < toY) {
			return "up";
		} else if (fromX < toX && fromY == toY) {
			return "upRight";
		} else if (fromX < toX && fromY > toY) {
			return "downRight";
		} else if (fromX == toX && fromY > toY) {
			return "down";
		} else if (fromX > toX && fromY == toY) {
			return "downLeft";
		} else {
			return "upLeft";
		}
	}

	@Override
	public List<HantoMoveRecord> generateMoveList(HantoCoordinate coord, HantoBoard board, HantoPieceType pieceType) {
		List<HantoMoveRecord> moves = new LinkedList<HantoMoveRecord>();
		int fromX = coord.getX();
		int fromY = coord.getY();

		int i = 2;

		boolean loop = true;

		//case "up":
		while(loop) {
			if(!board.containsPiece(new HantoCoordinateImpl(fromX, fromY + i)) ) {
				loop = false;
				if(isValid(coord, new HantoCoordinateImpl(fromX, fromY + i), board)) {
					moves.add(new HantoMoveRecord(pieceType, coord, new HantoCoordinateImpl(fromX, fromY + i)));
				}
			} else {
				i++;
			}
		}

		loop = true;
		i = 2;

		//case "upRight":
		while(loop) {
			if(!board.containsPiece(new HantoCoordinateImpl(fromX + i, fromY)) ) {
				loop = false;
				if(isValid(coord, new HantoCoordinateImpl(fromX + i, fromY), board)) {
					moves.add(new HantoMoveRecord(pieceType, coord, new HantoCoordinateImpl(fromX + i, fromY)));
				}
			} else {
				i++;
			}
		}

		loop = true;
		i = 2;

		//case "downRight":
		while(loop) {
			if(!board.containsPiece(new HantoCoordinateImpl(fromX + i, fromY - i))) {
				loop = false;
				if(isValid(coord, new HantoCoordinateImpl(fromX + i, fromY - i), board)) {
					moves.add(new HantoMoveRecord(pieceType, coord, new HantoCoordinateImpl(fromX + i, fromY - i)));
				}
			} else {
				i++;
			}
		}

		loop = true;
		i = 2;

		//case "down":
		while(loop) {
			if(!board.containsPiece(new HantoCoordinateImpl(fromX, fromY - i))) {
				loop = false;
				if(isValid(coord, new HantoCoordinateImpl(fromX, fromY - i), board)) {
					moves.add(new HantoMoveRecord(pieceType, coord, new HantoCoordinateImpl(fromX, fromY - i)));
				}
			} else {
				i++;
			}
		}

		loop = true;
		i = 2;

		//case "downLeft":
		while(loop) {
			if(!board.containsPiece(new HantoCoordinateImpl(fromX - i, fromY))) {
				loop = false;
				if(isValid(coord, new HantoCoordinateImpl(fromX - i, fromY), board)) {
					moves.add(new HantoMoveRecord(pieceType, coord, new HantoCoordinateImpl(fromX - i, fromY)));
				}
			} else {
				i++;
			}
		}

		loop = true;
		i = 2;

		//case "upLeft":
		while(loop) {
			if(!board.containsPiece(new HantoCoordinateImpl(fromX - i, fromY + i)))  {
				loop = false;
				if(isValid(coord, new HantoCoordinateImpl(fromX - i, fromY + i), board)) {
					moves.add(new HantoMoveRecord(pieceType, coord, new HantoCoordinateImpl(fromX - i, fromY + i)));
				}
			} else {
				i++;
			}
		}	

		return moves;
	}

}
