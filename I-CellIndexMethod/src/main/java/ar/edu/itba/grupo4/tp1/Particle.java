package ar.edu.itba.grupo4.tp1;

import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Particle {
    private double x;
    private double y;
    private double radius;
    private String name;
    private final int id;
    private Point2D.Double point;
    private Set<Particle> neighbors;

    private final boolean hasVelocity;
    private double velocityX;
    private double velocityY;

    public Particle(double xPosition, double yPosition, double radius, int id, String name){
        this.point = new Point2D.Double(xPosition, yPosition);
        this.radius = radius;
        this.hasVelocity = false;
        this.id = id;
        this.name = name;
        this.neighbors = new HashSet<>();
    }

    public Particle(double xPosition, double yPosition, double radius, int id){
        this.point = new Point2D.Double(xPosition, yPosition);
        this.radius = radius;
        this.hasVelocity = false;
        this.id = id;
        this.neighbors = new HashSet<>();
        this.name = String.format("p%d", id);
    }

    public Particle(double xPosition, double yPosition, double velocityX, double velocityY, double radius, int id){
        this.point = new Point2D.Double(xPosition, yPosition);
        this.radius = radius;
        this.hasVelocity = true;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.id = id;
        this.neighbors = new HashSet<>();
        this.name = String.format("p%d", id);
    }

    public void setX(double x){
        this.point.x = x;
    }

    public void setY(double y){
        this.point.y = y;
    }

    public void setRadius(int radius){
        this.radius = radius;
    }

    public void setVelocityX(double velocityX) {
        this.velocityX = velocityX;
    }

    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }

    public String getName() {
        return this.name;
    }

    public Point2D.Double getPoint() {
        return this.point;
    }

    public double getX(){
        return this.point.getX();
    }

    public double getY(){
        return this.point.getY();
    }

    public double getRadius(){
        return this.radius;
    }

    public int getId(){
        return this.id;
    }

    public double getVelocityX(){
        return this.velocityX;
    }
    public double getVelocityY(){
        return this.velocityY;
    }

    public boolean variesOverTime(){
        return this.hasVelocity;
    }

    public void addNeighbor(Particle pNeighbor) {
        this.neighbors.add(pNeighbor);
        pNeighbor.getNeighbors().add(this);
    }

    public void clearNeighbors() {
        this.neighbors.clear();
    }
    public Set<Particle> getNeighbors() {
        return this.neighbors;
    }

    @Override
    public String toString(){

        if(!hasVelocity)
            return String.format(
                    "%d %.3f %.3f %.3f %s",
                    this.getId() ,this.point.getX(), this.point.getY(), this.radius, this.neighbors.stream().map(Particle::getId).collect(Collectors.toList()).stream().map(String::valueOf).collect(Collectors.joining(","))
            );

        return String.format(
                "Name: %s (x: %.3f, y: %.3f, vx: %.3f, vy: %.3f, radius: %.3f)",
                this.getName(),this.point.getX(), this.point.getY(), this.velocityX, this.velocityY, this.radius
        );
    }
}
