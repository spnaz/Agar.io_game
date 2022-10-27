package ir.agar.Controller;

import ir.agar.Game;
import ir.agar.Game;
import ir.agar.Model.ClientEngine;
import ir.agar.Model.Objects.PlayerCircle;
import ir.agar.Model.SocketData;
import ir.agar.Utils.IpConverter;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ClientController extends Thread {
    private static ArrayList<String[]> foundServers = new ArrayList<>();
    private static ClientController currentInstance = null;
    private AgarClassLoader agarClassLoader = new AgarClassLoader();

    private String ip = null;
    private String name;
    private int port;
    private int width;
    private int height;
    private int clients;
    private String username;
    private String password;

    private boolean isConnected = false;
    private boolean isLoggedIn = false;


    private ObjectOutputStream objectOutputStream;
    private ClassLoaderObjectInputStream objectInputStream;

    private List<SocketData> dataToWrite = Collections.synchronizedList(new ArrayList<>());

    private Socket socket = null;


    private int mouseX;
    private int mouseY;

    private boolean haveToUpdate = false;

    public ClientController(String ip, String name, int port, int width, int height) {
        this.ip = ip;
        this.port = port;
        this.width = width;
        this.height = height;
        this.name = name;
        currentInstance = this;
    }

    public Socket connectServer() {
        if (ip == null)
            return null;

        if (socket != null && socket.isConnected())
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(ip, port), 500);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        try {
            objectInputStream = new ClassLoaderObjectInputStream(socket.getInputStream());
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

            objectOutputStream.writeObject(new SocketData(SocketData.DataType.CONNECTION_MODE, false));

            isConnected = true;
            return socket;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Socket connectServerTemp() {
        if (ip == null)
            return null;

        if (socket != null && socket.isConnected())
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(ip, port), 500);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        try {
            objectInputStream = new ClassLoaderObjectInputStream(socket.getInputStream());
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

            objectOutputStream.writeObject(new SocketData(SocketData.DataType.CONNECTION_MODE, true));

            isConnected = true;
            return socket;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void run() {

        int noData = 0;


        try {
            socket.setSoTimeout(100);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        login();


        while (true) {
            try {
                SocketData serverData = (SocketData) objectInputStream.readObject();

                if (serverData.getType() == SocketData.DataType.NEW_CLASSES) {
                    ArrayList<PowerTypeResource> ptr = (ArrayList<PowerTypeResource>) serverData.getData();
                    if (ptr.size() > 0) {
                        for (PowerTypeResource powerTypeResource : ptr) {
                            objectInputStream.loadClass(powerTypeResource.getName(), powerTypeResource.getBin());
                        }
                    }

                }


                serverData = (SocketData) objectInputStream.readObject();

                if (serverData.getType() == SocketData.DataType.LOOSE) {
                    Painter.setLoose(true);
                    return;
                }

                SocketData directionUpdate;
                synchronized (this) {
                    directionUpdate = new SocketData(SocketData.DataType.MOUSE_CORDS, new int[]{mouseX, mouseY, Game.getNumberPressed()});
                }
                objectOutputStream.writeObject(directionUpdate);
                if (serverData.getType() == SocketData.DataType.GAME_DATA) {
                    Object[] serverDataArray = (Object[]) serverData.getData();
                    ClientEngine.getCurrentInstance().updateData(serverDataArray);
                }
            } catch (SocketTimeoutException e) {
                e.printStackTrace();
                // ignore
                noData++;

                if (noData > 50) {
                    disconnect();
                    return;
                }
                continue;
            } catch (SocketException e) {
                e.printStackTrace();
                // ignore
                noData++;

                if (noData > 50) {
                    disconnect();
                    return;
                }
                continue;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            noData = 0;

            try {
                sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void disconnect() {
        isConnected = false;
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void findServers(String startingIp, String endIp, int port, Runnable doneCommand) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                long start = IpConverter.ipToLong(startingIp);
                long end = IpConverter.ipToLong(endIp);
                if (end < start)
                    return;

                foundServers.clear();

                for (long longIp = start; longIp <= end; longIp++) {
                    String[] serverData = getServerData(IpConverter.longToIp(longIp), port);
                    if (serverData != null && serverData.length == 5)
                        foundServers.add(new String[]{IpConverter.longToIp(longIp), serverData[0], serverData[1], serverData[2], serverData[3], serverData[4]});
                }

                doneCommand.run();
            }
        }).start();
    }

    public static String[] getServerData(String ip, int port) {
        Socket socket = null;

        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(ip, port), 1000);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        try {
            socket.setSoTimeout(500);
        } catch (SocketException e) {
            e.printStackTrace();
            return null;
        }


        ObjectOutputStream objectOutputStream;
        ObjectInputStream objectInputStream;

        try {

            objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

            SocketData socketData = new SocketData(SocketData.DataType.CONNECTION_MODE, true);
            objectOutputStream.writeObject(socketData);

            socketData = new SocketData(SocketData.DataType.SERVER_DATA, true);
            objectOutputStream.writeObject(socketData);

            SocketData serverData = ((SocketData) objectInputStream.readObject());
            if (serverData.getType() == SocketData.DataType.SERVER_DATA_RESPONSE) {
                return (String[]) serverData.getData();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static ArrayList<String[]> getFoundServers() {
        return foundServers;
    }

    public static void selectServer(String[] serverData) {
        ClientController clientController = new ClientController(serverData[0], serverData[1], Integer.parseInt(serverData[2]), Integer.parseInt(serverData[3]), Integer.parseInt(serverData[4]));
        clientController.setClients(Integer.parseInt(serverData[5]));
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getServerName() {
        return name;
    }

    public int getClients() {
        return clients;
    }

    public void setClients(int clients) {
        this.clients = clients;
    }

    public static ClientController getCurrentInstance() {
        return currentInstance;
    }

    public boolean login() {
        String[] loginDataStr = {username, password};

        SocketData loginData = new SocketData(SocketData.DataType.LOGIN, loginDataStr);
        System.out.println(Arrays.deepToString(((String[]) loginData.getData())));

        try {
            objectOutputStream.writeObject(loginData);
            SocketData loginResult = (SocketData) objectInputStream.readObject();
            if (loginResult.getType() != SocketData.DataType.LOGIN_RESPONSE || !((boolean) loginResult.getData())) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean register(String username, String password, String name, Color circlesColor, byte[] circlesImage) {
        Object[] registerData = {username, password, name, circlesColor, circlesImage};

        this.username = username;
        this.password = password;

        SocketData socketData = new SocketData(SocketData.DataType.REGISTER, registerData);
        try {
            objectOutputStream.writeObject(socketData);
            SocketData registerResult = (SocketData) objectInputStream.readObject();

            if (registerResult.getType() == SocketData.DataType.REGISTER_RESPONSE) {
                Boolean registerResultInt = (Boolean) registerResult.getData();
                if (registerResultInt) {
                    return true;
                } else {
                    JOptionPane.showMessageDialog(Game.getGameFrame(),
                            "Username already exists.",
                            "Input error",
                            JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public void setMouseLocation(int x, int y) {
        synchronized (this) {
            this.mouseX = x;
            this.mouseY = y;
        }
    }

    public boolean checkLogin(String user, String pass) {
        connectServerTemp();

        String[] loginDataStr = {user, pass};

        SocketData loginData = new SocketData(SocketData.DataType.LOGIN, loginDataStr);

        try {
            objectOutputStream.writeObject(loginData);
            SocketData loginResult = (SocketData) objectInputStream.readObject();
            disconnect();
            if (loginResult.getType() != SocketData.DataType.LOGIN_RESPONSE || !((boolean) loginResult.getData())) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            disconnect();
            return false;
        }

        return true;
    }

    public void setUserAndPass(String user, String pass) {
        this.username = user;
        this.password = pass;
    }

    public String getUsername() {
        return username;
    }
}
