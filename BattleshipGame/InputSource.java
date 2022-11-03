public interface InputSource {
    //public final static List<String> COLHEADER = Arrays.asList("A","B","C","D","E","F","G","H","I","J");

    String[] getShipPlacement(Ship s, int i);
    String getAttackAt();
}
