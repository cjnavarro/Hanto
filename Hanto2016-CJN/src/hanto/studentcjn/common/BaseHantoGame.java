/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design. The course was
 * taken at Worcester Polytechnic Institute. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License
 * v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *  
 * Copyright ©2016 Chris J. Navarro
 ********************************************************************************/

package hanto.studentcjn.common;

import static hanto.common.HantoPieceType.*;
import static hanto.common.HantoPlayerColor.*;
import static hanto.common.MoveResult.BLUE_WINS;
import static hanto.common.MoveResult.DRAW;
import static hanto.common.MoveResult.OK;
import static hanto.common.MoveResult.RED_WINS;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import hanto.common.HantoCoordinate;
import hanto.common.HantoException;
import hanto.common.HantoGame;
import hanto.common.HantoPiece;
import hanto.common.HantoPieceType;
import hanto.common.HantoPlayerColor;
import hanto.common.HantoPrematureResignationException;
import hanto.common.MoveResult;
import hanto.studentcjn.common.strategies.WalkingStrategy;
import hanto.tournament.HantoMoveRecord;

/**
 * Base class for all hanto variants
 * 
 * @author chrisnavarro
 *
 */
public abstract class BaseHantoGame implements HantoGame {

	//boolean value checking if the first move has been made
	protected boolean firstMove;

	//boolean value checking if second move, so second piece can be adjacent to opposite color
	protected boolean secondMoveDone; 

	protected boolean gameOver;

	protected boolean playerSurrendered;

	//boolean value used to determine whose move it is
	protected boolean bluesTurn;

	//HantoBoard (stores games pieces by HantoCoords)
	protected static HantoBoard board = new HantoBoardImpl();;

	//Map used for checking for disconnects once a move is made
	protected static final Map<HantoCoordinate,Boolean> disconnectMap = new HashMap<HantoCoordinate,Boolean>();

	//Map used for grabbing the correct movement strategy (strategies obtained through pieceTypes)
	protected static final Map<HantoPieceType, MovementStrategy> movementStrategyMap = new HashMap<HantoPieceType, MovementStrategy>();

	//values which keep track of butterfly positions
	protected static List<HantoCoordinate> blueButterflyLocations = new LinkedList<HantoCoordinate>();
	protected static List<HantoCoordinate> redButterflyLocations = new LinkedList<HantoCoordinate>();

	//values that track how many of each type have been used
	//TODO: add for all pieces
	protected int blueButterflyNum;
	protected int blueCrabsNum;
	protected int blueHorsesNum;
	protected int blueCranesNum;
	protected int blueDovesNum;
	protected int blueSparrowsNum;

	protected int redButterflyNum;
	protected int redCrabsNum;
	protected int redHorsesNum;
	protected int redCranesNum;
	protected int redDovesNum;
	protected int redSparrowsNum;

	//max number of pieces allowed
	protected int maxButterfliesAllowed;
	protected int maxCrabsAllowed;
	protected int maxHorsesAllowed;
	protected int maxCranesAllowed;
	protected int maxDovesAllowed;
	protected int maxSparrowsAllowed;

	protected int movesBeforeButterflyMustBePlaced;

	//red and blue move counter
	protected int blueMovesNum;
	protected int redMovesNum;

	//max number of moves allowed in game;
	protected int maxMovesAllowed;

