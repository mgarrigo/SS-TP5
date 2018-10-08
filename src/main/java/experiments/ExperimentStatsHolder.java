package experiments;

import java.util.*;

public class ExperimentStatsHolder<K extends Enum> {
    private Map<K,List<Double>> dataSeries;

    public ExperimentStatsHolder() {
        this.dataSeries = new HashMap<>();
    }

    public void addDataPoint(K series, Double value){
        if(dataSeries.get(series) == null){
            dataSeries.put(series,new ArrayList<>());
        }
        dataSeries.get(series).add(value);
    }


    public Set<K> getActiveSeries(){
        return dataSeries.keySet();
    }

    public List<Double> getDataSeries(K serie) {
        if(!dataSeries.containsKey(serie)){
            return Collections.emptyList();
        }
        return dataSeries.get(serie);
    }
}
