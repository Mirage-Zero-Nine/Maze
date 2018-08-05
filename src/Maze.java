import java.util.Arrays;
import java.util.LinkedList;
import java.util.Stack;

/**
 * Maze class.
 * This class defines the method to obtain the info of total rows & columns.
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
    private int[][] data;
    private LinkedList<MazeCoord> path = new LinkedList<>();
    private int[][] visitTimes;      // Record coord visited times
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
        data = mazeData;                // int array that store walls and distance
        visitTimes = new int[data.length][data[0].length];
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
    public boolean hasWallAt(MazeCoord loc) {
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
     * Search path from entry to exit.
     * Since the min distance was found through exit to entry, path needs to be reversed in pathOutput.
     * This method can be accessed outside of Maze class.
     *
     * @return true if there exist a path from entry to exit, otherwise false
     */
    public boolean searchPath() {

        if (hasWallAt(entry) || hasWallAt(exit)) {
            return false;
        }

        /* One-element maze */
        if (entry.equals(exit) && !hasWallAt(entry)) {
            path.add(exit);
            return true;
        }

        /* Set initial data into mazeData */
        setData(entry, 1);

        /* Fill each reachable MazeCoord in maze and put min distance into it */
        tryNext(entry, -1);
        System.out.println("DEBUG: Showing maze data. . . ");
        printData();

        /* Check if exit is visited hence assure if there is a path or not */
        if (getData(exit) != Integer.MAX_VALUE - 1) {
            traceBackPath(exit);
            path = new LinkedList<>(pathOutput(pathStack));
            return true;
        }
        return false;
    }

    /**
     * Try possible next coord recursively until each coord has been visited 4 times.
     * This recursion contains orientation in order to avoid duplicate movement.
     * It will try move direction that is not same as input orientation.
     * If all 3 different direction is not available, it will finally try last incoming direction.
     *
     * @param cur current MazeCoord
     * @param ori orientation from previous movement.
     *            i.e, if current MazeCoord came from previous MazeCoord move upward, then the input orientation is 0.
     *            See moveNext for more orientation int info
     */
    private void tryNext(MazeCoord cur, int ori) {

        /* First compare distance in current MazeCoord */
        greedy(cur);

        MazeCoord next;
        if (ori == -1) {
            for (int i = 0; i < 4; i++) {
                if (checkCoord(moveNext(cur, i), visitTimes) > 0) {
                    next = moveNext(cur, i);
                    addVisit(next);
                    tryNext(next, i);
                }
            }
        } else {
            for (int i = 0; i < 4; i++) {

                /* Avoid try incoming direction and check availability */
                if (3 - ori != i && checkCoord(moveNext(cur, i), visitTimes) > 0) {
                    next = moveNext(cur, i);
                    addVisit(next);
                    tryNext(next, i);
                }
            }
        }
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
            if (checkCoord(moveNext(coord, i), visitTimes) > -1 && getData(moveNext(coord, i)) < min) {
                min = getData(moveNext(coord, i));
                next = moveNext(coord, i);
            }
        }
        return next;
    }

    /**
     * Greedy method to fill the mazeData array with min distance to entry.
     *
     * @param coord input MazeCoord
     */
    private void greedy(MazeCoord coord) {
        for (int i = 0; i < 4; i++) {
            if (checkCoord(moveNext(coord, i), visitTimes) > -1) {
                setMin(coord, moveNext(coord, i));
            }
        }
    }

    /**
     * Set min distance in two continues coord.
     * coord1 and coord2
     *
     * @param coord1 coord 1
     * @param coord2 coord 2
     */
    private void setMin(MazeCoord coord1, MazeCoord coord2) {
        int curData = getData(coord1);
        int nextData = getData(coord2);
        if (curData + 1 < nextData) {
            setData(coord2, curData + 1);
        }
        if (nextData + 1 < curData) {
            setData(coord1, nextData + 1);
        }
    }

    /**
     * Check if input position can be moved to.
     *
     * @param c           input coord
     * @param visitRecord 2D int array record visit times
     * @return -1 if out of bound or has wall, 0 if visited more than 4 times, or 1 if available.
     */
    private int checkCoord(MazeCoord c, int[][] visitRecord) {
        if (c.getRow() > numRows() - 1 || c.getRow() < 0 || c.getCol() > numCols() - 1 || c.getCol() < 0 || hasWallAt(c)) {
            return -1;
        } else if (visitRecord[c.getRow()][c.getCol()] > 3) {
            return 0;
        } else {
            return 1;
        }
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
     * Add one to represented coord in visitTimes.
     *
     * @param coord input coord
     */
    private void addVisit(MazeCoord coord) {
        visitTimes[coord.getRow()][coord.getCol()] += 1;
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