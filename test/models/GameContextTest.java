package models;

import models.board.Board;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class GameContextTest {

    private GameContext gameContext;
    private Board topBoard;
    private Board bottomBoard;

    @Before
    public void setUp() {
        gameContext = new GameContext();
        topBoard = gameContext.topBoard;
        bottomBoard = gameContext.bottomBoard;
    }

    @Test
    public void changeTurnTest() {
        assertTrue(topBoard.isActive);
        assertFalse(bottomBoard.isActive);
        gameContext.onMove(1);
        assertFalse(topBoard.isActive);
        assertTrue(bottomBoard.isActive);
    }

    @Test
    public void captureTest() {
        int[] topPits = {1, 0, 0, 0, 0, 0, 0};
        int[] bottomPits = {0, 0, 0, 0, 2, 0, 0};
        topBoard.pits = topPits;
        topBoard.isActive = true;
        bottomBoard.pits = bottomPits;
        bottomBoard.isActive = false;

        gameContext.onMove(6);

        int[] expectedTop = {0, 0, 0, 0, 0, 0, 3};
        int[] expectedBottom = {0, 0, 0, 0, 0, 0, 0};

        assertArrayEquals(String.format("expected top board:%s; actual:%s", Arrays.toString(expectedTop), Arrays.toString(topBoard.pits)), expectedTop, topBoard.pits);
        assertArrayEquals("bottom board is different from expected", expectedBottom, bottomBoard.pits);
    }

    @Test
    public void samePlayerTurnTest() {
        int[] topPits = {0, 0, 0, 3, 0, 0, 0};
        topBoard.pits = topPits;
        topBoard.isActive = true;

        gameContext.onMove(3);

        assertTrue("top board is still active", topBoard.isActive);
        int[] expected = {0, 0, 0, 0, 1, 1, 1};
        assertArrayEquals("top board has correct state after last seed ends up in LB", topBoard.pits, expected);
    }

    @Test
    public void gameOverTest() {
        int[] topPits = {0, 0, 0, 0, 0, 1, 0};
        int[] bottomPits = {0, 0, 0, 0, 2, 0, 0};
        topBoard.pits = topPits;
        topBoard.isActive = true;
        bottomBoard.pits = bottomPits;
        bottomBoard.isActive = false;

        gameContext.onMove(1);

        assertTrue("game is over if top board is out of stones", gameContext.isOver);
    }
}