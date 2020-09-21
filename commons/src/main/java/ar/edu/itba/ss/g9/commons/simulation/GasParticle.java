package ar.edu.itba.ss.g9.commons.simulation;

import javafx.geometry.Point2D;

import java.util.*;

public class GasParticle extends Particle{
    private Point2D velocity;
    private int collisionCounter;

    public GasParticle(double xPosition, double yPosition, double direction, int id, double mass) {
        super(id, xPosition, yPosition, 0.0015, 0.01, mass);
        this.velocity = new Point2D(
                this.speed*Math.cos(direction),
                this.speed*Math.sin(direction)
        );
        this.collisionCounter = 0;
    }

    public GasParticle(double xPosition, double yPosition, double Vx, double Vy, int id, double mass){
        super(id, xPosition, yPosition, 0.015, 0.01, mass);
        this.velocity = new Point2D(Vx, Vy);
        this.collisionCounter = 0;
    }

    public Point2D getVelocity() {
        return velocity;
    }

    @Override
    public double getVx(){
        return velocity.getX();
    }

    @Override
    public double getVy(){
        return velocity.getY();
    }

    @Override
    public String toString(){
        return String.format(
                "%d %f %f %f %f %f %f",
                this.getId(), this.point.getX(), this.point.getY(), this.getVx(), this.getVy(), this.getRadius(), this.mass
        );
    }

    public void setVelocity(Point2D newVelocity){
        this.velocity = newVelocity;
    }
    public int getCollisionCounter() {
        return collisionCounter;
    }

    public void incrementCollisionCounter(){
        this.collisionCounter++;
    }

    public void decreaseCollisionCounter(){
        this.collisionCounter--;
    }

    public boolean isValid(Collection<GasParticle> particleList){
        return particleList.parallelStream().noneMatch(this::collidesWith);
    }

    public boolean collidesWith(GasParticle p){
        return this.point.distance(p.point) - this.radius - p.radius < 0;
    }

    public void updateParticlePosition(double time) {
        this.setPosition(this.point.add(this.velocity.getX()*time,this.velocity.getY()*time));
    }

    private double timeToCollideWith(GasParticle particle) {
        Point2D deltaR = particle.getPoint().subtract(this.getPoint());
        Point2D deltaV = particle.getVelocity().subtract(this.getVelocity());
        double deltaVdeltaR = deltaV.dotProduct(deltaR);

        if(deltaVdeltaR >= 0)
            return Double.POSITIVE_INFINITY;

        double sigma = this.radius + particle.getRadius();
        double deltaVdeltaV = deltaV.dotProduct(deltaV);
        double deltaRdeltaR = deltaR.dotProduct(deltaR);
        double d = deltaVdeltaR*deltaVdeltaR-deltaVdeltaV*(deltaRdeltaR-sigma*sigma);

        if(d < 0)
            return Double.POSITIVE_INFINITY;

        return  -(deltaVdeltaR + Math.sqrt(d)) / deltaVdeltaV;
    }

    private Optional<Collision> willCollideWith(GasParticle particle, double timeSoFar) {
        double deltaT = timeToCollideWith(particle);
        if(deltaT == Double.POSITIVE_INFINITY)
            return Optional.empty();

        return Optional.of(new ParticleCollision(this, particle, deltaT));
    }

    private Optional<Collision> willCollideWithWalls(Point2D[] wall, double timeSoFar) {
        if(this.getVx() == 0 && this.getVy() == 0)
            return Optional.empty();

        Point2D wallStart = wall[0];
        Point2D wallEnd = wall[1];
        boolean isVertical;
        double origin, wallPosition, velocity;
        isVertical = wallStart.getX() == wallEnd.getX();

        if(isVertical) {
            origin = this.getX();
            velocity = this.getVx();
            wallPosition = velocity > 0? wallEnd.getX():wallStart.getX();
        }
        else {
            origin = this.getY();
            velocity = this.getVy();
            wallPosition = velocity > 0? wallEnd.getY():wallStart.getY();
        }

//        System.out.println("id="+this.id);
//        System.out.println("wall position: "+wallPosition);
//        System.out.println("velocity: "+velocity);
//        System.out.println("origin: "+origin);
//        System.out.println("isVertical: "+isVertical);

        double deltaT = (wallPosition + velocity > 0? -1:1 * this.getRadius() - origin)/velocity;
//        System.out.println(deltaT);

        if(deltaT < 0)
            return Optional.empty();

        // TODO: calculate pressure?
        return Optional.of(new WallCollision(this, deltaT + timeSoFar, isVertical));
    }

    public List<Collision> calculateParticleNextCollision(Collection<GasParticle> particles, Point2D[][] verticalWalls, Point2D[][] horizontalWalls, double timeSoFar) {
        List<Collision> particleNextCollisions = new LinkedList<>();

        // TODO: uncomment when wall collisions work
//        // Search for collisions with other particles
//        for(GasParticle p: particles) {
//            if(!this.equals(p)) {
//                this.willCollideWith(p, timeSoFar).ifPresent(particleNextCollisions::add);
//            }
//        }

        // Search with collisions with walls
        for(Point2D[] verticalWall: verticalWalls)
            this.willCollideWithWalls(verticalWall, timeSoFar).ifPresent(particleNextCollisions::add);
        for(Point2D[] horizontalWall: horizontalWalls)
            this.willCollideWithWalls(horizontalWall, timeSoFar).ifPresent(particleNextCollisions::add);

        return particleNextCollisions;
    }
}
