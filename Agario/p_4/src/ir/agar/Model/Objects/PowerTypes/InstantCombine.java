package ir.agar.Model.Objects.PowerTypes;

import ir.agar.Model.Objects.Authority;
import ir.agar.Model.Objects.Player;
import ir.agar.Model.Objects.PowerType;

import java.awt.*;


public class InstantCombine implements PowerType {

    private long life = -1;
    private boolean active;
    private Player player;


    @Override
    public Color getColor() {
        return Color.decode("0xFF0059");
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
        this.active = true;
        this.life = System.currentTimeMillis();
        player.combine();
    }

    @Override
    public void destruct() {
        this.active = false;
    }

    @Override
    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public Player getPlayer() {
        return player;
    }
}
