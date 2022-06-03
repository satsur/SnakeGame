import java.awt.*;

public class SnakeGame {
    private static GameFrame game;
    public static void main(String[] args) {
        newGame();
    }

    protected static void newGame(int x, int y) {
        game = new GameFrame(x, y);
    }

    public static void close() {
        game.dispose();
    }

    public static Point getLocation() {
        return game.getLocation();
    }
}