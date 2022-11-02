import java.util.ArrayList;
import java.util.Random;
public class MachinePlayer implements InputSource {

    /*this code still needs fixing, will update ASAP*/
    String name;

    public MachinePlayer(String name) { this.name = name; }

//    public String[] getBlockPosition() {
//        Random rnd = new Random();
//        String colHdr1 = COLHEADER.get(rnd.nextInt(10));
//        int rowNo1 = rnd.nextInt(10)+1;
//        String colHdr2 = COLHEADER.get(rnd.nextInt(10));
//        int rowNo2 = rnd.nextInt(10)+1;
//        String[] pos =  {colHdr1+rowNo1,colHdr2+rowNo2};
//        return pos;
//    };
    @Override
    public String[] getShipPlacement(Ship shipModel) {
        String[] shipLoc = new String[2];
        return shipLoc;
    };
    public String getAttackAt() {
        return "";
    };

}
