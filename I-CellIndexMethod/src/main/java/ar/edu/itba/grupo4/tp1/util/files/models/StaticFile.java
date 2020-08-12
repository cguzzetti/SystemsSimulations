package ar.edu.itba.grupo4.tp1.util.files.models;

import ar.edu.itba.grupo4.tp1.Particle;

import java.util.ArrayList;

public class StaticFile extends InputFile{

    private ArrayList<Particle> particles;
    private Integer maxRadius;

    public StaticFile(){
        this.particles = new ArrayList<>();
        this.maxRadius = -1;
    }

    public void addParticle(Particle particle){
        this.particles.add(particle);
    }

    public ArrayList<Particle> getParticles(){
        return this.particles;
    }

    public Integer getOptimalM(){
        final double L = this.getAreaSideLength();
        final double rc = 0.9;
        final double bound = L /(rc + 2*this.maxRadius);
        final double M = Math.floor(bound);
        return (int) Math.max(1, (M < bound? M: M-1 ));
    }


    public Integer getMaxRadius() {
        return maxRadius;
    }

    public void updateMaxRadius(Integer maybeNewRadius){
        if(maybeNewRadius > this.maxRadius)
            this.maxRadius = maybeNewRadius;
    }
}
