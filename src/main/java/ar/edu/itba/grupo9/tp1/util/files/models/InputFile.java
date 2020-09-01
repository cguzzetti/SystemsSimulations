package ar.edu.itba.grupo9.tp1.util.files.models;



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
