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

    public AcceleratedParticle(int id, double radius, double mass){
        this.id = id;
        this.radius = radius;
        this.mass = mass;
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

    public void setPositionX(double x) {
        this.setPosition(new Point2D.Double(x, this.position.getY()));
    }

    public void setPositionY(double y) {
        this.setPosition(new Point2D.Double(this.position.getX(), y));
    }

    public Point2D getVelocity() {
        return velocity;
    }

    public void setVelocity(Point2D velocity) {
        this.velocity = velocity;
    }

    public void setVelocityX(double vx) {
        this.setVelocity(new Point2D.Double(vx, this.getVelocity().getY()));
    }

    public void setVelocityY(double vy) {
        this.setVelocity(new Point2D.Double(this.getVelocity().getX(), vy));
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

    public Point2D getPrevPosition() {
        return prevPosition;
    }

    public Point2D getPrevVelocity() {
        return prevVelocity;
    }
}
