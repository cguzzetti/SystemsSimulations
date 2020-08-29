package ar.edu.itba.grupo9.tp2;

import ar.edu.itba.grupo9.tp1.Particle;
import ar.edu.itba.grupo9.tp1.util.CellIndexMethod;
import ar.edu.itba.grupo9.tp1.util.Config;
import org.apache.commons.math3.analysis.function.Exp;

import javax.swing.text.html.Option;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.Buffer;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static ar.edu.itba.grupo9.tp1.util.files.FileParser.*;

public class OffLatticeAutomata {

    private List<Particle> particles;
    private double eta;
    private double deltaT;
    private Integer N;
    private Double L;
    private Double v;
    private final Optional<BufferedWriter> experimentWriter;
    private final ExperimentType experimentType;
    private List<Double> dataAccumulator;

    public OffLatticeAutomata(){
        this.experimentWriter = Optional.empty();
        this.experimentType = ExperimentType.NONE;
    }
    public OffLatticeAutomata(BufferedWriter writer, ExperimentType type){
        this.experimentWriter = Optional.of(writer);
        this.dataAccumulator = new ArrayList<>();
        this.experimentType = type;
    }

    public void runSimulation(Integer timeLapse, double eta, double deltaT, Integer N, Double L, Integer M, Double v, List<Particle> particles, double rc, double maxRadius1, double maxRadius2, Config config, LatticeInput input) throws IOException{
        this.particles = particles;
        this.eta = eta;
        this.deltaT = deltaT;
        this.N = N;
        this.L = L;
        this.v = v;

        CellIndexMethod cim = new CellIndexMethod(N, L, M, this.particles, true, rc, maxRadius1, maxRadius2);

        cim.CellIndexMethodRun(this.particles);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(config.getOutputFileName()));
            printHeadertoFile(input, config, writer, timeLapse);
            printParticlesInTimeToFile(input, config, 0, writer);
            for (int i = 1; i <= timeLapse; i++) {
                updateParticles();
                cim.CellIndexMethodRun(this.particles);
                printHeadertoFile(input, config, writer, timeLapse);
                printParticlesInTimeToFile(input, config, i, writer);
                this.updateExperimentFile(i);
            }
            this.printStatistics();
            writer.close();
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
        double atan2 = Math.atan2(avgSin, avgCos);
        return atan2;
    }

    private double calculateVa() {
        return Math.hypot(this.particles.stream().mapToDouble(Particle::getVx).sum(), this.particles.stream().mapToDouble(Particle::getVy).sum()) / (this.N*this.v);
    }

    private void updateExperimentFile(int currentTime){
        switch(this.experimentType){
            case NOISE:
                if(currentTime>= 1500)
                    this.dataAccumulator.add(calculateVa());
                break;
            case DENSITY:
                if(currentTime >= 1500)
                    System.out.println("Not yet implemented");
                    //Do Something
                break;
            case NONE:
            default:
                break;

        }
    }

    private void printStatistics() throws IOException{
        if(this.experimentType == ExperimentType.NONE)
            return;

        System.out.println(String.format("eta: %.3f", this.eta));
        double mean = this.dataAccumulator.stream().mapToDouble(Double::doubleValue).average().orElse(-1);
        if(mean == -1){
            System.out.println("Oops, mean == -1");
            return;
        }

        double variance = this.dataAccumulator.stream().map(i -> i - mean).map(i -> i*i).mapToDouble(Double::doubleValue).average().orElse(-1);

        if(mean == -1){
            System.out.println("Oops, variance == -1");
            return;
        }
        if(this.experimentType.equals(ExperimentType.NOISE)){
            this.experimentWriter.get().write(String.format(
                    "%.2f %.3f %.3f\n", this.eta, mean, Math.sqrt(variance)
            ));
        }else{
            System.out.println("Not implmemented");
        }


    }

}