	/**
	 * BaseHantoGame constructor
	 * 
	 * @param movesFirst player which moves first
	 */
	protected BaseHantoGame(HantoPlayerColor movesFirst) {
		firstMove = true;
		secondMoveDone = false;

		playerSurrendered = false;

		gameOver = false;

		//checks if red is starting player (blue is default)		
		bluesTurn = (movesFirst == BLUE) ? true : false;

		//Clear board
		board.clearBoard();

		disconnectMap.clear();

		//Associate pieceTypes with default moving
		movementStrategyMap.clear();
		buildMovementStrategyMap();

		//Clear butterfly positions
		blueButterflyLocations.clear();
		redButterflyLocations.clear();

		//reset player hands
		blueButterflyNum = 0;
		blueCrabsNum = 0;
		blueHorsesNum = 0;
		blueCranesNum = 0;
		blueDovesNum = 0;
		blueSparrowsNum = 0;

		redButterflyNum = 0;
		redCrabsNum = 0;
		redHorsesNum = 0;
		redCranesNum = 0;
		redDovesNum = 0;
		redSparrowsNum = 0;
		redSparrowsNum = 0;

		maxButterfliesAllowed = 1;
		//TODO: determine defaults here, for now set to zero
		maxCrabsAllowed = 4;
		maxHorsesAllowed = 0;
		maxCranesAllowed = 0;
		maxDovesAllowed = 0;
		maxSparrowsAllowed = 5;

		movesBeforeButterflyMustBePlaced = 4;

		blueMovesNum = 0;
		redMovesNum = 0;
		maxMovesAllowed = Integer.MAX_VALUE; //set to max, because by default no limit
	}

	/*
	 * @see hanto.common.HantoGame#makeMove(hanto.common.HantoPieceType, hanto.common.HantoCoordinate, hanto.common.HantoCoordinate)
	 */
	@Override
	public MoveResult makeMove(HantoPieceType pieceType, HantoCoordinate from, HantoCoordinate to) throws HantoException {

		preHook(pieceType, from, to);

		preemptiveMoveValidations(pieceType, from, to);

		//surrender check
		if(playerSurrendered) {
			return gameOverCheck();
		}

		//The first move is being made
		if(firstMove) {
			makeFirstMove(pieceType, to);

		} else {
			//a new piece is being added
			if(from == null) {
				placePieceMove(pieceType, to);
			}
			//A pre-existing piece on the board is attempting to move
			else{
				movePieceMove(pieceType, from, to);
			}
		}

		//Exit condition checks
		butterflyPlacedByCertainMoveCheck();
		pieceCountCheck();

		return gameOverCheck();
	}

	private void makeFirstMove(HantoPieceType pieceType, HantoCoordinate to) {
		//Checks first move is at origin and is valid
		firstMove = false;

		//Blue turn
		if(bluesTurn){
			//If piece is blue butterfly flag is set and location is stored
			//otherwise pieces is sparrow and sparrow counter incremented
			if(pieceType == BUTTERFLY) {
				blueButterflyLocations.add(new HantoCoordinateImpl(to));
			} 

			blueMovesNum++; //increment players turn count
			incrementPieceCount(pieceType); //increment piece count used TODO: decrement?
			board.placePieceAtCoord(null, to, new HantoPieceImpl(BLUE, pieceType));
		} 
		//Red Turn
		else {
			//If piece is red butterfly flag is set and location is stored
			//otherwise pieces is sparrow and sparrow counter incremented
			if(pieceType == BUTTERFLY) {
				redButterflyLocations.add(new HantoCoordinateImpl(to));
			} 

			redMovesNum++;
			incrementPieceCount(pieceType);
			board.placePieceAtCoord(null, to, new HantoPieceImpl(RED, pieceType));
		}
	}

	private void placePieceMove(HantoPieceType pieceType, HantoCoordinate to) throws HantoException {
		if(hasAdjacentPiece(new HantoCoordinateImpl(to))) {
			if(bluesTurn) {
				board.placePieceAtCoord(null, new HantoCoordinateImpl(to), new HantoPieceImpl(BLUE, pieceType));

				if(pieceType == BUTTERFLY) {
					blueButterflyLocations.add(new HantoCoordinateImpl(to));
				}

				incrementPieceCount(pieceType);
				blueMovesNum++;
			} else {
				board.placePieceAtCoord(null, new HantoCoordinateImpl(to), new HantoPieceImpl(RED, pieceType));

				if(pieceType == BUTTERFLY) {
					redButterflyLocations.add(new HantoCoordinateImpl(to));
				} 

				incrementPieceCount(pieceType);
				redMovesNum++;
			}
		} else {
			throw new HantoException("No adjacent piece/Placing by aposing team");
		}

		secondMoveDone = true;

	}

