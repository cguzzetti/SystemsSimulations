package ar.edu.itba.grupo9.tp2;

import ar.edu.itba.grupo9.tp1.Particle;
import ar.edu.itba.grupo9.tp1.util.CellIndexMethod;
import ar.edu.itba.grupo9.tp1.util.Config;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ar.edu.itba.grupo9.tp1.util.files.FileParser.*;

public class OffLatticeAutomata {

    private List<Particle> particles;
    private double eta;
    private double deltaT;
    private Integer N;
    private Double L;

    public OffLatticeAutomata(Integer timeLapse, double eta, double deltaT, Integer N, Double L, Integer M, List<Particle> particles, double rc, double maxRadius1, double maxRadius2, Config config, LatticeInput input) {
        this.particles = particles;
        this.eta = eta;
        this.deltaT = deltaT;
        this.N = N;
        this.L = L;

        CellIndexMethod cim = new CellIndexMethod(N, L, M, this.particles, true, rc, maxRadius1, maxRadius2);

        cim.CellIndexMethodRun(this.particles);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(config.getOutputFileName()));
            printHeadertoFile(input, config, writer);
            for (int i = 1; i <= timeLapse; i++) {
                updateParticles();
                cim.CellIndexMethodRun(this.particles);
                System.out.println(particles);
                printParticlesInTimeToFile(input, config, i, writer);
            }
            writer.close();
        }catch (IOException ex){
            ex.printStackTrace();
            System.exit(1);
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
        Set<Particle> neighborsWithParticle = new HashSet<>(p.getNeighbors());
        neighborsWithParticle.add(p);
        double avgSin = neighborsWithParticle.stream().collect(Collectors.averagingDouble((particle -> Math.sin(particle.getDirection()))));
        double avgCos = neighborsWithParticle.stream().collect(Collectors.averagingDouble((particle -> Math.cos(particle.getDirection()))));
        double atan2 = Math.atan2(avgSin, avgCos);
        //System.out.println(atan2 > 0? atan2 : atan2 + 2*Math.PI);
        return atan2 > 0 ? atan2 : atan2 + 2*Math.PI;
    }

}
