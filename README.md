# 🍄 Super Mario Bros Master

A **Java Swing implementation of the classic Super Mario Bros. experience** 🎮 featuring multiple playable heroes, enemies, power-ups, boss fights, save/load functionality, and crossover levels.

> 🏰 Jump into the Mushroom Kingdom, collect power-ups, defeat enemies, and take on Bowser! 🔥

For background on the original game, see [Super Mario Bros. on Wikipedia](https://en.wikipedia.org/wiki/Super_Mario_Bros.?utm_source=chatgpt.com).

---

## ✨ Features

### 🧑‍🤝‍🧑 Playable Characters

Choose your hero and take on the adventure:

* 🍄 **Mario**
* 🟢 **Luigi**
* 👑 **Princess Peach**
* 🌌 **Rosalina**
* 🍄 **Toad** 

### 👾 Enemies & Bosses

Watch your step! ⚠️

* 👣 Goomba
* 🐢 Koopa Troopa
* 🌱 Piranha Plant
* 🦔 Spiny
* 🐲 **Bowser** - Final boss

### ⭐ Power-Ups & Collectibles

Power up, get stronger, and survive longer! 💪

* 🪙 Coins
* 🍄 Super Mushroom
* 🌸 Fire Flower
* ⭐ Super Star
* ❤️ Heart Mushroom

### 🎮 Game Systems

* 🌍 Multiple worlds
* 👑 Boss fights
* 🚩 Checkpoints
* 🚇 Crossover tunnel levels
* 💾 JSON save/load system
* 🗂️ Three save slots
* ⏱️ Frame-based game loop (~60 Hz)
* ⏳ Tick-based timers
* 📷 Dual-camera scrolling for main & crossover maps

---

## 🏗️ Project Structure

```text
src/SuperMario/
├── config/          ⚙️ GameConstants
│                     └─ Physics, timings, window configuration
│
├── model/           🧩 Data-only entities
│                     └─ Hero, Enemy, Map, prizes, …
│
├── logic/           🧠 Game rules, physics, collisions & timers
│   ├── collision/   💥 Collision handlers & coordinator
│   ├── enemy/      👾 Enemy & Bowser behavior
│   ├── hero/       🦸 Hero movement, forms & hammer
│   ├── map/        🗺️ World updates & drawing orchestration
│   ├── prize/      🎁 PrizeHandler
│   ├── timer/      ⏱️ GameTimer & EntityTimerLogic
│   └── render/     🎨 EntityRenderer
│
├── graphic/         🖥️ Swing UI, input, maps & animations
├── input/           🔊 ImageLoader, SoundLoader & fonts
└── repository/      💾 JSON save/load persistence

src/resources/media/ 🎨 Sprites, maps & audio
src/data/            💾 Save game files
src/library/         📦 Legacy json-simple JAR
```

### 🧱 Layering

The project follows a clean separation of responsibilities:

**🧩 Models** → hold state & bounds
**🧠 Logic** → handles gameplay & rules
**🎨 Graphic** → handles rendering & menus

This keeps the game logic independent from the presentation layer and makes the codebase easier to maintain and extend. 🚀

---

## 📋 Prerequisites

Before running the game, make sure you have:

* ☕ **JDK 17+**

    * Maven `pom.xml` targets Java 17
* 🐘 **Maven 3.6+** — optional but recommended
* 💡 **IntelliJ IDEA** — optional, with the included `Super_Mario_master.iml`

### ⚠️ Assets

Game assets must be available under:

```text
src/resources/media/
```

🎯 **Run the game from the project root** so that resource paths resolve correctly.

---

## ▶️ Build & Run

### 🐘 Maven — Recommended

From the project root:

```bash
mvn compile
mvn exec:java
```

The exec plugin automatically uses the project root as the working directory.

---

### 💻 Manual Compilation — Without Maven

#### 🪟 PowerShell

```powershell
$sources = Get-ChildItem -Path src -Recurse -Filter *.java | ForEach-Object { $_.FullName }
javac -encoding UTF-8 -d out -cp "src\library\json-simple-1.1.1.jar" $sources
java -cp "out;src\library\json-simple-1.1.1.jar" SuperMario.logic.Launcher
```

#### 🐧 macOS / Linux

```bash
find src -name '*.java' > sources.txt
javac -encoding UTF-8 -d out -cp "src/library/json-simple-1.1.1.jar" @sources.txt
java -cp "out:src/library/json-simple-1.1.1.jar" SuperMario.logic.Launcher
```

---

### 💡 IntelliJ IDEA

1. 📂 Open the project/module `Super_Mario_master`.
2. ⚙️ Set the run configuration's working directory to the **project root**.
3. ▶️ Run:

```text
SuperMario.logic.Launcher
```

### 🚀 Main Class

```text
SuperMario.logic.Launcher
```

---

## 🎮 Controls

| 🎮 Key             | ⚡ Action                                  |
| ------------------ | ----------------------------------------- |
| ⬅️ Left / ➡️ Right | Move                                      |
| ⬆️ Up              | Jump / Menu up                            |
| ⬇️ Down            | Crouch / Menu down                        |
| ⬆️ + ⬇️ Hold ~2s   | 🔨 Activate hammer when super & enough coins |
| ␣ Space            | 🔥 Fireball / 🔨 Throw hammer when held      |
| ↵ Enter            | ✅ Confirm menu selection                  |
| Esc                | ⏸️ Pause / Return to start screen         |

> 💡 **Tip:** Some abilities depend on the hero's current form and available coins.

---

## 🧠 Architecture

The game is organized around several core managers and services:

### 🎮 GameEngine

The main **entry singleton** that wires together managers and starts the game loop.

### ⏱️ GameLoopManager

Responsible for:

* Fixed-tick updates
* Map timers
* Game-over checks

### 🗺️ MapManager

Handles:

* Level lifecycle
* Collision entry point
* Score
* Time management

### 💥 CollisionCoordinator

Coordinates collisions by delegating them to specialized collision handlers.

### 🎁 PrizeHandler

Centralizes prize-related gameplay behavior:

* 👆 Touch detection
* 🎁 Prize revealing
* 🔄 Updates
* 🎨 Drawing

### 🖼️ ImageLoader

A shared asset-loading singleton used throughout the game.

Other services are owned by `GameEngine` where possible, helping keep dependencies organized.

---

## 💾 Save System

The game includes a JSON-based save/load system with **three save slots**. 🗂️

Save files are stored as JSON text files under:

```text
src/data/
```

This allows players to save their progress and continue their adventure later. 🍄

---

## 📦 Dependencies

| 📦 Dependency                                                                         | 🎯 Purpose        |
| ------------------------------------------------------------------------------------- | ----------------- |
| [json-simple 1.1.1](https://github.com/fangyidong/json-simple?utm_source=chatgpt.com) | 💾 JSON save/load |

Maven resolves the dependency from **Maven Central**.

For manual compilation, the bundled JAR is used:

```text
src/library/json-simple-1.1.1.jar
```

---

## 🎓 License & Assets

This is an **educational project** 📚.

> ⚠️ **Super Mario Bros. assets, characters, names, and trademarks belong to Nintendo.**

This project is not affiliated with or endorsed by Nintendo.

---

## 👨‍💻 Author

**Aaron Izadi**

🐙 [GitHub](https://github.com/AaronIzadi?utm_source=chatgpt.com)

---

## ⭐ Project Highlights

**☕ Java 17** · **🎨 Swing** · **🎮 Game Development** · **🏗️ OOP** · **🧠 Game Logic** · **💥 Collision Detection** · **⏱️ Game Loop** · **💾 JSON Persistence** · **🧩 Modular Architecture**

> 🍄 **It's-a-me, Java!**
> A full-featured Super Mario-inspired game built from scratch with Java Swing. 🎮🔥