	private void movePieceMove(HantoPieceType pieceType, HantoCoordinate from, HantoCoordinate to) throws HantoException {
		//Check if valid walk move, and no disconnects are created
		if(movementStrategyMap.get(pieceType).isValid(from, to, board) && noDisconnect(from, to, pieceType)) {
			HantoPlayerColor color;
			if(bluesTurn) {
				blueMovesNum++;
				color = BLUE;
			} else {
				color = RED;
				redMovesNum++;
			}
			
			//check if butterfly, then updates new location
			if(pieceType == BUTTERFLY) {
				if(bluesTurn) {
					blueButterflyLocations.add(to);
				} else {
					redButterflyLocations.add(to);
				}
			}	
			
			board.placePieceAtCoord(from, to, new HantoPieceImpl(color, pieceType));
			
		} else {
			if(bluesTurn) {
				throw new HantoException("Blue made invalid move: RED_WINS");
			} else {
				throw new HantoException("Redmade invalid move: BLUE_WINS");
			}
		} 
	}

	/**
	 * Checks that no piece has gone over its limits
	 * @throws HantoException
	 */
	private void pieceCountCheck() throws HantoException{
		final HantoPlayerColor color = (bluesTurn) ? BLUE : RED;
		final HantoPlayerColor altColor = (!bluesTurn) ? BLUE : RED;

		if(blueButterflyNum > maxButterfliesAllowed || redButterflyNum > maxButterfliesAllowed) {
			throw new HantoException(color + " placed too many butterflies: " + altColor + "_WINS");
		}

		if(blueSparrowsNum > maxSparrowsAllowed || redSparrowsNum > maxSparrowsAllowed) {
			throw new HantoException(color + " placed too many sparrows: " + altColor + "_WINS");
		}

		if(blueCrabsNum > maxCrabsAllowed || blueCrabsNum > maxCrabsAllowed) {
			throw new HantoException(color + " placed too many crabs: " + altColor + "_WINS");
		}
		/*
		if(blueCranesNum > maxCranesAllowed || blueCranesNum > maxCranesAllowed) {
			throw new HantoException(color + " placed too many cranes: " + altColor + "_WINS");
		}

		if(blueDovesNum > maxDovesAllowed || blueDovesNum > maxDovesAllowed) {
			throw new HantoException(color + " placed too many doves: " + altColor + "_WINS");
		}*/

		if(blueHorsesNum > maxHorsesAllowed || blueHorsesNum > maxHorsesAllowed) {
			throw new HantoException(color + " placed too many horses: " + altColor + "_WINS");
		}
	}

	/**
	 * Increments the piece type number
	 * 
	 * @param pieceType
	 */
	private void incrementPieceCount(HantoPieceType pieceType) {
		switch(pieceType) {
		case BUTTERFLY:
			if(bluesTurn) {
				blueButterflyNum++;
			} else {
				redButterflyNum++;
			}
			break;
		case SPARROW:
			if(bluesTurn) {
				blueSparrowsNum++;
			} else {
				redSparrowsNum++;
			}
			break;
		case CRAB:
			if(bluesTurn) {
				blueCrabsNum++;
			} else {
				redCrabsNum++;
			}
			break;
			/*case CRANE:
			if(bluesTurn) {
				blueCranesNum++;
			} else {
				redCranesNum++;
			}
			break;
		case DOVE:
			if(bluesTurn) {
				blueDovesNum++;
			} else {
				blueDovesNum++;
			}
			break;*/
		case HORSE:
			if(bluesTurn) {
				blueHorsesNum++;
			} else {
				redHorsesNum++;
			}
			break;
		default:
			break;
		}
	}

	/*
	 * @see hanto.common.HantoGame#getPieceAt(hanto.common.HantoCoordinate)
	 */
	@Override
	public HantoPiece getPieceAt(HantoCoordinate where) {
		return board.getPieceAtCoord(new HantoCoordinateImpl(where));
	}

	/*
	 * @see hanto.common.HantoGame#getPrintableBoard()
	 */
	@Override
	public String getPrintableBoard() {
		return board.printBoard();
	}

