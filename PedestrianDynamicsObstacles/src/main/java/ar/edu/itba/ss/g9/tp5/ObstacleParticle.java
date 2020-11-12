package ar.edu.itba.ss.g9.tp5;

import java.awt.geom.Point2D;

import static ar.edu.itba.ss.g9.tp5.Configuration.HEIGHT;

/**
 * Particle that represents an obstacle. This particles ONLY move on the Y axis. Vx = 0
 */
public class ObstacleParticle extends Particle {
    public ObstacleParticle(int id, double xPosition, double yPosition, double xVelocity, double yVelocity, double mass, double radius) {
        super(id, xPosition, yPosition, xVelocity, yVelocity, mass, radius);
    }

    public void move(double dt) {
        double y = this.getPosY();
        double vy = this.getVy();
        if(y>= HEIGHT - this.getRadius() || y <= 0 + this.getRadius()){
            this.setVelocity(0, -1 * this.getVy()); // Bounce on the wall
            vy = -vy;
        }
        this.setPosition(this.getPosition().getX(), y +  vy * dt);
    }

    @Override
    public String toString() {
        return super.toString() + " 1 0 0";
    }
}
