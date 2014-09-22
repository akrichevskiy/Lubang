package models.board;

public class BoardState {
    public enum LastSeedPosition {
        NONEMPTY_PIT, EMPTY_PIT, LUBANG
    }

    public int captureIdx;
    public LastSeedPosition state;

    public BoardState(LastSeedPosition state) {
        this(state, -1);
    }

    public BoardState(LastSeedPosition state, int captureIdx) {
        this.captureIdx = captureIdx;
        this.state = state;
    }
}
