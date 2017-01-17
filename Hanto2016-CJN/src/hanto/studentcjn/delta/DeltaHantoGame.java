/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package hanto.studentcjn.delta;

import static hanto.common.HantoPieceType.*;
import static hanto.common.HantoPlayerColor.BLUE;
import static hanto.common.HantoPlayerColor.RED;
import static hanto.common.MoveResult.BLUE_WINS;
import static hanto.common.MoveResult.RED_WINS;

import hanto.common.HantoCoordinate;
import hanto.common.HantoException;
import hanto.common.HantoGame;
import hanto.common.HantoPieceType;
import hanto.common.HantoPlayerColor;
import hanto.common.MoveResult;
import hanto.studentcjn.common.BaseHantoGame;
import hanto.studentcjn.common.strategies.FlyingStrategy;
import hanto.studentcjn.common.strategies.WalkingStrategy;

/**
 * Delta Hanto Game Variant
 * 
 * @author chrisnavarro
 *
 */
public class DeltaHantoGame extends BaseHantoGame implements HantoGame {

	/**
	 * Constructor for Delat variant, sets any defaults
	 * 
	 * @param movesFirst
	 */
	public DeltaHantoGame(HantoPlayerColor movesFirst) {
		super(movesFirst);
		maxSparrowsAllowed = 4;
	}

	/**
	 * Build movement map with movements based on DeltaHanto variant
	 */
	@Override
	protected void buildMovementStrategyMap() {
		movementStrategyMap.put(BUTTERFLY, new WalkingStrategy(1));
		movementStrategyMap.put(SPARROW, new FlyingStrategy(Integer.MAX_VALUE));
		movementStrategyMap.put(CRAB, new WalkingStrategy(3));
	}

	/**
	 * Intended to handle all pre-checks for Delta Hanto
	 * 
	 * @param pieceType
	 * @param from
	 * @param to
	 * @throws HantoException
	 */
	@Override
	public void preHook(HantoPieceType pieceType, HantoCoordinate from, HantoCoordinate to) throws HantoException {
		final HantoPlayerColor color = (bluesTurn) ? BLUE : RED;
		final HantoPlayerColor altColor = (!bluesTurn) ? BLUE : RED;

		if(!filter(pieceType) && pieceType != null) {
			throw new HantoException(color + " made invalid move, only Butterfly, Sparrow, and Crab pieces are allowed: " + altColor + "_WINS");
		}
	}

	/**
	 * Returns corrects player if surrender
	 * 
	 * @return the MoveResult caused by last move
	 * @throws HantoException if a move is made after gameOver = true
	 */
	@Override
	public MoveResult surrenderCase() {		
		if(bluesTurn) {
			return RED_WINS;
		} else {
			return BLUE_WINS;
		}
	} 
	
	@Override
	protected boolean filter(HantoPieceType pieceType) {
		return pieceType == BUTTERFLY || pieceType == SPARROW || pieceType == CRAB;
	}

}
