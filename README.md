# 🍄 Super Mario Bros Master

A comprehensive Java implementation of the classic Super Mario Bros platform game, featuring multiple playable characters, enemies, power-ups, and a complete game management system.

## 🎮 Overview

Super Mario Bros Master is a faithful recreation of the beloved classic platformer where players guide their chosen hero through the Mushroom Kingdom, collecting coins, defeating enemies, and saving the day. The game features an advanced object-oriented architecture with save/load functionality, multiple game states, and authentic retro assets.

For more about the original game, visit the [Super Mario Bros Wikipedia page](https://en.wikipedia.org/wiki/Super_Mario_Bros.).

## ✨ Features

### 🦸 Playable Characters
Choose from **five unique heroes**, each with their own special abilities:
- **Mario** - The classic hero
- **Luigi** - Mario's brother
- **Princess Peach** - The princess herself
- **Rosalina** - Celestial guardian
- **Toad** - Loyal Mushroom Kingdom resident

### 👾 Enemies
Battle against five challenging enemy types:
- **Bowser** - The main antagonist
- **Koopa Troopa** - Shell-dwelling turtle soldiers
- **Goomba** - Walking mushroom minions
- **Piranha Plant** - Pipe-dwelling predators
- **Spiny** - Spiked enemies

### 🎁 Power-ups & Collectibles
Discover surprise bricks containing:
- **Coins** - Collect for points
- **Heart Mushroom** - Restore health
- **Super Mushroom** - Grow in size and power
- **Super Star** - Temporary invincibility
- **Fire Flower** - Shoot fireballs

### 🧱 Obstacles
Navigate through various environmental challenges:
- Bricks
- Pipes
- Slime
- And more!

### 🔫 Weapons
Use offensive items to defeat enemies:
- **Fire** - Projectile attacks
- **Axe** - Melee weapon

## 🏗️ Architecture

### Core Models

The game is built on **seven main model classes**:

1. **Hero** - Base class for playable characters
2. **Enemy** - Base class for hostile entities
3. **Obstacle** - Environmental hazards and platforms
4. **Prize** - Power-ups and collectibles
5. **Weapon** - Offensive items
6. **Map** - Game world container (supports dual maps for crossovers!)
7. **GameObject** (Abstract) - Base class for most game entities

#### GameObject Structure
```java
private double x, y;
private double velX, velY;
private Dimension dimension;
private BufferedImage style;
private boolean toRight;
private double gravityAcc;
private boolean falling, jumping;
```

### Input & Resource Management
- **ImageLoader** - Handles all sprite and image loading
- **SoundManager** - Manages audio and music
- **FontLoader** - Loads custom retro fonts
- **InputManager** - Keyboard input handling

### Game States
The game includes multiple screen states:
- **StartScreen** - Main menu
- **Store** - Character selection
- **About** - Game information
- **Help** - Instructions
- **LoadGame** - Save file management
- **Pause** - In-game pause menu
- **CheckPoint** - Level completion

### GameEngine

The heart of the project that orchestrates all managers:

```java
private GameLoopManager loopManager;
private InputManager inputManager;
private UIManager uiManager;
private MapManager mapManager;
private UserData userData;
private CameraManager cameraManager;
private SoundManager soundManager;
private GameStateManager stateManager;
private ImageLoader imageLoader;
```

Key selections managed:
- `StartScreenSelection`
- `LoadGameScreenSelection`
- `PauseScreenSelection`
- `StoreScreenSelection`
- `CheckPointSelection`
- `MapSelection`

## 🎨 Assets

All original Super Mario Bros assets are included:
- **Sprites** - Authentic pixel art graphics
- **Audio** - Classic sound effects and music
- **Fonts** - Retro-style text rendering

## 💾 Save System

Game progress is saved and can be loaded at any time:
- Save files stored as JSON (`.txt` format in `src` folder)
- Managed by **UserData** singleton class
- Tracks user information and current map state
- Resume from checkpoints

## 🔧 Technical Highlights

### Advanced Systems
- **Dual Camera System** - Handles main camera and crossover camera for multi-map levels
- **Animation Engine** - Smooth sprite animations for all entities
- **UIManager** - Comprehensive user interface system
- **MapManager** - Dynamic level loading and management
- **Physics Engine** - Gravity, velocity, jumping, and collision detection

### Design Patterns Used
- **Singleton Pattern** - UserData management
- **Abstract Factory Pattern** - GameObject hierarchy
- **Observer Pattern** - Game state management
- **Component Pattern** - Modular game entity design

## 🚀 Getting Started

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- IDE (IntelliJ IDEA, Eclipse, or VS Code recommended)

### Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/AaronIzadi/Super_Mario_Bros_Master.git
   ```

2. Open the project in your preferred IDE

3. Run the Launch (main) class to start the game

### Controls
- **Arrow Keys** - Move left/right
- **Space/Up Arrow** - Jump
- **Shift** - Sprint
- **F** - Fire (when powered up)
- **ESC** - Pause menu

## 🎯 Development Challenges

The project tackled several complex systems:
- **Camera Management** - Implementing smooth scrolling with dual camera support for crossover levels
- **Animation System** - Frame-perfect sprite animations
- **UI/Map Integration** - Seamless coordination between interface and game world
- **Save/Load Architecture** - Reliable JSON-based persistence

## 🤝 Contributing

Contributions, issues, and feature requests are welcome! Feel free to check the issues page or submit a pull request.

## 📝 License

This project is for educational purposes. All Super Mario Bros assets and intellectual property belong to Nintendo.

## 👨‍💻 Author

**Aaron Izadi**
- GitHub: [@AaronIzadi](https://github.com/AaronIzadi)

## 🌟 Acknowledgments

- Nintendo for the original Super Mario Bros game
- The open-source community for inspiration and resources
- All contributors and players

---

⭐ **Enjoy the game and happy coding!** 🍄
