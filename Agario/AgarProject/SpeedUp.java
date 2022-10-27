package ir.agar.Model.Objects.PowerTypes;

import ir.agar.Model.Objects.Authority;
import ir.agar.Model.Objects.Player;
import ir.agar.Model.Objects.PowerType;

import java.awt.*;

/**
 * Created by payam on 7/27/16.
 */
public class SpeedUp implements PowerType {

    private long life = -1;
    private boolean active;
    private Player player;

    @Override
    public Color getColor() {
        return Color.decode("0x0061FF");
    }

    @Override
    public boolean getAuthority(Authority authority) {
        return true;
    }

    @Override
    public boolean isAtomic() {
        return false;
    }

    @Override
    public boolean isAlive() {
        return life == -1 || life >= System.currentTimeMillis();
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void cycle() {

    }

    @Override
    public void construct() {
        active = true;
        life = System.currentTimeMillis() + 5000;
        player.setSpeedRatio(player.getSpeedRatio() * 1.5);
    }

    @Override
    public void destruct() {

        active = false;
        player.setSpeedRatio(player.getSpeedRatio() / 1.5);
    }

    @Override
    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }
}
