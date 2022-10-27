package ir.agar.Controller;

import ir.agar.Model.Objects.Player;
import ir.agar.Model.Objects.PowerType;
import ir.agar.Model.ServerEngine;
import ir.agar.Model.SocketData;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;


public class ClientConnection extends Thread {
    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    private boolean tmpConnection;

    private ArrayList<PowerTypeResource> sentPowerTypes = new ArrayList<>();

    private boolean isConnected = true;
    private boolean doUpdate = false;
    private boolean isPlaying = false;
    private Player player = null;

    public ClientConnection(Socket socket) {
        this.socket = socket;
    }

    private void init() throws Exception {
        socket.setSoTimeout(200);
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectInputStream = new ObjectInputStream(socket.getInputStream());

        SocketData connectionTypeData = (SocketData) objectInputStream.readObject();
        if (connectionTypeData.getType() == SocketData.DataType.CONNECTION_MODE) {
            tmpConnection = (Boolean) connectionTypeData.getData();
        }
    }

    @Override
    public void run() {
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
            disconnect();
            return;
        }

        SocketData dataObject = null;

        ServerEngine serverEngine = ServerEngine.getCurrentInstance();

        if (tmpConnection) {
            dataObject = readObject();

            if (dataObject == null) {
                disconnect();
                return;
            }

            SocketData.DataType dataType = dataObject.getType();

            if (dataType == SocketData.DataType.SERVER_DATA) {

                String[] serverData = ServerController.getCurrentInstance().getServerData();

                SocketData socketDataServerData = new SocketData(SocketData.DataType.SERVER_DATA_RESPONSE, serverData);

                writeObject(socketDataServerData);
            } else if (dataType == SocketData.DataType.REGISTER) {
                Object[] data = (Object[]) dataObject.getData();

                String username = (String) data[0];
                String password = (String) data[1];
                String name = (String) data[2];
                Color color = (Color) data[3];
                Player player = null;
                synchronized (serverEngine) {
                    player = ServerEngine.getCurrentInstance().registerPlayer(username, password, name, color);
                }
                SocketData registerResponse;
                if (player == null) {
                    registerResponse = new SocketData(SocketData.DataType.REGISTER_RESPONSE, false);
                } else {
                    registerResponse = new SocketData(SocketData.DataType.REGISTER_RESPONSE, true);
                }

                writeObject(registerResponse);
            } else if (dataType == SocketData.DataType.LOGIN) {
                String[] loginInfo = (String[]) dataObject.getData();
                boolean login = ServerEngine.getCurrentInstance().checkLogin(loginInfo[0], loginInfo[1]);

                writeObject(new SocketData(SocketData.DataType.LOGIN_RESPONSE, login));
            }

        } else {

            dataObject = readObject();

            if (dataObject == null) {
                disconnect();
                return;
            }

            if (dataObject.getType() == SocketData.DataType.LOGIN) {
                String[] loginInfo = (String[]) dataObject.getData();
                boolean login;
                synchronized (serverEngine) {
                    login = ServerEngine.getCurrentInstance().addPlayerToGame(loginInfo[0], loginInfo[1]);
                }
                writeObject(new SocketData(SocketData.DataType.LOGIN_RESPONSE, login));

                if (!login) {
                    disconnect();
                }

                player = ServerEngine.getCurrentInstance().getPlayer(loginInfo[0]);
            } else {
                disconnect();
                return;
            }


            Object[] objectsToSend = {serverEngine.getPlayers(), serverEngine.getPlayerCircles(), serverEngine.getPowerCircles(), serverEngine.getGearCircles(), serverEngine.getEnergyCircles()};
            while (true) {
                SocketData gameData = new SocketData(SocketData.DataType.GAME_DATA, objectsToSend);

                if (player.getPlayerCircles().size() == 0) {
                    writeObject(new SocketData(SocketData.DataType.LOOSE, ""));
                    disconnect();
                    return;
                }

                CopyOnWriteArrayList<PowerTypeResource> powerTypeResources = serverEngine.getPowerTypeResources();
                ArrayList<PowerTypeResource> newTypes = new ArrayList<>();

                for (PowerTypeResource ptr : powerTypeResources) {
                    if (!sentPowerTypes.contains(ptr))
                        newTypes.add(ptr);
                }

                sentPowerTypes.addAll(newTypes);

                writeObject(new SocketData(SocketData.DataType.NEW_CLASSES, newTypes), true);

                writeObject(gameData, true);


                dataObject = readObject();

                if (dataObject == null) {
                    disconnect();
                    return;
                }

                int[] mousePositions = (int[]) dataObject.getData();

                serverEngine.setMouseLocation(mousePositions[0], mousePositions[1], player);

                if (mousePositions[2] > 0) {
                    player.activePower(mousePositions[2]);
                }

                try {
                    sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        disconnect();

    }

    public void disconnect() {
        isConnected = false;

        try {
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        discard();
    }

    public void discard() {
        ServerController.getCurrentInstance().removeClientSocket(this);
    }

    public SocketData readObject() {
        int loopCounter = 0;

        while (true) {
            try {
                SocketData socketData = (SocketData) objectInputStream.readObject();
                return socketData;
            } catch (Exception e) {
                loopCounter++;
                e.printStackTrace();
                if (loopCounter > 30) {
                    return null;
                }
            }
        }
    }

    public boolean writeObject(SocketData o) {
        return writeObject(o, false);
    }

    public boolean writeObject(SocketData o, boolean reset) {
        try {
            if (reset)
                objectOutputStream.reset();
            objectOutputStream.writeObject(o);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public void updateClient() {
        doUpdate = true;
    }
}
