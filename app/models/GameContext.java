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
    private int currentIdx;

    public GameContext() {
        topBoard = new Board(true);
        bottomBoard = new Board(false);
        currentIdx = -1;
        isOver = false;
    }

    public void onMove(int idx) {
        currentIdx = idx;
        Board activeBoard = getActiveBoard();
        nextMove = activeBoard.onMove(idx);
        updateStatusHandler();
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
        Logger.info("top:" + topBoard.isActive + " bottom:" + bottomBoard.isActive);
    }

    private void doCapture() {
        Logger.info("capture:" + nextMove.captureIdx);
        Board src = getInactiveBoard();
        Board dst = getActiveBoard();
        int captureDstIdx = nextMove.captureIdx;
        int captureSrcIdx = Board.MAX_PITS - captureDstIdx - 2;
        int seedsCaptured = src.capture(captureSrcIdx);
        dst.pits[currentIdx] = 0;
        dst.add(seedsCaptured + 1);

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
