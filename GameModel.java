//import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GameModel {

    // starting chips for each player
//    private static final List<Ship> boatFleet = Arrays.asList(Ship.Carrier,Ship.Battleship,Ship.Battleship,
//            Ship.Submarine,Ship.Submarine,Ship.Submarine,Ship.Patrol,Ship.Patrol,Ship.Patrol,Ship.Patrol);
    private List<Ship> boatFleet = new ArrayList<>();
    private GameBoard oceanBoard = new OceanBoard();  // Human player board
    private GameBoard targetBoard = new TargetBoard();  // machine player board
    private HumanPlayer oceanUser;  // an ocean user (human user)
    private MachinePlayer targetUser;  // an computerized user

    private String startingPlayer;

    public GameModel(String player1, String player2) {
        oceanUser = new HumanPlayer(player1);
        targetUser = new MachinePlayer(player2);

        // setup boat fleet the game
        for (Ship s: Ship.values()) {
            for (int i=0; i<s.getQuantity(); i++) {
                this.boatFleet.add(s);
            }
        }
    }

    public void startGame() {
        String[] blockPos;

        showGameBoard();

        for (Ship s: this.boatFleet) {
            boolean shipPlaced = false;
            List<GridValue> humanChips;

            while (!shipPlaced) {
                humanChips = askPlayerForShipPlacement(s);
                shipPlaced = setShipOnBoard(oceanBoard, humanChips);
            }
            showGameBoard();
        }


        boolean gameOver = false;
        while(!gameOver) {
            gameOver = true;
            //asking for attack position

            //check for game over
        }
    }

    private boolean setShipOnBoard(GameBoard userBoard, List<GridValue> gridVal) {
        boolean occupied;
        int count = 0;
        Ship boat = gridVal.get(0).getShip();

        List<String> listOfPos = getListOfPosition(gridVal.get(0),gridVal.get(1));

        // check if all grids are available
        for (String pos: listOfPos) {
            occupied = userBoard.isOccupied(pos);
            if (!occupied) {
                count++;
            }
        }
        if (count!=boat.getSize()) {
            System.out.println("count is not right!");
            return false;   // some of grid is occupied
        }

        // to fill
        for (String s: listOfPos) {
            GridValue gv = new GridValue(s,boat);
            userBoard.placeShipOnBoard(gv);
        }
        return true;
    }
    private List<String> getListOfPosition(GridValue start, GridValue stop) {
        String col = "ABCDEFGHIJ";   // revise

        String startCol = start.getPosition().substring(0,1);
        String startRowNo = start.getPosition().substring(1,2);
        String stopCol = stop.getPosition().substring(0,1);
        String stopRowNo = stop.getPosition().substring(1,2);
        int row1 = Integer.parseInt(startRowNo);
        int row2 = Integer.parseInt(stopRowNo);

        List<String> listOfPos = new ArrayList<>();
        if (startCol.equals(stopCol)) {
            // vertical filling
            for (int i=row1; i<row2+1; i++) {
                listOfPos.add(startCol+String.valueOf(i));
            }
        } else {
            // need to revise to ensure it is really horizontal filling
            int idx1 = col.indexOf(startCol);
            int idx2 = col.indexOf(stopCol);
            for (int i=idx1; i<idx2+1; i++) {
                listOfPos.add(col.substring(i,i+1)+startRowNo);
            }
        }
        return listOfPos;
    }

    private List<GridValue> askPlayerForShipPlacement(Ship ship) {
        // get input from player
        List<GridValue> shipPlacement = new ArrayList<>();
        GridValue gv;
        String[] loc;   // starting and ending location

        loc = oceanUser.getShipPlacement(ship);
        gv = new GridValue(loc[0],ship);    // starting location
        shipPlacement.add(gv);
        gv = new GridValue(loc[1],ship);    // ending location
        shipPlacement.add(gv);


        return shipPlacement;  // contains starting and ending location
    }

    public void setStartingPlayer() {
        Random rand = new Random();
        float selectPlayer = rand.nextFloat();
        if (selectPlayer<0.5) {
            startingPlayer = "Human Player";
        } else {
            startingPlayer = "Machine Player";
        }
    }

    public void showGameBoard() {
        String[] tBoard;
        String[] oBoard;

        tBoard = targetBoard.getBoardDisplay();
        for (String line : tBoard) {
            System.out.println(line);
        }

        System.out.println("\n-----------------------\n");

        oBoard = oceanBoard.getBoardDisplay();
        for (String line : oBoard) {
            System.out.println(line);
        }
    }
}
