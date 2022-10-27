package ir.spnaz.agar.Model.Objects;

public class PlayerCircle extends Circle {
    private Player player;

    public PlayerCircle() {
    }

    public PlayerCircle(Player player) {
        this.player = player;
    }

    public PlayerCircle(int x, int y, int color, double radius) {
        super(x, y, color, radius);
    }

    public PlayerCircle(int x, int y, double radius, Player player) {
        super(x, y, player.getColor(), radius);
        this.player = player;
    }

    public PlayerCircle(double x, double y, double radius, Player player) {
        super((int) x, (int) y, player.getColor(), radius);
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
