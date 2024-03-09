package org.devster;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

/**
 * A Custom JPanel that acts as the canvas for drawing and manipulation
 */
public class DrawingCanvas extends JPanel {
	private final List<DrawablePath> drawablePaths = new ArrayList<>(); // List to store drawn paths
	private final List<DrawablePath> undonePaths = new ArrayList<>(); // List to store undone paths
	private Path2D.Double currentPath; // Current path being drawn
	private Color currentColor = Color.BLACK; // Default color
	private boolean isBucketToolOn = false;
	private boolean isErasing = false; // Flag to indicate if the eraser is active
	private int brushSize = 5; // Brush Size

	/**
	 * Constructs a new DrawingCanvas with default settings.
	 */
	public DrawingCanvas() {
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (isBucketToolOn) {
					// Call the fill method with the mouse coordinates
					fillbucket(e.getX(), e.getY(), (Graphics2D) getGraphics());
				} else {
					// If the bucket tool is not activated, start drawing a path
					currentPath = new Path2D.Double();
					currentPath.moveTo(e.getX(), e.getY());
					repaint();
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (currentPath != null) {
					drawablePaths.add(new DrawablePath(currentPath, currentColor, new Area(currentPath), brushSize));
					currentPath = null;
					undonePaths.clear(); // Clear the undone paths when a new drawing is added
					repaint();
				}
			}
		});

		addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				if (currentPath != null) {
					currentPath.lineTo(e.getX(), e.getY());
					repaint();
				}
			}
		});
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;

		// Render completed shapes
		for (DrawablePath drawablePath : drawablePaths) {
			g2d.setColor(getBackground());
			g2d.fill(drawablePath.getFillColor());
			g2d.setColor(drawablePath.getColor());
			g2d.setStroke(new BasicStroke(drawablePath.getStrokeSize(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			g2d.draw(drawablePath.getPath());
		}

		// Render current drawing path
		// Render current drawing path
		if (currentPath != null) {
			if (isErasing) {
				g2d.setColor(getBackground());
				g2d.setStroke(new BasicStroke(brushSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
				g2d.draw(currentPath);
			} else {
				g2d.setColor(currentColor);
				g2d.setStroke(new BasicStroke(brushSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
				g2d.draw(currentPath);
			}
		}
	}

	private void fillbucket(int x, int y, Graphics2D g2d) {
		if (isBucketToolOn) {
			Point fillPoint = new Point(x, y);
			for (DrawablePath drawablePath : drawablePaths) {
				Path2D.Double path = drawablePath.getPath();
				if (path.contains(fillPoint)) {
					// Create an Area from the path and fill it
					Area area = new Area(path);
					g2d.setColor(currentColor);
					g2d.fill(area);
					new DrawablePath(path, currentColor, area, brushSize);
					break;
				}
			}
		}
	}

	/**
	 * returns current status of Erasing
	 */
	public boolean isErasing() {
		return isErasing;
	}

	/**
	 * Changes the size of the brush used for drawing
	 *
	 * @param size The New Brush Size.
	 */
	public void changeSize(int size) {
		this.brushSize = size;
	}

	/**
	 * Sets the current drawing color.
	 *
	 * @param color The new color to use for the drawing
	 */
	public void setColor(Color color) {
		this.currentColor = color;
		this.isErasing = false; // Ensure erasing is turned off when changing colors
	}

	/**
	 * Toggles the eraser mode on or off.
	 */
	public void toggleEraser() {
		this.isErasing = !this.isErasing;
	}

	/**
	 * Toggles the bucket fill mode on or off.
	 */
	public void toggleBucketFill() {
		this.isBucketToolOn = !this.isBucketToolOn;
	}

	/**
	 * Undoes the last drawn or erased path.
	 */
	public void undo() {
		if (!drawablePaths.isEmpty()) {
			undonePaths.add(drawablePaths.remove(drawablePaths.size() - 1));
			repaint();
		}
	}

	/**
	 * Redoes the last undone path.
	 */
	public void redo() {
		if (!undonePaths.isEmpty()) {
			drawablePaths.add(undonePaths.remove(undonePaths.size() - 1));
			repaint();
		}
	}

	public boolean isBucketToolEnabled() {
		return isBucketToolOn;
	}

	/**
	 * A record to represent a drawable path with its color.
	 *
	 * @param path The Path to be drawn.
	 *
	 * @param color The color of the path
	 */
	private record DrawablePath(Path2D.Double path, Color color, Shape fillcolor, int StrokeSize) {
		/**
		 * Returns the path to be drawn
		 *
		 * @return the path
		 */
		public Path2D.Double getPath() {
			return path;
		}

		/**
		 * Returns the color of the path.
		 *
		 * @return the Color
		 */
		public Color getColor() {
			return color;
		}

		/**
		 * Returns the size of the path stroke.
		 *
		 * @return the size of the stroke
		 */
		public int getStrokeSize() {
			return StrokeSize;
		}

		public Shape getFillColor() {
			return fillcolor;
		}
	}
}
