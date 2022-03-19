# Javabot
## Robot Setup
In order to make a javabot, the `RobotBase` class must be extended, with its `run` method implemented, which
will be run when the robot starts. To start the robot, in the program's `main` method, a robot instance
should be made and its `start` method should be called. The `fps` and `InitialGameState` are passed into
this method (see below section on `InitialGameState` file formatting).

The following methods can be used while the robot is running:
- `turnLeft`
- `turnRight`
- `move`
- `canMove`
- `getDirection`
- `putItem`
- `pickItem`
- `checkForItem`

An example for the robot code may be:
```
import javabot.InitialGameState;
import javabot.RobotBase;

public class RobotExample extends RobotBase {
	public static void main (String[] args) {
		RobotExample robot = new RobotExample();
		robot.start(6, new InitialGameState("initial-game-state.txt"));
	}
	
	@Override
	public void run () {
		turnRight();
		while (canMove()) {
			if (!checkForItem()) putItem();
			move();
		}
	}
}
```

## Initial Game State File
`InitialGameState` is read from a file. This file should have the following format:
```
ROBOT-X: [Integer]
ROBOT-Y: [Integer]
ROBOT-DIRECTION: [NORTH | SOUTH | EAST | WEST]
FIELD-WIDTH: [Integer]
ITEMS: [Location]; [Location]; etc.
WALLS: [Location]; [Location]; etc.
```
Both `ITEMS` and `WALLS` attributes can be ommitted if there aren't supposed to be any items or walls,
and all attributes can be listed in any order. In the template above, `[Location]` should be replace with
an ordered pair of coordinates `(x, y)`. All coordinates are measured with `(0, 0)` being the top-right corner
of the field. Here's an example of the initial game state file:
```
ROBOT-X: 4
ROBOT-Y: 4
ROBOT-DIRECTION: NORTH
FIELD-WIDTH: 9
ITEMS: (4, 4); (5, 6)
WALLS: (2, 3)
```

## Simulation Tips
While the program is running, you can left click on the window to pause the simulation, and right click to advance
one frame of robot movement at a time.