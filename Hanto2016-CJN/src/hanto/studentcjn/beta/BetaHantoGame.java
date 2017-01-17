/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Copyright ©2016 Gary F. Pollice
 *******************************************************************************/

package hanto.studentcjn.beta;

import static hanto.common.HantoPieceType.*;
import static hanto.common.HantoPlayerColor.*;

import hanto.common.*;
import hanto.studentcjn.common.BaseHantoGame;
import hanto.studentcjn.common.HantoCoordinateImpl;

/**
 * Beta implementation of Hanto
 * 
 * @version Mar 16, 2016
 */
public class BetaHantoGame extends BaseHantoGame implements HantoGame
{
	/**
	 * Beta Hanto Constructor, sets any defaults needed for variant
	 * 
	 * @param movesFirst
	 */
	public BetaHantoGame(HantoPlayerColor movesFirst) {
		super(movesFirst);
		maxMovesAllowed = 6;
	}
	
	/**
	 * Checks is a HantoCoordinate has an adjacent pieces associated with it
	 * 
	 * @param coord the coord be checked for adjacent pieces
	 * @return true is adjacent piece found
	 */
	@Override
	public boolean hasAdjacentPiece(HantoCoordinate coord)
	{
		final int x = coord.getX();
		final int y = coord.getY();

		//checks all spots around piece for another adjacent piece
		return board.containsPiece(new HantoCoordinateImpl(x, y + 1))
				|| board.containsPiece(new HantoCoordinateImpl(x + 1, y))
				|| board.containsPiece(new HantoCoordinateImpl(x + 1, y - 1 ))
				|| board.containsPiece(new HantoCoordinateImpl(x, y - 1))
				|| board.containsPiece(new HantoCoordinateImpl(x - 1, y))
				|| board.containsPiece(new HantoCoordinateImpl(x - 1,  y + 1));
	}


	/* (non-Javadoc)
	 * @see hanto.studentcjn.common.BaseHantoGame#preHook(hanto.common.HantoPieceType, hanto.common.HantoCoordinate, hanto.common.HantoCoordinate)
	 */
	@Override
	protected void preHook(HantoPieceType pieceType, HantoCoordinate from, HantoCoordinate to) throws HantoException {
		final HantoPlayerColor color = (bluesTurn) ? BLUE : RED;
		final HantoPlayerColor altColor = (!bluesTurn) ? BLUE : RED;
		
		if(from == null && to == null) {
			throw new HantoException(color + " to and from are null, no surrender is allowed in this version: " + altColor + "_WINS");
		}
		
		if(!filter(pieceType)) {
			throw new HantoException(color + " made invalid move, only Butterfly and Sparrow pieces are allowed: " + altColor + "_WINS");
		}
		
		if(from != null) {
			throw new HantoException(color + " attempted piece movement, no movement is allowed in this version: " + altColor + "_WINS");
		}
		
	}
	
	@Override
	protected boolean filter(HantoPieceType pieceType) {
		return pieceType == BUTTERFLY || pieceType == SPARROW;
	}
	

}