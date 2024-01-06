import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * GameWorld - where the main game takes place
 * Instructions: 
 * Goal of the game is to protect the rainbow ores that are lined up around 
 * the player. 
 * When one of the ores runs out of hp, the game ends and the player loses. 
 * The player wins when they have finished all the waves and the boss battle. 
 * The player controls their character (Kirby) with the arrow keys, and shoots 
 * with the space bar, and they must shoot the enemies who are trying to get to the 
 * ores to destroy them. 
 * 
 * Extra: 
 * -The bomb power up gets rid of all enemies on the battlefield(except the boss)
 * -The hammer heals all the ores
 * -The red star allows you to get rid of all enemies in a lane 
 * CHEAT KEYS: 
 * -Press "7" to go to wave seven (where all the types of powerups and enemies are available)
 * -PRESS 'b' TO SKIP TO BOSS BATTLE - You have to get rid of all the enemies first to see the boss. 
 * 
 * Credits: 
 * -Kirby and the other characters, including all the enemies and the boss,
 * are the intellectual property of Nintendo and HAL Labratory. 
 * -Imported ScoreBar class from Mr. Cohen's Bug Simulator. 
 * 
 * Images: 
 * -The characters, backgrounds, and ores were drawn by Clara. 
 * -Star image was found on purepng.com: https://purepng.com/photo/10375/clipart-gold-star
 * -Fire image was taken from pngtree.com: https://pngtree.com/freepng/orange-fire-ball-flame-clip-art_5566544.html
 * -The red star was drawn, by me, on photopea
 * -Bomb image is from clipart-library.com: http://clipart-library.com/clip-art/explosion-clipart-transparent-18.htm
 * -Hammer image is from custom-cursor.com: https://custom-cursor.com/en/collection/kirby/kirby-hummer
 * 
 * Music: all from https://www.chosic.com/free-music/all/
 * -The main theme is called "Christmas Town" by TRG Banks. Artist Contact -> https://trgbanks.bandcamp.com/
 * -Boss battle theme is called "Hostiles Inbound [Epic Battle Music – Powerful Orchestral]"
 * by Miguel Johnson: Artist Contact -> https://soundcloud.com/migueljohnsonmjmusic
 * -Losing screen music is call "Simple Sadness" by Alexander Nakarada. Artist  Contact -> https://www.serpentsoundstudios.com
 * -Win music is called "Funny Bubbles" by Keys of Moon. Artist Contact -> https://soundcloud.com/keysofmoon
 * 
 * Sound Effects: all from mixkit.co 
 * This includes: 
 * -pop sound ("hard pop click")
 * -shooting sound ("short laser gun shot")
 * -exploding sound ("distant war explosions")
 * -magic hammer sound ("Magic sweep game trophy") 
 * 
 * Bugs: 
 * -If the player shoots the red star power-up, their star projectile just goes through the power-up and doesn't disappear 
 * -The wall's image state doesn't change back to "full" when the hammer is activated, but it will go back to the full image if hit by an enemy. 
 * -If you try to play again (from either of the ending screens), it might keep the red star power up.
 */
public class GameWorld extends World
{
    private int score; //total score
    private int wave; //current wave
    private int actCounter;
    private int cooldownAct; //the act when a new wave should start
    private boolean waveChange; //is true, when a wave is in a cooldown state
    private boolean hasBoss; //if the boss has spawned
    private ScoreBar bar; //bar at the top of the screen with the score and wave 
    
    //music
    private GreenfootSound mainMusic; 
    private GreenfootSound bossMusic; 
    
    //Constructor for GameWorld 
    public GameWorld()
    {    
        // Create a new world with 1200x800 cells with a cell size of 1x1 pixels.
        super(1200, 800, 1);
        bar = new ScoreBar(1200);
        //initialising variables
        score = 0; 
        wave = 1; 
        actCounter = 0;
        waveChange = false;
        hasBoss = false; 
        //music
        mainMusic = new GreenfootSound("ChristmasTown.mp3"); 
        bossMusic = new GreenfootSound("BossBattle.mp3");
        
        //game setup
        //adding the info on the score bar
        bar.update("Wave: " +wave+ "              Score: " +score);
        //placing the player near the center
        addObject(new Player(), 600, 493);
        //Adding the score bar
        addObject(bar, 600, 20); 
        buildWalls(); 
        //Plays main theme
        mainMusic.setVolume(20); //It's quiet so that other sound effects can be heard better
        mainMusic.playLoop(); 
    }
    
