package robotstate;

public abstract class Robot {
	
	double x = 0, y = 0;
	int rotation = 0; // 0 = facing right, 1 = up, 2 = left, 3 = down
	
	protected final void turnLeft () {
		waitForGameStateUpdate();
		rotation ++;
		if (rotation == 4) rotation = 0;
	}
	
	protected final void turnRight () {
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
	
	public abstract void run ();
	
}