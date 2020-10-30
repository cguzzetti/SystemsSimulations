package ar.edu.itba.ss.g9.tp5;

import javax.lang.model.element.VariableElement;
import java.awt.geom.Point2D;
import java.util.Set;
import java.util.stream.Collectors;

public class Particle {
    final int id;
    final double radius;
    private final String name;
    Point2D position;
    Point2D velocity;
    final double mass;



    public Particle( int id, double xPosition, double yPosition, double xVelocity, double yVelocity, double mass, double radius){
        this.id = id;
        this.name = String.format("p%d", id);
        this.position = new Point2D.Double(xPosition, yPosition);
        this.velocity = new Point2D.Double(xVelocity, yVelocity);
        this.radius = radius;


        this.mass = mass;
    }

    public void setX(double x){ this.position = new Point2D.Double(x, this.position.getY()); }

    public void setY(double y){
        this.position = new Point2D.Double(this.position.getX(), y);
    }

    public String getName() {
        return this.name;
    }

    public Point2D getPosition() {
        return this.position;
    }

    public double getPosX(){
        return this.position.getX();
    }

    public double getPosY(){
        return this.position.getY();
    }

    public double getRadius(){
        return this.radius;
    }

    public int getId(){
        return this.id;
    }


    public double getVx() {
        return this.velocity.getX();
    }

    public double getVy() {
        return this.velocity.getY();
    }

    public void setVelocity(double vx, double vy){
        this.velocity = new Point2D.Double(vx, vy);
    }


    @Override
    public String toString(){
        return String.format(
                "Particle{id=%d, radius=%.2f, mass=%.2f, pos=(%.3f, %.3f), vel=(%.3f, %.3f)",
                this.id, this.radius, this.mass, this.position.getX(), this.position.getY(),
                this.velocity.getX(), this.velocity.getY()
        );
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Particle)) return false;

        Particle particle = (Particle) obj;

        return this.getId() == particle.getId();
    }

    public double getMass() {
        return mass;
    }

    public void setPosition(double x, double y){
        this.position = new Point2D.Double(x, y);
    }

    public boolean isValid(Set<ObstacleParticle> obstacles){
        return obstacles.parallelStream().noneMatch(this::collidesWithInX);
    }

    public boolean collidesWithInX(ObstacleParticle p){
        return this.getPosX() - p.getPosX() < this.radius - p.radius;
    }
}
