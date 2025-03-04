package swen221.lab2.rules;


import swen221.lab2.model.*;
import swen221.lab2.util.ConwayAbstractRule;

/**
 * This should implement Conway's rule for reproduction:
 * 
 * "Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction"
 * 
 * @author David J. Pearce
 *
 */
public class ConwaysReproductionRule extends ConwayAbstractRule {
	
	@Override
	public int apply(int x, int y, int neighbours) {
		if(neighbours == 3){
			return ConwayAbstractRule.ALIVE;	
		}
		return Rule.NOT_APPLICABLE;		
	}
}
