package ir.spnaz.agar.Model.Objects;

import ir.spnaz.agar.Game;
import ir.spnaz.agar.Model.Direction;
import ir.spnaz.agar.Model.Engine;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

public class Player {
    private int color;
    private int id;
    private String name;
    private String picture;
    private Direction direction = Direction.STATIC;

    private double speedRatio = 1;
    private boolean godMode = false;



    ArrayList<PlayerCircle> playerCircles = new ArrayList<>();
    ArrayList<PowerType> powers = new ArrayList<>();

    public Player(int color, String name, String picture) {
        this.color = color;
        this.name = name;
        this.picture = picture;
    }


    public int getColor() {
        return color;
    }

    public void setColor(int color) {
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

    public ArrayList<PlayerCircle> getPlayerCircles() {
        CopyOnWriteArrayList<PlayerCircle> playerCircles = Engine.getCurrentInstance().getPlayerCircles();

        ArrayList<PlayerCircle> thisPlayerCircles = new ArrayList<>();

        for (int i = 0; i < playerCircles.size(); i++) {
            PlayerCircle playerCircle = playerCircles.get(i);
            if (playerCircle.getPlayer().equals(this)) {
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

    public void split() {
        ArrayList<PlayerCircle> oldCircles = getPlayerCircles();
        ArrayList<PlayerCircle> newCircles = new ArrayList<>();

        if(oldCircles.size() > 1)
            return;

        for (int i = 0; i < oldCircles.size(); i++) {
            PlayerCircle playerCircle = oldCircles.get(i);
            double newCircleRadius = playerCircle.getRadius() * Math.sqrt(2) / 2.0;
            if (newCircleRadius > Engine.PLAYER_CIRCLE_MIN_RADIUS) {
                newCircles.add(new PlayerCircle ((playerCircle.getBottomRightX() - newCircleRadius-1), playerCircle.getY(), newCircleRadius, this));
                newCircles.add(new PlayerCircle((playerCircle.getBottomRightX() + newCircleRadius), playerCircle.getY(), newCircleRadius, this));
            } else {
                newCircles.add(playerCircle);
            }
        }

        CopyOnWriteArrayList<PlayerCircle> playerCircles = Engine.getCurrentInstance().getPlayerCircles();
        playerCircles.removeAll(oldCircles);
        playerCircles.addAll(newCircles);

        Timer timer = new Timer();

        final Player player = this;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                player.implode();
            }
        }, 5000);
    }

    public void implode(){
        ArrayList<PlayerCircle> oldCircles = getPlayerCircles();
        ArrayList<PlayerCircle> newCircles = new ArrayList<>();

        double newRadius = 0;
        long centerX = 0, centerY = 0;


        for(PlayerCircle c: oldCircles){
            newRadius += c.getRadius() * c.getRadius();
            centerX += c.getX();
            centerY += c.getY();
        }

        newRadius = Math.sqrt(newRadius);

        centerX /= oldCircles.size();
        centerY /= oldCircles.size();

        newCircles.add(new PlayerCircle(centerX, centerY, newRadius, this));

        CopyOnWriteArrayList<PlayerCircle> playerCircles = Engine.getCurrentInstance().getPlayerCircles();
        playerCircles.removeAll(oldCircles);
        playerCircles.addAll(newCircles);
    }

    public void addPowerCircle(PowerCircle powerCircle) {
        PowerType powerType = powerCircle.getPowerType();
        if (powerType == PowerType.DESTRUCTOR){
            destructPowers();
            return;
        }

        if (powerType == PowerType.MAKE_HALF){
            split();
            return;
        }

        if(powerType == PowerType.SPEED_UP){
            speedUp();
            return;
        }

        if(powerType == PowerType.GODMODE){
            makeGodMode();
        }

        powers.add(powerType);
    }

    private void makeGodMode(){
        setGodMode(true);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                setGodMode(false);
            }
        }, Game.POWER_TIME);
    }

    private void speedUp(){
        speedRatio *= 2;

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                speedRatio /= 2;
            }
        }, Game.POWER_TIME);
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

    public void setGodMode(boolean godMode){
        this.godMode = godMode;
    }

    void destructPowers(){
        powers.clear();
    }


}
