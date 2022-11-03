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
            correct = validateBlockPosition(input);
            if (!correct) {
                System.out.println("Invalid position! Please re-enter a valid position");
            }
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
            if (!correct) {
                System.out.println("Invalid position!");
            }
        }
        return blockPos;
    }
    private boolean validatePositionFormat(String pos) {
        if (pos.length()!=2) {
            return false;   // expecting to have size = 2, e.g J5
        }
        if (!GameBoard.COLUMN_HEADER.contains(pos.substring(0,1))) {
            return false;   // expecting to have first letter from A-J
        }
        //String p = pos.substring(1,2);
        char p = pos.charAt(1);
        int q = (int) p;
        if (q<48 || q>57) {
            return false;   // expect integer
        }
//        int j = Integer.parseInt(p);
//        if (j<0 || j>GameBoard.GRID_DIMENSION) {
//            return false;   // expecting to have integer in [0,1,...,8,9]
//        }
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
    private boolean validateBlockPosition(List<String> blokPos) {
        final int SIZE = 2; // expecting List of size 2, starting and ending location, e.g. [A1,A5]

        if (blokPos.size()!=SIZE) {
            return false;
        }
        String loc;
        for (int i=0; i<SIZE; i++) {
            loc = blokPos.get(i);
            if (!validatePositionFormat(loc)) {
                return false;
            }
        }
        return true;
    }


}
