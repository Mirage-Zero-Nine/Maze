import java.security.InvalidParameterException;

/**
 * This class define some maze property.
 *
 * @author BorisMirage
 * Time: 2018/07/26 20:51
 * Created with IntelliJ IDEA
 */

public class MazeCoord {

    final private int row;
    final private int col;

    /**
     * Pass para into class
     *
     * @param row row number (start from 0)
     * @param col column number (start from 0)
     */
    public MazeCoord(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * Get current coord's row number.
     *
     * @return int row number
     */
    public int getRow() {
        return row;
    }

    /**
     * Get current coord's column number.
     *
     * @return int column number
     */
    public int getCol() {
        return col;
    }

    /**
     * Override equals method to check whether two coord is same.
     *
     * @param other input object(MazeCoord)
     * @return true if two coord is same, otherwise return false
     */
    public boolean equals(Object other) {

        if (other == null) {
            return false;
        }

        if (!(other instanceof MazeCoord)) {
            return false;
        }

        MazeCoord otherCoord = (MazeCoord) other;

        return this.row == otherCoord.row && this.col == otherCoord.col;
    }

    /**
     * Convert MazeCoord to format string for better output.
     *
     * @return MazeCoord in string
     */
    public String toString() {
        return "MazeCoord[row=" + row + ",col=" + col + "]";
    }

    /**
     * Return a moved coord on given direction.
     * Sum of opposite direction (i.e, up and down) is 3.
     *
     * @param orientation 0 - move upward
     *                    1 - move left
     *                    2 - move right
     *                    3 - move down
     * @return move MazeCoord
     * @throws InvalidParameterException orientation is invalid
     */
    public MazeCoord move(int orientation) {
        int r = this.row;
        int c = this.col;

        switch (orientation) {
            case 0:
                return new MazeCoord(r - 1, c);     // move up

            case 1:
                return new MazeCoord(r, c - 1);     // move left

            case 2:
                return new MazeCoord(r, c + 1);     // move right

            case 3:
                return new MazeCoord(r + 1, c);     // move down

            default:
                throw new InvalidParameterException("Invalid Orientation: " + orientation + " !");       // error message
        }
    }

}
