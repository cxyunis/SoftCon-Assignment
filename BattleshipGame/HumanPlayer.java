import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
//import java.util.Arrays;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class HumanPlayer implements InputSource {

    String name;

    public HumanPlayer(String name) {
        this.name = name;
    }

    @Override
    public String[] getShipPlacement(Ship shipModel, int shipNo) {
        // get ship locations from human user, return String array, i.e. [A1,A4]
        boolean correct = false;
        String blokPos;
        List<String> input = new ArrayList<String>();
        String[] shipPos = new String[2];
        String prompt = "Please enter the position of your "+shipModel.toString()+" "+shipNo+": ";

        while(!correct) {
            blokPos = getBlockPosition(prompt);  // A1,A3
            input = Arrays.asList(blokPos.split("\\s*,\\s*"));
            correct = validateBlockPosition(input,shipModel);
//            if (!correct) {
//                System.out.println("Invalid position!");
//            }
        }
        shipPos[0] = input.get(0);
        shipPos[1] = input.get(1);
        return shipPos;
    }

    public String getAttackAt() {
        String blockPos = "";
        boolean correct = false;
        while (!correct) {
            blockPos = getBlockPosition("Please enter attacking position: ");
            correct = validatePositionFormat(blockPos);
//            if (!correct) {
//                System.out.println("Invalid position!");
//            }
        }
        return blockPos;
    }
    private boolean validatePositionFormat(String pos) {
        // assuming that the following are responsibility of the player to know the rules of the game
        // unless for the information not available to him

        // expecting to have size = 2, e.g J5
        if (pos.length()!=2) {
            System.out.println("Invalid Position: input has more than 2 characters");
            return false;
        }

        // expecting 1st character to have letter from A-J
        if (!GameBoard.COLUMN_HEADER.contains(pos.substring(0,1))) {
            System.out.println("Invalid Position: input 1st character not in [A,B,...,I,J]");
            return false;
        }

        // expecting the 2nd character is integer from [0,9]
        char p = pos.charAt(1);
        int q = (int) p;
        if (q<48 || q>57) {
            System.out.println("Invalid Position: input 2nd character not in [0,1,...,8,9]");
            return false;   // expect integer
        }
        return true;
    }

    private String getBlockPosition(String prompt) {
        /* return block position: e.g A5 or A3,A6 */
        String blockPos = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print(prompt);

        try {
            blockPos = br.readLine();
        } catch (IOException exception) {
            System.out.println(exception.toString());
            System.out.println("Could not read the input");
        }
        return blockPos;
    }
    private boolean validateBlockPosition(List<String> blokPos, Ship shipModel) {
        // assuming that the following are responsibility of the player to know the rules of the game
        // unless for the information not available to him
        // this procedure is to validate for a range of positions (starting and ending positions)

        // expecting a list of 2 (SIZE) elements, e.q [A2,A5]
        final int SIZE = 2; // expecting List of size 2, starting and ending location, e.g. [A1,A5]
        if (blokPos.size()!=SIZE) {
            System.out.println("Invalid Position: input must have 2 position separated by comma e.g. A1,A6");
            return false;
        }

        String firstPos = blokPos.get(0);
        String secondPos = blokPos.get(1);

        // expecting 1st character is in [A,B,...,J] and 2nd character is in [0,1,...,9]
        if (!validatePositionFormat(firstPos) || !validatePositionFormat(secondPos)) {
            return false;
        }

        // expecting horizontal or vertical placement positions only
        if (firstPos.equals(secondPos)) {
            System.out.println("Invalid Position: input must be horizontal or vertical in range of position");
            return false;   //e.g [A4,A4]
        }
        if (firstPos.charAt(0)!=secondPos.charAt(0) &&
            firstPos.charAt(1)!=secondPos.charAt(1)) {
            System.out.println("Invalid Position: input must be horizontal or vertical in range of position");
            return false;   //e.g. [A4,B6], i.e not horizontal/vertical
        }

        // expecting the vertical block of positions equal to Ship.size()
        int length;
        if (firstPos.charAt(0)==secondPos.charAt(0)) {
            length = secondPos.charAt(1) - firstPos.charAt(1) + 1;
            if (length!=shipModel.getSize()) {
                System.out.println("Invalid Position: the range of position must be equal to "+shipModel.getSize());
                return false;
            }
        }

        // expecting the horizontal block of positions equal to Ship.size()
        if (firstPos.charAt(1)==secondPos.charAt(1)) {
            length = secondPos.charAt(0) - firstPos.charAt(0) + 1;
            if (length!=shipModel.getSize()) {
                System.out.println("Invalid Position: the range of position must be equal to "+shipModel.getSize());
                return false;
            }
        }

        return true;
    }


}
