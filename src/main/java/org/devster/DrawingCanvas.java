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
 * A custom JPanel that serves as a canvas for drawing and manipulation.
 * It supports drawing paths, erasing, filling shapes with a bucket tool,
 * and undo/redo operations.
 */
public class DrawingCanvas extends JPanel {
	// List to store drawn paths
	private final List<DrawablePath> drawablePaths = new ArrayList<>();
	// List to store undone paths
	private final List<DrawablePath> undonePaths = new ArrayList<>();
	// Current path being drawn, null when not drawing
	private Path2D.Double currentPath;
	// Default color for drawing
	private Color currentColor = Color.BLACK;
	// Fill Color of the Shape
	private Color fillColor;
	// Flag to indicate if the bucket tool is active
	private boolean isBucketToolOn = false;
	// Flag to indicate if the eraser is active
	private boolean isDrawingLine = false;
	// Flag to indicate drawing of a line.
	private boolean isErasing = false;
	// Brush Size for drawing
	private int brushSize = 5;

	private int lastX = 0;
	private int lastY = 0;
	private int firstX = 0;
	private int firstY = 0;

	/**
	 * Constructs a new DrawingCanvas with default settings.
	 */
	public DrawingCanvas() {
		// Mouse event listeners for drawing and bucket fill operations
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				// Check if the bucket tool is active to determine drawing behavior
				if (isBucketToolOn) {
					// Call the fill method with the mouse coordinates
					fillbucket(e.getX(), e.getY(), getGraphics());
				} else {
					// If the bucket tool is not activated, start drawing a path
					currentPath = new Path2D.Double();
					currentPath.moveTo(e.getX(), e.getY());
					firstX = e.getX();
					firstY = e.getY();
					repaint();
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (currentPath != null) {
					drawablePaths.add(new DrawablePath(currentPath, isErasing ? getBackground() : currentColor, new Area(currentPath), brushSize, false, fillColor));
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
					if (isDrawingLine) {
						// Clear the previous path and create a new one for the straight line
						currentPath.reset();
						currentPath.moveTo(firstX, firstY);
						currentPath.lineTo(e.getX(), e.getY());
					} else {
						// Free drawing mode
						currentPath.lineTo(e.getX(), e.getY());
					}
					lastX = e.getX();
					lastY = e.getY();
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
			if (drawablePath.isFilled()) {
				g2d.setColor(drawablePath.getFillColor());
				g2d.fill(drawablePath.getPath());
			}
			g2d.setColor(drawablePath.getColor());
			g2d.setStroke(new BasicStroke(drawablePath.getStrokeSize(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			g2d.draw(drawablePath.getPath());
		}

		// Render current drawing path
		if (currentPath != null) {
			if (isErasing) {
				g2d.setColor(getBackground());
			} else {
				g2d.setColor(currentColor);
			}
			g2d.setStroke(new BasicStroke(brushSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			if(isDrawingLine) {
				g2d.drawLine(firstX, firstY, lastX, lastY);
			} else {
				g2d.draw(currentPath);
			}
		}
	}

	private void fillbucket(int x, int y, Graphics g) {
		if(g == null){return;}
		Graphics2D g2d = (Graphics2D) g;
		if (isBucketToolOn) {
			Point fillPoint = new Point(x, y);
			for (DrawablePath drawablePath : drawablePaths) {
				Path2D.Double path = drawablePath.getPath();
				if (path.contains(fillPoint) && drawablePath.getColor() != getBackground()) {
					// Create an Area from the path and fill it
					Area area = new Area(path);
					g2d.setColor(currentColor);
					g2d.fill(area);
					drawablePath.setFilledColor(currentColor);
					drawablePaths.add(new DrawablePath(path, currentColor, area, brushSize, true, fillColor));
					currentPath = null;
					undonePaths.clear(); // Clear the undone paths when a new drawing is added
					break;
				}
			}
		}
	}

	/**
	 * Returns the current status of Erasing.
	 *
	 * @return true if erasing is active, false otherwise.
	 */
	public boolean isErasing() {
		return isErasing;
	}

	/**
	 * Changes the size of the brush used for drawing.
	 *
	 * @param size The new brush size.
	 */
	public void changeSize(int size) {
		this.brushSize = size;
	}

	/**
	 * Sets the current drawing color.
	 *
	 * @param color The new color to use for the drawing.
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
	 * Toggles the Line mode on or off.
	 */
	public void toggleLineTool() {
		this.isDrawingLine = !this.isDrawingLine;
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

	/**
	 * Is bucket tool enabled boolean.
	 *
	 * @return the boolean
	 */
	public boolean isBucketToolEnabled() {
		return isBucketToolOn;
	}

	/**
	 * Is bucket tool enabled boolean.
	 *
	 * @return the boolean
	 */
	public boolean isLineToolEnabled() {
		return isDrawingLine;
	}

	/**
	 * A class to represent a drawable path with its color, fill shape,
	 * stroke size, and whether it is filled.
	 */
	private static class DrawablePath {

		private Path2D.Double path;
		private Color color;
		private Shape fillshape;
		private int strokesize;
		private boolean isFilled;
		private Color fillColor;

		/**
		 * Instantiates a new Drawable path.
		 *
		 * @param path       the path
		 * @param color      the color
		 * @param fillshape  the fillshape
		 * @param StrokeSize the stroke size
		 * @param isFilled   the is filled
		 * @param fillColor  the fill color
		 */
		public DrawablePath(Path2D.Double path, Color color, Shape fillshape, int StrokeSize, boolean isFilled, Color fillColor) {
			this.path = path;
			this.color = color;
			this.fillshape = fillshape;
			this.strokesize = StrokeSize;
			this.isFilled = isFilled;
			this.fillColor = fillColor;
		}

		/**
		 * Returns the path to be drawn.
		 *
		 * @return the path.
		 */
		public Path2D.Double getPath() {
			return path;
		}

		/**
		 * Returns the color of the path.
		 *
		 * @return the Color.
		 */
		public Color getColor() {
			return color;
		}

		/**
		 * Returns the size of the path stroke.
		 *
		 * @return the size of the stroke.
		 */
		public int getStrokeSize() {
			return strokesize;
		}

		/**
		 * Gets fill color.
		 *
		 * @return the fill color
		 */
		public Color getFillColor() {
			return fillColor;
		}

		/**
		 * Sets filled color.
		 *
		 * @param c the c
		 */
		public void setFilledColor(Color c) {
			this.fillColor = c;
		}

		/**
		 * Is filled boolean.
		 *
		 * @return the boolean
		 */
		public boolean isFilled() {
			return fillColor != null;
		}
	}
}
