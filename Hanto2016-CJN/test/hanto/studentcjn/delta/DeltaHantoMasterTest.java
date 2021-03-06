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

package hanto.studentcjn.delta;

import static hanto.common.HantoPieceType.*;
import static hanto.common.MoveResult.*;
import static hanto.common.HantoPlayerColor.*;
import static org.junit.Assert.*;
import hanto.common.*;
import hanto.studentcjn.HantoGameFactory;
import hanto.studentcjn.common.HantoCoordinateImpl;
import hanto.studentcjn.delta.DeltaHantoMasterTest.MoveData;
import hanto.studentcjn.delta.DeltaHantoMasterTest.TestHantoCoordinate;

import org.junit.*;

/**
 * Test cases for GammaHanto.
 * @version April 7, 2016
 */
public class DeltaHantoMasterTest
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
		game = factory.makeHantoGame(HantoGameID.DELTA_HANTO, BLUE);
		gameRed = factory.makeHantoGame(HantoGameID.DELTA_HANTO, RED);
		
	}
	
	@Test //1
	public void bluePlacesCrabFirstTurn() throws HantoException {
		makeMoves(md(CRAB, 0, 0));
		checkPieceAt(0, 0, BLUE, CRAB);
	}
	
	@Test(expected=HantoException.class) //2
	public void blueMoveCrabSecondTurnBeforeButterflyPlaced() throws HantoException {
		makeMoves(md(CRAB, 0, 0), md(CRAB, 0, -1),
				  md(CRAB, 0, 0, 1, -1));
	}
	
	@Test //3
	public void blueResignsFirstMove() throws HantoException {
		MoveResult mr = game.makeMove(null, null, null);
		assertEquals(RED_WINS, mr);
	}
	
	@Test //3
	public void redResignsThirdMove() throws HantoException {
		makeMoves(md(CRAB, 0, 0), md(CRAB, 0, -1),
				  md(CRAB, 0, 1));
		MoveResult mr = game.makeMove(null, null, null);
		assertEquals(BLUE_WINS, mr);
	}
	
	@Test(expected=HantoException.class) //4
	public void bluePlaceTooManyCrabs() throws HantoException {
		makeMoves(md(BUTTERFLY, 0, 0), md(BUTTERFLY, 0, -1),
				  md(CRAB, 0, 1), md(CRAB, 0, -2),
				  md(CRAB, 0, 2), md(CRAB, 0, -3),
				  md(CRAB, 0, 3), md(CRAB, 0, -4),
				  md(CRAB, 0, 4), md(CRAB, 0, -5),
				  md(CRAB, 0, 5));
	}
	
	@Test(expected=HantoException.class) //5
	public void bluePlaceTooManySparrows() throws HantoException {
		makeMoves(md(BUTTERFLY, 0, 0), md(BUTTERFLY, 0, -1),
				  md(SPARROW, 0, 1), md(CRAB, 0, -2),
				  md(SPARROW, 0, 2), md(CRAB, 0, -3),
				  md(SPARROW, 0, 3), md(CRAB, 0, -4),
				  md(SPARROW, 0, 4), md(CRAB, 0, -5),
				  md(SPARROW, 0, 5));
	}
	
	@Test(expected=HantoException.class) //6
	public void bluePlaceTooManyButterflies() throws HantoException {
		makeMoves(md(BUTTERFLY, 0, 0), md(BUTTERFLY, 0, -1),
				  md(BUTTERFLY, 0, 1));
	}
	
	@Test(expected=HantoException.class) //7
	public void bluePlacesAtNonOriginFirstMove() throws HantoException {
		makeMoves(md(BUTTERFLY, 0, 1));
	}
	
	@Test(expected=HantoException.class) //8
	public void bluePlacesInvalidPieceFirstMove() throws HantoException {
		makeMoves(md(HORSE, 0, 1));
	}
	
	@Test //9
	public void printBoardTest() throws HantoException {
		makeMoves(md(BUTTERFLY, 0, 0));
		assertNotNull(game.getPrintableBoard());
		
	}
	
	@Test //10
	public void coordsAreEqualTest() throws HantoException {
		HantoCoordinate coord1 = new HantoCoordinateImpl(0,0);
		HantoCoordinate coord2 = new HantoCoordinateImpl(0,0);
		assertTrue(coord1.equals(coord2));
		
		assertTrue(coord1.equals(coord1));
		
		assertFalse(coord1.equals(0));	
	}
	
	@Test //11
	public void blueMoveSparrowThirdTurn() throws HantoException {
		makeMoves(md(BUTTERFLY, 0, 0), md(BUTTERFLY, 0, -1),
				  md(SPARROW, 0, 1), md(SPARROW, 0, -2));
		
		MoveResult mr = game.makeMove(SPARROW, makeCoordinate(0, 1), makeCoordinate(0, -3));
		assertEquals(OK, mr);
	}
	
	@Test(expected=HantoException.class) //12
	public void blueMoveSparrowThirdTurnMakingDisconnect() throws HantoException {
		makeMoves(md(BUTTERFLY, 0, 0), md(BUTTERFLY, 0, -1),
				  md(SPARROW, 0, 1), md(SPARROW, 0, -2),
				  md(SPARROW, 0,1, 0, -2));
	}
	
	@Test //13
	public void blueMovesCrabOneSpaceThirdTurn() throws HantoException {
		makeMoves(md(BUTTERFLY, 0, 0), md(BUTTERFLY, 0, -1),
				  md(CRAB, 0, 1), md(SPARROW, 0, -2));
		
		MoveResult mr = game.makeMove(CRAB, makeCoordinate(0, 1), makeCoordinate(1, 0));
		assertEquals(OK, mr);
	}
	
	@Test //14
	public void blueMovesCrabTwoSpacesThirdTurn() throws HantoException {
		makeMoves(md(BUTTERFLY, 0, 0), md(BUTTERFLY, 0, -1),
				  md(CRAB, 0, 1), md(SPARROW, 0, -2));
		
		MoveResult mr = game.makeMove(CRAB, makeCoordinate(0, 1), makeCoordinate(1, -1));
		assertEquals(OK, mr);
	}
	
	@Test //15
	public void blueMovesCrabThreeSpacesThirdTurn() throws HantoException {
		makeMoves(md(BUTTERFLY, 0, 0), md(BUTTERFLY, 0, -1),
				  md(CRAB, 0, 1), md(SPARROW, 0, -2));
		
		MoveResult mr = game.makeMove(CRAB, makeCoordinate(0, 1), makeCoordinate(1, -2));
		assertEquals(OK, mr);
	}
	
	@Test(expected=HantoException.class) //16
	public void blueMovesCrabFourSpacesThirdTurn() throws HantoException {
		makeMoves(md(BUTTERFLY, 0, 0), md(BUTTERFLY, 0, -1),
				  md(CRAB, 0, 1), md(SPARROW, 0, -2));
		
		game.makeMove(CRAB, makeCoordinate(0, 1), makeCoordinate(1, -3));
	}
	
	@Test(expected=HantoException.class) //17
	public void blueMovesCrabThreeSpacesThirdTurnMakingDisconnect() throws HantoException {
		makeMoves(md(BUTTERFLY, 0, 0), md(BUTTERFLY, 0, -1),
				  md(CRAB, 0, 1), md(SPARROW, 0, -2));
		
		MoveResult mr = game.makeMove(CRAB, makeCoordinate(0, 1), makeCoordinate(2, -2));
		assertEquals(OK, mr);
	}

	@Test(expected=HantoException.class) //18
	public void redCrabTriesMovingAcrossInvalidGapThatIsThreeSpaces() throws HantoException {
		makeMoves(md(BUTTERFLY, 0, 0), md(BUTTERFLY, 0, -1),
				  md(SPARROW, 0, 1), md(SPARROW, -1, -1),
				  md(SPARROW, -1, 2), md(CRAB, -2, -1),
				  md(CRAB, -2, 3));
		
		game.makeMove(CRAB, makeCoordinate(-2, -1), makeCoordinate(-2, 2));
	}
	
	@Test //19
	public void redCrabTriesMovingThreeSpaces() throws HantoException {
		makeMoves(md(BUTTERFLY, 0, 0), md(BUTTERFLY, 0, -1),
				  md(SPARROW, 0, 1), md(SPARROW, -1, -1),
				  md(SPARROW, -1, 2), md(CRAB, -2, -1),
				  md(CRAB, -2, 3));
		
		MoveResult mr = game.makeMove(CRAB, makeCoordinate(-2, -1), makeCoordinate(-1, 1));
		assertEquals(OK, mr);
	}
	
	//Pollices
	
	@Test
	public void bluePlacesButterflyFirst() throws HantoException
	{
		final MoveResult mr = game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		assertEquals(OK, mr);
		checkPieceAt(0, 0, BLUE, BUTTERFLY);
	}
	
	@Test
	public void redPlacesSparrowFirst() throws HantoException
	{
		game = factory.makeHantoGame(HantoGameID.GAMMA_HANTO, RED);
		final MoveResult mr = game.makeMove(SPARROW, null, makeCoordinate(0, 0));
		assertEquals(OK, mr);
	}
	
	@Test
	public void bluePlacesCrabFirst() throws HantoException
	{
		final MoveResult mr = game.makeMove(CRAB, null, makeCoordinate(0, 0));
		assertEquals(OK, mr);
		checkPieceAt(0, 0, BLUE, CRAB);
	}
	
	@Test
	public void blueMovesCrab1() throws HantoException
	{
		final MoveResult mr = makeMoves(md(BUTTERFLY, 0, 0), md(BUTTERFLY, 0, 1), md(CRAB, 0, -1),
				md(CRAB, 0, 2), md(CRAB, 0, -1, -1, 0));
		assertEquals(OK, mr);
		checkPieceAt(-1, 0, BLUE, CRAB);
	}
	
	@Test(expected=HantoException.class)
	public void moveToDisconnectConfiguration() throws HantoException
	{
		makeMoves(md(BUTTERFLY, 0, 0), md(BUTTERFLY, 0, 1), md(BUTTERFLY, 0, 0, 0, -1));
	}
	
	@Test(expected=HantoException.class)
	public void moveButterflyToSameHex() throws HantoException
	{
		makeMoves(md(BUTTERFLY, 0, 0), md(BUTTERFLY, 0, 1), md(BUTTERFLY, 0, 0, 0, 0));
	}
	
	@Test(expected=HantoException.class)
	public void moveCrabToOccupiedHex() throws HantoException
	{
		makeMoves(md(BUTTERFLY, 0, 0), md(BUTTERFLY, 0, 1), md(CRAB, 0, -1),
				md(CRAB, 0, 2), md(CRAB, 0, -1, 0, 0));
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
		makeMoves(md(BUTTERFLY, 0, 0), md(BUTTERFLY, 0, 1), md(CRAB, 0, -1),
				md(CRAB, 0, 2), md(SPARROW, 0, -1, -1, 0));
	}
	
	@Test(expected=HantoException.class)
	public void tryToMoveWrongColorPiece() throws HantoException
	{
		makeMoves(md(BUTTERFLY, 0, 0), md(BUTTERFLY, 0, 1), md(CRAB, 0, -1),
				md(CRAB, 0, 2), md(CRAB, 0, 2, 1, 1));
	}
	
	@Test(expected=HantoException.class)
	public void tryToMoveWhenNotEnoughSpace() throws HantoException
	{
		makeMoves(md(BUTTERFLY, 0, 0), md(BUTTERFLY, 0, 1), 
				md(CRAB, -1, 0), md(CRAB, 0, 2),
				md(CRAB, 1, -1), md(CRAB, 0, 3),
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
				md(SPARROW, 0, -5));
	}
	
	@Test(expected=HantoException.class)
	public void tryToUseTooManyCrabs() throws HantoException
	{
		makeMoves(md(BUTTERFLY, 0, 0), md(BUTTERFLY, 0, 1), 
				md(CRAB, 0, -1), md(CRAB, 0, 2),
				md(CRAB, 0, -2), md(CRAB, 0, 3),
				md(CRAB, 0, -3), md(CRAB, 0, 4),
				md(CRAB, 0, -4), md(CRAB, 0, 5),
				md(CRAB, 0, -5));
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
				md(CRAB, -1, 0), md(SPARROW, 1, 1),
				md(CRAB, 1, -1), md(SPARROW, 0, 2),
				md(CRAB, 1, -1, 1, 0), md(SPARROW, -1, 2),
				md(CRAB, -1, 0, -1, 1));
		assertEquals(BLUE_WINS, mr);
	}
	
	@Test
	public void redSelfLoses() throws HantoException
	{
		MoveResult mr = makeMoves(md(BUTTERFLY, 0, 0), md(BUTTERFLY, 0, 1),
				md(CRAB, -1, 0), md(SPARROW, 0, 2),
				md(CRAB, 1, -1), md(CRAB, 1, 2),
				md(CRAB, 1, -1, 1, 0), md(CRAB, -1, 2),
				md(CRAB, -1, 0, -1, 1), md(CRAB, 1, 2, 1, 1));
		assertEquals(BLUE_WINS, mr);
	}
	
	@Test(expected=HantoException.class)
	public void tryToPlacePieceNextToOpponent() throws HantoException
	{
		makeMoves(md(BUTTERFLY, 0, 0), md(BUTTERFLY, 0, 1),
				md(SPARROW, -1, 0), md(SPARROW, -2, 0));
	}
	
	@Test
	public void walkTwoHexes() throws HantoException
	{
		MoveResult mr = makeMoves(md(BUTTERFLY, 0, 0), md(BUTTERFLY, 0, 1),
				md(CRAB, 0, -1), md(CRAB, 0, 2),
				md(CRAB, 0, -1, 1, 0));
		checkPieceAt(1, 0, BLUE, CRAB);
		assertNull(game.getPieceAt(makeCoordinate(0, -1)));
	}
	
	@Test
	public void walkThreeHexes() throws HantoException
	{
		MoveResult mr = makeMoves(md(BUTTERFLY, 0, 0), md(BUTTERFLY, 0, 1),
				md(CRAB, 0, -1), md(CRAB, 0, 2),
				md(CRAB, 0, -1, -1, 2));
		checkPieceAt(-1, 2, BLUE, CRAB);
		assertNull(game.getPieceAt(makeCoordinate(0, -1)));
	}
	
	@Test(expected=HantoException.class)
	public void attemptToWalkFourHexes() throws HantoException
	{
		makeMoves(md(BUTTERFLY, 0, 0), md(BUTTERFLY, 0, 1),
				md(CRAB, 0, -1), md(CRAB, 0, 2),
				md(CRAB, 0, -1, -1, 3));
	}
	
	@Test(expected=HantoException.class)
	public void walkThreeHexesAndDisconnectConfiguration() throws HantoException
	{
		makeMoves(md(BUTTERFLY, 0, 0), md(BUTTERFLY, 0, 1),
				md(CRAB, 0, -1), md(CRAB, 0, 2),
				md(CRAB, 0, -1, 3, -2));
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
	public void attemptToMoveBeforeButterflyIsOnBoard() throws HantoException
	{
		makeMoves(md(CRAB, 0, 0), md (BUTTERFLY, 0, 1),
				md(CRAB, 0, 0, 1, 0));
	}
	
	@Test
	public void blueResignsOnFirstMove() throws HantoException
	{
		assertEquals(RED_WINS, game.makeMove(null, null, null));
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