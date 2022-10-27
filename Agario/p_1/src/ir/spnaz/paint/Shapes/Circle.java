package ir.spnaz.paint.Shapes;

import java.awt.*;
import java.awt.geom.Ellipse2D;


public class Circle extends Shape {

    public Circle(String name, int x, int y, double radius) {
        super(name, new Ellipse2D.Double(x - radius, y - radius, 2 * radius, 2 * radius));
    }

    public Circle(String name, int x, int y, double radius, Color borderColor, Color fillColor){
        this(name, x, y, radius);
        setBorderColor(borderColor);
        setFillColor(fillColor);
    }

    public Circle(String name, String group, double radius) {
        this(name, 0, 0, radius);
    }

    public Circle(String name, double radius) {
        this(name, 0, 0, radius);
    }
}
