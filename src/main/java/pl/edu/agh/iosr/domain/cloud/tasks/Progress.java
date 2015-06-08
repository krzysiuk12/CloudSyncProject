package pl.edu.agh.iosr.domain.cloud.tasks;

public class Progress {

    private final double value;

    public Progress(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }
}
