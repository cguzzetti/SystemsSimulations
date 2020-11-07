package ar.edu.itba.ss.g9.tp5;

import java.util.Comparator;

/**
 * Save the particle along with its time for collision
 */
public class Crash implements Comparable<Crash> {
    private ObstacleParticle particle;
    private double time;

    Crash(ObstacleParticle p, double time){
        this.particle   = p;
        this.time       = time;
    }

    public ObstacleParticle getParticle() {
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
