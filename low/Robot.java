package low;

public class Robot {
	
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
	
}