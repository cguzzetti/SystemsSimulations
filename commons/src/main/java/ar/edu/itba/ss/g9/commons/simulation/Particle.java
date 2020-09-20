package ar.edu.itba.ss.g9.commons.simulation;

import java.util.*;
import java.util.stream.Collectors;
import javafx.geometry.Point2D;

public class Particle {
    private double radius;
    private String name;
    private final int id;
    private Point2D point;
    private Set<Particle> neighbors;

    private final boolean hasVelocity;
    private double speed;
    private double direction;
    private final double mass;

    private int collisionCounter;


    public Particle(double xPosition, double yPosition, double radius, int id, String name){
        this.point = new Point2D(xPosition, yPosition);
        this.radius = radius;
        this.hasVelocity = false;
        this.id = id;
        this.name = name;
        this.neighbors = new HashSet<>();
        this.mass = 0.0;
        this.collisionCounter = 0;
    }

    public Particle(double xPosition, double yPosition, double radius, int id){
        this.point = new Point2D(xPosition, yPosition);
        this.radius = radius;
        this.hasVelocity = false;
        this.id = id;
        this.neighbors = new HashSet<>();
        this.name = String.format("p%d", id);
        this.mass = 0.0;
        this.collisionCounter = 0;
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
        this.collisionCounter = 0;
    }

    public Particle(double xPosition, double yPosition, double direction, int id, double mass){
        this.id = id;
        this.point = new Point2D(xPosition, yPosition);
        this.radius = 0.0015;
        this.hasVelocity = true;
        this.speed = 0.01;
        this.direction = direction;
        this.mass = mass;
        this.collisionCounter = 0;
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

    public int getCollisionCounter() {
        return collisionCounter;
    }

    public void setCollisionCounter(int collisionCounter) {
        this.collisionCounter = collisionCounter;
    }

    public void increaseCollisionCounter() {
        this.collisionCounter++;
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
        if(mass == 0)
            return String.format(
                    "%d %f %f %f %f %f",
                    this.getId(),this.point.getX(), this.point.getY(), this.getVx(), this.getVy(), this.direction
            );

        return String.format(
                "%d %f %f %f %f %f %f",
                this.getId(), this.point.getX(), this.point.getY(), this.getVx(), this.getVy(), this.direction, this.mass
        );
    }

    public double getMass() {
        return mass;
    }

    public boolean isValid(Collection<Particle> particleList){
        return particleList.parallelStream().noneMatch(this::collidesWith);
    }

    public boolean collidesWith(Particle p){
        return this.point.distance(p.point) - this.radius - p.radius < 0;
    }

    public void updateParticlePosition(double time) {
        this.point.add(this.getVx()*time,this.getVy()*time);
    }

    private double timeToCollideWith(Particle particle) {
        Point2D deltaR = new Point2D(particle.getX() - this.getX(), particle.getY() - this.getY());
        Point2D deltaV = new Point2D(particle.getVx() - this.getVx(), particle.getVy() - this.getVy());
        double deltaVdeltaR = deltaV.dotProduct(deltaR);

        if(deltaVdeltaR >= 0)
            return Double.POSITIVE_INFINITY;

        double sigma = this.radius + particle.getRadius();
        double deltaVdeltaV = deltaV.dotProduct(deltaV);
        double deltaRdeltaR = deltaR.dotProduct(deltaR);
        double d = deltaVdeltaR*deltaVdeltaR-deltaVdeltaV*(deltaRdeltaR-sigma*sigma);

        if(d < 0)
            return Double.POSITIVE_INFINITY;

        return  -(deltaVdeltaR+Math.sqrt(d))/deltaVdeltaV;
    }

    private Optional<Collision> willCollideWith(Particle particle) {
        double deltaT = timeToCollideWith(particle);
        if(deltaT == Double.POSITIVE_INFINITY)
            return Optional.empty();

        return Optional.of(new ParticleCollision(this, particle, deltaT));
    }

    private Optional<Collision> willCollideWithWalls(Point2D[] wall) {
        if(this.getVx() == 0 && this.getVy() == 0)
            return Optional.empty();

        Point2D wallStart = wall[0];
        Point2D wallEnd = wall[1];
        boolean vertical;
        double origin, wallPosition, velocity;
        vertical = wallStart.getX() == wallEnd.getX();

        if(vertical) {
            origin = this.getY();
            velocity = this.getVy();
            wallPosition = velocity > 0? wallEnd.getY():wallStart.getY();
        }
        else {
            origin = this.getX();
            velocity = this.getVx();
            wallPosition = velocity > 0? wallEnd.getX():wallStart.getX();
        }

        double deltaT = (wallPosition + velocity > 0? -1:1 * this.getRadius() - origin)/velocity;

        if(deltaT < 0)
            return Optional.empty();

        // TODO: calculate pressure?
        return Optional.of(new WallCollision(this, deltaT));
    }

    public List<Collision> calculateParticleNextCollision(Collection<Particle> particles, Point2D[][] verticalWalls, Point2D[][] horizontalWalls) {
        List<Collision> particleNextCollisions = new LinkedList<>();

        // Search for collisions with other particles
        for(Particle p: particles) {
            if(!this.equals(p)) {
                this.willCollideWith(p).ifPresent(particleNextCollisions::add);
            }
        }

        // Search with collisions with walls
        for(Point2D[] verticalWall: verticalWalls)
            this.willCollideWithWalls(verticalWall).ifPresent(particleNextCollisions::add);
        for(Point2D[] horizontalWall: horizontalWalls)
            this.willCollideWithWalls(horizontalWall).ifPresent(particleNextCollisions::add);

        return particleNextCollisions;
    }
}
