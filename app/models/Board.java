package models;

import play.Logger;

import java.util.Arrays;

import static models.NextMove.MoveType;

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
     * @return type of next move see @NextType
     */
    public NextMove onMove(int idx) {
        NextMove nextMove = nextMove(idx);
        if (nextMove.moveType != MoveType.CAPTURE) {
            doSow(idx);
        }
        return nextMove;
    }

    /**
     * Sows seeds in pits
     *
     * @param idx - pit to start sowing from
     */
    public void doSow(int idx) {
        int count = this.pits[idx];
        this.pits[idx] = 0;
        for (int i = 0; i < count; i++) {
            int nextPit = (idx + i + 1) % MAX_PITS;
            pits[nextPit] = pits[nextPit] + 1;
        }
    }

    public NextMove nextMove(int currentPitIdx) {
        int lastPitIdx = (currentPitIdx + pits[currentPitIdx]) % MAX_PITS;
        if (lastPitIdx == MAX_PITS - 1) {
            return new NextMove(MoveType.SAME_PLAYER_TURN);
        } else if (pits[lastPitIdx] == 0) {
            return new NextMove(MoveType.CAPTURE, lastPitIdx);
        } else return new NextMove(MoveType.CHANGE_TURN);
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
        StringBuilder result = new StringBuilder();
        result.append(this.getClass().getName());
        result.append(" pits: " + Arrays.toString(pits));
        return result.toString();
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
        return seedsOnBoard == 0 ? true : false;
    }
}
