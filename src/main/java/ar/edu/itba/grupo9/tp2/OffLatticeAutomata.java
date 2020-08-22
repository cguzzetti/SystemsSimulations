package ar.edu.itba.grupo9.tp2;

import ar.edu.itba.grupo9.tp1.Particle;
import ar.edu.itba.grupo9.tp1.util.CellIndexMethod;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OffLatticeAutomata {

    private List<Particle> particles;
    private double eta;
    private double deltaT;
    private Integer N;
    private Double L;

    public OffLatticeAutomata(Integer timeLapse, double eta, double deltaT, Integer N, Double L, Integer M, List<Particle> particles, double rc, double maxRadius1, double maxRadius2) {
        this.particles = particles;
        this.eta = eta;
        this.deltaT = deltaT;
        this.N = N;
        this.L = L;

        CellIndexMethod cim = new CellIndexMethod(N, L, M, this.particles, true, rc, maxRadius1, maxRadius2);

        cim.CellIndexMethodRun(this.particles);

        for(int i = 1; i <= timeLapse ; i++) {
            updateParticles();
            cim.CellIndexMethodRun(this.particles);
            System.out.println(particles);
        }
    }

    private void updateParticles() {
        for(Particle p: this.particles) {
            ThreadLocalRandom rnd = ThreadLocalRandom.current();
            double noise = rnd.nextDouble(-this.eta/2, this.eta/2);
            double atan2 = calculateDirectionWithNeighbors(p);
            double direction = (atan2 + noise) > 0 ? ((atan2 + noise) < 2*Math.PI ? (atan2 + noise) : (atan2 + noise) - 2*Math.PI) : (atan2 + noise) + 2*Math.PI;
            p.setDirection(direction);
            p.setX(p.getX() + p.getSpeed() * Math.cos(direction) * this.deltaT);
            p.setY(p.getY() + p.getSpeed() * Math.sin(direction) * this.deltaT);
        }
    }

    private double calculateDirectionWithNeighbors(Particle p) {
        Set<Particle> neighborsWithParticle = p.getNeighbors();
        neighborsWithParticle.add(p);
        double avgSin = neighborsWithParticle.stream().collect(Collectors.averagingDouble((particle -> Math.sin(particle.getDirection()))));
        double avgCos = neighborsWithParticle.stream().collect(Collectors.averagingDouble((particle -> Math.cos(particle.getDirection()))));
        double atan2 = Math.atan2(avgSin, avgCos);
        //System.out.println(atan2 > 0? atan2 : atan2 + 2*Math.PI);
        return atan2 > 0 ? atan2 : atan2 + 2*Math.PI;
    }

}
