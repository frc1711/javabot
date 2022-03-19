import javabot.InitialGameState;
import javabot.RobotBase;

public class RobotExample extends RobotBase {
	
	public static void main (String[] args) {
		RobotExample robot = new RobotExample();
		
		robot.start(
			6,
			new InitialGameState(
				4, 4,
				Direction.WEST,
				9));
	}
	
	@Override
	public void run () {
		tileGrid(false);
		spinzForDayz();
	}
	
	public void tileGrid (boolean startWithItem) {
		goToCoordinates(0, 0);
		
		while (true) {
			placeItemEveryOther(startWithItem);
			turnRight();
			if (!canMove()) break;
			move();
			turnRight();
			
			placeItemEveryOther(!startWithItem);
			turnLeft();
			if (!canMove()) break;
			move();
			turnLeft();
		}
	}
	
	public void spinzForDayz () {
		while (true) turnRight();
	}
	
	public void placeItemEveryOther (boolean startWithItem) {
		boolean placeItem = startWithItem;
		while (canMove()) {
			if (placeItem) putItem();
			placeItem = !placeItem;
			move();
		} if (placeItem) putItem();
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