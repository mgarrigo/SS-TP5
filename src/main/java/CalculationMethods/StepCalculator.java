package CalculationMethods;

import models.Particle;

import java.util.List;

public interface StepCalculator {

    // TODO: Hacer un metodo List<Particle> updateParticles(List<Particle> particles, CellGrid Cellgrid)

    List<Particle> updateParticles(List<Particle> particles);

}
