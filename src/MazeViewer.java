import javax.swing.*;
import java.io.*;
import java.util.LinkedList;

/**
 * Main function of Maze
 * The test maze file path should be regarded as program argument.
 *
 * @author BorisMirage
 * Time: 2018/07/26 20:52
 * Created with IntelliJ IDEA
 */

public class MazeViewer {

    private static final char WALL_CHAR = '1';
    private static final char FREE_CHAR = '0';

    /**
     * Main class that required maze file path as argument.
     *
     * @param args maze file path
     */
    public static void main(String[] args) {

        String fileDirectory = "./src/testfiles";
        File testFile = new File(fileDirectory);
        LinkedList<String> filePath = new LinkedList<>();
        String[] nameList = testFile.list();
        if (nameList != null) {
            for (String aNameList : nameList) {
                String temp = fileDirectory + "/" + aNameList;
                filePath.add(temp);
            }
        }
        try {
            while (filePath.size() != 0) {
                JFrame frame = readMazeFile(filePath.pop());
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }
        } catch (FileNotFoundException exc) {
            System.out.println("ERROR: File not found: " + fileDirectory);
        } catch (IOException exc) {
            exc.printStackTrace();
        }
        System.out.println(filePath);

    }

    /**
     * Read maze file and convert it to Maze data.
     * This method will read data in maze file and create a new MazeFrame object for display and Maze path searching.
     *
     * @param fileName maze file path
     * @return new MazeFrame for later usage
     * @throws IOException file does not find via given path
     */
    private static MazeFrame readMazeFile(String fileName) throws IOException {

        /* Create a File object and using Reader to read it. */
        File readMazeFile = new File(fileName);

        /* Create a input reader to read data and save to buffer. */
        InputStreamReader newReader = new InputStreamReader(new FileInputStream(readMazeFile));
        BufferedReader newBufferRead = new BufferedReader(newReader);

        /* Read first line about new maze, convert to integer. */
        String firstLine = newBufferRead.readLine();
        String[] para = firstLine.split(" ");
        int mazeRow = Integer.parseInt(para[0]);
        int mazeColumn = Integer.parseInt(para[1]);

        int[][] readMazeData = new int[mazeRow][mazeColumn];

        /* Read following lines. */
        for (int i = 0; i < mazeRow; i++) {
            String line = newBufferRead.readLine();
            for (int j = 0; j < line.length(); j++) {
                char current = line.charAt(j);

                /* Free = false, Wall = true.
                 *  Free = 0, Wall = 1. */
                if (current == WALL_CHAR) {
                    readMazeData[i][j] = -1;
                } else if (current == FREE_CHAR) {
                    readMazeData[i][j] = Integer.MAX_VALUE - 1;
                }
            }
        }

        /* Read last two lines to obtain elements: entry and exit. */
        String startLine = newBufferRead.readLine();
        String exitLine = newBufferRead.readLine();

        int startRow = Integer.parseInt(startLine.split(" ")[0]);
        int startColumn = Integer.parseInt(startLine.split(" ")[1]);
        int exitRow = Integer.parseInt(exitLine.split(" ")[0]);
        int exitColumn = Integer.parseInt(exitLine.split(" ")[1]);
        MazeCoord newMazeStart = new MazeCoord(startRow, startColumn);
        MazeCoord newMazeExit = new MazeCoord(exitRow, exitColumn);

        return new MazeFrame(readMazeData, newMazeStart, newMazeExit);
    }

}