# Button.exe

Version: **1.1.0**

**Button.exe** is a tiny chaotic Java Swing game about one suspicious button. Every click awards points, counts clicks, moves the button, changes the background, changes the button text, and displays a random message.

## Features

- Clean Java Swing desktop window
- Score counter and click counter
- Saved high score
- Button movement clamped inside the play area
- Random messages and random button text
- Dark readable random backgrounds
- Simple hover effect
- No external libraries

## Files

```text
ButtonExe.java    Main game window and gameplay logic
SaveManager.java  Small helper for saving/loading the high score
README.md         This file
```

## How To Run

From this folder:

```bash
javac *.java
java ButtonExe
```

You can also compile only the listed files explicitly:

```bash
javac ButtonExe.java SaveManager.java
java ButtonExe
```

## Save Data

The high score is saved to:

```text
<your user home>/.jps-games/button-exe.properties
```

If the save file cannot be read or written, the game still runs and prints a warning in the terminal.
