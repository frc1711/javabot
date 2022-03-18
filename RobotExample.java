public class RobotExample extends RobotBase {
	
	public static void main (String[] args) {
		new RobotExample().startRobot();
	}
	
	@Override
	public void run() {
		System.out.println("Turning left");
		turnLeft();
		System.out.println("Turning right");
		turnRight();
		
		for (int i = 1; i <= 10; i ++) {
			System.out.println("turning #"+i);
			turnLeft();
		}
		
		System.out.println("Done");
	}
	
}