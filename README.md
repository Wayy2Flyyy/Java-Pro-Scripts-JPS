# Java Mini Games

A collection of Java Swing games, tools, templates, and small apps built with plain Java.

This project is designed for learning Java through simple, fun, expandable projects.

## Current Files

- `RockExe.java`
- `ButtonExe.java`
- `ReflexExe.java`
- `GameTemplate.java`
- `FunctionsExe.java`
- `CalculatorExe.java`
- `ManPacExe.java`
- `RudeChatBotExe.java`

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

A Java utility toolkit filled with reusable functions for games, apps, and learning projects.

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

## ManPac.exe

An original Java maze-chase minigame inspired by classic arcade movement and pellet collection.

This is not an official Pac-Man clone and uses no copyrighted assets.

### Features

- Maze movement
- Pellet collection
- Power orbs
- Enemy hunters
- Hunter chase/scatter behaviour
- Power mode
- Eaten hunter return state
- Combo scoring
- Bonus fruit
- Lives system
- Score and high score
- Level progression
- Pause system
- Restart system
- WASD and arrow key movement
- Modern dark arcade-style UI

### Controls

```text
WASD / Arrow Keys = Move
P = Pause
R = Restart
```

### Run

```bash
java ManPacExe
```

---

## RudeChat Bot.exe

A local Java Swing chatbot simulator that detects user message intent and generates random rude-style replies.

This is a local template only. It does not connect to Discord, websites, APIs, or any live platform.

### Features

- Incoming message analyser
- Intent detection
- Random reply generation
- Conversation memory
- Follow-up handling
- Mixed message detection
- Aggressive nonsense greeting detection
- Rude fallback replies
- Copy reply button
- Copy analysis button
- Simulated user messages
- Clear/reset conversation button

### Detects

```text
Greetings
Random openers
Swearing / frustration
Bug reports
Support requests
Application / join requests
Payment / price questions
Discord / link questions
Event / schedule questions
Thanks / appreciation
Slang / casual messages
Common general words
Unknown messages
Mixed/confused messages
Follow-up messages
```

### Example Messages

```text
hi
quick question
fuck mate hi
this menu is broken wtf
how do i apply?
discord invite expired
refund this shit
banana microwave purple chair
```

### Run

```bash
java RudeChatBotExe
```

---

## Packages Used

Most files use built-in Java packages such as:

```java
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.datatransfer.StringSelection;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
```

Some files may also use:

```java
import java.text.DecimalFormat;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
├── ManPacExe.java
├── RudeChatBotExe.java
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

```bash
java ManPacExe
```

```bash
java RudeChatBotExe
```

---

## Purpose

This project is made to practise Java fundamentals through small game, app, chatbot, and utility projects.

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
- Maze movement
- Enemy AI
- Chatbot intent detection
- Random reply systems
- Conversation memory
- Beginner-friendly Java structure

---

## Version

```text
v0.5.0
```

---

## Author

Daniel
