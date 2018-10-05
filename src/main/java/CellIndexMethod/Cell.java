package CellIndexMethod;

import models.Particle;

import java.util.LinkedList;
import java.util.List;

public class Cell {

	public List<Particle> particles;

	public Cell() {
		this.particles = new LinkedList<>();
	}

	public void addParticle(Particle particle) {
		this.particles.add(particle);
	}

	public List<Particle> getParticles() {
		return particles;
	}
}