package robotwar.core;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * An abstract class representing a Robot in the battle. Subclasses of this can
 * implement different strategies for moving and fighting within the arena.
 * 
 * @author David J. Pearce
 * 
 */
public abstract class Robot {
	public String name;
//	public int xPosition;	
//	public int yPosition;
	private int  xPosition;
	private int yPosition;
	public int strength;
	public boolean isDead;
	
	/**setter and getter of coordinates**/
	public int getxPosition() {
		return xPosition;
	}

	public void setxPosition(int xPosition) {
		
		this.xPosition = xPosition;
	}

	public int getyPosition() {
		return yPosition;
	}

	public void setyPosition(int yPosition) {
		this.yPosition = yPosition;
	}

	
	
	/**
	 * Construct a robot at a given x and y position with a given strength.
	 * 
	 * @param xPosition starting x position of robot
	 * @param yPosition starting y position of robot
	 * @param strength strength of robot
	 */
	public Robot(String name, int xPosition, int yPosition, int strength) {
		this.name = name;
		this.xPosition = xPosition;
		this.yPosition = yPosition;
		this.strength = strength;
		this.isDead = false;
	}
	
	/**
	 * This method is called to allow a robot to do something.
	 * 
	 * @param battle
	 */
	public abstract void takeTurn(Battle battle);	
	/**
	 * This method is called when a robot is shot by another robot.
	 */
	public void isShot(int strength) {
		this.strength = this.strength - 1;
		if(this.strength<1){
			this.isDead = true;
		}
		// should check isDead here?
	}
	
	/**
	 * This helper method determines what robots are in sight of this robot.
	 * Robots which are in sight can be attacked.
	 * 
	 * @param distance
	 *            the distance that this robot can see.
	 * @return list of robots in sight
	 */
	protected List<Robot> findRobotsInSight(Battle battle,
			int distance) {
		
		List<Robot> robots = battle.getRobots();
		List<Robot> visibleRobots = new ArrayList<Robot>();
		for(Robot r : robots) {
			if(r != this && !r.isDead) {
				int dx = xPosition - r.xPosition;
				int dy = yPosition - r.yPosition;
				// Calculate distance from me to robot r using pythagorus
				// theorem.
				double distanceToR = Math.sqrt(dx*dx + dy*dy);
				if(((double)distance) > distanceToR) {
					// this robot is in range of sight
					visibleRobots.add(r);
				}
			}
		}
		
		return visibleRobots;
	}
	protected void common(Battle battle, int strength){
		List<Robot> robotsInSight = findRobotsInSight(battle, 10);

		if(!robotsInSight.isEmpty()) {
			// shoot a robot then!
			Robot target = robotsInSight.get(0);
			battle.getActions().add(new Shoot(this,target,strength));

				//target.strength = target.strength - 1;
				if(this.strength < 1) {
					isDead = true;
				}


		}
	}
	public abstract Image getRobotImage();



}
