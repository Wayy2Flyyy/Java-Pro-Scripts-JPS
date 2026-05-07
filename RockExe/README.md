# Rock.exe

Version: **1.1.0**

**Rock.exe** is a very serious pet rock simulator built with Java Swing. Use the action buttons to alter the rock's happiness, cleanliness, energy, boredom, and respect.

## Features

- Clean Java Swing desktop window
- Eight working rock actions
- Stats clamped to safe ranges
- Saved rock stats
- Mood/title updates based on rock state
- Safe shake animation when throwing the rock
- Emoji fallback text for systems with limited emoji font support
- Simple hover effects
- No external libraries

## Actions

- Feed Rock
- Wash Rock
- Talk To Rock
- Insult Rock
- Let Rock Sleep
- Praise Rock
- Throw Rock
- Stare At Rock

## Files

```text
RockExe.java      Main game window, actions, stat logic, mood logic, and animation
SaveManager.java  Small helper for saving/loading rock stats
README.md         This file
```

## How To Run

From this folder:

```bash
javac *.java
java RockExe
```

You can also compile only the listed files explicitly:

```bash
javac RockExe.java SaveManager.java
java RockExe
```

## Save Data

Rock stats are saved to:

```text
<your user home>/.jps-games/rock-exe.properties
```

If the save file cannot be read or written, the game still runs and prints a warning in the terminal.
