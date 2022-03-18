import low.Robot;

public abstract class RobotBase {
	
	private final Robot robot;
	
	public RobotBase () {
		robot = new Robot();
	}
	
	public final void turnLeft () {
		robot.turnLeft();
	}
	
	public final void turnRight () {
		robot.turnRight();
	}
	
	public final void startRobot () {
		robot.startRobot(this::run);
	}
	
	public abstract void run ();
	
}