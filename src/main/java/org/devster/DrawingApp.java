package org.devster;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A Drawing Application that allows users to draw, erase and modify images.
 * the application uses FlatLightLaf for its Look and Feel
 */
public class DrawingApp extends JFrame {

	/**
	 *  Constructs a new DrawingApp with a DrawingCanvas and a side Panel for controls.
	 */
    public DrawingApp() {
        DrawingCanvas canvas = new DrawingCanvas();
        setVisible(true);
        setTitle("Drawing App in Java");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
		getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		setForeground(Color.BLACK);
		add(canvas);
		canvas.setBorder(BorderFactory.createTitledBorder("Drawing Canvas"));
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

	/**
	 * The main method that initializes the look and feel of the application
	 * and creates a new instance of DrawingApp.
	 *
	 * @param args Command-line arguments ( not used )
	 */
    public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(new FlatLightLaf());
		} catch( Exception ex ) {
			System.err.println( "Failed to initialize LaF" );
		}
		JFrame.setDefaultLookAndFeelDecorated(true);
        new DrawingApp();
    }
}