package ar.edu.itba.ss.g9.tp5;

import java.awt.geom.Point2D;

public class Vector {
    public static Point2D add(Point2D point1, Point2D point2){
        return new Point2D.Double(point1.getX() + point2.getX(), point1.getY() + point2.getY());
    }

    public static Point2D scalarMultiplication(Point2D point, double alpha){
        return new Point2D.Double(point.getX() * alpha, point.getY() * alpha);
    }

    public static Point2D scalarDivsion(Point2D point, double alpha){
        return scalarMultiplication(point, 1/alpha);
    }

    public static Point2D subtract(Point2D point1, Point2D point2){
        return new Point2D.Double(point1.getX() - point2.getX(), point1.getY() - point2.getY());
    }

    public static double getNorm(Point2D point1) {
        return Math.sqrt(Math.pow(point1.getX(), 2) + Math.pow(point1.getY(), 2));
    }

    public static Point2D normalize(Point2D point1) {
        return scalarDivsion(point1, getNorm(point1));
    }

    public static double getDotProduct(Point2D point1, Point2D point2) {
        return point1.getX() * point2.getX() + point1.getY() * point2.getY();
    }
}
