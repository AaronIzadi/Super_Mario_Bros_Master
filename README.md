# Super Mario Bros.

## About this game:
Here is the [link](https://en.wikipedia.org/wiki/Super_Mario_Bros.) to the wikipedia webpage about this game.


## Models:
There are seven main models for this game:
1. Hero:
There are too many characters designed for this game and I used only five of them in this code (each has its own characterized power); Mario, Luigi, PrincessPeach, Rosalina and Toad.

2. Enemy:
There's also five enemy characters coded in this game. Bowser as the main enemy and there are also KoopaTroopa, Goomba, Piranha and Spiny.

3. Obstacle:
This model contains of different kinds of Brick, Pipe, Slime, etc.

4. Prize:
There is a kind of brick named SurpriseBrick that contains prizes for the hero to power up! Containing Coin, HeartMushroom, SuperMushroom, SuperStar and FireFlower.

5. Weapon:
Which contains Fire and Axe.

6. Map:
It contain all the models above as its own fields!
Also remember because of the existence of crossovers, at each level there should exist two maps at the same time.

7. GameObject:
Actually this is the most important one that all the above (except for map) extends this abstract class!
For example take a look at this class's fields:
```
    private double x, y;
    private double velX, velY;
    private Dimension dimension;
    private BufferedImage style;
    private boolean toRight;
    private double gravityAcc;
    private boolean falling, jumping;
```
Pretty interesting huh!


## Inputs:
The main classes recieving inputs and reading files are ImageLoader, SoundManager, FontLoader and InputManager (Key Listener).


## States:
As its name shows, this classes are the different states of the game. i.e StartScreen, Store, About, Help, LoadGame, Pause and CheckPoint.


## Graphics and Logic:
There were some challenges in this part such as building the Camera, Animation, UIManager and MapManager.
I used all of the original game's resources including media, audio and even the font!
It takes time but it is not impossible to find out how they work:)


## Repository:
As you can see there is a part where you can save or load previous games.
This part was coded using JSON, saving the data in .txt files located in src folder.
The part of the code that helps this proccess is the UserData class which is singleton that keeps data about user and current map.


## Game Engine:
This is the main class handling all the things that are happening in this project!
Take a carefull look at this class.
Here's the class's fields to see how imprtant this is:
```
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
I won't explain more about it to make sure you'll check it out yourself.


### Thank you for your time and enjoy the game:)
