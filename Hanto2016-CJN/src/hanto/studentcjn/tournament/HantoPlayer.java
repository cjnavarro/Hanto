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

package hanto.studentcjn.tournament;

import java.util.LinkedList;
import java.util.List;

import hanto.common.*;
import hanto.studentcjn.HantoGameFactory;
import hanto.studentcjn.common.BaseHantoGame;
import hanto.studentcjn.common.HantoCoordinateImpl;
import hanto.tournament.*;

/**
 * Description
 * @version Oct 13, 2014
 */
public class HantoPlayer implements HantoGamePlayer
{

	@SuppressWarnings("unused")
	private static HantoGameFactory factory = HantoGameFactory.getInstance();
	private HantoGame game;
	private boolean butterflyplaced = false;
	private boolean firstTurn = true;
	
	public HantoPlayer() {
		
	}
	
	/**
	 * Constructor which sets the hantoplayer game
	 * USED FOR TESTING ONLY
	 * 
	 * @param game
	 * @param first
	 * @param butterfly
	 */
	public HantoPlayer(HantoGame game, boolean first, boolean butterfly) {
		this.game = game;
		firstTurn = first;
		butterflyplaced = butterfly;
	}
	
	/*
	 * @see hanto.tournament.HantoGamePlayer#startGame(hanto.common.HantoGameID, hanto.common.HantoPlayerColor, boolean)
	 */
	@Override
	public void startGame(HantoGameID version, HantoPlayerColor myColor,
			boolean doIMoveFirst)
	{
		HantoPlayerColor color = determineColor(myColor, doIMoveFirst);
		
		game = HantoGameFactory.makeHantoGame(version, color);
		
	}

	/**
	 * Properly determines color of first turn player
	 * @param myColor
	 * @param doIMoveFirst
	 * @return
	 */
	private HantoPlayerColor determineColor(HantoPlayerColor myColor, boolean doIMoveFirst) {
		if(doIMoveFirst) {
			return myColor;
		} else {
			return (myColor == HantoPlayerColor.BLUE) ? HantoPlayerColor.RED : HantoPlayerColor.BLUE;
		}
	}

	/*
	 * @see hanto.tournament.HantoGamePlayer#makeMove(hanto.tournament.HantoMoveRecord)
	 */
	@Override
	public HantoMoveRecord makeMove(HantoMoveRecord opponentsMove)
	{
		if(opponentsMove != null) {
			firstTurn = false;
			try {
				game.makeMove(opponentsMove.getPiece(), opponentsMove.getFrom(), opponentsMove.getTo());
			} catch (HantoException e) {
				return new HantoMoveRecord(null, null, null);
			}
		}
		
		HantoMoveRecord myMove;
		
		if(!butterflyplaced) {
			butterflyplaced = true;
			if(firstTurn) {
				firstTurn = false;
				myMove = new HantoMoveRecord(HantoPieceType.BUTTERFLY, null, new HantoCoordinateImpl(0, 0));
			} else {
				myMove = new HantoMoveRecord(HantoPieceType.BUTTERFLY, null, new HantoCoordinateImpl(0, -1));
			}
			
			try {
				game.makeMove(myMove.getPiece(), myMove.getFrom(), myMove.getTo());
			} catch (HantoException e) {
				return new HantoMoveRecord(null, null, null);
			}
			
			return myMove;
		}
		
		List<HantoMoveRecord> moves = new LinkedList<HantoMoveRecord>();
		moves.addAll(((BaseHantoGame) game).generateCanMoveList());
		//moves.addAll(((BaseHantoGame) game).generateCanPlaceList());
		
		
		//if all moves exhausted player resigns
		if(moves.isEmpty()) {
			return new HantoMoveRecord(null, null, null);
		}
		
		int size = moves.size();
		int index = (int)(Math.random()*size);
		
		myMove = moves.get(index);
		
		try {
			game.makeMove(myMove.getPiece(), myMove.getFrom(), myMove.getTo());
		} catch (HantoException e) {
			return new HantoMoveRecord(null, null, null);
		}
		
		
		return myMove;
	}
	
}
