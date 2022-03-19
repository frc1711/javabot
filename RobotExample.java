import javabot.RobotBase;

public class RobotExample extends RobotBase {
	
	public static void main (String[] args) {
		new RobotExample().startRobot();
	}
	
	@Override
	public void run () {
		turnToDirection(Direction.SOUTH);
	}
	
	public void runIntoWall () {
		while (canMove()) move();
	}
	
	public void turnToDirection (Direction direction) {
		while (getDirection() != direction) turnRight();
	}
	
}