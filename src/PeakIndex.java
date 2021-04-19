public class PeakIndex {

    int index;
    double peak;

    PeakIndex(double peak, int index) {
        this.peak = peak;
        this.index = index;
    }

    @Override
    public String toString() {
        return "PeakIndex{" +
                "index=" + index +
                ", peak=" + peak +
                '}';
    }
}
