package ar.edu.itba.grupo4.tp1;

public class Particle {
    private double x;
    private double y;
    private int radius;

    public Particle(double xPosition, double yPosition, int radius){
        this.x      = xPosition;
        this.y      = yPosition;
        this.radius = radius;
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

    public double getX(){
        return this.x;
    }

    public double getY(){
        return this.y;
    }

    public int getRadius(){
        return this.radius;
    }

    @Override
    public String toString(){
        return String.format(
                "Particle: (x: %.3f, y: %.3f, radius: %d)",
                this.x, this.y, this.radius
        );
    }
}
