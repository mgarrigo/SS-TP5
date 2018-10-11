import Silo.SiloSimulator;
import experiments.ExperimentsStatsAgregator;
import experiments.Operation;
import models.Particle;
import models.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Mariano on 30/9/2018.
 */
public class Main {

	public static Random random = new Random();
	private static Long initialTime = System.currentTimeMillis();

	public static void main(String[] args) throws Exception {

		Double width = 1.5; // m
		Double height = 1.5; // m
		Double cellSize = 0.06; // m

		Double minRadius = 0.02; // m
		Double maxRadius = 0.02; // m

		Double mass = 0.01; // kg

		Double timeLimit = 5.0; // s
		Double timeStep = 8E-5; // s
		Integer totalAnimationFrames = timeLimit.intValue() * 60;

		Integer maxParticles = 100;
		Integer particlesPerLine = 20;

//		particles.add(new Particle(0, new Vector(0.0, 0.0), new Vector(0.80, 2.0), new Vector(0.0, 1.0), 1.01, 0.02));
//		particles.add(new Particle(1, new Vector(0.5, 1.0), new Vector(0.0, 0.0), new Vector(0.0, 1.0), 1.01, 0.02));
//		particles.add(new Particle(2, new Vector(-0.5, 1.0), new Vector(0.0, 0.0), new Vector(0.0, 1.0), 1.01, 0.02));
//		particles.add(new Particle(3, new Vector(-0.3, 1.0), new Vector(0.0, 0.0), new Vector(0.0, 1.0), 1.01, 0.02));

		SiloSimulator siloSimulator = new SiloSimulator(width, height, cellSize, timeLimit, timeStep,
				totalAnimationFrames, minRadius, maxRadius, mass, maxParticles,0.05);

		StringBuilder sb = ExperimentsStatsAgregator.getFromHolders(siloSimulator.call()).buildStatsOutput(Operation.MEAN);
		System.out.println(sb.toString());

        System.out.println(((System.currentTimeMillis() - initialTime)/1000.0) + " s");
	}
}
