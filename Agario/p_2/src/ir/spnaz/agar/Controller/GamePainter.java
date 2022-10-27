package ir.spnaz.agar.Controller;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.CopyOnWriteArrayList;

import ir.spnaz.agar.Game;
import ir.spnaz.agar.Model.Engine;
import ir.spnaz.agar.Model.Objects.*;

import javax.swing.*;

public class GamePainter {

    public static void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);


        g2d.setRenderingHints(rh);


        g.clearRect(0, 0, Game.width, Game.height);
        paintEnergyCircles(g2d);
        paintPlayerCircles(g2d);
        paintGearCircles(g2d);
        paintPowers(g2d);
    }

    private static void paintPowers(Graphics2D g) {
        CopyOnWriteArrayList<PowerCircle> powerCircles = Engine.getCurrentInstance().getPowerCircles();
        for (int i = 0; i < powerCircles.size(); i++) {
            PowerCircle powerCircle = powerCircles.get(i);
            Color color = new Color(powerCircle.getColor());

            double radius = powerCircle.getRadius();
            g.setColor(color);
            g.fillOval(powerCircle.getTopLeftX(), powerCircle.getTopLeftY(), powerCircle.getWidth(), powerCircle.getHeight());
            g.setColor(Color.BLACK);
            g.drawString(powerCircle.getPowerType().getId() + "", (int) (powerCircle.getX() - powerCircle.getRadius() * 0.5), (int) (powerCircle.getY() + powerCircle.getRadius() * 0.45));
        }
    }

    private static void paintGearCircles(Graphics2D g) {
        CopyOnWriteArrayList<GearCircle> gearCircles = Engine.getCurrentInstance().getGearCircles();
        for (int i = 0; i < gearCircles.size(); i++) {
            GearCircle gearCircle = gearCircles.get(i);

            int color = gearCircle.getColor();
            g.setColor(new Color((color / 0x10000) % 0x100, (color / 0x1000) % 0x100, color % 0x100, (int) (gearCircle.getAlpha())));


            int[] xPoints = new int[10];
            int[] yPoints = new int[10];

            double x = gearCircle.getX();
            double y = gearCircle.getY();
            double radius = gearCircle.getRadius();
            for (int j = 0; j < 10; j++) {
                xPoints[j] = (int) (x + radius * Math.cos(2.0 * Math.PI * j / 10));
                yPoints[j] = (int) (y + radius * Math.sin(2.0 * Math.PI * j / 10));
            }


            g.translate(gearCircle.getX(), gearCircle.getY());
            g.rotate(Math.toRadians(gearCircle.getAngle()));
            g.translate(-gearCircle.getX(), -gearCircle.getY());
            g.fillPolygon(xPoints, yPoints, 10);
            g.translate(gearCircle.getX(), gearCircle.getY());
            g.rotate(-Math.toRadians(gearCircle.getAngle()));
            g.translate(-gearCircle.getX(), -gearCircle.getY());


            double insideRadius = Engine.GEAR_MIN_RADIUS / 4.0;
            int circleAlpha = (int) gearCircle.getAlpha() * 2;
            if (circleAlpha > 255)
                circleAlpha = 255;
            g.setColor(new Color(255, 255, 255, circleAlpha));
            g.fillOval((int) (gearCircle.getX() - insideRadius), (int) (gearCircle.getY() - insideRadius), (int) insideRadius * 2, (int) insideRadius * 2);
        }

    }

    private static void paintEnergyCircles(Graphics2D g) {
        CopyOnWriteArrayList<EnergyCircle> energyCircles = Engine.getCurrentInstance().getEnergyCircles();
        for (int i = 0; i < energyCircles.size(); i++) {
            EnergyCircle energyCircle = energyCircles.get(i);
            Color color = new Color(energyCircle.getColor());

            double radius = energyCircle.getRadius();
            g.setColor(color);
            g.fillOval(energyCircle.getTopLeftX(), energyCircle.getTopLeftY(), energyCircle.getWidth(), energyCircle.getHeight());
            g.setColor(color.brighter());
            radius *= 0.7;
            g.fillOval(((int) (energyCircle.getX() - radius)) - 2, ((int) (energyCircle.getY() - radius)) - 2, ((int) (radius * 2)), ((int) (radius * 2)));
            g.setColor(color.darker());
            radius *= 0.7;
            g.fillOval(((int) (energyCircle.getX() - radius)) + 1, ((int) (energyCircle.getY() - radius)) + 1, ((int) (radius * 2)), ((int) (radius * 2)));

//            g.drawString("E", (int) (energyCircle.getX() - energyCircle.getRadius() * 0.5), (int) (energyCircle.getY() + energyCircle.getRadius() * 0.45));
        }
    }

    private static void paintPlayerCircles(Graphics2D g) {

        CopyOnWriteArrayList<PlayerCircle> playerCircles = Engine.getCurrentInstance().getPlayerCircles();

        playerCircles.sort(new Comparator<PlayerCircle>() {
            @Override
            public int compare(PlayerCircle c1, PlayerCircle c2) {
                double comp = c1.getArea() - c2.getArea();
                if (comp * comp < 1e-10) {
                    return 0;
                } else if (comp < 0) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });


        for (int i = 0; i < playerCircles.size(); i++) {
            PlayerCircle playerCircle = playerCircles.get(i);
            g.setColor(new Color(playerCircle.getColor()));
            g.fillOval(playerCircle.getTopLeftX(), playerCircle.getTopLeftY(), playerCircle.getWidth(), playerCircle.getHeight());
        }
    }

    public static void run() {
        JPanel jPanel = Game.getjPanel();
        Engine engine = Engine.getCurrentInstance();


        while (true) {

            engine.update();
            jPanel.revalidate();
            jPanel.repaint();

            try {
                Thread.sleep(Game.GAME_SLEEP);
            } catch (InterruptedException e) {
                // Ignore
            }
        }

    }
}
