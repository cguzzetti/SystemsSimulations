package ar.edu.itba.ss.g9.commons;

import ar.edu.itba.ss.g9.commons.simulation.Particle;

import java.util.ArrayList;

public class DynamicFile extends InputFile {

    private ArrayList<Particle[]> particlesOverTime;

    public DynamicFile(){
        this.particlesOverTime = new ArrayList<>();
    }

    public ArrayList<Particle[]> getParticles(){
        return particlesOverTime;
    }

    public void addParticle(Particle particle, int particleIndex){
        if(particleIndex == 0){
            this.particlesOverTime.add(new Particle[(int)this.getNumberOfParticles()+1]);
        }else{
            int currentTimeIndex = this.particlesOverTime.size() -1;
            this.particlesOverTime.get(currentTimeIndex)[particleIndex] = particle;
        }
    }
}