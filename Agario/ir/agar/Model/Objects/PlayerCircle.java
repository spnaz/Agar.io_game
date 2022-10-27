package ir.agar.Model.Objects;

import ir.agar.Game;
import ir.agar.Game;

import java.awt.*;

public class PlayerCircle extends Circle {
    private String player;
    private boolean disabled = false;

    public PlayerCircle() {

    }

    public PlayerCircle(Player player) {
        setPlayer(player);
    }

    public PlayerCircle(int x, int y, Color color, double radius) {
        super(x, y, color, radius);
    }

    public PlayerCircle(int x, int y, double radius, Player player) {
        super(x, y, player.getColor(), radius);
        setPlayer(player);
    }

    public PlayerCircle(double x, double y, double radius, Player player) {
        super((int) x, (int) y, player.getColor(), radius);
        setPlayer(player);
    }

    public Player getPlayer() {
        return Game.getGameEngine().getPlayer(player);
    }

    public void setPlayer(Player player) {
        this.player = player.getUsername();
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public String getUsername(){return this.player;}

    @Override
    public void addCircle(Circle circle) {
        if (!getPlayer().getAuthority(Authority.EAT))
            return;

        if (circle instanceof PlayerCircle && !((PlayerCircle) circle).getPlayer().getAuthority(Authority.EATEN))
            return;

        super.addCircle(circle);
    }
}
