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

import java.util.*;

import hanto.common.HantoCoordinate;
import hanto.studentcjn.common.HantoBoard;
import hanto.studentcjn.common.HantoCoordinateImpl;

/**
 * Finds a path between two HantoPoints using A*
 * 
 * @author chrisnavarro
 *
 */
public class AStarSearch {
	public static final int COST = 1;

	/**
	 * Node class that contain HantoCoordinate and Cost of travel to that coord
	 * @author chrisnavarro
	 *
	 */
	static class Node{  
		int heuristicCost = 0; //Heuristic cost
		int finalCost = 0; //G+H
		HantoCoordinate coord;
		Node parent; 

		/**
		 * Node constructor
		 * 
		 * @param coord
		 * @param hValue manhattan value from destination target
		 */
		Node(HantoCoordinate coord, int hValue){
			this.coord = coord;
			heuristicCost = hValue;
		}
	}

	//Blocked Nodes are just null Node values in grid
	private final HantoBoard board;

	static PriorityQueue<Node> open = new PriorityQueue<Node>(new Comparator<Object>() {
		@Override
		public int compare(Object o1, Object o2) {
			final Node n1 = (Node) o1;
			final Node n2 = (Node) o2;

			if(n1.finalCost > n2.finalCost) {
				return 1;
			}
			if(n1.finalCost < n2.finalCost) {
				return -1;
			}
			return 0;
			
		}
	});

	private final HantoCoordinate start;
	private final HantoCoordinate end;

	private static final HashMap<HantoCoordinate, Boolean> closed = new HashMap<HantoCoordinate, Boolean>();

	//Blocked Nodes are just null Node values in grid
	private static final Map<HantoCoordinate, Node> nodeMap = new HashMap<HantoCoordinate, Node>();


	/**
	 * Searches through the board for a path between two coordinates (using A*)
	 * 
	 * @param board
	 * @param start
	 * @param end
	 */
	public AStarSearch(HantoBoard board, HantoCoordinate start, HantoCoordinate end) {
		this.board = board;
		this.start = start;
		this.end = end;

		//initially resets everything
		open.clear();
		closed.clear();
		nodeMap.clear();

		nodeMap.put(start, new Node(start, 0));
		nodeMap.put(end, new Node(end, board.distanceBetween(start, end)));
		closed.put(end, false);
		
		this.AStar();
	}

	/**
	 * Updates the final costs for movement for the node being examined
	 * next to the current node
	 * 
	 * @param current
	 * @param t
	 * @param cost
	 */
	private void checkAndUpdateCost(Node current, Node t, int cost){
		
		//makes sure pieces is always adjacent to another and not traversing gaps
		if(!board.hasAdjacent(t.coord)) {
			closed.put(t.coord, true);
		}
		
		if(board.containsPiece(t.coord) || closed.get(t.coord)) {
			return;
		}

		final int t_final_cost = t.heuristicCost + cost;

		boolean inOpen = open.contains(t);
		if(!inOpen || t_final_cost < t.finalCost){
			t.finalCost = t_final_cost;
			t.parent = current;
			if(!inOpen) {
				open.add(t);
			}
		}
	}

	/**
	 * Main A* algorithm
	 */
	private void AStar(){ 

		//add the start location to open list.
		open.add(nodeMap.get(start));

		Node current, newNode;

		HantoCoordinate top;
		HantoCoordinate topRight;
		HantoCoordinate bottomRight;
		HantoCoordinate bottom;
		HantoCoordinate bottomLeft;
		HantoCoordinate topLeft;

		while(true){ 
			current = open.poll();
			
			if(current == null) {
				break;
			}

			top = new HantoCoordinateImpl(current.coord.getX(), current.coord.getY() + 1);
			topRight = new HantoCoordinateImpl(current.coord.getX() + 1, current.coord.getY());
			bottomRight = new HantoCoordinateImpl(current.coord.getX() + 1, current.coord.getY() - 1);
			bottom = new HantoCoordinateImpl(current.coord.getX(), current.coord.getY() - 1);
			bottomLeft = new HantoCoordinateImpl(current.coord.getX() - 1, current.coord.getY());
			topLeft = new HantoCoordinateImpl(current.coord.getX() - 1, current.coord.getY() + 1);

			closed.put(current.coord, true); 

			if(current.equals(nodeMap.get(end))){
				return; 
			} 

			Node t;

			//initially places the node in the map
			if(nodeMap.get(top) == null) {
				newNode = new Node(top, board.distanceBetween(top, end));
				nodeMap.put(top, newNode);
				
				//opens node initially for travel, until proven non-traversible
				closed.put(top, false);
			}
			t = nodeMap.get(top);
			checkAndUpdateCost(current, t, current.finalCost + COST);

			if(nodeMap.get(topRight) == null) {
				newNode = new Node(topRight, board.distanceBetween(topRight, end));
				nodeMap.put(topRight, newNode);
				closed.put(topRight, false);
			}
			t = nodeMap.get(topRight);
			checkAndUpdateCost(current, t, current.finalCost + COST);

			if(nodeMap.get(bottomRight) == null) {
				newNode = new Node(bottomRight, board.distanceBetween(bottomRight, end));
				nodeMap.put(bottomRight, newNode);
				closed.put(bottomRight, false);
			}
			t = nodeMap.get(bottomRight);
			checkAndUpdateCost(current, t, current.finalCost + COST);

			if(nodeMap.get(bottom) == null) {
				newNode = new Node(bottom, board.distanceBetween(bottom, end));
				nodeMap.put(bottom, newNode);
				closed.put(bottom, false);
			}
			t = nodeMap.get(bottom);
			checkAndUpdateCost(current, t, current.finalCost + COST);

			if(nodeMap.get(bottomLeft) == null) {
				newNode = new Node(bottomLeft, board.distanceBetween(bottomLeft, end));
				nodeMap.put(bottomLeft, newNode);
				closed.put(bottomLeft, false);
			}
			t = nodeMap.get(bottomLeft);
			checkAndUpdateCost(current, t, current.finalCost + COST);

			if(nodeMap.get(topLeft) == null) {
				newNode = new Node(topLeft, board.distanceBetween(topLeft, end));
				nodeMap.put(topLeft, newNode);
				closed.put(topLeft, false);
			}
			t = nodeMap.get(topLeft);
			checkAndUpdateCost(current, t, current.finalCost + COST);

		}
	} 
	
	/**
	 * Returns list of coordinates that make up the path between two spots
	 * 
	 * @return list of coordinates
	 */
	public List<HantoCoordinate> getMovesList() {

		final List<HantoCoordinate> moves = new LinkedList<HantoCoordinate>();
		
		if(closed.get(end)){
			Node current = nodeMap.get(end);
			moves.add(current.coord);
			while(current.parent != null){
				moves.add(current.parent.coord);
				current = current.parent;
			} 
		}
		
		return moves;
	}
}