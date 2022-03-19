public class RobotExample extends RobotBase {
	
	public static void main (String[] args) {
		new RobotExample().startRobot();
	}
	
	@Override
	public void run () {
		for (int i = 1; i <= 10; i ++) {
			move();
			turnRight();
			move();
			turnLeft();
		}
		
		System.out.println("Done");
	}
	
}