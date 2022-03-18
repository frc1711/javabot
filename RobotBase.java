import java.io.PrintStream;

import low.RobotState;
import low.RobotState.GameException;

public abstract class RobotBase {
	
	public final PrintStream output;
	private final RobotState robot;
	
	public RobotBase () {
		output = null; // TODO: Make the PrintStream usable
		robot = new RobotState();
	}
	
	public final void turnLeft () {
		robot.turnLeft();
	}
	
	public final void turnRight () {
		robot.turnRight();
	}
	
	public final void move () {
		robot.move();
	}
	
	public final void startRobot () {
		robot.startRobot(this::runWrapper);
	}
	
	private final void runWrapper () {
		try {
			run();
		} catch (GameException e) {
			output.println(e);
		}
	}
	
	public abstract void run () throws GameException;
	
}