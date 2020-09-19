package ar.edu.itba.ss.g9.tp3;

import ar.edu.itba.ss.g9.commons.simulation.Particle;
import jdk.jfr.internal.Options;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.List;

public class GasDifussion {
    private final int N;
    private final double height;
    private final double width;
    private final double partitionLen;
    private Queue<Collision> collisions;
    private Collection<Particle> particles;
    private Point2D.Double verticalWalls;
    private Point2D.Double horizontalWalls;

    public GasDifussion(GasDifussionConfig config, Collection<Particle> particles){
        this.N = config.getN();
        this.height = config.getHeight();
        this.width = config.getWidth();
        this.partitionLen = config.getPartitionLen();
        this.collisions = new PriorityQueue<>();
        this.particles = particles;
        // Definir paredes
    }

    public void simulate(){
        int dt = 0;
        calculateCollisions();
        // TODO: EndCondition won't be time but fp
        while(dt != 1000){
            //Optional<Collision>: getFirstCollision
            if(collisions.isEmpty()) return;
            Optional<Collision> maybeCollision = getCollisionIfValid(collisions.poll());
            if(!maybeCollision.isPresent()) break;


            // advanceParticles(firstCollision.time())
            // save system state
            // updateVelocityOfCollisionParticles
            // determineAllFutureCollisionsOfParticles

            dt++;
        }

    }

    // Calculates te next collision for all particles
    private void calculateCollisions() {
        for(Particle p: particles) {
            this.collisions.addAll(calculateParticleNextCollision(p));
        }
    }

    private Optional<Collision> getCollisionIfValid(Collision collision){
        if(collision.isValid())
            return Optional.of(collision);

        return Optional.empty();
    }

    private List<Collision> calculateParticleNextCollision(Particle particle) {
        List<Collision> particleNextCollisions = new LinkedList<>();
        for(Particle p: particles) {
            if(!p.equals(particle)) {
                //if(Collision(particle, p).isPresent(particleNextCollisions::add));
            }
                
        }


        return particleNextCollisions;
    }

    public double getHeight() {
        return height;
    }

    public int getN() {
        return N;
    }

    public double getWidth() {
        return width;
    }

    public double getPartitionLen() {
        return partitionLen;
    }


    public Queue<Collision> getCollisions() {
        return collisions;
    }

    public void setCollisions(PriorityQueue<Collision> collisions) {
        this.collisions = collisions;
    }
}
