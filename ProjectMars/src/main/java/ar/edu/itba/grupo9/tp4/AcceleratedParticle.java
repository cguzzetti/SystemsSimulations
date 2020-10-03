package ar.edu.itba.grupo9.tp4;


import java.awt.geom.Point2D;

public class AcceleratedParticle {
    private final int id;
    private Point2D position;
    private Point2D velocity;
    private Point2D prevPosition;
    private Point2D prevVelocity;
    private final double radius;
    private final double mass;
    double[] rListY;
    double[] rListX;

    public AcceleratedParticle(int id, double xPosition, double yPosition, double radius, double mass) {
        this.id = id;
        this.position    = new Point2D.Double(xPosition, yPosition);
        this.radius      = radius;
        this.mass        = mass;
        this.rListX = new double[6];
        this.rListY = new double[6];
    }


    public AcceleratedParticle(int id, Point2D position, double radius, double mass){
        this(id, position.getX(), position.getY(), radius, mass);
    }

    public int getId() {
        return id;
    }

    public Point2D getPosition() {
        return position;
    }

    public void setPosition(Point2D position) {
        this.position = position;
    }

    public Point2D getVelocity() {
        return velocity;
    }

    public void setVelocity(Point2D velocity) {
        this.velocity = velocity;
    }

    public double getRadius() {
        return radius;
    }

    public double getMass() {
        return mass;
    }

    public void setPrevPosition(Point2D prevPosition) {
        this.prevPosition = prevPosition;
    }

    public void setPrevVelocity(Point2D prevVelocity) {
        this.prevVelocity = prevVelocity;
    }
}
