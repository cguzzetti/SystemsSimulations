package ar.edu.itba.grupo4.tp1.util.files.models;


public class InputFile {
    private long numberOfParticles;
    private long areaSideLength;

    public void setNumberOfParticles(long numberOfParticles){
        this.numberOfParticles = numberOfParticles;
    }

    public void setAreaSideLength(long areaSideLength){
        this.areaSideLength = areaSideLength;
    }

    public long getNumberOfParticles(){
        return this.numberOfParticles;
    }

    public long getAreaSideLength(){
        return this.areaSideLength;
    }

}
