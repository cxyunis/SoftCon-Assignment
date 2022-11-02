public class PlayGame {

    public static void main(String[] args) {
        String p1 = "Ocean";
        String p2 = "Target";
        GameModel gameEngine;
        gameEngine = new GameModel(p1,p2);

        //gameEngine.showGameBoard();
        gameEngine.startGame();
    }
}
