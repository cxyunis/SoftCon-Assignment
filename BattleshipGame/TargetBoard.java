import java.util.ArrayList;
import java.util.List;

public class TargetBoard extends GameBoard {
    //private List<ShipGrid> playedShip = new ArrayList<>();  // (7) remove this

    // <9> new nested class for internal usage, good for encapsulation
    class FullShipLoc {
        private Ship aShip;
        private String[] gridLoc;   // store [A1,A2,...]
        private boolean shipSank = false;   // only update after first call, check getBoardDisplay

        public FullShipLoc(Ship aShip,List<String> gridPos) {
            //getting the size of the array of the fleet of ships
            this.aShip = aShip;
            this.gridLoc = new String[gridPos.size()];
            for (int i=0; i<gridPos.size(); i++) {
                this.gridLoc[i] = gridPos.get(i);
            }
        }
        public int[][] getBoardIndex() {
            // get indexes to the game board (10x10 grids)
            // return Nx2 integer array where 1st index is row, 2nd index is col
            int N = gridLoc.length;
            int[][] rowCol = new int[N][2];
            int row, col;
            if (gridLoc[0].charAt(0)==gridLoc[1].charAt(0)) {
                // vertical placement
                col = gridLoc[0].charAt(0)-65;
                for (int i=0; i<N; i++) {
                    rowCol[i][0] =  Integer.parseInt(gridLoc[i].substring(1,2));
                    rowCol[i][1] = col;
                }
            } else {
                // horizontal
                row = Integer.parseInt(gridLoc[0].substring(1,2));
                for (int i=0; i<N; i++) {
                    rowCol[i][0] = row;
                    rowCol[i][1] = gridLoc[i].charAt(0)-65;
                }
            }
            return rowCol;
        }
        public void setShipSank() { this.shipSank = true; }
        public Ship getShip() { return this.aShip; }
        public boolean hadSunk() { return shipSank; } // may not up-to-date at the first run
        public String getMarkerID() {
            return aShip.name().substring(0,1);
        }
        public String[] getBlockPosition() { return gridLoc; }
    }
    private List<FullShipLoc> shipFleet = new ArrayList<>(); // (8) add in this
    public TargetBoard(String name) {
        super(name);
    }

    // <12> override, with additional task, i.e. stores grids occupied by a ship
    public void setShipOnBoard(List<String> posList, Ship aShip) {
        super.setShipOnBoard(posList,aShip);
        shipFleet.add(new FullShipLoc(aShip,posList));  // additional task compared to super class
    }

    // <10> remove this
//    public void updatePlayedShip(Ship ship, List<String> posList) {
//        // a list of element (ShipGrid), ShipGrid contains 1 ship occupied many grids
//        playedShip.add(new ShipGrid(ship,posList));
//    }
    //<11> remove this
//    public String updateAttackedShipGrid(String pos) {
//        // playedShip contains 10 ShipGrid (10 ships/chips)
//        // 1 ShipGrid contains a) Ship, b) Ship.length grids
//        for (ShipGrid sg: playedShip) {
//            if (sg.updateGridPositionStatus(pos)) {
//                break;  //HERE
//            }
//        }
//        return "o";
//    }

    private String[][] getACopyOfTargetBoard() {
        // get a copy of board status, only containing O,X
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

    public String[] getFinalDisplay() {
        // when machine player wins
        return super.getBoardDisplay();
    }

    //<13> rewrite this
    public String[] getBoardDisplay() {
        String[] board = new String[16];
        String s;
        board[0] = "===== "+boardLabel+" =====";
        board[1] = "  A B C D E F G H I J  ";
        board[2] = " +-+-+-+-+-+-+-+-+-+-+ ";

        String[][] dispBoard = getACopyOfTargetBoard();
        //int[] rowCol = new int[2];

        // update dispBoard with sank ship (replace X with shipID)
        int totalGrid;  // total grids occupied by the ship
        int[][] rowCol ;// totalGridx2 integer array identify board indices, 1st index is row, 2nd index is col
        String shipID;  // expecting C,B,S,P
        int count;      // should be equal to totalGrid if the ship has sank
        int row, col;
        for (FullShipLoc fullShip: shipFleet) {
            shipID = fullShip.getMarkerID();
            rowCol = fullShip.getBoardIndex();  // get array of indexes of the board occupied by this ship
            totalGrid = fullShip.getBlockPosition().length;
            count = 0; // count how many X, if full (i.e. equal to totalGrid), then replace with shipID

            for (int i=0; i<totalGrid; i++) {
                row = rowCol[i][0];
                col = rowCol[i][1];
                if (dispBoard[row][col].equals("X")) {
                    count++;
                }
            }
            // remove partial ship but remain X
            if (count==totalGrid) {
                for (int i=0; i<totalGrid; i++) {
                    row = rowCol[i][0];
                    col = rowCol[i][1];
                    dispBoard[row][col] = shipID;
                }
            } else {
                for (int i=0; i<totalGrid; i++) {
                    row = rowCol[i][0];
                    col = rowCol[i][1];
                    if (!dispBoard[row][col].equals("X")) {
                        dispBoard[row][col] = "E";
                    }
                }
            }
        }
        for (int i=0; i<GRID_DIMENSION; i++) {
            s = Integer.toString(i);
            for (int j = 0; j < GRID_DIMENSION; j++) {
                if (dispBoard[i][j].equals("E")) {
                    //to take care of an empty grid showing up instead of "E"
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


//    public String[] getBoardDisplay() {
//        String[] board = new String[16];
//        String s;
//        board[0] = "===== "+boardLabel+" =====";
//        board[1] = "  A B C D E F G H I J  ";
//        board[2] = " +-+-+-+-+-+-+-+-+-+-+ ";
//
//        String[][] dispBoard = getACopyOfTargetBoard();
//        int[] rowCol = new int[2];
//
//        for (ShipGrid sg: playedShip) {
//            if (sg.hasSank()) {
//                // the ship has sank, all the grids it occupied
//                String[][] aState = sg.getStates();
//                for (int i=0; i<aState.length; i++) {
//                    rowCol = this.convertToRowCol(aState[i][0]);
//                    dispBoard[rowCol[0]][rowCol[1]] = aState[i][1];
//                }
//            }
//        }
//        for (int i=0; i<GRID_DIMENSION; i++) {
//            s = Integer.toString(i);
//            for (int j = 0; j < GRID_DIMENSION; j++) {
//                if (dispBoard[i][j].equals("E")) {
//                    s += "| ";
//                } else {
//                    s += "|" + dispBoard[i][j];
//                }
//            }
//            board[i+3] = s+"|"+Integer.toString(i);
//        }
//        board[13] = " +-+-+-+-+-+-+-+-+-+-+ ";
//        board[14] = "  A B C D E F G H I J  ";
//        board[15] = "=======================";
//        return board;
//    }
}
