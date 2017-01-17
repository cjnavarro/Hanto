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

package hanto.studentcjn.beta;

import static hanto.common.HantoPieceType.*;
import static hanto.common.MoveResult.*;
import static hanto.common.HantoPlayerColor.*;
import static org.junit.Assert.*;
import hanto.common.*;
import hanto.studentcjn.HantoGameFactory;

import org.junit.*;

/**
 * Test cases for Beta Hanto.
 * @version Sep 14, 2014
 */
public class BetaHantoMasterTest
{
	/**
	 * Internal class for these test cases.
	 * @version Sep 13, 2014
	 */
	class TestHantoCoordinate implements HantoCoordinate
	{
		private final int x, y;
		
		private TestHantoCoordinate(int x, int y)
		{
			this.x = x;
			this.y = y;
		}
		/*
		 * @see hanto.common.HantoCoordinate#getX()
		 */
		@Override
		public int getX()
		{
			return x;
		}

		/*
		 * @see hanto.common.HantoCoordinate#getY()
		 */
		@Override
		public int getY()
		{
			return y;
		}
		
		/*
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = prime * result + x;
			result = prime * result + y;
			return result;
		}

		/*
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj)
		{
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (!(obj instanceof TestHantoCoordinate)) {
				return false;
			}
			final TestHantoCoordinate other = (TestHantoCoordinate) obj;
			if (x != other.x) {
				return false;
			}
			if (y != other.y) {
				return false;
			}
			return true;
		}
	}
	
	private static HantoGameFactory factory;
	private HantoGame game;
	private HantoGame game2;
	
	@BeforeClass
	public static void initializeClass()
	{
		factory = HantoGameFactory.getInstance();
	}
	
	@Before
	public void setup()
	{
		// By default, blue moves first.
		game = factory.makeHantoGame(HantoGameID.BETA_HANTO, BLUE);
		
		//Red moves first
		game2 = factory.makeHantoGame(HantoGameID.BETA_HANTO, RED);
	}
	
	@Test	// 1
	public void bluePlacesInitialButterflyAtOrigin() throws HantoException
    {
		final MoveResult mr = game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		assertEquals(OK, mr);
		final HantoPiece p = game.getPieceAt(makeCoordinate(0, 0));
		assertEquals(BLUE, p.getColor());
		assertEquals(BUTTERFLY, p.getType());
	}
	
	@Test(expected = HantoException.class) //2
	public void bluePlacesInitialPieceAtNonOrigin() throws HantoException
	{
		game.makeMove(SPARROW, null, makeCoordinate(1, 0));	
	}
	
	@Test //3
	public void redPlacesValidSecondPiece() throws HantoException
	{
		
		//First valid move
		final MoveResult mr1 = game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		assertEquals(OK, mr1);
		
		//Second valid move
		final MoveResult mr2 = game.makeMove(SPARROW, null, makeCoordinate(0, 1));
		assertEquals(OK, mr2);
		
		final HantoPiece p = game.getPieceAt(makeCoordinate(0, 1));
		assertEquals(RED, p.getColor());
		assertEquals(SPARROW, p.getType());
	}
	
	@Test(expected = HantoException.class) //4
	public void redPlacesNonAdjacentSecondPiece() throws HantoException
	{
		
		//First valid move
		final MoveResult mr1 = game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		assertEquals(OK, mr1);
		
		//Second valid move
		game.makeMove(SPARROW, null, makeCoordinate(0, 2));
		//assertEquals(BLUE_WINS, mr2);
	}
	
	@Test(expected = HantoException.class) //5
	public void redPlacesSecondPieceOverExistingPiece() throws HantoException
	{
		//First valid move
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		
		//Second invalid move
		game.makeMove(SPARROW, null, makeCoordinate(0, 0));
	}
	
	@Test //6
	public void bluePlacesThirdValidPiece() throws HantoException
	{
		//First valid move
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		
		//Second valid move
		game.makeMove(SPARROW, null, makeCoordinate(0, 1));
		
		//Third valid move
		MoveResult mr = game.makeMove(SPARROW, null, makeCoordinate(0, 2));
		assertEquals(mr, OK);
		
		final HantoPiece p = game.getPieceAt(makeCoordinate(0, 2));
		assertEquals(BLUE, p.getColor());
		assertEquals(SPARROW, p.getType());
		
		
	}
	
	@Test //7
	public void redPlacesFourthValidPiece() throws HantoException
	{
		//First valid move
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		
		//Second valid move
		game.makeMove(SPARROW, null, makeCoordinate(0, 1));
		
		//Third valid move
	    game.makeMove(SPARROW, null, makeCoordinate(0, 2));
		
		//Third valid move
		MoveResult mr = game.makeMove(SPARROW, null, makeCoordinate(0, 3));
		assertEquals(mr, OK);
		
		final HantoPiece p = game.getPieceAt(makeCoordinate(0, 3));
		assertEquals(RED, p.getColor());
		assertEquals(SPARROW, p.getType());
		
		
	}
	
	@Test(expected = HantoException.class) //8
	public void blueFailsToPlaceButterflyByFourthMove() throws HantoException
	{
		//First valid moves
		game.makeMove(SPARROW, null, makeCoordinate(0, 0));//b

		game.makeMove(SPARROW, null, makeCoordinate(0, 1));//r
		
		//Second valid moves
		game.makeMove(SPARROW, null, makeCoordinate(0, 2));//b

		game.makeMove(SPARROW, null, makeCoordinate(0, 3));//r
		
		//Third valid moves
		game.makeMove(SPARROW, null, makeCoordinate(0, 4));//b
		
		game.makeMove(SPARROW, null, makeCoordinate(0, 5));//r
		
		//Fourth valid move
		game.makeMove(SPARROW, null, makeCoordinate(0, 6));//r
	}
	
	@Test(expected = HantoException.class) //8
	public void redFailsToPlaceButterflyByFourthMove() throws HantoException
	{
		//First valid moves
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));//b

		game.makeMove(SPARROW, null, makeCoordinate(0, 1));//r
		
		//Second valid moves
		game.makeMove(SPARROW, null, makeCoordinate(0, 2));//b

		game.makeMove(SPARROW, null, makeCoordinate(0, 3));//r
		
		//Third valid moves
		game.makeMove(SPARROW, null, makeCoordinate(0, 4));//b
		
		game.makeMove(SPARROW, null, makeCoordinate(0, 5));//r
		
		//Fourth valid move
		game.makeMove(SPARROW, null, makeCoordinate(0, 6));//b
		
		game.makeMove(SPARROW, null, makeCoordinate(0, 7));//r
	}
	
	@Test //9
	public void gameEndsInADraw() throws HantoException
	{
		//First valid moves
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));//b

		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 1));//r
		
		//Second valid moves
		game.makeMove(SPARROW, null, makeCoordinate(0, 2));//b

		game.makeMove(SPARROW, null, makeCoordinate(0, 3));//r
		
		//Third valid moves
		game.makeMove(SPARROW, null, makeCoordinate(0, 4));//b
		
		game.makeMove(SPARROW, null, makeCoordinate(0, 5));//r
		
		//Fourth valid move
		game.makeMove(SPARROW, null, makeCoordinate(0, 6));//b
		
		game.makeMove(SPARROW, null, makeCoordinate(0, 7));//r
		
		//Fifth valid move
		game.makeMove(SPARROW, null, makeCoordinate(0, 8));//b
		
		game.makeMove(SPARROW, null, makeCoordinate(0, 9));//r
		
		//Sixth valid move
		game.makeMove(SPARROW, null, makeCoordinate(0, 10));//b
		
		MoveResult mr = game.makeMove(SPARROW, null, makeCoordinate(0, 11));//r
		assertEquals(DRAW, mr);
	}
	
	@Test(expected = HantoException.class) //10
	public void blueMakesMoveAfterDraw() throws HantoException
	{
		//First valid moves
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));//b

		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 1));//r
		
		//Second valid moves
		game.makeMove(SPARROW, null, makeCoordinate(0, 2));//b

		game.makeMove(SPARROW, null, makeCoordinate(0, 3));//r
		
		//Third valid moves
		game.makeMove(SPARROW, null, makeCoordinate(0, 4));//b
		
		game.makeMove(SPARROW, null, makeCoordinate(0, 5));//r
		
		//Fourth valid move
		game.makeMove(SPARROW, null, makeCoordinate(0, 6));//b
		
		game.makeMove(SPARROW, null, makeCoordinate(0, 7));//r
		
		//Fifth valid move
		game.makeMove(SPARROW, null, makeCoordinate(0, 8));//b
		
		game.makeMove(SPARROW, null, makeCoordinate(0, 9));//r
		
		//Sixth valid move
		game.makeMove(SPARROW, null, makeCoordinate(0, 10));//b
		
		game.makeMove(SPARROW, null, makeCoordinate(0, 11));//r
		
		//Seventh invalid move
		game.makeMove(SPARROW, null, makeCoordinate(0, 12));//b
	}
	
	@Test //11
	public void redButterflyIsSouroundedAndBlueWins() throws HantoException
	{
		//First valid moves
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));//b

		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 1));//r

		//Second valid moves
		game.makeMove(SPARROW, null, makeCoordinate(0, 2));//b

		game.makeMove(SPARROW, null, makeCoordinate(1, 1));//r

		//Third valid moves
		game.makeMove(SPARROW, null, makeCoordinate(1, 0));//b

		MoveResult mr = game.makeMove(SPARROW, null, makeCoordinate(-1, 1));//r
		assertEquals(OK, mr);
		
		//Fourth valid move
		MoveResult mr2 = game.makeMove(SPARROW, null, makeCoordinate(-1, 2));//b
		assertEquals(BLUE_WINS, mr2);
	}
	
	@Test //12
	public void blueButterflyIsSouroundedAndRedWins() throws HantoException
	{
		//First valid moves
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));//b

		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 1));//r

		//Second valid moves
		game.makeMove(SPARROW, null, makeCoordinate(1, 0));//b

		game.makeMove(SPARROW, null, makeCoordinate(1, -1));//r

		//Third valid moves
		game.makeMove(SPARROW, null, makeCoordinate(0, -1));//b

		game.makeMove(SPARROW, null, makeCoordinate(-1, 0));//r

		//Fourth valid move
		MoveResult mr = game.makeMove(SPARROW, null, makeCoordinate(-1, 1));//b
		assertEquals(RED_WINS, mr);
	}
	
	@Test	// 13
	public void redPlacesInitialButterflyAtOrigin() throws HantoException
    {
		final MoveResult mr = game2.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		assertEquals(OK, mr);
		final HantoPiece p = game2.getPieceAt(makeCoordinate(0, 0));
		assertEquals(RED, p.getColor());
		assertEquals(BUTTERFLY, p.getType());
	}
	
	@Test(expected = HantoException.class) //14
	public void redPlacesInitialPieceAtNonOrigin() throws HantoException
	{
		game2.makeMove(SPARROW, null, makeCoordinate(1, 0));	
	}
	
	@Test //15
	public void bluePlacesValidSecondPiece() throws HantoException
	{
		
		//First valid move
		final MoveResult mr1 = game2.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		assertEquals(OK, mr1);
		
		//Second valid move
		final MoveResult mr2 = game2.makeMove(SPARROW, null, makeCoordinate(0, 1));
		assertEquals(OK, mr2);
		
		final HantoPiece p = game2.getPieceAt(makeCoordinate(0, 1));
		assertEquals(BLUE, p.getColor());
		assertEquals(SPARROW, p.getType());
	}
	
	@Test //16
	public void blueSurroundsRedAndWins() throws HantoException
	{
		game2.makeMove(SPARROW, null, makeCoordinate(0,0)); //r
		game2.makeMove(SPARROW, null, makeCoordinate(1,0)); //b
		
		game2.makeMove(BUTTERFLY, null, makeCoordinate(1,1)); //r
		game2.makeMove(SPARROW, null, makeCoordinate(1,-1)); //b
		
		game2.makeMove(SPARROW, null, makeCoordinate(2,0)); //r
		game2.makeMove(SPARROW, null, makeCoordinate(0,1)); //b
		
		game2.makeMove(SPARROW, null, makeCoordinate(0,2)); //r
		game2.makeMove(BUTTERFLY, null, makeCoordinate(1,-2)); //b
		
		game2.makeMove(SPARROW, null, makeCoordinate(1,2)); //r
		MoveResult mr = game2.makeMove(SPARROW, null, makeCoordinate(2,1)); //b
		assertEquals(BLUE_WINS, mr);
	}
	
	@Test(expected = HantoException.class) //17
	public void redPlacesInvalidPieceFirstTurn() throws HantoException {
		game2.makeMove(CRAB, null, makeCoordinate(0,0));
	}
	
	@Test(expected = HantoException.class) //18
	public void redTriesToResign() throws HantoException {
		game2.makeMove(null, null, null);
	}
	
	@Test(expected = HantoException.class) //19
	public void redTriesMove() throws HantoException {
		game2.makeMove(SPARROW, null, makeCoordinate(0,0)); //r
		game2.makeMove(SPARROW, null, makeCoordinate(1,0)); //b
		
		game2.makeMove(SPARROW, makeCoordinate(0,0), makeCoordinate(0, -1)); //r
	}
	
	//The following are the tests provided by Pollice for grading
	
	@Test	// 1
	public void bluePlacesInitialButterflyAtOrigin2() throws HantoException
	{
		final MoveResult mr = game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		assertEquals(OK, mr);
		final HantoPiece p = game.getPieceAt(makeCoordinate(0, 0));
		assertEquals(BLUE, p.getColor());
		assertEquals(BUTTERFLY, p.getType());
	}
	
	@Test	// 2
	public void bluePlacesInitialSparrowAtOrigin() throws HantoException
	{
		final MoveResult mr = game.makeMove(SPARROW, null, makeCoordinate(0, 0));
		assertEquals(OK, mr);
		final HantoPiece p = game.getPieceAt(makeCoordinate(0, 0));
		assertEquals(BLUE, p.getColor());
		assertEquals(SPARROW, p.getType());
	}
	
	@Test	// 3
	public void redPlacesInitialSparrowAtOrigin() throws HantoException
	{
		game = factory.makeHantoGame(HantoGameID.BETA_HANTO, RED);	// RedFirst
		final MoveResult mr = game.makeMove(SPARROW, null, makeCoordinate(0, 0));
		assertEquals(OK, mr);
		final HantoPiece p = game.getPieceAt(makeCoordinate(0, 0));
		assertEquals(RED, p.getColor());
		assertEquals(SPARROW, p.getType());
	}
	
	@Test	// 4
	public void validFirstAndSecondMove() throws HantoException
	{
		// Blue Butterfly -> (0, 0)
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		HantoPiece p = game.getPieceAt(makeCoordinate(0, 0));
		assertEquals(BLUE, p.getColor());
		assertEquals(BUTTERFLY, p.getType());
		final MoveResult mr = game.makeMove(SPARROW, null, makeCoordinate(1, 0));
		assertEquals(OK, mr);
		p = game.getPieceAt(makeCoordinate(1, 0));
		assertEquals(RED, p.getColor());
		assertEquals(SPARROW, p.getType());
	}
	
	@Test(expected = HantoException.class)	// 5
	public void validFirstMoveNonAdjacentHexSecondMove() throws HantoException
	{
		game.makeMove(SPARROW,  null, makeCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, makeCoordinate(1, 1));
	}
	
	@Test(expected = HantoException.class)	// 6
	public void firstMoveIsNotAtOrigin() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, makeCoordinate(-1, 0));
	}
	
	@Test(expected = HantoException.class)	// 7
	public void blueAttemptsToPlaceTwoButterflies() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, makeCoordinate(1, 0));
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, -1));
	}
	
	@Test(expected = HantoException.class)	// 8
	public void redAttemptsToPlaceTwoButterflies() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, makeCoordinate(1, 0));
		game.makeMove(SPARROW, null, makeCoordinate(0, -1));
		game.makeMove(SPARROW, 	null, makeCoordinate(0, -2));
		game.makeMove(SPARROW, null, makeCoordinate(0, -3));
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 1));
	}
	
	@Test(expected = HantoException.class)	// 9
	public void blueTriesToPlacePieceOnOccupiedHex() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, makeCoordinate(1, 0));
		game.makeMove(SPARROW, null, makeCoordinate(0, 0));
	}
	
	@Test(expected = HantoException.class)	// 10
	public void redTriesToPlacePieceOnOccupiedHex() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, makeCoordinate(1, 0));
		game.makeMove(SPARROW, null, makeCoordinate(0, -1));
		game.makeMove(SPARROW, 	null, makeCoordinate(0, -1));
	}
	
	@Test(expected = HantoException.class)	// 11
	public void blueDoesNotPlaceButterflyByFourthMove() throws HantoException
	{
		game.makeMove(SPARROW, null, makeCoordinate(0, 0));	// Move 1
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, -1));
		game.makeMove(SPARROW, null, makeCoordinate(0, 1));	// Move 2
		game.makeMove(SPARROW, null, makeCoordinate(0, -2));
		game.makeMove(SPARROW, null, makeCoordinate(0, 2));	// Move 3
		game.makeMove(SPARROW, null, makeCoordinate(0, -3));
		game.makeMove(SPARROW, null, makeCoordinate(0, 3));	// Move 4
	}
	
	@Test(expected = HantoException.class)	// 12
	public void redDoesNotPlaceButterflyByFourthTurn() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));	// Move 1
		game.makeMove(SPARROW, null, makeCoordinate(0, -1));
		game.makeMove(SPARROW, null, makeCoordinate(0, 1));	// Move 2
		game.makeMove(SPARROW, null, makeCoordinate(0, -2));
		game.makeMove(SPARROW, null, makeCoordinate(0, 2));	// Move 3
		game.makeMove(SPARROW, null, makeCoordinate(0, -3));
		game.makeMove(SPARROW, null, makeCoordinate(0, 3));	// Move 4
		game.makeMove(SPARROW, null, makeCoordinate(0, -4));
	}
	
	@Test	// 13
	public void blueWinsBeforeLastTurn() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));	// Move 1
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 1));
		game.makeMove(SPARROW, null, makeCoordinate(1, 0));		// Move 2
		game.makeMove(SPARROW, null, makeCoordinate(1, 1));
		game.makeMove(SPARROW, null, makeCoordinate(0, 2));		// Move 3
		game.makeMove(SPARROW, null, makeCoordinate(-1, 2));
		assertEquals(BLUE_WINS, game.makeMove(SPARROW, null, makeCoordinate(-1,1)));	// Move 4
	}
	
	@Test	// 14
	public void redSelfLosesBeforeLastTurn() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));	// Move 1
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 1));
		game.makeMove(SPARROW, null, makeCoordinate(1, 0));		// Move 2
		game.makeMove(SPARROW, null, makeCoordinate(1, 1));
		game.makeMove(SPARROW, null, makeCoordinate(0, 2));		// Move 3
		game.makeMove(SPARROW, null, makeCoordinate(-1, 2));
		game.makeMove(SPARROW, null, makeCoordinate(0, -1));	// Move 4
		assertEquals(BLUE_WINS, game.makeMove(SPARROW, null, makeCoordinate(-1,1)));
	}
	
	@Test	// 15
	public void redWinsOnLastTurn() throws HantoException
	{
		game.makeMove(SPARROW, null, makeCoordinate(0, 0));		// Move 1
		game.makeMove(BUTTERFLY, null, makeCoordinate(1, 0));
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 1));	// Move 2
		game.makeMove(SPARROW, null, makeCoordinate(1, 1));
		game.makeMove(SPARROW, null, makeCoordinate(0, 2));		// Move 3
		game.makeMove(SPARROW, null, makeCoordinate(-1, 2));
		game.makeMove(SPARROW, null, makeCoordinate(0, 3));		// Move 4
		game.makeMove(SPARROW, null, makeCoordinate(0, 4));
		game.makeMove(SPARROW, null, makeCoordinate(0, 5));		// Move 5
		game.makeMove(SPARROW, null, makeCoordinate(0, 6));
		game.makeMove(SPARROW, null, makeCoordinate(0, 7));		// Move 6
		assertEquals(RED_WINS, game.makeMove(SPARROW, null, makeCoordinate(-1,1)));
	}
	
	@Test(expected = HantoException.class)	// 16
	public void moveAfterGameIsOverLessThanSixTurns() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));	// Move 1
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 1));
		game.makeMove(SPARROW, null, makeCoordinate(1, 0));		// Move 2
		game.makeMove(SPARROW, null, makeCoordinate(1, 1));
		game.makeMove(SPARROW, null, makeCoordinate(0, 2));		// Move 3
		game.makeMove(SPARROW, null, makeCoordinate(-1, 2));
		game.makeMove(SPARROW, null, makeCoordinate(-1,1));		// Move 4
		game.makeMove(SPARROW, null, makeCoordinate(0, -1));
	}
	
	@Test	// 17
	public void drawnGame() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));	// Move 1
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 1));
		game.makeMove(SPARROW, null, makeCoordinate(-1, 0));	// Move 2
		game.makeMove(SPARROW, null, makeCoordinate(0, -1));
		game.makeMove(SPARROW, null, makeCoordinate(1, -1));	// Move 3
		game.makeMove(SPARROW, null, makeCoordinate(1, 0));
		game.makeMove(SPARROW, null, makeCoordinate(1, 1));		// Move 4
		game.makeMove(SPARROW, null, makeCoordinate(0, 2));
		game.makeMove(SPARROW, null, makeCoordinate(-1, 2));		// Move 5
		assertEquals(DRAW, game.makeMove(SPARROW, null, makeCoordinate(-1, 1)));
	}
	
	//End of Police's tests
	
	// Helper methods
	private HantoCoordinate makeCoordinate(int x, int y)
	{
		return new TestHantoCoordinate(x, y);
	}
}
