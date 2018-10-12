package Silo;

import CalculationMethods.ForceCalculators.SiloForceCalculator;
import CalculationMethods.StepCalculator;
import CalculationMethods.StepCalculators.LeapFrogVelvetCalculator;
import CellIndexMethod.CellGrid;
import experiments.ExperimentStatsHolder;
import helpers.AnimationBuilder;
import helpers.FileManager;
import measurers.FlowMeasurer;
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
	private Double timeBetweenParticles = 0.12;

	private Double timeBetweenMeasures=0.1;
	private Double lastMeasuredTime=0.0;
	private Queue<Particle> waitingToReincarante = new ArrayDeque<>();

	//This is the height of the funnel leading to the opening
	private Double operingHeight;
	private Double openingRatio;

	private FlowMeasurer flowMeasurer = new FlowMeasurer();

	public SiloSimulator(Double width, Double height, Double cellSize, Double timeLimit, Double timeStep,
                         Integer totalAnimationFrames, Double minRadius, Double maxRadius, Double mass,
                         Integer maxParticles, Double openingRatio) {
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
		this.openingRatio = openingRatio;
		this.particles = new ArrayList<>();
	}



	@Override
	public ExperimentStatsHolder<SiloMetrics> call() throws Exception {

//		List<Particle> particles = new LinkedList<>();
		CellGrid cellGrid = new CellGrid(width, height, cellSize);
		Integer id = 0;

		ExperimentStatsHolder<SiloMetrics> holder = new ExperimentStatsHolder<>();
		List<Wall> walls = getSiloWalls(openingRatio);
		StepCalculator stepCalculator = new LeapFrogVelvetCalculator(new SiloForceCalculator(walls), timeStep, width, height, cellSize);

		AnimationBuilder ab = new AnimationBuilder(walls);
		FileManager fm = new FileManager();

		double currentTime = 0.0;
		int animationCurrentFrame = 0;

		while (currentTime <= timeLimit) {
			if (currentTime > timeLimit / totalAnimationFrames * animationCurrentFrame) {
				ab.addParticlesforNextFrame(particles);
				animationCurrentFrame++;
			}

			particles = stepCalculator.updateParticles(particles);

			if (currentTime - lastMeasuredTime > timeBetweenMeasures) {
				holder.addDataPoint(SiloMetrics.TIME, currentTime);
				holder.addDataPoint(SiloMetrics.KINETIC_ENERGY,getCineticEnergy(particles));
				holder.addDataPoint(SiloMetrics.FLOW_BY_AMOUNT,flowMeasurer.getFlowByAmount());
				holder.addDataPoint(SiloMetrics.FLOW_BY_AMOUNT_NORM,flowMeasurer.getFlowByAmountNorm());
				holder.addDataPoint(SiloMetrics.FLOW_BY_TIME,flowMeasurer.getFlowByTime(currentTime));
				System.out.println("Time Simulated: " + currentTime);
				lastMeasuredTime = currentTime;
			}

			cellGrid.clear();
			cellGrid.addParticles(particles);
			List<Particle> outsideParticles = cellGrid.getOutsideParticles(particles);
			flowMeasurer.particlesFlowed(outsideParticles,currentTime);
			particles.removeAll(outsideParticles);

			if (particles.size() < maxParticles) {
				Double radius = random.nextDouble() * (maxRadius - minRadius) + minRadius;
				Particle particle = ParticleSpawner.spawnParticleOnTop(cellGrid, id++, radius, mass);
				if (particle != null) particles.add(particle);
//				System.out.println(particles.size());
			}

			currentTime += timeStep;
		}

		fm.writeString("p5/frontend/output.txt", ab.getString());

		return holder;
	}

	private Double getCineticEnergy(List<Particle> particles){
		return 0.5*particles.stream().mapToDouble(p -> p.getMass()*Math.pow(p.getVelocity().norm(),2.0)).sum();
	}

	private List<Particle> getParticlesToFillSilo(Integer amount) {
        Double currentTime=0.0;
        List<Particle> particles = new ArrayList<>();
        Boolean finished = false;
        Double secondsToStabilice = .30;
		CellGrid cellGrid = new CellGrid(width, height, cellSize);
		Integer id=0;
		Random r = new Random();
		StepCalculator stepCalculator = new LeapFrogVelvetCalculator(new SiloForceCalculator(getSiloWalls(openingRatio,true)), timeStep, width, height, cellSize);
	    while (! finished || currentTime < secondsToStabilice) {
            particles = stepCalculator.updateParticles(particles);
			if(!finished){
				if(particles.size() >= amount ){
					finished = true;
					currentTime = 0.0;
					System.out.println("Spawned all particles, waiting " + secondsToStabilice + " secs");
				} else {
					Particle particle = ParticleSpawner.spawnParticleOnTop(cellGrid, id++, r.nextDouble()*(maxRadius-minRadius) + minRadius, mass);
					if (particle != null){
						particles.add(particle);
						System.out.println("Spawning particle: " + (particles.size() -1));
					}
				}
			}
            currentTime += timeStep;
        }
        System.out.println("Finished stabilizing");
        return particles;
    }

    public List<Particle> linearSpawn(Double start, Double end, Integer amount, int startingId, Double height){
        List<Particle> particles = new ArrayList<>();
        Integer id=startingId;
        Random r = new Random();
        for (Double i = start; i < end; i+=((end-start)/(amount*1.0))) {
                particles.add(new Particle(id+++"", new Vector(i, height),
                        mass, r.nextDouble()*(maxRadius-minRadius) + minRadius));
        }
        System.out.println(particles.size() + " partículas\n");
        return particles;
    }


	private List<Wall> getSiloWalls(Double openingRatio){
        return getSiloWalls(openingRatio,false);
    }

	private List<Wall> getSiloWalls(Double openingRatio, Boolean closed){
		Double siloWidth = width;
		Double siloWallHeight = height;
		Double openingHeight = 0.0;
		this.operingHeight = -(openingHeight+siloWallHeight);
		List<Wall> walls = new ArrayList<>();
		//Left Wall
		walls.add(new Wall(new Vector(0.0, siloWallHeight), new Vector(0.0, openingHeight* (siloWallHeight-0.2) + 0.2)));
		//Right Wall
		walls.add(new Wall(new Vector(siloWidth, openingHeight* (siloWallHeight-0.2) + 0.2), new Vector(siloWidth, siloWallHeight)));
		//Right ramp
		walls.add(new Wall(new Vector(((width+openingRatio*width)/2), 0.2),new Vector(siloWidth, openingHeight* (siloWallHeight-0.2) + 0.2)));
		//Left Ramp
		walls.add(new Wall(new Vector(0.0, openingHeight* (siloWallHeight-0.2) + 0.2),new Vector(((width-openingRatio*width)/2), 0.2)));
        // Horizontal floor
        if(closed){
            walls.add(new Wall(new Vector(0.0, 0.2), new Vector(siloWidth, 0.2)));
        }
        return walls;
	}
}
