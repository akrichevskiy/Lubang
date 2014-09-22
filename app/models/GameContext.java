package models;


import models.board.Board;
import models.board.BoardState;
import models.command.CaptureCommand;
import models.command.FlipActiveBoardCommand;
import models.command.GameOverCommand;
import play.Logger;

/**
 * Mediator between boards
 */
public class GameContext {
    public Board topBoard;
    public Board bottomBoard;
    public boolean isOver;
    public String gameOverMessage;
    private BoardState boardState;

    public GameContext() {
        topBoard = new Board(true);
        bottomBoard = new Board(false);
        isOver = false;
    }

    public void onMove(int idx) {
        int updatedIdx = reverseIdxForTopBoard(idx);
        Logger.debug("[move]start seed from pit:" + updatedIdx);
        Board activeBoard = getActiveBoard();
        boardState = activeBoard.onMove(updatedIdx);
        updateStatusHandler();
    }

    private int reverseIdxForTopBoard(int idx) {
        if (topBoard.isActive) {
            return Board.MAX_SEEDS - idx;
        }
        return idx;
    }

    private void updateStatusHandler() {
        switch (boardState.state) {
//            CHANGE_TURN:
            case NONEMPTY_PIT:
                new FlipActiveBoardCommand().execute(this);
                break;
//             CAPTURE:
            case EMPTY_PIT:
                new CaptureCommand().execute(boardState.captureIdx, this);
                break;
//             SAME_PLAYER_TURN:
            case LUBANG:
                break;
            default:
                Logger.warn("unknown move type");
                break;
        }
        if (getActiveBoard().isEmpty() || getInactiveBoard().isEmpty()) {
            new GameOverCommand().execute(this);
        }
    }

    public Board getActiveBoard() {
        return topBoard.isActive ? topBoard : bottomBoard;
    }

    public Board getInactiveBoard() {
        return topBoard.isActive ? bottomBoard : topBoard;
    }
}
