package Silo;

import CalculationMethods.StepCalculator;
import CellIndexMethod.CellGrid;
import models.Particle;


import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

/**
 * Created by Mariano on 30/9/2018.
 */
public class SiloSimulator implements Callable {

	public static Random random = new Random();

	private Double width;
	private Double height;
	private Double cellSize;
	private Double timeLimit;
	private Double timeStep;
	private Double animationTimeStepMultiplier;
	private Double minRadius;
	private Double maxRadius;
	private Double mass;
	private Integer maxParticles;

	private StepCalculator stepCalculator;


	public SiloSimulator(Double width, Double height, Double cellSize, Double timeLimit, Double timeStep,
						 Double animationTimeStepMultiplier, Double minRadius, Double maxRadius, Double mass,
						 Integer maxParticles, StepCalculator stepCalculator) {
		this.width = width;
		this.height = height;
		this.cellSize = cellSize;
		this.timeLimit = timeLimit;
		this.timeStep = timeStep;
		this.animationTimeStepMultiplier = animationTimeStepMultiplier;
		this.minRadius = minRadius;
		this.maxRadius = maxRadius;
		this.mass = mass;
		this.maxParticles = maxParticles;
		this.stepCalculator = stepCalculator;
	}

	@Override
	public Object call() throws Exception {

		List<Particle> particles = new LinkedList<>();
		CellGrid cellGrid = new CellGrid(width, height + cellSize, cellSize);

		// TODO: Generate map or walls

		double currentTime = 0.0;
		int id = 0;

		while(currentTime <= timeLimit) {
			if (currentTime % timeStep * animationTimeStepMultiplier == 0) {
				// TODO: Save animation data
			}

			cellGrid.addParticles(particles);

			// Removes the particles outside the map.
			List<Particle> fallen = cellGrid.getFallenParticles();
			particles.removeAll(fallen);

			// Generates the particles on top of the silo, if they fit.
			// TODO: ACA LAS PARTICULAS SE TRATAN DE INSERTAR SIEMPRE HASTA LLEGAR AL MAXIMO. PREGUNTAR SI SE PUEDE HACER ASI O SE NECESITA QUE APAREZCA YA EL SILO LLENO AL EMPEZAR LA SIMULACION.
			while(particles.size() < maxParticles) {
				Double radius = random.nextDouble() * (maxRadius - minRadius) + minRadius;
				Particle particle = ParticleSpawner.spawnParticleOnTop(cellGrid, id, radius, mass);
				if (particle == null) break;
				System.out.println("Created Particle " + particle);
				particles.add(particle);
				id++;
			}

			stepCalculator.updateParticles(particles);

			cellGrid.clear();

			currentTime += timeStep;
		}



		return null;
	}
}
