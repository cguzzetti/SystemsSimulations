package ar.edu.itba.ss.g9.commons.simulation;

import java.util.*;
import java.util.stream.Collectors;
import javafx.geometry.Point2D;

public class Particle {
    /* package */ int id;
    /* package */ double radius;
    private final String name;
    /* package */ Point2D point;
    private Set<Particle> neighbors;

    /* package */ final boolean hasVelocity;
    /* package */ double speed;
    private double direction;
    /* package */ double mass;



    public Particle(double xPosition, double yPosition, double radius, int id, String name){
        this.point = new Point2D(xPosition, yPosition);
        this.radius = radius;
        this.hasVelocity = false;
        this.id = id;
        this.name = name;
        this.neighbors = new HashSet<>();
        this.mass = 0.0;
    }

    public Particle(double xPosition, double yPosition, double radius, int id){
        this.point = new Point2D(xPosition, yPosition);
        this.radius = radius;
        this.hasVelocity = false;
        this.id = id;
        this.neighbors = new HashSet<>();
        this.name = String.format("p%d", id);
        this.mass = 0.0;
    }

    public Particle(double xPosition, double yPosition, double speed, double direction, double radius, int id){
        this.point = new Point2D(xPosition, yPosition);
        this.radius = radius;
        this.hasVelocity = true;
        this.direction = direction;
        this.speed = speed;
        this.id = id;
        this.neighbors = new HashSet<>();
        this.name = String.format("p%d", id);
        this.mass = 0.0;
    }

    public Particle( int id, double xPosition, double yPosition, double radius, double speed, double mass){
        this.id = id;
        this.name = String.format("p%d", id);
        this.point = new Point2D(xPosition, yPosition);
        this.radius = radius;
        this.hasVelocity = true;
        this.speed = speed;
        this.direction = -2; // Should throw NaN
        this.mass = mass;
    }

    public void setX(double x){ this.point = new Point2D(x, this.point.getY()); }

    public void setY(double y){
        this.point = new Point2D(this.point.getX(), y);
    }

    public void setRadius(int radius){
        this.radius = radius;
    }

    public String getName() {
        return this.name;
    }

    public Point2D getPoint() {
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

    public double getDirection() {
        return this.direction;
    }
    public double getSpeed() {
        return this.speed;
    }


    public void setDirection(double direction) {
        this.direction = direction;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getVx() {
        return this.speed*Math.cos(this.direction);
    }

    public double getVy() {
        return this.speed*Math.sin(this.direction);
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
                    "%d %f %f %f %f %f",
                    this.getId(),this.point.getX(), this.point.getY(), this.getVx(), this.getVy(), this.direction
        );
    }

    public double getMass() {
        return mass;
    }
}
