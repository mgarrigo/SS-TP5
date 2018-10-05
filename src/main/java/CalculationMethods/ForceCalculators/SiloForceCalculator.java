package CalculationMethods.ForceCalculators;

import CalculationMethods.ForceCalculator;
import models.Particle;
import models.Vector;
import models.Wall;

import java.util.Collection;
import java.util.List;

public class SiloForceCalculator implements ForceCalculator {
    private List<Wall> walls;
    private static double Kn = 1.16675E2; // 10^5 N/m
    private static double Kt = 2*Kn;

    public SiloForceCalculator(List<Wall> walls) {
        this.walls = walls;
    }

    @Override
    public Vector calculateForce(Particle p, Collection<Particle> particles) {
        GravityCalculator gravityCalculator = new GravityCalculator();
        GranularMaterialForce granularMaterialForce = new GranularMaterialForce();

        return gravityCalculator.calculateForce(p, particles)
                .add(granularMaterialForce.calculateForce(p, particles))
                .add(calculateForceWithWalls(p));
    }

    public Vector calculateForceWithWalls(Particle p) {
        Vector FnSum = new Vector();
        Vector FtSum = new Vector();

        for (Wall w : walls) {
            Vector normalVersor = w.getNormalVersor();
            Vector tangentialVersor = new Vector(-normalVersor.getY(), normalVersor.getX());

            // Add normal force to summation
            double xi = p.getRadius() - w.distanceToParticle(p);
            if (xi < 0.0 ) continue; // not colliding with wall

            Vector Fn = normalVersor.dot(Kn * xi); // -kn * ξ (versor normal)

            FnSum = FnSum.add(Fn);

            // Add tangential force to summation

            //TODO: esto no se si va asi, o al reves
            Vector rrel = p.getVelocity();
            Vector Ft = tangentialVersor.dot( -Kt * xi * rrel.dot(tangentialVersor)); // -kt * ξ * [rrel x tversor] (versor tangencial)

            FtSum = FtSum.add(Ft);
        }
        System.out.println(FnSum.add(FtSum));
        return FnSum.add(FtSum);
    }
}
