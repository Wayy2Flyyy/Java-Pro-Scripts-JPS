# Java Mini Games

A small collection of simple Java Swing minigames built with plain Java.

Current games:

- **Rock.exe**
- **Button.exe**
- **Reflex.exe**

No external libraries are required.

---

## Games

### Rock.exe

A funny pet rock simulator where you interact with a rock using different actions.

Features:

- Rock stats
- Action buttons
- Random messages
- Mood changes
- Basic shake animation

---

### Button.exe

A chaotic one-button game where every click causes random effects.

Features:

- Score counter
- Click counter
- Random button movement
- Random messages
- Random button text
- Random background changes

---

### Reflex.exe

A reaction-time minigame that tests how quickly keyboard and mouse inputs register inside the game.

Features:

- Keyboard test using `SPACE`
- Mouse test using left click
- Random wait delay
- False-start detection
- Reaction time in milliseconds
- Best and average results
- Reset stats button

---

## Packages Used

```java
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
```

Some files may also use:

```java
import java.awt.event.ActionEvent;
```

---

## Dependencies

None.

No Maven, Gradle, JavaFX, external JARs, databases, or online APIs are required.

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
└── README.md
```

---

## How To Compile

Compile all games:

```bash
javac *.java
```

---

## How To Run

Run Rock.exe:

```bash
java RockExe
```

Run Button.exe:

```bash
java ButtonExe
```

Run Reflex.exe:

```bash
java ReflexExe
```

---

## Notes

Reflex.exe uses:

```java
System.nanoTime()
```

This gives accurate timing inside Java, then converts the result into milliseconds.

It does not measure perfect physical hardware latency. It measures the time between the game showing `GO!` and Java receiving the keyboard or mouse input.

---

## Version

```text
v0.1.0
```

---

## Author

Daniel
