public class GridValue {
    private String gridPos;
    private Ship aShip;

    public GridValue(String gridPos, Ship aShip) {
        this.gridPos = gridPos; // A1
        this.aShip = aShip;
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
