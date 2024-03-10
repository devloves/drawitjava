package org.devster;

import com.bric.colorpicker.ColorPickerDialog;
import com.formdev.flatlaf.FlatIntelliJLaf;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

/**
 * A Drawing Application that allows users to draw, erase and modify images.
 * the application uses FlatIntelliJLaf for its Look and Feel
 */
public class DrawingApp extends JFrame {

	/**
	 *  Constructs a new DrawingApp with a DrawingCanvas and a side Panel for controls.
	 */
    public DrawingApp() {
        DrawingCanvas canvas = new DrawingCanvas();
        setVisible(true);
        setTitle("Drawit - Java");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
		getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		setForeground(Color.BLACK);
		add(canvas);
		canvas.setBorder(BorderFactory.createTitledBorder("Drawing Canvas"));
		canvas.setMaximumSize(new Dimension(480, 400));
		JPanel sidePanel = new JPanel();
        JButton eraser = new JButton();
		try {
			eraser.setIcon(new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/eraser.png")))));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		eraser.addActionListener(e -> {
			canvas.toggleEraser();
			eraser.setBackground(canvas.isErasing() ? Color.GREEN : Color.WHITE);
			canvas.setCursor(canvas.isErasing() ? new Cursor(Cursor.W_RESIZE_CURSOR) : new Cursor(Cursor.CROSSHAIR_CURSOR));
		});
		String eraserTip = "This is used to erase your drawings";
		eraser.setToolTipText(eraserTip);
        eraser.setSize(new Dimension(50, 50));
		JButton bucketTool = new JButton();
		bucketTool.addActionListener(e -> {
			canvas.toggleBucketFill();
			bucketTool.setBackground(canvas.isBucketToolEnabled() ? Color.GREEN : Color.WHITE);
		});
		String bucketFillTip = "This is used to fill your shapes with color.";
		bucketTool.setToolTipText(bucketFillTip);
		bucketTool.setSize(new Dimension(50, 50));
		try {
			bucketTool.setIcon(new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/bucket.png")))));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		sidePanel.add(bucketTool);
        sidePanel.add(eraser);
		JButton undoButton = new JButton();
		JButton redoButton = new JButton();
		undoButton.addActionListener(e -> canvas.undo());
		redoButton.addActionListener(e -> canvas.redo());
		try {
			undoButton.setIcon(new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/undo-arrow.png")))));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		try {
			redoButton.setIcon(new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/redo-arrow.png")))));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		sidePanel.add(undoButton);
		sidePanel.add(redoButton);
		JSlider brushSlider = new JSlider(JSlider.HORIZONTAL, 0, 50, 5);
		brushSlider.addChangeListener(e -> canvas.changeSize(brushSlider.getValue()));
		brushSlider.isOptimizedDrawingEnabled();
		brushSlider.setMinorTickSpacing(5);
		brushSlider.setMajorTickSpacing(10);
		brushSlider.setPaintTicks(true);
		brushSlider.setPaintLabels(true);
		sidePanel.add(brushSlider);
		sidePanel.setBorder(BorderFactory.createTitledBorder("Features"));
		sidePanel.setMaximumSize(new Dimension(500, 100));
		getContentPane().add(sidePanel);
		JButton colorPicker = new JButton("Choose Color");
		colorPicker.addActionListener(e -> {
			Color c = ColorPickerDialog.showDialog(null, Color.GREEN);
			canvas.setColor(c);
		});
		sidePanel.add(colorPicker);
		setResizable(false);
		canvas.setCursor(canvas.isErasing() ? new Cursor(Cursor.W_RESIZE_CURSOR) : new Cursor(Cursor.CROSSHAIR_CURSOR));
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
			UIManager.setLookAndFeel(new FlatIntelliJLaf());
		} catch( Exception ex ) {
			System.err.println( "Failed to initialize LaF" );
		}
		JFrame.setDefaultLookAndFeelDecorated(true);
        new DrawingApp();
    }
}