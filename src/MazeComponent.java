import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.LinkedList;

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
        int entryRow = maze.getEntryLoc().getRow();
        int entryCol = maze.getEntryLoc().getCol();
        int exitRow = maze.getExitLoc().getRow();
        int exitCol = maze.getExitLoc().getCol();

        Graphics2D g2 = (Graphics2D) g;

        /* Draw border */
        Rectangle border = new Rectangle(START_X, START_Y, maze.numCols() * BOX_WIDTH, maze.numRows() * BOX_HEIGHT);
        g2.setColor(Color.BLACK);
        g2.draw(border);

        /* Draw maze wall */
        drawMaze(g2);

        /* Draw entry */
        int entryX = START_X + entryCol * BOX_WIDTH + INSET;
        int entryY = START_Y + entryRow * BOX_HEIGHT + INSET;
        Rectangle entryLocation = new Rectangle(entryX, entryY, BOX_WIDTH - 2 * INSET, BOX_WIDTH - 2 * INSET);
        g2.setColor(Color.YELLOW);
        g2.draw(entryLocation);
        g2.fill(entryLocation);

        /* Draw exit */
        int exitX = START_X + exitCol * BOX_WIDTH + INSET;
        int exitY = START_Y + exitRow * BOX_HEIGHT + INSET;
        Rectangle exitLocation = new Rectangle(exitX, exitY, BOX_WIDTH - 2 * INSET, BOX_HEIGHT - 2 * INSET);
        g2.setColor(Color.GREEN);
        g2.draw(exitLocation);
        g2.fill(exitLocation);

        /* Draw path*/
        if (maze.getPath().size() != 0) {
            drawPath(g2);
        }

        /* Draw grid */
        drawGrid(g2);
    }

    /**
     * Draw maze wall in black rectangle.
     *
     * @param g2 2-D graphics context
     */
    private void drawMaze(Graphics2D g2) {
        for (int i = 0; i < maze.numRows(); i++) {
            for (int j = 0; j < maze.numCols(); j++) {
                int currentX = START_Y + j * BOX_WIDTH;
                int currentY = START_X + i * BOX_HEIGHT;
                if (maze.hasWallAt(new MazeCoord(i, j))) {
                    Rectangle mazeWall = new Rectangle(currentX, currentY, BOX_WIDTH, BOX_HEIGHT);
                    g2.setColor(Color.DARK_GRAY);
                    g2.draw(mazeWall);
                    g2.fill(mazeWall);
                }
            }
        }
    }

    /**
     * Draw path from to exit if path is existing.
     * Each "path" component is a rectangle so that can be seen more directly.
     *
     * @param g2 2-D graphics context
     */
    private void drawPath(Graphics2D g2) {

        /* Copy path */
        LinkedList<MazeCoord> finalPath = new LinkedList<>(maze.getPath());

        /* Avoid entry and exit to be replaced */
        for (int i = 1; i < finalPath.size() - 1; i++) {
            MazeCoord cur = finalPath.get(i);
            int x1 = START_X + cur.getCol() * BOX_WIDTH;
            int y1 = START_Y + cur.getRow() * BOX_HEIGHT;
            Rectangle segment = new Rectangle(x1, y1, BOX_WIDTH, BOX_HEIGHT);

            /* Set path fill color */
            Color pathColor = new Color(83, 142, 217);

            g2.setColor(pathColor);
            g2.draw(segment);
            g2.fill(segment);
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
            int y1 = START_Y + i * BOX_HEIGHT;
            int x2 = START_X + maze.numCols() * BOX_WIDTH;
            int y2 = START_Y + i * BOX_HEIGHT;
            Line2D.Double segment = new Line2D.Double(START_X, y1, x2, y2);
            g2.setColor(Color.GRAY);
            g2.draw(segment);

        }

        /* Draw vertical lines */
        for (int j = 0; j < maze.numCols(); j++) {
            int x1 = START_X + j * BOX_WIDTH;
            int x2 = START_X + j * BOX_HEIGHT;
            int y2 = START_Y + maze.numRows() * BOX_HEIGHT;
            Line2D.Double segment = new Line2D.Double(x1, START_Y, x2, y2);
            g2.setColor(Color.GRAY);
            g2.draw(segment);
        }
    }
}





