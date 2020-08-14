package ar.edu.itba.grupo4.tp1.util;

import ar.edu.itba.grupo4.tp1.Particle;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class BruteForce {

    public BruteForce(List<Particle> particles, double rc) {
        double distance;
        for(Particle p: particles) {
            if (particles.indexOf(p) < particles.size() - 1) {
                Iterator<Particle> it = particles.listIterator(particles.indexOf(p) + 1);
                while (it.hasNext()) {
                    Particle curr = it.next();
                    distance = p.getPoint().distance(curr.getPoint()) - p.getRadius() - curr.getRadius();
                    if (Double.compare(distance, rc) <= 0) {
                        p.addNeighbor(curr);
                    }
                }
            }
        }
    }
}
