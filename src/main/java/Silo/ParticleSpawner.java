package Silo;

import CellIndexMethod.CellGrid;
import models.Particle;
import models.Vector;

import java.util.List;
import java.util.Random;

public class ParticleSpawner {

	public static Integer SPAWN_ATTEMPTS = 1000;

	public static Particle spawnParticleOnTop(CellGrid cellGrid, Integer id, Double radius, Double mass) {

		Double x;

		int attempt = 0;

		while(attempt++ < SPAWN_ATTEMPTS) {
			x = SiloSimulator.random.nextDouble() * (cellGrid.getWidth() - radius * 2) + radius;
			Particle particle = new Particle(id, new Vector(x, cellGrid.getHeight()), new Vector(), new Vector(), mass, radius);
			List<Particle> adjacent = cellGrid.getAdjacentParticles(particle);

			Boolean conflictFound = false;
			for (Particle p: adjacent) {
				if (particle.isCollisioningWith(p)) {
					conflictFound = true;
					break;
				}
			}

			if (!conflictFound) {
				cellGrid.addParticle(particle);
				return particle;
			}
		}

		return null;

	}
}
