package ir.spnaz.agar.View;

import ir.spnaz.agar.Controller.Audio;
import ir.spnaz.agar.Controller.GamePainter;
import ir.spnaz.agar.Model.Direction;
import ir.spnaz.agar.Model.Engine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class GamePanel extends JPanel {
    public GamePanel() {
        super(true);
        this.setFocusable(true);
        this.requestFocusInWindow();


        this.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseMoved(MouseEvent mouseEvent) {
                Engine.getCurrentInstance().setMouseLocation(mouseEvent.getX(), mouseEvent.getY());
            }
        });
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        GamePainter.paint(graphics);
    }
}
