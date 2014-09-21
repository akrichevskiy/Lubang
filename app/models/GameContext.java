package models;


import play.Logger;

import static models.NextMove.MoveType.CHANGE_TURN;
/**
 * Mediator between boards
 */
public class GameContext {
    public Board topBoard;
    public Board bottomBoard;
    public boolean isOver;
    public String gameOverMessage;
    private NextMove nextMove;

    public GameContext() {
        topBoard = new Board(true);
        bottomBoard = new Board(false);
        isOver = false;
    }

    public void onMove(int idx) {
        int updatedIdx = reverseIdxForTopBoard(idx);
        Logger.info("[move]start seed from pit:" + updatedIdx);
        Board activeBoard = getActiveBoard();
        nextMove = activeBoard.onMove(updatedIdx);
        updateStatusHandler();
    }

    private int reverseIdxForTopBoard (int idx ) {
        if (topBoard.isActive) {
            return Board.MAX_SEEDS - idx;
        }
        return idx;
    }

    private void updateStatusHandler() {
        switch (nextMove.moveType) {
            case CHANGE_TURN:
                flipActiveBoard();
                break;
            case CAPTURE:
                doCapture();
                break;
            case SAME_PLAYER_TURN:
                break;
            default:
                Logger.warn("unknown move type");
                break;
        }
        if (getActiveBoard().isEmpty() || getInactiveBoard().isEmpty()) {
            doFinishGame();
        }
    }

    private void flipActiveBoard() {
        if (topBoard.isActive) {
            topBoard.isActive = false;
            bottomBoard.isActive = true;
        } else {
            topBoard.isActive = true;
            bottomBoard.isActive = false;
        }
        Logger.info("[change turn] top:" + topBoard.isActive + " bottom:" + bottomBoard.isActive);
    }

    private void doCapture() {
        Board src = getInactiveBoard();
        Board dst = getActiveBoard();
        Logger.debug("[capture before] pit dst:" + nextMove.captureIdx + ";src board:" + src + ";dst board:" + dst);
        int captureDstIdx = nextMove.captureIdx;
        int captureSrcIdx = Board.MAX_PITS - captureDstIdx - 2;
        int seedsCaptured = src.capture(captureSrcIdx);

        dst.pits[captureDstIdx] = 0;
        dst.add(seedsCaptured + 1);
        Logger.debug("[capture after] seeds captured:" + seedsCaptured + ";src board:" + src + ";dst board:" + dst);
        nextMove.moveType = CHANGE_TURN;
        updateStatusHandler();
    }

    private void doFinishGame() {
        int topPlayerScore = topBoard.dump();
        int bottomPlayerScore = bottomBoard.dump();
        if (topPlayerScore > bottomPlayerScore) {
            gameOverMessage = String.format("Top player won, %d:%d", topPlayerScore, bottomPlayerScore);
        } else if (bottomPlayerScore > topPlayerScore) {
            gameOverMessage = String.format("Bottom player won, %d:%d", bottomPlayerScore, topPlayerScore);
        } else {
            gameOverMessage = String.format("Draw %d:%d", bottomPlayerScore, topPlayerScore);
        }
        isOver = true;
    }

    private Board getActiveBoard() {
        return topBoard.isActive ? topBoard : bottomBoard;
    }

    private Board getInactiveBoard() {
        return topBoard.isActive ? bottomBoard : topBoard;
    }
}
