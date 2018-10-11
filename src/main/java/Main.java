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
		Double cellSize = 0.25; // m

		Double minRadius = 0.02; // m
		Double maxRadius = 0.02; // m

		Double mass = 0.01; // kg

		Double timeLimit = 3.0; // s
		Double timeStep = 1E-4; // s
		Integer totalAnimationFrames = timeLimit.intValue() * 60;

		Integer maxParticles = 20;
		Integer particlesPerLine = 20;



		List<Particle> particles = linearSpawn(0.1,width - 0.1, particlesPerLine);
//		particles.add(new Particle(0, new Vector(0.0, 0.0), new Vector(0.80, 2.0), new Vector(0.0, 1.0), 1.01, 0.02));
//		particles.add(new Particle(1, new Vector(0.5, 1.0), new Vector(0.0, 0.0), new Vector(0.0, 1.0), 1.01, 0.02));
//		particles.add(new Particle(2, new Vector(-0.5, 1.0), new Vector(0.0, 0.0), new Vector(0.0, 1.0), 1.01, 0.02));
//		particles.add(new Particle(3, new Vector(-0.3, 1.0), new Vector(0.0, 0.0), new Vector(0.0, 1.0), 1.01, 0.02));

		SiloSimulator siloSimulator = new SiloSimulator(width, height, cellSize, timeLimit, timeStep,
				totalAnimationFrames, minRadius, maxRadius, mass, maxParticles,0.1, particles);

		StringBuilder sb = ExperimentsStatsAgregator.getFromHolders(siloSimulator.call()).buildStatsOutput(Operation.MEAN);
		System.out.println(sb.toString());

        System.out.println(((System.currentTimeMillis() - initialTime)/1000.0) + " s");
	}
	public static List<Particle> linearSpawn(Double start, Double end, Integer amount){
		List<Particle> particles = new ArrayList<>();
		Integer id=0;
		Random r = new Random();
		for (Double i = start; i < end; i+= ((end-start)/(amount*1.0))) {
			for (Double j = 0.5; j <= 1.3; j+=0.1) {
			particles.add(new Particle(id++, new Vector(i, j), new Vector(0.0, 0.0), new Vector(0.0, 1.0),
					0.01, r.nextDouble()*0.01 + 0.02));
			}
		}
		System.out.println(particles.size() + " partículas\n");
		return particles;
	}
}
