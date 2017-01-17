package hanto.studentcjn.epsilon;

import static hanto.common.HantoPieceType.*;
import static hanto.common.HantoPlayerColor.*;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import hanto.common.HantoCoordinate;
import hanto.common.HantoException;
import hanto.common.HantoGame;
import hanto.common.HantoGameID;
import hanto.common.HantoPiece;
import hanto.common.HantoPieceType;
import hanto.common.HantoPlayerColor;
import hanto.common.MoveResult;
import hanto.studentcjn.HantoGameFactory;
import hanto.studentcjn.common.BaseHantoGame;
import hanto.studentcjn.common.HantoBoard;
import hanto.studentcjn.common.HantoBoardImpl;
import hanto.studentcjn.common.HantoPieceImpl;
import hanto.studentcjn.common.strategies.FlyingStrategy;
import hanto.studentcjn.common.strategies.JumpingStrategy;
import hanto.studentcjn.common.strategies.WalkingStrategy;
import hanto.studentcjn.tournament.HantoPlayer;
import hanto.tournament.HantoMoveRecord;

public class EpsilonHantoMasterTest {
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

	@BeforeClass
	public static void initializeClass()
	{
		factory = HantoGameFactory.getInstance();
	}
	
	@SuppressWarnings("static-access")
	@Before
	public void setup()
	{
		// By default, blue moves first.
		game = factory.makeHantoGame(HantoGameID.EPSILON_HANTO, BLUE);
		
	}
	
	//My tests
	
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
	
	@Test(expected=HantoException.class) //3
	public void bluePlacesInvalidPieceType() throws HantoException {
		makeMoves(md(CRANE, 0, 0));
	}
	
	@Test(expected=HantoException.class) //4
	public void blueMovesHorseBeforeButterflyPlaced() throws HantoException {
		makeMoves(md(HORSE, 0, 0), md(BUTTERFLY, 0, -1),
				  md(HORSE, 0, 0, 0, -2));
	}
	
	@Test //5
	public void blueMovesHorseThirdTurnDown() throws HantoException {
		makeMoves(md(BUTTERFLY, 0, 0), md(BUTTERFLY, 0, -1),
				  md(HORSE, 0, 1), md(CRAB, 0, -2),
				  md(HORSE, 0, 1, 0, -3));
		checkPieceAt(0, -3, BLUE, HORSE);
	}
	
	@Test(expected=HantoException.class) //6
	public void blueMakesInvalidMoveWithHorseThirdTurn() throws HantoException {
		makeMoves(md(BUTTERFLY, 0, 0), md(BUTTERFLY, 0, -1),
				  md(HORSE, 0, 1), md(CRAB, 0, -2),
				  md(HORSE, 0, 1, 1, -3));
	}
	
	@Test //7
	public void blueMovesHorseThirdTurnUpRight() throws HantoException {
		makeMoves(md(BUTTERFLY, 0, 0), md(BUTTERFLY, 1, 0),
				  md(HORSE, -1, 0), md(CRAB, 2, 0),
				  md(HORSE, -1, 0, 3, 0));
		checkPieceAt(3, 0, BLUE, HORSE);
	}
	
	@Test //8
	public void blueMovesHorseThirdTurnDownLeft() throws HantoException {
		makeMoves(md(BUTTERFLY, 0, 0), md(BUTTERFLY, -1, 0),
				  md(HORSE, 1, 0), md(CRAB, -2, 0),
				  md(HORSE, 1, 0, -3, 0));
		checkPieceAt(-3, 0, BLUE, HORSE);
	}
	
	@Test //9
	public void blueMovesHorseThirdTurnUp() throws HantoException {
		makeMoves(md(BUTTERFLY, 0, 0), md(BUTTERFLY, 0, 1),
				  md(HORSE, 0, -1), md(CRAB, 0, 2),
				  md(HORSE, 0, -1, 0, 3));
		checkPieceAt(0, 3, BLUE, HORSE);
	}
	
	@Test //10
	public void blueMovesHorseThirdTurnUpLeft() throws HantoException {
		makeMoves(md(BUTTERFLY, 0, 0), md(BUTTERFLY, -1, 1),
				  md(HORSE, 1, -1), md(CRAB, -2, 2),
				  md(HORSE, 1, -1, -3, 3));
		checkPieceAt(-3, 3, BLUE, HORSE);
	}
	
