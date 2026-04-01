package com.concordia.robot;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DirectionTest {

    @Test
    public void testTurnRightFromNorth() {
        assertEquals(Direction.EAST, Direction.NORTH.turnRight());
    }

    @Test
    public void testTurnRightFromEast() {
        assertEquals(Direction.SOUTH, Direction.EAST.turnRight());
    }

    @Test
    public void testTurnRightFromSouth() {
        assertEquals(Direction.WEST, Direction.SOUTH.turnRight());
    }

    @Test
    public void testTurnRightFromWest() {
        assertEquals(Direction.NORTH, Direction.WEST.turnRight());
    }

    @Test
    public void testTurnLeftFromNorth() {
        assertEquals(Direction.WEST, Direction.NORTH.turnLeft());
    }

    @Test
    public void testTurnLeftFromWest() {
        assertEquals(Direction.SOUTH, Direction.WEST.turnLeft());
    }

    @Test
    public void testTurnLeftFromSouth() {
        assertEquals(Direction.EAST, Direction.SOUTH.turnLeft());
    }

    @Test
    public void testTurnLeftFromEast() {
        assertEquals(Direction.NORTH, Direction.EAST.turnLeft());
    }

    @Test
    public void testFullClockwiseRotation() {
        Direction d = Direction.NORTH;
        d = d.turnRight(); // EAST
        d = d.turnRight(); // SOUTH
        d = d.turnRight(); // WEST
        d = d.turnRight(); // NORTH
        assertEquals(Direction.NORTH, d);
    }

    @Test
    public void testFullCounterClockwiseRotation() {
        Direction d = Direction.NORTH;
        d = d.turnLeft(); // WEST
        d = d.turnLeft(); // SOUTH
        d = d.turnLeft(); // EAST
        d = d.turnLeft(); // NORTH
        assertEquals(Direction.NORTH, d);
    }

    @Test
    public void testToString() {
        assertEquals("north", Direction.NORTH.toString());
        assertEquals("east", Direction.EAST.toString());
        assertEquals("south", Direction.SOUTH.toString());
        assertEquals("west", Direction.WEST.toString());
    }
}
