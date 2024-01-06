import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A projectile is what either the player or the enemy shoots. It can be used to damage enemies or damage the rocks. 
 */
public class Projectile extends Actor
{
    //class variables
    //graphics-related 
    private GreenfootImage yellowStar, fire, redStar;
    private GreenfootImage image; 
    private boolean facingRight; 
    
    private int speed, damage; 
    private static boolean isRed; //is the Red Star ability on? 
    private static int redsLeft; //ammo count for red stars (power ups) 
    private String shooter; //who is shooting the projectile
    
    public Projectile(boolean facingRight, String user) {
        speed = 8; 
        damage = 100; //does 100 hp damage
        shooter = user; 
        yellowStar = new GreenfootImage ("star.png");
        fire = new GreenfootImage("Fire.png"); 
        redStar = new GreenfootImage("redStar.png"); 
        if (user.equals("player")) {
            image = yellowStar; //sets yellow star for players
            if (isRed) {
                image = redStar; //sets red star if the power up is activated
            }
        } else {
            image = fire; //fire image is set for enemies 
        }
        if (redsLeft > 0) {
            damage = 1000; //red stars do more damage
        } else if (redsLeft <= 0){
            isRed = false; 
            damage = 100; 
        }
        setImage(image);
        this.facingRight = facingRight; 
        if (!facingRight) {
            speed *= -1; //speed is set to negative or positive based on the direction. 
        }
    }
    
    //When it's added to world, it is placed at the location of the player. 
    public void addedToWorld(World w) {
        if (shooter.equals("player")) {
            //But it being placed at the centre of the player doesn't look good graphics-wise, so it is shifted a bit to look more pleasing. 
            setLocation(getX() + (speed * 6), getY() + 15);
        } else {
            //For the emy, the x-position is only shift a bit so that the projectile doesn't look like it's coming out of the centre of the enemy. 
            setLocation(getX() + (speed * 6), getY());
        }
        redsLeft--; //with every shot, the amount of red star ammo decreases 
    }
    
    public void act() {
        move(speed); 
        
        //All the possible classes that an projectile can intersect with: 
        Enemy e = (Enemy)getOneIntersectingObject(Enemy.class);
        PowerUp p = (PowerUp)getOneIntersectingObject(PowerUp.class);
        Boss b = (Boss)getOneIntersectingObject(Boss.class); 
        Wall w; 
        
        GameWorld g = (GameWorld)getWorld(); 
        if (speed < 0) {
            //speed < 0, means that it's moving left
            w = (Wall)getOneObjectAtOffset(55, 0, Wall.class);
        } else {
            w = (Wall)getOneObjectAtOffset(-55, 0, Wall.class);
        }
        if (shooter.equals("player")) {
            //if the shooter is the player, the projectile can intersect with 3 things: an enemy, a power-up, or the boss
            if (e != null){
                g.addScore(1); //Score is added for every hit on an enemy 
                e.damageMe(damage); //the enemy is damaged 
                if (!isRed) {
                    g.removeObject(this); //The projectile will be removed if it isn't a red star. Red stars will move on throughout the lane until it reaches the edge. 
                }
                return; 
            } else if (p != null){
                //activates the power-ups ability when it is shot 
                p.usePower(); 
                if (!isRed) {
                    g.removeObject(this);
                }
                return;
            } else if (b != null) {
                b.damageMe(10); //damages the boss 
                g.removeObject(this);
                return; 
            }
        } else { 
            //if the shooter is an enemy
            if (w != null) {
                w.damageMe(40); //damages the wall 
                g.removeObject(this);
                return; 
            }
        }
        if (isAtEdge()){
            //regardless of shooter, any projectile will be removed if it's at the edge of the world. 
            getWorld().removeObject(this);
        }
    }
    
    //setter methods
    public void setIsRed(boolean newIsRed) {
        isRed = newIsRed; 
    }
    public void setRedsLeft(int newRedsLeft) {
        redsLeft = newRedsLeft; 
    }
}
