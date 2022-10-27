package ir.spnaz.paint.App;

import ir.spnaz.paint.Shapes.*;
import ir.spnaz.paint.Shapes.Polygon;
import ir.spnaz.paint.Shapes.Rectangle;
import ir.spnaz.paint.Shapes.Shape;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.Exchanger;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.event.MouseInputAdapter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class PaintFrame extends JFrame {

    private final PaintPanel paintPanel;

    private JPanel commandsPanel;
    private Container pane;

    private Color currentFillColor = Color.YELLOW;
    private Color currentBorderColor = Color.BLACK;

    private Stroke strokeStyle = new BasicStroke(1);

    private JButton fillColorButton;
    private JButton borderColorButton;
    private JButton clearSheetButton;
    private JMenuItem group;
    private JMenuItem addToGroup;
    private JMenuItem removeFromGroup;
    private JMenuItem ungroup;
    private JMenuItem resizeButton;

    private static PaintFrame currentPaintFrame;


    private Scanner sc = new Scanner(System.in);

    public PaintFrame() {

        // set current instance
        this.currentPaintFrame = this;

        // set default page size
        this.setSize(new Dimension(700, 500));
        this.setResizable(false);

        pane = getContentPane();
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));

        paintPanel = new PaintPanel();
        paintPanel.setPreferredSize(new Dimension(0, 440));
        paintPanel.setAlignmentY(Box.CENTER_ALIGNMENT);
        paintPanel.setBorder(BorderFactory.createLineBorder(Color.gray));

        JPanel commandsPanel = new JPanel();
        commandsPanel.setPreferredSize(new Dimension(0, 60));
        commandsPanel.setAlignmentY(Box.CENTER_ALIGNMENT);
        commandsPanel.setBorder(BorderFactory.createLineBorder(Color.gray));
        commandsPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        clearSheetButton = new JButton("Clear Sheet");
        clearSheetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                paintPanel.clearShapes();
                paintPanel.repaint();
            }
        });

        fillColorButton = new JButton();
        fillColorButton.setBackground(currentFillColor);
        fillColorButton.setPreferredSize(new Dimension(20, 20));
        fillColorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                currentFillColor = JColorChooser.showDialog(fillColorButton, "Choose Fill Color ...", currentFillColor);
                fillColorButton.setBackground(currentFillColor);

                PaintPanel paintPanel = PaintPanel.getCurrentInstance();
                ArrayList<Shape> selectedShapes = paintPanel.getSelectedShapes();

                for (Shape shape : selectedShapes) {
                    shape.setFillColor(currentFillColor);
                }
                repaint();
            }
        });

        borderColorButton = new JButton();
        borderColorButton.setBackground(currentBorderColor);
        borderColorButton.setPreferredSize(new Dimension(20, 20));
        borderColorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                currentBorderColor = JColorChooser.showDialog(borderColorButton, "Choose Fill Color ...", currentFillColor);
                borderColorButton.setBackground(currentBorderColor);

                PaintPanel paintPanel = PaintPanel.getCurrentInstance();
                ArrayList<Shape> selectedShapes = paintPanel.getSelectedShapes();

                for (Shape shape : selectedShapes) {
                    shape.setBorderColor(currentBorderColor);
                }
                repaint();
            }
        });

        JButton borderStyleButton = new JButton("Select Border Style");

        borderStyleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Object[] possibilities = {"Plane", "Dotted", "Dashed"};
                try {
                    String borderStyle = (String) JOptionPane.showInputDialog(
                            PaintFrame.getCurrentInstance(),
                            "Select border style:",
                            "Border style",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            possibilities,
                            "Plane");

                    if (borderStyle.equals("Plane")) {
                        strokeStyle = new BasicStroke(1);
                    } else if (borderStyle.equals("Dotted")) {
                        final float dash1[] = {3.0f};
                        final BasicStroke dashed =
                                new BasicStroke(2.0f,
                                        BasicStroke.CAP_BUTT,
                                        BasicStroke.JOIN_MITER,
                                        3.0f, dash1, 0.0f);
                        strokeStyle = dashed;
                    } else {
                        final float dash1[] = {10.0f};
                        final BasicStroke dashed =
                                new BasicStroke(1.0f,
                                        BasicStroke.CAP_BUTT,
                                        BasicStroke.JOIN_MITER,
                                        10.0f, dash1, 0.0f);
                        strokeStyle = dashed;
                    }
                } catch (Exception e) {
                    // ignore
                }
            }
        });


        commandsPanel.add(clearSheetButton);
        commandsPanel.add(fillColorButton);
        commandsPanel.add(borderColorButton);
        commandsPanel.add(borderStyleButton);

        pane.add(paintPanel);
        pane.add(commandsPanel);
        paintPanel.addMouseListener(new MouseInputAdapter() {
            @Override
            public void mouseReleased(MouseEvent event) {
            }

            @Override
            public void mousePressed(MouseEvent event) {
                PaintPanel paintPanel = PaintPanel.getCurrentInstance();
                ArrayList<Shape> selectedShapes = paintPanel.getSelectedShapes();


                if (event.getButton() == MouseEvent.BUTTON1 && !event.isShiftDown()) {
                    Shape clickedShape = paintPanel.getClickedShape(event.getX(), event.getY());

                    if (event.isControlDown()) {
                        if (clickedShape != null) {
                            selectedShapes.add(clickedShape);
                            clickedShape.setSelected(true);
                        }
                    } else {
                        paintPanel.clearSelectedShapes();
                        if (clickedShape != null) {
                            selectedShapes.add(clickedShape);
                            clickedShape.setSelected(true);
                        }
                    }

                    if (selectedShapes.size() > 0) {
                        Shape lastSelectedShape = selectedShapes.get(selectedShapes.size() - 1);
                        Color fillColor = lastSelectedShape.getFillColor();
                        fillColorButton.setBackground(fillColor);
                    }

                    paintPanel.setCurrentShape(clickedShape);
                    repaint();
                }

                reloadPopupMenu();

                paintPanel.setPreviousMouseLocation(event.getPoint());

            }

            @Override
            public void mouseExited(MouseEvent event) {

            }

            @Override
            public void mouseEntered(MouseEvent event) {

            }

            @Override
            public void mouseClicked(MouseEvent event) {

            }
        });

        paintPanel.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent event) {
                Point currentLocation = new Point(event.getPoint());
                Point.Double difference = new Point.Double(currentLocation.getX() - paintPanel.getPreviousMouseLocation().getX(), currentLocation.getY() - paintPanel.getPreviousMouseLocation().getY());
                paintPanel.setPreviousMouseLocation(currentLocation);
                ArrayList<Shape> selectedShapes = paintPanel.getSelectedShapes();
                for (Shape shape : selectedShapes) {
                    shape.translate(difference.getX(), difference.getY());
                }
                repaint();
            }

            @Override
            public void mouseMoved(MouseEvent event) {
                PaintPanel paintPanel = PaintPanel.getCurrentInstance();
                paintPanel.setPreviousMouseLocation(event.getPoint());
            }
        });

        addPopupMenus();
    }

    public void addPopupMenus() {
        final JPopupMenu menu = new JPopupMenu();

        //Add ir.spnaz.paint.shapes.Shape item
        JMenuItem addShapeButton = new JMenu("Add Shape");

        JMenuItem addCircleButton = new JMenuItem("Circle");
        addCircleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                String shapeName = JOptionPane.showInputDialog("Circle's name: ");
                String groupName = JOptionPane.showInputDialog("Circle's group name: ");
                String radius = JOptionPane.showInputDialog("Radius: ");

                double circleRadius;

                try {
                    circleRadius = Double.parseDouble(radius);
                } catch (NumberFormatException e) {
                    circleRadius = -1;
                }


                if (circleRadius < 1)
                    circleRadius = 25;

                if (shapeName != null)
                    shapeName = "Circle-" + System.currentTimeMillis();

                paintPanel.addShapeInLocation(new Circle(shapeName, circleRadius), groupName);
                repaint();
            }
        });

        JMenuItem addRectangleButton = new JMenuItem("Rectangle");
        addRectangleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                String shapeName = JOptionPane.showInputDialog("Rectangle's name: ");
                String groupName = JOptionPane.showInputDialog("Rectangle's group name: ");
                String width = JOptionPane.showInputDialog("Width: ");
                String height = JOptionPane.showInputDialog("Height: ");

                int rectangleWidth, rectangleHeight;

                try {
                    rectangleWidth = Integer.parseInt(width);
                    rectangleHeight = Integer.parseInt(height);
                } catch (NumberFormatException e) {
                    rectangleWidth = -1;
                    rectangleHeight = -1;
                }


                if (rectangleWidth < 1)
                    rectangleWidth = 50;

                if (rectangleHeight < 1)
                    rectangleHeight = 50;

                if (shapeName != null)
                    shapeName = "Rectangle-" + System.currentTimeMillis();

                paintPanel.addShapeInLocation(new Rectangle(shapeName, rectangleWidth, rectangleHeight), groupName);
                repaint();
            }
        });

        JMenuItem addLineButton = new JMenuItem("Line");
        addLineButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                String shapeName = JOptionPane.showInputDialog("Rectangle's name: ");
                String groupName = JOptionPane.showInputDialog("Rectangle's group name: ");
                String minX = JOptionPane.showInputDialog("Min X: ");
                String minY = JOptionPane.showInputDialog("Min Y: ");
                String maxX = JOptionPane.showInputDialog("Max X: ");
                String maxY = JOptionPane.showInputDialog("Max Y: ");

                int x0, y0, x1, y1;

                try {
                    x0 = Integer.parseInt(minX);
                    y0 = Integer.parseInt(minY);
                    x1 = Integer.parseInt(maxX);
                    y1 = Integer.parseInt(maxY);
                } catch (NumberFormatException e) {
                    x0 = 50;
                    y0 = 50;
                    x1 = 150;
                    y1 = 150;
                }

                if (shapeName != null)
                    shapeName = "Line-" + System.currentTimeMillis();

                paintPanel.addShapeInLocation(new Line(shapeName, x0, y0, x1, y1), groupName);
                repaint();
            }
        });

        JMenuItem addPolygonButton = new JMenuItem("Polygon");
        addPolygonButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                String shapeName = JOptionPane.showInputDialog("Rectangle's name: ");
                String groupName = JOptionPane.showInputDialog("Rectangle's group name: ");
                String sidesCount = JOptionPane.showInputDialog("Sides count: ");
                String sidesLength = JOptionPane.showInputDialog("Length of sides: ");

                int sidesCountInteger = 0;
                double sidesLengthDouble = 0;

                try {
                    sidesCountInteger = Integer.parseInt(sidesCount);
                    sidesLengthDouble = Double.parseDouble(sidesLength);

                } catch (NumberFormatException e) {
                    sidesCountInteger = 5;
                    sidesLengthDouble = 30;
                }

                if (shapeName != null)
                    shapeName = "Polygon-" + System.currentTimeMillis();

                paintPanel.addShapeInLocation(new Polygon(sidesCountInteger, shapeName, sidesLengthDouble), groupName);
                repaint();
            }


        });


        JMenuItem addTriangleButton = new JMenuItem("Triangle");
        addTriangleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                String shapeName = JOptionPane.showInputDialog("Triangle's name: ");
                String groupName = JOptionPane.showInputDialog("Triangle's group name: ");
                String x0 = JOptionPane.showInputDialog("First point's x: ");
                String x1 = JOptionPane.showInputDialog("Second point's x: ");
                String x2 = JOptionPane.showInputDialog("Third point's x: ");
                String y0 = JOptionPane.showInputDialog("First point's y: ");
                String y1 = JOptionPane.showInputDialog("Second point's y: ");
                String y2 = JOptionPane.showInputDialog("Third point's y: ");

                int[] x = new int[3], y = new int[3];
                try {
                    x[0] = Integer.parseInt(x0);
                    x[1] = Integer.parseInt(x1);
                    x[2] = Integer.parseInt(x2);
                    y[0] = Integer.parseInt(y0);
                    y[1] = Integer.parseInt(y1);
                    y[2] = Integer.parseInt(y2);
                } catch (NumberFormatException ex) {
                    x = new int[]{50, 150, 50};
                    y = new int[]{50, 50, 150};
                }

                if (shapeName != null)
                    shapeName = "Triangle-" + System.currentTimeMillis();

                paintPanel.addShapeInLocation(new Triangle(shapeName, x[0], y[0], x[1], y[1], x[2], y[2]), groupName);
                repaint();
            }
        });

        JMenuItem addPointButton = new JMenuItem("Point");
        addPointButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                String shapeName = JOptionPane.showInputDialog("Circle's name: ");
                String groupName = JOptionPane.showInputDialog("Circle's group name: ");

                if (shapeName != null)
                    shapeName = "Point-" + System.currentTimeMillis();

                paintPanel.addShapeInLocation(new Circle(shapeName, 7), groupName);
                repaint();
            }
        });

        addShapeButton.add(addPointButton);
        addShapeButton.add(addCircleButton);
        addShapeButton.add(addRectangleButton);
        addShapeButton.add(addLineButton);
        addShapeButton.add(addPolygonButton);
        addShapeButton.add(addTriangleButton);

        //SCALE AND ROTATE
        JMenuItem scale = new JMenuItem("Scale");
        scale.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                String scaleRatio = JOptionPane.showInputDialog("Scale ratio : ");
                double ratio = 1;

                try {
                    ratio = Double.parseDouble(scaleRatio);
                } catch (NumberFormatException e) {
                    return;
                }

                ArrayList<Shape> selectedShapes = PaintPanel.getCurrentInstance().getSelectedShapes();
                for (Shape shape : selectedShapes) {
                    shape.scale(ratio);
                }
                paintPanel.repaint();
            }
        });

        JMenuItem rotate = new JMenuItem("Rotate");
        rotate.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                String rotateDegree = JOptionPane.showInputDialog("Rotation degree : ");
                double degree = 1;

                try {
                    degree = Double.parseDouble(rotateDegree);
                } catch (NumberFormatException e) {
                    return;
                }

                ArrayList<Shape> selectedShapes = PaintPanel.getCurrentInstance().getSelectedShapes();
                for (Shape shape : selectedShapes) {
                    shape.rotate(degree);
                }
                paintPanel.repaint();
            }
        });


        //Remove items
        JMenuItem removeitem = new JMenuItem("Clear shapes");
        removeitem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                paintPanel.clearShapes();
                paintPanel.repaint();
            }
        });

        JMenuItem coloritem = new JMenu("Set fill Color");
        JMenuItem coloritemborder = new JMenu("Set border Color");
        Color colors[] = new Color[]{Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.CYAN, Color.BLACK, Color.WHITE};
        String colornames[] = new String[]{"RED", "GREEN", "BLUE", "YELLOW", "CYAN", "BLACK", "WHITE"};


        for (int i = 0; i < colors.length; i++) {
            final ColorMenuItem c_item = new ColorMenuItem(colornames[i], colors[i]);
            c_item.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent arg0) {
                    currentFillColor = c_item.getColor();
                    fillColorButton.setBackground(currentFillColor);

                    ArrayList<Shape> selectedShapes = PaintPanel.getCurrentInstance().getSelectedShapes();

                    for (Shape shape : selectedShapes) {
                        shape.setFillColor(currentFillColor);
                    }
                    repaint();
                }
            });
            coloritem.add(c_item);
            final ColorMenuItem c_item2 = new ColorMenuItem(colornames[i], colors[i]);
            c_item2.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent arg0) {
                    currentFillColor = c_item2.getColor();

                    ArrayList<Shape> selectedShapes = PaintPanel.getCurrentInstance().getSelectedShapes();

                    for (Shape shape : selectedShapes) {
                        shape.setBorderColor(currentFillColor);
                    }
                    repaint();
                }
            });
            coloritemborder.add(c_item2);
        }

        JMenuItem openFile = new JMenuItem("Open");
        openFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                JFileChooser fileChooser = new JFileChooser();
                if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    paintPanel.loadSheet(file);
                    repaint();

                }
            }
        });
        JMenuItem saveFile = new JMenuItem("Save");
        saveFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                JFileChooser fileChooser = new JFileChooser();
                if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    paintPanel.saveSheet(file);

                }
            }
        });

        JMenuItem copy = new JMenuItem("Copy");
        copy.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub

                ArrayList<Shape> selectedShapes = paintPanel.getSelectedShapes();
                ArrayList<Shape> clipboard = paintPanel.getClipboard();

                clipboard.clear();
                for (int i = 0; i < selectedShapes.size(); i++) {
                    Shape shape = selectedShapes.get(i);
                    clipboard.add(shape.getCopy());
                }
                paintPanel.repaint();
            }
        });

        JMenuItem paste = new JMenuItem("Paste");

        paste.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                ArrayList<Shape> selectedShapes = paintPanel.getSelectedShapes();
                ArrayList<Shape> clipboard = paintPanel.getClipboard();

                for (int i = 0; i < clipboard.size(); i++) {
                    Shape shape = clipboard.get(i);
                    paintPanel.addShapeInLocation(shape.getCopy());
                }
                repaint();
            }
        });

        JMenuItem priority = new JMenuItem("Set Priority");


        priority.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {


                ArrayList<Shape> selectedShapes = paintPanel.getSelectedShapes();

                try {
                    int priority = Integer.parseInt(JOptionPane.showInputDialog("Priority for selected items: "));

                    for (Shape shape : selectedShapes) {
                        shape.setPriority(priority);
                    }

                    repaint();
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        });

