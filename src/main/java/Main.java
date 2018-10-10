import CalculationMethods.ForceCalculators.GravityCalculator;
import CalculationMethods.ForceCalculators.SiloForceCalculator;
import CalculationMethods.StepCalculator;
import CalculationMethods.StepCalculators.BeemanCalculator;
import CalculationMethods.StepCalculators.LeapFrogVelvetCalculator;
import Silo.ParticleSpawner;
import Silo.SiloSimulator;
import experiments.ExperimentsStatsAgregator;
import experiments.Operation;
import models.Particle;
import models.Vector;
import models.Wall;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by Mariano on 30/9/2018.
 */
public class Main {

	public static Random random = new Random();
	private static Long initialTime = System.currentTimeMillis();

	public static void main(String[] args) throws Exception {

		Double width = 10.0; // m
		Double height = 50.0; // m
		Double cellSize = 1.0; // m

		Double minRadius = 0.01; // m
		Double maxRadius = 0.055; // m

		Double mass = 0.01; // kg

		Double timeLimit = 10.0; // s
		Double timeStep = 1E-4; // s
		Integer totalAnimationFrames = timeLimit.intValue() * 60;

		Integer maxParticles = 50;



		List<Particle> particles = linearSpawn(-0.5,0.5,0);
//		particles.add(new Particle(0, new Vector(0.0, 1.0), new Vector(0.0, 0.0), new Vector(0.0, 1.0), 1.01, 0.02));
//		particles.add(new Particle(1, new Vector(0.5, 1.0), new Vector(0.0, 0.0), new Vector(0.0, 1.0), 1.01, 0.02));
//		particles.add(new Particle(2, new Vector(-0.5, 1.0), new Vector(0.0, 0.0), new Vector(0.0, 1.0), 1.01, 0.02));
//		particles.add(new Particle(3, new Vector(-0.3, 1.0), new Vector(0.0, 0.0), new Vector(0.0, 1.0), 1.01, 0.02));

		SiloSimulator siloSimulator = new SiloSimulator(width, height, cellSize, timeLimit, timeStep,
				totalAnimationFrames, minRadius, maxRadius, mass, maxParticles,0.13);

		StringBuilder sb = ExperimentsStatsAgregator.getFromHolders(siloSimulator.call()).buildStatsOutput(Operation.MEAN);
		System.out.println(sb.toString());

        System.out.println((System.currentTimeMillis() - initialTime) + " ms");
	}
	public static List<Particle> linearSpawn(Double start, Double end, Integer amount){
		List<Particle> particles = new ArrayList<>();
		Integer id=0;
		for (Double i = start; i < end; i+= ((end-start)/(amount*1.0))) {
			particles.add(new Particle(id++, new Vector(i, -0.3), new Vector(0.0, 0.0), new Vector(0.0, 1.0), 0.01, 0.02));
		}
		return particles;
	}
}
