package ar.edu.itba.grupo4.tp1.util;

import ar.edu.itba.grupo4.tp1.Particle;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BenchmarkRunner {
    public static void main(String[] args) throws Exception {
        org.openjdk.jmh.Main.main(args);
    }


    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @BenchmarkMode(Mode.AverageTime)
    @Warmup(iterations = 5)
    @Measurement(iterations = 5)
    public void bruteForce(Blackhole blackhole){
        ArrayList<Particle> particles = new ArrayList<>();

        int N = 5000;
        double r = 0.25;
        double rc = 1;
        long L = 20;

        for(int i =0; i < N; i++){
            ThreadLocalRandom rnd = ThreadLocalRandom.current();
            particles.add(new Particle(
                    rnd.nextDouble(0, L),
                    rnd.nextDouble(0, L),
                    r,
                    i,
                    String.format("p%d", i)
            ));
        }
        BruteForce bf = new BruteForce(particles, rc);
        blackhole.consume(bf);

    }


/*
    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @BenchmarkMode(Mode.AverageTime)
    @Warmup(iterations = 5)
    @Measurement(iterations = 5)
    public void cellIndexMethod(Blackhole blackhole){
        int N = 5000;
        long L = 20;
        boolean periodic = true;
        double rc = 1;
        double r = 0.25;
        Integer M = 13;
        ArrayList<Particle> particles = new ArrayList<>();

        for(int i =0; i< N; i++){
            ThreadLocalRandom rnd = ThreadLocalRandom.current();
            particles.add(new Particle(
                    rnd.nextDouble(0, L),
                    rnd.nextDouble(0, L),
                    r,
                    i,
                    String.format("p%d", i)
            ));
        }
        CellIndexMethod cim = new CellIndexMethod(N, (double) L, M, particles, periodic, rc, r, r);
        blackhole.consume(cim);
    }

*/
}
