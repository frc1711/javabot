package javabot.low;

import javabot.InitialGameState;
import javabot.RobotBase;
import javabot.InitialGameState.Position;
import javabot.RobotBase.Direction;

public class RobotState {
	
	final int fieldWidth;
	int x, y, rotation;
	
	boolean[][] items;
	boolean[][] walls;
	
	private final Object gameFrameWatcher = new Object();
	
	public RobotState (InitialGameState state) {
		x = state.robotX;
		y = state.robotY;
		rotation = rotationFromDirection(state.robotDirection);
		fieldWidth = state.fieldWidth;
		
		items = new boolean[fieldWidth][fieldWidth];
		walls = new boolean[fieldWidth][fieldWidth];
		for (Position item : state.items) items[item.x][item.y] = true;
		for (Position wall : state.walls) walls[wall.x][wall.y] = true;
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
	
	public final void move () {
		waitForGameStateUpdate();
		
		int x1 = x, y1 = y;
		moveNoWaitOrCheck();
		
		// Ensures the robot is still within the game bounds
		try {
			checkPosition();
		} catch (GameException e) {
			// Return to original position
			x = x1;
			y = y1;
			
			// Throw exception
			throw e;
		}
	}
	
	private final void moveNoWaitOrCheck () {
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
	}
	
	public final boolean canMove () {
		boolean exception = false;
		
		int x1 = x, y1 = y;
		moveNoWaitOrCheck();
		try {
			checkPosition();
		} catch (GameException e) {
			exception = true;
		}
		
		x = x1;
		y = y1;
		
		return !exception;
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
	
	private void checkPosition () throws GameException {
		if (x < 0 || x >= fieldWidth) throw new GameException("Attempted to move out of bounds");
		if (y < 0 || y >= fieldWidth) throw new GameException("Attempted to move out of bounds");
		if (walls[x][y] == true) throw new GameException("Hit a wall");
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