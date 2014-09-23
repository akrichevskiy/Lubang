package models.rules;

import models.GameContext;
import models.board.BoardState;

/**
 * Game strategy
 */
public interface GameRules {
    void apply(BoardState boardState , GameContext gameContext);
}
