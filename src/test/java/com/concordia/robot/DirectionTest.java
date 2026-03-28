package com.concordia.robot;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DirectionTest {

    @Test
    public void testTurnRightCyclesClockwise() {
        assertEquals(Direction.EAST, Direction.NORTH.turnRight());
        assertEquals(Direction.SOUTH, Direction.EAST.turnRight());
        assertEquals(Direction.WEST, Direction.SOUTH.turnRight());
        assertEquals(Direction.NORTH, Direction.WEST.turnRight());
    }

    @Test
    public void testTurnLeftCyclesCounterClockwise() {
        assertEquals(Direction.WEST, Direction.NORTH.turnLeft());
        assertEquals(Direction.SOUTH, Direction.WEST.turnLeft());
        assertEquals(Direction.EAST, Direction.SOUTH.turnLeft());
        assertEquals(Direction.NORTH, Direction.EAST.turnLeft());
    }

    @Test
    public void testToStringLowercase() {
        assertEquals("north", Direction.NORTH.toString());
        assertEquals("east", Direction.EAST.toString());
        assertEquals("south", Direction.SOUTH.toString());
        assertEquals("west", Direction.WEST.toString());
    }
}
