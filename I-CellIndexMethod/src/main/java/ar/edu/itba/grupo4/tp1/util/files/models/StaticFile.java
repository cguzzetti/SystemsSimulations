package ar.edu.itba.grupo4.tp1.util.files.models;

import ar.edu.itba.grupo4.tp1.Particle;

import java.util.ArrayList;

public class StaticFile extends InputFile{

    private ArrayList<Particle> particles;

    public StaticFile(){
        this.particles = new ArrayList<>();
    }

    public void addParticle(Particle particle){
        this.particles.add(particle);
    }

    public ArrayList<Particle> getParticles(){
        return this.particles;
    }


}
