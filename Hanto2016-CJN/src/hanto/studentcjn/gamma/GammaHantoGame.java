/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design. The course was
 * taken at Worcester Polytechnic Institute. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License
 * v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Copyright ©2016 Chris J. Navarro
 *******************************************************************************/

package hanto.studentcjn.gamma;

import static hanto.common.HantoPieceType.BUTTERFLY;
import static hanto.common.HantoPieceType.SPARROW;
import static hanto.common.HantoPlayerColor.BLUE;
import static hanto.common.HantoPlayerColor.RED;

import hanto.common.*;
import hanto.studentcjn.common.*;
import hanto.studentcjn.common.strategies.WalkingStrategy;

/**
 * Gamma implementation of Hanto. Similar to Beta however pieces are now able to 'walk'. Also
 * any new pieces cannot piece placed adjacent to pieces of the opposite color. This variant
 * allows for 20 turns until a draw is declared.
 * 
 * @author chrisnavarro
 *
 */
public class GammaHantoGame extends BaseHantoGame implements HantoGame {

	/**
	 * @param movesFirst player color that makes first move
	 */
	public GammaHantoGame(HantoPlayerColor movesFirst) {
		super(movesFirst);
		maxMovesAllowed = 20;
	}
	
	/**
	 * Build movement map with movements based on GammaHanto variant
	 */
	@Override
	protected void buildMovementStrategyMap() {
		movementStrategyMap.put(BUTTERFLY, new WalkingStrategy(1));
		movementStrategyMap.put(SPARROW, new WalkingStrategy(1));
	}

	/**
	 * Intended to handle all pre-checks for Gamma Hanto
	 * 
	 * @param pieceType
	 * @param from
	 * @param to
	 * @throws HantoException
	 */
	public void preHook(HantoPieceType pieceType, HantoCoordinate from, HantoCoordinate to) throws HantoException {
		final HantoPlayerColor color = (bluesTurn) ? BLUE : RED;
		final HantoPlayerColor altColor = (!bluesTurn) ? BLUE : RED;
		
		if(from == null && to == null) {
			throw new HantoException(color + " to and from are null, no surrender is allowed in this version: " + altColor + "_WINS");
		}
		
		if(!filter(pieceType)) {
			throw new HantoException(color + " made invalid move, only Butterfly and Sparrow pieces are allowed: " + altColor + "_WINS");
		}
	}
	
	@Override
	protected boolean filter(HantoPieceType pieceType) {
		return pieceType == BUTTERFLY || pieceType == SPARROW;
	}
}
