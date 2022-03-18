package robotstate;

import low.JSON;

public class GameState {
	
	private final Robot robot;
	
	public GameState (Robot	robot) {
		this.robot = robot;
	}
	
	public void update () {
		synchronized (robot) {
			robot.notify();
		}
	}
	
	public String getDataString () {
		JSON obj = new JSON();
		obj.put("x", robot.x);
		obj.put("y", robot.y);
		obj.put("rotation", robot.rotation);
		return obj.getJSONString();
	}
	
}