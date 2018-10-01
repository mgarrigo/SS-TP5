package Silo;

import CalculationMethods.ForceCalculator;
import models.Particle;
import models.Vector;

import java.util.Collection;

/**
 * Created by Mariano on 30/9/2018.
 */
public class GranularMaterialForce implements ForceCalculator {

	public static double gravity = 9.80665; // m/s^2

	@Override
	public Vector calculateForce(Particle p, Collection<Particle> particles) {

		// Gravity Force

		Vector Fg = new Vector(0.0, -gravity).dot(p.getMass());

		Vector FnSum = new Vector();
		Vector FtSum = new Vector();

		for (Particle particle: particles) {
			// Add normal force to summation
			//Vector Fn = // -kn * ξ
			//Fn.add()

			// Add tangential force to summation
		}

		return Fg.add(FnSum).add(FtSum);
	}

}
