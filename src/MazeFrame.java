import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Maze frame class.
 * This class determine the shape of maze and path line that displayed.
 *
 * @author BorisMirage
 * Time: 2018/07/26 20:52
 * Created with IntelliJ IDEA
 */


public class MazeFrame extends JFrame {

    private JLabel searchStatusLabel;

    private MazeComponent mazeComponent;

    private Maze maze;

    private static final int FRAME_WIDTH = 500;
    private static final int FRAME_HEIGHT = 500;

    private static final String PROMPT_STRING = "Type any key to start maze search...";
    private static final String SUCCESS_STRING = "Path was found!";
    private static final String FAIL_STRING = "No path can be found from entry to exit.";

    /**
     * Sets up GUI components with the given maze data.
     * Meanwhile, it will add a key adapter.
     *
     * @param mazeData store maze data
     * @param entryLoc the entry location of the maze
     * @param exitLoc  the exit location of the maze
     */
    public MazeFrame(int[][] mazeData, MazeCoord entryLoc, MazeCoord exitLoc) {

        setSize(FRAME_WIDTH, FRAME_HEIGHT);

        searchStatusLabel = new JLabel(PROMPT_STRING);
        add(searchStatusLabel, BorderLayout.NORTH); // put label at the top of the frame
        maze = new Maze(mazeData, entryLoc, exitLoc);

        mazeComponent = new MazeComponent(maze);
        add(mazeComponent, BorderLayout.CENTER); // put maze display in the middle of the frame

        KeyAdapter listener = new MazeKeyListener(); // defined below
        addKeyListener(listener); // process keyboard input
        setFocusable(true);
    }


    /**
     * getSearchMessage returns the message to display for a successful
     * or failed search.
     *
     * @param success whether the search succeeded
     * @return the string to display
     */
    public String getSearchMessage(boolean success) {
        if (success) {
            return SUCCESS_STRING;
        } else {
            return FAIL_STRING;
        }
    }


    /**
     * Listening to input key action.
     */
    class MazeKeyListener extends KeyAdapter { // inner class -- has access to outer object's instance variables
        int count = 0;

        /**
         * keyPressed is called when the user types a character.
         * The action taken is to do the maze search, then update the display according to the results of the search.
         *
         * @param event What the user typed. Ignored here.
         */

        public void keyPressed(KeyEvent event) {
            if (count == 0) {
                count = 1;

                /* Terminal Output */
                System.out.println("DEBUG: key pressed");
                System.out.println("DEBUG: doing maze search. . . ");

                boolean success = maze.search();

                /* Update drawing to show the results */
                mazeComponent.repaint();

                System.out.println("DEBUG: " + getSearchMessage(success));
                searchStatusLabel.setText(getSearchMessage(success));
                System.out.println("Finished! ");
            }
        }
    }
}

