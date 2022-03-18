package low;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.util.ArrayList;

public class ServerHandler {
	
	private static final int MAIN_PORT = 4444;
	private static final String MAIN_URL = "http://localhost:"+MAIN_PORT;
	
	private static final int ROBOT_DATA_PORT = 4445;
	private static final double ROBOT_DATA_TIMEOUT = 1;
	
	private static final String LOCAL_HTML_MAIN_PAGE = "display-page.html";
	
	private final RobotDataHandler dataHandler;
	private ServerSocket mainServerSocket, robotDataServerSocket;
	private boolean closedServers = false;
	
	public ServerHandler (RobotDataHandler dataHandler) {
		this.dataHandler = dataHandler;
	}
	
	public void start () {
		handleMainPage();
		
		try {
			establishRobotDataLoop();
		} catch (Exception e) {
			handleException(e);
		}
	}
	
	// Robot data socket
	private void establishRobotDataLoop () throws IOException {
		robotDataServerSocket = new ServerSocket(ROBOT_DATA_PORT);
		robotDataServerSocket.setSoTimeout((int)(ROBOT_DATA_TIMEOUT*1000));
		robotDataServerSocket.setReuseAddress(true);
		
		// Establish the connection (possibly getting a socket timeout exception)
		try {
			while (true) {
				Socket robotDataClientSocket = robotDataServerSocket.accept();
				PrintWriter robotDataOutput = new PrintWriter(robotDataClientSocket.getOutputStream(), true);
				BufferedReader robotDataInput = new BufferedReader(new InputStreamReader(robotDataClientSocket.getInputStream()));
				
				// Gives enough time for all the input to be sent
				try { Thread.sleep(200);
				} catch (Exception e) { }
				
				// Reads all received lines and processes them
				writeHeader(robotDataOutput);
				ArrayList<String> lines = new ArrayList<String>();
				
				// Scans all currently 
				while (robotDataInput.ready()) {
					lines.add(robotDataInput.readLine());
				}
				robotDataOutput.println(dataHandler.responseFromRawData(lines));
				
				robotDataClientSocket.close();
			}
		} catch (SocketTimeoutException e) {
			close();
		}
	}
	
	// Main page socket
	private void handleMainPage () {
		new Thread(() -> {
			try {
				// Open necessary socket for writing the main page
				mainServerSocket = new ServerSocket(MAIN_PORT);
				
				// Keeps accepting new sockets as they arrive
				while (true) {
					Socket mainClientSocket = mainServerSocket.accept();
					PrintWriter mainPageOut = new PrintWriter(mainClientSocket.getOutputStream(), true);
					
					// Get local html file
					BufferedReader htmlPageInput = new BufferedReader(new FileReader(LOCAL_HTML_MAIN_PAGE));
					
					// Write html file to the socket
					writeToMainPage(htmlPageInput, mainPageOut);
					
					// Closes the socket after it's had a chance to send the data
					mainClientSocket.close();
				}
			} catch (IOException e) {
				handleException(e);
			}
		}).start();
		
		openPage();
	}
	
	private void openPage () {
		boolean error = false;
		if (Desktop.isDesktopSupported()) {
			try {
				Thread.sleep(1000);
				Desktop.getDesktop().browse(URI.create(MAIN_URL));
			} catch (Exception e) {
				error = true;
			}
		} else error = true;
		
		if (error) {
			System.out.println("Could not open page in browser!");
			System.out.println("Please manually navigate to " + MAIN_URL + " in your browser.");
		}
	}
	
	private void writeToMainPage (BufferedReader htmlPageInput, PrintWriter mainPageOut) throws IOException {
		writeHeader(mainPageOut);
		
		String htmlLine = htmlPageInput.readLine();
		while (htmlLine != null) {
			mainPageOut.println(htmlLine);
			htmlLine = htmlPageInput.readLine();
		}
	}
	
	private void handleException (Exception e) {
		if (closedServers) return;
		System.out.println("There was an exception while running the program.");
		e.printStackTrace();
	}
	
	private void writeHeader (PrintWriter output) {
		output.println("HTTP/1.1 200 OK");
		output.println("Access-Control-Allow-Origin: *");
		output.println("Content-Type: text/html\n");
		output.flush();
	}
	
	public void close () {
		closedServers = true;
		
		try {
			mainServerSocket.close();
			robotDataServerSocket.close();
		} catch (IOException e) { }
	}
	
}