package ir.spnaz.agar.Model;

import ir.spnaz.agar.Game;
import ir.spnaz.agar.Model.Objects.*;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

public class Engine {

    public static final int PLAYER_CIRCLE_RADIUS = 50;
    public static final int PLAYER_CIRCLE_MIN_RADIUS = 35;
    public static final int PLAYER_CIRCLE_CENTER_ALIGN = 80;
    public static final int SPEED = 3;
    public static final int MAX_GEARS = 4;
    public static final int GEAR_MAX_RADIUS = 80;
    public static final int GEAR_MIN_RADIUS = 45;

    public static final Random random = new Random();

    private int waitUntilNextGear = (int) (random.nextInt() % (1000 / Game.GAME_SLEEP));

    public int remainingEnergyCircles;

    private int mouseX = 0, mouseY = 0;

    private static Engine currentInstance;
    private Timer createEnergyCirclesTimer;


    private Object lockObject = new Object();

    private CopyOnWriteArrayList<Player> players = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<PlayerCircle> playerCircles = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<PowerCircle> powerCircles = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<GearCircle> gearCircles = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<EnergyCircle> energyCircles = new CopyOnWriteArrayList<>();

    public Engine(int energyCircleCount) {

        this.currentInstance = this;

        Player player1 = new Player(0x000000, "Team 1", null);
        Player player2 = new Player(0xff0000, "Team 2", null);

        players.add(player1);
        players.add(player2);


        int[] centerOfScreen = new int[2];

        centerOfScreen[0] = Game.width / 2;
        centerOfScreen[1] = Game.height / 2;


        PlayerCircle player1Circle = new PlayerCircle(centerOfScreen[0] - PLAYER_CIRCLE_CENTER_ALIGN, centerOfScreen[1], PLAYER_CIRCLE_RADIUS, player1);
        PlayerCircle player2Circle = new PlayerCircle(centerOfScreen[0] + PLAYER_CIRCLE_CENTER_ALIGN, centerOfScreen[1], PLAYER_CIRCLE_RADIUS, player2);

        playerCircles.add(player1Circle);
        playerCircles.add(player2Circle);

        this.remainingEnergyCircles = energyCircleCount;

        createEnergyCirclesTimer();
    }

