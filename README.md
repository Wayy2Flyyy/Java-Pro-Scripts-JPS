# Java Mini Games

A small collection of simple Java Swing games built with plain Java.

This project is designed for learning Java through small, fun, expandable games.

## Current Games

- **Rock.exe**
- **Button.exe**
- **Reflex.exe**
- **GameTemplate**

No external libraries are required.

---

## Rock.exe

A funny pet rock simulator where you interact with a rock using different actions.

### Features

- Rock stats
- Action buttons
- Random messages
- Mood changes
- Basic shake animation

### Run

```bash
java RockExe
```

---

## Button.exe

A chaotic one-button game where every click causes random effects.

### Features

- Score counter
- Click counter
- Random button movement
- Random messages
- Random button text
- Random background changes

### Run

```bash
java ButtonExe
```

---

## Reflex.exe

A reaction-time minigame that tests how quickly keyboard and mouse inputs register inside the game.

### Features

- Keyboard test using `SPACE`
- Mouse test using left click
- Random wait delay
- False-start detection
- Reaction time in milliseconds
- Best and average results
- Reset stats button

### Accuracy Note

Reflex.exe uses:

```java
System.nanoTime()
```

This measures the time between the game showing `GO!` and Java receiving the input event.

It does not measure perfect physical hardware latency.

### Run

```bash
java ReflexExe
```

---

## GameTemplate

A configurable Java game template made for learning and creating your own small games.

Most of the game can be changed from the config section at the top of the file:

```java
static final class CFG {
```

### Features

- Config-based setup
- Player movement
- Mouse movement modes
- Enemy AI
- Collectibles
- Score system
- Health system
- Level system
- Win condition
- Game over system
- Pause system
- Debug mode
- Custom colours
- Custom controls
- Easy beginner-friendly structure

### Run

```bash
java GameTemplate
```

---

## Packages Used

Most games use built-in Java packages such as:

```java
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.ArrayList;
```

Some files may only use part of these packages.

---

## Dependencies

None.

You do not need:

- Maven
- Gradle
- JavaFX
- External JAR files
- Databases
- Online APIs
- Third-party libraries

Everything runs with plain Java.

---

## Requirements

Java 17 or newer is recommended.

Check Java:

```bash
java -version
javac -version
```

---

## File Structure

```text
JavaMiniGames/
├── RockExe.java
├── ButtonExe.java
├── ReflexExe.java
├── GameTemplate.java
└── README.md
```

---

## Compile All Games

```bash
javac *.java
```

---

## Run Each Game

```bash
java RockExe
```

```bash
java ButtonExe
```

```bash
java ReflexExe
```

```bash
java GameTemplate
```

---

## Purpose

This project is made to practise Java fundamentals through simple game development.

It covers:

- Java Swing windows
- Drawing with `Graphics2D`
- Keyboard input
- Mouse input
- Timers and game loops
- Collision detection
- Score systems
- Health systems
- Random events
- Basic UI design
- Beginner-friendly game structure

---

## Version

```text
v0.2.0
```

---

## Author

Daniel
