package CalculationMethods.ForceCalculators;

import CalculationMethods.ForceCalculator;
import models.Particle;
import models.Vector;

import java.util.Collection;

public class GravityCalculator implements ForceCalculator {
    private static double gravity = 9.80665; // m/s^2

    @Override
    public Vector calculateForce(Particle p, Collection<Particle> particles) {
        return new Vector(0.0, -gravity).dot(p.getMass());
    }
}