	@Test //11
	public void blueMovesHorseThirdTurnDownRight() throws HantoException {
		makeMoves(md(BUTTERFLY, 0, 0), md(BUTTERFLY, 1, -1),
				  md(HORSE, -1, 1), md(CRAB, 2, -2),
				  md(HORSE, -1, 1, 3, -3));
		checkPieceAt(3, -3, BLUE, HORSE);
	}
	
	@Test //12
	public void blueMovesSparrowThirdTurn() throws HantoException {
		makeMoves(md(BUTTERFLY, 0, 0), md(BUTTERFLY, 1, -1),
				  md(SPARROW, -1, 1), md(CRAB, 2, -2),
				  md(SPARROW, -1, 1, 3, -3));
		checkPieceAt(3, -3, BLUE, SPARROW);
	}
	
	@Test(expected=HantoException.class) //13
	public void blueMovesSparrowToFar() throws HantoException {
		makeMoves(md(BUTTERFLY, 0, 0), md(BUTTERFLY, 1, -1),
				  md(SPARROW, -1, 1), md(CRAB, 2, -2),
				  md(SPARROW, -2, 2), md(CRAB, 3, -3),
				  md(SPARROW, -2, 2, 4, -4));
	}
	
	@Test(expected=HantoException.class) // 14
	public void bluePlacesTooManyHorses() throws HantoException {
		makeMoves(md(BUTTERFLY, 0, 0), md(BUTTERFLY, 1, -1),
				  md(HORSE, -1, 1), md(CRAB, 2, -2),
				  md(HORSE, -2, 2), md(CRAB, 3, -3),
				  md(HORSE, -3, 3), md(CRAB, 4, -4),
				  md(HORSE, -4, 4), md(CRAB, 5, -5),
				  md(HORSE, -5, 5));
	}
	
	@Test(expected=HantoException.class) //15
	public void blueMovesHorseOverGapUp() throws HantoException {
		makeMoves(md(BUTTERFLY, 0, 0), md(BUTTERFLY, 1, 0),
				  md(HORSE, 0, -1), md(CRAB, 1, 1),
				  md(HORSE, 0, -1, 0, 2));
		checkPieceAt(0, 2, BLUE, HORSE);
	}
	
	@Test(expected=HantoException.class) //16
	public void blueMovesHorseOverGapDown() throws HantoException {
		makeMoves(md(BUTTERFLY, 0, 0), md(BUTTERFLY, 1, -1),
				  md(HORSE, 0, 1), md(CRAB, 1, -2),
				  md(HORSE, 0, 1, 0, -2));
		checkPieceAt(0, -2, BLUE, HORSE);
	}
	
	@Test(expected=HantoException.class) //17
	public void blueMovesHorseOverGapUpRight() throws HantoException {
		makeMoves(md(BUTTERFLY, 0, 0), md(BUTTERFLY, 1, -1),
				  md(HORSE, -1, 0), md(CRAB, 2, -1),
				  md(HORSE, -1, 0, 2, 0));
		checkPieceAt(2, 0, BLUE, HORSE);
	}
	
	@Test //18
	public void blueMovesHorseOverValidUpRight() throws HantoException {
		makeMoves(md(BUTTERFLY, 0, 0), md(BUTTERFLY, 1, -1),
				  md(HORSE, -1, 0), md(CRAB, 2, -1),
				  md(HORSE, -1, 0, 1, 0));
		checkPieceAt(1, 0, BLUE, HORSE);
	}
	
	@Test //19
	public void blueMovesCrabOneSpace() throws HantoException {
		makeMoves(md(BUTTERFLY, 0, 0), md(BUTTERFLY, 0, -1),
				  md(CRAB, 0, 1), md(CRAB, 0, -2),
				  md(CRAB, 0, 1, 1, 0));
		checkPieceAt(1, 0, BLUE, CRAB);
	}
	
	@Test(expected=HantoException.class) //20
	public void blueMovesCrabTwoSpaces() throws HantoException {
		makeMoves(md(BUTTERFLY, 0, 0), md(BUTTERFLY, 0, -1),
				  md(CRAB, 0, 1), md(CRAB, 0, -2),
				  md(CRAB, 0, 1, 1, -1));
	}
	
	@Test(expected=HantoException.class) //21
	public void blueResignsFirstMove() throws HantoException {
		game.makeMove(null, null, null);
	}
	
	@Test(expected=HantoException.class) //22
	public void redResignsSecondMove() throws HantoException {
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(null, null, null);
	}
	
