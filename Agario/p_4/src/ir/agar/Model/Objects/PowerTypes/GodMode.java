package ir.agar.Model.Objects.PowerTypes;

import ir.agar.Model.Objects.Authority;
import ir.agar.Model.Objects.Player;
import ir.agar.Model.Objects.PowerType;

import java.awt.*;


public class GodMode implements PowerType {

    private long life = -1;
    private boolean active = false;


    @Override
    public Color getColor() {
        return Color.decode("0x5D116E");
    }

    @Override
    public boolean getAuthority(Authority authority) {
        if (authority.equals(Authority.EATEN))
            return false;

        return true;
    }

    @Override
    public boolean isAtomic() {
        return false;
    }

    @Override
    public void cycle() {

    }

    @Override
    public void construct() {
        life = System.currentTimeMillis() + 5000;
        active = true;
    }

    @Override
    public void destruct() {
        active = false;
    }

    @Override
    public void setPlayer(Player player) {

    }

    @Override
    public Player getPlayer() {
        return null;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public boolean isAlive() {
        return life == -1 || life >= System.currentTimeMillis();
    }
}
