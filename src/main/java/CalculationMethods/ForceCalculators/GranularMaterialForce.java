package CalculationMethods.ForceCalculators;

import CalculationMethods.ForceCalculator;
import models.Particle;
import models.Vector;

import java.util.Collection;

/**
 * Created by Mariano on 30/9/2018.
 */
public class GranularMaterialForce implements ForceCalculator {

	private static double Kn = 1E5; // 10^5 N/m
	private static double Kt = 2*Kn;

	@Override
	public Vector calculateForce(Particle p, Collection<Particle> particles) {

		// Gravity Force

		Vector FnSum = new Vector();
		Vector FtSum = new Vector();

		//TODO: Tome N.2 y T.3 porque no tengo idea que es el gama. Hay que cambiarlo antes de entregar
		for (Particle particle: particles) {

			Vector normalVersor = particle.getPosition().subtract(p.getPosition()).normalize();
			Vector tangentialVersor = new Vector(-normalVersor.getY(), normalVersor.getX());

			// Add normal force to summation
			double xi = p.getRadius() + particle.getRadius() - p.getPosition().distance(particle.getPosition());
			Vector Fn = normalVersor.dot(-Kn * xi); // -kn * ξ (versor normal)

			FnSum.add(Fn);

			// Add tangential force to summation

			//TODO: esto no se si va asi, o al reves
			Vector rrel = p.getVelocity().subtract(particle.getVelocity());
			Vector Ft = tangentialVersor.dot( -Kt * xi * rrel.dot(tangentialVersor)); // -kt * ξ * [rrel x tversor] (versor tangencial)

			FtSum.add(Ft);
		}

		return FnSum.add(FtSum);
	}

}