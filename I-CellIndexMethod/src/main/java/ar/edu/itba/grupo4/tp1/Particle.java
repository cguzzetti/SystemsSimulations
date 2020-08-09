package ar.edu.itba.grupo4.tp1;

public class Particle {
    private double x;
    private double y;
    private int radius;
    private String name;
    private int id;

    private final boolean hasVelocity;
    private double velocityX;
    private double velocityY;

    public Particle(double xPosition, double yPosition, int radius, int id, String name){
        this.x      = xPosition;
        this.y      = yPosition;
        this.radius = radius;
        this.hasVelocity = false;
        this.id = id;
        this.name = name;
    }

    public Particle(double xPosition, double yPosition, int radius, int id){
        this.x      = xPosition;
        this.y      = yPosition;
        this.radius = radius;
        this.hasVelocity = false;
        this.id = id;
    }

    public Particle(double xPosition, double yPosition, double velocityX, double velocityY, int radius, int id){
        this.x      = xPosition;
        this.y      = yPosition;
        this.radius = radius;
        this.hasVelocity = true;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.id = id;
    }

    public void setX(double x){
        this.x = x;
    }

    public void setY(double y){
        this.y = y;
    }

    public void setRadius(int radius){
        this.radius = radius;
    }

    public void setVelocityX(double velocityX) {
        this.velocityX = velocityX;
    }

    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }

    public String getName() {
        return this.name;
    }

    public double getX(){
        return this.x;
    }

    public double getY(){
        return this.y;
    }

    public int getRadius(){
        return this.radius;
    }

    public int getId(){
        return this.id;
    }

    public double getVelocityX(){
        return this.velocityX;
    }
    public double getVelocityY(){
        return this.velocityY;
    }

    public boolean variesOverTime(){
        return this.hasVelocity;
    }

    @Override
    public String toString(){

        if(!hasVelocity)
            return String.format(
                    "Particle: (x: %.3f, y: %.3f, radius: %d)",
                    this.x, this.y, this.radius
            );

        return String.format(
                "Particle (x: %.3f, y: %.3f, vx: %.3f, vy: %.3f, radius: %d)",
                this.x, this.y, this.velocityX, this.velocityY, this.radius
        );
    }
}
