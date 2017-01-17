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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static hanto.common.HantoPlayerColor.*;

import hanto.common.HantoCoordinate;
import hanto.common.HantoPiece;
import hanto.common.HantoPlayerColor;

/**
 * Implementation of HantoBoard. Using a HashMap to contain HantoPieces, and uses their coords
 * as a key.
 * 
 * @author chrisnavarro
 *
 */
public class HantoBoardImpl implements HantoBoard {

	//hash map which stores games pieces by HantoCoords
	private static final Map<HantoCoordinate,HantoPiece> board = new HashMap<HantoCoordinate,HantoPiece>();

	/* (non-Javadoc)
	 * @see hanto.studentcjn.common.HantoBoard#getPieceAtCoord(hanto.common.HantoCoordinate)
	 */
	@Override
	public HantoPiece getPieceAtCoord(HantoCoordinate coord) {
		final HantoCoordinateImpl hantoCoord = new HantoCoordinateImpl(coord);

		return board.get(hantoCoord);
	}

	/* (non-Javadoc)
	 * @see hanto.studentcjn.common.HantoBoard#containsPiece(hanto.common.HantoCoordinate)
	 */
	@Override
	public boolean containsPiece(HantoCoordinate coord) {
		final HantoCoordinateImpl newCoord = new HantoCoordinateImpl(coord);

		return board.containsKey(newCoord);
	}

	/* (non-Javadoc)
	 * @see hanto.studentcjn.common.HantoBoard#placePieceAtCoord(hanto.common.HantoCoordinate, hanto.common.HantoCoordinate, hanto.common.HantoPiece)
	 */
	@Override
	public void placePieceAtCoord(HantoCoordinate from, HantoCoordinate to, HantoPiece piece) {
		final HantoCoordinateImpl destination = new HantoCoordinateImpl(to);
		HantoPieceImpl newPiece = null;

		if(piece != null) {
			newPiece = new HantoPieceImpl(piece);
		}

		if(from == null) {
			board.put(destination, newPiece);
		} else {
			final HantoCoordinateImpl origin = new HantoCoordinateImpl(from);
			board.remove(origin);
			board.put(destination, newPiece);
		}
	}

	/* (non-Javadoc)
	 * @see hanto.studentcjn.common.HantoBoard#clearBoard()
	 */
	@Override
	public void clearBoard() {

		board.clear();
	}

	/* (non-Javadoc)
	 * @see hanto.studentcjn.common.HantoBoard#printBoard()
	 */
	@Override
	public String printBoard() {
		String boardString = "";
		for (HantoCoordinate coord: board.keySet()) {

			String key = "x: " + coord.getX() + " y: " + coord.getY();
			String value = board.get(coord).getColor() + " " + board.get(coord).getType(); 
			boardString += "Key: " + key + " Value: " + value + "\r\n";
		}
		return boardString;
	}

	/* (non-Javadoc)
	 * @see hanto.studentcjn.common.HantoBoard#getPieceColorAtCoord()
	 */
	@Override
	public HantoPlayerColor getPieceColorAtCoord(HantoCoordinate coord) {
		final HantoCoordinate coord2 = new HantoCoordinateImpl(coord);

		if(board.containsKey(coord2)) {
			return board.get(coord2).getColor();
		}

		return null;
	}

	@Override
	public int distanceBetween(HantoCoordinate from, HantoCoordinate to) {
		final int x1 = from.getX();
		final int x2 = to.getX();

		final int y1 = from.getY();
		final int y2 = to.getY();

		final int z1 = ((-1 * x1) - y1);
		final int z2 = ((-1 * x2) - y2);

		return Math.max(Math.abs(x2 - x1), Math.max(Math.abs(y2 - y1), Math.abs(z2 - z1)));
	}

	@Override
	public boolean hasAdjacent(HantoCoordinate destination) {
		final int x = destination.getX();
		final int y = destination.getY();

		final HantoCoordinateImpl topLeft = new HantoCoordinateImpl(x - 1, y + 1);
		final HantoCoordinateImpl top = new HantoCoordinateImpl(x, y + 1);
		final HantoCoordinateImpl topRight = new HantoCoordinateImpl( x + 1, y);
		final HantoCoordinateImpl bottomRight = new HantoCoordinateImpl(x + 1, y - 1);
		final HantoCoordinateImpl bottom = new HantoCoordinateImpl(x, y - 1);
		final HantoCoordinateImpl bottomLeft = new HantoCoordinateImpl(x - 1, y);

		//checks all spots around butterfly
		return containsPiece(topLeft)
				|| containsPiece(top)
				|| containsPiece(topRight)
				|| containsPiece(bottomRight)
				|| containsPiece(bottom)
				|| containsPiece(bottomLeft);
	}

