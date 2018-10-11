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
	private Double timeBetweenParticles = 0.05;

	//This is the height of the funnel leading to the opening
	private Double operingHeight;
	private Double openingRatio;

	public SiloSimulator(Double width, Double height, Double cellSize, Double timeLimit, Double timeStep,
                         Integer totalAnimationFrames, Double minRadius, Double maxRadius, Double mass,
                         Integer maxParticles, Double openingRatio, List <Particle> particles) {
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
		this.particles =  particles;//getParticlesToFillSilo(maxParticles);
	}



	@Override
	public ExperimentStatsHolder<SiloMetrics> call() throws Exception {

//		List<Particle> particles = new LinkedList<>();
//		CellGrid cellGrid = new CellGrid(width, height + cellSize, cellSize);
//		Integer id = 0;

		ExperimentStatsHolder<SiloMetrics> holder = new ExperimentStatsHolder<>();
		List<Wall> walls = getSiloWalls(openingRatio);
		StepCalculator stepCalculator = new LeapFrogVelvetCalculator(new SiloForceCalculator(walls, width, height, cellSize), timeStep);

		AnimationBuilder ab = new AnimationBuilder(walls);
        FileManager fm = new FileManager();

        double currentTime = 0.0;
        int animationCurrentFrame = 0;

		while(currentTime <= timeLimit) {
			if (currentTime > timeLimit / totalAnimationFrames * animationCurrentFrame) {
				ab.addParticlesforNextFrame(particles);
                animationCurrentFrame++;
			}

			particles = stepCalculator.updateParticles(particles);

			if(currentTime - lastMeasuredTime > deltaTime) {
				holder.addDataPoint(SiloMetrics.FLOW,getFlowRate(currentTime,particles));
				holder.addDataPoint(SiloMetrics.TIME,currentTime);
                System.out.println("Time Simulated: " + currentTime);
			}


            resetParticles();
			currentTime += timeStep;
		}

		fm.writeString("p5/frontend/output.txt", ab.getString());

		return holder;
	}

	private void resetParticles() {
        for (int i = 0; i < particles.size(); i++) {
            Particle p = particles.get(i);
	        if (p.getPosition().getY() < -height/10) {
                particles.remove(i);
                particles.add(p.getCopyWithPosition(new Vector(0.9*random.nextDouble()*width,(0.2*random.nextDouble()+0.8)*height))
                        .getCopyWithVelocity(new Vector(0.0, 0.0)));
            }
        }
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

	private List<Particle> getParticlesToFillSilo(Integer amount) {
        Double currentTime=0.0;
        Double lastParticleSpawnedAt = 0.0;
        List<Particle> particles = new ArrayList<>();
        Boolean finished = false;
        Double secondsToStabilice = .30;
        StepCalculator stepCalculator = new LeapFrogVelvetCalculator(new SiloForceCalculator(getSiloWalls(openingRatio,true), width, height, cellSize), timeStep);
	    while (! finished || currentTime < secondsToStabilice) {
            particles = stepCalculator.updateParticles(particles);
            if(currentTime - lastParticleSpawnedAt > timeBetweenParticles){
                if(!finished){
                    if(particles.size() >= amount ){
                        finished = true;
                        currentTime = 0.0;
                        System.out.println("Spawned all particles, waiting " + secondsToStabilice + " secs");
                    } else {
                        lastParticleSpawnedAt = currentTime;
                        particles.add(new Particle(particles.size()+"a",new Vector(0.9*random.nextDouble()*width,height),maxRadius,mass));
                        System.out.println("Spawnign particle: " + (particles.size() -1));
                    }
                }
            }
            currentTime += timeStep;
        }
        System.out.println("Finished stabilizing");
        return particles;
    }


	private List<Wall> getSiloWalls(Double openingRatio){
        return getSiloWalls(openingRatio,false);
    }

	private List<Wall> getSiloWalls(Double openingRatio, Boolean closed){
		Double siloScale = 1.0;
		Double siloWidth = width;
		Double siloWallHeight = height;
		Double openingHeight = 0.2;
		this.operingHeight = -(openingHeight+siloWallHeight)*siloScale;
		List<Wall> walls = new ArrayList<>();
		//Left Wall
		walls.add(new Wall(new Vector(0.0, siloWallHeight*siloScale), new Vector(0.0, openingHeight* siloWallHeight*siloScale)));
		//Right Wall
		walls.add(new Wall(new Vector(siloScale*siloWidth, openingHeight* siloWallHeight*siloScale), new Vector(siloScale*siloWidth, siloWallHeight*siloScale)));
		//Right ramp
		walls.add(new Wall(new Vector(((width+openingRatio)/2)*siloScale, 0.2),new Vector(siloScale*siloWidth, openingHeight* siloWallHeight*siloScale)));
		//Left Ramp
		walls.add(new Wall(new Vector(0.0, openingHeight* siloWallHeight*siloScale),new Vector(((width-openingRatio)/2)*siloScale, 0.2)));
        // Horizontal floor
        if(closed){
//            walls.add(new Wall(new Vector(0.0, 0.5 * siloWallHeight*siloScale), new Vector(siloScale*siloWidth, 0.5 * siloWallHeight*siloScale)));
        }
        return walls;
	}
}