    public void act() {
        if (wave < 10) {
            //spawns enemies and powerups 
            spawnEnemy();
            spawnPowerUp();  
        } else { // wave 10 
            if (!hasBoss) {
                //removes all the enemies on the screen when the boss spawns
                if (getObjects(Enemy.class).size() == 0) {
                    //will spawn boss if there is no boss spawned already
                    addObject(new Boss(), 0, 0); 
                    mainMusic.stop(); //stops main theme to play the boss battle music
                    bossMusic.play(); 
                    hasBoss = true;
                }
            }
            spawnEnemy(); 
            spawnPowerUp(); 
        }
        if (waveChange) {
            if (wave != 10) {
                bar.update("Be prepared, the next wave is coming!"); //warning message between waves
            } else {
                bar.update("The final wave has arrived... THE BOSS IS COMING!"); 
            }
            if (actCounter > cooldownAct) {
                waveChange = false; //wave is not in cooldown state
            }
        } else {
            bar.update("Wave: " +wave+ "              Score: " +score); //updates wave and score
        }
        actCounter++; 
    }

    
    /**
     * This method helps the spawning of enemies, but enemies are not spawned within this method. 
     * Here, the score and wave number are taken in consideration, and the method will update the wave number based on the score. 
     * Each wave comes with a corresponding spawn rate, so the method initiates that spawn rate (variable "random")
     * Later, another spawnEnemy()  method is called, using the initiated spawn rate and cooldownAct as the parameters. 
     */
    //0 is left side spawning, 1 is right side spawning
    public void spawnEnemy() {
        int random = 120; //game starts with a 1 in 120 spawn rate for enemies 
        int cooldown = 330; //330-act cooldown period between waves
        int[] scores = {10, 30, 50, 80, 120, 150, 200, 250, 300}; //score barriers for each wave
        int[] randomSpawnRates = {120, 110, 100, 100, 90, 80, 70, 70, 70, 65}; //enemy spawn rate for each wave
        if (wave < 10 && score >= scores[wave-1]) {
            wave++; 
            random = randomSpawnRates[wave-1]; //sets spawn rate 
            cooldownAct = actCounter + cooldown;
            waveChange = true; 
        }
        spawnEnemy(random, cooldownAct); 
    }
    
    /**
     * Second spawnEnemy method. It takes in the spawn rate and the cooldownAct, all decided in the other spawnEnemy() method. 
     * It produces enemies based on spawn rate, and checks if the cooldown act has passed in order to create breaks between waves. 
     */
    public void spawnEnemy(int random, int cooldownAct) {
        //spawns enemy based on the spawn rate set in the spawnEnemy() method above 
        int genNumber = Greenfoot.getRandomNumber(random);
        //checks if the cooldownAct has been passed, so that enemies aren't 
        //spawned during the cooldown period. 
        if (genNumber < 4  && actCounter > cooldownAct) {
            if (wave < 3) { 
                for (int i = 0; i < genNumEnemies(genNumber); i++) {
                    addObject(new Enemy(1), 0, 0); //only 1 type of enemy for wave 1 and 2
                }
            } else if (wave < 7) {
                for (int i = 0; i < genNumEnemies(genNumber); i++) {
                    addObject(new Enemy(2), 0, 0); //Enemy(2) -> 2 types of enemy 
                }
            } else if (wave < 11){
                for (int i = 0; i < genNumEnemies(genNumber); i++) {
                    addObject(new Enemy(3), 0, 0); //Enemy(3) -> 3 types of enemy 
                }  
            }
        }
    }
    
    //This method decides the number of enemies that will be spawned in the given act. 
    public int genNumEnemies(int genNumber) {
        //Possible # of enemies that can be spawned in one act 
        int[] enemyNum1 = {1, 0, 0, 0};
        int[] enemyNum2 = {1, 1, 1, 0};
        int[] enemyNum3 = {1, 1, 1, 1};//adds more enemies as waves go on
        int[] enemyNum4 = {2, 1, 1, 0};
        
        int[][] enemiesWave = {enemyNum1, enemyNum2, enemyNum3, enemyNum4};
        
        if (wave < 3) {
            //index of the enemyNum1 array is decided by the genNumber from the spawnEnemy() method
            return enemiesWave[0][genNumber];
        } else if (wave < 5) {
            return enemiesWave[1][genNumber];
        } else if (wave < 8) {
            return enemiesWave[2][genNumber];
        } else {
            return enemiesWave[3][genNumber];
        }
    }
    
    /**
     * The method that spawns power ups. 
     * The amount of power-up types increases as the waves increase. 
     */
    public void spawnPowerUp() {
        int random = Greenfoot.getRandomNumber(1700); //power up has 1 in 1700 chance of spawning per act 
        if (random == 0) {
            if (wave > 1 && wave < 5) {
                addObject(new PowerUp(1), 0, 0); 
            } else if (wave < 6) {
                addObject(new PowerUp(2), 0, 0); //PowerUp(2) -> 2 types of power ups
            } else {
                addObject(new PowerUp(3), 0, 0);
            }
        }
    }
    
    //Builds 2 columns with 5 rocks (the "wall")
    //is called on at the start of the game, in the constructor 
    public void buildWalls() {
        int wallX = 470; 
        int wallY; 
        //walls on the left side
        for (int i = 1; i < 6; i++) {
            //adds rocks based on a given interval. 
            wallY = 800 - 120 * i + 55; 
            addObject(new Wall(), wallX, wallY); 
        }
        wallX = 730;
        //walls on the right side
        for (int i = 1; i < 6; i++) {
            wallY = 800 - 120 * i + 55; 
            addObject(new Wall(), wallX, wallY); 
        }
    }
    
    //changes to the ending world (losing screen)
    public void loseGame() {
        mainMusic.stop(); 
        bossMusic.stop(); 
        Greenfoot.setWorld(new EndingWorld(false)); 
    }
    
    //changes to the ending world (winning screen)
    public void winGame() {
        mainMusic.stop(); 
        bossMusic.stop(); 
        Greenfoot.setWorld(new EndingWorld(true)); 
    }
    
    //getters and setters
    public int getWave() {
        return wave; 
    }
    public void setWave(int newWave) {
        wave = newWave; 
    }
    public void setScore(int newScore)  {
        score = newScore; 
    }
    
    //addding points to the overall score
    public void addScore(int points) {
        score += points;
    }
    
    public void stopped() {
        mainMusic.stop(); 
        bossMusic.stop(); 
    }
}
