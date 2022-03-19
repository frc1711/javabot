package javabot.low;

import javabot.RobotBase;
import javabot.RobotBase.Direction;

public class RobotState {
	
	private static final int
		FIELD_WIDTH = 10,
		FIELD_HEIGHT = 10;
	
	int
		x = 5, y = 5,
		rotation = 2; // 0 = facing east, 1 = north, 2 = west, 3 = south
	
	private final Object gameFrameWatcher = new Object();
	
	public final void update () {
		synchronized (gameFrameWatcher) {
			gameFrameWatcher.notify();
		}
	}
	
	public final void turnLeft () {
		waitForGameStateUpdate();
		rotation ++;
		if (rotation == 4) rotation = 0;
	}
	
	public final void turnRight () {
		waitForGameStateUpdate();
		rotation --;
		if (rotation == -1) rotation = 3;
	}
	
	public final void move () throws GameException {
		waitForGameStateUpdate();
		
		final int x1 = x, y1 = y;
		switch (rotation) {
			case 0:
				x ++;
				break;
			case 1:
				y --;
				break;
			case 2:
				x --;
				break;
			case 3:
				y ++;
				break;
		}
		
		try {
			checkWithinBounds();
		} catch (GameException e) {
			// Return to original position
			x = x1;
			y = y1;
			
			// Throw exception
			throw e;
		}
	}
	
	public final boolean canMove () {
		switch (rotation) {
			case 0:
				return x < FIELD_WIDTH - 1;
			case 1:
				return y > 0;
			case 2:
				return x > 0;
			case 3:
				return y < FIELD_HEIGHT - 1;
			default:
				return false;
		}
	}
	
	public final RobotBase.Direction getDirection () {
		switch (rotation) {
			case 0:
				return Direction.EAST;
			case 1:
				return Direction.NORTH;
			case 2:
				return Direction.WEST;
			case 3:
				return Direction.SOUTH;
			default:
				return null;
		}
	}
	
	private void checkWithinBounds () throws GameException {
		if (x < 0 || x >= FIELD_WIDTH) throw new GameException("Out of bounds");
		if (y < 0 || y >= FIELD_HEIGHT) throw new GameException("Out of bounds");
	}
	
	private void waitForGameStateUpdate () {
		synchronized (gameFrameWatcher) {
			try { gameFrameWatcher.wait(); }
			catch (InterruptedException e) { }
		}
	}
	
	public class GameException extends RuntimeException {
		public GameException (String message) {
			super(message);
		}
	}
	
}