//import java.util.List;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Random;

public class GameModel {

    private List<Ship> boatFleet = new ArrayList<>();
    private GameBoard oceanBoard = new GameBoard("OCEAN  GRID");
    private TargetBoard targetBoard = new TargetBoard("TARGET GRID");  // machine player board
    private HumanPlayer oceanUser;  // an ocean user (human user)
    private MachinePlayer targetUser;  // an computerized user

    private String startingPlayer;
    String currentPlayer;
    private String winner;

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
                                    // may using ChipValue is more appropriate
                                    // should contain starting and ending grid location and type of ship
        List<GridValue> machineChips;
        List<String> listOfPos;     // list of gird positions, e.g. [A2,A3,A4,A5]

        setStartingPlayer();        // set the starting player

        showGameBoard();    // display the game boards status at the start of the game
        //showGameBoardSideBySide();

        // ************* human player **************
        System.out.println("\nHuman/Ocean Board player inputs\n");
        int shipNo = 0;
        int nShip = 0;
        currentPlayer = "Human Player";
        for (Ship s: this.boatFleet) {
            shipPlaced = false;         // become true if the chip (ship) placed on the board

            // handle ship number for input
            if (nShip!=s.getQuantity()) {
                nShip = s.getQuantity();
                shipNo = 0;
            }
            shipNo++;

            while (!shipPlaced) {
                humanChips = askPlayerForShipPlacement(this.oceanUser,s,shipNo);
                listOfPos = convertToListOfPosition(humanChips.get(0),humanChips.get(1));
                if (areGridsAvailable(oceanBoard,listOfPos,s)) {
                    oceanBoard.setShipOnBoard(listOfPos,s);     // (1) move this responsibility to GameBoard, a block of positions
                    shipPlaced = true;
                }
            }
            //showGameBoard();  // remove this once development completed
        }

        //  ************* machine player **************
        //  implemented with random algo for generating the ship locations
        System.out.println("\nMachine/Target Board player inputs\n");
        currentPlayer = "Machine Player";
        for (Ship s: this.boatFleet) {
            shipPlaced = false;

            while (!shipPlaced) {
                machineChips = askPlayerForShipPlacement(this.targetUser,s,shipNo);
                listOfPos = convertToListOfPosition(machineChips.get(0),machineChips.get(1));
                if (areGridsAvailable(targetBoard,listOfPos,s)) {
                    targetBoard.setShipOnBoard(listOfPos, s);  // (2) move this responsibility to GameBoard, a block of positions
                    shipPlaced = true;
                    // listOfPos = [E5,E6] for Patrol (s = Ship.Patrol)
                    //targetBoard.updatePlayedShip(s,listOfPos);  // specialised to handle display at diff stages
                }
            }
        }
          showGameBoard();    // remove this once development completed
        //showGameBoardSideBySide();

        // attacking starts here
        boolean gameOver = false;
        boolean validHit = false;
        String attackPos;
        String status;

        System.out.println("\n"+startingPlayer+" starts first.");
        if (startingPlayer.equals("Machine Player")) {
            currentPlayer = "Machine Player";
            attackPos = attackOpponent(targetUser,oceanBoard);
            status = oceanBoard.getGridStatus(attackPos);       // specialise
            targetUser.updateAttackAtStatus(attackPos,status);  // specialise

            showGameBoard();
            //showGameBoardSideBySide();
            //targetUser.displayBoard();
        }

        while(!gameOver) {
            currentPlayer = "Human Player";
            attackPos = attackOpponent(oceanUser,targetBoard);

            gameOver = targetBoard.isGameOver();
            if (gameOver) {
                winner = "Human Player";
            }

            if (!gameOver) {
                currentPlayer = "Machine Player";
                attackPos = attackOpponent(targetUser,oceanBoard);
                status = oceanBoard.getGridStatus(attackPos);       // specialise, obtain the status of the ocean board at given position
                targetUser.updateAttackAtStatus(attackPos,status);  // specialise, attack position to be used for estimation of next attack

                gameOver = oceanBoard.isGameOver();
                if (gameOver) {
                    winner = "Machine Player";
                }
                showGameBoard();
                //showGameBoardSideBySide();
                //targetUser.displayBoard();
            }
        }
        showGameBoard();    // remove / change this for showing to opponent remaining ships
        //showGameBoardSideBySide();

        System.out.println("\n****************** Game Over ******************\n");
        System.out.println("The winner is "+winner);
        if (winner.equals("Machine Player")) {
            // display unhit ships
            System.out.println("The remaining ship positions are revealed below: \n");
            String[] disp = targetBoard.getFinalDisplay();
            for (String line : disp) {
                System.out.println(line);
            }
        }

    }
    private String attackOpponent(InputSource user, GameBoard opponentBoard) {
        String attackPos = "";
        boolean validHitPos = false;
        while (!validHitPos) {
            attackPos = user.getAttackAt();
            validHitPos = opponentBoard.setAttackAt(attackPos);
            if (!validHitPos && currentPlayer.equals("Human Player")) {
                System.out.println("Invalid position!");
            }
        }
        return attackPos;
    }
    private boolean areGridsAvailable(GameBoard userBoard, List<String> posList, Ship boat) {
        // check if the input list of grids available completely for placing the ship
        boolean occupied;
        int count = 0;
        for (String pos: posList) {
            occupied = userBoard.isOccupied(pos);
            if (!occupied) {
                count++;
            }
        }
        if (count!=boat.getSize() && currentPlayer.equals("Human Player")) {
            System.out.println("Invalid input: Block positions not available!");
            return false;   // some of grid is occupied
        }
        if (count!=boat.getSize() && currentPlayer.equals("Machine Player")) {
            System.out.println("Invalid input: Block positions not available!");
            return false;   // some of grid is occupied
        }
        return true;
    }

    private List<String> convertToListOfPosition(GridValue start, GridValue stop) {
        /*
        convert starting and ending grids (GridValue) into a list of grids cover from starting
        to ending grids. e.g. A1,A4 become [A1,A2,A3,A4]
        * */
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
            // horizontal filling
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

    private void setStartingPlayer() {
        Random rand = new Random();
        float selectPlayer = rand.nextFloat();
        if (selectPlayer<0.5) {
            startingPlayer = "Human Player";
        } else {
            startingPlayer = "Machine Player";
        }
    }

    private void showGameBoard() {
        String[] tBoard;
        String[] oBoard;

        tBoard = targetBoard.getBoardDisplay();
        System.out.println("\n");
        for (String line : tBoard) {
            System.out.println(line);
        }

        System.out.println("\n-----------------------\n");

        oBoard = oceanBoard.getBoardDisplay();
        for (String line : oBoard) {
            System.out.println(line);
        }
    }

//    private void showGameBoardSideBySide() {
//        String[] tBoard;
//        String[] oBoard;
//
//        tBoard = targetBoard.getBoardDisplay();
//        System.out.println("\n");
//        String[] sideBySideBoard = new String[tBoard.length];
//        oBoard = oceanBoard.getBoardDisplay();
//        String str;
//        for (int i=0; i<tBoard.length; i++) {
//            System.out.println(tBoard[i]+"  ???  "+oBoard[i]);
//        }
//    }
}
