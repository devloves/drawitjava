package org.devster;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

public class DrawingCanvas extends JPanel {
	private List<DrawablePath> drawablePaths = new ArrayList<>(); // List to store drawn paths
	private List<DrawablePath> undonePaths = new ArrayList<>(); // List to store undone paths
	private Path2D.Double currentPath; // Current path being drawn
	private Color currentColor = Color.BLACK; // Default color
	private boolean isErasing = false; // Flag to indicate if the eraser is active
	private int brushSize = 5; // Brush Size

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
					drawablePaths.add(new DrawablePath(currentPath, currentColor));
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
			g2d.setStroke(new BasicStroke(brushSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			g2d.draw(drawablePath.getPath());
		}

		// Render current drawing path
		if (currentPath != null) {
			g2d.setColor(isErasing ? getBackground() : currentColor);
			g2d.setStroke(new BasicStroke(brushSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			g2d.draw(currentPath);
		}
	}

	public void changeSize(int size) {
		this.brushSize = size;
	}

	// Method to change the current color
	public void setColor(Color color) {
		this.currentColor = color;
		this.isErasing = false; // Ensure erasing is turned off when changing colors
	}

	// Method to toggle the eraser
	public void toggleEraser() {
		this.isErasing = !this.isErasing;
	}

	// Method to undo the last action
	public void undo() {
		if (!drawablePaths.isEmpty()) {
			undonePaths.add(drawablePaths.remove(drawablePaths.size() - 1));
			repaint();
		}
	}

	// Method to redo the last undone action
	public void redo() {
		if (!undonePaths.isEmpty()) {
			drawablePaths.add(undonePaths.remove(undonePaths.size() - 1));
			repaint();
		}
	}

	// Record to represent a drawable path with its color
	private record DrawablePath(Path2D.Double path, Color color) {
		public Path2D.Double getPath() {
			return path;
		}

		public Color getColor() {
			return color;
		}
	}
}
