package ir.spnaz.agar;

import ir.spnaz.agar.Controller.Audio;
import ir.spnaz.agar.Controller.GamePainter;
import ir.spnaz.agar.Model.Direction;
import ir.spnaz.agar.Model.Engine;
import ir.spnaz.agar.View.GamePanel;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Game {
    public static final long GAME_SLEEP = 20;
    public static final int RANDOM_POINTS_COUNT = 150;
    public static final long RANDOM_POINTS_CREATION_PREIOD = 5000L;
    public static final int RANDOM_POINTS_PER_PERIOD = 5;
    public static final long POWER_TIME = 3000;


    /***
     * Min : 400 * 400
     */


    public static int width = 1000;
    public static int height = 600;


    public static int mapWidth = 600;
    public static int mapHeight = 400;
    public static double gearRatio = 10;
    public static double maxSpeed = 40; // pixels per second

    public static Engine gameEngine;

    private static JPanel jPanel;

    public static void main(String[] args) {

        JFrame jFrame = new JFrame("Agar");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setVisible(true);
        jFrame.setBounds(0, 0, width, height);
//        jFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);



        jPanel = new GamePanel();
        jFrame.add(jPanel);

        jFrame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {
                if (keyEvent.getKeyChar() == 'M' || keyEvent.getKeyChar() == 'm'){
                    Audio.toggleStatus();
                }
            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                switch (keyEvent.getExtendedKeyCode()){
                    case 103:
                        Engine.getCurrentInstance().keyPressed(Direction.TOP_LEFT);
                        break;
                    case 104:
                        Engine.getCurrentInstance().keyPressed(Direction.TOP);
                        break;
                    case 105:
                        Engine.getCurrentInstance().keyPressed(Direction.TOP_RIGHT);
                        break;
                    case 100:
                        Engine.getCurrentInstance().keyPressed(Direction.LEFT);
                        break;
                    case 102:
                        Engine.getCurrentInstance().keyPressed(Direction.RIGHT);
                        break;
                    case 97:
                        Engine.getCurrentInstance().keyPressed(Direction.BOTTOM_LEFT);
                        break;
                    case 98:
                        Engine.getCurrentInstance().keyPressed(Direction.BOTTOM);
                        break;
                    case 99:
                        Engine.getCurrentInstance().keyPressed(Direction.BOTTOM_RIGHT);
                        break;
                    case 107:
                        Engine.getCurrentInstance().splitPlayer(1);
                        break;
                    case 32:
                        Engine.getCurrentInstance().splitPlayer(0);
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {
                switch (keyEvent.getExtendedKeyCode()){
                    case 103:
                        Engine.getCurrentInstance().keyReleased(Direction.TOP_LEFT);
                        break;
                    case 104:
                        Engine.getCurrentInstance().keyReleased(Direction.TOP);
                        break;
                    case 105:
                        Engine.getCurrentInstance().keyReleased(Direction.TOP_RIGHT);
                        break;
                    case 100:
                        Engine.getCurrentInstance().keyReleased(Direction.LEFT);
                        break;
                    case 102:
                        Engine.getCurrentInstance().keyReleased(Direction.RIGHT);
                        break;
                    case 97:
                        Engine.getCurrentInstance().keyReleased(Direction.BOTTOM_LEFT);
                        break;
                    case 98:
                        Engine.getCurrentInstance().keyReleased(Direction.BOTTOM);
                        break;
                    case 99:
                        Engine.getCurrentInstance().keyReleased(Direction.BOTTOM_RIGHT);
                        break;
                }
            }
        });

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
//                Audio.play();
            }
        });
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();

        gameEngine = new Engine();

        GamePainter.run();
    }

    public static JPanel getjPanel() {
        return jPanel;
    }

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }
}
