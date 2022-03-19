package javabot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javabot.RobotBase.Direction;

public class InitialGameState {
	
	public int robotX, robotY, fieldWidth;
	public Direction robotDirection = Direction.EAST;
	public Position[] items, walls;
	
	public InitialGameState (String fileName) {
		BufferedReader fileReader;
		
		try {
			fileReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(fileName))));
		} catch (FileNotFoundException e) {
			System.out.println("Could not find file \""+fileName+"\" for reading the initial game state.");
			return;
		}
		
		try {
			ArrayList<String> lines = new ArrayList<String>();
			String line = fileReader.readLine();
			while (line != null) {
				lines.add(line);
				line = fileReader.readLine();
			}
			
			robotX = Integer.parseInt(getAttr("ROBOT-X", lines));
			robotY = Integer.parseInt(getAttr("ROBOT-Y", lines));
			fieldWidth = Integer.parseInt(getAttr("FIELD-WIDTH", lines));
			robotDirection = Direction.parseDirection(getAttr("ROBOT-DIRECTION", lines));
			
			String itemsAttr = getAttr("ITEMS", lines);
			if (itemsAttr != null)
				items = parsePositions(getAttr("ITEMS", lines));
			else items = new Position[0];
			
			String wallsAttr = getAttr("WALLS", lines);
			if (wallsAttr != null)
				walls = parsePositions(getAttr("WALLS", lines));
			else walls = new Position[0];
		} catch (Exception e) {
			System.out.println("Error in reading file \""+fileName+"\" for the initial game state:");
			e.printStackTrace();
		}
	}
	
	private String getAttr (String searchKey, ArrayList<String> lines) {
		for (String line : lines) {
			int indexOfColon = line.indexOf(":");
			String key = line.substring(0, indexOfColon);
			if (searchKey.equals(key)) return line.substring(indexOfColon+1).strip();
		} return null;
	}
	
	private Position[] parsePositions (String str) {
		String[] positionsStr = str.split(";");
		ArrayList<Position> positionsList = new ArrayList<Position>();
		
		for (int i = 0; i < positionsStr.length; i ++) {
			String posStr = positionsStr[i].strip();
			posStr = (posStr.substring(1, posStr.length() - 1)); // remove parentheses
			int x = Integer.parseInt(posStr.split(",")[0].strip());
			int y = Integer.parseInt(posStr.split(",")[1].strip());
			positionsList.add(new Position(x, y));
		}
		
		Position[] positions = new Position[positionsList.size()];
		for (int i = 0; i < positions.length; i ++) {
			positions[i] = positionsList.get(i);
		}
		
		return positions;
	}
	
	public static class Position {
		public int x, y;
		public Position (int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
	
}