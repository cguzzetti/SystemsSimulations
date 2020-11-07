package ar.edu.itba.ss.g9.tp5;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static java.lang.Math.sqrt;
import static java.lang.Math.pow;
public class PredictiveCollisionAvoidance {

    // Personal space valid range: [1.2m, 3.6m]
    private static final double PERSONAL_SPACE      = 1.2;
    // Take the first 5 as seen in class
    private static final int NUMBER_OF_CRASHES      = 5;
    // Velocity desired by pedestrian
    private static final double DESIRED_VELOCITY    = 1.3;
    private static final double TAU                 = 0.5;

    // The pedestrian always moves towards the goal
    public static Point2D applyAutopropulsiveForce(Particle p, Point2D goal){
        double auxX = goal.getX() - p.getPosX();
        double auxY = goal.getY() - p.getPosY();

        double distanceToGoal = sqrt(pow(auxX, 2) + pow(auxY, 2));

        double xValue = (DESIRED_VELOCITY * auxX/distanceToGoal - p.getVx()) / TAU;
        double yValue = (DESIRED_VELOCITY * auxY/distanceToGoal - p.getVy()) / TAU;

        return new Point2D.Double(xValue, yValue);
    }

    public static Point2D applyElusiveForce(Point2D force, Particle p, Set<ObstacleParticle> otherParticles, double deltaT){
        List<Crash> crashes = new ArrayList<>();
        otherParticles.parallelStream().forEach((otherP)-> {
            double crashTime = predictCrashTime(p, otherP); //TODO Change
            double dij = Math.abs(p.getDistanceFrom(otherP));
            if(crashTime >= 0 && dij <= PERSONAL_SPACE + otherP.getRadius())
                crashes.add(new Crash(otherP, crashTime));
        });

        Collections.sort(crashes);

        boolean noCrashesLeft = false;
        // We only want the first {NUMBER_OF_CRASHES} crashes
        for(int i = 0; i < NUMBER_OF_CRASHES && i < crashes.size() && !noCrashesLeft;  i++){
            noCrashesLeft = true;
            Crash crash = crashes.get(i);
            Point2D evasiveForce = evade(p, crash.getParticle(), crash.getTime());
            // We want the avg of the force
            evasiveForce.setLocation(evasiveForce.getX() * (1.0/i+1), evasiveForce.getY() * (1.0/i+1));

            force.setLocation(force.getX() + evasiveForce.getX(), force.getY() + evasiveForce.getY());
            setDummyValues(p, force, deltaT);
            for (int j = i + 1; j < crashes.size() && noCrashesLeft; j++){
                noCrashesLeft = (crashTest(p, crashes.get(j).getParticle()) == -1);
            }
        }


        return force;
    }

    private static void setDummyValues(Particle p, Point2D force, double deltaT){
        p.setDummyVx(p.getVx() + force.getX() * deltaT);
        p.setDummyVy(p.getVy() + force.getY() * deltaT);
    }

    private static double crashTest(Particle p, Particle otherP){

        double xDifference = otherP.getPosX() - p.getPosX();
        double yDifference = otherP.getPosY() - p.getPosY();

        double xSpeedDifference = otherP.getVx() - p.getDummyVx();
        double ySpeedDifference = otherP.getVy() - p.getDummyVy();

        double XdotV = xSpeedDifference*xDifference + ySpeedDifference*yDifference;

        if(XdotV >= 0 ){
            return -1;
        }

        double modV = pow(xSpeedDifference,2) + pow(ySpeedDifference,2);
        double modX = pow(xDifference, 2) + pow(yDifference, 2);

        double d = XdotV*XdotV - modV*(modX - pow(PERSONAL_SPACE + otherP.getRadius(), 2));
        if(d < 0 ){
            return -1;
        }
        double dSquared = Math.sqrt(d);

        return -(XdotV + dSquared)/modV;
    }

    private static double predictCrashTime(Particle p, Particle otherP){
        double xDiff = otherP.getPosX() - p.getPosX();
        double yDiff = otherP.getPosY() - p.getPosY();

        double vxDiff = otherP.getVx() - p.getVx();
        double vyDiff = otherP.getVy() - p.getVy();

        double XdotV = xDiff*vxDiff + yDiff * vyDiff;

        if( XdotV >= 0){
            // Collision won't happen
            return -1;
        }

        double modulusV = pow(vxDiff,2) + pow(vyDiff, 2);
        double modulusX = pow(xDiff, 2) + pow(yDiff, 2);

        double d = pow(XdotV, 2) - modulusV *(modulusX - pow((PERSONAL_SPACE + otherP.getRadius()), 2));
        double dSquared = sqrt(d);
        if(d < 0 ) {
            return -1;
        }

        return -(XdotV + dSquared)/ modulusV;


    }

    private static Point2D evade(Particle p, Particle otherP, double time){

        // Apply avoidance maneuver
        double cix = p.getPosX() +  p.getVx() * time;
        double ciy = p.getPosY() + p.getVy() * time;
        double cjx = otherP.getPosX() + otherP.getVx() * time;
        double cjy = otherP.getPosY() + otherP.getVy() * time;

        double xDifference = cix - cjx;
        double yDifference = ciy - cjy;
        // ||c_i - c_j ||
        double distance = sqrt(pow(xDifference, 2) + pow(yDifference, 2));


        double rx = cix - p.getPosX();
        double ry = ciy - p.getPosY();

        // || c_i - x_i ||
        double modR = sqrt(pow(rx, 2) + pow(ry, 2));

        //TODO: Is this OK? Or should it be this.getRadius()
        final double D = modR + (distance - PERSONAL_SPACE - otherP.getRadius());

        /*
            We need to specify 3 values: d_min d_max and d_mid
            For more information check page 47 of
            https://www.researchgate.net/publication/221252067_A_Predictive_Collision_Avoidance_Model_for_Pedestrian_Simulation
            (this is part of the course lecture, the teacher should've given you the PDF).
            This also corresponds to slide 14 in Theory7
            We define the first decreasing part with D < d_min as: 1/D
            Te constant part, we define it as: 1/d_min
            The last segment, with d_mid < D < d_max: d_max/(d_min * (d_max - d_mid)) - D/(d_min*(d_max - d_mid))

         */
        double dmid = 1;
        double dmax = 2;
        double dmin = p.getRadius();

        // Get the Unit Vectors to specify direction
        double xDirection = xDifference / distance;
        double yDirection = yDifference / distance;

        if(D > dmax) {
            return new Point2D.Double(0,0);
        }
        else if(D > dmid) {
            double forceMagnitude = dmax/(dmin*(dmax - dmid)) - D/(dmin*(dmax - dmid));
            return new Point2D.Double(forceMagnitude*xDirection,forceMagnitude*yDirection);
        }
        else if(D > dmin) {
            double forceMagnitude = (1/dmin);
            return new Point2D.Double(forceMagnitude*xDirection,forceMagnitude*yDirection);
        } else {
            double forceMagnitude = (1/D);
            return new Point2D.Double(forceMagnitude*xDirection,forceMagnitude*yDirection);
        }

    }
}
