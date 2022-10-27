package ir.agar.View;

import ir.agar.Controller.Painter;
import ir.agar.Game;
import ir.agar.Model.ClientEngine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class GamePanel extends JPanel {
    public GamePanel() {
        super(true);
    }

    public void init() {
        this.setFocusable(true);
        this.requestFocusInWindow();

        this.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {
                System.out.println(keyEvent.getKeyChar());
            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                System.out.println(keyEvent.getKeyChar());
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {
                System.out.println(keyEvent.getKeyChar());
            }
        });

//        this.addKeyListener(new KeyListener() {
//            @Override
//            public void keyTyped(KeyEvent keyEvent) {
//                if (keyEvent.getKeyChar() == 'M' || keyEvent.getKeyChar() == 'm'){
//                    Audio.toggleStatus();
//                }
//            }
//
//            @Override
//            public void keyPressed(KeyEvent keyEvent) {
//                GameEngine gameEngine = Game.getGameEngine();
//                switch (keyEvent.getExtendedKeyCode()){
//                    case 103:
//                        gameEngine.setDirection(Direction.TOP_LEFT);
//                        break;
//                    case 104:
//                        gameEngine.setDirection(Direction.TOP);
//                        break;
//                    case 105:
//                        gameEngine.setDirection(Direction.TOP_RIGHT);
//                        break;
//                    case 100:
//                        gameEngine.setDirection(Direction.LEFT);
//                        break;
//                    case 102:
//                        gameEngine.setDirection(Direction.RIGHT);
//                        break;
//                    case 97:
//                        gameEngine.setDirection(Direction.BOTTOM_LEFT);
//                        break;
//                    case 98:
//                        gameEngine.setDirection(Direction.BOTTOM);
//                        break;
//                    case 99:
//                        gameEngine.setDirection(Direction.BOTTOM_RIGHT);
//                        break;
//                    case 107:
//                        gameEngine.splitPlayer(1);
//                        break;
//                    case 32:
//                        gameEngine.splitPlayer(0);
//                        break;
//                }
//            }
//
//            @Override
//            public void keyReleased(KeyEvent keyEvent) {
//                GameEngine gameEngine = Game.getGameEngine();
//                switch (keyEvent.getExtendedKeyCode()){
//                    case 103:
//                        if(gameEngine.getDirection() == Direction.TOP_LEFT)
//                            ((GameEngine) gameEngine).setDirection(Direction.TOP_LEFT);
//                        break;
//                    case 104:
//                        if(gameEngine.getDirection() == Direction.TOP)
//                            ((GameEngine) gameEngine).setDirection(Direction.TOP);
//                        break;
//                    case 105:
//                        if(gameEngine.getDirection() == Direction.TOP_RIGHT)
//                            ((GameEngine) gameEngine).setDirection(Direction.TOP_RIGHT);
//                        break;
//                    case 100:
//                        if(gameEngine.getDirection() == Direction.LEFT)
//                            ((GameEngine) gameEngine).setDirection(Direction.LEFT);
//                        break;
//                    case 102:
//                        if(gameEngine.getDirection() == Direction.RIGHT)
//                            ((GameEngine) gameEngine).setDirection(Direction.RIGHT);
//                        break;
//                    case 97:
//                        if(gameEngine.getDirection() == Direction.BOTTOM_LEFT)
//                            ((GameEngine) gameEngine).setDirection(Direction.BOTTOM_LEFT);
//                        break;
//                    case 98:
//                        if(gameEngine.getDirection() == Direction.BOTTOM)
//                            ((GameEngine) gameEngine).setDirection(Direction.BOTTOM);
//                        break;
//                    case 99:
//                        if(gameEngine.getDirection() == Direction.BOTTOM_RIGHT)
//                            ((GameEngine) gameEngine).setDirection(Direction.BOTTOM_RIGHT);
//                        break;
//                }
//            }
//        });
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Painter.paint(graphics);
    }
}
