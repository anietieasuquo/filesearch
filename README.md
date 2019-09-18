# Search Application

> By Anietie Asuquo

> This is just a simple console search application

# Please Note

* This is just a very minimal implementation deliberately designed to be simple.

* Since the rank is very important and is not displayed to the user, I decided not to scale the BigDecimal value because results with very close ranks could be differentiated by fractions.

* Usually I use Maven Checkstyle Plugin to enforce coding standards, but I decided to leave it out in this project as the instructions only permitted test libraries and frameworks.

* Tests are made with JUnit 5 and Mockito

* Maven is used for dependency management and building.

> Prerequisite to run

- Java 8 and above, Maven

# Steps to run the application

- Unpack the zip archive into `./filesearch`

- `cd filesearch` (cd into project)

- `mvn clean install` (Clean Install)

- `mvn exec:java -Dexec.args="/directory-to-index"` (Run the application)

- search> `:quit` (Quit the application)

- search> `:list` (List all available commands)

---
