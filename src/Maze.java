import java.util.Arrays;
import java.util.LinkedList;
import java.util.Stack;


/**
 * Maze class that defines the method to obtain the info of total rows & columns.
 * Also in this class search shortest method is defined (searchPath).
 * The searchPath method will be called by MazeFrame. If searchPath returns true, then path will be found and draw.
 *
 * @author BorisMirage
 * Time: 2018/07/26 20:49
 * Created with IntelliJ IDEA
 */
public class Maze {

    private MazeCoord entry;
    private MazeCoord exit;
    private int[][] data;       // int array that store walls and distance
    private int[][] close;      // whether this point will be revisited in further recursion
    private LinkedList<MazeCoord> path = new LinkedList<>();
    private Stack<MazeCoord> pathStack = new Stack<>();

    /**
     * Necessary info that to construct a maze.
     *
     * @param mazeData 2D int array that store the info of maze (wall, space, distance).
     * @param startLoc MazeCoord start location
     * @param exitLoc  MazeCoord exit location
     */
    public Maze(int[][] mazeData, MazeCoord startLoc, MazeCoord exitLoc) {
        entry = startLoc;
        exit = exitLoc;
        data = mazeData;
        close = new int[data.length][data[0].length];
    }

    /**
     * Get number of rows in maze.
     *
     * @return number of rows in maze
     */
    public int numRows() {
        return data.length;
    }

    /**
     * Get number of columns in maze.
     *
     * @return number of columns in maze.
     */
    public int numCols() {
        return data[0].length;
    }

    /**
     * Check if input MazeCoord has wall.
     *
     * @param loc input location
     * @return true if input location has wall, otherwise return false.
     */
    public boolean hasWall(MazeCoord loc) {
        return data[loc.getRow()][loc.getCol()] == -1;
    }

    /**
     * Get entry MazeCoord.
     * Used in MazeComponent.
     *
     * @return entry MazeCoord
     */
    public MazeCoord getEntryLoc() {
        return entry;
    }

    /**
     * Get exit MazeCoord.
     * Used in MazeComponent.
     *
     * @return exit MazeCoord
     */
    public MazeCoord getExitLoc() {
        return exit;
    }

    /**
     * Get path from entry to exit.
     * Used in MazeComponent.
     *
     * @return path from entry to exit
     */
    public LinkedList<MazeCoord> getPath() {
        return new LinkedList<>(path);
    }

    /**
     * Get represented value store in mazeData based on input MazeCoord.
     *
     * @param coord input MazeCoord
     * @return represented value store in 2D int array mazeData
     */
    private int getData(MazeCoord coord) {
        return data[coord.getRow()][coord.getCol()];
    }

    /**
     * Get value store in mazeData based on input MazeCoord.
     *
     * @param coord input MazeCoord
     * @param value value to be set
     */
    private void setData(MazeCoord coord, int value) {
        data[coord.getRow()][coord.getCol()] = value;
    }

    /**
     * Called by outside of class to initialize the finding process.
     *
     * @return if there is a shortest path
     */
    public boolean searchPath() {

        /* Direct check */
        if (hasWall(entry) || hasWall(exit)) {
            return false;
        }

        /* One-element maze */
        if (entry.equals(exit) && !hasWall(entry)) {
            path.add(exit);
            return true;
        }

        setData(entry, 1);
        findShortestPath(entry);
        if (getData(exit) != 0) {
            path = new LinkedList<>(generatePath(pathStack));
            return true;
        }
        return false;
    }

    /**
     * Find shortest path in maze using Dijkstra algorithm.
     *
     * @param c current coord
     */
    private void findShortestPath(MazeCoord c) {
        close[c.getRow()][c.getCol()] = 1;
        MazeCoord next;
        for (int i = 0; i < 4; i++) {
            next = moveNext(c, i);
            if (isAvailable(next) > -1 && (getData(next) == 0 || getData(next) > getData(c) + 1)) {
                setData(next, getData(c) + 1);
                close[next.getRow()][next.getCol()] = 0;        // Open this point
            }
            if (isAvailable(next) > 0) {
                findShortestPath(next);
            }
        }
    }

    /**
     * Check given MazeCoord is available to be moved or not.
     *
     * @param c MazeCoord
     * @return if this coord can be moved to or not
     */
    private int isAvailable(MazeCoord c) {

        /* If can basically move to this point */
        if (c.getRow() > numRows() - 1 || c.getRow() < 0 || c.getCol() > numCols() - 1 || c.getCol() < 0 || hasWall(c)) {
            return -1;
        }
        if (close[c.getRow()][c.getCol()] == 1) {
            return 0;
        }
        return 1;
    }

    /**
     * Generate shortest path depending on the data that set in findShortestPath.
     *
     * @param s stack for storing inverse order path
     * @return path from entry to exit
     */
    private LinkedList<MazeCoord> generatePath(Stack<MazeCoord> s) {
        MazeCoord move;
        MazeCoord temp = exit;
        MazeCoord coord = exit;
        s.push(exit);
        while (!coord.equals(entry)) {
            int min = getData(coord);
            for (int i = 0; i < 4; i++) {
                move = moveNext(coord, i);
                if (isAvailable(move) > -1 && getData(move) < min) {
                    min = getData(move);
                    temp = move;
                }
            }
            coord = temp;
            s.push(coord);
        }

        /* Reverse order */
        while (!s.empty()) {
            path.add(s.pop());
        }
        return path;
    }

    /**
     * Move to input MazeCoord to selected direction.
     * Sum of opposite direction (i.e, up and down) is 3.
     *
     * @param coord  input MazeCoord
     * @param orient 0 - move upward
     *               1 - move left
     *               2 - move right
     *               3 - move down
     * @return MazeCoord that after movement
     */
    private MazeCoord moveNext(MazeCoord coord, int orient) {
        int r = coord.getRow();
        int c = coord.getCol();

        if (orient == 0) {
            return new MazeCoord(r - 1, c);
        } else if (orient == 1) {
            return new MazeCoord(r, c - 1);
        } else if (orient == 2) {
            return new MazeCoord(r, c + 1);
        } else if (orient == 3) {
            return new MazeCoord(r + 1, c);
        } else {
            System.out.println("Wrong Orientation! " + orient);
            return coord;
        }
    }

    /**
     * Print distance data as debug purpose.
     */
    private void printData() {
        for (int[] aData : data) {
            System.out.println(Arrays.toString(aData));
        }
    }
}