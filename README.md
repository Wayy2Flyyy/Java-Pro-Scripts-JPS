# Reflex.exe

A simple Java Swing reaction-time minigame that tests how quickly keyboard and mouse inputs register inside the game.

The game measures the time between the **GO!** prompt appearing and Java receiving the correct input event.

## What It Tests

Reflex.exe includes two test modes:

- **Keyboard Test** — press `SPACE` when the screen says `GO!`
- **Mouse Test** — left-click when the screen says `GO!`

The result is shown in milliseconds.

## Important Accuracy Note

This does **not** measure perfect physical hardware latency.

It measures how fast the Java game receives your input after the visual prompt appears. Timing uses:

```java
System.nanoTime()
```

The result is converted into milliseconds for display.

## Features

- Keyboard reaction test
- Mouse click reaction test
- Random wait delay before `GO!`
- False-start detection
- Millisecond result display
- Best keyboard time
- Best mouse time
- Average keyboard time
- Average mouse time
- Attempt counter
- Reset stats button
- Simple dark Java Swing UI

## Packages Used

```java
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
```

## Dependencies

None.

No Maven, Gradle, JavaFX, external JARs, databases, or online APIs are required.

## Requirements

Java 17 or newer is recommended.

Check Java:

```bash
java -version
javac -version
```

## File Structure

```text
ReflexExe/
├── ReflexExe.java
└── README.md
```

## How To Run

Compile:

```bash
javac ReflexExe.java
```

Run:

```bash
java ReflexExe
```

## Controls

```text
Keyboard Test: SPACE
Mouse Test: Left Mouse Click
```

## Result Ratings

The game gives a basic reaction rating after each attempt:

```text
Under 120 ms: Extremely fast
Under 180 ms: Very fast
Under 250 ms: Solid
Under 350 ms: Average
350 ms or higher: Slow
```

## Current Version

```text
v0.1.0
```

## Future Ideas

- 10-round test mode
- Keyboard vs mouse comparison summary
- Save best times locally
- Reaction history graph
- Sound cue mode
- Visual cue mode
- Random colour cue mode
- Accuracy leaderboard
- Export results to text file

## Author

Daniel
