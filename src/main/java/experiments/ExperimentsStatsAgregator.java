package experiments;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ExperimentsStatsAgregator<K extends Enum> {
    List<ExperimentStatsHolder<K>> holders;
    List<Function<StringBuilder,StringBuilder>> builder = new ArrayList<>();


    public ExperimentsStatsAgregator() {
        holders = new ArrayList<>();
    }

    public void addStatsHolder(ExperimentStatsHolder<K> experimentStatsHolder){
        holders.add(experimentStatsHolder);
    }

    public StringBuilder buildStatsOutput(Operation... operations){
        return buildStatsOutput(Arrays.asList(operations));
    }

    public StringBuilder buildStatsOutput(List<Operation> operations){
        StringBuilder stringBuilder = new StringBuilder();
        Set<K> activeSeries = new HashSet<>();
        holders.stream().forEach(serie -> activeSeries.addAll(serie.getActiveSeries()));
        
        List<AgregatedSeries<K>> timeseriesStats = activeSeries.stream().map(serie ->
            new AgregatedSeries<>(fillStats(holders.stream()
                    .map(holder -> holder.getDataSeries(serie))
                    .filter( list -> !list.isEmpty())
                    .collect(Collectors.toList())),serie)
        ).collect(Collectors.toList());

        System.out.println("Header order");
        for (AgregatedSeries<K> timeseriesStat : timeseriesStats) {
            timeseriesStat.addHeaders(stringBuilder, operations);
        }
        stringBuilder.append("\n");
        Integer maxTimeseriesLenght = timeseriesStats.stream().mapToInt(AgregatedSeries::size).max().getAsInt();
        if(timeseriesStats.size() > 0){
            for (int i = 0; i < maxTimeseriesLenght; i++) {
                for (AgregatedSeries<K> agregatedSeries : timeseriesStats) {
                    agregatedSeries.addStats(stringBuilder, operations, i);
                }
                stringBuilder.append("\n");
            }
        }
        return stringBuilder;
    }

    public static Double getStandardDeviation(List<Double> values) {
        Double mean = values.stream().mapToDouble( x -> x).average().getAsDouble();
        return getStandardDeviation(values,mean);
    }

    public static Double getStandardDeviation(List<Double> values, Double mean) {
        Double standardDeviation = values.stream()
                .mapToDouble(x -> Math.pow(x - mean, 2))
                .sum();

        return Math.sqrt(standardDeviation /(values.size()-1));
    }

    public List<Stats> fillStats(List<List<Double>> timeseries){
        List<Stats> statsList = new ArrayList<>();
        Integer minLenght = getMinLenght(timeseries);
        for (int i = 0; i < minLenght; i++) {
            Double min = Double.MAX_VALUE;
            Double max = -Double.MAX_VALUE;
            Double mean = 0.0;
            Double std;
            Double median = null;
            List<Double> values = new ArrayList<>();
            for (int j = 0; j < timeseries.size(); j++) {
                Double analized = timeseries.get(j).get(i);
                min = min < analized?min:analized;
                max = max > analized?max:analized;
                mean += analized/timeseries.size();
                values.add(analized);
                if(j == timeseries.size()/2){
                    median = analized;
                }
            }
            std = getStandardDeviation(values,mean);
            Stats stats = new Stats(min,max,std,mean,median);
            statsList.add(stats);
        }
        return statsList;
    }

    public Integer getMinLenght(List<List<Double>> timeseries){
        Integer min = Integer.MAX_VALUE;
        for(List<Double> timeserie: timeseries){
            min = min < timeserie.size()?min:timeserie.size();
        }
        return min;
    }

    public static <U extends Enum> ExperimentsStatsAgregator<U> getFromHolders(ExperimentStatsHolder<U>... holders){
        ExperimentsStatsAgregator<U> agregator = new ExperimentsStatsAgregator<>();
        Arrays.stream(holders).forEach(agregator::addStatsHolder);
        return agregator;
    }
}
