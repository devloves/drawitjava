package org.devster;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DrawingCanvas extends JPanel {
    private Point previousPoint;
    private Color currentColor = Color.BLACK; // Default color
    private boolean isErasing = false; // Flag to indicate if the eraser is active
    private final int brushSize = 5;

    public DrawingCanvas() {
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Point currentPoint = e.getPoint();
                if (previousPoint != null) {
                    // Draw a line segment between the current and previous points
                    Graphics2D g = (Graphics2D) getGraphics();
                    g.setStroke(new BasicStroke(brushSize));
                    g.setColor(isErasing ? getBackground() : currentColor);
                    g.drawLine(previousPoint.x, previousPoint.y, currentPoint.x, currentPoint.y);
                }
                previousPoint = currentPoint;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                previousPoint = null;
            }

            @Override
            public void mousePressed(MouseEvent e) {
                // Reset the previous point when the mouse is pressed
                previousPoint = null;
            }
        });
    }

    // Method to change the current color
    public void setColor(Color color) {
        this.currentColor = color;
        this.isErasing = false; // Ensure erasing is turned off when changing colors
    }

    // Method to activate the eraser
    public void Eraser() {
        this.isErasing = !this.isErasing;
    }
}
