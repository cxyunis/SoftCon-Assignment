import java.util.ArrayList;
import java.util.List;

public class ShipGrid {
    // 1 ShipGrid contains many grids, one grid
    private Ship aShip; // e.g Ship.Carrier

    class GridState {

        String gridPos; //e.g. C7
        int gridStatus; //0: alive, 1: got hit

        public GridState(String gridPos, int status) {
            this.gridPos = gridPos;     //e.g. G7
            this.gridStatus = status;   //0: alive, 1: got hit
        }
    }

    // listOfShipState contains element of GridState
    private List<GridState> listOfShipState = new ArrayList<>(); //[GridState,GridState,...]

    public ShipGrid(Ship ship, List<String> listOfPos) {
        aShip = ship;
        for (String s: listOfPos) {
            listOfShipState.add(new GridState(s,0));
        }
    }
    public Ship getShip() { return this.aShip; }
    public String[][] getStates() {
        String[][] state = new String[aShip.getSize()][2];
        int row = 0;
        String[] marker = new String[2];
        if (hasSank()) {
            marker[0] = "";    // dummy
            marker[1] = aShip.name().substring(0,1);    // show Letter when hasSank()
        } else {
            marker[0] = ""; //aShip.name().substring(0,1); not display since not sank yet
            marker[1] = "X";
        }
        for (GridState gs: listOfShipState) {
            state[row][0] = gs.gridPos; //position: e.g. F2
            state[row][1] = marker[gs.gridStatus];  // e.g C for Carrier
            row++;
        }
        return state;
    }
    public String getMarkerID() {
        return aShip.name().substring(0,1);
    }
    public boolean hasSank() {
        int count = 0;
        for (GridState gs: listOfShipState) {
            count += gs.gridStatus;
        }
        return count==aShip.getSize();
    }
    public boolean updateGridPositionStatus(String gridPos) {
        for (GridState gs: listOfShipState) {
            if (gs.gridPos==gridPos) {
                gs.gridStatus = 1;
                return true;
            }
        }
        return false;
    }

}
