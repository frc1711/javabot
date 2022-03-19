package javabot;

import javabot.RobotBase.Direction;

public class InitialGameState {
	
	public final int robotX, robotY, fieldWidth;
	public final Direction robotDirection;
	
	public InitialGameState (int robotX, int robotY, Direction robotDirection, int fieldWidth) {
		this.robotX = robotX;
		this.robotY = robotY;
		this.fieldWidth = fieldWidth;
		this.robotDirection = robotDirection;
	}
	
}