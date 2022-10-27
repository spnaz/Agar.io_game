package ir.spnaz.paint.Shapes;

import java.awt.*;
import java.awt.Polygon;

public class Triangle extends Shape {

    public Triangle(String name) {
        super(name);

        int[] x = {10, 200, 300}, y = {10, 200, 10};
        setShape(new Polygon(x, y, 3));
    }

    public Triangle(String name, Point points[]) {
        super(name);

        int x[] = new int[3], y[] = new int[3];

        for (int i = 0; i < 3; i++) {
            x[i] = points[i].x;
            y[i] = points[i].y;
        }
        setShape(new Polygon(x, y, 3));
    }

    public Triangle(String name, int X_1, int Y_1, int X_2, int Y_2, int X_3, int Y_3) {
        super(name);

        int[] x = {X_1, X_2, X_3}, y = {Y_1, Y_2, Y_3};
        setShape(new Polygon(x, y, 3));
    }

    public Triangle(String name, int X_1, int Y_1, int X_2, int Y_2, int X_3, int Y_3, Color borderColor, Color fillColor) {
        super(name, fillColor, borderColor);
        int[] x = {X_1, X_2, X_3}, y = {Y_1, Y_2, Y_3};
        setShape(new Polygon(x, y, 3));
    }
}
