
# Super Mario Bros. 🎮🍄

## About this game:
Super Mario Bros. is a classic platform game featuring Mario and friends on a quest to save the Mushroom Kingdom.  
For more details, check out the [Wikipedia page](https://en.wikipedia.org/wiki/Super_Mario_Bros.).

---

## Models 🧩

This game includes **seven main models**, each with its own role:

1. **Hero**  
   Five playable characters with unique powers:  
   - Mario  
   - Luigi  
   - Princess Peach  
   - Rosalina  
   - Toad  

2. **Enemy**  
   Five enemy types to challenge the player:  
   - Bowser (main enemy)  
   - Koopa Troopa  
   - Goomba  
   - Piranha  
   - Spiny  

3. **Obstacle**  
   Various game obstacles such as:  
   - Brick  
   - Pipe  
   - Slime  
   - And more!  

4. **Prize**  
   Surprise bricks contain power-ups and collectibles:  
   - Coin  
   - Heart Mushroom  
   - Super Mushroom  
   - Super Star  
   - Fire Flower  

5. **Weapon**  
   Offensive items like:  
   - Fire  
   - Axe  

6. **Map**  
   The game world container, holding all the models above.  
   Note: Due to crossovers, each level contains two simultaneous maps!

7. **GameObject** (Abstract)  
   The base class for most entities (except Map).  
   Key fields include:  
   ```java
   private double x, y;
   private double velX, velY;
   private Dimension dimension;
   private BufferedImage style;
   private boolean toRight;
   private double gravityAcc;
   private boolean falling, jumping;
   ```
   Pretty cool, right? 😎

---

## Inputs 🎮⌨️

The main input and resource management classes are:  
- `ImageLoader`  
- `SoundManager`  
- `FontLoader`  
- `InputManager` (key listener)

---

## States 🕹️

Different classes represent game states such as:  
- StartScreen  
- Store  
- About  
- Help  
- LoadGame  
- Pause  
- CheckPoint

---

## Graphics and Logic 🎨⚙️

Challenges included:  
- Building the Camera and crossoverCamera  
- Animation system  
- UIManager and MapManager  

All original game assets (media, audio, font) were used!  
It's a complex system, but definitely worth exploring.

---

## Repository and Save System 💾

Game progress is saved and loaded via JSON files (stored as `.txt` in the `src` folder).  
This is managed by the `UserData` singleton class that keeps track of user info and current map state.

---

## Game Engine 🏎️🔥

The heart of the project, responsible for managing everything going on.  
Key fields include:

```java
private UserData userData;
private MapManager mapManager;
private UIManager uiManager;
private SoundManager soundManager;
private GameState gameState;
private Camera camera;
private Camera crossoverCamera;
private ImageLoader imageLoader;
private Thread thread;

private StartScreenSelection startScreenSelection = StartScreenSelection.LOAD_SCREEN;
private LoadGameScreenSelection loadGameScreenSelection = LoadGameScreenSelection.NEW_GAME;
private PauseScreenSelection pauseScreenSelection = PauseScreenSelection.GO_TO_MAIN_MENU;
private StoreScreenSelection storeScreenSelection = StoreScreenSelection.MARIO;
private CheckPointSelection checkPointSelection = CheckPointSelection.YES;
private final MapSelection mapSelection = MapSelection.WORLD_1;
```

Make sure to explore this class carefully — it's the backbone of the game!

---

### Thank you for your time and enjoy the game! 🍄✨
