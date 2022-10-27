package ir.spnaz.paint.Shapes;

import java.awt.*;

public class Rectangle extends Shape {
    public Rectangle(String name, int x, int y, int width, int height, Color borderColor, Color fillColor) {
        super(name, new java.awt.Rectangle(x, y, width, height), fillColor, borderColor);
    }
    public Rectangle(String name, int x, int y, int width, int height) {
        super(name, new java.awt.Rectangle(x, y, width, height));
    }

    public Rectangle(String name, int width, int height) {
        this(name, 0, 0, width, height);
    }
}
