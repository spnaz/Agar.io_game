package ir.spnaz.agar.Model.Objects;

public class EnergyCircle extends Circle {

    public static final int DEFAULT_RADIUS = 10;
    protected double addRatio = 1.5;

    public EnergyCircle() {
    }

    public EnergyCircle(int x, int y, int color, double radius) {
        super(x, y, color, radius);
    }

    public EnergyCircle(int x, int y, int color) {
        super(x, y, color, DEFAULT_RADIUS);
    }

    @Override
    public double getAddRatio() {
        return addRatio;
    }
}
