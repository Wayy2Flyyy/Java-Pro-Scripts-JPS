# Java Pro Scripts - Beginner Swing Games

Version: **1.2.0**

This repository contains two small, funny Java Swing desktop games. They are intentionally lightweight and beginner-friendly: no Maven, no Gradle, no external libraries, and no online features.

## Games

### Button.exe

A chaotic one-button game. Press the button to earn points, increase the click counter, move the button, change the background, and receive suspiciously dramatic messages.

Location: `ButtonExe/`

### Rock.exe

A deeply serious pet rock simulator. Feed, wash, praise, insult, throw, stare at, talk to, or let your rock sleep. Actions update happiness, cleanliness, energy, boredom, and respect.

Location: `RockExe/`

## Requirements

- Java Development Kit (JDK) 8 or newer
- A desktop environment capable of showing Java Swing windows

None.

```text
ButtonExe/
├── ButtonExe.java
├── SaveManager.java
└── README.md

RockExe/
├── RockExe.java
├── SaveManager.java
└── README.md

README.md
```

## Quick Run

Open a terminal in one of the game folders, compile all Java files, then run the main class.

```bash
cd ButtonExe
javac *.java
java ButtonExe
```

```bash
cd RockExe
javac *.java
java RockExe
```

## Save Files

Both games save tiny `.properties` files under your user home directory in a `.jps-games` folder:

- Button.exe saves the high score.
- Rock.exe saves the rock's current stats.

If saving fails because of permissions, the games continue running and print a warning to the terminal.