//

        group = new JMenuItem("Group");

        group.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ArrayList<Shape> selectedShapes = paintPanel.getSelectedShapes();
                ArrayList<Shape> shapes = paintPanel.getShapes();
                String name = JOptionPane.showInputDialog("Group name: ");
                Group group = new Group(name);

                int maxPriority = Integer.MIN_VALUE;

                for (Shape shape : selectedShapes) {
                    if (shape.getPriority() > maxPriority)
                        maxPriority = shape.getPriority();

                    group.addShape(shape);
                }

                shapes.removeAll(selectedShapes);
                paintPanel.clearSelectedShapes();
                shapes.add(group);

                group.setPriority(maxPriority);
                repaint();
            }
        });

        addToGroup = new JMenuItem("Add selected Shapes to group");

        addToGroup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Group group = null;

                String groupName = JOptionPane.showInputDialog("Group name: ");

                ArrayList<Shape> shapes = paintPanel.getShapes();
                ArrayList<Shape> selectedShapes = paintPanel.getSelectedShapes();

                for (Shape shapesItem : shapes) {
                    if (shapesItem instanceof Group && shapesItem.getName().toLowerCase().trim().equals(groupName.toLowerCase().trim())) {
                        group = ((Group) shapesItem);
                        break;
                    }
                }

                if (group == null && groupName != null && groupName.length() > 0) {
                    group = new Group(groupName);
                    shapes.add(group);
                }

                for (Shape shape : selectedShapes) {
                    group.addShape(shape);
                }

                shapes.removeAll(selectedShapes);
                paintPanel.clearSelectedShapes();
                repaint();

            }
        });


        removeFromGroup = new JMenuItem("Remove Shape from group");
        removeFromGroup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Group group = null;

                ArrayList<Shape> selectedShapes = paintPanel.getSelectedShapes();
                for (Shape shape : selectedShapes) {
                    if (shape instanceof Group) {
                        group = ((Group) shape);
                        break;
                    }
                }

                Shape shape = group.removeShape(paintPanel.getPreviousMouseLocation());
                if (shape != null)
                    PaintPanel.getCurrentInstance().addShape(shape);
                repaint();

            }
        });

        ungroup = new JMenuItem("Ungroup");

        ungroup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ArrayList<Shape> selectedShapes = paintPanel.getSelectedShapes();
                for (Shape shape : selectedShapes) {
                    if (shape instanceof Group) {
                        Group group = ((Group) shape);
                        ArrayList<Shape> shapes = paintPanel.getShapes();
                        shapes.remove(group);
                        shapes.addAll(group.getContainingShapes());
                    }
                }
                paintPanel.clearSelectedShapes();
                repaint();
            }
        });


        resizeButton = new JMenuItem("Resize");
        resizeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int width;
                int height;
                try {
                    width = Integer.parseInt(JOptionPane.showInputDialog("Width:"));
                    height = Integer.parseInt(JOptionPane.showInputDialog("Height:"));
                } catch (NumberFormatException e) {
                    return;
                }

                Shape shape = paintPanel.getSelectedShapes().get(0);
                shape.setSize(width, height);
                repaint();
            }
        });


        JMenuItem importText = new JMenuItem("Import");
        importText.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
                fileChooser.setFileFilter(filter);

                int returnVal = fileChooser.showOpenDialog(PaintFrame.getCurrentInstance());
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    try {
                        ArrayList<Shape> shapes = paintPanel.getShapes();
                        ArrayList<Shape> clipboard = paintPanel.getClipboard();
                        Scanner sc = new Scanner(file);
                        paintPanel.clearShapes();
                        paintPanel.repaint();
                        while (sc.hasNextLine()) {
                            String str = sc.nextLine();
                            String commandSplited[] = str.split(" ");
                            switch (commandSplited[0]) {
                                case "add":
                                    String commandParameters[] = str.substring(4, str.length() - 1).split(",");
                                    switch (commandParameters[0]) {
                                        case "Circle":
                                            paintPanel.addShape(new Circle(commandParameters[1], Integer.parseInt(commandParameters[2]), Integer.parseInt(commandParameters[3]), Double.parseDouble(commandParameters[4]), new Color(Integer.parseInt(commandParameters[5], 16)), new Color(Integer.parseInt(commandParameters[6], 16))));

                                            break;
                                        case "Line":
                                            paintPanel.addShape(new Line(commandParameters[1], Integer.parseInt(commandParameters[2]), Integer.parseInt(commandParameters[3]), Integer.parseInt(commandParameters[4]), Integer.parseInt(commandParameters[5]), new Color(Integer.parseInt(commandParameters[6]))));
                                            break;
                                        case "Point":
                                            paintPanel.addShape(new Circle(commandParameters[1], Integer.parseInt(commandParameters[2]), Integer.parseInt(commandParameters[3]), 7, new Color(Integer.parseInt(commandParameters[4], 16)), new Color(Integer.parseInt(commandParameters[4], 16))));
                                            break;
                                        case "Polygon":
                                            paintPanel.addShape(new Polygon(Integer.parseInt(commandSplited[1]), commandSplited[2], Integer.parseInt(commandParameters[3]), Integer.parseInt(commandParameters[4]), Integer.parseInt(commandParameters[5]) + 50, new Color(Integer.parseInt(commandParameters[6], 16)), new Color(Integer.parseInt(commandParameters[7], 16))));
                                            break;
                                        case "Triangle":
                                            paintPanel.addShape(new Triangle(commandSplited[1], Integer.parseInt(commandParameters[2]), Integer.parseInt(commandParameters[3]), Integer.parseInt(commandParameters[4]), Integer.parseInt(commandParameters[5]), Integer.parseInt(commandParameters[6]), Integer.parseInt(commandParameters[7]), new Color(Integer.parseInt(commandParameters[8], 16)), new Color(Integer.parseInt(commandParameters[9], 16))));
                                            break;
                                        case "Rectangle":
                                            paintPanel.addShape(new Rectangle(commandSplited[1], Integer.parseInt(commandParameters[2]), Integer.parseInt(commandParameters[3]), Integer.parseInt(commandParameters[4]), Integer.parseInt(commandParameters[5]), new Color(Integer.parseInt(commandParameters[6], 16)), new Color(Integer.parseInt(commandParameters[7], 16))));
                                            break;
                                    }
                                case "copy":
                                    String copyShape = str.substring(5, str.length() - 1);
                                    for (int i = 0; i < shapes.size(); ++i) {
                                        if (copyShape.equals(shapes.get(i).getName())) {
                                            clipboard.add(shapes.get(i).getCopy());
                                            break;
                                        }
                                    }
                                    break;
                                case "cut":
                                    String cutShape = str.substring(5, str.length() - 1);
                                    for (int i = 0; i < shapes.size(); ++i) {
                                        if (cutShape.equals(shapes.get(i).getName())) {
                                            clipboard.add(shapes.get(i).getCopy());
                                            break;
                                        }
                                    }
                                    break;
                                case "paste":
                                    String sPaste = str.substring(6, str.length() - 1);
                                    String sP[] = sPaste.split(",");
                                    for (Shape shape : clipboard) {
                                        Shape shapeCopy = shape.getCopy();
                                        shapeCopy.setPosition(Integer.parseInt(sP[0]), Integer.parseInt(sP[1]));
                                        shapes.add(shapeCopy);
                                    }
                                    break;
                                case"delete":
                                    String sName = str.substring(7,str.length()-1);
                                    for (int i = 0 ; i < shapes.size(); i++) {
                                        Shape shape = shapes.get(i);
                                        if (shape.getName().equals(sName)) {
                                            shapes.remove(shape);
                                            break;
                                        }
                                    }
                                    break;
                                case"scale":
                                    String sScale[] = str.substring(6, str.length()-1).split(",");
                                    for (Shape shape : shapes) {
                                        if (shape.getName().equals(sScale[0])) {
                                            shape.scale(Double.parseDouble(sScale[1]));
                                            break;
                                        }
                                    }
                                    break;
//
                                case"locate":
                                    String sLocate[] = str.substring(7, str.length()-1).split(",");
                                    for (Shape shape : shapes) {
                                        if(shape.getName().equals(sLocate[0])){
                                            shape.setPosition(Integer.parseInt(sLocate[1]),Integer.parseInt(sLocate[2]));
                                            break;
                                        }
                                    }
                                    break;
                                case"rotate":
                                    String sRotate[] = str.substring(7, str.length()-1).split(",");
                                    for (Shape shape : shapes) {
                                        if (shape.getName().equals(sRotate[0])) {
                                            shape.rotate(Double.parseDouble(sRotate[1]));
                                            break;
                                        }
                                    }
                                    break;
                                case "changeFillColor":
                                    String sChangeFillColor[] = str.substring(16, str.length() - 1).split(",");
                                    for (Shape shape : shapes) {
                                        if (shape.getName().equals(sChangeFillColor[0])) {
                                            shape.setFillColor(new Color(Integer.parseInt(sChangeFillColor[1])));
                                            break;
                                        }
                                    }
                                    break;
                                case "changeBorder":
                                    String sChangeBorderColor[] = str.substring(13, str.length() - 1).split(",");
                                    for (Shape shape : shapes) {
                                        if (shape.getName().equals(sChangeBorderColor[0])) {
                                            shape.setBorderColor(new Color(Integer.parseInt(sChangeBorderColor[1])));
                                            break;
                                        }
                                    }
                                    break;
                                case "priority":
                                    String sPriority[] = str.substring(9, str.length() - 1).split(",");
                                    for (Shape shape : shapes) {
                                        if (shape.getName().equals(sPriority[0])) {
                                            shape.setPriority(Integer.parseInt(sPriority[1]));
                                            break;
                                        }
                                    }
                                    break;
                            }
                        }
                        paintPanel.repaint();
                        sc.close();
                    } catch (FileNotFoundException e) {
                    }
                }
            }
        });

        menu.add(importText);
        menu.add(saveFile);
        menu.add(openFile);
        menu.add(removeitem);
        menu.add(addShapeButton);
        menu.add(coloritem);
        menu.add(coloritemborder);
        menu.add(scale);
        menu.add(rotate);
        menu.add(copy);
        menu.add(paste);
        menu.add(priority);
        menu.add(group);
        menu.add(addToGroup);
        menu.add(removeFromGroup);
        menu.add(ungroup);
        menu.add(resizeButton);

        MouseListener listener = new MouseListener() {

            @Override
            public void mouseExited(MouseEvent arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void mouseEntered(MouseEvent arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void mouseClicked(MouseEvent arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void mousePressed(MouseEvent arg0) {
                showPopup(arg0);
            }

            @Override
            public void mouseReleased(MouseEvent arg0) {
                showPopup(arg0);
            }

            private void showPopup(MouseEvent arg0) {
                if (arg0.isPopupTrigger()) {
                    menu.show(arg0.getComponent(),
                            arg0.getX(), arg0.getY());
                }
            }
        };

        //It is not reasonable
        //addMouseListener(listener);
        //commandsPanel.addMouseListener(listener);

        paintPanel.addMouseListener(listener);
    }

    public void reloadPopupMenu() {
        ArrayList<Shape> selectedShapes = paintPanel.getSelectedShapes();


        if (selectedShapes.size() == 0) {
            addToGroup.setEnabled(false);
            group.setEnabled(false);
        } else {
            group.setEnabled(true);
            addToGroup.setEnabled(true);
        }

        if (selectedShapes.size() == 1)
            resizeButton.setEnabled(true);
        else
            resizeButton.setEnabled(false);

        int groupsCount = 0;
        for (Shape shape : selectedShapes) {
            if (shape instanceof Group)
                groupsCount++;
        }

        if (groupsCount == 1)
            removeFromGroup.setEnabled(true);
        else
            removeFromGroup.setEnabled(false);

        if (groupsCount > 0)
            ungroup.setEnabled(true);
        else
            ungroup.setEnabled(false);

    }

    public PaintPanel getPaintPanel() {
        return paintPanel;
    }

    public static PaintFrame getCurrentInstance() {
        return currentPaintFrame;
    }

    public Color getCurrentFillColor() {
        return currentFillColor;
    }

    public void setCurrentFillColor(Color currentFillColor) {
        this.currentFillColor = currentFillColor;
    }

    public JMenuItem getGroup() {
        return group;
    }

    public JMenuItem getAddToGroup() {
        return addToGroup;
    }

    public JMenuItem getRemoveFromGroup() {
        return removeFromGroup;
    }

    public JMenuItem getUngroup() {
        return ungroup;
    }

    public Color getCurrentBorderColor() {
        return currentBorderColor;
    }

    public Stroke getStrokeStyle() {
        return strokeStyle;
    }
}

