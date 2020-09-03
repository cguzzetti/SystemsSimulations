package ar.edu.itba.ss.g9.commons;

import ar.edu.itba.ss.g9.commons.simulation.Particle;

import java.util.ArrayList;

public class InputFile {
    private Integer numberOfParticles;
    private Double areaSideLength;

    public void setNumberOfParticles(Integer numberOfParticles){
        this.numberOfParticles = numberOfParticles;
    }

    public void setAreaSideLength(Double areaSideLength){
        this.areaSideLength = areaSideLength;
    }

    public Integer getNumberOfParticles(){
        return this.numberOfParticles;
    }

    public Double getAreaSideLength(){
        return this.areaSideLength;
    }

}