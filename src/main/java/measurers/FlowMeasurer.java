package measurers;

import models.Particle;

import java.util.List;

public class FlowMeasurer {
    FlowMeasurerAmount flowMeasurerAmount = new FlowMeasurerAmount(10);
    FlowMeasurerAmount flowMeasurerAmountNorm = new FlowMeasurerAmount(10);
    FlowMeasurerTime flowMeasurerTime = new FlowMeasurerTime();

    public void particlesFlowed(List<Particle> particles, Double time){
        for (int i = 0; i < particles.size(); i++) {
            particleFlowed(time);
        }
    }

    public void particleFlowed(Double time){
        flowMeasurerAmount.particleFlowed(time);
        flowMeasurerAmountNorm.particleFlowed(time);
        flowMeasurerTime.particleFlowed(time);
    }

    //returns in time between 10 particles
    public Double getFlowByAmount(){
        return flowMeasurerAmount.getFlow();
    }

    //Returns in particles/second with particle window
    public Double getFlowByAmountNorm(){
        return flowMeasurerAmountNorm.getFlowNorm();
    }

    //Returns in particles/second with time window
    public Double getFlowByTime(Double time){
        return flowMeasurerTime.getFlow(time);
    }
}
