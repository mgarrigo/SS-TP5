package CalculationMethods.ForceCalculators;

import CalculationMethods.ForceCalculator;
import CellIndexMethod.CellGrid;
import Silo.ParticleSpawner;
import models.Particle;
import models.Vector;
import models.Wall;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class SiloForceCalculator implements ForceCalculator {
    private List<Wall> walls;
    private static double Kn = 1E5; // 10^5 N/m
    private static double Kt = 2*Kn;
    private static double gamma = 100.0;
    private static double mu = 0.7;

    public SiloForceCalculator(List<Wall> walls) {
        this.walls = walls;
    }

    @Override
    public Vector calculateForce(Particle p, Collection<Particle> particles) {
        GranularMaterialForce granularMaterialForce = new GranularMaterialForce();

        return calculateForceWithWalls(p);
    }

    public Vector calculateForceWithWalls(Particle p) {
        Vector FnSum = new Vector();
        Vector FtSum = new Vector();

        for (Wall w : walls) {
            Vector normalVersor = w.getNormalVersor();
            Vector tangentialVersor = w.getTangentVersor();

            // Add normal force to summation
            double xi = p.getRadius() - w.distanceToParticle(p);
            if (xi < 0.0 ) continue; // not colliding with wall

            Vector velocityNormalProjected = p.getVelocity().projectionOnVector(normalVersor); //project relative velocity on normal versor
            Vector Fn = normalVersor.dot(Kn * xi - gamma * velocityNormalProjected.norm() * Math.signum(velocityNormalProjected.getY())); // normal versor of the wall is up

            FnSum = FnSum.add(Fn);

            // Add tangential force to summation
            Vector rrel = p.getVelocity();
            Vector Ft = tangentialVersor.dot( -Kt * xi * velocityNormalProjected.dot(tangentialVersor)); // -kt * ξ * [rrel x tversor] (versor tangencial)

            FtSum = FtSum.add(Ft);
        }
        return FnSum.add(FtSum);
    }
}
