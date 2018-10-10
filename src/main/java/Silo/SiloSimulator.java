package Silo;

import CalculationMethods.ForceCalculator;
import CalculationMethods.ForceCalculators.SiloForceCalculator;
import CalculationMethods.StepCalculator;
import CalculationMethods.StepCalculators.LeapFrogVelvetCalculator;
import CellIndexMethod.CellGrid;
import experiments.ExperimentStatsHolder;
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
	private Double timeBetweenParticles = 0.1;

	//This is the ratio of the opening in relation to the width of the silo
	private Double operingHeight;

	public SiloSimulator(Double width, Double height, Double cellSize, Double timeLimit, Double timeStep,
                         Integer totalAnimationFrames, Double minRadius, Double maxRadius, Double mass,
                         Integer maxParticles, List<Particle> particles) {
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
		this.particles = particles;
	}



	@Override
	public ExperimentStatsHolder<SiloMetrics> call() throws Exception {

//		List<Particle> particles = new LinkedList<>();
//		CellGrid cellGrid = new CellGrid(width, height + cellSize, cellSize);
//        int id = 0;

		ExperimentStatsHolder<SiloMetrics> holder = new ExperimentStatsHolder<>();
		// TODO: El stepCalculator necesita que le pasemos un set de particulas. Cuando no existe el mismo hasta el momento.
		StepCalculator stepCalculator = new LeapFrogVelvetCalculator(new SiloForceCalculator(getSiloWalls(0.15)), timeStep);

		AnimationBuilder ab = new AnimationBuilder();
        FileManager fm = new FileManager();

        double currentTime = 0.0;
        Double lastParticleSpawnedAt = currentTime;
        int animationCurrentFrame = 0;

		while(currentTime <= timeLimit) {
			if (currentTime > timeLimit / totalAnimationFrames * animationCurrentFrame) {
				ab.addParticlesforNextFrame(particles);
                animationCurrentFrame++;
			}

			particles = stepCalculator.updateParticles(particles);

			if(currentTime - lastParticleSpawnedAt > timeBetweenParticles && particles.size() < maxParticles){
				lastParticleSpawnedAt = currentTime;
				particles.add(new Particle(particles.size()+"a",new Vector(random.nextDouble()-0.5,0.0),maxRadius,mass));
			}
			if(currentTime - lastMeasuredTime > deltaTime) {
				holder.addDataPoint(SiloMetrics.FLOW,getFlowRate(currentTime,particles));
				holder.addDataPoint(SiloMetrics.TIME,currentTime);
			}
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

		return holder;
	}

	private Double deltaTime=1.0;
	//If the fallen particles are removed, we need to substract the fallen from this value
	private Integer lastParticles=0;
	private Double lastMeasuredTime=0.0;

	private Double getFlowRate(Double currentTime, List<Particle> particles){
		Long escapedParticles = particles.stream().mapToDouble(p->p.getPosition().getY())
				.filter(y -> y < operingHeight).count();
		Double flow = (escapedParticles - lastParticles) / deltaTime;
		lastParticles = escapedParticles.intValue();
		lastMeasuredTime = currentTime;
		return flow;
	}

	private List<Wall> getSiloWalls(Double openingRatio){
		Double siloScale = 1.0;
		Double siloWidth = 0.6;
		Double siloWallHeight = 0.5;
		Double openingHeight = 0.15;
		this.operingHeight = -(openingHeight+siloWallHeight)*siloScale;
		List<Wall> walls = new ArrayList<>();
		//Left Wall
		walls.add(new Wall(new Vector(-siloScale*siloWidth, siloWallHeight*siloScale), new Vector(-siloScale*siloWidth, -siloWallHeight*siloScale)));
		//Right Wall
		walls.add(new Wall(new Vector(siloScale*siloWidth, -siloWallHeight*siloScale), new Vector(siloScale*siloWidth, siloWallHeight*siloScale)));
		//Right ramp
		walls.add(new Wall(new Vector(openingRatio*siloScale*siloWidth, -(openingHeight+siloWallHeight)*siloScale),new Vector(siloScale*siloWidth, -siloWallHeight*siloScale)));
		//Left Ramp
		walls.add(new Wall(new Vector(-siloScale*siloWidth, -siloWallHeight*siloScale),new Vector(-openingRatio*siloScale*siloWidth, -(openingHeight+siloWallHeight)*siloScale)));
        // Horizontal floor
//		walls.add(new Wall(new Vector(-siloScale*siloWidth, -siloWallHeight*siloScale), new Vector(siloScale*siloWidth, -siloWallHeight*siloScale)));
        return walls;
	}
}
