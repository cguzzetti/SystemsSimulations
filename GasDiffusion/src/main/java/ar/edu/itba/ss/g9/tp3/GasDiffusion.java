package ar.edu.itba.ss.g9.tp3;

import ar.edu.itba.ss.g9.commons.simulation.*;
import javafx.geometry.Point2D;


import java.io.IOException;
import java.util.*;

import static ar.edu.itba.ss.g9.tp3.GasDiffusionFileParser.getFilePath;

public class GasDiffusion {
   // private static Logger logger = LoggerFactory.getLogger(GasDiffusionFileParser.class);
    private final int N;
    private final double height;
    private final double width;
    private final double partitionLen;
    private Queue<Collision> collisions;
    private Set<GasParticle> particles;
    private Point2D[][] verticalWalls;
    private Point2D[][] horizontalWalls;
    private double particlesMass;
    private double particlesSpeed;
    private double currentTime;
    private static final double MAX_TIME = 10;
    private static final double BOLTZMANN = 1.38066e-23;
    private GasMetricsEngine metricsEngineGAS;

    public GasDiffusion(GasDifussionConfig config, Set<GasParticle> particles){
        this.N = config.getN();
        this.height = config.getHeight();
        this.width = config.getWidth();
        this.partitionLen = config.getPartitionLen();
        this.collisions = new PriorityQueue<>(new Comparator<Collision>() {
            @Override
            public int compare(Collision o1, Collision o2) {
                return Double.compare(o1.getTime(),o2.getTime());
            }
        });
        this.particles = particles;
        this.particlesMass = config.getParticleMass();
        this.particlesSpeed = config.getparticleSpeed();
        this.horizontalWalls = new Point2D[][]{
                {Point2D.ZERO, new Point2D(config.getWidth(), 0)},
                {new Point2D(0, config.getHeight()), new Point2D(config.getWidth(), config.getHeight())},
        };
        this.verticalWalls = new Point2D[][]{
                {Point2D.ZERO, new Point2D(0, config.getHeight())},
                {new Point2D(config.getWidth() / 2, 0), new Point2D(config.getWidth() / 2, config.getHeight() / 2 - config.getPartitionLen() / 2)},
                {new Point2D(config.getWidth() / 2, config.getHeight() / 2 + config.getPartitionLen() / 2), new Point2D(config.getWidth() / 2, config.getHeight())},
                {new Point2D(config.getWidth(), 0), new Point2D(config.getWidth(), config.getHeight())}
        };
        this.currentTime = 0.0;
    }

    public void setMetricsEngineGAS(GasMetricsEngine metricsEngineGAS) {
        this.metricsEngineGAS = metricsEngineGAS;
    }

    public void simulate(GasDiffusionFileParser parser){

        GasMetricsEngine metricsEngineFP = new GasMetricsEngine(
                ExperimentType.FP, getFilePath("fpExperiment.txt")
        );

        metricsEngineFP.writeIterationHeader();

        double maxTime = 0;
        int iteration = 0;
        double totalPressure = 0;

        calculateCollisions(this.particles, currentTime);
        if(collisions.isEmpty()) return;

        parser.writeStateToOutput(particles, iteration++);

        double fp = calculateParticleFraction();

        while( !inEquilibrium(fp) || currentTime < maxTime ){
            if(collisions.isEmpty()) {
                //logger.error("No more collisions to show!");
                System.out.printf("No more collisions to show! Exiting at t=%f\n", currentTime);
                break;
            }

            Optional<Collision> maybeCollision = getCollisionIfValid(collisions.poll());
            if(maybeCollision.isEmpty()) continue;
            Collision collision = maybeCollision.get();

            if(inEquilibrium(fp)) {
                totalPressure += collision.getPressure();
            }

            Set<GasParticle> particlesInCollision = collision.getParticles();

            // Time calculation
            // * We need deltaT in order to advance all particles
            // * We need to update the current time before
            //   calculating future collisions of particles
            //   involved in current collision
            double deltaT = collision.getTime() - currentTime;
            currentTime += deltaT;

            metricsEngineFP.writeFP(1 - fp, currentTime);

            particlesInCollision.forEach(GasParticle::incrementCollisionCounter); // Increase collision counter for particles involved in current collision
            advanceParticles(deltaT); // Advance all particles
            collision.updateVelocity(); // Update velocity of particles involved in current collision
            calculateCollisions(particlesInCollision, currentTime); // Determine future collisions of particles involved in current collision

            parser.writeStateToOutput(particles, iteration);

            fp = calculateParticleFraction();
            if(inEquilibrium(fp) && maxTime == 0) {
                maxTime = currentTime + MAX_TIME;
            }
            iteration++;
        }

        parser.finish();
        metricsEngineFP.finalizeExperiment();

        if(metricsEngineGAS != null) {
            System.out.println(this.particlesMass);
            System.out.println(this.particlesSpeed);
            double totalEnergy = this.N * this.particlesMass * this.particlesSpeed * this.particlesSpeed /2;
            double p = totalPressure/MAX_TIME;
            metricsEngineGAS.writeGas(p,totalEnergy);
            System.out.println("P: "+ p);
            System.out.println("Energy: "+ totalEnergy);
            System.out.println("diff: "+ Math.abs(totalEnergy-p));
            System.out.println("****************");
        }
    }

    private double calculateTemperature() {
        return this.particles.stream()
                .mapToDouble(p -> p.getMass() * p.getVelocity().magnitude() * p.getVelocity().magnitude())
                .average()
                .getAsDouble()/(2*BOLTZMANN);
    }

    private boolean inEquilibrium(double fp) {
        return fp - 0.5 < 0.01;
    }

    // Calculates the next collision for all particles
     private void calculateCollisions(Set<GasParticle> particles, double timeSoFar) {
        for(GasParticle p: particles) {
            this.collisions.addAll(p.calculateParticleNextCollision(this.particles, this.verticalWalls, this.horizontalWalls, timeSoFar));
        }
    }

    private Optional<Collision> getCollisionIfValid(Collision collision){
        if(collision.isValid())
            return Optional.of(collision);

        return Optional.empty();
    }

    private void advanceParticles(double time) {
        for(GasParticle p: particles) {
            p.updateParticlePosition(time);
        }
    }

    private double calculateParticleFraction() {
        int particleCounterLeftSide = 0;
        for(Particle p: particles) {
            if(p. getX() < width/2)
                particleCounterLeftSide++;
        }
        return (double) particleCounterLeftSide /particles.size();
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
