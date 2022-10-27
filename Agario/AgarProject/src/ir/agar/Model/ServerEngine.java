package ir.agar.Model;

import ir.agar.Controller.AgarClassLoader;
import ir.agar.Controller.PowerTypeResource;
import ir.agar.Game;
import ir.agar.Model.Objects.*;
import ir.agar.Model.Objects.PowerTypes.*;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class ServerEngine extends Thread implements GameEngine {
    public static final int PLAYER_CIRCLE_RADIUS = 30;
    public static final int PLAYER_CIRCLE_MIN_RADIUS = 20;
    public static final int PLAYER_CIRCLE_CENTER_ALIGN = 80;
    public static final int SPEED = 3;
    public static final int MAX_GEARS = 4;
    public static final int GEAR_MAX_RADIUS = 60;
    public static final int GEAR_MIN_RADIUS = 25;

    private AgarClassLoader agarClassLoader = new AgarClassLoader();

    private CopyOnWriteArrayList<Class> powerTypes = new CopyOnWriteArrayList<>(new Class[]{
            Destructor.class, GodMode.class, InstantCombine.class, Splitter.class
    });

    private CopyOnWriteArrayList<PowerTypeResource> powerTypeResources = new CopyOnWriteArrayList<>();

    public static final Random random = new Random();

    private boolean isUpdated = true;

    private int waitUntilNextGear = (int) (random.nextInt() % (1000 / Game.GAME_SLEEP));

    public int remainingEnergyCircles;

    private int mouseX = 0, mouseY = 0;

    private static ServerEngine currentInstance;
    private Timer createEnergyCirclesTimer;

    private Object lockObject = new Object();

    private Map<String, Player> players = new ConcurrentHashMap<>();

    private Map<String, String> passwords = new ConcurrentHashMap<>();

    private Map<String, Player> registeredPlayers = new ConcurrentHashMap<>();
    private List<PlayerCircle> playerCircles = new CopyOnWriteArrayList();
    private List<PowerCircle> powerCircles = new CopyOnWriteArrayList();
    private List<GearCircle> gearCircles = new CopyOnWriteArrayList();
    private List<EnergyCircle> energyCircles = new CopyOnWriteArrayList();

    public ServerEngine(int energyCircleCount) {

        this.currentInstance = this;
        createEnergyCirclesTimer();
        this.remainingEnergyCircles = energyCircleCount;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (this) {
                update();
            }
            isUpdated = true;

            try {
                sleep(Game.GAME_SLEEP * 3 / 4 + 1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public Player registerPlayer(String username, String password, String name, Color color) {
        if (registeredPlayers.containsKey(username))
            return null;

        Player player = new Player(username, name, color, null);

        passwords.put(username, password);

        registeredPlayers.put(username, player);

        return player;
    }

    public void addPlayerToGame(String username) {
        Player player = getPlayer(username);
        player.reset();

        players.put(player.getUsername(), player);
    }

    public boolean addPlayerToGame(String username, String password) {
        if (!checkLogin(username, password))
            return false;


        Player player = registeredPlayers.get(username);
        player.reset();

        players.put(player.getUsername(), player);

        return true;
    }

    private void createEnergyCirclesTimer() {
        createEnergyCirclesTimer = new Timer();
        createEnergyCirclesTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                ServerEngine.getCurrentInstance().createEnergyCircles();
            }
        }, Game.RANDOM_POINTS_CREATION_PREIOD, Game.RANDOM_POINTS_CREATION_PREIOD);
    }

    private synchronized void createEnergyCircles() {
        if (remainingEnergyCircles < 1)
            return;

        for (int i = 0; i < Game.RANDOM_POINTS_PER_PERIOD && remainingEnergyCircles > 0; i++) {

            int x = (int) (Math.random() * Game.width);
            int y = (int) (Math.random() * Game.height);
            int color = (int) (Math.random() * 0xffffff);

            EnergyCircle energyCircle = new EnergyCircle(x, y, new Color(color));

            energyCircles.add(energyCircle);

            remainingEnergyCircles--;
        }
    }

    public ServerEngine() {
        this(Game.RANDOM_POINTS_COUNT);
    }

    @Override
    public List<PowerCircle> getPowerCircles() {
        return powerCircles;
    }

    @Override
    public List<PlayerCircle> getPlayerCircles() {
        return playerCircles;
    }

    public static ServerEngine getCurrentInstance() {
        return currentInstance;
    }


    public void setMouseLocation(int x, int y, Player player) {
        List<PlayerCircle> playerCircles = player.getPlayerCircles();
        if (playerCircles.size() == 0)
            return;


        long centerX = 0, centerY = 0;
        for (int i = 0; i < playerCircles.size(); i++) {
            centerX += playerCircles.get(i).getX();
            centerY += playerCircles.get(i).getY();
        }

        centerX /= playerCircles.size();
        centerY /= playerCircles.size();

        int vx = (int) (x - centerX);
        int vy = (int) (y - centerY);

        if (Math.abs(vx) + Math.abs(vy) < 5)
            player.setDirection(Direction.STATIC);
        else
            player.setDirection(Direction.getDirectionByVector(vx, vy));

    }

    public void update() {
        updateGears();
        updateCirclesPosition();
        updatePowers();
        splitCirclesByGear();
        checkPlayersCircles();
        checkEnergyCircles();
        checkPowerCircles();
    }

    private void checkPowerCircles() {
        ArrayList<PowerCircle> powerCirclesToDelete = new ArrayList<>();
        for (PlayerCircle playerCircle : playerCircles) {
            for (PowerCircle powerCircle : powerCircles) {
                if (playerCircle.contains(powerCircle)) {
                    powerCirclesToDelete.add(powerCircle);
                    playerCircle.getPlayer().addPowerCircle(powerCircle);
                    break;
                }
            }
        }
        powerCircles.removeAll(powerCirclesToDelete);

        Collection<Player> ps = players.values();

        for (Player player : ps) {
            PowerType activePower = player.getActivePower();
            if (activePower != null) {
                if (!activePower.isAlive()) {
                    activePower.destruct();
                    player.getPowers().remove(activePower);
                    player.setActivePower(null);
                } else {
                    activePower.cycle();
                }
            }
        }

    }

    private void updatePowers() {
        ArrayList<PowerCircle> powerCirclesToDelete = new ArrayList<>();
        if (Math.random() <= 0.01 && powerCircles.size() < 3) {
            Class powerType = powerTypes.get((int) (Math.random() * powerTypes.size()));
//            Class powerType = powerTypes.get((int) (powerTypes.size() - 1));

            int x = (int) (Math.random() * Game.width);
            int y = (int) (Math.random() * Game.height);

            PowerType pt = null;
            try {
                pt = (PowerType) powerType.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
                return;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return;
            }

            PowerCircle powerCircle = new PowerCircle(x, y, pt);

            powerCircles.add(powerCircle);

        }

        for (PowerCircle powerCircle : powerCircles) {
            if (!powerCircle.isAlive())
                powerCirclesToDelete.add(powerCircle);
            powerCircle.update();
        }


        powerCircles.removeAll(powerCirclesToDelete);
    }

    private void splitCirclesByGear() {
        ArrayList<PlayerCircle> newPlayerCircles = new ArrayList<>();

        for (GearCircle gear : gearCircles) {
            PlayerCircles:
            for (PlayerCircle playerCircle : playerCircles) {
                if (playerCircle.contains(gear)) {
                    playerCircle.getPlayer().splitByGear();
                    break PlayerCircles;
                }
            }
        }

    }

    private void updateGears() {
        if (gearCircles.size() < MAX_GEARS && (waitUntilNextGear--) < 1) {
            waitUntilNextGear = 50 + random.nextInt() % 500;
            createGearCircle();
        }

        ArrayList<GearCircle> destroyedGearCircles = new ArrayList<>();


        for (GearCircle gearCircle : this.gearCircles) {
            gearCircle.updateAnimation();

            if (gearCircle.isDestroyed()) {
                destroyedGearCircles.add(gearCircle);
            }
        }


        gearCircles.removeAll(destroyedGearCircles);
    }

    private void createGearCircle() {
        int x = (int) (Math.random() * Game.width);
        int y = (int) (Math.random() * Game.height);
        double radius = GEAR_MIN_RADIUS + Math.random() * (GEAR_MAX_RADIUS - GEAR_MIN_RADIUS);

        for (GearCircle gearCircle : gearCircles) {
            if (gearCircle.contains(x, y, radius)) {
                createGearCircle();
                return;
            }
        }


        int color = 0x666666;


        GearCircle gearCircle = new GearCircle(x, y, new Color(color), radius);

        gearCircles.add(gearCircle);
    }

    private void checkEnergyCircles() {
        for (int i = 0; i < playerCircles.size(); i++) {
            PlayerCircle playerCircle = playerCircles.get(i);
            for (int j = 0; j < energyCircles.size(); j++) {
                EnergyCircle energyCircle = energyCircles.get(j);
                if (playerCircle.getArea() > energyCircle.getArea() && playerCircle.contains(energyCircle)) {
                    playerCircle.addCircle(energyCircle);
                    energyCircles.remove(energyCircle);
                    j--;
                }
            }
        }


    }


    private void checkPlayersCircles() {
        for (PlayerCircle biggerCircle : playerCircles) {
            if (biggerCircle.isDisabled())
                continue;

            for (PlayerCircle smallerCircle : playerCircles) {
                if (smallerCircle.isDisabled() || biggerCircle.getRadius() < smallerCircle.getRadius() || smallerCircle.getPlayer().isGodMode())
                    continue;

                if (biggerCircle.contains(smallerCircle)) {
                    biggerCircle.addCircle(smallerCircle);
                    smallerCircle.setDisabled(true);
                }

            }

            for (int i = 0; i < playerCircles.size(); i++) {
                if (playerCircles.get(i).isDisabled())
                    playerCircles.remove(i--);
            }
        }
    }

    private void updateCirclesPosition() {
        for (PlayerCircle playerCircle : playerCircles) {
            Direction direction = playerCircle.getPlayer().getDirection();
            double x = playerCircle.getX();
            double y = playerCircle.getY();
            playerCircle.setX(direction.moveX(x, SPEED * playerCircle.getPlayer().getSpeedRatio()));
            playerCircle.setY(direction.moveY(y, SPEED * playerCircle.getPlayer().getSpeedRatio()));
        }

    }

    public void splitPlayer(int playerNumber) {
        players.get(playerNumber).splitByPlayer();
    }


    public Player getPlayer(String username) {
        return players.getOrDefault(username, null);
    }

    @Override
    public synchronized boolean isUpdated() {
        if (isUpdated) {
            this.isUpdated = false;
            return true;
        }
        return false;
    }

    @Override
    public Map<String, Player> getPlayers() {
        return players;
    }

    @Override
    public List<GearCircle> getGearCircles() {
        return gearCircles;
    }

    @Override
    public List<EnergyCircle> getEnergyCircles() {
        return energyCircles;
    }

    public boolean checkLogin(String username, String password) {
        if (passwords.containsKey(username) &&
                passwords.get(username).equals(password))
            return true;
        return false;
    }

    public boolean addPowerType(String className, byte[] bin) {
        Class aClass = agarClassLoader.loadDynamicClass(className, bin);
        if (aClass == null)
            return false;

        powerTypes.add(aClass);
        PowerTypeResource powerTypeResource = new PowerTypeResource(className, bin);

        powerTypeResources.add(powerTypeResource);

        return true;
    }

    public CopyOnWriteArrayList<Class> getPowerTypes() {
        return powerTypes;
    }

    public CopyOnWriteArrayList<PowerTypeResource> getPowerTypeResources() {
        return powerTypeResources;
    }
}
