import CalculationMethods.ForceCalculators.GravityCalculator;
import CalculationMethods.StepCalculator;
import CalculationMethods.StepCalculators.BeemanCalculator;
import Silo.SiloSimulator;

import java.util.LinkedList;

/**
 * Created by Mariano on 30/9/2018.
 */
public class Main {

	public static void main(String[] args) throws Exception {

		Double width = 10.0; // m
		Double height = 50.0; // m
		Double cellSize = 1.0; // m

		Double minRadius = 0.01; // m
		Double maxRadius = 0.015; // m

		Double mass = 0.01; // kg

		Double timeLimit = 120.0; // s
		Double timeStep = 0.01; // s
		Double animationTimeStepMultiplier = 1 / (60 * timeStep); // 60 FPS

		Integer maxParticles = 10000;

		// TODO: El stepCalculator necesita que le pasemos un set de particulas. Cuando no existe el mismo hasta el momento.
		// TODO: Los StepCalculator solo toman un FoceCalculator, no varios. Por lo tanto no podemos agregarle el Gravity y Granular.
		StepCalculator stepCalculator = new BeemanCalculator(new GravityCalculator(), timeStep, new LinkedList<>());

		SiloSimulator siloSimulator = new SiloSimulator(width, height, cellSize, timeLimit, timeStep,
				animationTimeStepMultiplier, minRadius, maxRadius, mass, maxParticles, stepCalculator);

		siloSimulator.call();
	}
}
