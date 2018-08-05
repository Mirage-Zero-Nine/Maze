import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ListIterator;

/**
 * MazeComponent class
 * A component that displays the maze and path through it if one has been found.
 *
 * @author BorisMirage
 * Time: 2018/07/26 20:50
 * Created with IntelliJ IDEA
 */


public class MazeComponent extends JComponent {

    private static final int START_X = 10;      // top left of corner of maze in frame
    private static final int START_Y = 10;
    private static final int BOX_WIDTH = 20;    // width and height of one maze "location"
    private static final int BOX_HEIGHT = 20;
    private static final int INSET = 2;         // how much smaller on each side to make entry/exit inner box

    private Maze maze;

    /**
     * Constructs the component.
     *
     * @param maze the maze to display
     */
    public MazeComponent(Maze maze) {

        this.maze = maze;
    }

    /**
     * Draws the current state of maze including the path through it if one has
     * been found.
     *
     * @param g the graphics context
     */
    public void paintComponent(Graphics g) {

        /* Get entry and exit coord */
        MazeCoord entry = maze.getEntryLoc();
        MazeCoord exit = maze.getExitLoc();
        int entryRow = entry.getRow();
        int entryCol = entry.getCol();
        int exitRow = exit.getRow();
        int exitCol = exit.getCol();

        /* Recover Graphics2D */
        Graphics2D g2 = (Graphics2D) g;

        /* Draw the border */
        Rectangle border = new Rectangle(START_X, START_Y, maze.numCols() * BOX_WIDTH, maze.numRows() * BOX_HEIGHT);
        g2.setColor(Color.BLACK);
        g2.draw(border);

        /* Draw the maze */
        drawMaze(g2);

        /* Draw the entry location */
        int entryX = START_X + entryCol * BOX_WIDTH + INSET;
        int entryY = START_Y + entryRow * BOX_HEIGHT + INSET;
        Rectangle entryLocation = new Rectangle(entryX, entryY, BOX_WIDTH - 2 * INSET, BOX_WIDTH - 2 * INSET);
        g2.setColor(Color.YELLOW);
        g2.draw(entryLocation);
        g2.fill(entryLocation);

        /* Draw the exit location */
        int exitX = START_X + exitCol * BOX_WIDTH + INSET;
        int exitY = START_Y + exitRow * BOX_HEIGHT + INSET;
        Rectangle exitLocation = new Rectangle(exitX, exitY, BOX_WIDTH - 2 * INSET, BOX_HEIGHT - 2 * INSET);
        g2.setColor(Color.GREEN);
        g2.draw(exitLocation);
        g2.fill(exitLocation);

        /* Draw the path*/
        if (maze.getPath().size() != 0) {
            drawPath(g2);
        }

        /* Draw grid */
        drawGrid(g2);
    }

    /**
     * Draw maze wall
     *
     * @param g2 2-D graphics context
     */
    private void drawMaze(Graphics2D g2) {
        for (int i = 0; i < maze.numRows(); i++) {
            for (int j = 0; j < maze.numCols(); j++) {
                int currentX = START_Y + j * BOX_WIDTH;
                int currentY = START_X + i * BOX_HEIGHT;
                MazeCoord temp = new MazeCoord(i, j);
                if (maze.hasWallAt(temp)) {
                    Rectangle Wall = new Rectangle(currentX, currentY, BOX_WIDTH, BOX_HEIGHT);
                    g2.setColor(Color.BLACK);
                    g2.draw(Wall);
                    g2.fill(Wall);
                }
            }
        }
    }

    /**
     * Draw path to exit (if exist)
     *
     * @param g2 2-D graphics context
     */
    private void drawPath(Graphics2D g2) {

        ListIterator<MazeCoord> iter = maze.getPath().listIterator();

        MazeCoord location1 = iter.next();

        while (iter.hasNext()) {

            /* Get next location */
            MazeCoord location2 = iter.next();

            /* Set coord */
            int x1 = START_X + location1.getCol() * BOX_WIDTH + BOX_WIDTH / 2;
            int y1 = START_Y + location1.getRow() * BOX_HEIGHT + BOX_HEIGHT / 2;
            int nextX = START_X + location2.getCol() * BOX_WIDTH + BOX_WIDTH / 2;
            int nextY = START_Y + location2.getRow() * BOX_HEIGHT + BOX_HEIGHT / 2;

            /* Draw line */
            Line2D.Double segment = new Line2D.Double(x1, y1, nextX, nextY);
            g2.setColor(Color.RED);
            g2.draw(segment);
            location1 = location2;
        }
    }

    /**
     * Draw maze grid.
     *
     * @param g2 input Graphics2D
     */
    private void drawGrid(Graphics2D g2) {

        /* Draw parallel lines */
        for (int i = 0; i < maze.numRows(); i++) {
            int x1 = START_X;
            int y1 = START_Y + i * BOX_HEIGHT;
            int x2 = START_X + maze.numCols() * BOX_WIDTH;
            int y2 = START_Y + i * BOX_HEIGHT;
            Line2D.Double segment = new Line2D.Double(x1, y1, x2, y2);
            g2.setColor(Color.GRAY);
            g2.draw(segment);

        }

        /* Draw vertical lines */
        for (int j = 0; j < maze.numCols(); j++) {
            int x1 = START_X + j * BOX_WIDTH;
            int y1 = START_Y;
            int x2 = START_X + j * BOX_HEIGHT;
            int y2 = START_Y + maze.numRows() * BOX_HEIGHT;
            Line2D.Double segment = new Line2D.Double(x1, y1, x2, y2);
            g2.setColor(Color.GRAY);
            g2.draw(segment);
        }
    }
}