	/**
	 * Build movement map with default movements assigned to pieces, may be overridden
	 */
	protected void buildMovementStrategyMap() {
		movementStrategyMap.put(BUTTERFLY, new WalkingStrategy(1));
		movementStrategyMap.put(CRAB, new WalkingStrategy(1));
		movementStrategyMap.put(HORSE, new WalkingStrategy(1));
		movementStrategyMap.put(SPARROW, new WalkingStrategy(1));
	}

	/**
	 * Intended to handle all pre-checks for different hanto versions
	 * May be left blank or overidden
	 * 
	 * @param pieceType
	 * @param from
	 * @param to
	 * @throws HantoException
	 */
	protected void preHook(HantoPieceType pieceType, HantoCoordinate from, HantoCoordinate to) throws HantoException {}

	/**
	 * Intended to handle surrender case
	 * May be left blank or overidden
	 * 
	 * @return MoveResult of winner
	 * @throws HantoException
	 */
	public MoveResult surrenderCase() throws HantoException {

		//generate list of possible moves (what can be placed what can be moved)
		List<HantoMoveRecord> canPlace = generateCanPlaceList();

		List<HantoMoveRecord> canMove = generateCanMoveList();

		if(canPlace.isEmpty() && canMove.isEmpty()) {
			if(bluesTurn) {
				return RED_WINS;
			} else {
				return BLUE_WINS;
			}
		} else {
			throw new HantoPrematureResignationException();
		}
	}

	/**
	 * Generates all possible move options for a player
	 * 
	 * @return the list of can move pieces
	 */
	public List<HantoMoveRecord> generateCanMoveList() {
		List<HantoCoordinate> coords = board.generateCanMoveList(bluesTurn, firstMove, secondMoveDone);
		List<HantoMoveRecord> moveList = new LinkedList<HantoMoveRecord>();

		List<HantoMoveRecord> tempList = new LinkedList<HantoMoveRecord>();

		for(HantoCoordinate coord : coords) {
			tempList.addAll(movementStrategyMap.get(getPieceAt(coord).getType()).generateMoveList(coord, board, getPieceAt(coord).getType()));
		}
		
		for(HantoMoveRecord record : tempList) {
			if(noDisconnect(new HantoCoordinateImpl(record.getFrom().getX(), record.getFrom().getY()), 
							new HantoCoordinateImpl(record.getTo().getX(), record.getTo().getY()), record.getPiece())
					&& !board.containsPiece(record.getTo())) {
				moveList.add(record);
			}
		}

		return moveList;
	}

	/**
	 * Generates  a list of all possible pieceTypes that can be add and where they can be added
	 * 
	 * TODO: add support for all versions of hanto
	 * 
	 * @return a list HantoMoveRecord
	 */
	public List<HantoMoveRecord> generateCanPlaceList() {
		List<HantoCoordinate> canPlaceCoords = board.generateCanPlaceCoordinates(bluesTurn, firstMove, secondMoveDone);
		List<HantoPieceType> canPlacePieceTypes = generateCanPlacePieceTypes();

		List<HantoMoveRecord> canPlaceList = new LinkedList<HantoMoveRecord>();

		//Combines pieceTypes and coords to make master list
		for(HantoPieceType pieceType : canPlacePieceTypes) {
			for(HantoCoordinate coord : canPlaceCoords) {
				//this is where the filter is added for pieceTypes for different versions
				if(filter(pieceType)) {
					canPlaceList.add(new HantoMoveRecord(pieceType, null, coord));
				}
			}
		}

		return canPlaceList;
	}

	/**
	 * Returns true if pieceType is allowed in the specific game version
	 * 
	 * @param pieceType the pieceType being checked
	 * @return true if piece is allowed in gameType
	 */
	protected boolean filter(HantoPieceType pieceType) {
		return true;
	}

