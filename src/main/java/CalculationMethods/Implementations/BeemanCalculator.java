package CalculationMethods.Implementations;

import CalculationMethods.ForceCalculator;
import CalculationMethods.StepCalculator;
import models.Particle;

import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

public class BeemanCalculator implements StepCalculator {

    private ForceCalculator forceCalculator;
    private Double deltaT;
    private List<Particle> previousState;

    public BeemanCalculator(ForceCalculator forceCalculator, Double deltaT, List<Particle> particles) {
        this.forceCalculator = forceCalculator;
        this.deltaT = deltaT;

        // Calculate previous initial state with Euler
        previousState = particles.stream().map(p -> {
            Vector a = forceCalculator.calculateAcceleration(p, particles);
            Vector prevPos = p.getPosition()
                    .add(p.getVelocity().dot(-deltaT))
                    .add(a.dot(deltaT * deltaT / 2.0));
            Vector prevVel = p.getVelocity().add(a.dot(-deltaT));

            Particle tempParticle = p.getCopyWithVelocity(prevVel).getCopyWithPosition(prevPos);
            Vector prevAcc = forceCalculator.calculateAcceleration(tempParticle, particles);
            return tempParticle.getCopyWithAcceleration(prevAcc);
        }).collect(Collectors.toList());
    }

    @Override
    public List<Particle> updateParticles(List<Particle> particles) {

        List<Particle> predictedParticles = particles.stream().map(p -> {
            Particle previousParticleState = previousState.get(previousState.indexOf(p));
            Vector currAcc = forceCalculator.calculateAcceleration(p, particles);
            Vector nextPos = p.getPosition()
                    .add(p.getVelocity().dot(deltaT))
                    .add(currAcc.dot(deltaT * deltaT * (2.0 / 3.0)))
                    .substract(previousParticleState.getAcceleration().dot(1.0 / 6.0 * deltaT * deltaT));
            Vector predictedVel = p.getVelocity()
                    .add(currAcc.dot(3.0 / 2.0 * deltaT))
                    .substract(previousParticleState.getAcceleration().dot(1.0 / 2.0 * deltaT));

            return p.getCopyWithAcceleration(currAcc).getCopyWithVelocity(predictedVel).getCopyWithPosition(nextPos);
        }).collect(Collectors.toList());

        List<Particle> nextParticles = particles.stream().map(p -> {
            Particle previousParticleState = previousState.get(previousState.indexOf(p));
            Particle predictedParticleState = predictedParticles.get(predictedParticles.indexOf(p));

            Vector prevAcc = previousParticleState.getAcceleration();
            Vector currAcc = predictedParticleState.getAcceleration();
            Vector predictedAcc = forceCalculator.calculateAcceleration(predictedParticleState, predictedParticles); // Acceleration using predicted velocity

            Vector correctedVelocity = p.getVelocity()
                    .add(predictedAcc.dot(1.0 / 3.0 * deltaT))
                    .add(currAcc.dot(5.0 / 6.0 * deltaT))
                    .substract(prevAcc.dot(1.0 / 6.0 * deltaT));

            return predictedParticleState.getCopyWithVelocity(correctedVelocity).getCopyWithAcceleration(predictedAcc);
        }).collect(Collectors.toList());

        previousState = particles;

        return nextParticles;

    }
}
