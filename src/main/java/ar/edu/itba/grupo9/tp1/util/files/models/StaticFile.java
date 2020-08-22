package ar.edu.itba.grupo9.tp1.util.files.models;

import ar.edu.itba.grupo9.tp1.Particle;
import ar.edu.itba.grupo9.tp1.util.Config;

import java.util.ArrayList;

public class StaticFile extends InputFile{

    private ArrayList<Particle> particles;
    private Double firstMaxRadius;
    private Double secondMaxRadius;

    public StaticFile(){
        this.particles = new ArrayList<>();
        this.firstMaxRadius  = 0.0;
        this.secondMaxRadius = 0.0;
    }

    public void addParticle(Particle particle){
        this.particles.add(particle);
    }

    public ArrayList<Particle> getParticles(){
        return this.particles;
    }

    public Integer getOptimalM(Config config, StaticFile file){
        // Avoid calculation if it was given as a parameter.
        if(config.getM() != null)
            return config.getM();

        final double L = file.getAreaSideLength();
        final double rc = config.getRc();
        final double maxR1 = file.getFirstMaxRadius(), maxR2 = file.getSecondMaxRadius();
        int M = 1;
        boolean foundM = false;
        while(!foundM){
            if(rc > (L/M - maxR1 - maxR2))
                foundM = true;
            else
                M++;
        }
        return M-1;
    }


    public Double getFirstMaxRadius() {
        return this.firstMaxRadius;
    }

    public Double getSecondMaxRadius() {
        return this.secondMaxRadius;
    }

    public void updateMaxRadius(double maybeNewRadius){
        if(maybeNewRadius > this.firstMaxRadius)
            this.firstMaxRadius = maybeNewRadius;
        else if (maybeNewRadius > this.secondMaxRadius)
            this.secondMaxRadius = maybeNewRadius;
    }
}