	/**
	 * Create a list of all pieceTypes that can be still used
	 * 
	 * @return
	 */
	private List<HantoPieceType> generateCanPlacePieceTypes() {
		List<HantoPieceType> types = new LinkedList<HantoPieceType>();

		if(bluesTurn) {
			if(maxButterfliesAllowed > blueButterflyNum) {
				types.add(BUTTERFLY);
			}
			if(maxCrabsAllowed > blueCrabsNum) {
				types.add(CRAB);
			}
			if(maxHorsesAllowed > blueHorsesNum) {
				types.add(HORSE);
			}
			if(maxSparrowsAllowed > blueSparrowsNum) {
				types.add(SPARROW);
			}
		} else {
			if(maxButterfliesAllowed > redButterflyNum) {
				types.add(BUTTERFLY);
			}
			if(maxCrabsAllowed > redCrabsNum) {
				types.add(CRAB);
			}
			if(maxHorsesAllowed > redHorsesNum) {
				types.add(HORSE);
			}
			if(maxSparrowsAllowed > redSparrowsNum) {
				types.add(SPARROW);
			}
		}	
		return types;
	}

	/**
	 * Initial checking to ensure the move can even be considered before more checking
	 * 
	 * @param pieceType the type being checked
	 * @param to the location the piece is trying to move to
	 * @param from the location the piece is trying to move from
	 * @throws HantoException
	 */
	protected void preemptiveMoveValidations(HantoPieceType pieceType, HantoCoordinate from, HantoCoordinate to) throws HantoException {

		final HantoPlayerColor color = (bluesTurn) ? BLUE : RED; 
		final HantoPlayerColor altColor = (!bluesTurn) ? BLUE : RED; 

		//Is player surrendering?
		if(from == null && to == null && pieceType == null) {
			playerSurrendered = true;
		} else {

			if(to != null) {
				final HantoCoordinateImpl destination = new HantoCoordinateImpl(to);

				//if first move is not placing at 0,0
				if(firstMove & (destination.getX() != 0 || destination.getY() != 0)) {
					throw new HantoException(color + " made invalid move, first move not at origin: " + altColor + "_WINS");
				}

				//Checks is pre-existing piece already exists on 'to' spot
				if(board.containsPiece(to)) {
					throw new HantoException(color + " made invalid move, piece already exists on spot: " + altColor + "_WINS");
				}
			}

			//If a piece is being moved
			if(from != null) {
				if(!board.containsPiece(from)) {
					throw new HantoException(color + " made invalid move, no piece at from: " + altColor + "_WINS");
				}
				//make sure player is moving there own piece
				if(color != board.getPieceAtCoord(from).getColor()) {
					throw new HantoException(color + " made invalid move, moved wrong color: " + altColor + "_WINS");
				}
				//make sure pieceTypes match, otherwise a piece can transform during a move
				if(pieceType != board.getPieceAtCoord(from).getType()) {
					throw new HantoException(color + " made invalid move, pieceType mismatch: " + altColor + "_WINS");
				}
				//require a location
				if(to == null) {
					throw new HantoException(color + " made invalid move, destination is null: " + altColor + "_WINS");

				}

				//Checks that piece types match when a piece is being moved
				if(board.getPieceAtCoord(from).getType() != pieceType) {
					throw new HantoException(color + " made invalid move, piece mismatch: " + altColor + "_WINS");
				}

				//Check that the butterfly is placed before any moves are accepted
				if(bluesTurn) {
					if(blueButterflyNum == 0) {
						throw new HantoException("Blue made invalid move, move before butterfly placed: RED_WINS");
					}
				} else {
					if(redButterflyNum == 0) {
						throw new HantoException("Red made invalid move, move before butterfly placed: BLUE_WINS");
					}
				}
			}
		}
	}

	/**
	 * Check to ensure each side has placed there butterfly by the fourth move
	 * 
	 * @throws HantoException if either player has failed to place butterfly by fourth move
	 */
	protected void butterflyPlacedByCertainMoveCheck() throws HantoException{

		if(blueMovesNum == movesBeforeButterflyMustBePlaced && blueButterflyNum == 0) {
			throw new HantoException("Blue failed to place butterfly: RED_WINS");
		}

		if(redMovesNum == movesBeforeButterflyMustBePlaced && redButterflyNum == 0) {
			throw new HantoException("Red failed to place butterfly: BLUE_WINS");
		}
	}

