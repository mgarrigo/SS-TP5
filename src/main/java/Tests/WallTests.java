package Tests;

import models.Particle;
import models.Vector;
import models.Wall;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WallTests {

    private final static Double DOUBLE_PRECISION = 1E-10;

    @Test
    public void distanceToWallTest()
    {
        Wall w = new Wall(new Vector(-1.0, -1.0), new Vector(1.0, 1.0));
        Particle p = new Particle(0, new Vector(0.0, 0.0), null, null, 1.0, 0.0);
        double distance = w.distanceToParticle(p);
        assertTrue(Math.abs(distance) < DOUBLE_PRECISION);
    }

    @Test
    public void distanceToWallTest2() {
        Wall w = new Wall(new Vector(-1.0, 1.0), new Vector(1.0, 1.0));
        Particle p = new Particle(0, new Vector(1.0, 0.0), null, null, 1.0, 0.0);
        double distance = w.distanceToParticle(p);
        assertEquals(1.0, distance);
    }

    @Test
    public void distanceToWallTest3() {
        Wall w = new Wall(new Vector(-1.0, -1.0), new Vector(1.0, 1.0));
        Particle p = new Particle(0, new Vector(-1.0, 1.0), null, null, 1.0, 0.0);
        Double distance = w.distanceToParticle(p);
        assertTrue(Math.abs(Math.sqrt(2)-distance) < DOUBLE_PRECISION);
    }

    @Test
    public void distanceToWallTest4() {
        Wall w = new Wall(new Vector(-1.0, -1.0), new Vector(1.0, 1.0));
        Particle p = new Particle(0, new Vector(1.0, -1.0), null, null, 1.0, 0.0);
        Double distance = w.distanceToParticle(p);
        assertTrue(Math.abs(Math.sqrt(2)-distance) < DOUBLE_PRECISION);
    }

    @Test
    public void distanceToWallTest5() {
        Wall w = new Wall(new Vector(-1.0, -1.0), new Vector(1.0, 1.0));
        Particle p = new Particle(0, new Vector(-10.0, 10.0), null, null, 1.0, 0.0);
        Double distance = w.distanceToParticle(p);
        assertTrue(Math.abs(new Vector(0.0, 0.0).distance(p.getPosition())-distance) < DOUBLE_PRECISION);
    }

    @Test
    public void distanceToWallTest6() {
        Wall w = new Wall(new Vector(-1.0, -1.0), new Vector(1.0, 1.0));
        Particle p = new Particle(0, new Vector(-2.0, -2.0), null, null, 1.0, 0.0);
        double distance = w.distanceToParticle(p);
        assertEquals(Double.POSITIVE_INFINITY, distance);
    }

    @Test
    public void distanceToWallTest7() {
        Wall w = new Wall(new Vector(-1.0, -1.0), new Vector(1.0, 1.0));
        Particle p = new Particle(0, new Vector(2.0, 2.0), null, null, 1.0, 0.0);
        double distance = w.distanceToParticle(p);
        assertEquals(Double.POSITIVE_INFINITY, distance);
    }

    @Test
    public void distanceToWallTest8() {
        Wall w = new Wall(new Vector(-1.0, -1.0), new Vector(1.0, 1.0));
        Particle p = new Particle(0, new Vector(5.0, 20.0), null, null, 1.0, 0.0);
        double distance = w.distanceToParticle(p);
        assertEquals(Double.POSITIVE_INFINITY, distance);
    }

    @Test
    public void distanceToWallTest9() {
        Wall w = new Wall(new Vector(-1.0, 0.0), new Vector(1.0, 2.0));
        Particle p = new Particle(0, new Vector(0.0, 1.0), null, null, 1.0, 0.0);
        double distance = w.distanceToParticle(p);
        assertTrue(Math.abs(distance) < DOUBLE_PRECISION);
    }

    @Test
    public void distanceToWallTest10() {
        Wall w = new Wall(new Vector(-2.0, 2.0), new Vector(2.0, 0.0));
        Particle p = new Particle(0, new Vector(0.0, 1.0), null, null, 1.0, 0.0);
        double distance = w.distanceToParticle(p);
        assertTrue(Math.abs(distance) < DOUBLE_PRECISION);
    }

}
