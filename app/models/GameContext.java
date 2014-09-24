package models;


import models.board.Board;
import models.board.BoardState;
import models.rules.GameRules;
import models.rules.LubangRules;
import play.Logger;

/**
 * Mediator between boards
 */
public class GameContext {
    public Board topBoard;
    public Board bottomBoard;
    public boolean isOver;
    public String gameOverMessage;
    private GameRules rules;

    public GameContext() {
        topBoard = new Board(true);
        bottomBoard = new Board(false);
        isOver = false;
        rules = new LubangRules();
    }

    public void setRules(GameRules rules) {
        this.rules = rules;
    }

    public void onMove(int idx) {
        int updatedIdx = reverseIdxForTopBoard(idx);
        Logger.debug("[move]seeding from pit:" + updatedIdx + "; activeBoard:" + dbgActiveBoard());
        Board activeBoard = getActiveBoard();
        BoardState boardState = activeBoard.onMove(updatedIdx);
        rules.apply(boardState, this);
    }

    private int reverseIdxForTopBoard(int idx) {
        if (topBoard.isActive) {
            return Board.MAX_SEEDS - idx;
        }
        return idx;
    }

    public Board getActiveBoard() {
        return topBoard.isActive ? topBoard : bottomBoard;
    }

    public Board getInactiveBoard() {
        return topBoard.isActive ? bottomBoard : topBoard;
    }

    private String dbgActiveBoard() {
        if (topBoard.isActive) {
            return "top";
        } else {
            return "bottom";
        }

    }
}
