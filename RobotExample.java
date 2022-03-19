import javabot.InitialGameState;
import javabot.RobotBase;

public class RobotExample extends RobotBase {
	public static void main (String[] args) {
		RobotExample robot = new RobotExample();
		robot.start(6, new InitialGameState("initial-game-state.txt"));
	}
	
	@Override
	public void run () {
		while (!checkForItem()) {
			putItem();
			move();
			move();
			turnRight();
		}
		
		move();
		turnRight();
		move();
		putItem();
	}
}