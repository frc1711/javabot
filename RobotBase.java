import java.util.Timer;
import java.util.TimerTask;

import low.GameWindow;
import low.RobotState;
import low.RobotState.GameException;

public abstract class RobotBase {
	
	private static final int FRAME_MILLIS = 250;
	
	private final RobotState robotState = new RobotState();
	private final GameWindow gameWindow = new GameWindow(robotState);
	
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
		// Start the game window
		gameWindow.start();
		
		// Create the scheduled update loop
		TimerTask timerTask = new TimerTask(){
			@Override
			public void run () {
				robotState.update();
				gameWindow.displayNextFrame();
			}
		};
		new Timer().scheduleAtFixedRate(timerTask, 0, FRAME_MILLIS);
		
		// Begin running the robot program
		try {
			run();
		} catch (Exception e) {
			System.out.println("While running the game, an exception occurred:");
			System.out.println(e);
		}
		System.out.println("Robot \"" + this.getClass().getName() + "\" has finished");
	}
	
	public abstract void run () throws GameException;
	
}