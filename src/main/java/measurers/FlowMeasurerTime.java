package measurers;

import java.util.ArrayList;
import java.util.List;

public class FlowMeasurerTime {
    private Integer particleAccumulator=0;
    private Double firstParticleTime=0.0;

    public void particleFlowed(Double time){
        if(particleAccumulator==0){
            firstParticleTime = time;
        }
        particleAccumulator++;
    }

    public Double getFlow(Double currentTime){
        Double result = particleAccumulator/(currentTime-firstParticleTime);
        particleAccumulator = 0;
        return result;
    }
}
