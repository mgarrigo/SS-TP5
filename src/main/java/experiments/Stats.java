package experiments;

public class Stats {
    private Double min;
    private Double max;
    private Double std;
    private Double mean;
    private Double median;

    public Double getValueByOperation(Operation operation){
        switch (operation){
            case MIN:
                return min;
            case MAX:
                return max;
            case MEAN:
                return mean;
            case STD:
                return std;
            case MEDIAN:
                return median;
            case STD_LOW:
                return mean - std;
            case STD_HIGH:
                return mean + std;
        }
        throw new IllegalStateException("No operation found");
    }

    public Stats(Double min, Double max, Double std, Double mean, Double median) {
        this.min = min;
        this.max = max;
        this.std = std;
        this.mean = mean;
        this.median = median;
    }
}
