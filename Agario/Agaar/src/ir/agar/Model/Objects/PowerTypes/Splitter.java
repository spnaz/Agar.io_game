package ir.agar.Model.Objects.PowerTypes;

import ir.agar.Model.Objects.Authority;
import ir.agar.Model.Objects.Player;
import ir.agar.Model.Objects.PowerType;

import java.awt.*;


public class Splitter implements PowerType {

    private Player player;

    @Override
    public Color getColor() {
        return Color.yellow;
    }

    @Override
    public boolean getAuthority(Authority authority) {
        return true;
    }

    @Override
    public boolean isAtomic() {
        return true;
    }

    @Override
    public boolean isAlive() {
        return false;
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void cycle() {

    }

    @Override
    public void construct() {
        player.split();
    }

    @Override
    public void destruct() {

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
