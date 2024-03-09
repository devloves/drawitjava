package org.devster;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DrawingApp extends JFrame {

    public DrawingApp() {
        DrawingCanvas canvas = new DrawingCanvas();
        add(canvas);
        setVisible(true);
        setLayout(new GridLayout());
        setTitle("Drawing App in Java");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);

        JButton eraser = new JButton("Eraser");
        eraser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                canvas.Eraser();
            }
        });
        eraser.setSize(new Dimension(50, 50));
        add(eraser);
        System.out.println("Hello world!");
    }

    public static void main(String[] args) {
        new DrawingApp();
    }
}