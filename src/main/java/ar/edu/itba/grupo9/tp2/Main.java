package ar.edu.itba.grupo9.tp2;

import ar.edu.itba.grupo9.tp1.Particle;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
    public static void main(String[] args){
        System.out.println("Hello World!");

        ArrayList<Particle> particles = new ArrayList<>();

        for(int i =0; i < 10; i++){
            ThreadLocalRandom rnd = ThreadLocalRandom.current();
            particles.add(new Particle(
                    rnd.nextDouble(0, 10),
                    rnd.nextDouble(0, 10),
                    0,
                    i,
                    String.format("p%d", i)
            ));
        }

        OffLatticeAutomata ola = new OffLatticeAutomata(5,10,(double) 10,5, particles,1,0,0);

    }
}
