package org.devster;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
	private boolean isErasing = false; // Flag to indicate if the eraser is active
	private int brushSize = 5; // Brush Size

	/**
	 * Constructs a new DrawingCanvas with default settings.
	 */
	public DrawingCanvas() {
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				currentPath = new Path2D.Double();
				currentPath.moveTo(e.getX(), e.getY());
				repaint();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (currentPath != null) {
					drawablePaths.add(new DrawablePath(currentPath, currentColor, brushSize));
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
			g2d.setColor(drawablePath.getColor());
			g2d.setStroke(new BasicStroke(drawablePath.getStrokeSize(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			g2d.draw(drawablePath.getPath());
		}

		// Render current drawing path
		if (currentPath != null) {
			g2d.setColor(isErasing ? getBackground() : currentColor);
			g2d.setStroke(new BasicStroke(brushSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			g2d.draw(currentPath);
		}
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

	/**
	 * A record to represent a drawable path with its color.
	 *
	 * @param path The Path to be drawn.
	 *
	 * @param color The color of the path
	 */
	private record DrawablePath(Path2D.Double path, Color color, int StrokeSize) {
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
	}
}
