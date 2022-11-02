import java.util.List;

public class GameBoard {

    protected String boardLabel;
    private final String[][] aBoard = new String[10][10];
    public GameBoard () {
        boardLabel = "GAME   GRID";
        for (int i=0; i<10; i++) {
            for (int j=0; j<10; j++) {
                aBoard[i][j] = "E"; //E = empty
            }
        }
    }

    public void placeShipOnBoard(GridValue gridVal) {
        int[] rc = convertToRowCol(gridVal.getPosition());
        int r = rc[0];
        int c = rc[1];
        aBoard[r][c] = gridVal.getMarkerID();
        System.out.println(aBoard[r][c]);
    }
    public boolean isOccupied(String blockPos) {
        String s = this.getGridStatus(blockPos);
        boolean got_ship = s.equals("C") | s.equals("S") |
                           s.equals("P") | s.equals("B");
        return got_ship;
    }

    /**
     * @param x position on board, 0<=x<10
     * @param y position on board, 0<=y<10
     */
    public void setAttackAt(String blockPos) {
        int[] rc = convertToRowCol(blockPos);
        int r = rc[0];
        int c = rc[1];

        if (isOccupied(blockPos)) {
            aBoard[r][c] = "X";
        } else {
            aBoard[r][c] = "O";
        }
    }

    private int[] convertToRowCol(String blockPos) {
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
        String[] col = {"A","B","C","D","E","F","G","H","I","J"};
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
        String blockPos;
        int count = 0;
        for (int r=0; r<10; r++) {
            for (int c=0; c<10; c++) {
                blockPos = indexToRowCol(r,c);
                if (!hasShipAt(blockPos)) {
                    count++;
                }
            }
        }
        return (count==100);
    }

    public String getGridStatus(String blockPos) {
        int[] rc = convertToRowCol(blockPos);
        return aBoard[rc[0]][rc[1]];
    }
    public String[] getBoardDisplay() {
        String[] board = new String[16];
        String s;
        board[0] = "===== "+boardLabel+" =====";
        board[1] = "  A B C D E F G H I J  ";
        board[2] = " +-+-+-+-+-+-+-+-+-+-+ ";
        for (int i=0; i<10; i++) {
            s = Integer.toString(i);
            for (int j = 0; j < 10; j++) {
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
        board[15] = "========================";
        return board;
    }

}
