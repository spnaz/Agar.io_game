package ir.agar.Model.Objects.PowerTypes;

import ir.agar.Model.Objects.Authority;
import ir.agar.Model.Objects.Player;
import ir.agar.Model.Objects.PowerType;
import java.awt.Color;

public class SpeedUp implements PowerType {
    private long life = -1L;
    private boolean active;
    private Player player;

    public SpeedUp() {
    }

    public Color getColor() {
        return Color.RED;
    }

    public boolean getAuthority(Authority authority) {
        return true;
    }

    public boolean isAtomic() {
        return false;
    }

    public boolean isAlive() {
        return this.life == -1L || this.life >= System.currentTimeMillis();
    }

    public boolean isActive() {
        return this.active;
    }

    public void cycle() {
    }

    public void construct() {
        this.active = true;
        this.life = System.currentTimeMillis() + 5000L;
        this.player.setSpeedRatio(this.player.getSpeedRatio() * 1.5D);
    }

    public void destruct() {
        this.active = false;
        this.player.setSpeedRatio(this.player.getSpeedRatio() / 1.5D);
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return this.player;
    }
}
