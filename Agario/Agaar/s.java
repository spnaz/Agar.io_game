import ir.agar.Model.Objects.Authority;
import ir.agar.Model.Objects.Player;
import ir.agar.Model.Objects.PowerType;

import java.awt.*;

/**
 * Created by bh on 03/08/2016
 */
public class SPower implements PowerType {
    private long t = 0;
    private boolean actived = false;
    private Player player;

    @Override
    public Color getColor() {
        return Color.PINK;
    }

    @Override
    public boolean getAuthority(Authority authority) {
        if (authority == Authority.SPLIT_GEAR)
            return false;
        else
            return true;
    }

    @Override
    public boolean isAtomic() {
        return false;
    }

    @Override
    public boolean isAlive() {
        if (System.currentTimeMillis() < t + 5000)
            return true;
        else if (!actived)
            return true;
        else
            return false;
    }

    @Override
    public boolean isActive() {
        return actived;
    }

    @Override
    public void cycle() {

    }

    @Override
    public void construct() {
        actived = true;
        t = System.currentTimeMillis();

    }

    @Override
    public void destruct() {

    }

    @Override
    public void setPlayer(Player player) {
       this.player=player;

    }

    @Override
    public Player getPlayer() {
        return player;
    }
}
