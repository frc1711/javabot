public class Main {
	
	public static void main (String[] args) {
		ServerHandler server = new ServerHandler(new RobotDataHandler());
		server.start();
	}
	
}