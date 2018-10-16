import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Stack;


/**
 * Maze class that defines the method to obtain the info of total rows & columns.
 * In <code>StartMaze</code> class, it will read all maze file in <code>TestMaze</code> folder and create new mazes.
 * Each <code>Maze</code> structure contains entry & exit location, with all walls.
 * In this class, search path method is provided as <code>searchPath</code>, if path is found then it will return true.
 * If path is found, call <code>getPath</code> method will obtain the path and in <code>MazeFrame</code> will draw it.
 *
 * @author BorisMirage
 * Time: 2018/07/26 20:49
 * Created with IntelliJ IDEA
 */
public class Maze {

    private MazeCoord entry;
    private MazeCoord exit;
    private int[][] distance;       // int array that store walls and distance
    private boolean[][] close;      // true if this point is "closed", and false if this point is "open"
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
        distance = mazeData;
        close = new boolean[distance.length][distance[0].length];
    }

    /**
     * Get number of rows in maze.
     *
     * @return number of rows in maze
     */
    public int numRows() {
        return distance.length;
    }

    /**
     * Get number of columns in maze.
     *
     * @return number of columns in maze.
     */
    public int numCols() {
        return distance[0].length;
    }

    /**
     * Check if input MazeCoord has wall.
     *
     * @param loc input location
     * @return true if input location has wall, otherwise return false.
     */
    public boolean hasWall(MazeCoord loc) {
        return distance[loc.getRow()][loc.getCol()] == -1;
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
        return distance[coord.getRow()][coord.getCol()];
    }

    /**
     * Get value store in mazeData based on input MazeCoord.
     *
     * @param coord input MazeCoord
     * @param value value to be set
     */
    private void setData(MazeCoord coord, int value) {
        distance[coord.getRow()][coord.getCol()] = value;
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
     * In this approach one 2D boolean array <code>close</code> is included.
     * This array store the status of each MazeCoord can be searched in future or not.
     * If current MazeCoord's corresponding position in array is marked as "true", then this coord will not be visited.
     * During the search process, mark current MazeCoord as "close", then update all four directions' min distance.
     * If any previous MazeCoord that is marked as "close" updated its distance, remark it as "open".
     * Since if one MazeCoord has update its min distance, then it may update its related MazeCoord.
     * Finally, if all MazeCoord has been marked as their min distance from entry to exit, then end this search process.
     *
     * @param c current coord
     */
    private void findShortestPath(MazeCoord c) {

        /* Mark this point to close */
        close[c.getRow()][c.getCol()] = true;
        MazeCoord next;
        for (int i = 0; i < 4; i++) {
            next = moveCoord(c, i);
            if (isAvailable(next) > -1 && (getData(next) == 0 || getData(next) > getData(c) + 1)) {
                setData(next, getData(c) + 1);
                close[next.getRow()][next.getCol()] = false;        // Open this point
            }
            if (isAvailable(next) > 0) {
                findShortestPath(next);
            }
        }
    }

    /**
     * Check given MazeCoord is available to be moved or not.
     * If given MazeCoord is out of bound or wall existing, then return -1. (Unreachable)
     * If given MazeCoord is "close" in respective <code>close</code> array, then return 0. (Don't search)
     * Otherwise, return true.
     *
     * @param c MazeCoord
     * @return if this coord can be moved to or not
     */
    private int isAvailable(MazeCoord c) {

        /* If can basically move to this point */
        if (c.getRow() > numRows() - 1 || c.getRow() < 0 || c.getCol() > numCols() - 1 || c.getCol() < 0 || hasWall(c)) {
            return -1;
        }
        if (close[c.getRow()][c.getCol()]) {
            return 0;
        }
        return 1;
    }

    /**
     * Generate shortest path depending on the distance that set in findShortestPath.
     * Use a stack to store path from exit to entry.
     * If trace from entry to exit, then different path will be found, hence increases complexity.
     * After this trace process is done (reach entry), pop stack content into <code>LinkedList path</code>.
     * In this way to generate final output path.
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
                move = moveCoord(coord, i);
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
     * Return a moved coord on given direction.
     * Sum of opposite direction (i.e, up and down) is 3.
     *
     * @param coord  input MazeCoord
     * @param orient 0 - move upward
     *               1 - move left
     *               2 - move right
     *               3 - move down
     * @return MazeCoord that after movement
     * @throws InvalidParameterException orientation is invalid
     */
    private MazeCoord moveCoord(MazeCoord coord, int orient) throws InvalidParameterException {
        int r = coord.getRow();
        int c = coord.getCol();

        if (orient == 0) {
            return new MazeCoord(r - 1, c);     // move up
        }
        if (orient == 1) {
            return new MazeCoord(r, c - 1);     // move left
        }
        if (orient == 2) {
            return new MazeCoord(r, c + 1);     // move right
        }
        if (orient == 3) {
            return new MazeCoord(r + 1, c);     // move down
        }
        throw new InvalidParameterException("Invalid Orientation: " + orient + " !");       // error message
    }

    /**
     * Print distance for debug purpose.
     */
    void printData() {
        for (int[] dist : distance) {
            System.out.println(Arrays.toString(dist));
        }
    }
}