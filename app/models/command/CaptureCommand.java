package models.command;

import models.GameContext;
import models.board.Board;
import play.Logger;

public class CaptureCommand {
    public void execute(int dstIdx, GameContext gc) {
        Board srcBoard = gc.getInactiveBoard();
        Board dstBoard = gc.getActiveBoard();
        Logger.debug("[capture before] pit dst:" + dstIdx + ";from board:" + srcBoard + ";to board:" + dstBoard);
        int captureSrcIdx = Board.MAX_PITS - dstIdx - 2;
        int seedsCaptured = srcBoard.capture(captureSrcIdx);
        dstBoard.pits[dstIdx] = 0;
        dstBoard.add(seedsCaptured + 1);
        Logger.debug("[capture after] seeds captured:" + seedsCaptured + ";from board:" + srcBoard + ";to board:" + dstBoard);
        new FlipActiveBoardCommand().execute(gc);
    }
}
