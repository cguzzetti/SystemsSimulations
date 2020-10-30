package ar.edu.itba.ss.g9.tp5;

import java.util.Comparator;

/**
 * Save the particle along with its time for collision
 */
public class Crash implements Comparable<Crash> {
    private Particle particle;
    private double time;

    Crash(Particle p, double time){
        this.particle   = p;
        this.time       = time;
    }

    public Particle getParticle() {
        return particle;
    }

    public double getTime() {
        return time;
    }

    @Override
    public int compareTo(Crash o) {
        return Double.compare(this.time, o.getTime());
    }

}
