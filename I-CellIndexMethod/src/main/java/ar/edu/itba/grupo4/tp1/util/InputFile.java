package ar.edu.itba.grupo4.tp1.util;

import ar.edu.itba.grupo4.tp1.Particle;

import java.util.ArrayList;

public class InputFile {
    private long numberOfParticles;
    private long areaSideLength;
    private ArrayList<Particle> particles;

    InputFile(){
        this.particles = new ArrayList<>();
    }

    public void setNumberOfParticles(long numberOfParticles){
        this.numberOfParticles = numberOfParticles;
    }

    public void setAreaSideLength(long areaSideLength){
        this.areaSideLength = areaSideLength;
    }

    public void addParticle(Particle particle){
        this.particles.add(particle);
    }

    public long getNumberOfParticles(){
        return this.numberOfParticles;
    }

    public long getAreaSideLength(){
        return this.areaSideLength;
    }

    public ArrayList<Particle> getParticles(){
        return this.particles;
    }


}
