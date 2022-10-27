package ir.spnaz.agar.Model.Objects;

public class PowerCircle extends Circle {
    private PowerType powerType;
    private int life = 300;

    public PowerCircle(PowerType powerType) {
        this.powerType = powerType;
    }

    public PowerCircle(int x, int y, int color, double radius, PowerType powerType) {
        super(x, y, color, radius);
        this.powerType = powerType;
    }

    public PowerCircle(int x, int y, double radius, PowerType powerType) {
        super(x, y, powerType.getColor(), radius);
        this.powerType = powerType;
    }


    public PowerCircle(int x, int y, PowerType powerType) {
        this(x, y, 10, powerType);
    }

    public PowerType getPowerType() {
        return powerType;
    }

    public void setPowerType(PowerType powerType) {
        this.powerType = powerType;
    }

    public boolean isAlive(){
        return life > 0;
    }

    public void update() {
        life--;
    }
}
