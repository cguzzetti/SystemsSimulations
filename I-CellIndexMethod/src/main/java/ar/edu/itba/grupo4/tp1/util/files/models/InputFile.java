package ar.edu.itba.grupo4.tp1.util.files.models;



public class InputFile {
    private Integer numberOfParticles;
    private long areaSideLength;

    public void setNumberOfParticles(Integer numberOfParticles){
        this.numberOfParticles = numberOfParticles;
    }

    public void setAreaSideLength(long areaSideLength){
        this.areaSideLength = areaSideLength;
    }

    public Integer getNumberOfParticles(){
        return this.numberOfParticles;
    }

    public long getAreaSideLength(){
        return this.areaSideLength;
    }

}
