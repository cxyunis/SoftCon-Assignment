public enum Ship {
    Carrier (6,1),
    Battleship (4,2),
    Submarine (3,3),
    Patrol (2,4);

    private final int size;
    private final int quantity;
    Ship(int size, int quantity) {
        this.size = size;
        this.quantity = quantity;
    }
    int getQuantity() {
        return quantity;
    }
    int getSize() {
        return size;
    }
}


