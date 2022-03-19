package low;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GameWindow {
	
	public static final double SIZE = 600;
	public static final int NUM_CELLS = 10;
	
	private static final double CELL_SIZE = SIZE / NUM_CELLS;
	
	private final JFrame jFrame = new JFrame();
	private final GameCanvas canvas = new GameCanvas();
	
	private final RobotState robotState;
	
	public GameWindow (RobotState robotState) {
		this.robotState = robotState;
		
		canvas.setPreferredSize(new Dimension((int)SIZE, (int)SIZE));
		
		jFrame.setTitle("Javabot");
		jFrame.setSize((int)SIZE, (int)SIZE);
		jFrame.setResizable(false);
		jFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		jFrame.setLocationRelativeTo(null);
		jFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing (WindowEvent e) { onClose(); }
		});
		
		jFrame.add(canvas);
		jFrame.pack();
	}
	
	private class GameCanvas extends JPanel {
		@Override
		public void paintComponent (Graphics graphics) {
			Graphics2D g = (Graphics2D)graphics;
			drawBackground(g);
			drawRobot(g);
		}
		
		private void drawRobot (Graphics2D g) {
			AffineTransform transform = g.getTransform();
			
			g.translate((int)((robotState.x + 0.5) * CELL_SIZE), (int)((robotState.y + 0.5) * CELL_SIZE));
			g.rotate(-Math.PI / 2 * robotState.rotation);
			
			g.setColor(Color.BLACK);
			g.setStroke(new BasicStroke(2));
			g.drawPolygon(
				new int[] {
					(int)(-0.4 * CELL_SIZE),
					(int)(0.4 * CELL_SIZE),
					(int)(-0.4 * CELL_SIZE)},
				new int[] {
					(int)(-0.35 * CELL_SIZE),
					0,
					(int)(0.35 * CELL_SIZE)},
				3);
			
			g.setTransform(transform);
		}
		
		private void drawBackground (Graphics2D g) {
			// Draw background
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, (int)SIZE, (int)SIZE);
			
			// Draw gridlines
			g.setColor(Color.BLACK);
			g.setStroke(new BasicStroke(1));
			
			for (int row = 1; row < NUM_CELLS; row ++)
				g.drawLine(0, (int)(row*CELL_SIZE), (int)SIZE, (int)(row*CELL_SIZE));
			
			for (int col = 1; col < NUM_CELLS; col ++)
				g.drawLine((int)(col*CELL_SIZE), 0, (int)(col*CELL_SIZE), (int)SIZE);
		}
	}
	
	public void displayNextFrame () {
		canvas.repaint();
	}
	
	public void onClose () {
		jFrame.setVisible(false);
		jFrame.dispose();
		System.exit(0);
	}
	
	public void start () {
		jFrame.setVisible(true);
		canvas.setVisible(true);
	}
	
}