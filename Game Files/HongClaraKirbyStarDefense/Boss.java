import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The Boss is the big enemy that comes onto the world at the last wave of the game, wave 10. 
 * He takes the image of chef Kawasaki, and he remains on the right side of the world. 
 * Every now and then, he moves up and down, then shoots about 10 shots of 3 projectiles in each shot. 
 * The player must defeat him to win the game. 
 */
public class Boss extends Actor
{
    private int spawnCoolDown; 
    private int hp; 
    private int cooldown; //boss' counter to determine what state he's in: shooting, moving, still
    private int actCounter; 
    private HpBar h; 
    private int direction; 
    private int newBossY; 

    private GreenfootImage still, mouthOpen, dead; 
    public Boss () {
        //initiating graphics 
        still = new GreenfootImage ("chef.png"); 
        mouthOpen = new GreenfootImage ("chefMouthOpen.png"); 
        dead = new GreenfootImage ("chefDead.png"); 
        
        //adds hp bar
        h = new HpBar(500); 
        hp = 500;
        
        actCounter = 0; 
        cooldown = -1; 
        setImage(still); 
        
        //newBossY is where the boss SHOULD move 
        newBossY = Greenfoot.getRandomNumber(240) + 380; 
    }
    
    public void addedToWorld(World w) {
        //should spawn off the map 
        setLocation (1700, 490); 
        w.addObject(h, getX(), getY() + 50); 
    }
    
    public void act()
    {
        GameWorld g = (GameWorld) getWorld(); 
        //walks to x-position of 1100 to start 
        if (getX() > 1100) {
            move(-2); 
        }
        //updates the hp bar with  info on its hp
        h.updateHp(hp);
        //makes sure the hp bar always follows the boss
        h.setLocation(getX(), getY() + 50);
        
        //Every act, cooldown decreases by one. 
        //When it is reaches below -500, it is set tto 100 and initiates the shooting period. 
        //If it is between 0 and -500, the boss remains still and doesn't move. 
        if (cooldown < -500) {
            cooldown = 100; 
            setImage(mouthOpen); 
        } else if (cooldown < 0) {
            setImage(still);   
            if (getX() <= 1100) {                
                int d = (newBossY > getY())? 1:-1; //looks at the boss' current position and its wanted future position and decides whether he needs to move up or down
                if (getY() != newBossY) setLocation(getX(), getY() + d); 
            }
        } else if (cooldown > 0) {
            shoot(); 
            newBossY = Greenfoot.getRandomNumber(240) + 380; //generating new Y location 
        } 
        checkWin(); //checks if the game has been won
        cooldown--; 
        actCounter++; 
    }
    
    //changes boss hp based on damage taken 
    public void damageMe(int damage) {
        hp -= damage; 
    }
    
    //In this method, 3 objects of the Projectile class are created, and this boss will get the world to add them in order to "shoot". 
    public void shoot() {
        Projectile p = new Projectile(false, "enemy");
        Projectile q = new Projectile(false, "enemy");
        Projectile r = new Projectile(false, "enemy");
        
        //shoots projectiles every 10 acts 
        if (actCounter % 10 == 0) {
            getWorld().addObject(p, getX(), getY());
            getWorld().addObject(q, getX(), getY()-110); //spawns in the lane above
            getWorld().addObject(r, getX(), getY()+130); //spawns in lane below 
        }
    }
    
    //checks if the game has been won based on the boss' hp
    public void checkWin() {
        GameWorld g = (GameWorld)getWorld(); 
        if (hp <= 0) {
            setImage(dead); 
            Greenfoot.delay(100); //the game is delayed to have the player proccess the win 
            g.winGame(); //GameWorld method is called which brings about the win screen
        }
    }
}
