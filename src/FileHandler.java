import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author Miguel Flores
 * A class to ease the handling of files
 */
public class FileHandler extends File {
    /**
     * The path to the file
     */
    private final String fileName;

    /**
     * Creates a new file object using the directory of the fileName
     * @param fileName
     */
    public FileHandler(String fileName) {
        super(fileName);
        this.fileName = fileName;
    }

    /**
     * Gets a line of file
     *
     * @param line the line number of the line to be returned
     * @return String of the line specified
     */
    public String getLine(int line) {
        try {
            return Files.readAllLines(Paths.get(fileName)).get(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getNumOfLines()
    {
        try {
            return Files.readAllLines(Paths.get(fileName)).size();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
}