import java.util.List;

public class GameBoard {

    /* Super class of specific board, i.e. OceanBoard and TargetBoard */

    public static final String COLUMN_HEADER = "ABCDEFGHIJ";
    public static final int GRID_DIMENSION = 10;

    protected String boardLabel;    // label appears on top of the board
    protected final String[][] aBoard = new String[GRID_DIMENSION][GRID_DIMENSION];


    public GameBoard (String name) {
        boardLabel = name;
        for (int i=0; i<GRID_DIMENSION; i++) {
            for (int j=0; j<GRID_DIMENSION; j++) {
                aBoard[i][j] = "E";
            }
        }
    }

    // (5) change to private method
    private void placeShipOnBoard(GridValue gridVal) {
        int[] rc = convertToRowCol(gridVal.getPosition());
        int r = rc[0];
        int c = rc[1];
        aBoard[r][c] = gridVal.getMarkerID();
    }
    // (4) move to here from GameModel
    public void setShipOnBoard(List<String> posList, Ship ship) {
        // place ships on grid, no validation of input parameters because confirmed inputs are correct
        // posList is a list of position for ship: e.g. [A4,A5] for Patrol
        for (String pos: posList) {
            GridValue gv = new GridValue(pos,ship);
            placeShipOnBoard(gv);
        }
    }
    public boolean isOccupied(String blockPos) {
        String s = this.getGridStatus(blockPos);
        boolean got_ship = s.equals("C") || s.equals("S") ||
                           s.equals("P") || s.equals("B");
        return got_ship;
    }
    protected boolean isHit(String blockPos) {
        String s = this.getGridStatus(blockPos);
        return s.equals("X") || s.equals("o");
    }

    public boolean setAttackAt(String blockPos) {
        /* return false: either has X or O, set again
        *  return true : got ship or empty
        * */
        int[] rc = convertToRowCol(blockPos);
        int r = rc[0];
        int c = rc[1];
        if (isHit(blockPos)) {
            return false;   // invalid position
        } else if (isOccupied(blockPos)) {
            aBoard[r][c] = "X";
        } else {
            aBoard[r][c] = "o";
        }   //HERE
        return true;
    }

    protected int[] convertToRowCol(String blockPos) {
        int r = rowToInteger(blockPos.charAt(1));    // 2 of A2
        int c = columnToInteger(blockPos.charAt(0)); // A of A2
        int[] rc = {r,c};
        return rc;
    }
    private int columnToInteger(char col) {
        int c = (int) col;
        return c-65;
    }
    private int rowToInteger(char row) {
        int r = (int) row;
        return r-48;    // char value 48 is 0, 49 is 1,...
    }
    private String indexToRowCol(int r, int c) {
        //String[] col = {"A","B","C","D","E","F","G","H","I","J"};
        String[] col = GameBoard.COLUMN_HEADER.split("");
        String[] row = {"0","1","2","3","4","5","6","7","8","9"};
        return col[c]+row[r];
    }

    private boolean hasShipAt(String blockPos) {
        if (isOccupied(blockPos)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isGameOver() {
        // can be improved in speed
        String blockPos;
        int count = 0;
        int expectedCount = GRID_DIMENSION*GRID_DIMENSION;
        for (int r=0; r<GRID_DIMENSION; r++) {
            for (int c=0; c<GRID_DIMENSION; c++) {
                blockPos = indexToRowCol(r,c);
                if (!hasShipAt(blockPos)) {
                    count++;
                }
            }
        }
        return (count==expectedCount);
    }

    protected String getGridStatus(String blockPos) {
        int[] rc = convertToRowCol(blockPos);
        return aBoard[rc[0]][rc[1]];
    }
    public String[] getBoardDisplay() {
        String[] board = new String[16];
        String s;
        board[0] = "===== "+boardLabel+" =====";
        board[1] = "  A B C D E F G H I J  ";
        board[2] = " +-+-+-+-+-+-+-+-+-+-+ ";
        for (int i=0; i<GRID_DIMENSION; i++) {
            s = Integer.toString(i);
            for (int j = 0; j < GRID_DIMENSION; j++) {
                if (this.aBoard[i][j].equals("E")) {
                    s += "| ";
                } else {
                    s += "|" + this.aBoard[i][j];
                }
            }
            board[i+3] = s+"|"+Integer.toString(i);
        }
        board[13] = " +-+-+-+-+-+-+-+-+-+-+ ";
        board[14] = "  A B C D E F G H I J  ";
        board[15] = "=======================";
        return board;
    }

}
