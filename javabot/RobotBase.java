package javabot;

import java.util.Timer;
import java.util.TimerTask;

import javabot.low.GameWindow;
import javabot.low.RobotState;
import javabot.low.RobotState.GameException;

public abstract class RobotBase {
	
	private static final int FRAME_MILLIS = 250;
	
	private final RobotState robotState = new RobotState();
	private final GameWindow gameWindow = new GameWindow(robotState);
	
	public enum Direction {
		NORTH, SOUTH, EAST, WEST;
	}
	
	public final void turnLeft () {
		robotState.turnLeft();
	}
	
	public final void turnRight () {
		robotState.turnRight();
	}
	
	public final void move () {
		robotState.move();
	}
	
	public final boolean canMove () {
		return robotState.canMove();
	}
	
	public final Direction getDirection () {
		return robotState.getDirection();
	}
	
	public final void startRobot () {
		// Start the game window
		gameWindow.start();
		
		// Create the scheduled update loop
		TimerTask timerTask = new TimerTask(){
			@Override
			public void run () {
				robotState.update();
				gameWindow.displayNextFrame();
			}
		};
		new Timer().scheduleAtFixedRate(timerTask, FRAME_MILLIS, FRAME_MILLIS);
		
		// Begin running the robot program
		try {
			gameWindow.displayNextFrame();
			run();
		} catch (Exception e) {
			System.out.println("While running the game, an exception occurred:");
			System.out.println(e);
		}
		System.out.println("Robot \"" + this.getClass().getName() + "\" has finished");
	}
	
	public abstract void run () throws GameException;
	
}