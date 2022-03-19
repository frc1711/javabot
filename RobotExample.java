import javabot.InitialGameState;
import javabot.RobotBase;

public class RobotExample extends RobotBase {
	
	public static void main (String[] args) {
		RobotExample robot = new RobotExample();
		
		robot.start(new InitialGameState(
			4, 4,
			Direction.WEST,
			9));
	}
	
	@Override
	public void run () {
		goToCoordinates(3, 3);
	}
	
	public void goToCoordinates (int x, int y) {
		// Go to (0, 0)
		turnToDirection(Direction.NORTH);
		runIntoWall();
		turnLeft();
		runIntoWall();
		
		turnLeft();
		for (int i = 0; i < y; i ++) move();
		
		turnLeft();
		for (int i = 0; i < x; i ++) move();
	}
	
	public void runIntoWall () {
		while (canMove()) move();
	}
	
	public void turnToDirection (Direction direction) {
		while (getDirection() != direction) turnRight();
	}
	
}