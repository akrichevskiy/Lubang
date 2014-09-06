package models;

import org.junit.Test;

import static models.NextMove.*;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class BoardTest {

    @Test
    public void onMove() {
        Board board = new Board(true);
        board.onMove(3);
        int[] expected = {7, 7, 7, 0, 7, 7, 1};
        assertArrayEquals(board.pits, expected);
    }

    @Test
    public void nextMoveToAnotherPlayer() {
        NextMove expected = new NextMove(MoveType.CHANGE_TURN, -1);
        Board board = new Board(true);
        NextMove actual = board.onMove(3);
        assertEquals(actual.moveType, expected.moveType);
    }

    @Test
    public void nextMoveToTheSamePlayer() {
        NextMove expected = new NextMove(MoveType.SAME_PLAYER_TURN, -1);
        Board board = new Board(true);
        int[] p = {1, 1, 1, 1, 1, 1, 0};
        board.pits = p;
        NextMove actual = board.onMove(5);
        assertEquals(actual.moveType, expected.moveType);
    }

    @Test
    public void nextMoveCapture() {
        NextMove expected = new NextMove(MoveType.CAPTURE, 2);
        Board board = new Board(true);
        int[] p = {1, 1, 0, 1, 1, 4, 2};
        board.pits = p;
        NextMove actual = board.onMove(5);
        assertEquals(actual.moveType, expected.moveType);
    }

    @Test
    public void captureTest() {
        int[] expectedBoardState = {0, 6, 6, 6, 6, 6, 0};
        Board board = new Board(true);
        int seedsCaptured = board.capture(0);
        assertEquals(seedsCaptured, 6);
        assertArrayEquals(board.pits, expectedBoardState);
    }
}