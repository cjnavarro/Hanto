/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package hanto.studentcjn.epsilon;

import static hanto.common.HantoPieceType.*;
import static hanto.common.HantoPlayerColor.BLUE;
import static hanto.common.HantoPlayerColor.RED;

import hanto.common.HantoCoordinate;
import hanto.common.HantoException;
import hanto.common.HantoGame;
import hanto.common.HantoPieceType;
import hanto.common.HantoPlayerColor;
import hanto.studentcjn.common.BaseHantoGame;
import hanto.studentcjn.common.strategies.FlyingStrategy;
import hanto.studentcjn.common.strategies.JumpingStrategy;
import hanto.studentcjn.common.strategies.WalkingStrategy;


/**
 * Episilon Game Variant of Hanto
 * 
 * @author chrisnavarro
 *
 */
public class EpsilonHantoGame extends BaseHantoGame implements HantoGame {

	public EpsilonHantoGame(HantoPlayerColor movesFirst) {
		super(movesFirst);
		
		//set piece maxes
		maxButterfliesAllowed = 1;
		maxSparrowsAllowed = 2;
		maxCrabsAllowed = 6;
		maxHorsesAllowed = 4;
	}
	
	/**
	 * Build movement map with movements based on GammaHanto variant
	 */
	@Override
	protected void buildMovementStrategyMap() {
		movementStrategyMap.put(BUTTERFLY, new WalkingStrategy(1)); //also placed by fourth move
		movementStrategyMap.put(SPARROW, new FlyingStrategy(4));
		movementStrategyMap.put(CRAB, new WalkingStrategy(1));
		movementStrategyMap.put(HORSE, new JumpingStrategy(0));
	}
	
	/**
	 * Intended to handle all pre-checks for Epsilon Hanto
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
			throw new HantoException(color + " made invalid move, only Butterfly, Sparrow, Crab, and Horse pieces are allowed: " + altColor + "_WINS");
		}
	}

	@Override
	protected boolean filter(HantoPieceType pieceType) {
		return pieceType == BUTTERFLY || pieceType == SPARROW || pieceType == CRAB || pieceType == HORSE;
	}
	
}
