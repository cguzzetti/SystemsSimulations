package ar.edu.itba.ss.g9.tp5;

import java.awt.geom.Point2D;

/**
 * Particle that represents an obstacle. This particles ONLY move on the Y axis. Vx = 0
 */
public class ObstacleParticle extends Particle {
    // TODO: Maybe be able to modify this??
    public static final int WALL_HEIGHT = 30;
    public ObstacleParticle(int id, double xPosition, double yPosition, double xVelocity, double yVelocity, double mass, double radius) {
        super(id, xPosition, yPosition, xVelocity, yVelocity, mass, radius);
    }

    public void move(Point2D finalForce, double dt) {
        double y = this.getPosY();
        double vy = this.getVy()*dt;
        if(y>= WALL_HEIGHT - this.getRadius() || y <= 0 + this.getRadius()){
            this.setVelocity(0, -1 * this.getVy()); // Bounce on the wall
            vy = -vy;
        }
        this.setPosition(this.getPosition().getX(), y +  vy);
    }
}
