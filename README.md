# Robot Motion Simulator

This project implements a **Robot Motion Simulator** developed in Java for the course **COEN 448 – Software Testing and Validation (Winter 2026)**.

The simulator allows a robot to move on an **N × N grid floor** while supporting commands for movement, direction changes, drawing paths, displaying robot status, and replaying previously executed commands.

---

## Technologies Used

- Java
- Maven
- JUnit 5
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

The project uses **Maven** to build and run the automated tests.

To execute the tests, run the following command in the project directory:

```bash
mvn test
```

This command will run all **JUnit unit tests** included in the project.

---

## Testing

The system was validated using automated unit testing to verify the correctness of robot movements, command execution, floor initialization, and other core behaviors of the simulator.

A total of **42 test cases** are executed to ensure the system behaves as expected.

---

## Repository Note

During development, issues were encountered with the original GitHub repository related to commit synchronization and repository history tracking. The commit history became difficult to interpret due to inconsistencies between local and remote commits.

To resolve this and maintain a clearer project structure, the repository was recreated with a clean setup. As a result, this repository mainly reflects the finalized version of the project code and documentation rather than the complete development history from earlier stages.

This repository contains the full implementation, test cases, and documentation used for the project submission.

---

## Author

Faisal AlMasri