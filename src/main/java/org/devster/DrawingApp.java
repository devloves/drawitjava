package org.devster;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DrawingApp extends JFrame {

    public DrawingApp() {
        DrawingCanvas canvas = new DrawingCanvas();
        setVisible(true);
        setTitle("Drawing App in Java");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
		getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		setForeground(Color.BLACK);
		add(canvas);
		canvas.setBorder(BorderFactory.createBevelBorder(1));
		canvas.setMaximumSize(new Dimension(480, 400));
		JPanel sidePanel = new JPanel();
        JButton eraser = new JButton("Eraser");
        eraser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                canvas.toggleEraser();
            }
        });
        eraser.setSize(new Dimension(50, 50));
        sidePanel.add(eraser);

		JSlider brushSlider = new JSlider(JSlider.HORIZONTAL, 0, 50, 5);
		brushSlider.addChangeListener(e -> {
			canvas.changeSize(brushSlider.getValue());
		});
		brushSlider.isOptimizedDrawingEnabled();
		sidePanel.add(brushSlider);
		sidePanel.setBorder(BorderFactory.createTitledBorder("Features"));
		sidePanel.setMaximumSize(new Dimension(500, 100));
		getContentPane().add(sidePanel);
		JButton chooseColor = new JButton("Choose Color");
		chooseColor.addActionListener(e -> {
			Color c = JColorChooser.showDialog(this, "Choose", Color.BLACK);
			canvas.setColor(c);
		});
		sidePanel.add(chooseColor);
		JButton undoButton = new JButton("undo");
		JButton redoButton = new JButton("redo");
		undoButton.addActionListener(e -> {
			canvas.undo();
		});
		redoButton.addActionListener(e -> {
			canvas.redo();
		});
		sidePanel.add(undoButton);
		sidePanel.add(redoButton);
		setResizable(false);
		canvas.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
        System.out.println("Hello world!");
    }

    public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		JFrame.setDefaultLookAndFeelDecorated(true);
        new DrawingApp();
    }
}