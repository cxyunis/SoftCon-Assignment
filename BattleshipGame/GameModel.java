import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameModel {
    private List<Ship> boatFleet = new ArrayList<>();
    private GameBoard oceanBoard = new GameBoard("OCEAN  GRID");
    private TargetBoard targetBoard = new TargetBoard("TARGET GRID");  // machine player board
    private HumanPlayer oceanUser;  // an ocean user (human user)
    private MachinePlayer targetUser;  // a computerized user

    private String startingPlayer;

    public GameModel(String player1, String player2) {
        oceanUser = new HumanPlayer(player1);
        targetUser = new MachinePlayer(player2);

        // setup boat fleet for the game, each player should only has fixed amount of chips (or ships)
        //                          Carrier, Battleship, Submarine, Patrol
        // sizes (length of ship) =    6,         4,         3,        1
        // quantity of each ship  =    1,         2,         3,        4
        for (Ship s: Ship.values()) {
            for (int i=0; i<s.getQuantity(); i++) {
                this.boatFleet.add(s);
            }
        }
    }

    public void startGame() {
        /*
        * controls the game flows and rules of the game from start until game over
        *  */

        String[] blockPos;  // input for starting and ending grid (e.g. C3,C5 for BattleShip)
        boolean shipPlaced;
        List<GridValue> humanChips; // a list of chips (ships), each chip is an object of GridValue
                                    // using ChipValue is more appropriate
                                    // should contain starting and ending grid location and type of ship
        List<GridValue> machineChips;
        List<String> listOfPos;     // list of gird positions, e.g. [A2,A3,A4,A5]

        setStartingPlayer();        // set the starting player

        showGameBoard();    // display the game boards status at the start of the game

        // ************* human player **************
        System.out.println("Human/Ocean Board player inputs"); //maybe human player is enough
        int shipNo = 0;
        int nShip = 0;
        for (Ship s: this.boatFleet) {
            shipPlaced = false;         // become true if the chip (ship) placed on the board

            // handle ship number for input: everytime there is a ship type change, nship!=s.getQuantity()
            if (nShip!=s.getQuantity()) {
                nShip = s.getQuantity();
                shipNo = 0;
            }
            shipNo++; //to be used in getShipPlacement prompt in HumanPlayer

            while (!shipPlaced) {
                humanChips = askPlayerForShipPlacement(this.oceanUser,s,shipNo);
                listOfPos = convertToListOfPosition(humanChips.get(0),humanChips.get(1));
                if (areGridsAvailable(oceanBoard,listOfPos,s)) {
                    setShipOnBoard(oceanBoard,listOfPos,s);
                    shipPlaced = true;
                }
            }
            showGameBoard();  // remove this once development completed: used for testing
        }

        //  ************* machine player **************
        //  implemented with random algo for generating the ship locations
        System.out.println("Machine/Target Board player inputs");
        for (Ship s: this.boatFleet) {
            shipPlaced = false;

            while (!shipPlaced) {
                machineChips = askPlayerForShipPlacement(this.targetUser,s,shipNo);
                listOfPos = convertToListOfPosition(machineChips.get(0),machineChips.get(1));
                if (areGridsAvailable(targetBoard,listOfPos,s)) {
                    setShipOnBoard(targetBoard, listOfPos, s);
                    shipPlaced = true;
                    targetBoard.updatePlayedShip(s,listOfPos);  // specialist to handle display at diff stages
                }
            }
        }
        showGameBoard();    // remove this once development completed: used for testing

        // attacking starts here
        boolean gameOver = false;
        boolean validHit = false;
        String attackPos;
        System.out.println(startingPlayer+" starts first.");
        if (startingPlayer=="Machine Player") {
            while(!validHit) {
                attackPos = targetUser.getAttackAt();
                validHit = oceanBoard.setAttackAt(attackPos);
            }
            showGameBoard();
        }

        while(!gameOver) {
            validHit = false;
            while (!validHit) {
                attackPos = oceanUser.getAttackAt();
                validHit = targetBoard.setAttackAt(attackPos);
                if (validHit) {
//working on it
                } else {
                    System.out.println("Invalid position!");
                }
            }
            gameOver = targetBoard.isGameOver();

            if (!gameOver) {
                validHit = false;
                while (!validHit) {
                    attackPos = targetUser.getAttackAt();
                    validHit = oceanBoard.setAttackAt(attackPos);
                }
                showGameBoard();
                gameOver = targetBoard.isGameOver();
            }
        }
        showGameBoard();    // remove / change this for showing to opponent remaining ships
    }
    private boolean areGridsAvailable(GameBoard userBoard, List<String> posList, Ship boat) {
        // check if the input list of grids are available completely for placing the ship
        boolean occupied;
        int count = 0;
        for (String pos: posList) {
            occupied = userBoard.isOccupied(pos);
            if (!occupied) {
                count++;
            }
        }
//check if the boat size can fit in the non occupied given position
        if (count!=boat.getSize()) {
            System.out.println("Invalid block position!");
            return false;   // some of grid is occupied
        }
        return true;
    }

    private void setShipOnBoard(GameBoard userBoard, List<String> posList, Ship ship) {
        // place ships on grid, not validation of input parameters
        // posList is a list of position for ship: e.g. [A4,A5] for Patrol
        for (String pos: posList) {
            GridValue gv = new GridValue(pos,ship);
            userBoard.placeShipOnBoard(gv);
        }
    }
    private List<String> convertToListOfPosition(GridValue start, GridValue stop) {
        /*
        convert starting and ending grids (GridValue) into a list of grids cover from starting
        to ending grids. e.g. A1,A4 become [A1,A2,A3,A4]
        * */
        String startCol = start.getPosition().substring(0,1); //charAt was not used because i will need to convert to string again
        String startRowNo = start.getPosition().substring(1,2);
        String stopCol = stop.getPosition().substring(0,1);
        String stopRowNo = stop.getPosition().substring(1,2);
        int row1 = Integer.parseInt(startRowNo); //convert to integer
        int row2 = Integer.parseInt(stopRowNo);

        List<String> listOfPos = new ArrayList<>();
        if (startCol.equals(stopCol)) {
            // vertical filling
            for (int i=row1; i<row2+1; i++) {
                listOfPos.add(startCol+String.valueOf(i));
            }
        } else {
            //horizontal filling
            int idx1 = GameBoard.COLUMN_HEADER.indexOf(startCol);
            int idx2 = GameBoard.COLUMN_HEADER.indexOf(stopCol);
            for (int i=idx1; i<idx2+1; i++) {
                listOfPos.add(GameBoard.COLUMN_HEADER.substring(i,i+1)+startRowNo);
            }
        }
        return listOfPos;
    }

    private List<GridValue> askPlayerForShipPlacement(InputSource player, Ship ship, int shipNo) {
        // get input from player
        List<GridValue> shipPlacement = new ArrayList<>();
        GridValue gv;
        String[] loc;   // starting and ending location, e.g. A4,A7 for battleship

        loc = player.getShipPlacement(ship,shipNo);
        gv = new GridValue(loc[0],ship);    // starting location
        shipPlacement.add(gv);
        gv = new GridValue(loc[1],ship);    // ending location
        shipPlacement.add(gv);


        return shipPlacement;  // contains starting and ending GridValue
    }

    public void setStartingPlayer() {
        Random rand = new Random();
        float selectPlayer = rand.nextFloat(); //chances
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
