package models;


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

    private int reverseIdxForTopBoard (int idx ) {
        if (topBoard.isActive) {
            return Board.MAX_SEEDS - idx;
        }
        return idx;
    }

    private void updateStatusHandler() {
        switch (boardState.state) {
//            CHANGE_TURN:
            case NONEMPTY_PIT:
                flipActiveBoard();
                break;
//             CAPTURE:
            case EMPTY_PIT:
                doCapture(boardState.captureIdx);
                break;
//             SAME_PLAYER_TURN:
            case LUBANG:
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
        Logger.debug("[change turn] top:" + topBoard.isActive + " bottom:" + bottomBoard.isActive);
    }

    private void doCapture(int dstIdx) {
        Board src = getInactiveBoard();
        Board dst = getActiveBoard();
        Logger.debug("[capture before] pit dst:" + dstIdx + ";src board:" + src + ";dst board:" + dst);
        int captureSrcIdx = Board.MAX_PITS - dstIdx - 2;
        int seedsCaptured = src.capture(captureSrcIdx);

        dst.pits[dstIdx] = 0;
        dst.add(seedsCaptured + 1);
        Logger.debug("[capture after] seeds captured:" + seedsCaptured + ";src board:" + src + ";dst board:" + dst);
        boardState.state = BoardState.LastSeedPosition.NONEMPTY_PIT;
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
