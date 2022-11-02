public class GridValue {
    private String gridPos;
    private Ship aShip;

    public GridValue(String gridPos, Ship gridVal) {
        this.gridPos = gridPos; // A1
        this.aShip = gridVal; // String Carrier/Submarine/Empty/...
    }
    public String getMarkerID() {
        return aShip.toString().substring(0,1);
    }
    public Ship getShip() {
        return aShip;
    }
    public String getPosition() {
        return gridPos;
    }
}
