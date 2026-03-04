# Robot Motion Simulator

This project implements a **Robot Motion Simulator** developed in Java for the course **COEN 448 – Software Testing and Validation (Winter 2026)** at Concordia University.

The simulator models a robot moving on an **N × N grid floor**. The robot can move across the grid, change direction, draw paths using a pen, display its status, print the grid, and replay previously executed commands.

---

## Technologies Used

- Java  
- Maven  
- JUnit 5  
- JaCoCo (Code Coverage)  
- Git / GitHub  

---

## Supported Commands

| Command | Description |
|--------|-------------|
| U / u | Lift the pen up so the robot moves without drawing |
| D / d | Put the pen down so the robot draws while moving |
| R / r | Turn the robot right |
| L / l | Turn the robot left |
| M s | Move the robot forward by *s* spaces |
| P / p | Print the current floor grid |
| C / c | Display the robot's current position and status |
| I n | Initialize a new *n × n* floor grid |
| H / h | Replay the command history |
| Q / q | Quit the program |

---

## Running the Tests

The project uses **Maven** to build and execute automated tests.

To run the test suite, execute the following command in the project directory:

```bash
mvn test
```

This will execute all **JUnit unit tests** included in the project.

---

## Testing

The system was validated using automated unit testing to verify the correctness of robot movements, command execution, floor initialization, and other core behaviors of the simulator.

A total of **42 test cases** are executed to ensure the system behaves as expected.

---

## Code Coverage

Code coverage is measured using the **JaCoCo Maven Plugin**. Coverage reports are automatically generated when running the test suite.

To generate the coverage report, run:

```bash
mvn clean test
```

After execution, the HTML coverage report can be found at:

target/site/jacoco/index.html

The project enforces minimum coverage thresholds to ensure sufficient testing of the system behavior.

---

## Repository Note

During development, issues were encountered with the original GitHub repository related to commit synchronization and repository history tracking. The commit history became difficult to interpret due to inconsistencies between local and remote commits.

To resolve this and maintain a clearer project structure, the repository was recreated with a clean setup. As a result, this repository mainly reflects the finalized version of the project code and documentation rather than the complete development history from earlier stages.

This repository contains the full implementation, test cases, and documentation used for the project submission.

---

## Course

COEN 448 – Software Testing and Validation  
Concordia University

---

## Author

Faisal AlMasri