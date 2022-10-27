package ir.agar.Model.Objects;

import ir.agar.Game;
import ir.agar.Model.ServerEngine;
import ir.agar.Model.Direction;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Player implements Serializable {
    private static final int USER_MAX_POWERS = 5;
    private Color color;
    private int id;
    private String name;
    private String picture;
    private String username;

    private Direction direction = Direction.STATIC;

    private double speedRatio = 1;
    private boolean godMode = false;


    ArrayList<PowerType> powers = new ArrayList<>();

    private PowerType activePower = null;

    public Player(String username, String name, Color color, String picture) {
        this.username = username;
        this.name = name;
        this.color = color;
        this.picture = picture;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public List<PlayerCircle> getPlayerCircles() {
        List<PlayerCircle> playerCircles = ServerEngine.getCurrentInstance().getPlayerCircles();

        List<PlayerCircle> thisPlayerCircles = new ArrayList<>();

        for (int i = 0; i < playerCircles.size(); i++) {
            PlayerCircle playerCircle = playerCircles.get(i);
            if (playerCircle.getUsername().equals(username)) {
                thisPlayerCircles.add(playerCircle);
            }
        }
        return thisPlayerCircles;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean getAuthority(Authority authority) {
        if (activePower == null)
            return true;

        return activePower.getAuthority(authority);
    }

    public void activePower(int number) {
        if (activePower == null && number <= powers.size() && number > 0) {
            activePower = powers.get(number - 1);
            activePower.construct();
        }
    }

    public void split() {
        List<PlayerCircle> oldCircles = getPlayerCircles();
        List<PlayerCircle> newCircles = new ArrayList<>();

        if (oldCircles.size() > 1)
            return;

        for (int i = 0; i < oldCircles.size(); i++) {
            PlayerCircle playerCircle = oldCircles.get(i);
            double newCircleRadius = playerCircle.getRadius() * Math.sqrt(2) / 2.0;
            if (newCircleRadius > ServerEngine.PLAYER_CIRCLE_MIN_RADIUS) {
                newCircles.add(new PlayerCircle((playerCircle.getBottomRightX() - newCircleRadius - 1), playerCircle.getY(), newCircleRadius, this));
                newCircles.add(new PlayerCircle((playerCircle.getBottomRightX() + newCircleRadius), playerCircle.getY(), newCircleRadius, this));
            } else {
                newCircles.add(playerCircle);
            }
        }

        List<PlayerCircle> playerCircles = ServerEngine.getCurrentInstance().getPlayerCircles();
        playerCircles.removeAll(oldCircles);
        playerCircles.addAll(newCircles);

        Timer timer = new Timer();

        final Player player = this;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                player.combine();
            }
        }, 5000);
    }

    public void combine() {
        if (!getAuthority(Authority.COMBINE))
            return;

        List<PlayerCircle> oldCircles = getPlayerCircles();
        List<PlayerCircle> newCircles = new ArrayList<>();
        List<PlayerCircle> playerCircles = ServerEngine.getCurrentInstance().getPlayerCircles();

        double newRadius = 0;
        long centerX = 0, centerY = 0;


        for (PlayerCircle c : oldCircles) {
            newRadius += c.getRadius() * c.getRadius();
            centerX += c.getX();
            centerY += c.getY();
        }

        newRadius = Math.sqrt(newRadius);

        centerX /= oldCircles.size();
        centerY /= oldCircles.size();

        newCircles.add(new PlayerCircle(centerX, centerY, newRadius, this));

        playerCircles.removeAll(oldCircles);
        playerCircles.addAll(newCircles);
    }

    public void addPowerCircle(PowerCircle powerCircle) {
        PowerType powerType = powerCircle.getPowerType();

        if (powers.size() >= USER_MAX_POWERS && !powerType.isAtomic())
            return;

        powerType.setPlayer(this);
        if (powerType.isAtomic())
            powerType.construct();
        else
            powers.add(powerType);
    }


    public double getSpeedRatio() {
        return speedRatio;
    }

    public void setSpeedRatio(double speedRatio) {
        this.speedRatio = speedRatio;
    }

    public boolean isGodMode() {
        return godMode;
    }

    public void setGodMode(boolean godMode) {
        this.godMode = godMode;
    }

    public void destructPowers() {
        powers.clear();
    }

    public PowerType getActivePower() {
        return activePower;
    }

    public void reset() {
        List<PlayerCircle> playerCircles = ServerEngine.getCurrentInstance().getPlayerCircles();
        powers.clear();
        playerCircles.removeAll(getPlayerCircles());

        int x = ((int) (Math.random() * (Game.width - ServerEngine.PLAYER_CIRCLE_MIN_RADIUS * 2) + ServerEngine.PLAYER_CIRCLE_MIN_RADIUS));
        int y = ((int) (Math.random() * (Game.height - ServerEngine.PLAYER_CIRCLE_MIN_RADIUS * 2) + ServerEngine.PLAYER_CIRCLE_MIN_RADIUS));

        PlayerCircle playerCircle = new PlayerCircle(x, y, color, ServerEngine.PLAYER_CIRCLE_MIN_RADIUS);
        playerCircle.setPlayer(this);
        playerCircles.add(playerCircle);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<PowerType> getPowers() {
        return powers;
    }

    public void setPowers(ArrayList<PowerType> powers) {
        this.powers = powers;
    }

    public void splitByPlayer() {
        if (!getAuthority(Authority.SPLIT_USER))
            return;

        split();
    }

    public void splitByGear() {
        if (!getAuthority(Authority.SPLIT_GEAR))
            return;

        split();
    }

    public void setActivePower(PowerType activePower) {
        this.activePower = activePower;
    }
}
