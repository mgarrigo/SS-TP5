package Silo;

import CalculationMethods.StepCalculator;
import models.Particle;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by Mariano on 30/9/2018.
 */
public class SiloSimulator implements Callable {

	private Double width;
	private Double height;
	private Double timeLimit;
	private Double timeStep;
	private Double animationTimeStepMultiplier;
	private Double minRadius;
	private Double maxRadius;
	private Double mass;
	private Integer maxParticles;

	private StepCalculator stepCalculator;


	@Override
	public Object call() throws Exception {

		List<Particle> particles = new LinkedList<>();

		// TODO: Generate map or walls

		double currentTime = 0.0;

		while(currentTime <= timeLimit) {
			if (currentTime % timeStep * animationTimeStepMultiplier == 0) {
				// TODO: Save animation data
			}

			if (particles.size() < maxParticles) {
				// TODO: generate particles on top layer, if they fit.
			}

			stepCalculator.updateParticles(particles);

			// TODO: Create new CellGrid with particles and remove particles outside.

			currentTime += timeStep;
		}

		// Particle Generator -> Create particles

		//



		return null;
	}
}
