import java.util.Random;
public class MachinePlayer implements InputSource {

    String name;

    public MachinePlayer(String name) { this.name = name; }

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
        Random rnd = new Random();
        int randomCol = rnd.nextInt(GameBoard.GRID_DIMENSION);
        String colHdr = GameBoard.COLUMN_HEADER.substring(randomCol,randomCol+1);
        int randomRow = rnd.nextInt(GameBoard.GRID_DIMENSION);
        return colHdr+randomRow;
    };

}
