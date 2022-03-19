package javabot.low;

import javabot.InitialGameState;
import javabot.RobotBase;
import javabot.RobotBase.Direction;

public class RobotState {
	
	final int fieldWidth;
	int x, y, rotation;
	
	boolean[][] items;
	
	private final Object gameFrameWatcher = new Object();
	
	public RobotState (InitialGameState state) {
		x = state.robotX;
		y = state.robotY;
		rotation = rotationFromDirection(state.robotDirection);
		fieldWidth = state.fieldWidth;
		items = new boolean[fieldWidth][fieldWidth];
	}
	
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
				return x < fieldWidth - 1;
			case 1:
				return y > 0;
			case 2:
				return x > 0;
			case 3:
				return y < fieldWidth - 1;
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
	
	private final int rotationFromDirection (Direction direction) {
		switch (direction) {
			case EAST:
				return 0;
			case NORTH:
				return 1;
			case WEST:
				return 2;
			case SOUTH:
				return 3;
			default:
				return -1;
		}
	}
	
	public final void putItem () {
		waitForGameStateUpdate();
		if (items[x][y]) throw new GameException("Cannot add a second item to a cell");
		items[x][y] = true;
	}
	
	public final void pickItem () {
		waitForGameStateUpdate();
		if (!items[x][y]) throw new GameException("Cannot pick up a nonexistent item");
		items[x][y] = false;
	}
	
	public final boolean checkForItem () {
		return items[x][y];
	}
	
	private void checkWithinBounds () throws GameException {
		if (x < 0 || x >= fieldWidth) throw new GameException("Out of bounds");
		if (y < 0 || y >= fieldWidth) throw new GameException("Out of bounds");
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