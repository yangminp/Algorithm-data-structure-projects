package swen221.lab2;

import swen221.lab2.model.*;
import swen221.lab2.rules.*;
import swen221.lab2.view.BoardFrame;

public class GameOfLife {
	/**
	 * The standard rule set for Conway's "Game of Life".
	 */
	public static final Rule[] ConwaysOriginalRules = {
			new ConwaysUnderpopulationRule(),
			new ConwaysReproductionRule(),
			new ConwaysOverpopulationRule()
	};
	
	/**
	 * The entry point for the GameOfLife application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Board board = new Board(50,50);
		Simulation sim = new Simulation(board,ConwaysOriginalRules);
		new BoardFrame(sim);
	}
	/*
	 * Create a board from a given starting state
	 * */
}
