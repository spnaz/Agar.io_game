package ir.agar.Controller;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;

import ir.agar.Game;
import ir.agar.Model.ClientEngine;
import ir.agar.Model.Objects.*;
import ir.agar.Model.ServerEngine;
import ir.agar.Game;
import ir.agar.Model.ServerEngine;
import ir.agar.Model.GameEngine;

import javax.swing.*;

public class Painter extends Thread {

    private static boolean loose = false;


    public static void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        if (isLoose()) {
            ((Graphics2D) g).drawString("You Lost !", Game.width / 2, Game.height / 2);
            return;
        }


        g2d.setRenderingHints(rh);


        g.clearRect(0, 0, Game.width, Game.height);
        paintEnergyCircles(g2d);
        paintPlayerCircles(g2d);
        paintGearCircles(g2d);
        paintPowers(g2d);

        paintUserPowers(g2d);
    }

    private static void paintUserPowers(Graphics2D g2d) {
        try {
            Player player = ClientEngine.getCurrentInstance().getPlayer(ClientController.getCurrentInstance().getUsername());
            ArrayList<PowerType> powers = player.getPowers();

            int x0 = 20;
            int y0 = 20;

            for (int i = 0; i < powers.size(); i++) {
                Color color = powers.get(i).getColor();
                g2d.setColor(color);
                g2d.setStroke(new BasicStroke(8));
                g2d.fillOval(x0, y0, 25, 25);

                if (powers.get(i).isActive())
                    g2d.setColor(Color.WHITE);
                else
                    g2d.setColor(Color.BLACK);

                g2d.drawOval(x0, y0, 25, 25);
                g2d.drawString(i + 1 + "", x0 + 8, y0 + 16);
                y0 += 30;
            }
        } catch (Exception e) {
            // ignore
        }
    }

    private static void paintPowers(Graphics2D g) {
        List<PowerCircle> powerCircles = Game.getGameEngine().getPowerCircles();
        for (int i = 0; i < powerCircles.size(); i++) {
            PowerCircle powerCircle = powerCircles.get(i);
            Color color = (powerCircle.getColor());

            double radius = powerCircle.getRadius();
            g.setColor(color);
            g.fillOval(powerCircle.getTopLeftX(), powerCircle.getTopLeftY(), powerCircle.getWidth(), powerCircle.getHeight());
        }
    }

    private static void paintGearCircles(Graphics2D g) {
        List<GearCircle> gearCircles = Game.getGameEngine().getGearCircles();
        for (int i = 0; i < gearCircles.size(); i++) {
            GearCircle gearCircle = gearCircles.get(i);

            Color color = gearCircle.getColor();
            g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) (gearCircle.getAlpha())));


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


            double insideRadius = ServerEngine.GEAR_MIN_RADIUS / 4.0;
            int circleAlpha = (int) gearCircle.getAlpha() * 2;
            if (circleAlpha > 255)
                circleAlpha = 255;
            g.setColor(new Color(255, 255, 255, circleAlpha));
            g.fillOval((int) (gearCircle.getX() - insideRadius), (int) (gearCircle.getY() - insideRadius), (int) insideRadius * 2, (int) insideRadius * 2);
        }

    }

    private static void paintEnergyCircles(Graphics2D g) {
        List<EnergyCircle> energyCircles = Game.getGameEngine().getEnergyCircles();
        for (int i = 0; i < energyCircles.size(); i++) {
            EnergyCircle energyCircle = energyCircles.get(i);
            Color color = (energyCircle.getColor());

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

        List<PlayerCircle> playerCircles = Game.getGameEngine().getPlayerCircles();


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
            g.setColor(playerCircle.getColor());
            g.fillOval(playerCircle.getTopLeftX(), playerCircle.getTopLeftY(), playerCircle.getWidth(), playerCircle.getHeight());
            g.setColor(Color.BLACK);
            g.drawString(playerCircle.getPlayer().getName(), (int) (playerCircle.getX() - playerCircle.getRadius() * 0.5), (int) (playerCircle.getY() + playerCircle.getRadius() * 0.5));
        }
    }

    @Override
    public void run() {
        JPanel gamePanel = Game.getGamePanel();
        GameEngine engine = Game.getGameEngine();


        while (true) {
            try {
                if (engine.isUpdated()) {
                    gamePanel.revalidate();
                    gamePanel.repaint();
                }

                if (isLoose()) {
                    gamePanel.revalidate();
                    gamePanel.repaint();
                    return;
                }
                sleep(Game.GAME_SLEEP);
            } catch (Exception e) {
                // Ignore
            }
        }
    }

    public synchronized static boolean isLoose() {
        return loose;
    }

    public synchronized static void setLoose(boolean loose) {
        Painter.loose = loose;
    }
}
