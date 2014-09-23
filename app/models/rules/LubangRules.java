package models.rules;

import models.GameContext;
import models.board.BoardState;
import models.command.CaptureCommand;
import models.command.ChangeTurnCommand;
import models.command.GameOverCommand;
import play.Logger;

/**
 * Lubang strategy
 */
public class LubangRules implements GameRules {
    @Override
    public void apply(BoardState boardState, GameContext gameContext) {
        switch (boardState.state) {
            case NONEMPTY_PIT:
                new ChangeTurnCommand().execute(gameContext);
                break;
            case EMPTY_PIT:
                new CaptureCommand().execute(boardState.captureIdx, gameContext);
                break;
//             SAME_PLAYER_TURN:
            case LUBANG:
                break;
            default:
                Logger.warn("unknown move type");
                break;
        }
        checkGameOver(gameContext);
    }

    private void checkGameOver(GameContext gameContext) {
        if (gameContext.getActiveBoard().isEmpty() || gameContext.getInactiveBoard().isEmpty()) {
            new GameOverCommand().execute(gameContext);
        }
    }
}
