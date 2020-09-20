package ar.edu.itba.ss.g9.commons.simulation;

import javafx.geometry.Point2D;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

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
        this.point.add(this.velocity.getX()*time,this.velocity.getY()*time);
    }

    private double timeToCollideWith(GasParticle particle) {
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

    private Optional<Collision> willCollideWith(GasParticle particle) {
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

    public List<Collision> calculateParticleNextCollision(Collection<GasParticle> particles, Point2D[][] verticalWalls, Point2D[][] horizontalWalls) {
        List<Collision> particleNextCollisions = new LinkedList<>();

        // Search for collisions with other particles
        for(GasParticle p: particles) {
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
