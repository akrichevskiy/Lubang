package models.command;

import models.GameContext;

public class FlipActiveBoardCommand {
    public void execute(GameContext gc) {
        if (gc.topBoard.isActive) {
            gc.topBoard.isActive = false;
            gc.bottomBoard.isActive = true;
        } else {
            gc.topBoard.isActive = true;
            gc.bottomBoard.isActive = false;
        }
    }
}