	@Test(expected=HantoException.class) //23
	public void blueResignsThirdMove() throws HantoException {
		game.makeMove(HORSE, null, makeCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 1));
		game.makeMove(null, null, null);
	}
	
	@Test(expected=HantoException.class) //24
	public void blueResignsThirdMoveCrab() throws HantoException {
		game.makeMove(CRAB, null, makeCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 1));
		game.makeMove(null, null, null);
	}
	
	@Test(expected=HantoException.class) //25
	public void blueResignsFifthMove() throws HantoException {
		game.makeMove(SPARROW, null, makeCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 1));
		game.makeMove(SPARROW, null, makeCoordinate(0, -1));
		game.makeMove(SPARROW, null, makeCoordinate(0, 2));
		game.makeMove(null, null, null);
	}
	
	@Test //26
	public void testFlyGenerationListNotEmpty() throws HantoException {
		HantoBoard board = new HantoBoardImpl();
		
		board.placePieceAtCoord(null, makeCoordinate(0, 0), new HantoPieceImpl(BLUE, SPARROW));
		
		List<HantoMoveRecord> records = new FlyingStrategy(4).generateMoveList(makeCoordinate(0, 0), board, SPARROW);
		
		assertNotNull(records);
	}
	
	@Test //27
	public void testWalkGenerationListNotEmpty() throws HantoException {
		HantoBoard board = new HantoBoardImpl();
		
		board.placePieceAtCoord(null, makeCoordinate(0, 0), new HantoPieceImpl(BLUE, BUTTERFLY));
		
		List<HantoMoveRecord> records = new WalkingStrategy(1).generateMoveList(makeCoordinate(0, 0), board, BUTTERFLY);
		
		assertNotNull(records);
	}
	
	@Test //28
	public void testJumpGenerationListNotEmpty() throws HantoException {
		HantoBoard board = new HantoBoardImpl();
		
		board.placePieceAtCoord(null, makeCoordinate(0, 0), new HantoPieceImpl(BLUE, HORSE));
		board.placePieceAtCoord(null, makeCoordinate(0, 1), new HantoPieceImpl(BLUE, HORSE));
		board.placePieceAtCoord(null, makeCoordinate(-1, 1), new HantoPieceImpl(BLUE, HORSE));
		board.placePieceAtCoord(null, makeCoordinate(-1, 0), new HantoPieceImpl(BLUE, HORSE));
		board.placePieceAtCoord(null, makeCoordinate(1, -1), new HantoPieceImpl(BLUE, HORSE));
		board.placePieceAtCoord(null, makeCoordinate(1, 0), new HantoPieceImpl(BLUE, HORSE));
		board.placePieceAtCoord(null, makeCoordinate(0, -1), new HantoPieceImpl(BLUE, HORSE));
		
		List<HantoMoveRecord> records = new JumpingStrategy(0).generateMoveList(makeCoordinate(0, 0), board, HORSE);
		
		assertNotNull(records);
	}
	
	@Test //29
	public void testMoveGeneration() throws HantoException {
		game.makeMove(SPARROW, null, makeCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 1));
		game.makeMove(SPARROW, null, makeCoordinate(0, -1));
		game.makeMove(SPARROW, null, makeCoordinate(0, 2));
		
		List<HantoMoveRecord> record = ((BaseHantoGame) game).generateCanMoveList();
		
		assertFalse(record.isEmpty());
	}
	
	@Test //30
	public void testPlaceGeneration() throws HantoException {
		game.makeMove(SPARROW, null, makeCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 1));
		game.makeMove(SPARROW, null, makeCoordinate(0, -1));
		game.makeMove(SPARROW, null, makeCoordinate(0, 2));
		
		List<HantoMoveRecord> record = ((BaseHantoGame) game).generateCanPlaceList();
		
		assertFalse(record.isEmpty());
	}
	
	@Test //31
	public void testHantoBoardCanPlaceListForSecondTurn() throws HantoException {
		game.makeMove(SPARROW, null, makeCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 1));
		game.makeMove(SPARROW, null, makeCoordinate(0, -1));
		game.makeMove(SPARROW, null, makeCoordinate(0, 2));
		
		
		HantoBoard board = new HantoBoardImpl();
		List<HantoCoordinate> coord = board.generateCanPlaceCoordinates(true, false, false);
		
		assertEquals(6, coord.size());
	}
	
	@Test(expected=HantoException.class) //32
	public void testSurrenderMethod() throws HantoException {
		
		@SuppressWarnings("unused")
		MoveResult mr = ((BaseHantoGame) game).surrenderCase();
	}
	
	@Test//32
	public void testHantoPlayerFirstMove() throws HantoException {
		HantoPlayer player = new HantoPlayer();
		player.startGame(HantoGameID.EPSILON_HANTO, HantoPlayerColor.BLUE, true);
		assertEquals(HantoPieceType.BUTTERFLY, player.makeMove(null).getPiece());
	}
	
	@Test//32
	public void testHantoPlayerSecondMove() throws HantoException {
		
		HantoPlayer player = new HantoPlayer();
		player.startGame(HantoGameID.EPSILON_HANTO, HantoPlayerColor.BLUE, false);
		assertEquals(HantoPieceType.BUTTERFLY, player.makeMove(new HantoMoveRecord(HantoPieceType.BUTTERFLY, null, makeCoordinate(0, 0))).getPiece());
	}
	
	@Test//32
	public void testHantoPlayerThirdMove() throws HantoException {
		
		HantoPlayer player = new HantoPlayer();
		player.startGame(HantoGameID.EPSILON_HANTO, HantoPlayerColor.BLUE, true);
		player.makeMove(new HantoMoveRecord(HantoPieceType.BUTTERFLY, null, makeCoordinate(0, 0))).getPiece();
		
		assertNotNull(player.makeMove(null));
	
	}
	
	@Test//33
	public void hantoPlayerSuccessfullyResignsWhenNoMovesLeft() throws HantoException {
		makeMoves(md(BUTTERFLY, 0, 0), md(BUTTERFLY, 0, 1),
				  md(BUTTERFLY, 0, 0, 1, 0), md(SPARROW, -1, 1),
				  md(BUTTERFLY, 1, 0, 0, 0), md(SPARROW, 0, 2),
				  md(BUTTERFLY, 0, 0, 1, 0), md(CRAB, -1, 0),
				  md(CRAB, 1, -1), md(CRAB, -1, -1),
				  md(CRAB, 1, -1, 0, 0), md(CRAB, -1, -1, 0, -1),
				  md(BUTTERFLY, 1, 0, 1, -1), md(SPARROW, 0, 2, 2, -1));
		
		MoveResult mr = game.makeMove(null, null, null);
		
		assertEquals(MoveResult.RED_WINS, mr);

	}
	
	@Test //34
	public void playerForcedToResign() throws HantoException {
		makeMoves(md(BUTTERFLY, 0, 0), md(BUTTERFLY, 0, 1),
				  md(BUTTERFLY, 0, 0, 1, 0), md(SPARROW, -1, 1),
				  md(BUTTERFLY, 1, 0, 0, 0), md(SPARROW, 0, 2),
				  md(BUTTERFLY, 0, 0, 1, 0), md(CRAB, -1, 0),
				  md(CRAB, 1, -1), md(CRAB, -1, -1),
				  md(CRAB, 1, -1, 0, 0), md(CRAB, -1, -1, 0, -1),
				  md(BUTTERFLY, 1, 0, 1, -1), md(SPARROW, 0, 2, 2, -1));
		
		HantoPlayer player = new HantoPlayer(game, false, true);
		HantoMoveRecord record = player.makeMove(null);
		
		assertEquals(null, record.getPiece());

	}
	
	@Test //35
	public void testButterFlyMovementGeneration() throws HantoException {
		makeMoves(md(BUTTERFLY, 0, 0), md(BUTTERFLY, 1, 0),
				md(BUTTERFLY, 0, 0, 0, 1), md(SPARROW, 1, -1));
		
		List<HantoMoveRecord> records = ((BaseHantoGame) game).generateCanMoveList();
//		System.out.println("+++++++++++");
//		for(HantoMoveRecord record: records) {
//			System.out.println(record.getPiece() + "(" + record.getFrom().getX() + ", " + record.getFrom().getY() + ")" + "(" + record.getTo().getX() + ", " + record.getTo().getY() + ")");
//		}
		
	}
	
	@Test //36
	public void testGameFactory() throws HantoException {
		
		game = factory.makeHantoGame(HantoGameID.EPSILON_HANTO);
		
		assertNotNull(game);
	}
	
	@Test //36
	public void testGameFactoryWithBadId() throws HantoException {
		
		game = factory.makeHantoGame(HantoGameID.ALPHA_HANTO, HantoPlayerColor.BLUE);
		assertNull(game);
	}
	
	
	//pollice's tests
	
	//helper functions
	
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
