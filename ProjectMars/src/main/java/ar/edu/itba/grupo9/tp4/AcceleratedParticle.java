package ar.edu.itba.grupo9.tp4;


import java.awt.geom.Point2D;

public class AcceleratedParticle {
    private final int id;
    private Point2D position;
    private double velocity;
    private final double radius;
    private double mass;
    private double acceleration;

    public AcceleratedParticle(int id, double xPosition, double yPosition, double radius, double v, double mass, double a) {
        this.id = id;
        this.position    = new Point2D.Double(xPosition, yPosition);
        this.velocity    = v;
        this.acceleration = a;
        this.radius      = radius;
        this.mass        = mass;

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

    public double getVelocity() {
        return velocity;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public double getRadius() {
        return radius;
    }

    public double getMass() {
        return mass;
    }

    public double getAcceleration() {
        return acceleration;
    }


    public void setAcceleration(double acceleration) {
        this.acceleration = acceleration;
    }
}
