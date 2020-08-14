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

/*
    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @BenchmarkMode(Mode.AverageTime)
    public void bruteForce(Blackhole blackhole){
        ArrayList<Particle> particles = new ArrayList<>();

        for(int i =0; i< 100; i++){
            ThreadLocalRandom rnd = ThreadLocalRandom.current();
            particles.add(new Particle(
                    rnd.nextDouble(0, 20),
                    rnd.nextDouble(0, 20),
                    0.25,
                    i,
                    String.format("p%d", i)
            ));
        }
        BruteForce bf = new BruteForce(particles, 1);
        blackhole.consume(bf);

    }
*/


    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @BenchmarkMode(Mode.AverageTime)
    @Warmup(iterations = 5)
    @Measurement(iterations = 5)
    public void cellIndexMethod(Blackhole blackhole){
        int N = 100;
        long L = 20;
        boolean periodic = false;
        double rc = 1;
        Integer M=9;
        ArrayList<Particle> particles = new ArrayList<>();

        for(int i =0; i< 100; i++){
            ThreadLocalRandom rnd = ThreadLocalRandom.current();
            particles.add(new Particle(
                    rnd.nextDouble(0, L),
                    rnd.nextDouble(0, L),
                    0.25,
                    i,
                    String.format("p%d", i)
            ));
        }
        CellIndexMethod cim = new CellIndexMethod(N, (double) L, M, particles, periodic, rc);
        blackhole.consume(cim);
    }


}
