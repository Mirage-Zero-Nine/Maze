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
        findPath(entry);
        System.out.println(Arrays.deepToString(data));
        System.out.println(Arrays.deepToString(close));
        if (getData(exit) != 0) {
            traceBackPath(exit);
            path = new LinkedList<>(pathOutput(pathStack));
            return true;
        }
        return false;
    }


    /**
     * Find shortest path in maze using Dijkstra algorithm.
     *
     * @param c current coord
     */
    private void findPath(MazeCoord c) {
        close[c.getRow()][c.getCol()] = 1;
        System.out.println(c.toString());
        MazeCoord next;

        for (int i = 0; i < 4; i++) {
            next = moveNext(c, i);
            if (isAvailable(next) > -1 && (getData(next) == 0 || getData(next) > getData(c) + 1)) {
                setData(next, getData(c) + 1);
                close[next.getRow()][next.getCol()] = 0;        // Open this point

            }
        }
        for (int i = 0; i < 4; i++) {
            next = moveNext(c, i);
            if (isAvailable(next) > 0) {
                findPath(next);
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
     * Pop each element in stack into LinkedList path and return it as final path output.
     *
     * @param s stack that store entire path
     * @return path LinkedList
     */
    private LinkedList<MazeCoord> pathOutput(Stack<MazeCoord> s) {
        LinkedList<MazeCoord> inOrderPath = new LinkedList<>();

        /* Make path in order */
        while (s.size() != 0) {
            inOrderPath.add(s.pop());
        }
        return inOrderPath;
    }

    /**
     * Trace min distance recursively from exit to entry, which is in inverse order.
     * Hence, use FIFO stack to store when adding.
     * Pop each MazeCoord in pathOutput to reverse path in order.
     *
     * @param pos current MazeCoord
     */
    private void traceBackPath(MazeCoord pos) {

        /* Store current position */
        pathStack.push(pos);

        /* If current position is not entry (path finish point), continue process */
        if (!pos.equals(entry)) {
            traceBackPath(findMinNext(pos));
        }
    }

    /**
     * Find min distance in four possible direction based on input MazeCoord.
     * Have to check the availability of each direction first to avoid wall or out of boundary.
     *
     * @param coord input MazeCoord
     * @return next MazeCoord that has min distance to entry
     */
    private MazeCoord findMinNext(MazeCoord coord) {
        int min = getData(coord);
        MazeCoord next = coord;
        for (int i = 0; i < 4; i++) {
            if (isAvailable(moveNext(coord, i)) > -1 && getData(moveNext(coord, i)) < min) {
                min = getData(moveNext(coord, i));
                next = moveNext(coord, i);
            }
        }
        return next;
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
        int newCol = coord.getCol();
        int newRow = coord.getRow();

        if (orient == 0) {
            return new MazeCoord(newRow - 1, newCol);
        } else if (orient == 1) {
            return new MazeCoord(newRow, newCol - 1);
        } else if (orient == 2) {
            return new MazeCoord(newRow, newCol + 1);
        } else if (orient == 3) {
            return new MazeCoord(newRow + 1, newCol);
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