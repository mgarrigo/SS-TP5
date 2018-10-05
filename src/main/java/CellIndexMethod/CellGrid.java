package CellIndexMethod;

import models.Particle;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class CellGrid {

	private Cell[][] cells;

	private Double width;
	private Double height;
	private Double cellSize;
	private Integer rows;
	private Integer cols;

	public CellGrid(Double width, Double height, Double cellSize) throws IllegalArgumentException {

		if (width / cellSize % 1 != 0 || height / cellSize % 1 != 0) {
			throw new IllegalArgumentException("Width or Height not multiple of cellSize");
		}

		this.width = width;
		this.height = height;
		this.cellSize = cellSize;

		this.rows = (int) (height / cellSize);
		this.cols = (int) (width / cellSize);
		generateGrid();
	}

	private void generateGrid() {
		this.cells = new Cell[rows][cols];
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				cells[row][col] = new Cell();
			}
		}
	}

	public void clear() {
		generateGrid();
	}

	public Double getWidth() {
		return width;
	}

	public Double getHeight() {
		return height;
	}

	public List<Particle> getAdjacentParticles(Particle particle) {
		int centerRow = (int) Math.floor(particle.getPosition().getY() / cellSize);
		int centerCol = (int) Math.floor(particle.getPosition().getX() / cellSize);

		List<Particle> adjacent = new LinkedList<>();

		for (int row = centerRow - 1; row < centerRow + 1; row++) {
			for (int col = centerCol - 1; col < centerCol + 1; col++) {
				if (row >= 0 && row < rows && col >= 0 && col < cols) {
					adjacent.addAll(cells[row][col].getParticles());
				}
			}
		}

		adjacent.remove(particle);

		return adjacent;
	}

	public void addParticle(Particle particle) {
		int row = (int) Math.floor(particle.getPosition().getY() / cellSize);
		int col = (int) Math.floor(particle.getPosition().getX() / cellSize);

		cells[row][col].addParticle(particle);
	}

	// TODO: Test if this works properly, or make CellIndexMethod.Cell list a Concurrent List
	public void addParticles(Collection<Particle> particles) {
		particles.parallelStream().forEach(p -> addParticle(p));
	}

	public List<Particle> getFallenParticles() {
		List<Particle> fallen = new LinkedList<>();
		for (int col = 0; col < cols; col++) {
			fallen.addAll(cells[rows-1][col].getParticles());
		}
		return fallen;
	}

}