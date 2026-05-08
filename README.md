# Java Mini Games

A small collection of Java Swing games, tools, and learning templates built with plain Java.

This project is designed for learning Java through simple, fun, expandable projects.

## Current Files

- `RockExe.java`
- `ButtonExe.java`
- `ReflexExe.java`
- `GameTemplate.java`
- `FunctionsExe.java`
- `CalculatorExe.java`

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

A configurable Java game template made for learning and creating your own small 2D games.

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

### Run

```bash
java GameTemplate
```

---

## Functions.exe

A Java utility toolkit file filled with reusable functions for games, apps, and learning projects.

It also includes a small demo window so you can test the functions.

### Function Categories

- Maths
- Random utilities
- Text helpers
- Time helpers
- File saving/loading
- 2D game helpers
- Stats helpers
- Colour helpers
- UI helpers
- Debug helpers

### Example Usage

```java
int health = FunctionsExe.Fn.Maths.clamp(150, 0, 100);
String title = FunctionsExe.Fn.Text.titleCase("rock fella");
boolean lucky = FunctionsExe.Fn.Randoms.chancePercent(25);
```

### Run

```bash
java FunctionsExe
```

---

## Calculator.exe

A modern Java Swing scientific calculator with a dark glass-style interface.

### Features

- Modern glass-style UI
- Basic calculator functions
- Scientific functions
- Brackets
- Powers
- Square root
- Reciprocal
- Percentage
- Positive/negative toggle
- Constants `π` and `e`
- `Ans` memory value
- EXP scientific notation input
- Keyboard support
- Custom expression parser
- No external libraries

### Supported Functions

```text
sin()
cos()
tan()
log()
ln()
sqrt()
abs()
```

### Supported Operators

```text
+
-
*
/
%
^
```

### Run

```bash
java CalculatorExe
```

---

## Packages Used

Most files use built-in Java packages such as:

```java
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.ArrayList;
```

Some files may also use:

```java
import javax.swing.border.EmptyBorder;
import java.text.DecimalFormat;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.List;
```

All packages are built into Java.

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
├── FunctionsExe.java
├── CalculatorExe.java
└── README.md
```

---

## Compile Everything

```bash
javac *.java
```

---

## Run Each Project

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

```bash
java FunctionsExe
```

```bash
java CalculatorExe
```

---

## Purpose

This project is made to practise Java fundamentals through small game, app, and utility projects.

It covers:

- Java Swing windows
- Drawing with `Graphics2D`
- Buttons and labels
- Keyboard input
- Mouse input
- Timers and game loops
- Random events
- Collision detection
- Score systems
- Health systems
- Config-based design
- Utility functions
- File saving/loading basics
- Scientific calculator logic
- Expression parsing
- Beginner-friendly Java structure

---

## Version

```text
v0.4.0
```

---

## Author

Daniel
