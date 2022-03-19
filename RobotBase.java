import low.GameWindow;
import low.RobotState;
import low.RobotState.GameException;

public abstract class RobotBase {
	
	private final RobotState robotState = new RobotState();
	
	public final void turnLeft () {
		robotState.turnLeft();
	}
	
	public final void turnRight () {
		robotState.turnRight();
	}
	
	public final void move () {
		robotState.move();
	}
	
	public final void startRobot () {
		GameWindow gameWindow = new GameWindow(robotState);
		gameWindow.start();
		
		// TODO: Make a run loop here
		new Thread(this::runWrapper).start();
		for (int i = 0; i < 100; i++) {
			System.out.println("Advancing frame");
			gameWindow.advanceFrame();
		}
	}
	
	private final void runWrapper () {
		try {
			run();
		} catch (GameException e) {
			System.out.println(e);
		}
	}
	
	public abstract void run () throws GameException;
	
}