package low;

public class RobotState {
	
	private static final int
		FIELD_WIDTH = 10,
		FIELD_HEIGHT = 10;
	
	private double x = 0, y = 0;
	private int rotation = 0; // 0 = facing right, 1 = up, 2 = left, 3 = down
	
	public final void update () {
		synchronized (this) {
			this.notify();
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
		switch (rotation) {
			case 0:
				x ++;
				break;
			case 1:
				y ++;
				break;
			case 2:
				x --;
				break;
			case 3:
				y --;
				break;
		}
		
		checkWithinBounds();
	}
	
	private void checkWithinBounds () throws GameException {
		if (x < 0 || x >= FIELD_WIDTH) throw new GameException("Out of bounds");
		if (y < 0 || y >= FIELD_HEIGHT) throw new GameException("Out of bounds");
	}
	
	private void waitForGameStateUpdate () {
		synchronized (this) {
			try { this.wait(); }
			catch (InterruptedException e) { }
		}
	}
	
	public final void startRobot (Runnable robotRunFunction) {
		// Makes the server and prepares the robot thread
		ServerHandler server = new ServerHandler(new RobotDataHandler(this));
		Thread robotThread = new Thread(robotRunFunction);
		robotThread.setDaemon(true);
		
		// Starts the robot thread and starts the server
		robotThread.start();
		server.start();
	}
	
	public final String getDataString () {
		JSON obj = new JSON();
		obj.put("x", x);
		obj.put("y", y);
		obj.put("rotation", rotation);
		return obj.getJSONString();
	}
	
	public class GameException extends RuntimeException {
		public GameException (String message) {
			super(message);
		}
	}
	
}