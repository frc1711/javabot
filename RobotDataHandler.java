import java.util.ArrayList;
import java.util.List;

public class RobotDataHandler {
	
	private static final String
		ROBOT_DATA_UPDATE_INDICATOR = "ROBOT DATA UPDATE",
		PAUSED_KEY = "PAUSED";
	
	private final GameState gameState = new GameState();
	
	public String responseFromRawData (List<String> linesList) {
		// Gets the actual lines of data from the entire request
		// (headers + data) -- data starts after the first empty line
		ArrayList<String> dataLines = new ArrayList<String>();
		boolean dataContentReached = false;
		for (String line : linesList) {
			if (dataContentReached) dataLines.add(line);
			if (line.equals("")) dataContentReached = true;
		}
		
		// Ensures the data actually signifies an update
		if (dataLines.size() >= 1 && dataLines.get(0).equals(ROBOT_DATA_UPDATE_INDICATOR)) {
			dataLines.remove(0);
			try {
				processDataLines(dataLines);
				return gameState.getDataString();
			} catch (LookupException e) {
				return "";
			}
		} return "";
	}
	
	private void processDataLines (List<String> dataLines) throws LookupException {
		// Only update the game state if the updates aren't paused
		if ((getValue(dataLines, PAUSED_KEY)).equals("false"))
			gameState.update();
	}
	
	// In the "key:value&key:value&..." structure
	private String getValue (List<String> dataLines, String key) throws LookupException {
		for (String line : dataLines) {
			String[] keyValuePair = line.split(":");
			if (keyValuePair.length != 2) throw new LookupException();
			if (keyValuePair[0].equals(key)) return keyValuePair[1];
		} throw new LookupException();
	}
	
	private class LookupException extends Exception { }
	
}