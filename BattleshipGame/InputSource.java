import java.util.Arrays;
import java.util.List;

public interface InputSource {
    //column header
    public final static List<String> COLHEADER = Arrays.asList("A","B","C","D","E","F","G","H","I","J");

    String[] getShipPlacement(Ship ship);
    String getAttackAt();
}
