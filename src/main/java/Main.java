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
		Double timeStep = 9E-5; // s
		Integer totalAnimationFrames = timeLimit.intValue() * 60;

		Integer maxParticles = 300;

		Double opening = 0.15; // m

		SiloSimulator siloSimulator = new SiloSimulator(width, height, cellSize, timeLimit, timeStep,
				totalAnimationFrames, minRadius, maxRadius, mass, maxParticles,opening/width);

		StringBuilder sb = ExperimentsStatsAgregator.getFromHolders(siloSimulator.call()).buildStatsOutput(Operation.MEAN);
		System.out.println(sb.toString());

        System.out.println(((System.currentTimeMillis() - initialTime)/1000.0) + " s");
	}
}