    private void createEnergyCirclesTimer() {
        createEnergyCirclesTimer = new Timer();
        createEnergyCirclesTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Engine.getCurrentInstance().createEnergyCircles();
            }
        }, Game.RANDOM_POINTS_CREATION_PREIOD, Game.RANDOM_POINTS_CREATION_PREIOD);
    }

    private void createEnergyCircles() {
        if (remainingEnergyCircles < 1)
            return;

        for (int i = 0; i < Game.RANDOM_POINTS_PER_PERIOD && remainingEnergyCircles > 0; i++) {

            int x = (int) (Math.random() * Game.width);
            int y = (int) (Math.random() * Game.height);
            int color = (int) (Math.random() * 0xffffff);

            EnergyCircle energyCircle = new EnergyCircle(x, y, color);

            energyCircles.add(energyCircle);

            remainingEnergyCircles--;
        }
    }

    public Engine() {
        this(Game.RANDOM_POINTS_COUNT);
    }



    public static Engine getCurrentInstance() {
        return currentInstance;
    }



    public void keyPressed(Direction direction) {
        Player player1 = getPlayers().get(1);
        player1.setDirection(direction);
    }

    public void keyReleased(Direction direction) {
        Player player1 = getPlayers().get(1);
        if (player1.getDirection().equals(direction))
            player1.setDirection(Direction.STATIC);
    }

    public void update() {
        updateMousePosition();
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

        for(PlayerCircle playerCircle: playerCircles){
            for (PowerCircle powerCircle: powerCircles){
                if (playerCircle.contains(powerCircle)){
                    powerCirclesToDelete.add(powerCircle);
                    playerCircle.getPlayer().addPowerCircle(powerCircle);
                    break;
                }
            }
        }

        powerCircles.removeAll(powerCirclesToDelete);
    }

    private void updatePowers() {
        ArrayList<PowerCircle> powerCirclesToDelete = new ArrayList<>();
        if(Math.random() <= 0.01 && powerCircles.size() < 3){
            PowerType[] powerTypes = PowerType.values();
            PowerType powerType = powerTypes[(int) (Math.random()*powerTypes.length)];

            int x = (int) (Math.random() * Game.width);
            int y = (int) (Math.random() * Game.height);

            PowerCircle powerCircle = new PowerCircle(x, y, powerType);
            powerCircles.add(powerCircle);
        }

        for (PowerCircle powerCircle: powerCircles){
            if (!powerCircle.isAlive())
                powerCirclesToDelete.add(powerCircle);
            powerCircle.update();
        }

        powerCircles.removeAll(powerCirclesToDelete);
    }

    private void splitCirclesByGear() {
        ArrayList<PlayerCircle> newPlayerCircles = new ArrayList<>();
        for(GearCircle gear: gearCircles){
            PlayerCircles:
            for(PlayerCircle playerCircle: playerCircles){
                if(playerCircle.contains(gear)){
                    playerCircle.getPlayer().split();
                    break PlayerCircles;
                }
            }
        }
    }

    private void updateGears() {
        if (gearCircles.size() < MAX_GEARS && (waitUntilNextGear--) < 1){
            waitUntilNextGear = 50 + random.nextInt() % 500;
            createGearCircle();
        }

        ArrayList<GearCircle> destroyedGearCircles = new ArrayList<>();

        for (GearCircle gearCircle: this.gearCircles){
            gearCircle.updateAnimation();

            if (gearCircle.isDestroyed()){
                destroyedGearCircles.add(gearCircle);
            }
        }

        gearCircles.removeAll(destroyedGearCircles);
    }

    private void createGearCircle() {
        int x = (int) (Math.random() * Game.width);
        int y = (int) (Math.random() * Game.height);
        double radius = GEAR_MIN_RADIUS + Math.random()*(GEAR_MAX_RADIUS-GEAR_MIN_RADIUS);

        for (GearCircle gearCircle: gearCircles){
            if (gearCircle.contains(x, y, radius)){
                createGearCircle();
                return;
            }
        }

        int color = 0x666666;


        GearCircle gearCircle = new GearCircle(x, y, color, radius);

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
        ArrayList<PlayerCircle> player1Circles = players.get(0).getPlayerCircles();
        ArrayList<PlayerCircle> player2Circles = players.get(1).getPlayerCircles();

        // Very complicated loops :D

        Player1Loop:
        for (int i = 0; i < player1Circles.size(); i++) {
            PlayerCircle player1Circle = player1Circles.get(i);
            for (int j = 0; j < player2Circles.size(); j++) {
                PlayerCircle player2Circle = player2Circles.get(j);


                if (player1Circle.getArea() < player2Circle.getArea()) {
                    if (player2Circle.contains(player1Circle) && !player1Circle.getPlayer().isGodMode()) {
                        player2Circle.addCircle(player1Circle);
                        playerCircles.remove(player1Circle);
                        player1Circles.remove(player1Circle);
                        i--;
                        continue Player1Loop;
                    }
                } else {
                    if (player1Circle.contains(player2Circle) && !player2Circle.getPlayer().isGodMode()) {
                        player1Circle.addCircle(player2Circle);
                        playerCircles.remove(player2Circle);
                        player2Circles.remove(player2Circle);
                        j--;
                    }
                }
            }
        }
    }

    private void updateCirclesPosition() {
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            Direction playerDirection = player.getDirection();
            ArrayList<PlayerCircle> playerCircles = player.getPlayerCircles();
            for (int j = 0; j < playerCircles.size(); j++) {
                PlayerCircle playerCircle = playerCircles.get(j);
                double x = playerCircle.getX();
                double y = playerCircle.getY();
                playerCircle.setX(playerDirection.moveX(x, SPEED * playerCircle.getPlayer().getSpeedRatio()));
                playerCircle.setY(playerDirection.moveY(y, SPEED * playerCircle.getPlayer().getSpeedRatio()));
            }
        }
    }

    private void updateMousePosition() {
        Player player1 = players.get(0);
        ArrayList<PlayerCircle> playerCircles = player1.getPlayerCircles();

        if (playerCircles.size() == 0)
            return;

        long centerX = 0, centerY = 0;
        for (int i = 0; i < playerCircles.size(); i++) {
            centerX += playerCircles.get(i).getX();
            centerY += playerCircles.get(i).getY();
        }

        centerX /= playerCircles.size();
        centerY /= playerCircles.size();

        int vx = (int) (mouseX - centerX);
        int vy = (int) (mouseY - centerY);

        if (Math.abs(vx) + Math.abs(vy) < 5)
            player1.setDirection(Direction.STATIC);
        else
            player1.setDirection(Direction.getDirectionByVector(vx, vy));
    }

    public void setMouseLocation(int x, int y) {
        this.mouseX = x;
        this.mouseY = y;
    }

    public void splitPlayer(int playerNumber) {
        players.get(playerNumber).split();
    }

    public CopyOnWriteArrayList<GearCircle> getGearCircles() {
        return gearCircles;
    }

    public CopyOnWriteArrayList<EnergyCircle> getEnergyCircles() {
        return energyCircles;
    }

    public CopyOnWriteArrayList<Player> getPlayers() {
        return players;
    }

    public CopyOnWriteArrayList<PlayerCircle> getPlayerCircles() {
        return playerCircles;
    }

    public CopyOnWriteArrayList<PowerCircle> getPowerCircles() {
        return powerCircles;
    }
}
