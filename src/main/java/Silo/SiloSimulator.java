package Silo;

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
	private Double timeBetweenParticles = 0.12;
	private Double lastParticleSpawnedAt = 0.0;
	Queue<Particle> waitingToReincarante = new ArrayDeque<>();

	//This is the height of the funnel leading to the opening
	private Double operingHeight;
	private Double openingRatio;

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

			cellGrid.clear();
			cellGrid.addParticles(particles);
			particles.removeAll(cellGrid.getOutsideParticles(particles));

			if (particles.size() < maxParticles) {
				Double radius = random.nextDouble() * (maxRadius - minRadius) + minRadius;
				Particle particle = ParticleSpawner.spawnParticleOnTop(cellGrid, id++, radius, mass);
				if (particle != null) particles.add(particle);
//				System.out.println(particles.size());
			}


//            killParticles();
//
//			if(currentTime - lastParticleSpawnedAt > timeBetweenParticles && !waitingToReincarante.isEmpty()){
//                lastParticleSpawnedAt = currentTime;
//                Particle revived = waitingToReincarante.poll();
//                revived = revived.getCopyWithVelocity(new Vector(0.0, 0.0))
//                        .getCopyWithPosition(new Vector((0.2+0.6*random.nextDouble())*width, height));
//                particles.add(revived);
//            }

			currentTime += timeStep;
		}

		fm.writeString("p5/frontend/output.txt", ab.getString());

		return holder;
	}


	private void killParticles() {
        for (int i = 0; i < particles.size(); i++) {
            Particle p = particles.get(i);
	        if (p.getPosition().getY() < -height/10) {
                waitingToReincarante.add(particles.remove(i));
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
        StepCalculator stepCalculator = new LeapFrogVelvetCalculator(new SiloForceCalculator(getSiloWalls(openingRatio,true)), timeStep, width, height, cellSize);
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
                        particles.addAll(linearSpawn(0.20*width, 0.8*width, 10, particles.size(), height));
//                        particles.add(new Particle(particles.size()+"a",new Vector((0.2+0.5*random.nextDouble())*width,height),mass,maxRadius));
                        System.out.println("Spawnign particle: " + (particles.size() -1));
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
                particles.add(new Particle(id++, new Vector(i, height), new Vector(0.0, 0.0), new Vector(0.0, 0.0),
                        0.01, r.nextDouble()*0.01 + 0.02));
        }
        System.out.println(particles.size() + " partículas\n");
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
		this.operingHeight = -(openingHeight+siloWallHeight);
		List<Wall> walls = new ArrayList<>();
		//Left Wall
		walls.add(new Wall(new Vector(0.0, siloWallHeight), new Vector(0.0, openingHeight* siloWallHeight)));
		//Right Wall
		walls.add(new Wall(new Vector(siloWidth, openingHeight* siloWallHeight), new Vector(siloWidth, siloWallHeight)));
		//Right ramp
		walls.add(new Wall(new Vector(((width+openingRatio*width)/2), 0.2),new Vector(siloWidth, openingHeight* siloWallHeight)));
		//Left Ramp
		walls.add(new Wall(new Vector(0.0, openingHeight* siloWallHeight),new Vector(((width-openingRatio*width)/2), 0.2)));
        // Horizontal floor
        if(closed){
            walls.add(new Wall(new Vector(0.0, 0.2), new Vector(siloWidth, 0.2)));
        }
        return walls;
	}
}
