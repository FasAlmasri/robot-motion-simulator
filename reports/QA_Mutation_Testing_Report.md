# AI Static Analysis Report — Mutation Testing

**Name:** Khalid Abdulrahim  
**Student ID:** 40054836  

**Project:** Robot Motion Simulator  
**Module Under Test:** `Robot.move(int steps)` — `Robot.java`  
**Test Suite:** `RobotControllerTest.java`  
**QA Role:** Senior Software Test Engineer (Static Analysis)  
**Date:** April 1, 2026

---

## Table of Contents

1. [Objective](#1-objective)
2. [Method Under Test](#2-method-under-test)
3. [DDG Extraction — Variable `newY`](#3-ddg-extraction--variable-newy)
4. [Mutation Generation](#4-mutation-generation)
5. [Reachability-Infection-Propagation Analysis](#5-reachability-infection-propagation-analysis)
6. [Dynamic Slice — Propagation Failure Case](#6-dynamic-slice--propagation-failure-case)
7. [Summary and Mutation Score](#7-summary-and-mutation-score)
8. [Recommendations](#8-recommendations)

---

## 1. Objective

Apply AI-driven static analysis to perform an **LKW Data Flow and Mutation Test** on the Robot Motion Simulator project. The analysis targets five test cases from the existing test suite and evaluates a single **Arithmetic Operator Replacement (AOR)** mutant using the **Reachability-Infection-Propagation (RIP)** framework. The goal is to report the number of **live** (surviving) and **killed** (terminated) mutants and identify weaknesses in the test suite.

---

## 2. Method Under Test

The mutation is applied to `Robot.move(int steps)` in `Robot.java`. This method computes a speculative future position (`newX`, `newY`) for boundary validation before executing the actual step-by-step movement loop.

```java
// Robot.java — Lines 45–81
public boolean move(int steps) {
    if (steps < 0) {                              // Line 46
        System.out.println("Steps must be a non-negative integer.");
        return false;                              // Line 48
    }

    int newX = this.x;                             // Line 51
    int newY = this.y;                             // Line 52

    switch (this.direction) {                      // Line 54
        case NORTH -> newY += steps;               // Line 55  ← MUTATION TARGET
        case EAST  -> newX += steps;               // Line 56
        case SOUTH -> newY -= steps;               // Line 57
        case WEST  -> newX -= steps;               // Line 58
    }

    if (!floor.isValidPosition(newX, newY)) {      // Line 61
        System.out.println("Move would go out of bounds. Move cancelled.");
        return false;                              // Line 63
    }

    for (int i = 0; i < steps; i++) {              // Line 66
        switch (this.direction) {
            case NORTH -> this.y++;                // Line 68
            case SOUTH -> this.y--;
            case EAST  -> this.x++;
            case WEST  -> this.x--;
        }
        if (penDown) {
            floor.mark(x, y);                      // Line 75
        }
    }

    return true;                                   // Line 80
}
```

**Key architectural observation:** The speculative variables `newX`/`newY` (Lines 51–58) are used **only** for boundary validation at Line 61. The actual robot position (`this.x`, `this.y`) is updated independently in the loop (Lines 66–78). This separation is central to the propagation failure analysis.

---

## 3. DDG Extraction — Variable `newY`

### 3.1 Definitions

| Label | Line | Statement               | Condition              |
|:-----:|:----:|--------------------------|------------------------|
| d1    | 52   | `int newY = this.y;`     | Always (method entry)  |
| d2    | 55   | `newY += steps;`         | `direction == NORTH`   |
| d3    | 57   | `newY -= steps;`         | `direction == SOUTH`   |

### 3.2 Uses

| Label | Line | Statement                            | Reads Definition |
|:-----:|:----:|--------------------------------------|:----------------:|
| u1    | 55   | `newY += steps` (RHS read of newY)   | d1               |
| u2    | 57   | `newY -= steps` (RHS read of newY)   | d1               |
| u3    | 61   | `floor.isValidPosition(newX, newY)`  | d2 or d3         |

### 3.3 Definition-Use (DU) Pairs

| DU Pair   | Def → Use     | Def-Clear Path | Path Condition         |
|:---------:|:-------------:|:--------------:|------------------------|
| (d1, u1)  | Line 52 → 55  | Yes            | `direction == NORTH`   |
| (d1, u2)  | Line 52 → 57  | Yes            | `direction == SOUTH`   |
| (d2, u3)  | Line 55 → 61  | Yes            | `direction == NORTH`   |
| (d3, u3)  | Line 57 → 61  | Yes            | `direction == SOUTH`   |

### 3.4 Kill Points

| Definition Killed | Killed By | Condition            |
|:-----------------:|:---------:|----------------------|
| d1 (Line 52)      | d2 (Line 55) | NORTH path       |
| d1 (Line 52)      | d3 (Line 57) | SOUTH path       |

For the EAST and WEST paths, `newY` retains d1's value through to u3 — no redefinition occurs.

### 3.5 Data Dependence Graph

```
  d1 (Line 52: newY = this.y)
       │
       ├──[NORTH]──► u1/d2 (Line 55: newY += steps)──► u3 (Line 61: isValidPosition)
       │
       ├──[SOUTH]──► u2/d3 (Line 57: newY -= steps)──► u3 (Line 61: isValidPosition)
       │
       └──[EAST/WEST]──────────────────────────────────► u3 (Line 61: isValidPosition)
```

---

## 4. Mutation Generation

### 4.1 Mutant Specification

| Property         | Value                                       |
|------------------|---------------------------------------------|
| **Mutant ID**    | m1                                          |
| **Operator**     | AOR (Arithmetic Operator Replacement)       |
| **Location**     | `Robot.java`, Line 55                       |
| **Original**     | `case NORTH -> newY += steps;`              |
| **Mutated**      | `case NORTH -> newY *= steps;`              |
| **Change**       | `+` replaced with `*`                       |

### 4.2 Semantic Impact

| Scenario (`this.y`, `steps`) | Original `newY` | Mutant `newY` | Differ? |
|:----------------------------:|:----------------:|:-------------:|:-------:|
| (0, 5)                       | 5                | 0             | Yes     |
| (0, 3)                       | 3                | 0             | Yes     |
| (3, 3)                       | 6                | 9             | Yes     |
| (5, 3)                       | 8                | 15            | Yes     |
| (1, 1)                       | 2                | 1             | Yes     |
| (0, 0)                       | 0                | 0             | **No**  |

The mutant produces identical output only when `this.y == 0 && steps == 0`, or trivially when `this.y == 1 && steps == 2` (since 1+2 = 3, 1*2 = 2 — still differs). The infection condition is broadly satisfied for non-zero inputs.

---

## 5. Reachability-Infection-Propagation Analysis

Five test cases were selected from `RobotControllerTest.java` and evaluated against mutant **m1**.

### Test Case 1: `testMoveCommand` (Lines 49–54) — KILLED

```java
assertTrue(controller.processCommand("M 5"));
assertEquals(5, controller.getRobot().getY());
assertTrue(controller.processCommand("m 3"));
assertEquals(8, controller.getRobot().getY());
```

**Setup:** 10×10 floor, robot at (0, 0), facing NORTH.

| Phase | 1st Call: `"M 5"` | 2nd Call: `"m 3"` |
|-------|--------------------|--------------------|
| **Reachability** | `move(5)` → NORTH → Line 55 executed | `move(3)` → NORTH → Line 55 executed |
| **Infection** | Orig: `newY = 0 + 5 = 5`; Mutant: `newY = 0 × 5 = 0` | Orig: `newY = 5 + 3 = 8`; Mutant: `newY = 5 × 3 = 15` |
| **Propagation** | Both pass `isValidPosition` → return `true` | Orig: `isValidPosition(0,8)` = true → `true`; Mutant: `isValidPosition(0,15)` = **false** → `false` |
| **Assertion** | `assertTrue` passes for both | `assertTrue` **FAILS** for mutant |

> **Result: MUTANT KILLED.** The second move starts from y=5 where multiplication (5×3=15) exceeds the floor boundary, causing a divergent return value.

---

### Test Case 2: `testNegativeMoveCommand` (Lines 96–98) — LIVES

```java
assertFalse(controller.processCommand("M -5"));
```

| Phase            | Analysis |
|------------------|----------|
| **Reachability** | In `processCommand`, Line 90: `value < 0` evaluates to `true` → returns `false` immediately. `robot.move()` is **never invoked**. Line 55 is unreachable. |
| **Infection**    | N/A |
| **Propagation**  | N/A |

> **Result: MUTANT LIVES** — reachability failure. The negative-value guard in `processCommand` prevents execution from reaching the mutated line.

---

### Test Case 3: `testPenDownCommand` (Lines 30–34) — LIVES

```java
assertTrue(controller.processCommand("D"));
assertTrue(controller.processCommand("d"));
assertTrue(controller.getRobot().isPenDown());
```

| Phase            | Analysis |
|------------------|----------|
| **Reachability** | Both `"D"` and `"d"` are single-character commands handled by `case 'D'` in the switch (Line 56 of `RobotController.java`). `robot.move()` is **never invoked**. |
| **Infection**    | N/A |
| **Propagation**  | N/A |

> **Result: MUTANT LIVES** — reachability failure. This test exercises pen control logic only; the movement subsystem is untouched.

---

### Test Case 4: `testCaseInsensitiveCommands` (Lines 107–112) — LIVES

```java
assertTrue(controller.processCommand("d"));
assertTrue(controller.processCommand("D"));
assertTrue(controller.processCommand("m 3"));
assertTrue(controller.processCommand("M 3"));
```

**Setup:** 10×10 floor, robot at (0, 0), facing NORTH.

| Phase | 3rd Call: `"m 3"` (robot at y=0) | 4th Call: `"M 3"` (robot at y=3) |
|-------|----------------------------------|----------------------------------|
| **Reachability** | `move(3)` → NORTH → Line 55 executed ✓ | `move(3)` → NORTH → Line 55 executed ✓ |
| **Infection** | Orig: `newY = 0 + 3 = 3`; Mutant: `newY = 0 × 3 = 0`. **States differ.** ✓ | Orig: `newY = 3 + 3 = 6`; Mutant: `newY = 3 × 3 = 9`. **States differ.** ✓ |
| **Propagation** | `isValidPosition(0,3)` = true; `isValidPosition(0,0)` = true. Both return `true`. ✗ | `isValidPosition(0,6)` = true; `isValidPosition(0,9)` = true (9 < 10). Both return `true`. ✗ |

> **Result: MUTANT LIVES** — propagation failure. The infected `newY` value (0 instead of 3, and 9 instead of 6) still falls within the valid floor range [0, 10), so `isValidPosition` evaluates identically. The corruption is absorbed at Line 61. The test only asserts `assertTrue` on the return, not on the computed boundary value.

---

### Test Case 5: `testCommandHistory` (Lines 74–79) — LIVES

```java
controller.processCommand("D");
controller.processCommand("M 5");
controller.processCommand("R");
assertEquals(3, controller.getCommandHistory().size());
```

**Setup:** 10×10 floor, robot at (0, 0), facing NORTH.

| Phase            | Analysis for `"M 5"` |
|------------------|-----------------------|
| **Reachability** | `move(5)` → NORTH → Line 55 executed ✓ |
| **Infection**    | Orig: `newY = 0 + 5 = 5`; Mutant: `newY = 0 × 5 = 0`. **States differ.** ✓ |
| **Propagation**  | Both pass `isValidPosition` → return `true`. Commands are added to history **before** processing (Line 46 of `RobotController.java`), so history size is 3 regardless. The test asserts only on `getCommandHistory().size()`. ✗ |

> **Result: MUTANT LIVES** — propagation failure. The test oracle observes only command history length, which is completely independent of the mutated boundary computation.

---

## 6. Dynamic Slice — Propagation Failure Case

**Test case:** `testCaseInsensitiveCommands`, sub-call `processCommand("m 3")` with robot at (0, 0) facing NORTH on a 10×10 floor.

**Slicing criterion:** `return moved;` at Line 98 of `RobotController.java`.

| Step | File                   | Line | Statement                                           | Value (Orig) | Value (Mutant) |
|:----:|------------------------|:----:|-----------------------------------------------------|:------------:|:--------------:|
| 1    | RobotController.java   | 37   | `command == null \|\| command.trim().isEmpty()`      | false        | false          |
| 2    | RobotController.java   | 41   | `command = command.trim()`                           | `"m 3"`      | `"m 3"`        |
| 3    | RobotController.java   | 42   | `String upperCommand = command.toUpperCase()`        | `"M 3"`      | `"M 3"`        |
| 4    | RobotController.java   | 50   | `command.length() == 1`                              | false        | false          |
| 5    | RobotController.java   | 82   | `String[] parts = command.split("\\s+")`             | `["m","3"]`  | `["m","3"]`    |
| 6    | RobotController.java   | 83   | `parts.length == 2`                                  | true         | true           |
| 7    | RobotController.java   | 84   | `char cmd = parts[0].toUpperCase().charAt(0)`        | `'M'`        | `'M'`          |
| 8    | RobotController.java   | 86   | `int value = Integer.parseInt(parts[1])`             | 3            | 3              |
| 9    | RobotController.java   | 88   | `switch (cmd)` → case `'M'`                         | —            | —              |
| 10   | RobotController.java   | 90   | `value < 0`                                          | false        | false          |
| 11   | RobotController.java   | 94   | `boolean moved = robot.move(value)`                  | → enter move | → enter move   |
| 12   | Robot.java             | 46   | `steps < 0`                                          | false        | false          |
| 13   | Robot.java             | 51   | `int newX = this.x`                                  | 0            | 0              |
| 14   | Robot.java             | 52   | `int newY = this.y`                                  | 0            | 0              |
| 15   | Robot.java             | 54   | `switch (this.direction)` → NORTH                    | —            | —              |
| **16** | **Robot.java**       | **55** | **`newY += steps`** ← **MUTATED**                 | **3**        | **0**          |
| 17   | Robot.java             | 61   | `!floor.isValidPosition(newX, newY)`                 | false        | false          |
| 18   | Robot.java             | 80   | `return true`                                        | true         | true           |
| 19   | RobotController.java   | 94   | `moved = true`                                       | true         | true           |
| 20   | RobotController.java   | 95   | `!moved`                                             | false        | false          |
| 21   | RobotController.java   | 98   | `return moved`                                       | **true**     | **true**       |

### Propagation Failure Explanation

At **Step 16**, the mutation causes an infection: `newY` is 0 (mutant) instead of 3 (original). This corrupted value flows into **Step 17** where `floor.isValidPosition(0, 0)` and `floor.isValidPosition(0, 3)` are both evaluated. Since the floor is 10×10 and both 0 and 3 fall within the valid range [0, 10), the predicate returns `true` in both cases. The branch at Line 61 is **not taken** in either execution. From Step 18 onward, both executions follow an identical path, and the return value `true` propagates unchanged to the caller. The infection is **absorbed** at the `isValidPosition` boundary check.

---

## 7. Summary and Mutation Score

### Results Table

| #  | Test Case                      | Reachability | Infection | Propagation | Verdict     |
|:--:|--------------------------------|:------------:|:---------:|:-----------:|:-----------:|
| 1  | `testMoveCommand`              | ✓            | ✓         | ✓           | **KILLED**  |
| 2  | `testNegativeMoveCommand`      | ✗            | —         | —           | **LIVES**   |
| 3  | `testPenDownCommand`           | ✗            | —         | —           | **LIVES**   |
| 4  | `testCaseInsensitiveCommands`  | ✓            | ✓         | ✗           | **LIVES**   |
| 5  | `testCommandHistory`           | ✓            | ✓         | ✗           | **LIVES**   |

### Final Count

| Metric               | Value    |
|----------------------|:--------:|
| **Killed (Terminated)** | **1**    |
| **Live (Surviving)**    | **4**    |
| **Total Test Cases**    | 5        |
| **Mutation Score**      | **20%**  |

### Failure Breakdown

| Failure Type            | Count | Test Cases           |
|-------------------------|:-----:|----------------------|
| Reachability Failure    | 2     | TC2, TC3             |
| Propagation Failure     | 2     | TC4, TC5             |
| Fully Killed            | 1     | TC1                  |

---

## 8. Recommendations

### 8.1 Strengthen Boundary-Sensitive Assertions

Tests 4 and 5 exhibit **propagation failure** because their assertions do not observe the boundary computation. Adding assertions that verify the robot's position after movement (e.g., `assertEquals(expectedY, robot.getY())`) would force the corrupted path to diverge.

### 8.2 Add Boundary-Edge Test Cases

The mutant survives when the robot starts at y=0 (since 0 × steps = 0, which is always valid). Test cases where the robot starts at a non-zero y-position and moves far enough that `y + steps` is valid but `y × steps` is not would kill this mutant:

```
Example: robot at y=4, move 5 on 10×10 floor
  Original:  newY = 4 + 5 = 9   → valid   → returns true
  Mutant:    newY = 4 × 5 = 20  → invalid → returns false   ← KILLED
```

### 8.3 Improve Reachability Coverage

Tests 2 and 3 never reach `Robot.move()` at all. The test suite would benefit from additional move-specific test cases that exercise `move()` from diverse starting positions and directions.

---

*Report generated via AI Static Analysis — Senior Software Test Engineer methodology.*
