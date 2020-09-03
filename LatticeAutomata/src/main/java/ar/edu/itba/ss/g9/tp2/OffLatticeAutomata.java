package ar.edu.itba.ss.g9.tp2;

import ar.edu.itba.ss.g9.commons.Config;
import ar.edu.itba.ss.g9.commons.simulation.Particle;
import ar.edu.itba.ss.g9.tp1.CellIndexMethod;
import ar.edu.itba.ss.g9.tp2.LatticeFileParser;
import ar.edu.itba.ss.g9.tp2.LatticeInput;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static ar.edu.itba.ss.g9.tp2.LatticeFileParser.*;
import static ar.edu.itba.ss.g9.tp2.Main.VISUALIZATION_PATH;


public class OffLatticeAutomata {
    private List<Particle> particles;
    private double eta;
    private double deltaT;
    private Integer N;
    private Double L;
    private Double v;

    public OffLatticeAutomata(){ }

    public void runSimulation(Integer timeLapse, double eta, double deltaT, Integer N, Double L, Integer M, Double v, List<Particle> particles, double rc, double maxRadius1, double maxRadius2, Config config, LatticeInput input, Integer repetitionNumber) throws IOException {
        this.particles = particles;
        this.eta = eta;
        this.deltaT = deltaT;
        this.N = N;
        this.L = L;
        this.v = v;

        CellIndexMethod cim = new CellIndexMethod(N, L, M, this.particles, true, rc, maxRadius1, maxRadius2);

        cim.CellIndexMethodRun(this.particles);
        try {
            BufferedWriter writerStates = new BufferedWriter(new FileWriter(config.getOutputFileName(VISUALIZATION_PATH)+"States"+repetitionNumber+".xyz"));
            BufferedWriter writerVa = new BufferedWriter(new FileWriter(config.getOutputFileName(VISUALIZATION_PATH)+"Va"+repetitionNumber+".txt"));
            printHeadertoFile(input, config, writerStates, timeLapse);
            LatticeFileParser.printHeadertoVaFile(writerVa, timeLapse, N, L, eta);
            printParticlesInTimeToFile(input, config, 0, writerStates);
            printVaInTimeToFile(calculateVa(), writerVa);
            for (int i = 1; i <= timeLapse; i++) {
                updateParticles();
                cim.CellIndexMethodRun(this.particles);
                printHeadertoFile(input, config, writerStates, timeLapse);
                printParticlesInTimeToFile(input, config, i, writerStates);
                printVaInTimeToFile(calculateVa(), writerVa);
            }
            writerVa.close();
            writerStates.close();
        }catch (IOException ex){
            ex.printStackTrace();
            System.exit(1);
        }
    }

    private void updateParticles() {
        double x, y, noise, atan2, direction;
        for(Particle p: this.particles) {
            ThreadLocalRandom rnd = ThreadLocalRandom.current();

            noise = (this.eta == 0)? 0 : rnd.nextDouble(-this.eta/2, this.eta/2);
            atan2 = calculateDirectionWithNeighbors(p);
            direction = atan2 + noise;
            if(direction < -Math.PI)
                direction += 2*Math.PI;
            if(direction > Math.PI)
                direction -= 2*Math.PI;
            p.setDirection(direction);
            x = p.getX() + p.getSpeed() * Math.cos(direction) * this.deltaT;
            if(x<0)
                x += L;
            if(x>=L)
                x -= L;
            y = p.getY() + p.getSpeed() * Math.sin(direction) * this.deltaT;
            if(y<0)
                y +=L;
            if(y>=L)
                y -= L;
            p.setX(x);
            p.setY(y);
        }
    }

    private double calculateDirectionWithNeighbors(Particle p) {
        Set<Particle> neighborsWithParticle = new HashSet<>(p.getNeighbors());
        neighborsWithParticle.add(p);
        double avgSin = neighborsWithParticle.stream().collect(Collectors.averagingDouble((particle -> Math.sin(particle.getDirection()))));
        double avgCos = neighborsWithParticle.stream().collect(Collectors.averagingDouble((particle -> Math.cos(particle.getDirection()))));
        return Math.atan2(avgSin, avgCos);
    }

    private double calculateVa() {
        return Math.hypot(this.particles.stream().mapToDouble(Particle::getVx).sum(), this.particles.stream().mapToDouble(Particle::getVy).sum()) / (this.N*this.v);
    }

}