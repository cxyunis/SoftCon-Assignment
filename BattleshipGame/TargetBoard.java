import java.util.ArrayList;
import java.util.List;

public class TargetBoard extends GameBoard {
    private List<ShipGrid> playedShip = new ArrayList<>();
    public TargetBoard(String name) {
        super(name);
    }

    public void updatePlayedShip(Ship ship, List<String> posList) {
        // a list of element (ShipGrid), ShipGrid contains 1 ship occupied many grids
        playedShip.add(new ShipGrid(ship,posList));
    }
    public String updateAttackedShipGrid(String pos) {
        // playedShip contains 10 ShipGrid (10 ships/chips)
        // 1 ShipGrid contains a) Ship, b) Ship.length grids
        for (ShipGrid sg: playedShip) {
            if (sg.updateGridPositionStatus(pos)) {
                break;
            }
        }
        return "o";
    }

    private String[][] getACopyOfTargetBoard() {
        // get a copy of board status, only contain O,X
        String s;
        String[][] copyBoard = new String[GRID_DIMENSION][GRID_DIMENSION];
        for (int i=0; i<GRID_DIMENSION; i++) {
            for (int j = 0; j < GRID_DIMENSION; j++) {
                if (this.aBoard[i][j].equals("o")) {
                    copyBoard[i][j] = "o";
                } else if (this.aBoard[i][j].equals("X")) {
                    copyBoard[i][j] = "X";
                } else {
                    copyBoard[i][j] = "E";
                }
            }
        }
        return copyBoard;
    }
    private String[][] getACopyOfGameOverTargetBoard() {
        // get a copy of board status, if machine player won the game
        String s;
        String[][] copyBoard = new String[GRID_DIMENSION][GRID_DIMENSION];
        for (int i=0; i<GRID_DIMENSION; i++) {
            for (int j = 0; j < GRID_DIMENSION; j++) {
                if (this.aBoard[i][j].equals("o")) {
                    copyBoard[i][j] = "o";
                } else if (this.aBoard[i][j].equals("X")) {
                    copyBoard[i][j] = "X";
                } else {
                    copyBoard[i][j] = aBoard[i][j];
                }
            }
        }
        return copyBoard;
    }

    public String[] getFinalDisplay() {
        // when machine player wins
        return super.getBoardDisplay();
    }

    public String[] getBoardDisplay() {
        String[] board = new String[16];
        String s;
        board[0] = "===== "+boardLabel+" =====";
        board[1] = "  A B C D E F G H I J  ";
        board[2] = " +-+-+-+-+-+-+-+-+-+-+ ";

        String[][] dispBoard = getACopyOfTargetBoard();
        int[] rowCol = new int[2];

        for (ShipGrid sg: playedShip) {
            if (sg.hasSank()) {
                // the ship has sank, all the grids it occupied
                String[][] aState = sg.getStates();
                for (int i=0; i<aState.length; i++) {
                    rowCol = this.convertToRowCol(aState[i][0]);
                    dispBoard[rowCol[0]][rowCol[1]] = aState[i][1];
                }
            }
        }
        for (int i=0; i<GRID_DIMENSION; i++) {
            s = Integer.toString(i);
            for (int j = 0; j < GRID_DIMENSION; j++) {
                if (dispBoard[i][j].equals("E")) {
                    s += "| ";
                } else {
                    s += "|" + dispBoard[i][j];
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