	/**
	 * Checks is a HantoCoordinate has an adjacent pieces associated with it and also that no colored
	 * adjacent pieces exist next to it
	 * 
	 * @param coord the coordinate to be checked for adjacent pieces
	 * @return true is adjacent piece found
	 */
	protected boolean hasAdjacentPiece(HantoCoordinate coord)
	{
		final int x = coord.getX();
		final int y = coord.getY();
		final HantoPlayerColor color = (bluesTurn) ? BLUE : RED;

		final HantoCoordinateImpl topLeft = new HantoCoordinateImpl(x - 1, y + 1);
		final HantoCoordinateImpl top = new HantoCoordinateImpl(x, y + 1);
		final HantoCoordinateImpl topRight = new HantoCoordinateImpl( x + 1, y);
		final HantoCoordinateImpl bottomRight = new HantoCoordinateImpl(x + 1, y - 1);
		final HantoCoordinateImpl bottom = new HantoCoordinateImpl(x, y - 1);
		final HantoCoordinateImpl bottomLeft = new HantoCoordinateImpl(x - 1, y);

		HantoPlayerColor topLeftColor = color;
		HantoPlayerColor topColor = color;
		HantoPlayerColor topRightColor = color;
		HantoPlayerColor bottomRightColor = color;
		HantoPlayerColor bottomColor = color;
		HantoPlayerColor bottomLeftColor = color;

		//if the second move has been executed then adjacent colors can be properly set
		if(secondMoveDone) {
			topLeftColor = board.getPieceColorAtCoord(topLeft);
			topColor = board.getPieceColorAtCoord(top);
			topRightColor = board.getPieceColorAtCoord(topRight);
			bottomRightColor = board.getPieceColorAtCoord(bottomRight);
			bottomColor = board.getPieceColorAtCoord(bottom);
			bottomLeftColor = board.getPieceColorAtCoord(bottomLeft);
		}

		return board.containsPiece(topLeft) && topLeftColor == color
				|| board.containsPiece(top) && topColor == color
				|| board.containsPiece(topRight) && topRightColor == color
				|| board.containsPiece(bottomRight) && bottomRightColor == color
				|| board.containsPiece(bottom) && bottomColor == color
				|| board.containsPiece(bottomLeft) && bottomLeftColor == color;
	}

	/**
	 * Checks if the move creates a disconnect in the board
	 * TODO move to GammaHanto
	 * 
	 * @param origin spot where piece is being moved from
	 * @param destination where piece is being moved to
	 * @param pieceType
	 * @return true is no disconnect
	 */
	protected boolean noDisconnect(HantoCoordinate origin, HantoCoordinate destination, HantoPieceType pieceType) {

		final HantoCoordinateImpl from = new HantoCoordinateImpl(origin);
		final HantoCoordinateImpl to = new HantoCoordinateImpl(destination);
		final HantoPiece piece;
		
		
		//places the initial coordinate in the map
		final int oldCount = countConnectedPieces(from); //connected pieces count before move

		disconnectMap.clear();//cleans up map for next check

		piece = (bluesTurn) ? new HantoPieceImpl(BLUE, pieceType) : new HantoPieceImpl(RED, pieceType);

		board.placePieceAtCoord(from, to, piece);
		
		final int newCount = countConnectedPieces(to); //connected pieces count after move

		disconnectMap.clear();//cleans up map for next method call
		
		board.placePieceAtCoord(to, from, piece);
		
		//checks if they are the same, if not a disconnect has been made
		return oldCount == newCount;	
	}

