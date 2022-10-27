package ir.spnaz.agar.Model;

public enum Direction {
    TOP_LEFT(-1, -1),
    TOP_RIGHT(1, -1),
    TOP(0, -1),
    LEFT(-1, 0),
    RIGHT(1, 0),
    BOTTOM_LEFT(-1, 1),
    BOTTOM_RIGHT(1, 1),
    BOTTOM(0, 1),
    STATIC(0, 0);

    private double x, y;

    private double angle;

    Direction() {
    }

    Direction(double x, double y) {
        this.angle = Math.atan2(x, y);

        double size = Math.sqrt(x*x + y*y);
        if (size > 1.01){
            x /= size;
            y /= size;
        }



        this.x = x;
        this.y = y;
    }

    public double moveX(double prevX, double ratio){
        return (prevX + x * ratio);
    }
    public double moveY(double prevY, double ratio){
        return (prevY + y * ratio);
    }

    public double getAngle() {
        return angle;
    }

    public static Direction getDirectionByVector(int x, int y){
        Direction[] directions = values();

        double thisAngle = Math.atan2(x, y);

        Direction selected = null;
        for (int i = 0; i < directions.length; i++) {
            if (directions[i] == Direction.STATIC)
                continue;

            if (selected == null || (Math.abs(selected.getAngle()-thisAngle) > Math.abs(directions[i].getAngle()-thisAngle))){
                selected = directions[i];
            }
        }
        return selected;
    }
}
