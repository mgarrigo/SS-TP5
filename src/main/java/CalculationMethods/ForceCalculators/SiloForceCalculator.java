package CalculationMethods.ForceCalculators;

import CalculationMethods.ForceCalculator;
import models.Particle;
import models.Vector;

import java.util.Collection;

public class SiloForceCalculator implements ForceCalculator {
    @Override
    public Vector calculateForce(Particle p, Collection<Particle> particles) {
        GravityCalculator gravityCalculator = new GravityCalculator();
        GranularMaterialForce granularMaterialForce = new GranularMaterialForce();

        return gravityCalculator.calculateForce(p, particles)
                .add(granularMaterialForce.calculateForce(p, particles));
    }
}
