package ir.spnaz.agar.Model.Objects;

import ir.spnaz.agar.Model.Engine;

public class GearCircle extends Circle {

    public static final double ANGLE_STEP = 3;
    public double alphaStep = 1;

    private double angle = Math.random() * 360;
    private double alpha = 0;

    private int life = (int) (250 + Math.random() * 1000);

    private boolean destroyed = false;

    public GearCircle() {
    }

    public GearCircle(int x, int y, int color, double radius) {
        super(x, y, color, radius);
    }

    public void updateAnimation() {
        this.angle += ANGLE_STEP * (((double) (getRadius() - (Engine.GEAR_MAX_RADIUS + Engine.GEAR_MIN_RADIUS) / 2)) / (Engine.GEAR_MAX_RADIUS - Engine.GEAR_MIN_RADIUS) * -0.8 + 1);
        this.angle %= 360;

        if (alphaStep > 0 && alpha < 180) {
            alpha += alphaStep;
        }

        if (alphaStep < 0 && alpha > 0) {
            alpha += alphaStep;
            if (alpha <= 0) {
                alpha = 0;
                this.destroyed = true;
            }
        }

        life--;
        if (life == 0)
            destroy();
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    public void destroy() {
        alphaStep = -1;
    }
}
