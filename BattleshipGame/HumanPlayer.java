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
    public String[] getShipPlacement(Ship shipModel) {
        boolean correct = false; //to test if all the string inputs have been read
        String blokPos;
        List<String> input = new ArrayList<String>();
        String[] shipPos = new String[2];
        String prompt = "Please enter the position of your "+shipModel.toString()+": ";

        while(!correct) {
            blokPos = getBlockPosition(prompt);  // A1,A3
            input = Arrays.asList(blokPos.split("\\s*,\\s*")); //split on any number of whitespace characters and comma
            correct = validateBlockPosition(input);
        }
        shipPos[0] = input.get(0);
        shipPos[1] = input.get(1);
        return shipPos;
    }

//    @Override
//    public String[] getShipPlacement() {
//
//        return new String[0];
//    }

    public String getAttackAt() {
        // to implement
        return "";
    }

    private String getBlockPosition(String prompt) {
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
        if (blokPos.size()!=2) {
            //what does this do
            return false;
        }
        int row;
        String s;
        String loc;
        for (int i=0; i<2; i++) {
            loc = blokPos.get(i);
            s = String.valueOf(loc.charAt(0)); //charAt return character at specified index of string
            if (!COLHEADER.contains(s)) {
                return false;
            }
            row = Integer.parseInt(loc.substring(1,loc.length()));
            if (row<0 || row>10) {
                return false;
            }
        }
        return true;
    }


}
