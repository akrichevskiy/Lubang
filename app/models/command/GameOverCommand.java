package models.command;

import models.GameContext;

public class GameOverCommand {
    public void execute(GameContext gc) {
        int topPlayerScore = gc.topBoard.dump();
        int bottomPlayerScore = gc.bottomBoard.dump();
        if (topPlayerScore > bottomPlayerScore) {
            gc.gameOverMessage = String.format("Top player won, %d:%d", topPlayerScore, bottomPlayerScore);
        } else if (bottomPlayerScore > topPlayerScore) {
            gc.gameOverMessage = String.format("Bottom player won, %d:%d", bottomPlayerScore, topPlayerScore);
        } else {
            gc.gameOverMessage = String.format("Draw %d:%d", bottomPlayerScore, topPlayerScore);
        }
        gc.isOver = true;
    }
}
