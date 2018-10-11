package CalculationMethods.StepCalculators;

import CalculationMethods.ForceCalculator;
import CalculationMethods.StepCalculator;
import CellIndexMethod.CellGrid;
import models.Particle;
import models.Vector;

import java.util.List;
import java.util.stream.Collectors;

public class LeapFrogVelvetCalculator implements StepCalculator {

    private ForceCalculator forceCalculator;
    private Double deltaT;
    CellGrid cellGrid;

    public LeapFrogVelvetCalculator(ForceCalculator forceCalculator, Double deltaT, Double width, Double height, Double cellSize) {
        this.forceCalculator = forceCalculator;
        this.deltaT = deltaT;
        cellGrid = new CellGrid(width, height, cellSize);
    }

    @Override
    public List<Particle> updateParticles(List<Particle> particles) {


        cellGrid.addParticles(particles);
        List<Particle> updatedParticles = particles.parallelStream().map(p -> {
            List<Particle> neighbors = cellGrid.getAdjacentParticles(p);
            Vector a = forceCalculator.calculateAcceleration(p, neighbors);
            Vector halfVelocity = p.getVelocity().add(a.dot(deltaT / 2.0)); // v(0.5) = v(0) + a(0) * 0.5
            Vector pos = p.getPosition().add(halfVelocity.dot(deltaT)); // r(1) = p(0) + v(0.5) * 1

            return p.getCopyWithVelocity(halfVelocity)
                .getCopyWithPosition(pos)
                .getCopyWithAcceleration(a);
        }).collect(Collectors.toList());

        cellGrid.clear();
        cellGrid.addParticles(updatedParticles);
        return updatedParticles.parallelStream().map(particle -> {
            List<Particle> neighbors = cellGrid.getAdjacentParticles(particle);
            Vector a = forceCalculator.calculateAcceleration(particle, neighbors);
            Vector velocity = particle.getVelocity().add(a.dot(deltaT / 2.0));

            return particle.getCopyWithVelocity(velocity).getCopyWithAcceleration(a);
        }).collect(Collectors.toList());

    }

}
