# GameTemplate

A configurable Java Swing game template designed for learning Java and building simple 2D games quickly.

The goal of this template is to let you create your own game by changing the config section at the top of the file instead of rewriting the full engine.

---

## Main File

```text
GameTemplate.java
```

---

## What This Template Includes

- Java Swing window
- Game loop
- Player movement
- Mouse movement options
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
- Easy config section

---

## Config System

Most of the game is controlled from this section:

```java
static final class CFG {
```

Inside the config, you can change:

- Window title
- Window size
- FPS
- Game name
- Win score
- Background colour
- Grid settings
- Player size
- Player speed
- Player health
- Enemy count
- Enemy speed
- Enemy damage
- Collectible count
- Collectible score value
- Level scaling
- Mouse mode
- Controls
- HUD colours
- Fonts

---

## Mouse Modes

The template supports three mouse modes:

```java
TELEPORT
MOVE_TO_CLICK
DISABLED
```

### TELEPORT

Left-click instantly moves the player to the clicked location.

### MOVE_TO_CLICK

Left-click sets a target, and the player walks toward it.

### DISABLED

Mouse movement is turned off.

Change it here:

```java
static final MouseMode MOUSE_MODE = MouseMode.MOVE_TO_CLICK;
```

---

## Controls

Default controls:

```text
WASD = Move
Mouse Left Click = Move or teleport depending on config
R = Restart
P = Pause
F3 = Debug mode
```

Controls can be changed inside:

```java
static final class CONTROLS {
```

Example:

```java
static final int UP = KeyEvent.VK_W;
static final int DOWN = KeyEvent.VK_S;
static final int LEFT = KeyEvent.VK_A;
static final int RIGHT = KeyEvent.VK_D;
```

---

## How To Run

Compile:

```bash
javac GameTemplate.java
```

Run:

```bash
java GameTemplate
```

---

## Requirements

Java 17 or newer is recommended.

Check Java:

```bash
java -version
javac -version
```

---

## Dependencies

None.

This template uses plain Java only.

No Maven, Gradle, JavaFX, external JARs, databases, or online APIs are required.

---

## Packages Used

```java
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
```

---

## Easy Things To Change

### Game Name

```java
static final String GAME_NAME = "Config Quest";
```

### Window Size

```java
static final int WIDTH = 960;
static final int HEIGHT = 640;
```

### Player Speed

```java
static final double SPEED = 4.8;
```

### Enemy Count

```java
static final int COUNT = 3;
```

### Win Score

```java
static final int WIN_SCORE = 300;
```

### Collectible Score Value

```java
static final int SCORE_VALUE = 10;
```

---

## Learning Purpose

This template is made to teach:

- Java classes
- Java constants
- Game loops
- Keyboard input
- Mouse input
- Collision detection
- Basic AI movement
- Drawing with `Graphics2D`
- Config-based game design
- Simple Java game architecture

---

## Recommended Next Upgrades

- Add sound effects
- Add sprite images
- Add save/load
- Add a menu screen
- Add power-ups
- Add more enemy types
- Add multiple levels
- Add an inventory
- Add achievements

---

## Version

```text
v0.1.0
```

---

## Author

Daniel
