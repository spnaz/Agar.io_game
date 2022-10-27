package ir.spnaz.paint.Shapes;

import java.awt.Color;
import java.awt.geom.Line2D;

public class Line extends Shape {
    public Line(String name) {
        super(name, new Line2D.Double(50, 50, 350, 350));
    }

    public Line(String name, int X_1, int Y_1, int X_2, int Y_2) {
        super(name, new Line2D.Double(X_1, Y_1, X_2, Y_2));
    }

    public Line(String name, int X_1, int Y_1, int X_2, int Y_2, Color borderColor) {
        super(name, new Line2D.Double(X_1, Y_1, X_2, Y_2), borderColor);
    }

}
