import java.util.ArrayList;
import java.util.List;
import java.util.Random;
public class MachinePlayer implements InputSource {

    private String name;

    private String[][] enemyBoard = new String[GameBoard.GRID_DIMENSION][GameBoard.GRID_DIMENSION];

    class NeighbourHood {

        List<String> listOfXPos = new ArrayList<>();

        public void add(String xPos) {
            listOfXPos.add(xPos);
        }
        public String getAttackAt() {
            String x;
            int row, col;

            while(!listOfXPos.isEmpty()) {
                x = listOfXPos.get(0);
                row = Integer.parseInt(x.substring(1,2));
                col = x.charAt(0)-65;
                if (col>0) {
                    if (enemyBoard[row][col-1].equals("E")) {
                        return gridPosition(row,col-1);
                    }
                }
                if (col<GameBoard.GRID_DIMENSION-1) {
                    if (enemyBoard[row][col+1].equals("E")) {
                        return gridPosition(row,col+1);
                    }
                }
                if (row>0) {
                    if (enemyBoard[row-1][col].equals("E")) {
                        return gridPosition(row-1,col);
                    }
                }
                if (row<GameBoard.GRID_DIMENSION-1) {
                    if (enemyBoard[row+1][col].equals("E")) {
                        return gridPosition(row+1,col);
                    }
                }
                listOfXPos.remove(0);
            }
            Random rnd = new Random();
            row = rnd.nextInt(GameBoard.GRID_DIMENSION);
            col = rnd.nextInt(GameBoard.GRID_DIMENSION);
            String pos = gridPosition(row,col);
            return pos;
        }
        private String gridPosition(int row, int col) {
            return GameBoard.COLUMN_HEADER.substring(col,col+1)+row;
        }
    }

    private NeighbourHood trackX = new NeighbourHood();

    public MachinePlayer(String name) {
        this.name = name;
        for (int i=0; i<GameBoard.GRID_DIMENSION; i++) {
            for (int j=0; j<GameBoard.GRID_DIMENSION; j++) {
                enemyBoard[i][j] = "E";
            }
        }
    }

    private String[] getBlockPosition(Ship ship) {
        Random rnd = new Random();
        int randomCol1 = 0;
        int col2 = 0;
        String colHdr1 = "";
        String colHdr2 = "";
        int randomRow1 = 0;
        int row2 = 0;
        int size = ship.getSize();
        boolean accepted = false;

        // decide horizontal (0) or vertical (1) filling
        int hozOrVer = rnd.nextInt(2);  // 0 or 1

        while(!accepted) {
            // randomly pick the first position
            randomCol1 = rnd.nextInt(GameBoard.GRID_DIMENSION);
            colHdr1 = GameBoard.COLUMN_HEADER.substring(randomCol1, randomCol1 + 1);
            randomRow1 = rnd.nextInt(GameBoard.GRID_DIMENSION);

            if (hozOrVer == 0) {
                // horizontal filling, fixed row
                col2 = randomCol1+size-1;
                // check if selected end block position is within the board
                if (col2<GameBoard.GRID_DIMENSION) {
                    colHdr2 = GameBoard.COLUMN_HEADER.substring(col2,col2+1);
                    row2 = randomRow1;
                    accepted = true;
                }
            } else {
                // vertical filling, fix col
                row2 = randomRow1+size-1;
                // check if selected end block position is within the board
                if (row2<GameBoard.GRID_DIMENSION) {
                    colHdr2 = colHdr1;
                    accepted = true;
                }
            }
        }
        String[] pos =  {colHdr1+randomRow1,colHdr2+row2};
        return pos;
    };
    @Override
    public String[] getShipPlacement(Ship shipModel, int shipNo) {
        String[] shipPos = new String[2];
        shipPos = getBlockPosition(shipModel);
        return shipPos;
    };
    public String getAttackAt() {
        return trackX.getAttackAt();
    };
    public void updateAttackAtStatus(String gridPos, String gridMarker) {
        // requires update from GameModel if hit (X) or not (o), then
        // update opponent board for internal use of selecting next attacking position
        int col = gridPos.charAt(0)-65;
        int row = gridPos.charAt(1)-48;
        enemyBoard[row][col] = gridMarker;   // only o or X
        if (gridMarker.equals("X")) {
            trackX.add(gridPos);
        }
    }

//    public void displayBoard() {
//        String line;
//        for (int i=0; i<GameBoard.GRID_DIMENSION; i++) {
//            line = "";
//            for (int j=0; j<GameBoard.GRID_DIMENSION; j++) {
//                if (enemyBoard[i][j].equals("E")) {
//                    line += "| ";
//                } else {
//                    line += "|"+enemyBoard[i][j];
//                }
//            }
//            System.out.println(line);
//        }
//    }
}
