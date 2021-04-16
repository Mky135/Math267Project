import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class main {

    static ArrayList<Double> rabbitPercentages;
    static ArrayList<Double> grassPercentages;
    static ArrayList<PeakIndex> peakIndices;
    static ArrayList<PeakIndex> maxes;
    static ArrayList<PeakIndex> mins;
    static ArrayList<PeakIndex> tropeIndex;
    static ArrayList<Double> peaks;


    public static void main(String[] args) {
        FileHandler fileHandler = new FileHandler("src/data.txt");
        rabbitPercentages = new ArrayList<>();
        grassPercentages = new ArrayList<>();
        peakIndices = new ArrayList<>();
        maxes = new ArrayList<>();
        peaks = new ArrayList<>();
        mins = new ArrayList<>();
        tropeIndex = new ArrayList<>();

        for (int i = 0; i < fileHandler.getNumOfLines(); i++) {
            formatLine(fileHandler.getLine(i));
        }

        System.out.println(rabbitPercentages);
        System.out.println(grassPercentages);

        lookForPeaks(rabbitPercentages);
        siftThroughPeaks();
        averagePeaks();
        averagePeriod();

        peakIndices = new ArrayList<>();
        maxes = new ArrayList<>();
        peaks = new ArrayList<>();

        lookForPeaks(grassPercentages);
        siftThroughPeaks();
        averagePeaks();
        averagePeriod();
        writeToFile();
    }

    public static void averagePeriod() {
        ArrayList<Integer> differencesInPeaks = new ArrayList<>();
        for (int i = 0; i < peakIndices.size() - 1; i++) {
            int dif = peakIndices.get(i+1).index - peakIndices.get(i).index;

            if(dif >= 10)
                differencesInPeaks.add(dif);
        }

        System.out.println(differencesInPeaks);

        double avgDif = 0;
        for (int i = 0; i < differencesInPeaks.size(); i++) {
            if(differencesInPeaks.get(i) >= 10) {
                avgDif += differencesInPeaks.get(i);
            }
        }

        avgDif = avgDif/differencesInPeaks.size();
        System.out.println("Average Period: " + avgDif);
    }

    public static void averagePeaks() {
        double sumOfPeaks = 0;

        for (PeakIndex peak : peakIndices) {
            sumOfPeaks += peak.peak;
        }

        System.out.println("Average of Peaks: " + sumOfPeaks / peakIndices.size());
    }

    public static void siftThroughPeaks() {
        double maxPeak = 0;
        double threshold = 20   ;

        for (PeakIndex peakIndex: peakIndices) {
            maxPeak = Math.max(maxPeak, peakIndex.peak);
        }

        ArrayList<PeakIndex> toRemove = new ArrayList<>();
        for (PeakIndex peakIndex : peakIndices) {
            if(peakIndex.peak <= maxPeak - threshold) {
                toRemove.add(peakIndex);
            }
        }

        for (PeakIndex peakIndexToRemove: toRemove) {
            peakIndices.remove(peakIndexToRemove);
        }
    }

    public static void lookForPeaks(ArrayList<Double> list) {
        double peak;

        for (int i = 1; i < list.size() - 1; i++) {
            if (list.get(i-1) < list.get(i)
                    && list.get(i+1) < list.get(i)) {
                peak = list.get(i);
                peakIndices.add(new PeakIndex(peak, i));
//                System.out.println("Peak: " + peak);
            }
        }
    }


    public static void formatLine(String line) {
        double rabbitPercent;
        double grassPercent;

        //      2.7%          0%        20.9%
        line = line.trim();
        rabbitPercent = Double.parseDouble(line.substring(0, line.indexOf("%")));
        line = line.substring(line.indexOf("%") + 1);
        line = line.trim();
        line = line.substring(line.indexOf("%") + 1);
        line = line.trim();
        grassPercent = Double.parseDouble(line.substring(0, line.indexOf("%")));
        rabbitPercentages.add(rabbitPercent);
        grassPercentages.add(grassPercent);
    }


    public static void writeToFile() {
        try {
            File myObj = new File("src/rabbitPeaks.txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                myObj.delete();
                myObj.createNewFile();
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        try {
            FileWriter myWriter = new FileWriter("src/rabbitPeaks.txt");
            for (Double peak : peaks) {
                myWriter.write(peak + "\n");
            }
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}

class PeakIndex {

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