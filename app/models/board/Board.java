package models.board;

import play.Logger;

import java.util.Arrays;


/**
 * Representation for player's pits and his Lubang Menggali
 */
public class Board {
    public static int MAX_PITS = 7;
    public static int MAX_SEEDS = 6;
    public int[] pits = new int[MAX_PITS];
    public boolean isActive;
    private int lubangMenggaliIdx = MAX_PITS - 1;


    public Board(boolean isActive) {
        this.isActive = isActive;
        for (int i = 0; i < MAX_PITS - 1; i++) {
            pits[i] = MAX_SEEDS;
        }
    }

    /**
     * Handles movement
     *
     * @param idx pit number
     * @return boardState see @BoardStatus
     */
    public BoardState onMove(int idx) {
        BoardState boardState = lastSeedLocation(idx);
        doSow(idx);
        return boardState;
    }

    /**
     * Sows seeds in pits
     *
     * @param fromPitIdx - pit to start sowing from
     * @return  lastPitIdx - pit where the last seed will end up
     */
    private int doSow(int fromPitIdx) {
        int count = this.pits[fromPitIdx];
        this.pits[fromPitIdx] = 0;
        int nextPit = -1;
        for (int i = 0; i < count; i++) {
            nextPit = (fromPitIdx + i + 1) % MAX_PITS;
            pits[nextPit] = pits[nextPit] + 1;
        }
        return nextPit;
    }

    private BoardState lastSeedLocation(int fromPitIdx) {
        int lastPitIdx = (fromPitIdx + pits[fromPitIdx]) % MAX_PITS;
        Logger.trace("[lastSeedLocation] pitIdx:" + fromPitIdx + "; lastPitIdx:" + lastPitIdx);
        if (lastPitIdx == MAX_PITS - 1) {
            return new BoardState(BoardState.LastSeedPosition.LUBANG);
        } else if (pits[lastPitIdx] == 0) {
            return new BoardState(BoardState.LastSeedPosition.EMPTY_PIT, lastPitIdx);
        } else return new BoardState(BoardState.LastSeedPosition.NONEMPTY_PIT);
    }

    public int capture(int idx) {
        int capturedSeeds = pits[idx];
        pits[idx] = 0;
        return capturedSeeds;
    }

    /**
     * Adds seeds to LB
     *
     * @param seeds - seeds to add to LB
     */
    public void add(int seeds) {
        pits[lubangMenggaliIdx] += seeds;
    }

    @Override
    public String toString() {
        return String.format("%s, pits:%s; total seeds:%d", this.getClass().getName(), Arrays.toString(pits), totalSeeds());
    }

    private int totalSeeds() {
        int totalSeeds = 0;
        for(int i = 0 ; i < pits.length;i++ ) {
            totalSeeds += pits[i];
        }
        return  totalSeeds;
    }

    /**
     * dumps all seeds in LB
     *
     * @return number of seeds in LB
     */
    public int dump() {
        for (int i = 0; i < MAX_PITS - 1; i++) {
            pits[lubangMenggaliIdx] += pits[i];
            pits[i] = 0;
        }
        return pits[lubangMenggaliIdx];
    }

    public boolean isEmpty() {
        int seedsOnBoard = 0;
        for (int i = 0; i < MAX_PITS - 1; i++) {
            seedsOnBoard += pits[i];
        }
        return seedsOnBoard == 0;
    }
}
