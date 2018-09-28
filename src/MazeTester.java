import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * Test function
 *
 * @author BorisMirage
 * Time: 2018/07/26 20:52
 * Created with IntelliJ IDEA
 */


public class MazeTester {


    private static final char WALL_CHAR = '1';
    private static final char FREE_CHAR = '0';

    /**
     * Test main class.
     *
     * @param args argument, has no usage
     */
    public static void main(String[] args) {

        String fileDirectory = "./src/TestMaze";
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
                readMazeFile(filePath.pop());
            }
        } catch (FileNotFoundException exc) {
            System.out.println("ERROR: File not found: " + fileDirectory);
        } catch (IOException exc) {
            exc.printStackTrace();
        }
        System.out.println(filePath);
    }

    /**
     * Same as readMazeFile class in StartMaze.
     *
     * @param fileName maze file path
     * @throws IOException file does not found exception
     */
    private static void readMazeFile(String fileName) throws IOException {

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

        int[][] newMazeData = new int[mazeRow][mazeColumn];

        /* Read following lines. */
        for (int i = 0; i < mazeRow; i++) {
            String line = newBufferRead.readLine();
            for (int j = 0; j < line.length(); j++) {
                char current = line.charAt(j);

                /* Free = 0, Wall = 1. */
                if (current == WALL_CHAR) {
                    newMazeData[i][j] = -1;
                } else if (current == FREE_CHAR) {
                    newMazeData[i][j] = Integer.MAX_VALUE;
                }
            }
        }

        String startLine = newBufferRead.readLine();
        String exitLine = newBufferRead.readLine();
        int startRow = Integer.parseInt(startLine.split(" ")[0]);
        int startColumn = Integer.parseInt(startLine.split(" ")[1]);
        int exitRow = Integer.parseInt(exitLine.split(" ")[0]);
        int exitColumn = Integer.parseInt(exitLine.split(" ")[1]);
        newMazeData[startRow][startColumn] = 1;

        int[][] isv = new int[mazeRow][mazeColumn];
        int[][] rest = res(newMazeData, isv, startRow, startColumn);
        for (int[] aa : rest) {
            System.out.println(Arrays.toString(aa));
        }
        System.out.println(rest[exitRow][exitColumn]);

    }

    /**
     * Test finding path algorithm.
     *
     * @param data        maze data
     * @param visitRecord visit time record
     * @param row         current position row
     * @param col         current position column
     * @return maze data that contains min distance from entry to exit
     */
    private static int[][] res(int[][] data, int[][] visitRecord, int row, int col) {

        System.out.println(row + " " + col + " " + data[row][col]);

        if (checkA(data, row + 1, col) && data[row][col] + 1 < data[row + 1][col]) {
            data[row + 1][col] = data[row][col] + 1;
        }

        if (checkA(data, row, col + 1) && data[row][col] + 1 < data[row][col + 1]) {
            data[row][col + 1] = data[row][col] + 1;
        }

        if (checkA(data, row - 1, col) && data[row][col] + 1 < data[row - 1][col]) {
            data[row - 1][col] = data[row][col] + 1;
        }

        if (checkA(data, row, col - 1) && data[row][col] + 1 < data[row][col - 1]) {
            data[row][col - 1] = data[row][col] + 1;
        }

        if (row == 2 && col == 3) {
            System.out.println(data[row][col]);
        }
        if (check(data, row + 1, col, visitRecord)) {
            visitRecord[row + 1][col] += 1;
            data[row + 1][col] = Math.min(data[row][col] + 1, data[row + 1][col]);
            res(data, visitRecord, row + 1, col);
        }
        if (check(data, row, col + 1, visitRecord)) {
            visitRecord[row][col + 1] += 1;
            data[row][col + 1] = Math.min(data[row][col] + 1, data[row][col + 1]);
            res(data, visitRecord, row, col + 1);
        }
        if (check(data, row - 1, col, visitRecord)) {
            visitRecord[row - 1][col] += 1;
            data[row - 1][col] = Math.min(data[row][col] + 1, data[row - 1][col]);
            res(data, visitRecord, row - 1, col);
        }
        if (check(data, row, col - 1, visitRecord)) {
            visitRecord[row][col - 1] += 1;
            data[row][col - 1] = Math.min(data[row][col] + 1, data[row][col - 1]);
            res(data, visitRecord, row, col - 1);
        }
        return data;
    }

    /**
     * Check position availability.
     *
     * @param data   maze data
     * @param row    current position row
     * @param column current position column
     * @param v      visit time record
     * @return if position is available
     */
    private static boolean check(int[][] data, int row, int column, int[][] v) {
        if (row > data.length - 1 || row < 0) {
            return false;
        }
        if (column > data[0].length - 1 || column < 0) {
            return false;
        }
        return data[row][column] != -1 && v[row][column] < 5;
    }

    /**
     * Check position availability without visit time
     *
     * @param data   maze data
     * @param row    current position row
     * @param column current position column
     * @return if position is available
     */
    private static boolean checkA(int[][] data, int row, int column) {
        if (row > data.length - 1 || row < 0) {
            return false;
        }
        if (column > data[0].length - 1 || column < 0) {
            return false;
        }
        return data[row][column] != -1;
    }
}
