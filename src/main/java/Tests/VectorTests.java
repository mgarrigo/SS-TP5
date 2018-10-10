package Tests;

import models.Vector;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class VectorTests {

    private final static Double DOUBLE_PRECISION = 1E-10;

    @Test
    public void proyectionTest()
    {
        Vector v1 = new Vector(1.0, 0.0);
        Vector v2 = new Vector(4.0, 6.0);
        assertEquals(new Vector(4.0, 0.0), v2.projectionOnVector(v1));
    }

    @Test
    public void proyectionTest2()
    {
        Vector v1 = new Vector(1.0, 0.0);
        Vector v2 = new Vector(-4.0, 6.0);
        assertEquals(new Vector(-4.0, 0.0), v2.projectionOnVector(v1));
    }

    @Test
    public void proyectionTest3()
    {
        Vector v1 = new Vector(1.0, 1.0);
        Vector v2 = new Vector(0.0, 1.0);
        assertEquals(new Vector(0.5, 0.5), v2.projectionOnVector(v1));
    }

    @Test
    public void proyectionTest4()
    {
        Vector v1 = new Vector(1.0, 1.0);
        Vector v2 = new Vector(0.0, -1.0);
        assertEquals(new Vector(-0.5, -0.5), v2.projectionOnVector(v1));
    }

}
