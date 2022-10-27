package ir.agar.Controller;

import ir.agar.Game;
import ir.agar.Game;
import ir.agar.Model.ServerEngine;

import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;


public class ServerController extends Thread {

    private static ServerController currentInstance;

    private String name;
    private int port;
    private int width;
    private int height;
    private int players;

    private List<ClientConnection> clients = Collections.synchronizedList(new ArrayList<>());

    public ServerController(String name, int port, int width, int height) {
        this.name = name;
        this.port = port;
        this.width = width;
        this.height = height;
        currentInstance = this;
    }

    public static ServerController getCurrentInstance() {
        return currentInstance;
    }

    public static ServerController create(String name, String port, String width, String height) {
        try {
            return new ServerController(name, Integer.parseInt(port), Integer.parseInt(width), Integer.parseInt(height));
        } catch (NumberFormatException e) {
            return new ServerController(name, 8585, 1000, 600);
        }
    }

    public void init() {
        Game.getGameFrame().setSize(width, height);
        ServerEngine serverEngine = new ServerEngine();
        serverEngine.start();
        Game.setGameEngine(serverEngine);
        Game.getGamePanel().init();
        Thread listenThread = new Thread(new Runnable() {
            @Override
            public void run() {
                ServerSocket serverSocket = null;
                try {
                    serverSocket = new ServerSocket(port);

                    while (true) {
                        try {
                            Socket client = serverSocket.accept();
                            ClientConnection clientConnectionThread = new ClientConnection(client);
                            clients.add(clientConnectionThread);
                            clientConnectionThread.start();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(Game.getGameFrame(),
                            "There was an error for creating server: \n\n" + e.getMessage(),
                            "Server Creation Error",
                            JOptionPane.ERROR_MESSAGE);

                    JButton createServerButton = Game.getCreateServer().getCreateServerButton();
                    createServerButton.setEnabled(true);
                    createServerButton.setText("Create Server");
                }
            }
        });

        listenThread.start();
    }

    public void updatePlayersCount() {
        players = 0;

        for (ClientConnection client : clients)
            if (client.isConnected() && client.isPlaying())
                players++;
    }

    public int getPlayers() {
        return players;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getPort() {
        return port;
    }

    public String getServerName() {
        return name;
    }

    public String[] getServerData() {
        String[] serverData = new String[5];
        serverData[0] = name;
        serverData[1] = port + "";
        serverData[2] = width + "";
        serverData[3] = height + "";
        serverData[4] = players + "";

        return serverData;
    }

    public void removeClientSocket(ClientConnection clientConnection) {
        clients.remove(clientConnection);
    }

    @Override
    public void run() {
        while (true) {
            if (Game.getGameEngine().isUpdated()) {
                for (ClientConnection c : clients) {
                    c.updateClient();
                }
            }
            try {
                sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
