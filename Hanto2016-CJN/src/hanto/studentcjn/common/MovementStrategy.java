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

package hanto.studentcjn.common;

import java.util.List;

import hanto.common.HantoCoordinate;
import hanto.common.HantoPieceType;
import hanto.tournament.HantoMoveRecord;

/**
 * Interface for all movement strategies Hanto will have in the future. Walking,
 * Running, Jumping, Flying, etc. Each movement strategy will be associated with a piece
 * 
 * @author chrisnavarro
 *
 */
public interface MovementStrategy {
	
	/**
	 * Checks, based on the current layout of the board, whether or not a piece is allowed to move
	 * with the given strategy
	 * 
	 * @param from where piece is being moved from
	 * @param to the proposed new location of piece
	 * @param board
	 * @return true if the strategy approves the suggested movement
	 */
	boolean isValid(HantoCoordinate from, HantoCoordinate to, HantoBoard board);

	/**
	 * Generates possible movements the piece can undergo with its associated strategy
	 * 
	 * @param coord starting coordinate
	 * @param board hantoboard
	 * @param pieceType what pieceType is moving
	 * @return the generate list for all possible movements
	 */
	List<HantoMoveRecord> generateMoveList(HantoCoordinate coord, HantoBoard board, HantoPieceType pieceType);

}