	/**
	 * Counts all HantoPieces connected to the piece (at coord) by using
	 * a depth first search
	 * 
	 * @param coordCounted the starting point for the board count
	 * @return the total number of connected pieces to the coord
	 */
	protected int countConnectedPieces(HantoCoordinate coordCounted) {

		int count = 1;

		final HantoCoordinateImpl coord = new HantoCoordinateImpl(coordCounted);

		final int x = coord.getX();
		final int y = coord.getY();

		disconnectMap.put(coord, true);

		final HantoCoordinate topLeft = new HantoCoordinateImpl(x - 1, y + 1);
		final HantoCoordinate top = new HantoCoordinateImpl(x, y + 1);
		final HantoCoordinate topRight = new HantoCoordinateImpl( x + 1, y);
		final HantoCoordinate bottomRight = new HantoCoordinateImpl(x + 1, y - 1);
		final HantoCoordinate bottom = new HantoCoordinateImpl(x, y - 1);
		final HantoCoordinate bottomLeft = new HantoCoordinateImpl(x - 1, y);

		//checks all around the piece at the specified coord, if a piece is found it is marked
		//and recursively calls the function again on the discovered piece (depth first search)
		if(board.containsPiece(topLeft) && disconnectMap.get(topLeft) == null) {
			count += countConnectedPieces(topLeft);
		}

		if(board.containsPiece(top) && disconnectMap.get(top) == null) {
			count += countConnectedPieces(top);
		}

		if(board.containsPiece(topRight) && disconnectMap.get(topRight) == null) {
			count += countConnectedPieces(topRight);
		}

		if(board.containsPiece(bottomRight) && disconnectMap.get(bottomRight) == null) {
			count += countConnectedPieces(bottomRight);
		}

		if(board.containsPiece(bottom) && disconnectMap.get(bottom) == null) {
			count += countConnectedPieces(bottom);
		}

		if(board.containsPiece(bottomLeft) && disconnectMap.get(bottomLeft) == null) {
			count += countConnectedPieces(bottomLeft);
		}

		return count; 
	}

	/**
	 * Checks if the specific colored butterfly has been surrounded
	 * 
	 * @param color the player whose butterfly is being checked for losing
	 * @return true is player's butterfly is surrounded
	 */
	public boolean hasLost(HantoPlayerColor color) {

		//false if butterfly hasn't been placed yet
		if((color == BLUE && blueButterflyNum == 0) || (color == RED && redButterflyNum == 0)) {
			return false;
		}

		int x, y;

		final List<HantoCoordinate> locations = (color == BLUE) ? blueButterflyLocations : redButterflyLocations;

		//iterates through all butterflies (if more than one are played)
		for(HantoCoordinate location : locations) {
			x = location.getX();
			y = location.getY();

			final HantoCoordinateImpl topLeft = new HantoCoordinateImpl(x - 1, y + 1);
			final HantoCoordinateImpl top = new HantoCoordinateImpl(x, y + 1);
			final HantoCoordinateImpl topRight = new HantoCoordinateImpl( x + 1, y);
			final HantoCoordinateImpl bottomRight = new HantoCoordinateImpl(x + 1, y - 1);
			final HantoCoordinateImpl bottom = new HantoCoordinateImpl(x, y - 1);
			final HantoCoordinateImpl bottomLeft = new HantoCoordinateImpl(x - 1, y);

			//checks all spots around butterfly
			return board.containsPiece(topLeft)
					&& board.containsPiece(top)
					&& board.containsPiece(topRight)
					&& board.containsPiece(bottomRight)
					&& board.containsPiece(bottom)
					&& board.containsPiece(bottomLeft);
		}

		return false; 
	}

	/**
	 * Checks if either player has lost from the previous move
	 * 
	 * @return the MoveResult caused by last move
	 * @throws HantoException if a move is made after gameOver = true
	 */
	public MoveResult gameOverCheck() throws HantoException {

		final int totalMoves = blueMovesNum + redMovesNum;

		if(!gameOver) {
			if(playerSurrendered) {
				gameOver = true;
				return surrenderCase();
			} else if(hasLost(BLUE) && hasLost(RED)) {
				gameOver = true;
				return DRAW;
			} else if(hasLost(BLUE)) {
				gameOver = true;
				return RED_WINS;
			} else if(hasLost(RED)) {
				gameOver = true;
				return BLUE_WINS;
			} else if(totalMoves == 2 * maxMovesAllowed) {
				gameOver = true;
				return DRAW;
			}
		} 

		//Game has already ended, but a player is still attempting a move
		else {
			final HantoPlayerColor color = (bluesTurn) ? BLUE : RED;
			final HantoPlayerColor altColor = (!bluesTurn) ? BLUE : RED;
			throw new HantoException(color + " made move after game : " + altColor + "_WINS");
		}

		//change player turn
		bluesTurn = (bluesTurn) ? false : true;

		return OK;
	}
}
