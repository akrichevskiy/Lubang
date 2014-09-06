package models;

public class NextMove {
    public enum MoveType {
        CHANGE_TURN, SAME_PLAYER_TURN, CAPTURE
    }

    public int captureIdx;
    public MoveType moveType;

    public NextMove(MoveType moveType) {
        this(moveType, -1);
    }

    public NextMove(MoveType moveType, int captureIdx) {
        this.captureIdx = captureIdx;
        this.moveType = moveType;
    }
}
