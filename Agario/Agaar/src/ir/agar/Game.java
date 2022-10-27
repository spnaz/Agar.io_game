package ir.agar;

import ir.agar.Controller.*;
import ir.agar.View.*;
import ir.agar.Model.ClientEngine;
import ir.agar.Model.GameEngine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class Game {
    public static final long GAME_SLEEP = 50;
    public static final int RANDOM_POINTS_COUNT = 150;
    public static final long RANDOM_POINTS_CREATION_PREIOD = 5000L;
    public static final int RANDOM_POINTS_PER_PERIOD = 5;
    public static final long POWER_TIME = 3000;


    /***
     * Min : 400 * 400
     */



    public static int width = 1000;
    public static int height = 600;


    public static GameEngine gameEngine;

    public static JFrame gameFrame;
    public static CardLayout gameFrameLayout;

    private static JPanel containerPanel;
    private static GamePanel gamePanel;

    private static RegisterPanel registerPanel;
    private static LoginOrRegister loginOrRegister;

    private static CreateServer createServer;


    // used for activating powerups and powerdowns
    private static int numberPressed = -1;

    public static int getNumberPressed() {
        int numberPressed = Game.numberPressed;
        Game.numberPressed = -1;

        return numberPressed;
    }

    public static void setNumberPressed(int numberPressed) {
        System.out.println(numberPressed);
        Game.numberPressed = numberPressed;
    }

    public static void main(String[] args) {

        gameFrame = new JFrame("Agar");
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setVisible(true);
        gameFrameLayout = new CardLayout();
        gameFrame.setLocationRelativeTo(null);
        gameFrame.setBounds(0, 0, width, height);
//        jFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        gameFrame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {
            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {
                if (keyEvent.getKeyChar() - '0' >= 0 && keyEvent.getKeyChar() - '0' <= 9) {
                    setNumberPressed(keyEvent.getKeyChar() - '0');
                }
            }
        });


        FindServers findServers = new FindServers();

        loginOrRegister = new LoginOrRegister();
        MainMenu mainMenu = new MainMenu();

        createServer = new CreateServer();
        gamePanel = new GamePanel();

        registerPanel = new RegisterPanel();

        containerPanel = new JPanel();
        containerPanel.setSize(width, height);
        containerPanel.setLayout(gameFrameLayout);
        gameFrame.add(containerPanel);

        containerPanel.add(findServers, "find");
        containerPanel.add(loginOrRegister, "lor");
        containerPanel.add(mainMenu, "main");
        containerPanel.add(createServer, "create");
        containerPanel.add(gamePanel, "game");
        containerPanel.add(registerPanel, "register");

        gameFrameLayout.show(containerPanel, "main");
//
//        RegisterPanel registerPanel = new RegisterPanel();
//        jFrame.add(registerPanel);
//
//        jFrame.revalidate();
//        jFrame.repaint();

//

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                //Audio.play();
            }
        });
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();


//       Painter.run();
    }

    public static GamePanel getGamePanel() {
        return gamePanel;
    }

    public static void setGameEngine(GameEngine gameEngine) {
        Game.gameEngine = gameEngine;
    }

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }

    public static GameEngine getGameEngine() {
        return gameEngine;
    }

    public static JFrame getGameFrame() {
        return gameFrame;
    }

    public static RegisterPanel getRegisterPanel() {
        return registerPanel;
    }

    public static void openPanel(String name) {
        gameFrameLayout.show(containerPanel, name);
    }

    public static LoginOrRegister getLoginOrRegister() {
        return loginOrRegister;
    }

    public static void setSize(int width, int height) {
        Game.width = width;
        Game.height = height;
        getGameFrame().setSize(width, height);
    }

    public static void start() {

        containerPanel.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseMoved(MouseEvent mouseEvent) {
                ClientEngine.getCurrentInstance().setMouseLocation(mouseEvent.getX(), mouseEvent.getY());
            }
        });
        ClientController.getCurrentInstance().start();
        gameEngine = new ClientEngine();
        new ir.agar.Controller.Painter().start();
        openPanel("game");
        gameFrame.requestFocusInWindow();
        gameFrame.requestFocus();
    }

    public static CreateServer getCreateServer() {
        return createServer;
    }
}
