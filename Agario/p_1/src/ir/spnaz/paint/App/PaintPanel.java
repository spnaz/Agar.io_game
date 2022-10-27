package ir.spnaz.paint.App;

import ir.spnaz.paint.Shapes.Group;
import ir.spnaz.paint.Shapes.Shape;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Comparator;

import javax.swing.JPanel;

public class PaintPanel extends JPanel {

    private static PaintPanel currentInstance;

    private ArrayList<Shape> shapes = new ArrayList<>();
    private ArrayList<Shape> selectedShapes = new ArrayList();
    private ArrayList<Shape> clipboard = new ArrayList();


    private Point previousMouseLocation = new Point(250, 250);
    private Shape currentShape = null;

    public PaintPanel() {
        this.currentInstance = this;
    }

    public static PaintPanel getCurrentInstance() {
        return currentInstance;
    }

    public void paintComponent(Graphics G) {
        super.paintComponent(G);
        Render(G);
    }

    public void addShapeInLocation(Shape shape) {
        if (previousMouseLocation != null) {
            shape.setPosition(previousMouseLocation.x, previousMouseLocation.y);
        } else {
            previousMouseLocation = new Point(250, 250);
        }
        shapes.add(shape);
    }

    public void addShapeInLocation(Shape shape, String groupName) {
        Group group = null;

        for (Shape shapesItem : shapes) {
            if (shapesItem instanceof Group && shapesItem.getName().toLowerCase().trim().equals(groupName.toLowerCase().trim())) {
                group = ((Group) shapesItem);
                break;
            }
        }

        if (group == null && groupName != null && groupName.length() > 0){
            group = new Group(groupName);
            shapes.add(group);
        }

        shape.setPosition(previousMouseLocation.x, previousMouseLocation.y);

        if (group == null)
            shapes.add(shape);
        else
            group.addShape(shape);
    }

    public void addShape(Shape shape) {
        shapes.add(shape);
    }

    public Shape getClickedShape(int x, int y) {

        for (int i = shapes.size() - 1; i >= 0; i--) {
            if (shapes.get(i).contains(x, y))
                return shapes.get(i);
        }
        return null;
    }

    public void clearShapes() {
        shapes.clear();
        selectedShapes.clear();
    }

    public void saveSheet(File file) {
        ObjectOutputStream outputStream;
        try {
            outputStream = new ObjectOutputStream(new FileOutputStream(file));
            outputStream.writeObject(shapes);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadSheet(File file) {
        ObjectInputStream inputStream;
        try {
            inputStream = new ObjectInputStream(new FileInputStream(file));
            shapes = (ArrayList<Shape>) inputStream.readObject();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void Render(Graphics G) {

        Graphics2D g2d = (Graphics2D) G;
        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setRenderingHints(rh);

        shapes.sort(new Comparator<Shape>() {
            @Override
            public int compare(Shape t0, Shape t1) {
                return t0.getPriority() - t1.getPriority();
            }
        });

        for (Shape shape : shapes) {
            shape.render(g2d);
        }
    }

    public ArrayList<Shape> getSelectedShapes() {
        return selectedShapes;
    }

    public void setSelectedShapes(ArrayList<Shape> selectedShapes) {
        this.selectedShapes = selectedShapes;
    }

    public Point getPreviousMouseLocation() {
        return previousMouseLocation;
    }

    public void setPreviousMouseLocation(Point previousMouseLocation) {
        this.previousMouseLocation = previousMouseLocation;
    }

    public Shape getCurrentShape() {
        return currentShape;
    }

    public void setCurrentShape(Shape currentShape) {
        this.currentShape = currentShape;
    }

    public void clearSelectedShapes() {
        for (Shape shape: selectedShapes)
            shape.setSelected(false);

        selectedShapes.clear();
    }

    public ArrayList<Shape> getShapes() {
        return shapes;
    }

    public ArrayList<Shape> getClipboard() {
        return clipboard;
    }
}
