import java.util.stream.Stream;

public class RobotDataHandler {
	
	private double x = 100, y = 300;
	
	public String processData (Stream<String> input) {
		x += 10;
		y -= 4;
		return x + ":" + y;
	}
	
}