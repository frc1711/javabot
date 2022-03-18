import low.RobotDataHandler;
import low.ServerHandler;

import robotstate.GameState;
import robotstate.Robot;

public class Main {
	
	public static void main (String[] args) {
		
		// Instantiate the robot, serverhandler, etc.
		Robot robot = new Robot() {
			@Override
			public void run () {
				System.out.println("ROBOT RUNNING");
				for (int i = 0; i < 10; i ++) {
					turnLeft();
					System.out.println("turned left");
				}
			}
		};
		ServerHandler server = new ServerHandler(new RobotDataHandler(new GameState(robot)));
		
		// Start running the robot on a new thread
		Thread robotThread = new Thread(robot::run);
		robotThread.setDaemon(true);
		robotThread.start();
		
		// Start the server
		server.start();
	}
	
}