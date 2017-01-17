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

package hanto.studentcjn.gamma;

import static hanto.common.HantoPieceType.*;
import static hanto.common.MoveResult.*;
import static hanto.common.HantoPlayerColor.*;
import static org.junit.Assert.*;
import hanto.common.*;
import hanto.studentcjn.HantoGameFactory;

import org.junit.*;

/**
 * Test cases for GammaHanto.
 * @version April 7, 2016
 */
public class GammaHantoMasterTest
{	
	class MoveData {
		final HantoPieceType type;
		final HantoCoordinate from, to;
		
		private MoveData(HantoPieceType type, HantoCoordinate from, HantoCoordinate to) 
		{
			this.type = type;
			this.from = from;
			this.to = to;
		}
	}
	
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
	}
	
	private static HantoGameFactory factory;
	private HantoGame game;
	private HantoGame gameRed;

	@BeforeClass
	public static void initializeClass()
	{
		factory = HantoGameFactory.getInstance();
	}
	
	@Before
	public void setup()
	{
		// By default, blue moves first.
		game = factory.makeHantoGame(HantoGameID.GAMMA_HANTO, BLUE);
		gameRed = factory.makeHantoGame(HantoGameID.GAMMA_HANTO, RED);
		
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
	
	@Test	// 2
	public void redPlacesSecondPiece() throws HantoException
    {
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		
		final MoveResult mr = game.makeMove(SPARROW, null, makeCoordinate(0, 1));
		assertEquals(OK, mr);
		final HantoPiece p = game.getPieceAt(makeCoordinate(0, 1));
		assertEquals(RED, p.getColor());
		assertEquals(SPARROW, p.getType());
	}
	
	@Test(expected = HantoException.class)	// 3
	public void blueTriesToPlaceThirdPieceNextToRed() throws HantoException
    {
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(SPARROW, null, makeCoordinate(0, 1));	
		game.makeMove(SPARROW, null, makeCoordinate(0, 2));
	}
	
	@Test // 4
	public void blueSlidesFirstPlacedPieceNextToRed() throws HantoException
    {
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(SPARROW, null, makeCoordinate(0, 1));	
		
		MoveResult mr = game.makeMove(BUTTERFLY, makeCoordinate(0, 0), makeCoordinate(1, 0));
		assertEquals(OK, mr);
		
		assertEquals(null, game.getPieceAt(makeCoordinate(0, 0)));
		assertEquals(BUTTERFLY, game.getPieceAt(makeCoordinate(1, 0)).getType());
		assertEquals(BLUE, game.getPieceAt(makeCoordinate(1, 0)).getColor());
	}
	
	@Test(expected = HantoException.class)	// 5
	public void redFailsToPlaceButterflyByFourthMove() throws HantoException
    {
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0)); //1
		game.makeMove(SPARROW, null, makeCoordinate(0, -1));
		
		game.makeMove(SPARROW, null, makeCoordinate(0, 1)); //2
		game.makeMove(SPARROW, null, makeCoordinate(0, -2));
		
		game.makeMove(SPARROW, null, makeCoordinate(0, 2)); //3
		game.makeMove(SPARROW, null, makeCoordinate(0, -3));
		
		game.makeMove(SPARROW, null, makeCoordinate(0, 3)); //4
		game.makeMove(SPARROW, null, makeCoordinate(0, -4));
	}
	
	@Test(expected = HantoException.class)	// 6
	public void blueFailsToPlaceButterflyByFourthMove() throws HantoException
    {
		game.makeMove(SPARROW, null, makeCoordinate(0, 0)); //1
		game.makeMove(SPARROW, null, makeCoordinate(0, -1));
		
		game.makeMove(SPARROW, null, makeCoordinate(0, 1)); //2
		game.makeMove(SPARROW, null, makeCoordinate(0, -2));
		
		game.makeMove(SPARROW, null, makeCoordinate(0, 2)); //3
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, -3));
		
		game.makeMove(SPARROW, null, makeCoordinate(0, 3)); //4
		game.makeMove(SPARROW, null, makeCoordinate(0, -4));
	}
	
	@Test(expected = HantoException.class)	// 7
	public void blueFailsToPlaceFirstPieceAtNonOrigin() throws HantoException
    {
		game.makeMove(SPARROW, null, makeCoordinate(0, 1));
		
	}
	
	@Test(expected = HantoException.class)	// 8
	public void redFailsToPlaceSecondAdjacentPiece() throws HantoException
    {
		game.makeMove(SPARROW, null, makeCoordinate(0, 0));
		game.makeMove(SPARROW, null, makeCoordinate(0, 2));
	}
	
	@Test(expected = HantoException.class) // 9
	public void blueMovesNonMatchingPieceType() throws HantoException
    {
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(SPARROW, null, makeCoordinate(0, 1));	
		game.makeMove(SPARROW, makeCoordinate(0, 0), makeCoordinate(1, 0));
	}
	
	@Test(expected = HantoException.class) // 10
	public void blueMovesFirstPlacedPieceToInvalidPosition() throws HantoException
    {
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(SPARROW, null, makeCoordinate(0, 1));	
		game.makeMove(BUTTERFLY, makeCoordinate(0, 0), makeCoordinate(0, 2));
	}
	
	@Test(expected = HantoException.class) // 11
	public void blueAttemptsInvalidSlideThirdTurn() throws HantoException {
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(SPARROW, null, makeCoordinate(0, 1));
		
		game.makeMove(SPARROW, null, makeCoordinate(0, -1));
		game.makeMove(SPARROW, null, makeCoordinate(0, 2));
		
		game.makeMove(SPARROW, null, makeCoordinate(-1, 0));
		game.makeMove(SPARROW, null, makeCoordinate(0, 3));
		
		game.makeMove(BUTTERFLY, makeCoordinate(0, 0), makeCoordinate(-1, 1));	
	}
	
	
	@Test //12
	public void redAttemptValidSlideDuringFifthMove() throws HantoException {
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(SPARROW, null, makeCoordinate(0, 1));
		
		game.makeMove(SPARROW, null, makeCoordinate(0, -1));
		game.makeMove(SPARROW, null, makeCoordinate(0, 2));
		
		game.makeMove(SPARROW, null, makeCoordinate(0, -2));
		game.makeMove(SPARROW, null, makeCoordinate(0, 3));
		
		game.makeMove(SPARROW, null, makeCoordinate(0, -3));	
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 4));
		
		game.makeMove(SPARROW, null, makeCoordinate(0, -4)); //5
		MoveResult mr = game.makeMove(BUTTERFLY, makeCoordinate(0, 4), makeCoordinate(-1, 4));
		assertEquals(OK, mr);
		assertEquals(BUTTERFLY, game.getPieceAt(makeCoordinate(-1, 4)).getType());
		assertEquals(RED, game.getPieceAt(makeCoordinate(-1, 4)).getColor());
	}
	
	@Test(expected = HantoException.class) //13
	public void blueAttemptValidSlideDuringThirdMoveThatCreatesAnInvalidDisconnect() throws HantoException {
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(SPARROW, null, makeCoordinate(0, 1));
		
		game.makeMove(SPARROW, null, makeCoordinate(0, -1));
		game.makeMove(SPARROW, null, makeCoordinate(0, 2));
		
		game.makeMove(SPARROW, makeCoordinate(0, -1), makeCoordinate(0, -2));
	}
	
	@Test //14
	public void blueLosesToRed() throws HantoException {
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(SPARROW, null, makeCoordinate(0, -1));
		
		game.makeMove(SPARROW, null, makeCoordinate(0, 1));
		game.makeMove(SPARROW, null, makeCoordinate(0, -2));
		
		game.makeMove(SPARROW, null, makeCoordinate(0, 2));
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, -3));
		
		game.makeMove(SPARROW, makeCoordinate(0, 2), makeCoordinate(1, 1));
		game.makeMove(BUTTERFLY, makeCoordinate(0, -3), makeCoordinate(-1, -2));
		
		game.makeMove(SPARROW, makeCoordinate(1, 1), makeCoordinate(1, 0));
		game.makeMove(BUTTERFLY, makeCoordinate(-1, -2), makeCoordinate(-1, -1));
		
		game.makeMove(SPARROW, makeCoordinate(1, 0), makeCoordinate(1, -1));
		game.makeMove(BUTTERFLY, makeCoordinate(-1, -1), makeCoordinate(-1, 0));
		
		game.makeMove(SPARROW, null, makeCoordinate(1, 1));
		game.makeMove(SPARROW, makeCoordinate(0, -2), makeCoordinate(1, -2));
		
		game.makeMove(SPARROW, makeCoordinate(1, 1), makeCoordinate(1, 0));
		game.makeMove(SPARROW, makeCoordinate(1, -2), makeCoordinate(0, -2));
		
		game.makeMove(SPARROW,  null, makeCoordinate(-1, 2));
		game.makeMove(SPARROW, makeCoordinate(0, -2), makeCoordinate(1, -2));
		
		MoveResult mr = game.makeMove(SPARROW,  makeCoordinate(-1, 2), makeCoordinate(-1, 1));
		
		assertEquals(RED_WINS, mr);		
	}
	
	@Test(expected = HantoException.class) //15
	public void blueLosesToRedAndRedMakesExtraTurn() throws HantoException {
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(SPARROW, null, makeCoordinate(0, -1));
		
		game.makeMove(SPARROW, null, makeCoordinate(0, 1));
		game.makeMove(SPARROW, null, makeCoordinate(0, -2));
		
		game.makeMove(SPARROW, null, makeCoordinate(0, 2));
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, -3));
		
		game.makeMove(SPARROW, makeCoordinate(0, 2), makeCoordinate(1, 1));
		game.makeMove(BUTTERFLY, makeCoordinate(0, -3), makeCoordinate(-1, -2));
		
		game.makeMove(SPARROW, makeCoordinate(1, 1), makeCoordinate(1, 0));
		game.makeMove(BUTTERFLY, makeCoordinate(-1, -2), makeCoordinate(-1, -1));
		
		game.makeMove(SPARROW, makeCoordinate(1, 0), makeCoordinate(1, -1));
		game.makeMove(BUTTERFLY, makeCoordinate(-1, -1), makeCoordinate(-1, 0));
		
		game.makeMove(SPARROW, null, makeCoordinate(1, 1));
		game.makeMove(SPARROW, makeCoordinate(0, -2), makeCoordinate(1, -2));
		
		game.makeMove(SPARROW, makeCoordinate(1, 1), makeCoordinate(1, 0));
		game.makeMove(SPARROW, makeCoordinate(1, -2), makeCoordinate(0, -2));
		
		game.makeMove(SPARROW,  null, makeCoordinate(-1, 2));
		game.makeMove(SPARROW, makeCoordinate(0, -2), makeCoordinate(1, -2));
		
		game.makeMove(SPARROW,  makeCoordinate(-1, 2), makeCoordinate(-1, 1));
		game.makeMove(SPARROW, makeCoordinate(1, -2), makeCoordinate(0, -2));
			
	}
	
	@Test //16
	public void drawByMaxTurns() throws HantoException {
		
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));//1
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, -1));
		
		game.makeMove(SPARROW, null, makeCoordinate(0, 1));//2
		game.makeMove(SPARROW, null, makeCoordinate(0, -2));
		
		game.makeMove(SPARROW, makeCoordinate(0, 1), makeCoordinate(-1, 1));
		game.makeMove(SPARROW, makeCoordinate(0, -2), makeCoordinate(1, -2));
		
		game.makeMove(SPARROW, makeCoordinate(-1, 1), makeCoordinate(0, 1));
		game.makeMove(SPARROW, makeCoordinate(1, -2), makeCoordinate(0, -2));
		
		game.makeMove(SPARROW, makeCoordinate(0, 1), makeCoordinate(-1, 1));
		game.makeMove(SPARROW, makeCoordinate(0, -2), makeCoordinate(1, -2));
		
		game.makeMove(SPARROW, makeCoordinate(-1, 1), makeCoordinate(0, 1));
		game.makeMove(SPARROW, makeCoordinate(1, -2), makeCoordinate(0, -2));
		
		game.makeMove(SPARROW, makeCoordinate(0, 1), makeCoordinate(-1, 1));
		game.makeMove(SPARROW, makeCoordinate(0, -2), makeCoordinate(1, -2));
		
		game.makeMove(SPARROW, makeCoordinate(-1, 1), makeCoordinate(0, 1));
		game.makeMove(SPARROW, makeCoordinate(1, -2), makeCoordinate(0, -2));
		
		game.makeMove(SPARROW, makeCoordinate(0, 1), makeCoordinate(-1, 1));
		game.makeMove(SPARROW, makeCoordinate(0, -2), makeCoordinate(1, -2));
		
		game.makeMove(SPARROW, makeCoordinate(-1, 1), makeCoordinate(0, 1));
		game.makeMove(SPARROW, makeCoordinate(1, -2), makeCoordinate(0, -2));
		
		game.makeMove(SPARROW, makeCoordinate(0, 1), makeCoordinate(-1, 1));
		game.makeMove(SPARROW, makeCoordinate(0, -2), makeCoordinate(1, -2));
		
		game.makeMove(SPARROW, makeCoordinate(-1, 1), makeCoordinate(0, 1));
		game.makeMove(SPARROW, makeCoordinate(1, -2), makeCoordinate(0, -2));
		
		game.makeMove(SPARROW, makeCoordinate(0, 1), makeCoordinate(-1, 1));
		game.makeMove(SPARROW, makeCoordinate(0, -2), makeCoordinate(1, -2));
		
		game.makeMove(SPARROW, makeCoordinate(-1, 1), makeCoordinate(0, 1));
		game.makeMove(SPARROW, makeCoordinate(1, -2), makeCoordinate(0, -2));
		
		game.makeMove(SPARROW, makeCoordinate(0, 1), makeCoordinate(-1, 1));
		game.makeMove(SPARROW, makeCoordinate(0, -2), makeCoordinate(1, -2));
		
		game.makeMove(SPARROW, makeCoordinate(-1, 1), makeCoordinate(0, 1));
		game.makeMove(SPARROW, makeCoordinate(1, -2), makeCoordinate(0, -2));
		
		game.makeMove(SPARROW, makeCoordinate(0, 1), makeCoordinate(-1, 1));
		game.makeMove(SPARROW, makeCoordinate(0, -2), makeCoordinate(1, -2));
		
		game.makeMove(SPARROW, makeCoordinate(-1, 1), makeCoordinate(0, 1));
		game.makeMove(SPARROW, makeCoordinate(1, -2), makeCoordinate(0, -2));
		
		game.makeMove(SPARROW, makeCoordinate(0, 1), makeCoordinate(-1, 1));
		game.makeMove(SPARROW, makeCoordinate(0, -2), makeCoordinate(1, -2));

		game.makeMove(SPARROW, makeCoordinate(-1, 1), makeCoordinate(0, 1));
		MoveResult mr = game.makeMove(SPARROW, makeCoordinate(1, -2), makeCoordinate(0, -2));
		
		assertEquals(DRAW, mr);
	}

	@Test //18
	public void redLosesToBlue() throws HantoException {
		game.makeMove(BUTTERFLY, null,  makeCoordinate(0,0));
		game.makeMove(BUTTERFLY, null,  makeCoordinate(0,1));
		
		game.makeMove(BUTTERFLY, makeCoordinate(0,0), makeCoordinate(1,0));
		game.makeMove(SPARROW, null,  makeCoordinate(0,2));
		
		game.makeMove(SPARROW, null,  makeCoordinate(2, 0));
		game.makeMove(SPARROW, null,  makeCoordinate(-1,2));
		
		game.makeMove(SPARROW, null,  makeCoordinate(1, -1));
		game.makeMove(SPARROW, null,  makeCoordinate(-1,1));
		
		game.makeMove(SPARROW, makeCoordinate(1, -1), makeCoordinate(0, 0));
		game.makeMove(SPARROW, null,  makeCoordinate(-2,2));
		
		MoveResult mr = game.makeMove(SPARROW, makeCoordinate(2, 0), makeCoordinate(1, 1));
		assertEquals(BLUE_WINS, mr);	
	}
	
	@Test(expected = HantoException.class) //20
	public void blueTriesWalkingTwoSpacesDuringSecondMove() throws HantoException {
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 1));
		
		game.makeMove(BUTTERFLY, makeCoordinate(0, 0), makeCoordinate(1, 1));
	}
	
	@Test(expected = HantoException.class) //21
	public void redTriesInvalidSlide() throws HantoException {
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 1));
		
		game.makeMove(SPARROW, null, makeCoordinate(-1, 0));
		game.makeMove(SPARROW, null, makeCoordinate(0, 2));
		
		game.makeMove(SPARROW, makeCoordinate(-1, 0), makeCoordinate(-1, 1));
		game.makeMove(BUTTERFLY, makeCoordinate(0, 1), makeCoordinate(-1, 2));
	}
	
	@Test // 22
	public void redPlacesInitialButterflyAtOrigin() throws HantoException
    {
		final MoveResult mr = gameRed.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		assertEquals(OK, mr);
		final HantoPiece p = gameRed.getPieceAt(makeCoordinate(0, 0));
		assertEquals(RED, p.getColor());
		assertEquals(BUTTERFLY, p.getType());
	}
	
	@Test	// 23
	public void bluePlacesButterflySecondTurn() throws HantoException
    {
		gameRed.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		final MoveResult mr = gameRed.makeMove(BUTTERFLY, null, makeCoordinate(0, 1));
		assertEquals(OK, mr);
		final HantoPiece p = gameRed.getPieceAt(makeCoordinate(0, 1));
		assertEquals(BLUE, p.getColor());
		assertEquals(BUTTERFLY, p.getType());
	}
	
	@Test(expected = HantoException.class) // 24
	public void redPlacesInvalidPieceType() throws HantoException
    {
		gameRed.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		gameRed.makeMove(DOVE, null, makeCoordinate(0, 1));

	}
	
	@Test(expected = HantoException.class) // 25
	public void redMovesToNullSpotSecondTurn() throws HantoException
    {
		gameRed.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		gameRed.makeMove(BUTTERFLY, null, makeCoordinate(0, 1));
		
		gameRed.makeMove(SPARROW, null, makeCoordinate(0, -1));
		gameRed.makeMove(BUTTERFLY, makeCoordinate(0, 1), null);
	}
	
	@Test(expected = HantoException.class) // 26
	public void redTriesToPlaceSecondButterfly() throws HantoException
    {
		gameRed.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		gameRed.makeMove(BUTTERFLY, null, makeCoordinate(0, 1));
		
		gameRed.makeMove(BUTTERFLY, null, makeCoordinate(0, -1));	
	}
	
	@Test(expected = HantoException.class) // 27
	public void blueTriesToPlaceSecondButterfly() throws HantoException
    {
		gameRed.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		gameRed.makeMove(BUTTERFLY, null, makeCoordinate(0, 1));
		
		gameRed.makeMove(SPARROW, null, makeCoordinate(0, -1));	
		gameRed.makeMove(BUTTERFLY, null, makeCoordinate(0, 2));
	}
	
	@Test(expected = HantoException.class) // 28
	public void blueTriesToMoveSecondPieceOverRedPiece() throws HantoException
    {
		gameRed.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		gameRed.makeMove(BUTTERFLY, null, makeCoordinate(0, 1));
		
		gameRed.makeMove(SPARROW, null, makeCoordinate(0, -1));	
		gameRed.makeMove(BUTTERFLY, makeCoordinate(0, 1), makeCoordinate(0, 0));
	}
	
	@Test(expected = HantoException.class) // 29
	public void redMakesSecondMoveThatCreatesDisconnect() throws HantoException
    {
		gameRed.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		gameRed.makeMove(BUTTERFLY, null, makeCoordinate(0, 1));
		
		gameRed.makeMove(BUTTERFLY, makeCoordinate(0, 0), makeCoordinate(0, -1));	
	}
	
	@Test(expected = HantoException.class) // 30
	public void redMakesSecondMoveThatIsAPieceTypeMismatch() throws HantoException
    {
		gameRed.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		gameRed.makeMove(BUTTERFLY, null, makeCoordinate(0, 1));
		
		gameRed.makeMove(SPARROW, makeCoordinate(0, 0), makeCoordinate(0, -1));	
	}
	
	@Test(expected = HantoException.class) // 31
	public void redAttemptsInvalidSlideTopRight() throws HantoException
    {
		gameRed.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		gameRed.makeMove(BUTTERFLY, null, makeCoordinate(0, 1));
		
		gameRed.makeMove(SPARROW, null, makeCoordinate(1, -1));	
		gameRed.makeMove(SPARROW, null, makeCoordinate(0, 2));	
		
		gameRed.makeMove(BUTTERFLY, makeCoordinate(0, 0), makeCoordinate(1, 0));

	}
	
	@Test(expected = HantoException.class) // 32
	public void redAttemptsInvalidSlideDown() throws HantoException
    {
		gameRed.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		gameRed.makeMove(BUTTERFLY, null, makeCoordinate(0, 1));
		
		gameRed.makeMove(SPARROW, null, makeCoordinate(1, -1));	
		gameRed.makeMove(SPARROW, null, makeCoordinate(0, 2));	
		
		gameRed.makeMove(SPARROW, null, makeCoordinate(-1, 0));
		gameRed.makeMove(SPARROW, null, makeCoordinate(0, 3));
		
		gameRed.makeMove(BUTTERFLY, makeCoordinate(0, 0), makeCoordinate(0, -1));
	}
	
	@Test(expected = HantoException.class) // 33
	public void redAttemptsInvalidSlideDownRight() throws HantoException
    {
		gameRed.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		gameRed.makeMove(BUTTERFLY, null, makeCoordinate(1, 0));
		
		gameRed.makeMove(SPARROW, null, makeCoordinate(0, -1));	
		gameRed.makeMove(SPARROW, null, makeCoordinate(1, 1));	
		
		gameRed.makeMove(SPARROW, null, makeCoordinate(-1, 1));
		gameRed.makeMove(SPARROW, null, makeCoordinate(0, 2));
		
		gameRed.makeMove(BUTTERFLY, makeCoordinate(0, 0), makeCoordinate(1, -1));
	}
	
	@Test(expected = HantoException.class) // 34
	public void redMovesBeforeButterflyPlaced() throws HantoException
    {
		gameRed.makeMove(SPARROW, null, makeCoordinate(0, 0));
		gameRed.makeMove(BUTTERFLY, null, makeCoordinate(1, 0));
		
		gameRed.makeMove(SPARROW, makeCoordinate(0, 0), makeCoordinate(0, 1));
    }
	
	@Test(expected = HantoException.class) //35
	public void redTriesToResign() throws HantoException {
		game.makeMove(null, null, null);
	}
	
	//Pollices tests
	@Test
	public void bluePlacesButterflyFirst() throws HantoException
	{
		final MoveResult mr = game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		assertEquals(OK, mr);
		final HantoPiece piece = game.getPieceAt(makeCoordinate(0, 0));
		assertEquals(BLUE, piece.getColor());
		assertEquals(BUTTERFLY, piece.getType());
	}
	
	@Test
	public void redPlacesSparrowFirst() throws HantoException
	{
		game = factory.makeHantoGame(HantoGameID.GAMMA_HANTO, RED);
		final MoveResult mr = game.makeMove(SPARROW, null, makeCoordinate(0, 0));
		assertEquals(OK, mr);
	}
	
	@Test
	public void blueMovesSparrow() throws HantoException
	{
		final MoveResult mr = makeMoves(md(BUTTERFLY, 0, 0), md(BUTTERFLY, 0, 1), md(SPARROW, 0, -1),
				md(SPARROW, 0, 2), md(SPARROW, 0, -1, -1, 0));
		assertEquals(OK, mr);
		checkPieceAt(-1, 0, BLUE, SPARROW);
	}
	
	@Test(expected=HantoException.class)
	public void moveToDisconnectConfiguration() throws HantoException
	{
		makeMoves(md(BUTTERFLY, 0, 0), md(BUTTERFLY, 0, 1),
				  md(BUTTERFLY, 0, 0, 0, -1));
	}
	
	@Test(expected=HantoException.class)
	public void moveButterflyToSameHex() throws HantoException
	{
		makeMoves(md(BUTTERFLY, 0, 0), md(BUTTERFLY, 0, 1), md(BUTTERFLY, 0, 0, 0, 0));
	}
	
	@Test(expected=HantoException.class)
	public void moveSparrowToOccupiedHex() throws HantoException
	{
		makeMoves(md(BUTTERFLY, 0, 0), md(BUTTERFLY, 0, 1), md(SPARROW, 0, -1),
				md(SPARROW, 0, 2), md(SPARROW, 0, -1, 0, 0));
	}
	
	@Test(expected=HantoException.class)
	public void moveFromEmptyHex() throws HantoException
	{
		makeMoves(md(BUTTERFLY, 0, 0), md(BUTTERFLY, 0, 1), md(BUTTERFLY, 1, 0, 1, -1));
	}
	
	@Test(expected=HantoException.class)
	public void tryToMoveTooFar() throws HantoException
	{
		makeMoves(md(BUTTERFLY, 0, 0), md(BUTTERFLY, 0, 1), md(BUTTERFLY, 0, 0, -1, 2));
	}
	
	@Test(expected=HantoException.class)
	public void tryToMoveWrongPieceType() throws HantoException
	{
		makeMoves(md(BUTTERFLY, 0, 0), md(BUTTERFLY, 0, 1), md(SPARROW, 0, -1),
				md(SPARROW, 0, 2), md(BUTTERFLY, 0, -1, -1, 0));
	}
	
	@Test(expected=HantoException.class)
	public void tryToMoveWrongColorPiece() throws HantoException
	{
		makeMoves(md(BUTTERFLY, 0, 0), md(BUTTERFLY, 0, 1), md(SPARROW, 0, -1),
				md(SPARROW, 0, 2), md(SPARROW, 0, 2, 1, 1));
	}
	
	@Test(expected=HantoException.class)
	public void tryToMoveWhenNotEnoughSpace() throws HantoException
	{
		makeMoves(md(BUTTERFLY, 0, 0), md(BUTTERFLY, 0, 1), 
				md(SPARROW, -1, 0), md(SPARROW, 0, 2),
				md(SPARROW, 1, -1), md(SPARROW, 0, 3),
				md(BUTTERFLY, 0, 0, 0, -1));
	}
	
	@Test(expected=HantoException.class)
	public void tryToUseTooManyButterflies() throws HantoException
	{
		makeMoves(md(BUTTERFLY, 0, 0), md(BUTTERFLY, 0, 1), md(BUTTERFLY, 0, -1));
	}
	
	@Test(expected=HantoException.class)
	public void tryToUseTooManySparrows() throws HantoException
	{
		makeMoves(md(BUTTERFLY, 0, 0), md(BUTTERFLY, 0, 1), 
				md(SPARROW, 0, -1), md(SPARROW, 0, 2),
				md(SPARROW, 0, -2), md(SPARROW, 0, 3),
				md(SPARROW, 0, -3), md(SPARROW, 0, 4),
				md(SPARROW, 0, -4), md(SPARROW, 0, 5),
				md(SPARROW, 0, -5), md(SPARROW, 0, 6),
				md(SPARROW, 0, -6));
	}
	
	@Test(expected=HantoException.class)
	public void tryToUsePieceNotInGame() throws HantoException
	{
		makeMoves(md(BUTTERFLY, 0, 0), md(BUTTERFLY, 0, 1), 
				md(CRANE, 0, -1));
	}
	
	@Test
	public void blueWins() throws HantoException
	{
		MoveResult mr = makeMoves(md(BUTTERFLY, 0, 0), md(BUTTERFLY, 0, 1),
				md(SPARROW, -1, 0), md(SPARROW, 1, 1),
				md(SPARROW, 1, -1), md(SPARROW, 0, 2),
				md(SPARROW, 1, -1, 1, 0), md(SPARROW, -1, 2),
				md(SPARROW, -1, 0, -1, 1));
		assertEquals(BLUE_WINS, mr);
	}
	
	@Test
	public void redSelfLoses() throws HantoException
	{
		MoveResult mr = makeMoves(md(BUTTERFLY, 0, 0), md(BUTTERFLY, 0, 1),
				md(SPARROW, -1, 0), md(SPARROW, 0, 2),
				md(SPARROW, 1, -1), md(SPARROW, 1, 2),
				md(SPARROW, 1, -1, 1, 0), md(SPARROW, -1, 2),
				md(SPARROW, -1, 0, -1, 1), md(SPARROW, 1, 2, 1, 1));
		assertEquals(BLUE_WINS, mr);
	}
	
	@Test(expected=HantoException.class)
	public void tryToPlacePieceNextToOpponent() throws HantoException
	{
		makeMoves(md(BUTTERFLY, 0, 0), md(BUTTERFLY, 0, 1),
				md(SPARROW, -1, 0), md(SPARROW, -2, 0));
	}
	
	@Test
	public void drawAfterTwentyTurns() throws HantoException
	{
		MoveResult mr = makeMoves(
				md(BUTTERFLY, 0, 0), md(BUTTERFLY, 0, 1),
				md(SPARROW, 1, -1), md(SPARROW, -1, 2),
				md(SPARROW, 1, -1, 0, -1), md(SPARROW, -1, 2, 0, 2),
				md(SPARROW, 0, -1, 1, -1), md(SPARROW, 0, 2, -1, 2),
				md(SPARROW, 1, -1, 0, -1), md(SPARROW, -1, 2, 0, 2),
				md(SPARROW, 0, -1, 1, -1), md(SPARROW, 0, 2, -1, 2),
				md(SPARROW, 1, -1, 0, -1), md(SPARROW, -1, 2, 0, 2),
				md(SPARROW, 0, -1, 1, -1), md(SPARROW, 0, 2, -1, 2),
				md(SPARROW, 1, -1, 0, -1), md(SPARROW, -1, 2, 0, 2),
				md(SPARROW, 0, -1, 1, -1), md(SPARROW, 0, 2, -1, 2),
				md(SPARROW, 1, -1, 0, -1), md(SPARROW, -1, 2, 0, 2),
				md(SPARROW, 0, -1, 1, -1), md(SPARROW, 0, 2, -1, 2),
				md(SPARROW, 1, -1, 0, -1), md(SPARROW, -1, 2, 0, 2),
				md(SPARROW, 0, -1, 1, -1), md(SPARROW, 0, 2, -1, 2),
				md(SPARROW, 1, -1, 0, -1), md(SPARROW, -1, 2, 0, 2),
				md(SPARROW, 0, -1, 1, -1), md(SPARROW, 0, 2, -1, 2),
				md(SPARROW, 1, -1, 0, -1), md(SPARROW, -1, 2, 0, 2),
				md(SPARROW, 0, -1, 1, -1), md(SPARROW, 0, 2, -1, 2),
				md(SPARROW, 1, -1, 0, -1), md(SPARROW, -1, 2, 0, 2),
				md(SPARROW, 0, -1, 1, -1), md(SPARROW, 0, 2, -1, 2));
		assertEquals(DRAW, mr);
	}
	
	@Test(expected=HantoException.class)
	public void butterflyNotPlacedByFourthMoveByFirstPlayer() throws HantoException
	{
		makeMoves(md(SPARROW, 0, 0), md(SPARROW, 0, 1),
				md(SPARROW, 0, -1), md(SPARROW, 0, 2),
				md(SPARROW, 0, -2), md(SPARROW, 0, 3),
				md(SPARROW, 0, -3));
	}
	
	@Test(expected=HantoException.class)
	public void butterflyNotPlacedByFourthMoveBySecondPlayer() throws HantoException
	{
		makeMoves(md(SPARROW, 0, 0), md(SPARROW, 0, 1),
				md(BUTTERFLY, 0, -1), md(SPARROW, 0, 2),
				md(SPARROW, 0, -2), md(SPARROW, 0, 3),
				md(SPARROW, 0, -3), md(SPARROW, 0, 4));
	}
	
	@Test(expected=HantoException.class)
	public void tryToMoveAfterGameIsOver() throws HantoException
	{
		makeMoves(md(BUTTERFLY, 0, 0), md(BUTTERFLY, 0, 1),
				md(SPARROW, -1, 0), md(SPARROW, 1, 1),
				md(SPARROW, 1, -1), md(SPARROW, 0, 2),
				md(SPARROW, 1, -1, 1, 0), md(SPARROW, -1, 2),
				md(SPARROW, -1, 0, -1, 1), md(SPARROW, 0, 3));
	}
	
	@Test(expected=HantoException.class)
	public void extraCreditMoveSparrowBeforeButterflyIsOnBoard() throws HantoException
	{
		makeMoves(md(SPARROW, 0, 0), md (BUTTERFLY, 0, 1));
		final HantoPiece piece = game.getPieceAt(makeCoordinate(0, 0));
		assertEquals(SPARROW, piece.getType());
		assertEquals(BLUE, piece.getColor());
		
		makeMoves(md(SPARROW, 0, 0), md (BUTTERFLY, 0, 1),
				  md(SPARROW, 0, 1));
	}
	
	// Helper methods
	private HantoCoordinate makeCoordinate(int x, int y)
	{
		return new TestHantoCoordinate(x, y);
	}
	
	/**
	 * Make sure that the piece at the location is what you expect
	 * @param x x-coordinate
	 * @param y y-coordinate
	 * @param color piece color expected
	 * @param type piece type expected
	 */
	private void checkPieceAt(int x, int y, HantoPlayerColor color, HantoPieceType type)
	{
		final HantoPiece piece = game.getPieceAt(makeCoordinate(x, y));
		assertEquals(color, piece.getColor());
		assertEquals(type, piece.getType());
	}
	
	/**
	 * Make a MoveData object given the piece type and the x and y coordinates of the
	 * desstination. This creates a move data that will place a piece (source == null)
	 * @param type piece type
	 * @param toX destination x-coordinate
	 * @param toY destination y-coordinate
	 * @return the desitred MoveData object
	 */
	private MoveData md(HantoPieceType type, int toX, int toY) 
	{
		return new MoveData(type, null, makeCoordinate(toX, toY));
	}
	
	private MoveData md(HantoPieceType type, int fromX, int fromY, int toX, int toY)
	{
		return new MoveData(type, makeCoordinate(fromX, fromY), makeCoordinate(toX, toY));
	}
	
	/**
	 * Make the moves specified. If there is no exception, return the move result of
	 * the last move.
	 * @param moves
	 * @return the last move result
	 * @throws HantoException
	 */
	private MoveResult makeMoves(MoveData... moves) throws HantoException
	{
		MoveResult mr = null;
		for (MoveData md : moves) {
			mr = game.makeMove(md.type, md.from, md.to);
		}
		return mr;
	}
}
