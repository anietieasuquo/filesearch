# File Search Application

> By Anietie Asuquo

> This is a simple command line driven text search engine. It reads all the text files in the given directory, builds an in-memory representation of the files
> and their contents, and then provides a command prompt at which interactive searches can be performed.
>
> The search takes the words entered in the prompt and returns the top 10 matching filenames in rank order, giving the rank score against each match.

# Ranking
- Ranking is done using a variant of Google's PageRank algorithm.
- The rank score will be 100% if a file contains all the words in the search term.
- It will be 0% if it contains none of the words
- It will be between 0 - 100% if it contains only some of the words.

# Please Note

* This is just a very minimal implementation deliberately designed to be simple.

* Since the rank is very important and is not displayed to the user, I decided not to scale the `BigDecimal` value because results with very close ranks could be differentiated by fractions.

* Tests are made with JUnit 5 and Mockito

* Maven is used for dependency management and build.

> Prerequisite to run

- Java 8 and above, Maven

# Steps to run the application

- Unpack the zip archive into `./filesearch`

- `cd filesearch` (cd into project)

- `mvn clean install` (Clean Install)

- `mvn exec:java -Dexec.args="/directory-to-index"` (Run the application)

- `mvn exec:java -Dexec.args="/directory-to-index -s"` (Run the application with case-sensitivity turned-on)

- search> `:quit` (Quit the application)

- search> `:list` (List all available commands)

---
