package ir.agar.Model.Objects;

import ir.agar.Game;
import ir.agar.Game;

import java.awt.*;
import java.io.Serializable;

public class Circle implements Serializable {
    protected double x, y;
    protected Color color;
    protected double radius;
    protected double addRatio = 1;

    public Circle() {
    }

    public Circle(int x, int y, Color color, double radius) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.radius = radius;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        if (x < 0 || x > Game.width)
            return;
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        if (y < 0 || y > Game.height)
            return;

        this.y = y;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public int getTopLeftX(){
        return (int)(getX() - getRadius());
    }
    public int getTopLeftY(){
        return (int)(getY() - getRadius());
    }
    public int getBottomRightX(){
        return (int)(getX() + getRadius());
    }
    public int getBottomRightY(){
        return (int)(getY() + getRadius());
    }
    public int getWidth(){
        return (int)(2*getRadius());
    }
    public int getHeight(){
        return (int)(2*getRadius());
    }
    public double getArea(){
        return getRadius()*getRadius()*Math.PI;
    }

    public boolean contains(Circle circle){

        if (circle.getRadius() > radius)
            return false;

        double distance = Math.hypot(getX()-circle.getX(), getY()-circle.getY());
        double maxDistance = getRadius() - circle.getRadius();
        if (distance < maxDistance)
            return true;
        else
            return false;
    }

    public boolean contains(int x, int y){
        double distance = Math.hypot(x-getX(), y-getY());
        if (distance <= getRadius())
            return true;
        return false;
    }

    public boolean contains(int x, int y, double radius){
        double distance = Math.hypot(x-getX(), y-getY());
        double maxDistance = getRadius() + radius;
        if (distance < maxDistance)
            return true;
        else
            return false;
    }

    public void addCircle(Circle circle){
        double newRadius = Math.hypot(circle.getRadius() * circle.getAddRatio(), getRadius());
        setRadius(newRadius);
    }

    public double getAddRatio() {
        return addRatio;
    }

    public void setAddRatio(double addRatio) {
        this.addRatio = addRatio;
    }
}
