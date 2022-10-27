package ir.spnaz.paint.Shapes;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Comparator;



public class Group extends Shape {
    private ArrayList<Shape> containingShapes = new ArrayList<>();

    public Group(String name) {
        super(name);
    }

    @Override
    public Rectangle2D getBounds() {
        double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE, maxX = Double.MIN_VALUE, maxY = Double.MIN_VALUE;

        for (Shape shape : containingShapes) {
            Rectangle2D shapeBounds = shape.getBounds();
            if (shapeBounds.getMinX() < minX)
                minX = shapeBounds.getMinX();
            if (shapeBounds.getMinY() < minY)
                minY = shapeBounds.getMinY();
            if (shapeBounds.getMaxX() > maxX)
                maxX = shapeBounds.getMaxX();
            if (shapeBounds.getMaxY() > maxY)
                maxY = shapeBounds.getMaxY();
        }

        return new Rectangle2D.Double(minX, minY, maxX - minX, maxY - minY);

    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public void setName(String name) {
        super.setName(name);
    }


    @Override
    public boolean contains(int x, int y) {
        for (Shape shape : containingShapes) {
            if (shape.contains(x, y))
                return true;
        }
        return false;
    }

    @Override
    public void render(Graphics2D g) {
        for (Shape shape : containingShapes) {
            shape.render(g);
        }

        if (isSelected()) {
            Rectangle2D bounds = getBounds();
            g.setColor(Color.GRAY);
            g.drawRect(((int) bounds.getX()) - 2, ((int) bounds.getY()) - 2, ((int) bounds.getWidth()) + 4, ((int) bounds.getHeight()) + 4);
        }
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        for (Shape shape : containingShapes) {
            shape.setSelected(selected);
        }
    }

    @Override
    public void scale(double s) {
        scale(s, s);
    }

    @Override
    public void scale(double sx, double sy) {
        Point center = getCenter();
        AffineTransform translateToZero = AffineTransform.getTranslateInstance(-center.x, -center.y);
        AffineTransform scale = AffineTransform.getScaleInstance(sx, sy);
        AffineTransform translateToPreviusPosition = AffineTransform.getTranslateInstance(center.x, center.y);

        for (Shape shape : containingShapes) {
            java.awt.Shape awtShape = shape.getShape();
            awtShape = translateToZero.createTransformedShape(awtShape);
            awtShape = scale.createTransformedShape(awtShape);
            awtShape = translateToPreviusPosition.createTransformedShape(awtShape);
            shape.setShape(awtShape);
        }
    }

    @Override
    public void rotate(double r) {
        Point center = getCenter();
        AffineTransform translateToZero = AffineTransform.getTranslateInstance(-center.x, -center.y);
        AffineTransform scale = AffineTransform.getRotateInstance(Math.toRadians(r));
        AffineTransform translateToPreviusPosition = AffineTransform.getTranslateInstance(center.x, center.y);

        for (Shape shape : containingShapes) {
            java.awt.Shape awtShape = shape.getShape();
            awtShape = translateToZero.createTransformedShape(awtShape);
            awtShape = scale.createTransformedShape(awtShape);
            awtShape = translateToPreviusPosition.createTransformedShape(awtShape);
            shape.setShape(awtShape);
        }
    }

    @Override
    public void translate(double x, double y) {
        for (Shape shape : containingShapes) {
            java.awt.Shape awtShape = shape.getShape();
            awtShape = AffineTransform.getTranslateInstance(x, y).createTransformedShape(awtShape);
            shape.setShape(awtShape);
        }
    }

    public ArrayList<Shape> getContainingShapes() {
        return containingShapes;
    }

    public void setContainingShapes(ArrayList<Shape> containingShapes) {
        this.containingShapes = containingShapes;
    }

    @Override
    public Shape getCopy() {
        Group groupCopy = (Group) super.getCopy();
        ArrayList<Shape> containingShapes = groupCopy.getContainingShapes();
        ArrayList<Shape> containingShapesDuplicate = new ArrayList<>();
        for (Shape shape : containingShapes) {
            containingShapesDuplicate.add(shape.getCopy());
        }

        return ((Shape) groupCopy);
    }

    public void sort() {
        containingShapes.sort(new Comparator<Shape>() {
            @Override
            public int compare(Shape t0, Shape t1) {
                return t0.getPriority() - t1.getPriority();
            }
        });
    }

    public void addShape(Shape shape) {
        containingShapes.add(shape);
        shape.setSelected(isSelected());
        shape.setGrouped(true);
        sort();
    }

    public void removeShape(Shape shape) {
        shape.setSelected(isSelected());
        shape.setGrouped(false);
        containingShapes.remove(shape);
    }

    @Override
    public void setFillColor(Color fillColor) {
        if (containingShapes == null)
            containingShapes = new ArrayList<>();

        for (Shape shape : containingShapes) {
            shape.setFillColor(fillColor);
        }
    }

    @Override
    public void setBorderColor(Color borderColor) {
        if (containingShapes == null)
            containingShapes = new ArrayList<>();

        for (Shape shape : containingShapes) {
            shape.setBorderColor(borderColor);
        }
    }

    @Override
    public Color getBorderColor() {
        if (containingShapes.size() > 0)
            return containingShapes.get(containingShapes.size() - 1).getBorderColor();

        return null;
    }

    @Override
    public Color getFillColor() {
        if (containingShapes.size() > 0)
            return containingShapes.get(containingShapes.size() - 1).getFillColor();

        return null;
    }

    @Override
    public void setBorderSize(int borderSize) {
        for (Shape shape : containingShapes) {
            shape.setBorderSize(borderSize);
        }
    }

    @Override
    public int getBorderSize() {
        if (containingShapes.size() > 0)
            return containingShapes.get(containingShapes.size() - 1).getBorderSize();

        return 0;
    }

    public Shape removeShape(Point previousMouseLocation) {
        for (int i = 0; i < containingShapes.size(); i++) {
            Shape shape = containingShapes.get(i);
            if (shape.contains(((int) previousMouseLocation.getX()), ((int) previousMouseLocation.getY()))) {
                containingShapes.remove(shape);
                return shape;
            }
        }
        return null;
    }
}
