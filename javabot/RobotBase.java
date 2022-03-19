package javabot;

import java.util.Timer;
import java.util.TimerTask;

import javabot.low.GameWindow;
import javabot.low.RobotState;
import javabot.low.RobotState.GameException;

public abstract class RobotBase {
	
	private static final int FRAME_MILLIS = 250;
	
	private RobotState robotState;
	private GameWindow gameWindow;
	
	public enum Direction {
		NORTH, SOUTH, EAST, WEST;
	}
	
	private boolean hasStarted = false;
	
	public final void turnLeft () {
		assertHasStarted(true);
		robotState.turnLeft();
	}
	
	public final void turnRight () {
		assertHasStarted(true);
		robotState.turnRight();
	}
	
	public final void move () {
		assertHasStarted(true);
		robotState.move();
	}
	
	public final boolean canMove () {
		assertHasStarted(true);
		return robotState.canMove();
	}
	
	public final Direction getDirection () {
		assertHasStarted(true);
		return robotState.getDirection();
	}
	
	public final void putItem () {
		assertHasStarted(true);
		robotState.putItem();
	}
	
	public final void pickItem () {
		assertHasStarted(true);
		robotState.pickItem();
	}
	
	public final boolean checkForItem () {
		assertHasStarted(true);
		return robotState.checkForItem();
	}
	
	public final void start (InitialGameState gameState) {
		assertHasStarted(false);
		hasStarted = true;
		
		robotState = new RobotState(gameState);
		gameWindow = new GameWindow(robotState);
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
			System.out.println("Robot \"" + this.getClass().getName() + "\" has finished");
		} catch (Exception e) {
			System.out.println("While the robot was running, an exception occurred:");
			e.printStackTrace();
		}
	}
	
	private void assertHasStarted (boolean started) {
		if (hasStarted && !started) throw new RuntimeException("Robot has already been started");
		if (!hasStarted && started) throw new RuntimeException("Robot has not yet been started");
	}
	
	public abstract void run () throws GameException;
	
}