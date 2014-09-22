package models;

import models.board.Board;
import models.board.BoardState;
import org.junit.Test;

import static models.board.BoardState.*;
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
        BoardState expected = new BoardState(LastSeedPosition.NONEMPTY_PIT, -1);
        Board board = new Board(true);
        BoardState actual = board.onMove(3);
        assertEquals(actual.state, expected.state);
    }

    @Test
    public void nextMoveToTheSamePlayer() {
        BoardState expected = new BoardState(LastSeedPosition.LUBANG, -1);
        Board board = new Board(true);
        int[] p = {1, 1, 1, 1, 1, 1, 0};
        board.pits = p;
        BoardState actual = board.onMove(5);
        assertEquals(actual.state, expected.state);
    }

    @Test
    public void nextMoveCapture() {
        BoardState expected = new BoardState(LastSeedPosition.EMPTY_PIT, 2);
        Board board = new Board(true);
        int[] p = {1, 1, 0, 1, 1, 4, 2};
        board.pits = p;
        BoardState actual = board.onMove(5);
        assertEquals(actual.state, expected.state);
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