	@Override
	public List<HantoCoordinate> generateCanMoveList(boolean bluesTurn, boolean firstTurn, boolean secondTurn) {
		List<HantoCoordinate> moves = new LinkedList<HantoCoordinate>();
		HantoPlayerColor filterColor = (bluesTurn) ? BLUE : RED;

			for(HantoCoordinate coord : board.keySet()) {
				if(board.get(coord).getColor() == filterColor) {
					moves.add(coord);
				}
			}

		return moves;		
	}

	@Override
	public List<HantoCoordinate> generateCanPlaceCoordinates(boolean bluesTurn, boolean firstTurn, boolean secondTurn) {
		List<HantoCoordinate> coords = new LinkedList<HantoCoordinate>();
		HantoPlayerColor filterColor = (bluesTurn) ? BLUE : RED;

		if(firstTurn) {
			coords.add(new HantoCoordinateImpl(0, 0));

		} else if(!secondTurn) {
			coords.add(new HantoCoordinateImpl(0, 1));
			coords.add(new HantoCoordinateImpl(1, 0));
			coords.add(new HantoCoordinateImpl(1, -1));
			coords.add(new HantoCoordinateImpl(0, -1));
			coords.add(new HantoCoordinateImpl(-1, 0));
			coords.add(new HantoCoordinateImpl(-1, 1));

		} else {

			int x, y;

			HantoCoordinateImpl topLeft;
			HantoCoordinateImpl top;
			HantoCoordinateImpl topRight;
			HantoCoordinateImpl bottomRight;
			HantoCoordinateImpl bottom;
			HantoCoordinateImpl bottomLeft;

			for(HantoCoordinate coord : board.keySet()) {
				if(board.get(coord).getColor() == filterColor) {
					x = coord.getX();
					y = coord.getY();

					topLeft = new HantoCoordinateImpl(x - 1, y + 1);
					top = new HantoCoordinateImpl(x, y + 1);
					topRight = new HantoCoordinateImpl( x + 1, y);
					bottomRight = new HantoCoordinateImpl(x + 1, y - 1);
					bottom = new HantoCoordinateImpl(x, y - 1);
					bottomLeft = new HantoCoordinateImpl(x - 1, y);

					if(!hasAdjacentOppositeColor(topLeft, filterColor) && !containsPiece(topLeft)) {
						if(!coords.contains(topLeft)) {
							coords.add(topLeft);
						}
					}
					if(!hasAdjacentOppositeColor(top, filterColor) && !containsPiece(top)) {
						if(!coords.contains(top)) {
							coords.add(top);
						}
					}
					if(!hasAdjacentOppositeColor(topRight, filterColor) && !containsPiece(topRight)) {
						if(!coords.contains(topRight)) {
							coords.add(topRight);
						}
					}
					if(!hasAdjacentOppositeColor(bottomRight, filterColor) && !containsPiece(bottomRight)) {
						if(!coords.contains(bottomRight)) {
							coords.add(bottomRight);
						}
					}
					if(!hasAdjacentOppositeColor(bottom, filterColor) && !containsPiece(bottom)) {
						if(!coords.contains(bottom)) {
							coords.add(bottom);
						}
					}
					if(!hasAdjacentOppositeColor(bottomLeft, filterColor) && !containsPiece(bottomLeft)) {
						if(!coords.contains(bottomLeft)) {
							coords.add(bottomLeft);
						}
					}

				}
			}
		}

		return coords;
	}

	private boolean hasAdjacentOppositeColor(HantoCoordinate coord, HantoPlayerColor color) {
		final int x = coord.getX();
		final int y = coord.getY();

		final HantoCoordinateImpl topLeft = new HantoCoordinateImpl(x - 1, y + 1);
		final HantoCoordinateImpl top = new HantoCoordinateImpl(x, y + 1);
		final HantoCoordinateImpl topRight = new HantoCoordinateImpl( x + 1, y);
		final HantoCoordinateImpl bottomRight = new HantoCoordinateImpl(x + 1, y - 1);
		final HantoCoordinateImpl bottom = new HantoCoordinateImpl(x, y - 1);
		final HantoCoordinateImpl bottomLeft = new HantoCoordinateImpl(x - 1, y);

		if(containsPiece(topLeft)) {
			if(getPieceAtCoord(topLeft).getColor() != color) {
				return true;
			}
		} 
		
		if(containsPiece(top)) {
			if(getPieceAtCoord(top).getColor() != color) {
				return true;
			}
		}
		
		if(containsPiece(topRight)) {
			if(getPieceAtCoord(topRight).getColor() != color) {
				return true;
			}
		}
		
		if(containsPiece(bottomRight)) {
			if(getPieceAtCoord(bottomRight).getColor() != color) {
				return true;
			}
		} 
		
		if(containsPiece(bottom)) {
			if(getPieceAtCoord(bottom).getColor() != color) {
				return true;
			}
		} 
		
		if(containsPiece(bottomLeft)) {
			if(getPieceAtCoord(bottomLeft).getColor() != color) {
				return true;
			}
		}

		return false;
	}
}
