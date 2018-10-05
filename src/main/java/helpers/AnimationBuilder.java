package helpers;

import models.Particle;

import java.util.Collection;
import java.util.Locale;

public class AnimationBuilder {

    private int precision = 8;
    private StringBuilder sb = new StringBuilder();

    public void addParticlesforNextFrame(Collection<Particle> particles) {
        for (Particle p : particles) {
            sb.append(String.format(new Locale("en", "us"),"%."+precision+"f ", p.getPosition().getX()));
            sb.append(String.format(new Locale("en", "us"),"%."+precision+"f ", p.getPosition().getY()));
        }
        sb.append("\n");
    }

    public String getString() {
        return sb.toString();
    }
}
