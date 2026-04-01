# QA Test Report — Black-Box and White-Box Testing

**Name:** Khalid Abdulrahim  
**Student ID:** 40054836  
**Course:** COEN 448 — Software Testing and Validation  
**Date:** March 28, 2026  

**Application Under Test:** Robot Motion Simulator  
**Dev Team Author:** Faisal AlMasri  
**GitHub Repository:** [https://github.com/FasAlmasri/robot-motion-simulator](https://github.com/FasAlmasri/robot-motion-simulator)

---

## Table of Contents

1. [Project Overview](#1-project-overview)
2. [Build Configuration](#2-build-configuration)
3. [Test Cases for Coverage Criteria (a–d)](#3-test-cases-for-coverage-criteria-ad)
4. [Mutation Testing (e)](#4-mutation-testing-e)
5. [Data Flow Testing (f)](#5-data-flow-testing-f)
6. [Test Results](#6-test-results)
7. [Test Conclusion](#7-test-conclusion)
8. [Summary and Comments for the Dev Team](#8-summary-and-comments-for-the-dev-team)

---

## 1. Project Overview

The Robot Motion Simulator is a Java application that models a robot moving on an N x N grid floor. The robot can:

- Move forward, turn left/right
- Raise/lower a drawing pen
- Print the floor grid and robot status
- Initialize a new floor, replay command history, and quit

### Source Classes Under Test

| Class | Lines | Description |
|-------|:-----:|-------------|
| `RobotController.java` | 183 | Command parser and dispatcher |
| `Robot.java` | 122 | Robot state: position, direction, pen, movement |
| `Floor.java` | 102 | N x N grid with mark/clear/print |
| `Direction.java` | 41 | Enum for NORTH/EAST/SOUTH/WEST with turn logic |
| `Main.java` | 75 | Console entry point (excluded from unit testing) |

### Test Classes

| Test Class | Test Methods |
|------------|:------------:|
| `RobotControllerTest.java` | 48 |
| `RobotTest.java` | 15 |
| `FloorTest.java` | 8 |
| `DirectionTest.java` | 11 |
| **Total** | **82** |

---

## 2. Build Configuration

The project uses **Maven** with the following plugins configured in `pom.xml`:

| Tool | Plugin | Purpose |
|------|--------|---------|
| **JUnit 5** | `junit-jupiter 5.9.3` | Unit test framework |
| **JaCoCo** | `jacoco-maven-plugin 0.8.12` | Code coverage (statement, branch) |
| **PITest** | `pitest-maven 1.15.3` | Mutation testing |
| **Surefire** | `maven-surefire-plugin 3.0.0` | Test runner |

### Commands to Reproduce

```bash
# Run all tests with coverage
mvn clean test

# Generate mutation testing report
mvn org.pitest:pitest-maven:mutationCoverage
```

### Report Locations

| Report | Path |
|--------|------|
| JaCoCo HTML | `target/site/jacoco/index.html` |
| JaCoCo CSV | `target/site/jacoco/jacoco.csv` |
| PITest HTML | `target/pit-reports/index.html` |
| PITest XML | `target/pit-reports/mutations.xml` |

---

## 3. Test Cases for Coverage Criteria (a–d)

### 3.1 (a) Statement Coverage

**Tool:** JaCoCo (INSTRUCTION counter)  
**Threshold:** > 50%  
**Result: 84.1% overall (98.5% excluding Main)**

| Class | Missed | Covered | Total | Coverage |
|-------|:------:|:-------:|:-----:|:--------:|
| RobotController | 0 | 250 | 250 | **100%** |
| Robot | 2 | 204 | 206 | **99.0%** |
| Direction | 4 | 57 | 61 | **93.4%** |
| Floor | 4 | 154 | 158 | **97.5%** |
| Main | 116 | 0 | 116 | 0% |
| **Overall** | **126** | **665** | **791** | **84.1%** |

**Key test cases contributing to statement coverage:**

| # | Test Case | Class | Statements Covered |
|---|-----------|-------|--------------------|
| 1 | `testMoveForwardNorth` | RobotControllerTest | `processCommand` parsing, `Robot.move()` NORTH path |
| 2 | `testMoveForwardEast` | RobotControllerTest | `Robot.move()` EAST path, `turnRight()` |
| 3 | `testMoveForwardSouth` | RobotControllerTest | `Robot.move()` SOUTH path |
| 4 | `testMoveForwardWest` | RobotControllerTest | `Robot.move()` WEST path |
| 5 | `testPenDownCommand` | RobotControllerTest | Pen down path, `Robot.penDown()`, `Floor.mark()` |
| 6 | `testPenUpCommand` | RobotControllerTest | Pen up path, `Robot.penUp()` |
| 7 | `testInitializeCommand` | RobotControllerTest | Initialize path, `new Floor()`, `new Robot()` |
| 8 | `testNullCommand` | RobotControllerTest | Null guard (line 37) |
| 9 | `testEmptyCommand` | RobotControllerTest | Empty string guard (line 37) |
| 10 | `testMoveWithNonNumericArg` | RobotControllerTest | `NumberFormatException` catch block (line 111-113) |
| 11 | `testInvalidSingleCharCommand` | RobotControllerTest | Default switch branch (line 77) |
| 12 | `testInvalidTwoPartCommand` | RobotControllerTest | Default multi-char switch branch (line 109) |
| 13 | `testNegativeMoveCommand` | RobotControllerTest | Negative move guard (line 90-92) |
| 14 | `testInitializeZero` | RobotControllerTest | Zero init guard (line 101-103) |
| 15 | `testDrawWithPenDown` | RobotTest | Pen-down marking loop in `move()` |
| 16 | `testNoDrawWithPenUp` | RobotTest | Pen-up skip in `move()` |
| 17 | `testFloorInitialization` | FloorTest | Floor constructor, grid init |
| 18 | `testInvalidFloorSize` | FloorTest | Floor constructor exception path |
| 19 | `testClear` | FloorTest | `Floor.clear()` nested loops |
| 20 | `testGetCellOutOfBounds` | FloorTest | `getCell()` invalid position path |
| 21 | `testTurnRightFromNorth` | DirectionTest | `turnRight()` NORTH case |
| 22 | `testTurnLeftFromNorth` | DirectionTest | `turnLeft()` NORTH case |
| 23 | `testToString` | DirectionTest | `Direction.toString()` |

### 3.2 (b) Decision Coverage (Branch Coverage)

**Tool:** JaCoCo (BRANCH counter)  
**Threshold:** > 50%  
**Result: 84.7% overall (92.2% excluding Main)**

| Class | Missed | Covered | Total | Coverage |
|-------|:------:|:-------:|:-----:|:--------:|
| RobotController | 0 | 34 | 34 | **100%** |
| Robot | 3 | 17 | 20 | **85.0%** |
| Direction | 2 | 8 | 10 | **80.0%** |
| Floor | 2 | 24 | 26 | **92.3%** |
| Main | 8 | 0 | 8 | 0% |
| **Overall** | **15** | **83** | **98** | **84.7%** |

**Key decisions tested and their true/false outcomes:**

| # | Decision (Class:Line) | True Test Case | False Test Case |
|---|----------------------|----------------|-----------------|
| 1 | `command == null` (RC:37) | `testNullCommand` | `testPenUpCommand` |
| 2 | `command.trim().isEmpty()` (RC:37) | `testEmptyCommand` | `testMoveForwardNorth` |
| 3 | `!upperCommand.equals("Q")` (RC:45) | `testCommandHistory` | `testQuitNotInHistory` |
| 4 | `!upperCommand.equals("H")` (RC:45) | `testCommandHistory` | `testHistoryNotInHistory` |
| 5 | `command.length() == 1` (RC:50) | `testPenUpCommand` | `testMoveForwardNorth` |
| 6 | `parts.length == 2` (RC:83) | `testMoveForwardNorth` | `testThreePartCommand` |
| 7 | `value < 0` (RC:90) | `testNegativeMoveCommand` | `testMoveForwardNorth` |
| 8 | `value <= 0` (RC:101) | `testInitializeZero` | `testInitializeCommand` |
| 9 | `!moved` (RC:95) | `testMoveOutOfBounds` | `testMoveForwardNorth` |
| 10 | `steps < 0` (Robot:46) | `testMoveNegativeSpaces` | `testMoveNorth` |
| 11 | `!floor.isValidPosition(...)` (Robot:61) | `testMoveOutOfBounds` | `testMoveNorth` |
| 12 | `penDown` (Robot:74) | `testDrawWithPenDown` | `testNoDrawWithPenUp` |
| 13 | `n <= 0` (Floor:15) | `testInvalidFloorSize` | `testFloorInitialization` |
| 14 | `isValidPosition(x,y)` (Floor:29) | `testMarkCell` | (implicit out-of-bounds) |
| 15 | `x >= 0 && x < size && y >= 0 && y < size` (Floor:41) | `testValidPosition` | `testGetCellOutOfBounds` |

*(RC = RobotController)*

### 3.3 (c) Condition Coverage

Condition coverage requires each **atomic boolean sub-expression** to evaluate to both `true` and `false`.

**Result: > 50% — all compound conditions individually exercised**

| # | Compound Condition | Atom | True By | False By |
|---|-------------------|------|---------|----------|
| 1 | `command == null \|\| command.trim().isEmpty()` | `command == null` | `testNullCommand` | `testEmptyCommand` |
| | | `command.trim().isEmpty()` | `testEmptyCommand` | `testPenUpCommand` |
| 2 | `!upper.equals("Q") && !upper.equals("H")` | `!upper.equals("Q")` | `testCommandHistory` (cmd="D") | `testQuitNotInHistory` (cmd="Q") |
| | | `!upper.equals("H")` | `testCommandHistory` (cmd="D") | `testHistoryNotInHistory` (cmd="H") |
| 3 | `x >= 0 && x < size && y >= 0 && y < size` | `x >= 0` | `testValidPosition(0,0)` | `testValidPosition(-1,0)` |
| | | `x < size` | `testValidPosition(5,5)` | `testValidPosition(10,0)` |
| | | `y >= 0` | `testValidPosition(0,0)` | `testValidPosition(0,-1)` |
| | | `y < size` | `testValidPosition(5,5)` | `testValidPosition(0,10)` |

### 3.4 (d) Multiple Condition Coverage

Multiple condition coverage requires every combination of truth values for compound conditions.

**Focus: `isValidPosition(int x, int y)` in `Floor.java` (Line 41)**

Condition: `x >= 0 && x < size && y >= 0 && y < size` (floor size = 10)

| # | x >= 0 | x < size | y >= 0 | y < size | Test Case | Result |
|---|:------:|:--------:|:------:|:--------:|-----------|:------:|
| 1 | T | T | T | T | `testValidPosition` → `(5, 5)` | true |
| 2 | T | T | T | F | `testValidPosition` → `(0, 10)` | false |
| 3 | T | T | F | — | `testValidPosition` → `(0, -1)` | false |
| 4 | T | F | — | — | `testValidPosition` → `(10, 0)` | false |
| 5 | F | — | — | — | `testValidPosition` → `(-1, 0)` | false |

Due to Java's short-circuit evaluation, once a sub-expression is false the remaining ones are not evaluated. All feasible combinations (5 of 5 reachable paths) are covered.

**Focus: `command == null || command.trim().isEmpty()` in `RobotController.java` (Line 37)**

| # | command == null | trim().isEmpty() | Test Case | Result |
|---|:--------------:|:----------------:|-----------|:------:|
| 1 | T | — (short-circuit) | `testNullCommand` → `null` | true |
| 2 | F | T | `testEmptyCommand` → `""` | true |
| 3 | F | F | `testPenUpCommand` → `"U"` | false |

All 3 feasible combinations covered (combination null=F, empty=F with non-empty is the normal path).

---

## 4. Mutation Testing (e)

### 4.1 Tool and Configuration

**Tool:** PITest 1.15.3 with JUnit 5 plugin  
**Target Function:** `Robot.move(int steps)` (selected for detailed analysis)  
**Mutators:** DEFAULTS (Conditionals Boundary, Math, Negate Conditionals, Remove Conditionals, Void Method Calls, Boolean Returns, Primitive Returns, Empty Object Returns, Null Returns)

### 4.2 Project-Wide Mutation Summary

| Metric | Value |
|--------|:-----:|
| Total Mutations Generated | **155** |
| Killed | **88** |
| Survived | **34** |
| No Coverage | **33** |
| **Mutation Score** | **57%** |
| **Test Strength** (killed / covered) | **72%** |

### 4.3 Mutations by Class

| Class | Generated | Killed | Survived | No Coverage | Score |
|-------|:---------:|:------:|:--------:|:-----------:|:-----:|
| RobotController | 56 | 37 | 19 | 0 | 66% |
| Robot | 28 | 21 | 7 | 0 | 75% |
| Floor | 33 | 22 | 10 | 1 | 67% |
| Direction | 11 | 8 | 0 | 3 | 73% |
| Main | 27 | 0 | 0 | 27 | 0% |

### 4.4 Complete List of Mutations for `Robot.move()`

The following table lists all 25 mutations generated by PITest for the selected function `Robot.move(int steps)`:

| # | Line | Mutator | Description | Status | Killing Test |
|---|:----:|---------|-------------|:------:|-------------|
| 1 | 46 | ConditionalsBoundary | Changed `steps < 0` boundary | KILLED | `testMoveZeroSteps` |
| 2 | 66 | ConditionalsBoundary | Changed `i < steps` boundary | KILLED | `testMoveNorth` |
| 3 | 55 | Math | Replaced `+` with `-` in `newY += steps` | KILLED | `testMoveNorth` |
| 4 | 56 | Math | Replaced `+` with `-` in `newX += steps` | KILLED | `testMoveEast` |
| 5 | 57 | Math | Replaced `-` with `+` in `newY -= steps` | SURVIVED | — |
| 6 | 58 | Math | Replaced `-` with `+` in `newX -= steps` | SURVIVED | — |
| 7 | 68 | Math | Replaced `++` with `--` in `this.y++` | KILLED | `testMoveNorth` |
| 8 | 69 | Math | Replaced `--` with `++` in `this.y--` | KILLED | `testMoveSouth` |
| 9 | 70 | Math | Replaced `++` with `--` in `this.x++` | KILLED | `testMoveEast` |
| 10 | 71 | Math | Replaced `--` with `++` in `this.x--` | KILLED | `testMoveWest` |
| 11 | 61 | RemoveConditional (EQUAL) | Removed `!isValidPosition` check | KILLED | `testMoveOutOfBounds` |
| 12 | 74 | RemoveConditional (EQUAL) | Removed `penDown` check | KILLED | `testDrawWithPenDown` |
| 13 | 46 | RemoveConditional (ORDER) | Removed `steps < 0` check | SURVIVED | — |
| 14 | 66 | RemoveConditional (ORDER) | Removed loop condition | KILLED | `testMoveNorth` |
| 15 | 47 | VoidMethodCall | Removed `println` (error msg) | SURVIVED | — |
| 16 | 62 | VoidMethodCall | Removed `println` (error msg) | SURVIVED | — |
| 17 | 75 | VoidMethodCall | Removed `floor.mark(x,y)` | KILLED | `testDrawWithPenDown` |
| 18 | 80 | BooleanFalseReturn | Replaced `return true` with `false` | KILLED | `testMoveNorth` |
| 19 | 48 | BooleanTrueReturn | Replaced `return false` with `true` (neg) | KILLED | `testMoveNegativeSpaces` |
| 20 | 63 | BooleanTrueReturn | Replaced `return false` with `true` (OOB) | KILLED | `testMoveOutOfBounds` |

**Mutation Score for `Robot.move()`:** 15 killed / 20 total = **75%**

### 4.5 Analysis of Survived Mutants

| Survived Mutant | Reason |
|----------------|--------|
| Line 57: `newY -= steps` → `newY += steps` | The SOUTH-path bounds check uses `newY` only for validation. When moving south from y=5 by 3 steps, both `5-3=2` and `5+3=8` are valid, so the bounds check passes identically. Test suite lacks a south move that would exceed bounds under the mutant. |
| Line 58: `newX -= steps` → `newX += steps` | Same reasoning for the WEST path. |
| Line 46: Remove `steps < 0` | All negative-step tests go through `processCommand` which catches `value < 0` before calling `move()`. The direct `Robot.move(-5)` test returns `false` regardless because the check is redundant with the caller. |
| Lines 47, 62: Remove `println` | Tests don't capture `System.out` output, so removing print statements has no observable effect on assertions. |

---

## 5. Data Flow Testing (f)

### 5.1 Selected Function

**Function:** `Robot.move(int steps)` in `Robot.java` (Lines 45–81)

### 5.2 Variable: `newY` — Definition-Use Analysis

#### Definitions

| ID | Line | Statement | Condition |
|:--:|:----:|-----------|-----------|
| d1 | 52 | `int newY = this.y` | Always |
| d2 | 55 | `newY += steps` | direction == NORTH |
| d3 | 57 | `newY -= steps` | direction == SOUTH |

#### Uses

| ID | Line | Statement | Type |
|:--:|:----:|-----------|------|
| u1 | 55 | `newY += steps` (RHS) | c-use |
| u2 | 57 | `newY -= steps` (RHS) | c-use |
| u3 | 61 | `floor.isValidPosition(newX, newY)` | p-use |

#### DU-Pairs

| DU-Pair | Def → Use | Path Condition |
|:-------:|:---------:|---------------|
| (d1, u1) | 52 → 55 | NORTH |
| (d1, u2) | 52 → 57 | SOUTH |
| (d2, u3) | 55 → 61 | NORTH |
| (d3, u3) | 57 → 61 | SOUTH |

#### Kill Points

- d1 (Line 52) is killed by d2 (Line 55) on the NORTH path
- d1 (Line 52) is killed by d3 (Line 57) on the SOUTH path

### 5.3 Variable: `newX` — Definition-Use Analysis

#### Definitions

| ID | Line | Statement | Condition |
|:--:|:----:|-----------|-----------|
| d4 | 51 | `int newX = this.x` | Always |
| d5 | 56 | `newX += steps` | direction == EAST |
| d6 | 58 | `newX -= steps` | direction == WEST |

#### Uses

| ID | Line | Statement | Type |
|:--:|:----:|-----------|------|
| u4 | 56 | `newX += steps` (RHS) | c-use |
| u5 | 58 | `newX -= steps` (RHS) | c-use |
| u6 | 61 | `floor.isValidPosition(newX, newY)` | p-use |

#### DU-Pairs

| DU-Pair | Def → Use | Path Condition |
|:-------:|:---------:|---------------|
| (d4, u4) | 51 → 56 | EAST |
| (d4, u5) | 51 → 58 | WEST |
| (d5, u6) | 56 → 61 | EAST |
| (d6, u6) | 58 → 61 | WEST |

### 5.4 Data Flow Test Cases

The following test cases achieve **All-DU-Pairs coverage** for variables `newY` and `newX`:

| # | Test Case | Input | Direction | DU-Pairs Covered | Expected |
|---|-----------|-------|-----------|-----------------|----------|
| 1 | `testMoveNorth` | `move(5)` from (0,0) | NORTH | (d1,u1), (d2,u3) | y=5, returns true |
| 2 | `testMoveSouth` | `move(3)` from (0,5) | SOUTH | (d1,u2), (d3,u3) | y=2, returns true |
| 3 | `testMoveEast` | `move(7)` from (0,0) | EAST | (d4,u4), (d5,u6) | x=7, returns true |
| 4 | `testMoveWest` | `move(3)` from (5,0) | WEST | (d4,u5), (d6,u6) | x=2, returns true |
| 5 | `testMoveOutOfBounds` | `move(15)` from (0,0) | NORTH | (d1,u1), (d2,u3) — false branch | returns false |
| 6 | `testDrawWithPenDown` | `move(3)` pen=down | NORTH | (d1,u1), (d2,u3) + `penDown` p-use | cells marked |
| 7 | `testNoDrawWithPenUp` | `move(3)` pen=up | NORTH | (d1,u1), (d2,u3) + `penDown` p-use (false) | cells unmarked |
| 8 | `testMoveNegativeSpaces` | `move(-5)` | — | Guard at line 46 | returns false |

### 5.5 Coverage Summary

| Coverage Criterion | DU-Pairs Covered | Total | Percentage |
|-------------------|:----------------:|:-----:|:----------:|
| All-Defs | 6/6 | 6 | **100%** |
| All-Uses | 8/8 | 8 | **100%** |
| All-DU-Pairs | 8/8 | 8 | **100%** |

### 5.6 Data Flow Graph

```
    ┌─────────────────────────────────────┐
    │ Entry: move(steps)                  │
    └──────────────┬──────────────────────┘
                   ▼
    ┌─────────────────────────────────────┐
    │ Line 46: if (steps < 0)             │──[true]──► return false (Line 48)
    └──────────────┬──────────────────────┘
              [false]
                   ▼
    ┌─────────────────────────────────────┐
    │ Line 51: d4: newX = this.x          │
    │ Line 52: d1: newY = this.y          │
    └──────────────┬──────────────────────┘
                   ▼
    ┌─────────────────────────────────────┐
    │ Line 54: switch (direction)         │
    ├──[NORTH]──► Line 55: d2: newY += steps (uses d1)
    ├──[EAST ]──► Line 56: d5: newX += steps (uses d4)
    ├──[SOUTH]──► Line 57: d3: newY -= steps (uses d1)
    └──[WEST ]──► Line 58: d6: newX -= steps (uses d4)
                   ▼
    ┌─────────────────────────────────────┐
    │ Line 61: if (!isValidPosition       │
    │            (newX, newY))            │──[true]──► return false (Line 63)
    │          (uses d2/d3, d5/d6)        │
    └──────────────┬──────────────────────┘
              [false]
                   ▼
    ┌─────────────────────────────────────┐
    │ Line 66-78: for loop (move steps)   │
    │   this.x++/-- or this.y++/--        │
    │   if (penDown) floor.mark(x,y)      │
    └──────────────┬──────────────────────┘
                   ▼
    ┌─────────────────────────────────────┐
    │ Line 80: return true                │
    └─────────────────────────────────────┘
```

---

## 6. Test Results

### 6.1 Test Execution Summary

```
-------------------------------------------------------
 T E S T S
-------------------------------------------------------
Running com.concordia.robot.DirectionTest
Tests run: 11, Failures: 0, Errors: 0, Skipped: 0

Running com.concordia.robot.FloorTest
Tests run: 8,  Failures: 0, Errors: 0, Skipped: 0

Running com.concordia.robot.RobotControllerTest
Tests run: 48, Failures: 0, Errors: 0, Skipped: 0

Running com.concordia.robot.RobotTest
Tests run: 15, Failures: 0, Errors: 0, Skipped: 0

Results:
Tests run: 82, Failures: 0, Errors: 0, Skipped: 0

BUILD SUCCESS
```

**All 82 test cases pass with 0 failures.**

### 6.2 JaCoCo Coverage Summary

| Metric | Missed | Covered | Total | Percentage |
|--------|:------:|:-------:|:-----:|:----------:|
| Instructions | 126 | 665 | 791 | **84.1%** |
| Branches | 15 | 83 | 98 | **84.7%** |
| Lines | 46 | 170 | 216 | **78.7%** |
| Methods | 12 | 31 | 43 | **72.1%** |

*(Excluding `Main.java`: Instructions 98.5%, Branches 92.2%, Lines 98.3%)*

**Screenshot Reference:** `target/site/jacoco/index.html` — open in browser after running `mvn clean test`

### 6.3 PITest Mutation Summary

| Metric | Value |
|--------|:-----:|
| Total Mutations | 155 |
| Killed | 88 (57%) |
| Survived | 34 |
| No Coverage | 33 |
| Test Strength | 72% |

**Screenshot Reference:** `target/pit-reports/index.html` — open in browser after running `mvn org.pitest:pitest-maven:mutationCoverage`

### 6.4 Per-Class Coverage Results

| Class | Statement | Branch | Mutations Killed |
|-------|:---------:|:------:|:----------------:|
| RobotController | 100% | 100% | 66% |
| Robot | 99.0% | 85.0% | 75% |
| Floor | 97.5% | 92.3% | 67% |
| Direction | 93.4% | 80.0% | 73% |
| Main | 0% | 0% | 0% |

---

## 7. Test Conclusion

### 7.1 Combined Coverage Analysis

| Criterion | Target | Achieved | Status |
|-----------|:------:|:--------:|:------:|
| (a) Statement Coverage | > 50% | **84.1%** | PASS |
| (b) Decision/Branch Coverage | > 50% | **84.7%** | PASS |
| (c) Condition Coverage | > 50% | **100%** (all atoms T/F) | PASS |
| (d) Multiple Condition Coverage | > 50% | **100%** (all feasible combos) | PASS |
| (e) Mutation Testing | — | **57%** mutation score, **72%** test strength | REPORTED |
| (f) Data Flow Testing | All-DU-Pairs | **100%** for `Robot.move()` | PASS |

### 7.2 QA Verdict

The application under test **passes all coverage thresholds** defined in the assignment. Key findings:

1. **Structural coverage is strong.** Excluding the untestable `Main.java` (console I/O), the four testable classes achieve 98.5% instruction coverage and 92.2% branch coverage.

2. **Mutation testing reveals gaps.** While 72% test strength is acceptable, 34 mutants survive. The majority are:
   - `System.out.println` removals (tests don't capture console output)
   - Equivalent or quasi-equivalent mutants in SOUTH/WEST boundary checks
   - `replayHistory()` side-effect mutants

3. **Data flow testing is complete** for the selected function `Robot.move()`, achieving 100% All-DU-Pairs coverage across both `newX` and `newY` variables.

### 7.3 Pass/Fail Criterion

A test case is considered **passing** if all assertions hold and the method returns the expected value. The overall QA result is **PASS** if:
- All 82 unit tests pass (verified: 0 failures)
- All four coverage criteria exceed 50% (verified: all > 80%)
- Mutation testing report is generated and analyzed
- Data flow testing achieves All-DU-Pairs coverage for the selected function

**QA Result: PASS**

---

## 8. Summary and Comments for the Dev Team

### 8.1 Strengths

1. **Clean architecture.** The separation of `Robot`, `Floor`, `Direction`, and `RobotController` enables focused unit testing of each component independently.
2. **Command parsing is robust.** Case-insensitive handling, null/empty guards, and `NumberFormatException` handling are all present and well-structured.
3. **Boundary checks are correct.** The speculative position check before movement prevents the robot from going out of bounds.

### 8.2 Issues Found

| # | Severity | Description | Location |
|---|----------|-------------|----------|
| 1 | Low | `Direction.turnRight()` and `turnLeft()` have unreachable `default` branches (enum exhaustive). These generate NO_COVERAGE mutants. | Direction.java:19, 33 |
| 2 | Low | `Floor.print()` output is not testable via unit tests (writes directly to `System.out`). Consider returning a `String` or accepting a `PrintStream` parameter. | Floor.java:70-90 |
| 3 | Low | `Robot.printStatus()` writes directly to `System.out` — same issue. | Robot.java:96-100 |
| 4 | Info | The `Main.java` class has 0% coverage, which is expected for a console-based entry point. No changes needed. | Main.java |

### 8.3 Recommendations

1. **Inject `PrintStream` for testability.** Refactoring `Floor.print()` and `Robot.printStatus()` to accept a `PrintStream` parameter (defaulting to `System.out`) would make console output testable and kill ~15 additional mutants.

2. **Add boundary-edge tests for SOUTH/WEST.** The surviving mutants on lines 57–58 (`newY -= steps` → `+`) can be killed by adding tests where the robot is near position (0,0) and a southward/westward move would go out of bounds under the mutant.

3. **Remove unreachable `default` cases.** The `default: return this;` in `Direction.turnRight()`/`turnLeft()` is unreachable with a finite enum. Removing it eliminates dead code.

4. **Consider integration tests.** The `replayHistory()` method involves complex state (save → reinitialize → replay). Integration-level tests that verify the full state after replay would strengthen confidence.

---

*Report generated for COEN 448 — Software Testing and Validation, Winter 2026, Concordia University.*
