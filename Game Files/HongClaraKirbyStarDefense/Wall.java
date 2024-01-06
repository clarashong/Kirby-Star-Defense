import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The Wall is a class that encompasses all the rainbow ores in the game. The game ends when one of these ores have reached 0hp. 
 * The player must protect them to win the game. 
 * Each rock has five stages of images that display the state of health that it's in. 
 */
public class Wall extends Actor
{
    //Graphics and sound
    private GreenfootImage full;
    private GreenfootImage[] images;
    private GreenfootImage image; 
    
    //The hp bar that goes with each rock 
    private HpBar hpBar;
    
    private int wallHp; 
    private int boxesLost; //how many health boxes have been lost 
    public Wall() {
        genImageArray(); //generates all the possible image-stages of the rock 
        setImage(full); //gives the wall a "full" image when first created. 
        genWallState(); 
        wallHp = 2000; //2000 is also the max hp
        boxesLost = 0;
        hpBar = new HpBar(wallHp);
    }
    
    //immediately adds an hp bar when it's added to the world. 
    public void addedToWorld (World w){
        //places the bar a little to the right and bottom of each rock
        w.addObject(hpBar, getX() + 20,getY() + 50);
    }
    
    public void act() {
        GameWorld g = (GameWorld)getWorld(); 
        hpBar.updateHp(wallHp); //updates the hp bar with the its health 
        genWallState(); //generates the image that it should have 
        if (wallHp < 0) {
            //loses game if wall-hp is 0
            wallHp = 0; 
            hpBar.setAllBlack();  
            Greenfoot.delay(100); //delays the game before going to the lose screen 
            g.loseGame(); 
        }
    }
    
    //setter and getter methods 
    public void setMax() {
        wallHp = 2000; //puts the object back at its max hp  
    }
    public int getHp() {
        return wallHp; 
    }
    
    //updates wallHp based on damage caused by enemies 
    public void damageMe(int damage) {
        wallHp -= damage; 
    }
    
    //generates an array of images for each stage of health for each rock. 
    public void genImageArray() {
        images = new GreenfootImage[6];
        images[0] = new GreenfootImage("RockFull.png");
        for (int i = 1; i < 6; i++) {
            //I have numbered images that file name is in the form of "Rock#lost.png" this makes it easy to set the image array
            //The number represents the number of boxes lost. 
            images[i] = new GreenfootImage("Rock" +i+ "lost.png");
        }
    }
    
    //Sets the image of the object based on its health. 
    public void genWallState() {
        if (wallHp < 2000) {
            //Finding how many hp boxes have been lost. 400hp is a fifth of a rock's max hp. 
            boxesLost = 5 - (wallHp/400 + 1); //1 is added to avoid rounding errors
        }
        if (wallHp > 0) {
            image = images[boxesLost]; //if the wall has hp, the image will be updated. 
        } else {
            image = images[5]; //if the wall has no hp, the image will be automatically updated to the last image in the array 
        }
        setImage(image);
        if (wallHp == 2000) {
            setImage(images[0]); //sets the image to the "full" image 
        }
    }
}
