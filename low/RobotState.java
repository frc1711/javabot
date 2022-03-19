package low;

public class RobotState {
	
	private static final int
		FIELD_WIDTH = 10,
		FIELD_HEIGHT = 10;
	
	int
		x = 0, y = 0,
		rotation = 0; // 0 = facing right, 1 = up, 2 = left, 3 = down
	
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