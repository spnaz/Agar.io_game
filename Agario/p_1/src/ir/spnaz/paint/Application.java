package ir.spnaz.paint;

import ir.spnaz.paint.App.PaintFrame;

import javax.swing.JFrame;


public class Application {
    public static void main(String[] args) {
        PaintFrame paintFrame = new PaintFrame();
        paintFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        paintFrame.setVisible(true);
    }
}
