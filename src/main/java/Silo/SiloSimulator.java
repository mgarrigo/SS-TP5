package Silo;

import CalculationMethods.StepCalculator;
import CellIndexMethod.CellGrid;
import helpers.AnimationBuilder;
import helpers.FileManager;
import models.Particle;
import models.Vector;
import models.Wall;


import java.util.*;
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
	private Integer totalAnimationFrames;
	private Double minRadius;
	private Double maxRadius;
	private Double mass;
	private Integer maxParticles;
	private List<Particle> particles;

	private StepCalculator stepCalculator;


	public SiloSimulator(Double width, Double height, Double cellSize, Double timeLimit, Double timeStep,
                         Integer totalAnimationFrames, Double minRadius, Double maxRadius, Double mass,
                         Integer maxParticles, StepCalculator stepCalculator, List<Particle> particles) {
		this.width = width;
		this.height = height;
		this.cellSize = cellSize;
		this.timeLimit = timeLimit;
		this.timeStep = timeStep;
		this.totalAnimationFrames = totalAnimationFrames;
		this.minRadius = minRadius;
		this.maxRadius = maxRadius;
		this.mass = mass;
		this.maxParticles = maxParticles;
		this.stepCalculator = stepCalculator;
		this.particles = particles;
	}

	@Override
	public Object call() throws Exception {

//		List<Particle> particles = new LinkedList<>();
//		CellGrid cellGrid = new CellGrid(width, height + cellSize, cellSize);
//        int id = 0;

        // TODO: Generate map or walls
        Wall w1 = new Wall(new Vector(0.5, 0.0), new Vector(1.0, 0.5));
        Wall w2 = new Wall(new Vector(1.0, 0.5), new Vector(1.0, 1.0));
        Wall w3 = new Wall(new Vector(-0.5, 0.0), new Vector(-1.0, 0.5));
        Wall w4 = new Wall(new Vector(-1.0, 0.5), new Vector(-1.0, 1.0));

        AnimationBuilder ab = new AnimationBuilder();
        FileManager fm = new FileManager();

        double currentTime = 0.0;
        int animationCurrentFrame = 0;

		while(currentTime <= timeLimit) {
			if (currentTime > timeLimit / totalAnimationFrames * animationCurrentFrame) {
				ab.addParticlesforNextFrame(particles);
                animationCurrentFrame++;
			}

			particles = stepCalculator.updateParticles(particles);


//			cellGrid.addParticles(particles);

			// Removes the particles outside the map.
//			List<Particle> fallen = cellGrid.getFallenParticles();
//			particles.removeAll(fallen);

			// Generates the particles on top of the silo, if they fit.
			// TODO: ACA LAS PARTICULAS SE TRATAN DE INSERTAR SIEMPRE HASTA LLEGAR AL MAXIMO. PREGUNTAR SI SE PUEDE HACER ASI O SE NECESITA QUE APAREZCA YA EL SILO LLENO AL EMPEZAR LA SIMULACION.
//			while(particles.size() < maxParticles) {
//				Double radius = random.nextDouble() * (maxRadius - minRadius) + minRadius;
//				Particle particle = ParticleSpawner.spawnParticleOnTop(cellGrid, id, radius, mass);
//				if (particle == null) break;
//				System.out.println("Created Particle " + particle);
//				particles.add(particle);
//				id++;
//			}

//			stepCalculator.updateParticles(particles);

//			cellGrid.clear();

			currentTime += timeStep;
		}

		fm.writeString("p5/frontend/output.txt", ab.getString());

		return null;
	}
}
