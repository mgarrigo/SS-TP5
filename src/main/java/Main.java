import CalculationMethods.ForceCalculators.GravityCalculator;
import CalculationMethods.ForceCalculators.SiloForceCalculator;
import CalculationMethods.StepCalculator;
import CalculationMethods.StepCalculators.BeemanCalculator;
import CalculationMethods.StepCalculators.LeapFrogVelvetCalculator;
import Silo.ParticleSpawner;
import Silo.SiloSimulator;
import models.Particle;
import models.Vector;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by Mariano on 30/9/2018.
 */
public class Main {

	public static Random random = new Random();

	public static void main(String[] args) throws Exception {

		Double width = 10.0; // m
		Double height = 50.0; // m
		Double cellSize = 1.0; // m

		Double minRadius = 0.01; // m
		Double maxRadius = 0.015; // m

		Double mass = 0.01; // kg

		Double timeLimit = 1.0; // s
		Double timeStep = 0.01; // s
		Integer totalAnimationFrames = 100;

		Integer maxParticles = 200;



		List<Particle> particles = new LinkedList<>();
		particles.add(new Particle(0, new Vector(0.0, 1.0), new Vector(0.0, 0.0), new Vector(0.0, 1.0), 1.0, 0.1));
		particles.add(new Particle(1, new Vector(0.5, 1.0), new Vector(0.0, 0.0), new Vector(0.0, 1.0), 1.0, 0.1));
		particles.add(new Particle(2, new Vector(-0.5, 1.0), new Vector(0.0, 0.0), new Vector(0.0, 1.0), 1.0, 0.1));

		// TODO: El stepCalculator necesita que le pasemos un set de particulas. Cuando no existe el mismo hasta el momento.
		// TODO: Los StepCalculator solo toman un FoceCalculator, no varios. Por lo tanto no podemos agregarle el Gravity y Granular.
		StepCalculator stepCalculator = new LeapFrogVelvetCalculator(new SiloForceCalculator(), timeStep);

		SiloSimulator siloSimulator = new SiloSimulator(width, height, cellSize, timeLimit, timeStep,
				totalAnimationFrames, minRadius, maxRadius, mass, maxParticles, stepCalculator, particles);

		siloSimulator.call();
	}
}
