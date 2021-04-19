import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class main {

    static ArrayList<Double> rabbitPercentages;
    static ArrayList<Double> grassPercentages;
    static ArrayList<PeakIndex> peakIndices;
    static ArrayList<PeakIndex> maxes;
    static ArrayList<PeakIndex> troughsIndex;
    static ArrayList<Double> peaks;


    static int squareCount = 25*30;
    static double averagePeakForRabbit;
    static double averageTroughForRabbit;
    static double averagePeriodForRabbit;

    static double averagePeakForGrass;
    static double averageTroughForGrass;
    static double averagePeriodForGrass;

    public static void main(String[] args) {
        FileHandler fileHandler = new FileHandler("src/data.txt");
        rabbitPercentages = new ArrayList<>();
        grassPercentages = new ArrayList<>();
        peakIndices = new ArrayList<>();
        maxes = new ArrayList<>();
        peaks = new ArrayList<>();
        troughsIndex = new ArrayList<>();

        for (int i = 0; i < fileHandler.getNumOfLines(); i++) {
            formatLine(fileHandler.getLine(i));
        }

//        System.out.println(rabbitPercentages);
//        System.out.println(grassPercentages);

        System.out.println("Rabbit Info: ");
        lookForPeaks(rabbitPercentages);
        siftThroughPeaks();
        averagePeakForRabbit = averagePeaks();
        lookForTroughs(rabbitPercentages);
        siftThroughTroughs();
        averageTroughForRabbit = averageTroughs();
        averagePeriodForRabbit = averagePeriod();

        peakIndices = new ArrayList<>();
        maxes = new ArrayList<>();
        peaks = new ArrayList<>();
        troughsIndex = new ArrayList<>();

        System.out.println("\nGrass Info: ");
        lookForPeaks(grassPercentages);
        siftThroughPeaks();
        averagePeakForGrass = averagePeaks();
        lookForTroughs(grassPercentages);
        siftThroughTroughs();
        averageTroughForGrass = averageTroughs();
        averagePeriodForGrass = averagePeriod();
        writeToFile();
    }


    public static double averagePeriod() {
        ArrayList<Integer> differencesInPeaks = new ArrayList<>();
        for (int i = 0; i < peakIndices.size() - 1; i++) {
            int dif = peakIndices.get(i+1).index - peakIndices.get(i).index;

            if(dif >= 10)
                differencesInPeaks.add(dif);
        }

        double avgDif = 0;
        for (int i = 0; i < differencesInPeaks.size(); i++) {
            if(differencesInPeaks.get(i) >= 10) {
                avgDif += differencesInPeaks.get(i);
            }
        }

        avgDif = avgDif/differencesInPeaks.size();
        System.out.println("Average Period: " + avgDif);
        return avgDif;
    }

    public static double averagePeaks() {
        double sumOfPeaks = 0;

        for (PeakIndex peak : peakIndices) {
            sumOfPeaks += peak.peak;
        }

        System.out.println("Average of Peaks: " + ((sumOfPeaks / peakIndices.size())/100) * squareCount);
        return ((sumOfPeaks / peakIndices.size())/100) * squareCount;
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

    private static double averageTroughs() {
        double sumOfTropes = 0;

        for (PeakIndex peak : troughsIndex) {
            sumOfTropes += peak.peak;
        }

        System.out.println("Average of Troughs: " + ((sumOfTropes / troughsIndex.size())/100) * squareCount);
        return ((sumOfTropes / troughsIndex.size())/100) * squareCount;
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

    public static void lookForTroughs(ArrayList<Double> list) {
        double trope;

        for (int i = 1; i < list.size() - 1; i++) {
            if (list.get(i-1) > list.get(i)
                    && list.get(i+1) > list.get(i)) {
                trope = list.get(i);
                troughsIndex.add(new PeakIndex(trope, i));
//                System.out.println("Peak: " + peak);
            }
        }
    }

    public static void siftThroughTroughs() {
        double minTrope = 100;
        double threshold = 20;

        for (PeakIndex peakIndex: troughsIndex) {
            minTrope = Math.min(minTrope, peakIndex.peak);
        }

        ArrayList<PeakIndex> toRemove = new ArrayList<>();
        for (PeakIndex peakIndex : troughsIndex) {
            if(peakIndex.peak >= minTrope + threshold) {
                toRemove.add(peakIndex);
            }
        }

        for (PeakIndex peakIndexToRemove: toRemove) {
            troughsIndex.remove(peakIndexToRemove);
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
            File myObj = new File("src/output.txt");
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
            FileWriter myWriter = new FileWriter("src/output.txt");

            myWriter.write("Rabbit Info:\n" +
                    "Average of Peaks: " + averagePeakForRabbit +
                    "\nAverage of Troughs: " + averageTroughForRabbit +
                    "\nAverage Period: " + averagePeriodForRabbit);

            myWriter.write("\n\nGrassInfo:\n"+
                    "Average of Peaks: " + averagePeakForGrass +
                    "\nAverage of Troughs: " + averageTroughForGrass +
                    "\nAverage Period: " + averagePeriodForGrass);

            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}