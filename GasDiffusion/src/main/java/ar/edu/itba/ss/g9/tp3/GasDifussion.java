package ar.edu.itba.ss.g9.tp3;

import ar.edu.itba.ss.g9.commons.simulation.Particle;

import java.util.Collection;
import java.util.PriorityQueue;
import java.util.Queue;

public class GasDifussion {
    private final int N;
    private final double height;
    private final double width;
    private final double partitionLen;
    private Queue<Particle> colisions;

    public GasDifussion(GasDifussionConfig config){
        this.N = config.getN();
        this.height = config.getHeight();
        this.width = config.getWidth();
        this.partitionLen = config.getPartitionLen();
        colisions = new PriorityQueue<>();
        // Definir paredes
    }

    public void simulate(Collection<Particle> particles){
        int dt = 0;
        // calculate all colisions
        // TODO: EndCondition won't be time but fp
        while(dt != 1000){
            //Optional<Event>: getFirstColision
            // if(!getFirstColision.isPresent()) break;

            // advanceParticles(firstColision.time())
            // save system state
            // updateVelocityOfEventParticles
            // determineAllFutureColisionsOfParticles

            dt++;
        }

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

    public Queue<Particle> getColisions() {
        return colisions;
    }

    public void setColisions(PriorityQueue<Particle> colisions) {
        this.colisions = colisions;
    }
}
