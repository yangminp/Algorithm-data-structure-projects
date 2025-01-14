package model;


import gameComponent.Piece;
import gameComponent.Player;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;


@SuppressWarnings("serial")
public class LeftCreation extends Observable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int rows = 6;
	private int cols = 4;

	private Player greenPlayer;
	private List<Piece> pieces;
	private List<Point> piecesPoint; //the coordinator of Point is the piece left-top location
	public List<Piece> getPieces(){
		return pieces;
	}

	public Player getGreenPlayer(){
		return greenPlayer;
	}
	public void setPieces(List<Piece> temp){
		pieces=temp;
		setChanged();
		notifyObservers();
	}
	public List<Point> getPiecesPoint(){
		return piecesPoint;
	}

	public LeftCreation(Player player) {
		pieces=new ArrayList<Piece>();
		piecesPoint=new ArrayList<Point>();
		greenPlayer=player;
		
		//initialise all pieces
		for(int row =0;row<rows;row++){
			for(int col = 0;col<cols;col++){
				greenPlayer.addDiffPiece(Piece.Type.GreenPiece);

			}
		}
		setChanged();
		notifyObservers();

	}

}