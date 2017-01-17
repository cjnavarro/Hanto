/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design. The course was
 * taken at Worcester Polytechnic Institute. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License
 * v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *  
 * Copyright ©2016 Chris J. Navarro
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
 * Flying Strategy Class
 * 
 * @author chrisnavarro
 *
 */
public class FlyingStrategy implements MovementStrategy {

	final private int maxSpaces;
	/**
	 * Constructor for flying strategy
	 * 
	 * @param maxSpaces spaces allowed for flight
	 */
	public FlyingStrategy(int maxSpaces) {
		this.maxSpaces = maxSpaces;
	}
	
	@Override
	public boolean isValid(HantoCoordinate from, HantoCoordinate to, HantoBoard board) {
		
		return board.distanceBetween(from, to) <= maxSpaces;
		
	}

	@Override
	public List<HantoMoveRecord> generateMoveList(HantoCoordinate coordStart, HantoBoard board, HantoPieceType pieceType) {
		int x = 0;
		int y = maxSpaces;

		List<HantoMoveRecord> moves = new LinkedList<HantoMoveRecord>();
		HantoCoordinateImpl coord = new HantoCoordinateImpl(coordStart);

		for(int i = maxSpaces; i > 0; i--) {
			for(int j = i; j > 0; j--){
				if(!board.containsPiece(new HantoCoordinateImpl(x , y)) 
						&& isValid(coord, new HantoCoordinateImpl(x , y), board)) {
					moves.add(new HantoMoveRecord(pieceType, coord, new HantoCoordinateImpl(x , y)));
				}
				x++;
				y--;
			}
			for(int k = i; k > 0; k--){
				if(!board.containsPiece(new HantoCoordinateImpl(x , y)) 
						&& isValid(coord, new HantoCoordinateImpl(x , y), board)) {
					moves.add(new HantoMoveRecord(pieceType, coord, new HantoCoordinateImpl(x , y)));
				}
				y--;
			}

			for(int l = i; l > 0; l--){
				if(!board.containsPiece(new HantoCoordinateImpl(x , y)) 
						&& isValid(coord, new HantoCoordinateImpl(x , y), board)) {
					moves.add(new HantoMoveRecord(pieceType, coord, new HantoCoordinateImpl(x , y)));
				}
				x--;
			}
			for(int m = i; m > 0; m--){
				if(!board.containsPiece(new HantoCoordinateImpl(x , y)) 
						&& isValid(coord, new HantoCoordinateImpl(x , y), board)) {
					moves.add(new HantoMoveRecord(pieceType, coord, new HantoCoordinateImpl(x , y)));
				}
				x--;
				y++;
			}
			for(int n = i; n > 0; n--){
				if(!board.containsPiece(new HantoCoordinateImpl(x , y)) 
						&& isValid(coord, new HantoCoordinateImpl(x , y), board)) {
					moves.add(new HantoMoveRecord(pieceType, coord, new HantoCoordinateImpl(x , y)));
				}
				y++;
			}
			for(int p = i; p > 0; p--){
				if(!board.containsPiece(new HantoCoordinateImpl(x , y)) 
						&& isValid(coord, new HantoCoordinateImpl(x , y), board)) {
					moves.add(new HantoMoveRecord(pieceType, coord, new HantoCoordinateImpl(x , y)));
				}
				x++;
			}
			y--;
		}

		return moves;
	}

}
