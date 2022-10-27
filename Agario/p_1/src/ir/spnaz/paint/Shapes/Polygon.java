package ir.spnaz.paint.Shapes;

import java.awt.*;

public class Polygon extends Shape {
    public Polygon(int n, String name, double l) {
        super(name);

        double radius = Math.sqrt(Math.pow(l, 2) / (2 * (1 - Math.cos(Math.PI * 2 / n))));

        int[] x = new int[n];
        int[] y = new int[n];
        for (int i = 0; i < n; i++) {
            x[i] = (int) (radius * Math.cos(2 * Math.PI * i / n));
            y[i] = (int) (radius * Math.sin(2 * Math.PI * i / n));
        }

        setShape(new java.awt.Polygon(x, y, n));
    }


    public Polygon(int n, String name, int x, int y, int l, Color borderColor, Color fillColor) {
        this(n, name, l);
        setPosition(x, y);
        setBorderColor(borderColor);
        setFillColor(fillColor);
    }
}
