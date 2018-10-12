package measurers;

import models.Particle;

import java.util.ArrayList;
import java.util.List;

public class FlowMeasurerAmount {
    private Integer particleAccumulator=0;
    private List<Double> lastFlows=new ArrayList<>();
    private Integer particleWindow=0;
    private Double firstParticleTime=0.0;

    public FlowMeasurerAmount(Integer particleWindow) {
        this.particleWindow = particleWindow;
    }

    public void particleFlowed(Double time){
        if(particleAccumulator==0 && lastFlows.size()==0){
            firstParticleTime = time;
        }
        particleAccumulator++;
        if(particleAccumulator >= particleWindow){
            lastFlows.add(time-firstParticleTime);
            firstParticleTime = time;
            particleAccumulator = 0;
        }
    }

    public Double getFlow(){
        Double result = lastFlows.stream().mapToDouble(x->x).average().orElse(-1);
        lastFlows.clear();
        return result;
    }

    public Double getFlowNorm(){
        return particleWindow/getFlow();
    }
}
