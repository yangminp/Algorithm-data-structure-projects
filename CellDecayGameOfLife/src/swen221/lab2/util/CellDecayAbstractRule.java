package swen221.lab2.util;

import swen221.lab2.model.*;

/**
 * Implements a standard class for implementing the standard rules for CellDecay's
 * game of life. Specifically, those based around the number of neighbours which
 * are alive. In the standard game, cell's are either ON or OFF which, in our
 * customizable version of CellDecay, corresponds to state from 0 to 9.
 */
public abstract class CellDecayAbstractRule implements Rule{
	public final static int STATE_DECRESE = -1;
	public final static int STATE_INCRESE = 1;
    public static BoardView boardView;
    
	public final static int DEAD = 9;

	
	@Override
	public int apply(int x, int y, BoardView board) {
		int count = 0;
		
		this.boardView = board;
		// top row
		count += getNumAlive(x-1,y-1,board);
		count += getNumAlive(x,y-1,board);
		count += getNumAlive(x+1,y-1,board);
		// middle row
		count += getNumAlive(x-1,y,board);
		count += getNumAlive(x+1,y,board);
		// bottom row
		count += getNumAlive(x-1,y+1,board);
		count += getNumAlive(x,y+1,board);
		count += getNumAlive(x+1,y+1,board);
		//
		return apply(x,y,count);
	}
	
	/**
	 * Apply the actual rule having already calculated the number of alive
	 * neighbours
	 * 
	 * @param x
	 *            Horizontal position of cell on the board
	 * @param y
	 *            Vertical position of cell on the board
	 * @param neighbours
	 *            Number of alive neighbours on the board.
	 * @return
	 */
	public abstract int apply(int x, int y, int neighbours);
	
	/**
	 * Check the state of an adjancent cell, taking into account the edges of
	 * the board.
	 * 
	 * @param x
	 * @param y
	 * @param board
	 * @return
	 */
	private int getNumAlive(int x, int y, BoardView board) {
		if (x < 0 || x >= board.getWidth()) {
			return 0;
		} else if (y < 0 || y >= board.getWidth()) {
			return 0;
		} else if (board.getCellState(x, y) == DEAD) {
			return 0;
		} else {
			return 1;
		}
	}
}
