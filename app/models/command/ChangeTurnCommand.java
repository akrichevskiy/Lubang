package models.command;

import models.GameContext;

public class ChangeTurnCommand {
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
