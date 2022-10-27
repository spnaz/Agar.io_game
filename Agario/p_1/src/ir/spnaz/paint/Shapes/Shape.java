package ir.spnaz.paint.Shapes;

import com.sun.imageio.spi.OutputStreamImageOutputStreamSpi;
import ir.spnaz.paint.App.PaintFrame;

import java.awt.*;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public abstract class Shape implements Serializable, Cloneable {
    private java.awt.Shape shape;
    private Color fillColor, borderColor = PaintFrame.getCurrentInstance().getCurrentBorderColor();
    private String name;
    private String group;
    private int priority;
    private StrokeWrapper stroke;
    private boolean selected = false;
    private boolean grouped = false;
    private int borderSize = 1;


    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }


    public Shape(String name) {
        this.name = name;
        this.stroke = new StrokeWrapper(PaintFrame.getCurrentInstance().getStrokeStyle());
        setFillColor(PaintFrame.getCurrentInstance().getCurrentFillColor());
    }

    public Shape(String name, String group) {
        this.name = name;
        this.group = group;
        this.stroke = new StrokeWrapper(PaintFrame.getCurrentInstance().getStrokeStyle());
        setFillColor(PaintFrame.getCurrentInstance().getCurrentFillColor());
    }

    public Shape(String name, String group, Color fillColor, Color borderColor) {
        this.name = name;
        this.group = group;
        this.fillColor = fillColor;
        this.borderColor = borderColor;
        this.stroke = new StrokeWrapper(PaintFrame.getCurrentInstance().getStrokeStyle());
        setFillColor(PaintFrame.getCurrentInstance().getCurrentFillColor());
    }

    public Shape(String name, Color fillColor, Color borderColor) {
        this.name = name;
        this.fillColor = fillColor;
        this.borderColor = borderColor;
        this.stroke = new StrokeWrapper(PaintFrame.getCurrentInstance().getStrokeStyle());
        setFillColor(PaintFrame.getCurrentInstance().getCurrentFillColor());
    }

    public Shape(String name, java.awt.Shape shape) {
        this.name = name;
        this.shape = shape;
        this.stroke = new StrokeWrapper(PaintFrame.getCurrentInstance().getStrokeStyle());
        setFillColor(PaintFrame.getCurrentInstance().getCurrentFillColor());
    }

    public Shape(String name, String group, java.awt.Shape shape) {
        this.name = name;
        this.group = group;
        this.shape = shape;
        this.stroke = new StrokeWrapper(PaintFrame.getCurrentInstance().getStrokeStyle());
        setFillColor(PaintFrame.getCurrentInstance().getCurrentFillColor());
    }

    public Shape(String name, java.awt.Shape shape, Color borderColor) {
        this.name = name;
        this.shape = shape;
        this.borderColor = borderColor;
        this.stroke = new StrokeWrapper(PaintFrame.getCurrentInstance().getStrokeStyle());
        setFillColor(PaintFrame.getCurrentInstance().getCurrentFillColor());
    }

    public Shape(String name, java.awt.Shape shape, Color fillColor, Color borderColor) {
        this.name = name;
        this.shape = shape;
        this.fillColor = fillColor;
        this.borderColor = borderColor;
        this.stroke = new StrokeWrapper(PaintFrame.getCurrentInstance().getStrokeStyle());
        setFillColor(PaintFrame.getCurrentInstance().getCurrentFillColor());
    }

    public Shape(String name, String group, java.awt.Shape shape, Color fillColor, Color borderColor) {
        this.name = name;
        this.group = group;
        this.shape = shape;
        this.fillColor = fillColor;
        this.borderColor = borderColor;
        this.stroke = new StrokeWrapper(PaintFrame.getCurrentInstance().getStrokeStyle());
        setFillColor(PaintFrame.getCurrentInstance().getCurrentFillColor());
    }

    public java.awt.Shape getShape() {
        return shape;
    }

    public Point getCenter() {
        Point center = new Point(((int) getBounds().getCenterX()), ((int) getBounds().getCenterY()));
        return center;
    }

    public Rectangle2D getBounds() {
        return shape.getBounds2D();
    }

    public void setShape(java.awt.Shape shape) {
        this.shape = shape;
    }

    public Color getFillColor() {
        return fillColor;
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean contains(int x, int y) {
        return shape.contains(x, y);
    }

    public void render(Graphics2D g) {


        g.setColor(fillColor);
        g.fill(shape);

        if (stroke != null && stroke.getStroke() != null)
            g.setStroke(stroke.getStroke());

        g.setColor(borderColor);
        g.draw(shape);

        g.setStroke(new BasicStroke(1));

        if (selected) {
            Rectangle2D bounds = getBounds();
            int boxSize = ((int) Math.max(getHeight(), getWidth()));
            g.setColor(Color.black);
            for (int i = 0; i < boxSize; i += 5) {
                for (int j = 0; j < boxSize; j += 5) {
                    if (contains(((int) (bounds.getX() + i)), ((int) (bounds.getY() + j))))
                        g.fillOval(((int) (bounds.getX() + i)), ((int) (bounds.getY() + j)), 1, 1);
                }
            }

            if (!grouped) {
                g.setColor(new Color(255, 255, 255, 150));
                g.fillOval(((int) (bounds.getMaxX() - 10)), ((int) (bounds.getMaxY() - 10)), 20, 20);
                g.setColor(Color.BLACK);
                g.drawString(getPriority() + "", ((int) (bounds.getMaxX() - 10)) + 6, ((int) (bounds.getMaxY() - 10)) + 14);
            }
        }
    }

    public void scale(double s) {
        Point center = getCenter();
        AffineTransform translateToZero = AffineTransform.getTranslateInstance(-center.x, -center.y);
        AffineTransform scale = AffineTransform.getScaleInstance(s, s);
        AffineTransform translateToPreviusPosition = AffineTransform.getTranslateInstance(center.x, center.y);
        shape = translateToZero.createTransformedShape(shape);
        shape = scale.createTransformedShape(shape);
        shape = translateToPreviusPosition.createTransformedShape(shape);
    }

    public void scale(double sx, double sy) {
        Point center = getCenter();
        AffineTransform translateToZero = AffineTransform.getTranslateInstance(-center.x, -center.y);
        AffineTransform scale = AffineTransform.getScaleInstance(sx, sy);
        AffineTransform translateToPreviusPosition = AffineTransform.getTranslateInstance(center.x, center.y);
        shape = translateToZero.createTransformedShape(shape);
        shape = scale.createTransformedShape(shape);
        shape = translateToPreviusPosition.createTransformedShape(shape);
    }

    public void rotate(double r) {
        Point center = getCenter();
        AffineTransform translateToZero = AffineTransform.getTranslateInstance(-center.x, -center.y);
        AffineTransform rotate = AffineTransform.getRotateInstance(Math.toRadians(r));
        AffineTransform translateToPreviusPosition = AffineTransform.getTranslateInstance(center.x, center.y);
        shape = translateToZero.createTransformedShape(shape);
        shape = rotate.createTransformedShape(shape);
        shape = translateToPreviusPosition.createTransformedShape(shape);
    }

    public double getWidth() {
        return getBounds().getWidth();
    }

    public double getHeight() {
        return getBounds().getHeight();
    }

    public void translate(double x, double y) {
        shape = AffineTransform.getTranslateInstance(x, y).createTransformedShape(shape);
    }

    public void setPosition(int x, int y) {
        Point center = getCenter();
        double tx = x - center.getX();
        double ty = y - center.getY();
        translate(tx, ty);
    }

    public Shape getCopy() {
        try {
            return (Shape) this.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getBorderSize() {
        return borderSize;
    }

    public void setBorderSize(int borderSize) {
        this.borderSize = borderSize;
    }

    public boolean isGrouped() {
        return grouped;
    }

    public void setGrouped(boolean grouped) {
        this.grouped = grouped;
    }

    public void setSize(int w, int h) {
        double scaleXRatio = ((double) w) / getWidth();
        double scaleYRatio = ((double) h) / getHeight();

        scale(scaleXRatio, scaleYRatio);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        stroke = new StrokeWrapper();
        in.defaultReadObject();
    }
}
