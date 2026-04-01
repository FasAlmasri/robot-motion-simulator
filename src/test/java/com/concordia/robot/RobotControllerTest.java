package com.concordia.robot;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class RobotControllerTest {

    private RobotController controller;

    @BeforeEach
    public void setUp() {
        controller = new RobotController(10);
    }

    // --- Initialization ---

    @Test
    public void testDefaultConstructor() {
        RobotController defaultCtrl = new RobotController();
        assertNotNull(defaultCtrl.getRobot());
        assertNotNull(defaultCtrl.getFloor());
        assertEquals(10, defaultCtrl.getFloor().getSize());
    }

    @Test
    public void testParameterizedConstructor() {
        assertNotNull(controller.getRobot());
        assertNotNull(controller.getFloor());
        assertEquals(10, controller.getFloor().getSize());
    }

    // --- Null / Empty / Whitespace input (decision: command==null || command.trim().isEmpty()) ---

    @Test
    public void testNullCommand() {
        assertFalse(controller.processCommand(null));
    }

    @Test
    public void testEmptyCommand() {
        assertFalse(controller.processCommand(""));
    }

    @Test
    public void testWhitespaceOnlyCommand() {
        assertFalse(controller.processCommand("   "));
    }

    // --- Single-character commands (all switch branches) ---

    @Test
    public void testPenUpCommand() {
        controller.processCommand("D");
        assertTrue(controller.processCommand("U"));
        assertFalse(controller.getRobot().isPenDown());
    }

    @Test
    public void testPenUpLowercase() {
        assertTrue(controller.processCommand("u"));
    }

    @Test
    public void testPenDownCommand() {
        assertTrue(controller.processCommand("D"));
        assertTrue(controller.getRobot().isPenDown());
    }

    @Test
    public void testPenDownLowercase() {
        assertTrue(controller.processCommand("d"));
        assertTrue(controller.getRobot().isPenDown());
    }

    @Test
    public void testTurnRightCommand() {
        assertTrue(controller.processCommand("R"));
        assertEquals(Direction.EAST, controller.getRobot().getDirection());
    }

    @Test
    public void testTurnRightLowercase() {
        assertTrue(controller.processCommand("r"));
        assertEquals(Direction.EAST, controller.getRobot().getDirection());
    }

    @Test
    public void testTurnLeftCommand() {
        assertTrue(controller.processCommand("L"));
        assertEquals(Direction.WEST, controller.getRobot().getDirection());
    }

    @Test
    public void testTurnLeftLowercase() {
        assertTrue(controller.processCommand("l"));
        assertEquals(Direction.WEST, controller.getRobot().getDirection());
    }

    @Test
    public void testPrintFloorCommand() {
        assertTrue(controller.processCommand("P"));
    }

    @Test
    public void testPrintFloorLowercase() {
        assertTrue(controller.processCommand("p"));
    }

    @Test
    public void testPrintStatusCommand() {
        assertTrue(controller.processCommand("C"));
    }

    @Test
    public void testPrintStatusLowercase() {
        assertTrue(controller.processCommand("c"));
    }

    @Test
    public void testQuitCommand() {
        assertTrue(controller.processCommand("Q"));
    }

    @Test
    public void testQuitLowercase() {
        assertTrue(controller.processCommand("q"));
    }

    @Test
    public void testHistoryCommand() {
        assertTrue(controller.processCommand("H"));
    }

    @Test
    public void testHistoryLowercase() {
        assertTrue(controller.processCommand("h"));
    }

    @Test
    public void testInvalidSingleCharCommand() {
        assertFalse(controller.processCommand("X"));
    }

    @Test
    public void testAnotherInvalidSingleCharCommand() {
        assertFalse(controller.processCommand("Z"));
    }

    // --- Move command (M s) ---

    @Test
    public void testMoveForwardNorth() {
        assertTrue(controller.processCommand("M 5"));
        assertEquals(5, controller.getRobot().getY());
        assertEquals(0, controller.getRobot().getX());
    }

    @Test
    public void testMoveForwardEast() {
        controller.processCommand("R");
        assertTrue(controller.processCommand("M 4"));
        assertEquals(4, controller.getRobot().getX());
        assertEquals(0, controller.getRobot().getY());
    }

    @Test
    public void testMoveForwardSouth() {
        controller.processCommand("M 5");
        controller.processCommand("R");
        controller.processCommand("R");
        assertTrue(controller.processCommand("M 3"));
        assertEquals(2, controller.getRobot().getY());
    }

    @Test
    public void testMoveForwardWest() {
        controller.processCommand("R");
        controller.processCommand("M 5");
        controller.processCommand("R");
        controller.processCommand("R");
        assertTrue(controller.processCommand("M 3"));
        assertEquals(2, controller.getRobot().getX());
    }

    @Test
    public void testMoveLowercase() {
        assertTrue(controller.processCommand("m 3"));
        assertEquals(3, controller.getRobot().getY());
    }

    @Test
    public void testMoveZeroSteps() {
        assertTrue(controller.processCommand("M 0"));
        assertEquals(0, controller.getRobot().getY());
    }

    @Test
    public void testMoveOutOfBounds() {
        assertFalse(controller.processCommand("M 15"));
    }

    @Test
    public void testNegativeMoveCommand() {
        assertFalse(controller.processCommand("M -5"));
    }

    // --- Initialize command (I n) ---

    @Test
    public void testInitializeCommand() {
        assertTrue(controller.processCommand("I 15"));
        assertEquals(15, controller.getFloor().getSize());
    }

    @Test
    public void testInitializeLowercase() {
        assertTrue(controller.processCommand("i 20"));
        assertEquals(20, controller.getFloor().getSize());
    }

    @Test
    public void testInitializeZero() {
        assertFalse(controller.processCommand("I 0"));
    }

    @Test
    public void testInitializeNegative() {
        assertFalse(controller.processCommand("I -10"));
    }

    @Test
    public void testInitializeResetsRobot() {
        controller.processCommand("M 5");
        controller.processCommand("I 8");
        assertEquals(0, controller.getRobot().getX());
        assertEquals(0, controller.getRobot().getY());
        assertEquals(8, controller.getFloor().getSize());
    }

    // --- Invalid multi-character commands ---

    @Test
    public void testInvalidTwoPartCommand() {
        assertFalse(controller.processCommand("X 5"));
    }

    @Test
    public void testMoveWithNonNumericArg() {
        assertFalse(controller.processCommand("M abc"));
    }

    @Test
    public void testThreePartCommand() {
        assertFalse(controller.processCommand("M 5 3"));
    }

    @Test
    public void testSingleMWithNoArg() {
        assertFalse(controller.processCommand("M"));
    }

    // --- Command history ---

    @Test
    public void testCommandHistory() {
        controller.processCommand("D");
        controller.processCommand("M 5");
        controller.processCommand("R");
        assertEquals(3, controller.getCommandHistory().size());
    }

    @Test
    public void testQuitNotInHistory() {
        controller.processCommand("D");
        controller.processCommand("Q");
        assertEquals(1, controller.getCommandHistory().size());
    }

    @Test
    public void testHistoryNotInHistory() {
        controller.processCommand("D");
        controller.processCommand("H");
        assertEquals(1, controller.getCommandHistory().size());
    }

    @Test
    public void testEmptyHistory() {
        assertEquals(0, controller.getCommandHistory().size());
    }

    // --- Replay ---

    @Test
    public void testReplayEmptyHistory() {
        controller.processCommand("H");
        assertEquals(0, controller.getCommandHistory().size());
    }

    @Test
    public void testReplayWithCommands() {
        controller.processCommand("D");
        controller.processCommand("M 3");
        controller.processCommand("R");
        controller.processCommand("H");
        assertEquals(Direction.EAST, controller.getRobot().getDirection());
        assertEquals(3, controller.getRobot().getY());
    }

    // --- Combined scenario ---

    @Test
    public void testDrawAndVerifyFloor() {
        controller.processCommand("D");
        controller.processCommand("M 2");
        assertEquals(1, controller.getFloor().getCell(0, 1));
        assertEquals(1, controller.getFloor().getCell(0, 2));
    }

    @Test
    public void testSequentialMovesAllDirections() {
        controller.processCommand("M 3");
        controller.processCommand("R");
        controller.processCommand("M 3");
        controller.processCommand("R");
        controller.processCommand("M 3");
        controller.processCommand("R");
        controller.processCommand("M 3");
        assertEquals(0, controller.getRobot().getX());
        assertEquals(0, controller.getRobot().getY());
    }
}
